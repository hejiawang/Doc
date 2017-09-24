package com.wang.test.net.io.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Nio 服务端
 */
public class NioServer {

    private Selector selector;

    public void init() throws Exception {
        this.selector = Selector.open();    //创建选择器
        ServerSocketChannel channel = ServerSocketChannel.open();   //创建ServerSocketChannel
        channel.configureBlocking(false);   //设置为非阻塞
        ServerSocket serverSocket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(8080);    //绑定端口
        serverSocket.bind(address);
        channel.register(this.selector, SelectionKey.OP_ACCEPT);    //注册accept事件
    }

    public void start() throws Exception {
        while (true) {
            this.selector.select(); //此方法会阻塞，直到至少有一个已注册的事件发生
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();   //获取发生事件的SelectionKey对象集合
            while (true) {
                SelectionKey key = ite.next();
                ite.remove();   //从集合中移除即将处理的SelectionKey，避免重复处理
                if( key.isAcceptable() ){   //客户端请求链接事件
                    accept( key );
                } else if( key.isReadable() ){  //读事件
                    read(key);
                }
            }
        }
    }

    private void accept( SelectionKey key ) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();    //接收链接
        channel.configureBlocking(false);   //设置为非阻塞
        channel.register(this.selector, SelectionKey.OP_READ);  //为通道注册读事件
    }

    private void read( SelectionKey key ) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);  //创建读取的缓冲区
        channel.read(buffer);
        String request = new String( buffer.array() ).trim();
        System.out.println("客户端请求:" + request );
        ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
        channel.write(outBuffer);   //将消息回送给客户端
    }

    public static void main(String[] args) throws Exception {
        NioServer server = new NioServer();
        server.init();
        server.start();
    }
}
