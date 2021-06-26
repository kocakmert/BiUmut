package com.example.blood;
import android.annotation.SuppressLint;
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
public class admin_mesaj extends AppCompatActivity {
    private Button btn_back;
    private String result="";
    private ListView listView;
    final List<Class_Admin_MessageBox_Users_Photo> user = new ArrayList<Class_Admin_MessageBox_Users_Photo>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mesaj);
        listView = (ListView)findViewById(R.id.listView1);
        btn_back=(Button)findViewById(R.id.main_back);
        mesaj_atanlar("1");   // mesaj atan kullanıcların bilgikeri alınır ...
        CustomList_Admin_MessageBox_UsersPhoto adapter = new CustomList_Admin_MessageBox_UsersPhoto(this, user); // ...sınıf oluşturuldu...
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Adminin Class_Real_Blood_Request kutusuna tıklayarak onaylayabildiği
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                result=((TextView)view.findViewById(R.id.user_name)).getText().toString();
               Intent intent=new Intent(admin_mesaj.this,admin_message_user.class);
               intent.putExtra("name",result);
               startActivity(intent);
           }
        });
        // --- Buton Tıklama Efeckt ---
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
                Intent in = new Intent(admin_mesaj.this,AdminPanel.class);
                startActivity(in);
            }
        });
        // --- Bitiş ---
    }
    // ... Admin ' e Mesaj atanları Listesi ...
    private  void mesaj_atanlar(final String  user_id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"admin_message_show.php";
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
                                final String n_message=ob.getString("name").trim();
                                final String n_photo=ob.getString("photo").trim();
                                user.add(new Class_Admin_MessageBox_Users_Photo(n_message, n_photo));
                                //ResimYukle(n_photo);
                                //name_info.put(n_message,n_photo);
                                adapter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(admin_mesaj.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(admin_mesaj.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
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

    // --- Liste Güncellemesi ---
    private void adapter(){
        CustomList_Admin_MessageBox_UsersPhoto adapter = new CustomList_Admin_MessageBox_UsersPhoto(this, user);
        listView.setAdapter(adapter);
    }
}

