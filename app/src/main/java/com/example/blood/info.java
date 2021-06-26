package com.example.blood;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
public class info extends AppCompatActivity {
    private Button btn_back;
    private TextView txt_name;
    private ImageView profil_photo;
    private String str_id,str_name,str_photo,str_session_value;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        profil_photo=(ImageView) findViewById(R.id.logos);
        txt_name=(TextView) findViewById(R.id.name);
        btn_back=(Button)findViewById(R.id.buton_back);
        // ... Ana Sayfadan Bilgilerin Alınması ...
        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_id=intent.getStringExtra("id");
        str_photo=intent.getStringExtra("photo");
        str_session_value=intent.getStringExtra("session_active_value");
        // ... Bitiş ...
        txt_name.setText(str_name);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(info.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(info.this,Giris.class));
                }
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
                        view.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.SRC_ATOP);
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
        ResimYukle(str_photo);
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
