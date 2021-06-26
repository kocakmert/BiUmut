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
public class kan_ihtiyaci_istekleri extends AppCompatActivity {
    private HashMap<String, String>name_info=new HashMap<>();
    Context cn=this;
    private String result;
    private Button btn_back;
    private ListView listView;
    final List<Class_ListDirectory> blood_query = new ArrayList<Class_ListDirectory>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kan_ihtiyaci_istekleri);
        istekleri_getir("1");
        listView = (ListView)findViewById(R.id.listView1);
        btn_back= (Button)findViewById(R.id.btn_back) ;
        CustomList_Adapter adapter = new CustomList_Adapter(this, blood_query,3);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Adminin Class_Real_Blood_Request kutusuna tıklayarak onaylayabildiği
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                result=((TextView)view.findViewById(R.id.text1)).getText().toString();
                AlertDialog.Builder alert=new AlertDialog.Builder(cn);
                alert.setTitle(result)
                        .setPositiveButton("İsteği Çıkar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                karar_istek_çıkar(result);
                            }
                        }).setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                }).create().show();
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
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kan_ihtiyaci_istekleri.this,AdminPanel.class);
                startActivity(in);
            }
        });
    }
    // --- Veritabanından istekleri getir ---
    private  void istekleri_getir(final String  name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION + "map_blood_query_show.php";
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
                                final String nn=ob.getString("adres").trim();
                                final String n_istek_ad=ob.getString("istek_ad").trim();
                                final String n_tarih=ob.getString("tarih").trim();
                                blood_query.add(new Class_ListDirectory(n_istek_ad, nn,n_tarih));
                                //name_info.put(n_istek_ad,nn);
                                adapter();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(kan_ihtiyaci_istekleri.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(kan_ihtiyaci_istekleri.this, "İnternet Problemi" + error.toString(), Toast.LENGTH_SHORT).show();
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

    // --- İstek Çıkarma ---
    public  void karar_istek_çıkar(final String istek_ad){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION + "map_blood_query_remove_show.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("1")){
                                Toast.makeText(kan_ihtiyaci_istekleri.this,"Reddedildi", Toast.LENGTH_SHORT).show();
                                name_info.remove(istek_ad);
                                adapter();
                            }
                            else{
                                Toast.makeText(kan_ihtiyaci_istekleri.this,"Hata", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(kan_ihtiyaci_istekleri.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(kan_ihtiyaci_istekleri.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
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

    // --- Liste Güncelleme ---
    private void adapter(){
        CustomList_Adapter adapter = new CustomList_Adapter(this, blood_query,3);
        listView.setAdapter(adapter);
    }
}
