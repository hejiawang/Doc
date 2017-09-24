package com.wang.test.net.thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 有对象实例的回调
 */
public class InstanceCallbackDigest implements  Runnable {

    private String filename;
    private InstanceCallbackDigestUserInterface callback;

    public InstanceCallbackDigest(String filename, InstanceCallbackDigestUserInterface callback) {
        this.filename = filename;
        this.callback = callback;
    }

    @Override
    public void run() {
        try{
            FileInputStream in = new FileInputStream(filename);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while (din.read() != -1) ;//读取整个文件
            din.close();
            byte[] digest = sha.digest();

            callback.receiveDigest(digest);    //调用回调函数
        }  catch (IOException ex){
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex){
            System.out.println(ex);
        }
    }
}
