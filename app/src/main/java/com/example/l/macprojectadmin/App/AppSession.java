package com.example.l.macprojectadmin.App;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by L on 12/12/17.
 */

public class AppSession {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public AppSession(Context context){
        sharedPreferences = context.getSharedPreferences("akun",Context.MODE_APPEND);
        editor            = sharedPreferences.edit();
    }
    public void setUsernameAndPassword(String username,String password,String nama){
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putBoolean("islogin",true);
        editor.putString("nama",nama);
        editor.commit();
    }
    public void setEmail(String email){
        editor.putString("email",email);
        editor.commit();
    }
    public String getEmail(){
        return this.sharedPreferences.getString("email",null);
    }
    public void setNama(String nama){
        editor.putString("nama",nama);
        editor.commit();
    }
    public String getUsername(){
        return sharedPreferences.getString("username",null);
    }
    public String getPassword(){
        return sharedPreferences.getString("password",null);
    }
    public boolean isLogin(){
        return sharedPreferences.getBoolean("islogin",false);
    }

    public String getNama(){
        return sharedPreferences.getString("nama",null);
    }
    public void keluar(){
        editor.clear();
        editor.commit();
    }
    public void setBadgeCount(int badgeCount){
        editor.putInt("badge",badgeCount);
        editor.commit();
    }
    public int getBadgeCount(){
        return sharedPreferences.getInt("badge",0);
    }


}
