package com.example.blood;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class mesaj extends AppCompatActivity {
    private String aa="";
    private ListView listView;
    private Button btn_send,btn_back;
    private ImageView profil_photo;
    private TextView txt_user_name;
    private EditText edttxt_message;
    private String str_id,str_photo,str_session_value;
    final List<Class_ListDirectory> mesaj_list = new ArrayList<Class_ListDirectory>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);
        listView = (ListView)findViewById(R.id.listView1);
        btn_send=(Button)findViewById(R.id.send);
        btn_back=(Button)findViewById(R.id.main_back);
        profil_photo=(ImageView) findViewById(R.id.profile_photo);
        edttxt_message=(EditText)findViewById(R.id.message);
        txt_user_name=(TextView)findViewById(R.id.txt_user_name);
        Intent intent=getIntent();
        final String str_name=intent.getStringExtra("name");
        str_id=intent.getStringExtra("id");
        str_photo=intent.getStringExtra("photo");
        str_session_value=intent.getStringExtra("session_active_value");
        txt_user_name.setText(str_name);
        mesajları_getir(str_id); // ... tüm mesajları getirir ...
        CustomList_Adapter adapter = new CustomList_Adapter(this, mesaj_list,2);
        listView.setAdapter(adapter);
        // --- Butona Tıklama Efect ---
        btn_send.setOnTouchListener(new View.OnTouchListener() {
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
        //  ---  Bitiş ---
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(mesaj.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(mesaj.this,Giris.class));
                }
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_message=edttxt_message.getText().toString().trim();
                if(!m_message.isEmpty()){
                    mesaj_gonder(m_message,str_name,str_id);
                }
            }
        });
        ResimYukle(str_photo);
    }
    // --- Kullanıcı Mesajları Getir ---
    private  void mesajları_getir(final String  user_id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_message_show.php";
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
                                final String n_message=ob.getString("mesaj_user").trim();
                                final String n_tarih=ob.getString("tarih_user").trim();
                                final String n_name=ob.getString("name").trim();
                                String nv_tarih=n_tarih.substring(11,n_tarih.length());
                                String nv_tarih1=nv_tarih.substring(0,5);
                                String nv_tarih2=n_tarih.substring(0,10);
                                String nv_yıl=nv_tarih2.substring(0,4);
                                String nv_ay=nv_tarih2.substring(4,8);
                                String nv_gun=nv_tarih2.substring(8,10);
                                String new_tarih=nv_gun+nv_ay+nv_yıl;
                                if(i==0){
                                    mesaj_list.add(new Class_ListDirectory(n_message,n_name,nv_tarih1 ));
                                    aa=new_tarih;
                                    continue;
                                }
                                else{
                                    if(aa.contains(new_tarih)){
                                        mesaj_list.add(new Class_ListDirectory(n_message,n_name,nv_tarih1 ));
                                    }
                                    else{
                                        mesaj_list.add(new Class_ListDirectory("","",new_tarih ));
                                        mesaj_list.add(new Class_ListDirectory(n_message,n_name,nv_tarih1 ));
                                        aa=new_tarih;
                                    }
                                }

                                adapter();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mesaj.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mesaj.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("user_id",user_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //  --- Mesaj Gonderme ---
    private  void mesaj_gonder(final String  mesaj,final String  name,final String user_id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_message_send.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("0")){
                                mesaj_list.clear();
                                mesajları_getir(str_id);
                                edttxt_message.setText("");
                                Toast.makeText(mesaj.this,"Mesaj Yollandı...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mesaj.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mesaj.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("mesaj",mesaj);
                params.put("name",name);
                params.put("user_id",user_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // ---Liste Güncellemesi ---
    private void adapter(){
        CustomList_Adapter adapter = new CustomList_Adapter(this, mesaj_list,2);
        listView.setAdapter(adapter);
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
}
