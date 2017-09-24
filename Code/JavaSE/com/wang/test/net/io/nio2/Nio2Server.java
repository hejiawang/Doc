package com.wang.test.net.io.nio2;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * NIO2(AIO) 服务端
 */
public class Nio2Server {

    private ExecutorService taskExecutor;
    private AsynchronousServerSocketChannel serverChannel;

    class Worker implements Callable<String> {

        private CharBuffer charBuffer;
        private CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        private AsynchronousSocketChannel channel;

        public Worker(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public String call() throws Exception {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (channel.read( buffer ).get() != -1){ //读取请求
                buffer.flip();
                charBuffer = decoder.decode( buffer );
                String request = charBuffer.toString().trim();
                System.out.println("客户端请求:" + request);
                ByteBuffer outBuffer = ByteBuffer.wrap("请求收到:".getBytes());
                channel.write(outBuffer).get();
                if( buffer.hasRemaining() ){
                    buffer.compact();
                } else {
                    buffer.clear();
                }
            }
            channel.close();
            return "ok";
        }
    }

    public void init() throws  Exception {
        taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory()); //创建ExecutorService
        serverChannel = AsynchronousServerSocketChannel.open(); //创建AsynchronousServerSocketChannel
        if( serverChannel.isOpen() ){
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverChannel.bind(new InetSocketAddress( "127.0.0.1", 8080 ));
        } else {
            throw new RuntimeException("通道未打开!");
        }
    }

    public void start(){
        System.out.println("等待客户端请求……");
        while (true) {
            Future<AsynchronousSocketChannel> future = serverChannel.accept();
            try{
                AsynchronousSocketChannel channel = future.get();   //等待并得到请求通道
                taskExecutor.submit( new Worker(channel) ); //提交到线程池进行请求处理
            } catch (Exception ex){
                System.err.println(ex);
                System.err.println("服务器关闭!");
                taskExecutor.shutdown();
                while (!taskExecutor.isTerminated()){
                }
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Nio2Server server = new Nio2Server();
        server.init();
        server.start();
    }
}
