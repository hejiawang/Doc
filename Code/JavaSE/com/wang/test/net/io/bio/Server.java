package com.wang.test.net.io.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器端
 */
public class Server {

    public static void main(String[] args) {
        try(
            //创建ServerSocket监听端口8080
            ServerSocket server = new ServerSocket(8080);
            //等待客户端请求
            Socket socket = server.accept();
            //根据标准输入构造BufferedReader对象
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
            //通过Socket对象得到输出流，BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //通过Socket对象得到输出流，并构造PrintWrite对象
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
        ){
            //读取客户端请求
            System.out.println("客户端请求:" + reader.readLine());
            //输入服务端相应
            String input = serverInput.readLine();
            //如果输入内容为"exit",则退出
            while (!input.equals("exit")){
                writer.print(input);
                //向客户端输出该字符串
                writer.flush();
                //读取客户端请求
                System.out.println("客户端请求:" + reader.readLine());
                //输入服务器相应
                input = serverInput.readLine();
            }
        } catch ( Exception e ){
            System.out.println(e);
        }
    }
}
