package com.wang.test.net.thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 回调
 */
public class CallbackDigest implements Runnable {

    private String fileName;

    public CallbackDigest(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try{
            FileInputStream in = new FileInputStream(fileName);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while (din.read() != -1) ;//读取整个文件
            din.close();
            byte[] digest = sha.digest();
            CallbackDigestUserInterface.receiveDigest(digest, fileName);    //调用回调函数
        }  catch (IOException ex){
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex){
            System.out.println(ex);
        }
    }
}
