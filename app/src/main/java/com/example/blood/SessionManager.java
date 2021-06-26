package com.example.blood;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;
public class SessionManager {
    private SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private Context context;
    final private int PRIVATE_MODE=0;
    private static final String PREF_NAME="LOGIN";
    private static final String LOGIN="IS_LOGIN";
    public static final String NAME="NAME";
    public static final String PHOTO="PHOTO";
    public static final String ID="ID";
    private int deger =0;
    public SessionManager(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }
    public void createSession(String name,String id,String photo){
        deger=1;
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(ID,id);
        editor.putString(PHOTO,photo);
        editor.apply();
    }
    public  boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }
    public  int checkLogin(){
        if(this.isLoggin()){
            deger=1;
        }
        else{
            deger=0;
        }
        return  deger;
    }
    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user=new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(ID,sharedPreferences.getString(ID,null));
        return  user;
    }
    public String getPHOTO() {
        String Photo =sharedPreferences.getString(PHOTO,null);
        return Photo;
    }
    public void logout(){
        deger=0;
        editor.clear();
        editor.commit();
        Intent i=new Intent(context,LoginActivity.class);
        context.startActivity(i);
        ((Giris) context).finish();
    }
}
