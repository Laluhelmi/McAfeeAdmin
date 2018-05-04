package com.example.l.macprojectadmin.Request;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Interface.OnLoginRequested;
import com.example.l.macprojectadmin.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by L on 12/12/17.
 */

public class LoginRequest extends MyVolley{

    public LoginRequest(Context context){
        super(context);
    }

    public void loginVoid(final String username, final String password, final OnLoginRequested
                          loginRequested){
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                ,Http.url + "login_admin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String nama = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("status").equals("succes ")){
                                context.startActivity(
                                        new Intent(context, MainActivity.class)
                                );
                                nama = object.getJSONObject("data").getString("nama");
                                loginRequested.onLogin(nama);
                            }else {
                                Toast.makeText(context,
                                        "Username dan Password " +
                                                "tidak Valid", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Log.d("ada error json",e.getMessage());
                        }

                    }
                }, errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("email"   ,username);
                map.put("password",password);
                if(new AppSession(context).isLogin() == false){
                    map.put("token", FirebaseInstanceId.getInstance().getToken());
                }
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
