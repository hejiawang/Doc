package com.wang.test.net.thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用存储方法返回结果的线程
 */
public class ReturnDigest extends Thread {

    private String fileName;
    private byte[] digest;

    public ReturnDigest( String fileName ){
        this.fileName = fileName;
    }

    public byte[] getDigest(){
        return digest;
    }

    @Override
    public void run() {
        try{
            FileInputStream in = new FileInputStream(fileName);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while (din.read() != -1) ;//读取整个文件
            din.close();
            digest = sha.digest();
        }  catch (IOException ex){
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex){
            System.out.println(ex);
        }
    }
}
