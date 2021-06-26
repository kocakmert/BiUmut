package com.example.blood;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
public class Giris extends AppCompatActivity {
    private ImageView profil_Photo,closepopup;
    private Dialog epicdia;
    private TextView txt_name;
    private Button btn_logout,btn_form,btn_map,btn_info,btn_mesaj,btn_goto_profile,btn_gundem,btn_faq,btn_yes;
    private SessionManager sessionManager;
    private String str_Name,str_id,str_photo ;
    private Intent intent;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        // ... Session işlemleri ...
        sessionManager=new SessionManager(this);
        final int deger =sessionManager.checkLogin();
        if(deger == 0){ // session yok
            intent=getIntent();
            str_Name=intent.getStringExtra("name");
            str_id=intent.getStringExtra("id");
            str_photo=intent.getStringExtra("photo");
        }
        else{
            HashMap<String,String> user=sessionManager.getUserDetail();
            str_Name=user.get(SessionManager.NAME);
            str_id=user.get(SessionManager.ID);
            str_photo=sessionManager.getPHOTO();
        }
        // ... bitiş ...
        // ... XML Ayarlamaları ...
        txt_name=(TextView)findViewById(R.id.name);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_form=(Button)findViewById(R.id.formrele);
        btn_map=(Button)findViewById(R.id.locationrela);
        btn_gundem=(Button) findViewById(R.id.formgundem);
        btn_info=(Button)findViewById(R.id.informationrela);
        btn_mesaj=(Button)findViewById(R.id.gamerela);
        btn_faq = (Button)findViewById(R.id.btn_faq);
        btn_goto_profile=(Button)findViewById(R.id.btn_goto_profile);
        profil_Photo=(ImageView) findViewById(R.id.logos);
        txt_name.setText(str_Name);
        epicdia=new Dialog(this);
        ResimYukle(str_photo);
        // ... bitiş ...
        // ...BUTONLARA TIKLANDIĞINDA RENK DEĞİŞTİRMESİ ...
        btn_form.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xFF4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_gundem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_mesaj.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff4F76C5, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_faq.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff1984b4, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        btn_goto_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0xff1984b4, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view=(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        // ...   BİTİŞ ...
        // ... Butonlara tıklama Eventlerinin Verilmesi ...
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });
        btn_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,formsecond.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                startActivity(intent);
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,info.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                startActivity(intent);
            }
        });
        btn_mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,mesaj.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                startActivity(intent);
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,maps.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                intent.putExtra("role","0");
                startActivity(intent);
            }
        });
        btn_goto_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,user_bilgi.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                startActivity(intent);
            }
        });
        btn_gundem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Giris.this,gundem.class);
                intent.putExtra("name",str_Name);
                intent.putExtra("id",str_id);
                intent.putExtra("photo",str_photo);
                intent.putExtra("session_active_value",String.valueOf(deger));
                intent.putExtra("role","0");
                startActivity(intent);
            }
        });
        btn_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // ...  Bitiş ...
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            showdia();
        }
        return super.onKeyDown(keyCode, event);
    }
    // --- Çıkış ekranında pop_up ---
    public void showdia(){
        epicdia.setContentView(R.layout.pop_upexit);
        closepopup= epicdia.findViewById(R.id.pop_ex);
        btn_yes= epicdia.findViewById(R.id.button_yes);
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicdia.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });
        epicdia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicdia.show();
    }
    // --- Resim' i yüklemek için api ---
    private void ResimYukle(String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(profil_Photo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

}