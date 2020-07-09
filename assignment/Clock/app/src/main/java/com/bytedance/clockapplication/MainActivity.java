package com.bytedance.clockapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bytedance.clockapplication.widget.Clock;
import com.bytedance.clockapplication.widget.Needle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private View mRootView;
    private Clock mClockView;
    private TextView textView;
    private Needle needleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root);
        mClockView = findViewById(R.id.clock);
        textView=findViewById(R.id.tv);
        textView.setTextColor(Color.WHITE);
        needleView=findViewById(R.id.needle);

        //控制时分秒针的刷新和时间TextView的刷新
        final Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String timeStr;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
                timeStr=simpleDateFormat.format(new Date());
                textView.setText(timeStr);
                needleView.invalidate();

            }
        };

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    while (true){
                        mHandler.sendEmptyMessage(0);
                        Thread.sleep(100);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();


    }


}
