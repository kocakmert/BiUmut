package com.example.blood;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class maps extends FragmentActivity implements OnMapReadyCallback ,GoogleApiClient.OnConnectionFailedListener {
    private Boolean mLocationPermissionGranted=false;
    private  static  final float DEFAULT_ZOOM=15f;
    private  LatLng sd=new LatLng(39.10384,35.23509); // --- Türkiye'nin Koordinatları ---
    private GoogleMap mMap;
    private Button btn_admin_istek_olustur,btn_kaydet,btn_user_istek_olustur,btn_back;
    private Dialog epic;
    ImageView closepopup;
    TextView pop_kan,pop_rh;
    RadioGroup radioGroup_kan,radioGroup_rh;
    RadioButton rkan_sıfır,rkan_a,rkan_b,rkan_ab,rh_pozitif,rh_negatif;
    String secilen_tkan="";
    String secilen_trh="";
    private String str_id,str_name,str_photo,str_session_value,str_role,str_adres;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    List<Place.Field> placefield= Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,Place.Field.LAT_LNG);
    AutocompleteSupportFragment mautocompleteSupportFragment;
    PlacesClient placesClient;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        epic=new Dialog(this);
        // --- Places Api Kullanmak İçin Keyi Tanımlandı ---
        Places.initialize(getApplicationContext(), "AIzaSyDYRMcBEkEiZkhCOT18GMiDPMfh__AuPWI");
        placesClient = Places.createClient(this);
        // ... Google Maps için hazırlıklar ...
        getLocationPermisiion();  // --- Kullanıcıdan İzin İsteme ---
        // ... bitiş ...
        // ... Ana Sayfadan Bilgilerin Alınması ...
        Intent intent=getIntent();
        str_id=intent.getStringExtra("id");
        str_role=intent.getStringExtra("role");
        // ... Bitiş ...
        btn_admin_istek_olustur=(Button)findViewById(R.id.btn);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_user_istek_olustur=(Button)findViewById(R.id.btn_user_ıstek);
        if(str_role.equals("0")){
            str_name=intent.getStringExtra("name");
            str_id=intent.getStringExtra("id");
            str_photo=intent.getStringExtra("photo");
            str_session_value=intent.getStringExtra("session_active_value");
            btn_admin_istek_olustur.setVisibility(View.INVISIBLE);
        }
        else{
            btn_admin_istek_olustur.setVisibility(View.VISIBLE);
            btn_user_istek_olustur.setVisibility(View.INVISIBLE);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_role.contains("0")){
                    if(str_session_value.contains("0")){
                        Intent intent=new Intent(maps.this,Giris.class);
                        intent.putExtra("name",str_name);
                        intent.putExtra("id",str_id);
                        intent.putExtra("photo",str_photo);
                        startActivity(intent);
                    }
                    else{
                        startActivity(new Intent(maps.this,Giris.class));
                    }
                }
                else{
                    Intent in = new Intent(maps.this,AdminPanel.class);
                    startActivity(in);
                }
            }
        });
        user_bilgi_getir(str_id);  // ... Kullanıcının  En son ve Arama Yaptığında  Arama Kriterleri getirilir ...
    }

    // --- Kan İsteği Oluşturma Ekranı ---
    private  void istek_olusturma(final String adress,final String mekan_ad){
        epic.setContentView(R.layout.istekpop_up);
        pop_kan= epic.findViewById(R.id.pop_kan);
        pop_rh= epic.findViewById(R.id.rh);
        radioGroup_kan=epic.findViewById(R.id.kan_grup);
        radioGroup_rh=epic.findViewById(R.id.rh_rdgroup);
        rkan_sıfır=epic.findViewById(R.id.sıfır);
        rkan_a=epic.findViewById(R.id.a);
        rkan_b=epic.findViewById(R.id.b);
        rkan_ab=epic.findViewById(R.id.ab);
        rh_pozitif=epic.findViewById(R.id.pozitif);
        rh_negatif=epic.findViewById(R.id.negatif);
        btn_kaydet= epic.findViewById(R.id.btn_kaydet);
        closepopup= epic.findViewById(R.id.pop_ex);
        epic.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epic.show();
        btn_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int secilenkancins=radioGroup_kan.getCheckedRadioButtonId();
                int secilenkanrh=radioGroup_rh.getCheckedRadioButtonId();
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
                final String finalSecilen_tkan = secilen_tkan;
                final String finalSecilen_trh = secilen_trh;
                if(str_role.equals("0")){
                    if(!finalSecilen_tkan.isEmpty() && !finalSecilen_trh.isEmpty()){
                        final String istek_ad=mekan_ad+" "+finalSecilen_tkan+" "+finalSecilen_trh;
                        user_kayit_olustur(str_id,adress,mekan_ad,finalSecilen_tkan,finalSecilen_trh,istek_ad);
                        epic.cancel();
                    }
                    else{
                        pop_kan.setError("Boş Bırakamazsınız");
                        pop_rh.setError("Boş Bırakamazsınız");
                    }

                }
                else{
                    if(!finalSecilen_tkan.isEmpty() && !finalSecilen_trh.isEmpty()){
                        final String istek_ad=mekan_ad+" "+finalSecilen_tkan+" "+finalSecilen_trh;
                        admin_istek_olustur(adress,mekan_ad,finalSecilen_tkan, finalSecilen_trh,istek_ad);
                    }
                    else{
                        pop_kan.setError("Boş Bırakamazsınız");
                        pop_rh.setError("Boş Bırakamazsınız");
                    }
                }
            }
        });
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epic.dismiss();
            }
        });
    }

    // ---  Aranan Adresleri  Otamatik Setlenmesi ve Tamamlama ---
    private void setupauto(){
        mautocompleteSupportFragment=(AutocompleteSupportFragment)getSupportFragmentManager()
                .findFragmentById(R.id.auto_complete);
        mautocompleteSupportFragment.setPlaceFields(placefield);
        mautocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                sd=place.getLatLng();
                final String adres=place.getAddress();
                str_adres=adres;
                final String mekan_ad=place.getName();
                final String toplam_mekan_bilgi = mekan_ad+" "+adres;
                aranan_adres(sd,DEFAULT_ZOOM,place.getName(),adres);   // ... Adres Çubuğunda Aranan Adresin Gösterilmesi ...
                btn_admin_istek_olustur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        istek_olusturma(toplam_mekan_bilgi,mekan_ad);
                    }
                });
                btn_user_istek_olustur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        istek_olusturma(toplam_mekan_bilgi,mekan_ad);

                    }
                });
            }
            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    // --- Kullanıcının  En son ve Arama Yaptığında  Arama Kriterleri getirilir ---
    private  void user_bilgi_getir(final String id){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"map_user_detail_get.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                final String konum=ob.getString("konum").trim();
                                final String kangrup=ob.getString("kangrup").trim();
                                final String kanrh=ob.getString("kanrh").trim();
                                user_istenen_istekler(konum,kangrup,kanrh);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(maps.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(maps.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //  --- Kullanıcının İsteğine Göre Ekranda Kan İsteklerinin Konumların Gösterilmesi İçin Sorgu ---
    private  void user_istenen_istekler(final String konum,final String kangrup,final String kanrh) {
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"map_user_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("read");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ob=jsonArray.getJSONObject(i);
                                final String adres=ob.getString("adres").trim();
                                final String mekan_adi=ob.getString("mekan_ad").trim();
                                final String kangrup_=ob.getString("kan").trim();
                                final String kanrh_=ob.getString("rh").trim();
                                istekler_liste_donusum(adres,kangrup_,kanrh_,mekan_adi); // ... Gelen Bilgiler listeye eklenmesi ...
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(maps.this, "Hata" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(maps.this, "Hatakkkk" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("konum",konum);
                params.put("kangrup",kangrup);
                params.put("kanrh",kanrh);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //  --- kullanıcı_istenen_istekler' fonksiyonundan gelen Verileri Liste'den istene bilgilerin alınması  ---
    private  void istekler_liste_donusum(final String adres_,final String kan_grup,final String kan_rh,final String mekan_adi){
        Geocoder geocoder=new Geocoder(maps.this);
        List<Address> list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(adres_,1);
        }catch (IOException e){
        }
        if(list.size() >0){
            final Address address=list.get(0);
            istek_mark(new LatLng(address.getLatitude(),address.getLongitude()),address.getAddressLine(0),kan_grup,kan_rh,mekan_adi); //Listeden Adres Bilgiisi Alınıp Haritada Görüntülenmesi
        }
    }

    //  --- Harita'da Bir Adres Arandıgında O Adresi Mark'lama ---
    private  void aranan_adres(LatLng latLng,float zoom,String title,String adres){
        String snippet="Adres:"+" "+adres;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options=new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet);
        mMap.addMarker(options);
    }

    // --- Kullanıcının Aradığı İsteklerin Mark'lanması (Gösterilmesi) ---
    private  void istek_mark(LatLng latLng,String title,final String nn1,final  String nn2,final String nnx){
        String ss=nnx+" "+ title;
        String snippet="İstek:"+" "+nn1+" "+"Rh"+" "+ nn2;
        MarkerOptions options=new MarkerOptions()
                .position(latLng)
                .title(ss)
                .snippet(snippet);
        mMap.addMarker(options);
    }

    // --- Kullanıcıdan İzin İsteme ---
    private  void getLocationPermisiion(){
        String[] permission={Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= 23) {
            if (!izinKontrol(maps.this, permission)) { // izin verilmiş mi
                requestPermissions(permission, 100); // verilmediyse, izin isteme penceresi açılacak
            } else
                // ... Zaten İzin Verildiyse ...
                mLocationPermissionGranted=true;
                initMap();
        } else{
            mLocationPermissionGranted=true;
            initMap(); // api 6.0 altındaysa izne bakmadan doğrudan açılır.
        }
    }

    //  --- İzin Verildiğinde Yapılacaklar ---
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    boolean kameraIzniVerildiMi = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (kameraIzniVerildiMi) {
                        mLocationPermissionGranted=true;
                        initMap();
                    } else {
                        mLocationPermissionGranted=false;
                        Toast.makeText(getApplicationContext(), "İzin verilmediği için Harita Açılmıyor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mLocationPermissionGranted=false;
                    Toast.makeText(getApplicationContext(), "İzin verilmediği için Harita Açılmıyor", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // --- Harita Üzerinde Gösterilmesi (Google Maps Api ile) ---
    private void initMap(){
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupauto();
    }

    // --- İznin Verilip Verilmediğinin Kontrolü ---
    public static boolean izinKontrol(Context context, String... izinler) {
        if (context != null && izinler != null) {
            for (String izin : izinler) {
                if (ActivityCompat.checkSelfPermission(context, izin) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    // --- Konum Bilgisine Erişme İzin Verilirse Telefonun Konumunu Alma  ---
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        if(mLocationPermissionGranted){
            getDeviceLocation();
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return; // Google Burada Tekrar İzinlerin Check Edilmesini İstiyor .
            }
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            }
    }

    // --- Telefonun Konum Bilgisi Alma ---
    private void getDeviceLocation(){
        mfusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted){
                final Task location=mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation= (Location) task.getResult();
                            if(currentLocation !=null){
                                KameraKonum(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                        DEFAULT_ZOOM );
                            }
                            else{
                                // --- Konum Açık Değilse Türkiye Ekranda Gösterilecek ---
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sd,4.6f));
                                Toast.makeText(maps.this,"Konumunuzu Açınız.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(maps.this,"Konumunuzu Açınız.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Toast.makeText(maps.this,"Hatak",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    //   --- Admin Kan İsteği Oluşturma ---
    private  void admin_istek_olustur(final String adres,final String mekan_ad,final String kan,final String rh,final String istek_ad){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"map_request.php";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse( String response) {
                        try {
                            if(response.contains("0")){
                                Toast.makeText(maps.this, "Öyle Bir İstek Zaten Var ", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(maps.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                                //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(maps.this, "Hatar"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(maps.this, "Hatax"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("adres",adres);
                params.put("mekan_ad",mekan_ad);
                params.put("kan",kan);
                params.put("rh",rh);
                params.put("istek_ad",istek_ad);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // --- Kullanıcı Kayıt Olusturma ---
    private  void user_kayit_olustur(final String user_id,final String adres,final String mekan_ad,final String kan,final String rh,final String istek_ad){
        Class_DataBase_Connection dbc= Class_DataBase_Connection.getDataBaseCon();
        final String CONNECTION = dbc.getCONNECTION();
        final String FULL_CONNECTION = CONNECTION+"user_map_record_create.php";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, FULL_CONNECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse( String response) {
                        try {
                            if(response.contains("0")){
                                Toast.makeText(maps.this, "Öyle Bir İstek Zaten Var ", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(maps.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(maps.this, "Hatar"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(maps.this, "Hatax"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("user_id",user_id);
                params.put("adres",adres);
                params.put("mekan_ad",mekan_ad);
                params.put("kan",kan);
                params.put("rh",rh);
                params.put("istek_ad",istek_ad);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // --- Ekranda Konumun Olduğu Yerin Gösterilmesi ---
    private void KameraKonum(LatLng latLng,float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    // --- Geri Tuşuna Basıldığında Yapılacaklar ---
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            if(str_role.contains("0")){
                if(str_session_value.contains("0")){
                    Intent intent=new Intent(maps.this,Giris.class);
                    intent.putExtra("name",str_name);
                    intent.putExtra("id",str_id);
                    intent.putExtra("photo",str_photo);
                    startActivity(intent);
                }
                else{
                    startActivity(new Intent(maps.this,Giris.class));
                }
            }
            else{
                Intent in = new Intent(maps.this,AdminPanel.class);
                startActivity(in);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}