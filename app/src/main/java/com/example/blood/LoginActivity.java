package com.example.blood;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private String id,photo;
    private final String[] capcha={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","R","S","T","U","V","Y","Z","0","1","2","3","4","5","6","7","8","9"};
    private int number;
    private TextView link_regist,link_forget,cc_capcha;
    private CheckBox ch;
    private EditText editText_name,editText_password,entry_capcha;
    private Button btn_login;
    private ProgressBar loading;
    private String capcha_text="";
    private SessionManager sessionManager;
    private Typeface capcha_font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager= new SessionManager(this);
        // ... Capcha Kodunun font ayarları ...
        capcha_font= Typeface.createFromAsset(getAssets(), "fonts/font11.ttf");
        cc_capcha=(TextView) findViewById(R.id.capcha);
        cc_capcha.setTypeface(capcha_font);
        // ...  bitiş ...
        // ... Capcha kodunun üretilmesi...
        Random rnd=new Random();
        for (int i=0;i<5;i++){
            number = rnd.nextInt(capcha.length);
            capcha_text+=capcha[number];
        }
        cc_capcha.setText(capcha_text);
        // ... bitiş ...
        // ... XML Ayarlamaları ...
        loading=(ProgressBar)findViewById(R.id.loading);
        ch=(CheckBox)findViewById(R.id.ch);
        entry_capcha=(EditText) findViewById(R.id.entry_capcha);
        editText_name=(EditText) findViewById(R.id.name);
        editText_password=(EditText) findViewById(R.id.password);
        btn_login=(Button)findViewById(R.id.btn_login);
        link_regist=(TextView)findViewById(R.id.link_regist);
        link_forget=(TextView) findViewById(R.id.link_forget);
        // ...  bitiş ...
        // adapter ve servis oluşturma

        // ... Görünüm ayarlamaları ...
        loading.setVisibility(View.INVISIBLE);
        btn_login.setVisibility(View.VISIBLE);
        // ... bitiş ...
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mName=editText_name.getText().toString().trim();
                final String mPassword=editText_password.getText().toString().trim();
                final String mcapcha=entry_capcha.getText().toString().trim();
                if(!mName.isEmpty() && !mPassword.isEmpty() && !mcapcha.isEmpty()){
                    if(mcapcha.contains(capcha_text)){
                        loading.setVisibility(View.VISIBLE);
                        btn_login.setVisibility(View.GONE);
                        /*Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://alpersahinoner.com/kocak/blood/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        RetroFitApi retrofitAPI = retrofit.create(RetroFitApi.class);

                        Call<User> call = retrofitAPI.login(mName,mPassword);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> cell, retrofit2.Response<User> response) {
                                String s= response.toString();
                                if(s.isEmpty()){
                                    loading.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    loading.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("Login",t.toString());
                                Toast.makeText(LoginActivity.this, call.toString(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });*/
                        Login(mName,mPassword);
                    }
                    else{
                        entry_capcha.setError("Karakterler Eşleşmiyor");
                        capcha_text="";
                        entry_capcha.setText("");
                        set_capcha();
                    }
                }else{
                    capcha_text="";
                    editText_name.setError("Lütfen Geçerli Bir Kullanıcı Adı Giriniz.");
                    editText_password.setError("Lütfen Geçerli Bir Şifre Giriniz.");
                    entry_capcha.setError("Lütfen Doğrulamaya Dikkat Edin");
                    entry_capcha.setText("");
                    set_capcha();
                }
            }
        });
        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
        link_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,password_forget.class));
            }
        });
    }
    private void Login(final String name, final String password){
        btn_login.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"login.php";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final String respons_substring=response.substring(0,1);
                            if(respons_substring.contains("0")){ // Role 0 kullanıcı girişi...
                                final String[] str_split = response.substring(1).split("-");
                                id = str_split[0]; // id
                                photo= str_split[1]; // string photo
                                if(ch.isChecked()){
                                    sessionManager.createSession(name,id,photo); //Session açıldı...
                                    Intent intent=new Intent(LoginActivity.this,Giris.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent=new Intent(LoginActivity.this,Giris.class);
                                    intent.putExtra("name",name);
                                    intent.putExtra("id",id);
                                    intent.putExtra("photo",photo);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else if (respons_substring.equals("1")){ // Role 1 admin girişi...
                                Toast.makeText(LoginActivity.this, "Admin Girişi Yapıldı...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,AdminPanel.class));
                            }
                            else{
                                loading.setVisibility(View.INVISIBLE);
                                btn_login.setVisibility(View.VISIBLE);
                                editText_name.setError("Geçerli Bir Hesap Giriniz");
                                editText_password.setError("Geçerli Bir Hesap Giriniz");
                                entry_capcha.setText("");
                                set_capcha();
                            }

                        }catch (Exception e) {
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            loading.setVisibility(View.INVISIBLE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "İşlemi Tekrar Deneyiniz."+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.INVISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "İşlemi Tekrar DeneyenizVolley"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("name",name);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    private void set_capcha(){
        capcha_text="";
        Random rnd=new Random();
        for (int i=0;i<5;i++){
            number = rnd.nextInt(capcha.length);
            capcha_text+=capcha[number];
        }
        cc_capcha.setText(capcha_text);
    }
}