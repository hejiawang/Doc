package com.wang.test.net.io.nio2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * NIO2(AIO) 客户端
 */
public class Nio2Client {

    private AsynchronousSocketChannel channel;
    private CharBuffer charBuffer;
    private CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
    private BufferedReader clientInput = new BufferedReader(new InputStreamReader( System.in ));

    public void init() throws Exception {
        channel = AsynchronousSocketChannel.open(); //创建异步Socket通道
        if( channel.isOpen() ){
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            Void connect = channel.connect( new InetSocketAddress("127.0.0.1", 8080)).get();    //链接服务器
            if (connect != null){
                throw new RuntimeException("链接服务器失败");
            }
        } else {
            throw new RuntimeException("通道未打开!!!");
        }
    }

    public void start() throws Exception {
        System.out.println("输入客户端请求:");
        String request = clientInput.readLine();
        channel.write(ByteBuffer.wrap(request.getBytes())).get();   //发送客户端请求
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);    //创建读取的缓存区，直接缓冲区，allocateDirect方法可以一次性分配capacity大小的连续字节空间
        while ( channel.read(buffer).get() != -1 ){ //读取服务端相应
            buffer.flip();  //反转
            charBuffer = decoder.decode(buffer);
            String response = charBuffer.toString().trim();
            System.out.println("服务端响应:" + response);
            if( buffer.hasRemaining() ){
                buffer.compact();   //压缩
            } else {
                buffer.clear(); //清除
            }
            request = clientInput.readLine();   //读取并发送下一次请求
            channel.write( ByteBuffer.wrap( request.getBytes() ) ).get(); //wrap()方法：包装，将byte数组引用为缓存区数组，如果缓存区内容变更，byte数组也相应变更
        }
    }

    public static void main(String[] args) throws Exception {
        Nio2Client clinet = new Nio2Client();
        clinet.init();
        clinet.start();
    }
}
