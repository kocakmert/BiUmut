package com.example.blood;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class gundem extends AppCompatActivity {
    private ImageView profil_photo;
    private ListView listView;
    private TextView txt_name,txt_top_name;
    private Button btn_back,btn_ekle;
    private String str_id,str_name,str_photo,str_session_value,str_role,result,news;
    private final List<Class_ListDirectory> n_news= new ArrayList<Class_ListDirectory>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gundem);
        gundem_getir("1");  // Haberlerin Getirlemesi...
        // ...  XML Ayarlamaları ...
        listView = (ListView)findViewById(R.id.listView1);
        btn_ekle=(Button)findViewById(R.id.ekle);
        btn_back=(Button)findViewById(R.id.btn_back);
        profil_photo=(ImageView) findViewById(R.id.logos);
        txt_top_name= (TextView) findViewById(R.id.teext);
        txt_name=(TextView) findViewById(R.id.name);
        // ... Bitiş ...
        // ... Ana Sayfadan Bilgilerin Alınması ...
        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_role=intent.getStringExtra("role");
        if(str_role.equals("0")){   // Role Kullanıcı
            str_id=intent.getStringExtra("id");
            str_photo=intent.getStringExtra("photo");
            str_session_value=intent.getStringExtra("session_active_value");
            ResimYukle(str_photo);
        }
        else{
            btn_ekle.setVisibility(View.VISIBLE);
            profil_photo.setBackgroundResource(R.drawable.administrator);
            txt_top_name.setText("Yönetici");
        }
        // ... Bitiş ...
        txt_name.setText(str_name);
        // ...  Liste İşlemleri ...
        CustomList_Adapter adapter = new CustomList_Adapter(this, n_news,1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                result=((TextView)view.findViewById(R.id.text1)).getText().toString();
                news=((TextView)view.findViewById(R.id.text2)).getText().toString();
                Intent intent=new Intent(gundem.this,haberler.class);
                intent.putExtra("result",result);
                intent.putExtra("news",news);
                startActivity(intent);
            }
        });
        // ... Butonlara Tıklanıldığında Renk Değiştirme ...
        btn_back.setOnTouchListener(new View.OnTouchListener() {
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
        btn_ekle.setOnTouchListener(new View.OnTouchListener() {
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
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_role.equals("0")){
                    if(str_session_value.contains("0")){
                        Intent intent=new Intent(gundem.this,Giris.class);
                        intent.putExtra("name",str_name);
                        intent.putExtra("id",str_id);
                        intent.putExtra("photo",str_photo);
                        startActivity(intent);
                    }
                    else{
                        startActivity(new Intent(gundem.this,Giris.class));
                    }
                }
                else{
                    Intent in = new Intent(gundem.this,AdminPanel.class);
                    startActivity(in);
                }
            }
        });
        // ... Bitiş ...
        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(gundem.this,haber_ekleme.class);
                startActivity(in);
            }
        });

    }
    // ... Resmin Yüklenmesi için api ...
    private void ResimYukle(String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(profil_photo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
    }
    // ...İçeriklerin Veri Tabanından Çekilmesi...
    private  void gundem_getir(final String  admin){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"content_show.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            int i=0;
                            for (i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                final String n_title=ob.getString("title").trim();
                                final String n_message=ob.getString("message").trim();
                                final String n_tarih=ob.getString("tarih").trim();
                                n_news.add(new Class_ListDirectory(n_title, n_message,n_tarih));
                                //name_info.put(n_title,n_message);
                                adapter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(gundem.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(gundem.this, "İnternet Problemi" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("admin",admin);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    // ... Gelen İçeriklerin adaptera eklenmesi ...
    private void adapter(){
        CustomList_Adapter adapter = new CustomList_Adapter(this, n_news,1);
        listView.setAdapter(adapter);
    }
    // ... Telefonda Geri Tuşuna Basıldığında Yapılacaklar...
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            if(str_role.equals("0")){
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(gundem.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(gundem.this,Giris.class));
                }
            }
            else{
                Intent in = new Intent(gundem.this,AdminPanel.class);
                startActivity(in);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
