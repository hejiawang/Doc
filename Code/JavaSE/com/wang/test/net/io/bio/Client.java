package com.wang.test.net.io.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端
 */
public class Client {

    public static void main(String[] args) throws Exception {
        try (
            //向本机的8080端口发送请求
            Socket socket = new Socket("127.0.0.1", 8080);
            //根据标准输入构造BufferedReader对象
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
            //通过Socket得到输出流，构造PrintWriter对象
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            //通过Socket得到输入流，构造BufferedReader对象
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            //读取输入信息
            String input = clientInput.readLine();
            while ( !input.equals("exit") ){
                //将输入信息发送到服务器
                writer.print(input);
                //刷新输出流，使服务器端可以马上收到请求信息
                writer.flush();
                //读取服务器端返回信息
                System.out.println("服务器端相应为:" + reader.readLine());
                //读取下一条输入信息
                input = clientInput.readLine();
            }
        } catch ( Exception e ) {
            System.out.println(e);
        }
    }
}
