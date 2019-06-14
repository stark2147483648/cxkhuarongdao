package com.example.administrator.huarongdao;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    Button Qz[] = new Button[10];
    //棋盘布局
    int BG[][] = new int[5][4];
    ConstraintLayout mLayout;
    AbsoluteLayout absLayout;
    TextView txt1;
    TextView TextOfTime;
    //屏幕宽度
    float SW;
    float x1, x2, y1, y2;
    int Step=0;
    int level=1;
    //声音开关
    boolean voiceSwitch=true;
    Intent musicIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Qz[0] = (Button) findViewById(R.id.Qz1);
        Qz[1] = (Button) findViewById(R.id.Qz2);
        Qz[2] = (Button) findViewById(R.id.Qz3);
        Qz[3] = (Button) findViewById(R.id.Qz4);
        Qz[4] = (Button) findViewById(R.id.Qz5);
        Qz[5] = (Button) findViewById(R.id.Qz6);
        Qz[6] = (Button) findViewById(R.id.Qz7);
        Qz[7] = (Button) findViewById(R.id.Qz8);
        Qz[8] = (Button) findViewById(R.id.Qz9);
        Qz[9] = (Button) findViewById(R.id.Qz10);

        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        txt1 = (TextView) findViewById(R.id.step);
        TextOfTime = (TextView) findViewById(R.id.time);

        absLayout = (AbsoluteLayout) findViewById(R.id.Qp);

        for (int i = 0; i < 10; i++)
            Qz[i].setOnTouchListener(new mTouch());

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
                BG[i][j] = 1;
        BG[4][1] = 0;
        BG[4][2] = 0;

        //计步
        boolean post = txt1.post(new Runnable() {
            @Override
            public void run() {
                txt1.setText("步数：0步");
                SW = mLayout.getWidth();
                init(level);
            }
        });

        musicIntent = new Intent(MainActivity.this,MusicServer.class);
        if (voiceSwitch)
            startService(musicIntent);

    }

    protected void onStop(){
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        stopService(intent);
        super.onStop();
    }


    public void setLevel1(View view) {
        this.level = 1;
        init(level);
        Step=0;
        BG[4][1] = 0;
        BG[4][2] = 0;
        txt1.setText("步数：0步");
    }


    public void switchVoice(View view) {
        if (voiceSwitch==true) {
            voiceSwitch=false;
            stopService(musicIntent);
        }
        else {
            voiceSwitch=true;
            startService(musicIntent);
        }
    }

    public void reSet(View view) {
        if (level==1) setLevel1(view);
    }



    public class mTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            int type; // 1 bing    2  zhangfei  3 guanyu 4 caocao
            int r, c;
            if (v.getWidth() == v.getHeight()) {
                if (v.getHeight() > 300)
                    type = 4;
                else
                    type = 1;

            } else {
                if (v.getHeight() > v.getWidth())
                    type = 2;
                else
                    type = 3;
            }

            r = (int) (v.getY() / 270f);
            c = (int) (v.getX() / 270f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = event.getX();
                y1 = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                x2 = event.getX();
                y2 = event.getY();

                if (y1 - y2 > 30  &&  y1-y2>abs(x1-x2))
                {

                    switch (type) {

                        case 1:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r + 1][c] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;

                    }

                } else if (y2 - y1 > 30  &&y2-y1>abs(x1-x2)) //向下滑
                {
                    switch (type) {
                        case 1:
                            if (r < 4 && BG[r + 1][c] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 1][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (r < 3 && BG[r + 2][c] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 2][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }

                            break;
                        case 3:
                            if (r < 4 && BG[r + 1][c] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 1][c] = BG[r + 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (r < 3 && BG[r + 2][c] == 0 && BG[r + 2][c + 1] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 2][c] = BG[r + 2][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("蔡徐坤成功逃脱");
                                    builder.setMessage("共用：        "+Step+"步");
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    builder.show();
                                }
                            }
                            break;
                    }
                } else if (x1 - x2 > 30  &&x1-x2>abs(y1-y2)) //向左滑
                {
                    switch (type) {
                        case 1:
                            if (c > 0 && BG[r][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r + 1][c - 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (c > 0 & BG[r][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = BG[r + 1][c - 1] = 1;
                                BG[r][c + 1] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    txt1.setText("你赢了！共用" + Step + "步");
                                }
                            }
                            break;
                    }
                } else if (x2 - x1 > 30  &&x2-x1>abs(y1-y2)) //向右滑
                {
                    switch (type) {
                        case 1:
                            if (c < 3 && BG[r][c + 1] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (c < 3 & BG[r][c + 1] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r + 1][c + 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (c < 2 && BG[r][c + 2] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 2] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (c < 2 && BG[r][c + 2] == 0 && BG[r + 1][c + 2] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 2] = BG[r + 1][c + 2] = 1;
                                BG[r][c] = BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    txt1.setText("你赢了！共用" + Step + "步");
                                }
                            }
                            break;
                    }
                }
                if (voiceSwitch==true){
                    mMediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.voice);
                    mMediaPlayer.start();
                    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                        }
                    };
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
            return true;
        }
    }


    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    void SetSize(Button v, int w, int h) {
        v.setWidth(w*(int)SW/4);
        v.setHeight(h * dip2px(getApplicationContext(), SW / 4));
    }

    void SetPois(View v, int r, int c) {
        v.setX(c * SW / 4f);
        v.setY(r * SW / 4f);
    }

    void init(int level) {
        SetSize(Qz[0], 1, 1);
        SetSize(Qz[1], 1, 1);
        SetSize(Qz[2], 1, 1);
        SetSize(Qz[3], 1, 1);
        SetSize(Qz[4], 1, 2);
        SetSize(Qz[5], 1, 2);
        SetSize(Qz[6], 1, 2);
        SetSize(Qz[7], 1, 2);
        SetSize(Qz[8], 2, 1);
        SetSize(Qz[9], 2, 2);
        switch (level) {
            case 1:
                SetPois(Qz[0], 4, 0);
                SetPois(Qz[1], 3, 1);
                SetPois(Qz[2], 3, 2);
                SetPois(Qz[3], 4, 3);
                SetPois(Qz[4], 0, 0);
                SetPois(Qz[5], 0, 3);
                SetPois(Qz[6], 2, 0);
                SetPois(Qz[7], 2, 3);
                SetPois(Qz[8], 2, 1);
                SetPois(Qz[9], 0, 1);
                break;


        }
    }
}
