package com.example.l.macprojectadmin.Request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Entity.ValidasiEntity;
import com.example.l.macprojectadmin.Interface.AfterValidate;
import com.example.l.macprojectadmin.Interface.ValidasiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by L on 14/12/17.
 */

public class UserValidasiReqeust extends MyVolley{

    public UserValidasiReqeust(Context context){
        super(context);
    }
    public void getData(final ValidasiInterface validasiInterface){
        StringRequest stringRequest = new StringRequest(Http.url + "validasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<ValidasiEntity> validasiEntities = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                ValidasiEntity entity = new ValidasiEntity();
                                entity.setNo_rek(object.getString("no_rek"));
                                entity.setNama_user(object.getString("nama_user"));
                                entity.setJ_kelamin(object.getString("j_kelamin"));
                                entity.setTelp(object.getString("telp"));
                                entity.setEmail(object.getString("email"));
                                entity.setNama_toko(object.getString("nama_toko"));
                                entity.setAlamat(object.getString("alamat"));
                                entity.setStatus(object.getString("status"));
                                entity.setToken(object.getString("token"));
                                validasiEntities.add(entity);
                            }
                            validasiInterface.getData(validasiEntities);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },errorListener());
        requestQueue.add(stringRequest);
    }
    public void terimaValidasi(String token, final AfterValidate validate){
        StringRequest stringRequest = new StringRequest(Http.url + "verify1/" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                validate.lakukaSesuatu(response);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },errorListener());
        requestQueue.add(stringRequest);
    }
    public void tolakValidasi(final String token, final AfterValidate validate){
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , Http.url + "verify1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                validate.lakukaSesuatu(response);
            }
        },errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("token",token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
