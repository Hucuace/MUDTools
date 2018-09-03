package com.hucuace.www.mudtools.ConnectClass;


import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;

import java.net.InetSocketAddress;

import java.net.Socket;

import java.net.SocketTimeoutException;


import android.annotation.SuppressLint;
import android.os.Handler;

import android.os.Looper;

import android.os.Message;




public class ClientThread implements Runnable {

    private Socket s;


    // 定义向UI线程发送消息的Handler对象

    Handler handler;

    // 定义接收UI线程的Handler对象

    //Handler revHandler;

    // 该线程处理Socket所对用的输入输出流

    BufferedReader br = null;

    OutputStream os = null;
    public Handler revHandler;

    InputStream is = null;
    byte[] buffer = new byte[1024];


    public ClientThread(Handler handler) {

        this.handler = handler;

    }


    @SuppressLint("HandlerLeak")
    @Override

    public void run() {

        s = new Socket();

        try {

            s.connect(new InetSocketAddress("hucuace.com", 13103), 5000);
            is= s.getInputStream();

            //br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();
            // 启动一条子线程来读取服务器相应的数据
            new Thread() {
                @Override
                public void run() {
                    //String content = null;
                    // 不断的读取Socket输入流的内容
                    try {
                        int readLen=0;
                        //readLen = is.read(buffer);
                        //while ((content = br.readLine()) != null) {
                        while((readLen= is.read(buffer)) >0){
                            // 每当读取到来自服务器的数据之后，发送的消息通知程序
                            // 界面显示该数据
                            byte[] data = new byte[readLen];
                            System.arraycopy(buffer, 0, data, 0, readLen);
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = data;
                            handler.sendMessage(msg);

                        }

                    } catch (IOException io) {

                        io.printStackTrace();

                    }

                }


            }.start();

            // 为当前线程初始化Looper

            Looper.prepare();

            // 创建revHandler对象

            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 接收到UI线程的中用户输入的数据
                    if (msg.what == 0x345) {
                        // 将用户在文本框输入的内容写入网络
                        try {
                            os.write((msg.obj.toString() + "\r\n")
                                    .getBytes("gbk"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            // 启动Looper
            Looper.loop();


        } catch (SocketTimeoutException e) {

            Message msg = new Message();
            msg.what = 0x123;

            msg.obj = "网络连接超时！";

            handler.sendMessage(msg);

        } catch (IOException io) {

            io.printStackTrace();

        }


    }

}