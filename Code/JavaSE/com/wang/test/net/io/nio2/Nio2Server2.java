package com.wang.test.net.io.nio2;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Nio2Server2 {

    private AsynchronousServerSocketChannel serverChannel;

    class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private AsynchronousServerSocketChannel serverChannel;
        private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        private CharBuffer charBuffer;
        private CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

        ServerCompletionHandler(AsynchronousServerSocketChannel serverChannel) {
            this.serverChannel = serverChannel;
        }

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
            serverChannel.accept(null, this);   //立即接收下一个请求
            try {
                while ( result.read(buffer).get() != -1 ) {
                    buffer.flip();
                    charBuffer = decoder.decode(buffer);
                    String request = charBuffer.toString().trim();
                    System.out.println("客户端请求:" + request);
                    ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
                    result.write(outBuffer).get();  //将相应输出到客户端
                    if (buffer.hasRemaining()){
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex);
            } finally {
                try {
                    result.close();
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            serverChannel.accept(null, this);   //立即接收下一个请求
            throw new RuntimeException("链接失败!");
        }
    }

    public void init() throws Exception {
        serverChannel = AsynchronousServerSocketChannel.open(); //创建AsynchronousServerSocketChannel
        if( serverChannel.isOpen() ){
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverChannel.bind( new InetSocketAddress("127.0.0.1", 8080));
        } else {
            throw new RuntimeException("通道未打开");
        }
    }

    public void start() throws Exception {
        System.out.println("等待客户端请求……");
        serverChannel.accept(null, new ServerCompletionHandler(serverChannel));

        while (true) Thread.sleep(5000);    //主线程等待
    }

    public static void main(String[] args) throws Exception {
        Nio2Server2 server2 = new Nio2Server2();
        server2.init();
        server2.start();
    }
}
