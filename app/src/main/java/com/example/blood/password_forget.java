package com.example.blood;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.regex.Pattern;
public class password_forget extends AppCompatActivity {
    private EditText editTxt_name,editTxt_email,editTxt_password,editTxt_password_v;
    private TextView link_main_menu;
    private Button btn_dogrula,btn_degistir;
    private ProgressBar loading;
    private static final Pattern PASWORD_PATTERN =
            Pattern.compile("^"+
                    "(?=.*[0-9])"+
                    "(?=.*[a-z])"+
                    "(?=.*[A-Z])"+
                    "(?=\\S+$)"+
                    ".{6,}"+
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        // ... XML Ayarlamaları ...
        loading=(ProgressBar) findViewById(R.id.loading);
        link_main_menu=(TextView)findViewById(R.id.giris_yap);
        editTxt_name=(EditText) findViewById(R.id.name);
        editTxt_email=(EditText)findViewById(R.id.email);
        editTxt_password=(EditText)findViewById(R.id.password);
        editTxt_password_v=(EditText)findViewById(R.id.c_password);
        btn_dogrula=(Button) findViewById(R.id.btn_onay);
        btn_degistir=(Button)findViewById(R.id.btn_degis);
        // ... bitiş ...
        // ... Görünürlük belirlemeleri ...
        editTxt_password.setVisibility(View.GONE);
        editTxt_password_v.setVisibility(View.GONE);
        btn_degistir.setVisibility(View.GONE);
        // ... bitiş ...
        btn_dogrula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String mname=editTxt_name.getText().toString().trim();
                final String mEmail=editTxt_email.getText().toString().trim();
                if(!mname.isEmpty() &&!mEmail.isEmpty()){
                    dogrula(mname,mEmail);
                }
                else{
                    editTxt_name.setError("Lütfen KullanıcıAdı Giriniz.");
                    editTxt_email.setError("Lütfen Email Giriniz.");
                }
            }
        });
        btn_degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String mname=editTxt_name.getText().toString().trim();
                final String mEmail=editTxt_email.getText().toString().trim();
                final String c_password=editTxt_password.getText().toString().trim();
                final String cm_password=editTxt_password_v.getText().toString().trim();
                if(!c_password.isEmpty() && !cm_password.isEmpty()){
                    if(PASWORD_PATTERN.matcher(c_password).matches()){
                        if(c_password.contains(cm_password)){
                            kayıt(mname,mEmail,c_password);
                        }
                        else{
                            editTxt_password.setError("Şifre Eşleşmiyor.");
                        }
                    }
                    else{
                        editTxt_password.setError("Zayıf Parola:En az 1 Sayı, 1 Büyük harf,1Küçük harf ve 6 Karekter Olmasına Dikkat Edin. ");
                    }
                }
            }
        });
        link_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(password_forget.this,LoginActivity.class));finish();
            }
        });
    }
    private  void dogrula(final  String name,final String email){
        loading.setVisibility(View.VISIBLE);
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION +"password_forget.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST,FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("0")){
                                loading.setVisibility(View.GONE);
                                btn_dogrula.setVisibility(View.INVISIBLE);
                                Toast.makeText(password_forget.this, "Kişi Bulundu", Toast.LENGTH_SHORT).show();
                                editTxt_password.setVisibility(View.VISIBLE);
                                editTxt_password_v.setVisibility(View.VISIBLE);
                                btn_degistir.setVisibility(View.VISIBLE);
                            }
                            else{
                                loading.setVisibility(View.INVISIBLE);
                                editTxt_name.setError("Geçerli Bir Kullanıcı Adı Giriniz!");
                                editTxt_email.setError("Geçerli Bir Email Giriniz!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(password_forget.this, "HATA!!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(password_forget.this, "HATA!!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private  void kayıt(final  String name,final  String email,final  String password){
        loading.setVisibility(View.VISIBLE);
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION +"password_new.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST,FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("0")){
                                loading.setVisibility(View.GONE);
                                startActivity(new Intent(password_forget.this,LoginActivity.class));
                            }
                            else{
                                loading.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(password_forget.this, "HATA!!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(password_forget.this, "HATA!!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
