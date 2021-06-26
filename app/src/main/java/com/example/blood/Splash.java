package com.example.blood;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
public class Splash extends AppCompatActivity {
    SessionManager sessionManager;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager=new SessionManager(this);
        i= sessionManager.checkLogin();
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(i==0){
                        startActivity(new Intent(Splash.this,LoginActivity.class));
                    }
                    else if(i==1){
                        startActivity(new Intent(Splash.this,Giris.class));
                    }

                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    }