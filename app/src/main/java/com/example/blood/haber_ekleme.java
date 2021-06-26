package com.example.blood;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class haber_ekleme extends AppCompatActivity {
    private Button btn_foto,btn_back;
    private EditText et_title,et_message;
    private String txt_title,txt_message;
    private Bitmap bitmap;
    private ImageView profil_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_ekleme);
        btn_foto=(Button)findViewById(R.id.resim);
        btn_back=(Button)findViewById(R.id.main_back);
        et_title=findViewById(R.id.title);
        et_message=findViewById(R.id.message1);
        profil_image=findViewById(R.id.image);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=et_title.getText().toString().trim();
                String message=et_message.getText().toString().trim();
                txt_title=title;
                txt_message=message;
                if(!title.isEmpty()&& !message.isEmpty()){
                    choseFile();
                }
                else{
                    et_title.setError("Boş Giriş");
                    et_message.setError("Boş Giriş");
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(haber_ekleme.this,gundem.class);
                intent.putExtra("name","admin");
                intent.putExtra("role","1");
                startActivity(intent);
            }
        });
    }
    //   ---Telefondan Resim Seçme Ekranı ---
    private void choseFile(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    //  ---Galeriden resim seçildiğinde ---
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
            final String path=getStringImage(bitmap);
            Toast.makeText(this, path+txt_title+txt_message, Toast.LENGTH_SHORT).show();
            UploadPicture(path,txt_title,txt_message);
        }
    }

    //   --- Haber ekleme  ---
    private void UploadPicture(final String image,final String title,final String message){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Yükleniyor..");
        progressDialog.show();
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"icerik_kaydet.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //JSONObject jsonObject=new JSONObject(response);
                            //String success=jsonObject.getString("success");
                            if(response.equals("1")){
                                Toast.makeText(haber_ekleme.this, "Yüklendi..", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(haber_ekleme.this, "Yüklenemedi..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(haber_ekleme.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(haber_ekleme.this, "Hata.."+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("image",image);
                params.put("title",title);
                params.put("message",message);
                return  params;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

   //   ... Fotoğrafın Formatlanması Ve Sıkıştırılması ...
    public  String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imageByteArray =byteArrayOutputStream.toByteArray();
        String encodedImage= Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodedImage;
    }

    // --- Telefondaki Geri Düğmesine Basıldığında ---
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(haber_ekleme.this,gundem.class);
            intent.putExtra("name","admin");
            intent.putExtra("role","1");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
