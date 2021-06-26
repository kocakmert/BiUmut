package com.example.blood;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class AdminPanel extends AppCompatActivity {
    Context context=this;
    private  Button btn_loga,btn_kayıt,btn_olustur,btn_search,btn_sil,btn_istek_gor,btn_istekler,btn_mesaj,btn_gundem;
    EditText ed_user;
    Dialog epic_search;
    ImageView closepopup;
    private TextView us_name,email;
    String bos="";
    String bos1="";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        btn_gundem=findViewById(R.id.gundem);
        btn_mesaj=findViewById(R.id.btn_mesajlasma);
        btn_loga=findViewById(R.id.btn_loga);
        btn_kayıt=findViewById(R.id.kayıtlar);
        btn_olustur=findViewById(R.id.btn_olustur);
        btn_istek_gor=findViewById(R.id.istekler);
        epic_search=new Dialog(this);
        //  --- Buton Tıklama Efect ---
        btn_istek_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this,istekleri_gorme.class));
            }
        });
        btn_kayıt.setOnTouchListener(new View.OnTouchListener() {
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
        btn_olustur.setOnTouchListener(new View.OnTouchListener() {
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
        //  --- Bitiş ---

        // --- Butonlara Tıklandığında ---
        btn_loga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this,LoginActivity.class));
            }
        });
        btn_kayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_up_admin_islemler(bos1,bos);   // ...  admin kullanıcı silme ve tüm kan isteklerini görebilmesi için ...
            }
        });
        btn_olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPanel.this,maps.class);
                intent.putExtra("id","1");
                intent.putExtra("role","1");
                startActivity(intent);
            }
        });
        btn_mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this,admin_mesaj.class));
            }
        });
        btn_gundem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPanel.this,gundem.class);
                intent.putExtra("name","admin");
                intent.putExtra("role","1");
                startActivity(intent);
            }
        });
        // --- Bitiş ---
    }
    // --- Geri Tuşuna Basıldığında ---
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder alert=new AlertDialog.Builder(context);
            alert.setTitle("Çıkış Yapmak İstiyor Musunuz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            startActivity(new Intent(AdminPanel.this,LoginActivity.class));
                        }
                    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            }).create().show();
        }
        return super.onKeyDown(keyCode, event);
    }
    @SuppressLint("ClickableViewAccessibility")
    // --- Admin İşlemleri ---
    private  void pop_up_admin_islemler(final String bos1, String bos){
        epic_search.setContentView(R.layout.pop_up_userdetails);
        btn_search=epic_search.findViewById(R.id.btn_search);
        btn_sil=epic_search.findViewById(R.id.btn_sil);
        btn_istekler=epic_search.findViewById(R.id.btn_istekler);
        closepopup=epic_search.findViewById(R.id.pop_ex);
        ed_user=epic_search.findViewById(R.id.user_name); //aranancak olan kullanıcı!!
        us_name=epic_search.findViewById(R.id.name);
        email=epic_search.findViewById(R.id.email);
        us_name.setText(bos1);
        email.setText(bos);
        btn_sil.setOnTouchListener(new View.OnTouchListener() {
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
                        view.getBackground().setColorFilter(0xCC3131, PorterDuff.Mode.SRC_ATOP);
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
        btn_istekler.setOnTouchListener(new View.OnTouchListener() {
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
                        view.getBackground().setColorFilter(0xCC3131, PorterDuff.Mode.SRC_ATOP);
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
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epic_search.dismiss();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s_name=ed_user.getText().toString().trim();
                if(!s_name.isEmpty()){
                    get_user(s_name);
                }
                else{
                    ed_user.setError("Kullanıcı Adı Giriniz");
                }
            }
        });
        btn_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s_name=us_name.getText().toString().trim();
                if(!s_name.isEmpty()){
                    sil_user(s_name);
                }
                else{
                    ed_user.setError("Kullanıcı Adı Giriniz");
                }
            }
        });
        btn_istekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminPanel.this,kan_ihtiyaci_istekleri.class);
                startActivity(in);
            }
        });
        epic_search.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epic_search.show();
    }
    private  void sil_user( final String name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"admin_user_remove.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,FULL_CONNECTION ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("1")){
                                Toast.makeText(AdminPanel.this, "Silme  Başarılı", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(AdminPanel.this, "Öyle Bir Kullanıcı Yok Başka Bir Kullanıcı Adı Deneyin", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AdminPanel.this, "Aradığınız Kayıtta Bir Kullanıcı Yoktur." , Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminPanel.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("name",name);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void get_user(final String name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"admin_userget_detail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                final String STR_NAME=ob.getString("name").trim();
                                final String STR_EMAIL =ob.getString("email").trim();
                                pop_up_admin_islemler(STR_NAME,STR_EMAIL);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminPanel.this, "Aradığınız Kayıtta Bir Kullanıcı Yoktur." , Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminPanel.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("name",name);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
