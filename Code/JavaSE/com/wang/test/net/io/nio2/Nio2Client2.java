package com.wang.test.net.io.nio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Nio2Client2 {

    class ClientCompletionHandler implements CompletionHandler<Void, Void> {

        private AsynchronousSocketChannel channel;
        private CharBuffer charBuffer = null;
        private CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        private BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));

        ClientCompletionHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Void result, Void attachment) {
            try {
                System.out.println("输入客户端请求:");
                String request = clientInput.readLine();
                channel.write(ByteBuffer.wrap(request.getBytes())).get();   //发送客户端请求
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);    //创建读取的缓冲区
                while ( channel.read( buffer ).get() != -1 ){   //读取服务端相应
                    buffer.flip();  //切换读模式，准备读数据
                    charBuffer = decoder.decode(buffer);
                    System.out.println(charBuffer.toString());
                    if( buffer.hasRemaining() ){
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }
                    request = clientInput.readLine();   //读取并发送下一次请求
                    channel.write(ByteBuffer.wrap(request.getBytes())).get();
                }
            } catch ( Exception ex ){
                System.err.println(ex);
            } finally {
                try{
                    channel.close();
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            throw new RuntimeException("链接服务器端失败!");
        }
    }

    public void start() throws Exception {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();   //创建异步Socket通道
        if( channel.isOpen() ){
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress("127.0.0.1", 8080),
                    null, new ClientCompletionHandler(channel));  //链接服务器，指定CompletionHandler
            while (true) Thread.sleep(5000);    //主线程等待
        } else {
            throw new RuntimeException("通道未打开!");
        }
    }

    public static void main(String[] args) throws Exception {
        Nio2Client2 client2 = new Nio2Client2();
        client2.start();
    }
}
