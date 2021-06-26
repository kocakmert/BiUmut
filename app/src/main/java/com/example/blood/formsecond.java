package com.example.blood;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
public class formsecond extends AppCompatActivity {
    private final String[] iller={"ADANA","ADIYAMAN","AFYON","AĞRI","AMASYA","ANKARA","ANTALYA","ARTVİN","AYDIN","BALIKESİR","BİLECİK","BİNGÖL","BİTLİS","BOLU","BURDUR","BURSA","ÇANAKKALE","ÇANKIRI","ÇORUM","DENİZLİ",
            "DİYARBAKIR","EDİRNE","ELAZIĞ","ERZİNCAN","ERZURUM","ESKİŞEHİR","GAZİANTEP","GİRESUN","GÜMÜŞHANE","HAKKARİ","HATAY","ISPARTA","MERSİN","İSTANBUL","İZMİR","KARS","KASTAMONU","KAYSERİ","KIRKLARELİ","KIRŞEHİR",
            "KOCAELİ","KONYA","KÜTAHYA","MALATYA","MANİSA","KAHRAMANMARAŞ","MARDİN","MUĞLA","MUŞ","NEVŞEHİR","NİĞDE","ORDU","RİZE","SAKARYA","SAMSUN","SİİR","SİNOP","SİVAS","TEKİRDAĞ","TOKAT","TRABZON","TUNCELİ","ŞANLIURFA",
            "UŞAK","VAN","YOZGAT","ZONGULDAK","AKSARAY","BAYBURT","KARAMAN","KIRIKKALE","ŞIRNAK","BATMAN","ŞIRNAK","BARTIN","ARDAHAN","IĞDIR","YALOVA","KARABÜK","KİLİS","OSMANİYE","DÜZCE"};
    private Spinner spinneriller;
    private Context cn=this;
    private ArrayAdapter<String> dataAdapterForIller;
    private RadioGroup radio_kangrup;
    private RadioButton rkan_a,rkan_b,rkan_ab,rkan_sıfır;
    private RadioGroup radio_kanrh;
    private RadioButton rh_pozitif,rh_negatif;
    private String secilen_trh="";
    private String secilen_tkan="";
    private String secilen_konum="";
    private TextView name,konum,kan_grubu,rh_grup;
    private Button kayıt,back;
    private ProgressBar loading;
    private ImageView profil_Photo;
    private String str_id,str_photo,str_name,str_session_value;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formsecond);
        radio_kanrh=findViewById(R.id.rh_rdgroup);
        rh_pozitif=findViewById(R.id.pozitif);
        rh_negatif=findViewById(R.id.negatif);
        radio_kangrup=findViewById(R.id.kan_grup);
        rkan_sıfır=findViewById(R.id.sıfır);
        rkan_a=findViewById(R.id.a);
        rkan_b=findViewById(R.id.b);
        rkan_ab=findViewById(R.id.ab);
        kan_grubu=findViewById(R.id.kan_cins);
        rh_grup=findViewById(R.id.rh);
        spinneriller=findViewById(R.id.spinner1);
        profil_Photo=(ImageView) findViewById(R.id.logos);
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iller);
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneriller.setAdapter(dataAdapterForIller);
        name=findViewById(R.id.name);
        konum=findViewById(R.id.konum);
        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_id=intent.getStringExtra("id");
        str_photo=intent.getStringExtra("photo");
        str_session_value=intent.getStringExtra("session_active_value");
        name.setText(str_name);
        kayıt=findViewById(R.id.main_edit);
        back=findViewById(R.id.main_back);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(formsecond.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    intent.putExtra("session_active_value",str_session_value);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(formsecond.this,Giris.class));
                }

            }
        });
        spinneriller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                for(int i=0;i<80;i++){
                    if(parent.getSelectedItem().toString().equals(iller[i])){
                        secilen_konum=iller[i];
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        kayıt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int secilenkancins=radio_kangrup.getCheckedRadioButtonId();
                int secilenkanrh=radio_kanrh.getCheckedRadioButtonId();
                switch (secilenkancins){
                    case R.id.sıfır:{
                        secilen_tkan=rkan_sıfır.getText().toString();
                        break;
                    }
                    case R.id.a:{
                        secilen_tkan=rkan_a.getText().toString();
                        break;
                    }
                    case  R.id.b:{
                        secilen_tkan=rkan_b.getText().toString();
                        break;
                    }
                    case  R.id.ab:{
                        secilen_tkan=rkan_ab.getText().toString();
                        break;
                    }
                }
                switch (secilenkanrh){
                    case R.id.pozitif:{
                        secilen_trh=rh_pozitif.getText().toString();
                        break;
                    }
                    case R.id.negatif:{
                        secilen_trh=rh_negatif.getText().toString();
                        break;
                    }
                }
                if(!secilen_konum.isEmpty()&& !secilen_tkan.isEmpty()&& !secilen_trh.isEmpty()){
                    loading.setVisibility(View.VISIBLE);
                    form_ara(str_id,secilen_konum,secilen_tkan,secilen_trh,str_name);
                }
                else{
                   konum.setError("Boş Bırakamazsınız");
                   kan_grubu.setError("Boş Bırakamazsınız");
                   rh_grup.setError("Boş Bırakamazsınız");
                }
            }
        });
        kayıt.setOnTouchListener(new View.OnTouchListener() {
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
        ResimYukle(str_photo);
    }
    // --- Kullanıcının Sorgu Yapması ve Map Activity'e gidişi ---
    private void form_ara(final String id,final String secilen_konum,final String secilen_kan,final String secilen_rh,final String name){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"form_change_save.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.contains("1")) {
                                Intent intent=new Intent(formsecond.this,maps.class);
                                intent.putExtra("role","0");
                                intent.putExtra("name",name);
                                intent.putExtra("id",str_id);
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("session_active_value",str_session_value);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(formsecond.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(formsecond.this, "Hata"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String ,String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("id",id);
                params.put("secilen_kan",secilen_kan);
                params.put("secilen_konum",secilen_konum);
                params.put("secilen_rh",secilen_rh);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
    // --- Geri Butonuna Basıldığında ---
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder alert=new AlertDialog.Builder(cn);
            alert.setTitle("Ana Menüye Dönmek İstiyor Musunuz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            if(str_session_value.contains("0")){
                                Intent intent=new Intent(formsecond.this,Giris.class);
                                intent.putExtra("name",str_name);
                                intent.putExtra("id",str_id);
                                intent.putExtra("photo",str_photo);
                                startActivity(intent);
                            }
                            else{
                                startActivity(new Intent(formsecond.this,Giris.class));
                            }
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
}