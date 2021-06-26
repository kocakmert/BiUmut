package com.example.blood;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity {
    private final String[] capcha={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","R","S","T","U","V","Y","Z","0","1","2","3","4","5","6","7","8","9"};
    private int number;
    private String capcha_text="";
    private Context cn=this;
    private TextView link_main_menu;
    private MailSend mailSend;
    private EditText editText_email,editText_password ,c_password,edit_onaykodu,editText_name;
    private Button btn_onay,btn_regist;
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
        setContentView(R.layout.activity_main);
        // ... Onay Kodu Üretilmesi ...
        Random rnd=new Random();
        for (int i=0;i<4;i++){
            number = rnd.nextInt(31);
            capcha_text+=capcha[number];
        }
        //    ... bitiş  ...
        // ... XML Ayarlamaları ...
        editText_name=(EditText)findViewById(R.id.name);
        editText_email=(EditText)findViewById(R.id.email);
        editText_password=(EditText)findViewById(R.id.password);
        c_password=(EditText)findViewById(R.id.c_password);
        edit_onaykodu=(EditText)findViewById(R.id.onay_kodu);
        btn_onay=(Button)findViewById(R.id.btn_onay);
        btn_regist=(Button) findViewById(R.id.btn_register);
        link_main_menu=(TextView)findViewById(R.id.giris_yap);
        edit_onaykodu.setVisibility(View.INVISIBLE);
        loading=findViewById(R.id.loading);
        // ... bitiş ...

        // ... Görünürlük ayarlamaları ...
        loading.setVisibility(View.INVISIBLE);
        btn_onay.setVisibility(View.INVISIBLE);
        // ... bitiş ...
        btn_onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ONAY Request inden gelen bilgiler doğrultusunda btn_onay visible olup e postaya gönderilen kod girilip kayıt gerçekleştiril.
                final String mEmail=editText_email.getText().toString().trim();
                final String mPassword=editText_password.getText().toString().trim();
                final String mname=editText_name.getText().toString().trim();
                String mcapcha=edit_onaykodu.getText().toString().trim();
                if(mcapcha.contains(capcha_text)&& !mcapcha.isEmpty()){
                    loading.setVisibility(View.VISIBLE);
                    Regist(mname,mEmail,mPassword);
                }
                else{
                    edit_onaykodu.setError("Boş Giriş");
                }
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail=editText_email.getText().toString().trim();
                final String mPassword=editText_password.getText().toString().trim();
                final String mname=editText_name.getText().toString().trim();
                final String c_mPassword=c_password.getText().toString().trim();
                if(!mEmail.isEmpty() && !mPassword.isEmpty() && !mname.isEmpty()){
                    if(PASWORD_PATTERN.matcher(mPassword).matches()){
                        if (Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                            if(mPassword.contains(c_mPassword)){
                                loading.setVisibility(View.VISIBLE);
                                btn_regist.setVisibility(View.INVISIBLE);
                                mailSend= new MailSend(MainActivity.this, mEmail, "Doğrulama Kodu", capcha_text);
                                Onay(capcha_text);
                                //Regist(mname,mEmail,mPassword);
                            }
                            else{
                                editText_password.setError("Şifre Eşleşmiyor.");
                            }
                        }
                        else{
                            editText_email.setError("Geçerli Bir Mail Adresi Girin");
                        }
                    }
                    else{
                        editText_password.setError("Zayıf Parola:En az 1 Sayı, 1 Büyük harf,1Küçük harf ve 6 Karekter Olmasına Dikkat Edin. ");
                    }
                }else{
                    editText_email.setError("Lütfen Email Giriniz.");
                    editText_password.setError("Lütfen Şifre Giriniz.");
                    editText_name.setError("Kullanıcı Adı Giriniz.");
                }
            }
        });
        link_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));finish();
            }
        });
    }
    private void Regist(final String name,final  String email,final String password){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION +"register.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("0")){
                                btn_regist.setVisibility(View.INVISIBLE);
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(MainActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                           }
                        } catch (Exception e) {
                            e.printStackTrace();
                            btn_regist.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Bazı şeyler Düzgün gitmedi tekrar deneyiniz.!!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn_regist.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Bazı şeyler Düzgün gitmedi tekrar deneyiniz."+error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    // ... E-postanın onaylanması ....
    private void Onay(final String capcha){
        //Toast.makeText(this, capcha, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert=new AlertDialog.Builder(cn);
        alert.setTitle("Email Adresinize Gönderdiğimiz Onay Kodunu Girin?")
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mailSend.execute();
                        loading.setVisibility(View.INVISIBLE);
                        btn_regist.setVisibility(View.INVISIBLE);
                        btn_onay.setVisibility(View.VISIBLE);
                        edit_onaykodu.setVisibility(View.VISIBLE);
                        dialog.cancel();
                    }
                }).setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        }).create().show();
    }
}