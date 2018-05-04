package com.example.l.macprojectadmin.Request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Interface.OnGetNotifikasi;

import org.json.JSONObject;

/**
 * Created by L on 14/12/17.
 */

public class CountNotifRequest extends MyVolley {

    public CountNotifRequest(Context context){
        super(context);
    }

    public void getAllNotif(final OnGetNotifikasi notifikasi){
        StringRequest stringRequest = new StringRequest(Http.url + "notifikasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            notifikasi.getNotif(object.getJSONObject("data")
                                    .getString("permintaan validasi"),
                                    object.getJSONObject("data")
                                    .getString("klaim pending"));
                        }catch (Exception e){
                            Log.d("error",e.getMessage());
                        }
                    }
                }, errorListener());

        requestQueue.add(stringRequest);
    }
}
