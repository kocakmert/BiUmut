package com.example.blood;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
public class admin_message_user extends AppCompatActivity {
    private String aa;
    private TextView txt_name;
    private ImageView profil_Photo;
    private ListView listView;
    private Button send,back;
    private String id;
    private EditText message;
    final List<Class_ListDirectory> mesaj_list = new ArrayList<Class_ListDirectory>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message_user);
        listView = (ListView)findViewById(R.id.listView1);
        txt_name=(TextView)findViewById(R.id.name);
        profil_Photo = (ImageView)findViewById(R.id.profile_photo);
        send=findViewById(R.id.send);
        back=findViewById(R.id.main_back);
        message=findViewById(R.id.message);
        Intent intent=getIntent();
        final String final_str_final=intent.getStringExtra("name");
        txt_name.setText(final_str_final);
        id_getir(final_str_final);
        CustomList_Adapter adapter = new CustomList_Adapter(this, mesaj_list,2);
        listView.setAdapter(adapter);
        send.setOnTouchListener(new View.OnTouchListener() {
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
        back.setOnTouchListener(new View.OnTouchListener() {
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_message=message.getText().toString().trim();
                if(!m_message.isEmpty()){
                    mesaj_gonder(m_message, id);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(admin_message_user.this,AdminPanel.class);
                startActivity(in);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Adminin Class_Real_Blood_Request kutusuna tıklayarak onaylayabildiği
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            }
        });
    }
    // --- Kullanıcının id' si ---
    private  void id_getir(final String  name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"admin_message_id_get.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                id=ob.getString("id");
                                final String STR_PHOTO=ob.getString("photo");
                                ResimYukle(STR_PHOTO);
                                mesajları_getir(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(admin_message_user.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(admin_message_user.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
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

    // --- Mesaj Gönderme ---
    private  void mesaj_gonder(final String mesaj, final String user_id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION + "admin_message_send.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("0")){
                                mesaj_list.clear();
                                mesajları_getir(id);
                                message.setText("");
                                Toast.makeText(admin_message_user.this,"Mesaj Yollandı...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(admin_message_user.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(admin_message_user.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("mesaj",mesaj);
                params.put("name", "admin");
                params.put("user_id",user_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // --- Mesajları Getirme ---
    private  void mesajları_getir(final String  user_id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"admin_message_user_get.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            for (int i = 0; i < jsonArray.length(); i++) {
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
                            Toast.makeText(admin_message_user.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(admin_message_user.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
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

    // --- Listeyi Güncelleme ---
    private void adapter(){
        CustomList_Adapter adapter = new CustomList_Adapter(this, mesaj_list,2);
        listView.setAdapter(adapter);
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
