package com.example.blood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class user_bilgi extends AppCompatActivity {
    private TextView txt_name,txt_email,txt_kangrup,txt_konum,txt_tarih,txt_name_info;
    private Button btn_back,btn_foto;
    private Bitmap bitmap;
    private ImageView profil_image;
    private String str_id,str_name,str_photo,str_session_value;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bilgi);
        // ...Ayarlamalar...
        btn_foto=(Button)findViewById(R.id.foto);
        btn_back=(Button)findViewById(R.id.main_back);
        profil_image=(ImageView)findViewById(R.id.logos);
        txt_name_info=(TextView)findViewById(R.id.namex);
        txt_name=(TextView)findViewById(R.id.name);txt_email=findViewById(R.id.email);
        txt_kangrup=(TextView)findViewById(R.id.kangrup);txt_konum=findViewById(R.id.konum);
        txt_tarih=(TextView)findViewById(R.id.tarih);
        // ...  ---   ...
        // ... Giris'ten gelen verilerin atanması ...
        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_id=intent.getStringExtra("id");
        str_photo=intent.getStringExtra("photo");
        str_session_value=intent.getStringExtra("session_active_value");
        // ...  ---  ...
        txt_name.setText(str_name);
        txt_name_info.setText(str_name);
        bilgi_getir(str_name);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] Galeri_izin = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!izinKontrol(user_bilgi.this, Galeri_izin)) { // izin verilmiş mi
                        requestPermissions(Galeri_izin, 100); // verilmediyse, izin isteme penceresi aç
                    } else
                        choseFile(); // zaten izin verilmişse galeriye eriş
                } else
                    choseFile(); // api 6.0 altındaysa izne bakmadan doğrudan galeriye eriş
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(user_bilgi.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(user_bilgi.this,Giris.class));
                }
            }
        });
        btn_back.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
                        view.getBackground().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_ATOP);
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
        btn_foto.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
                        view.getBackground().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_ATOP);
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
        ResimYukle(str_photo);// Profil Fotoğrafının Yüklenmesi
    }
    //  --- Kullancı Bilgilerinin Getirilmesi ---
    private  void bilgi_getir(final String name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"get_user_info.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            //String success=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            // if(success.equals("1")){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                final String final_email=ob.getString("email").trim();
                                final String final_kangrup=ob.getString("kangrup").trim();
                                final String final_kanrh=ob.getString("kanrh").trim();
                                final String final_konum=ob.getString("konum").trim();
                                final String final_tarih=ob.getString("tarih").trim();
                                final String finalsum_kangrup=final_kangrup+" "+final_kanrh;
                                txt_email.setText(final_email);
                                txt_kangrup.setText(finalsum_kangrup);
                                txt_konum.setText(final_konum);
                                txt_tarih.setText(final_tarih);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(user_bilgi.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(user_bilgi.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("name",name);
                // params.put("latling",latling);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // --- Resim Yüklemek İçin Kullanıcının Galerisine Erişim ---
    private void choseFile(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 &&resultCode==RESULT_OK && data!=null &&data.getData() !=null){
            Uri filePath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profil_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path=getStringImage(bitmap);
           // Toast.makeText(getApplicationContext(), path.trim(), Toast.LENGTH_SHORT).show();
            UploadPicture(str_id,path);

        }
    }

    // --- Resmin Veri Tabanına Kaydedilmesi ---
    private void UploadPicture(final String id,final String photo){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Yükleniyor..");
        progressDialog.show();
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"image_upload.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                           JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");
                            String photo =jsonObject.getString("photo");
                            if(success.equals("1")){
                                str_photo =photo;
                                ResimYukle(str_photo); // profil fotoğrafı değiştirildiğinde değişiklik gösterilir...
                                Toast.makeText(user_bilgi.this, "Yüklendi..", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(user_bilgi.this, "Yüklenemedi..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(user_bilgi.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(user_bilgi.this, "Hata.."+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return  params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //  --- Fotoğrafın  Formatlanması  ve Sıkıştırma ---
    public  String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imageByteArray =byteArrayOutputStream.toByteArray();
        String encodedImage= Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodedImage;
    }

    // --- Resim' i yüklemek için api ---
    private void ResimYukle(String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(profil_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    // --- İzin Verildiğinde Yapılacaklar ---
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    boolean kameraIzniVerildiMi = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (kameraIzniVerildiMi) {
                        choseFile();
                    } else {
                        Toast.makeText(getApplicationContext(), "İzin verilmediği için Galeriye Ulaşılamıyor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "İzin verilmediği için Galeriye Ulaşılamıyor", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // --- İstenilen izin Kullanıcı Tarafından Verilemesine Bakma ---
    public static boolean izinKontrol(Context context, String... izinler) {
        if (context != null && izinler != null) {
            for (String izin : izinler) {
                if (ActivityCompat.checkSelfPermission(context, izin) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }
}
