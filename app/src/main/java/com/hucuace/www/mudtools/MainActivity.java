package com.hucuace.www.mudtools;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hucuace.www.mudtools.ConnectClass.ClientThread;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button btn;
    Handler handler;
    ClientThread clientThread;


    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ;

        textView = (TextView) findViewById(R.id.txtview1);
        //textView.setBackgroundColor(Color.BLACK);
        editText = (EditText) findViewById(R.id.edtxt1);
        btn = (Button) findViewById(R.id.btn1);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自子线程
                if (msg.what == 0x123) {
                    // 将读取的内容追加显示在文本框中

                    try {
                        //String str1 = msg.obj.toString();
                        ByteArrayOutputStream bo =new ByteArrayOutputStream();
                        ObjectOutputStream oo = new ObjectOutputStream(bo);
                        oo.writeObject(msg.obj);
                        byte[] bytes = bo.toByteArray();
                        String str2 = new String(bytes,"GBK");
                        //String strU= URLDecoder.decode(str2,"UTF-8");
                        String strU=new String(str2.getBytes(),"UTF-8");
                                //String strU2 = strU.replace("\r\n","<BR>");
                        textView.append(Html.fromHtml(AsciToHtml(strU)));
                        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                        int offset = textView.getLineCount()*textView.getLineHeight();
                        if(offset>(textView.getHeight()-textView.getLineHeight()-20)){
                            textView.scrollTo(0,offset-textView.getHeight()+textView.getLineHeight()+20);
                        };
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        clientThread = new ClientThread(handler);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 当用户按下按钮之后，将用户输入的数据封装成Message
                    // 然后发送给子线程Handler
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj = editText.getText().toString();
                    clientThread.revHandler.sendMessage(msg);
                    editText.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    public String AsciToHtml(String s){
        String s2 = null;
        s2 =s.substring(25);
        s2=s2.replace("<","&lt;");
        s2=s2.replace(">","&gt;");
        s2=s2.replace("\r\n","<BR>");


        //字体色
        s2=s2.replace("\u001B[30m","<font color=\"Gray\">");
        s2=s2.replace("\u001B[31m","<font color=\"Red\">");
        s2=s2.replace("\u001B[32m","<font color=\"Green\">");
        s2=s2.replace("\u001B[33m","<font color=\"Yellow\">");
        s2=s2.replace("\u001B[34m","<font color=\"Blue\">");
        s2=s2.replace("\u001B[35m","<font color=\"Magenta\">");
        s2=s2.replace("\u001B[36m","<font color=\"Cyan\">");
        s2=s2.replace("\u001B[37m","<font color=\"White\">");

        s2=s2.replace("\u001B[1;30m","<font color=\"Gray\">");
        s2=s2.replace("\u001B[1;31m","<font color=\"Red\">");
        s2=s2.replace("\u001B[1;32m","<font color=\"Green\">");
        s2=s2.replace("\u001B[1;33m","<font color=\"Yellow\">");
        s2=s2.replace("\u001B[1;34m","<font color=\"Blue\">");
        s2=s2.replace("\u001B[1;35m","<font color=\"Magenta\">");
        s2=s2.replace("\u001B[1;36m","<font color=\"Cyan\">");
        s2=s2.replace("\u001B[1;37m","<font color=\"White\">");

        s2=s2.replace("\u001B[2;37;0m","</font>");
        s2=s2.replace("\u001B[0;37;0m","</font>");
        s2=s2.replace("\u001B[m","</font>");

        s2=s2.replace("\u001B[41;1m","");
        s2=s2.replace("\u001B[42;1m","");
        s2=s2.replace("\u001B[43;1m","");
        s2=s2.replace("\u001B[44;1m","");
        s2=s2.replace("\u001B[45;1m","");
        s2=s2.replace("\u001B[46;1m","");
        s2=s2.replace("\u001B[47;1m","");

        s2=s2.replace("\u001B[40m","");
        s2=s2.replace("\u001B[41m","");
        s2=s2.replace("\u001B[42m","");
        s2=s2.replace("\u001B[43m","");
        s2=s2.replace("\u001B[44m","");
        s2=s2.replace("\u001B[45m","");
        s2=s2.replace("\u001B[46m","");

        s2=s2.replace("\u001B[0m","");
        s2=s2.replace("\u001B[1m","");
        s2=s2.replace("\u001B[4m","");
        s2=s2.replace("\u001B[5m","");
        s2=s2.replace("\u001B[7m","");

        s2=s2.replace("\u001B[3;33m","");
        s2=s2.replace("\u001B[4;53m","");
        s2=s2.replace("\u001B[7m","");
        s2=s2.replace("\u001B[1A\u001B[256D\u001B[K","");

        return s2;

    }


}