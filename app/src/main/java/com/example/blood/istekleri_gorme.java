package com.example.blood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class istekleri_gorme extends AppCompatActivity {
    private String result;  // _---- ??
    private Context cn=this;
    private ListView listView;
    private Button btn_back;
    final List<Class_ListDirectory> Class_Real_Blood_Request = new ArrayList<Class_ListDirectory>();
    private CustomList_Adapter adapter;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istekleri_gorme);
        istekleri_getir("1"); //  ... İstekler getirilecek ...
        listView = (ListView)findViewById(R.id.listView1);
        btn_back = (Button)findViewById(R.id.main_back);
        adapter = new CustomList_Adapter(this, Class_Real_Blood_Request,4);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                result=((TextView)view.findViewById(R.id.text2)).getText().toString();
                AlertDialog.Builder alert=new AlertDialog.Builder(cn);
                alert.setTitle(result)
                        .setPositiveButton("Onay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                karar(result);
                            }
                        }).setNegativeButton("Red", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        karar_red(result);
                    }
                }).create().show();
            }
        });
        // --- Buton Efect ---
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
        // --- Butona Tıklandınğında ---
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(istekleri_gorme.this,AdminPanel.class));
            }
        });
    }
    //  --- İstekler Getirilir ---
    private  void istekleri_getir(final String  name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_query_show.php";
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
                                final String nn=ob.getString("name").trim();
                                final String n_istek_ad=ob.getString("istek_ad").trim();
                                final String n_tarih=ob.getString("tarih").trim();
                                Class_Real_Blood_Request.add(new Class_ListDirectory(nn, n_istek_ad,n_tarih));
                                adapter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(istekleri_gorme.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(istekleri_gorme.this, "İnternet Problemi" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                )
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

    //  --- Gelen İstekler Liste'ye ekleme ---
    private void adapter(){
        adapter = new CustomList_Adapter(this, Class_Real_Blood_Request,4);
        listView.setAdapter(adapter);
    }

    // --- İstek Red---
    public  void karar_red(final String istek_ad){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_query_ignore.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("1")){
                                Toast.makeText(istekleri_gorme.this,"Reddedildi", Toast.LENGTH_SHORT).show();
                                Class_Real_Blood_Request.remove(istek_ad);
                                adapter();
                            }
                            else{
                                Toast.makeText(istekleri_gorme.this,"Hata", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(istekleri_gorme.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(istekleri_gorme.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("istek_ad",istek_ad);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // --- İstek Kabul ---
    public  void karar(final String istek_ad){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_query_confirm.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("1")){
                                Toast.makeText(istekleri_gorme.this,"Onaylandı", Toast.LENGTH_SHORT).show();
                                //name_info.remove(istek_ad);
                                Class_Real_Blood_Request.remove(istek_ad);
                                adapter();

                            }
                            else{
                                Toast.makeText(istekleri_gorme.this,"Hata", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(istekleri_gorme.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(istekleri_gorme.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("istek_ad",istek_ad);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
