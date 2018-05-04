package com.example.l.macprojectadmin.Request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Interface.AyoReqestListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by L on 12/12/17.
 */

public class MyVolley {
    protected RequestQueue requestQueue;
    protected Context context;
    protected StringRequest request;
    public static int POST = Request.Method.POST;
    public static int GET  = Request.Method.GET;
    public static int DELETE = Request.Method.DELETE;

    public MyVolley(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    protected Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    Toast.makeText(context,
                            "Periksa Jaringan Internet Anda ",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                }

            }

        };
    }
    public void ayoRequest(String url, int method, final AyoReqestListener listener
                          , final Map<String,String> map){
        this.request = new StringRequest(method, Http.url + url+"/?X-API-KEY=istimewa"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.lakukanSesuatu(response);
            }
        },errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        this.request.setRetryPolicy(requstTimeOut());
        requestQueue.add(request);
    }
    public void ayoRequestMap(String url, int method, final AyoReqestListener listener
            , final Map<String,String> map){
        this.request = new StringRequest(method,  url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.lakukanSesuatu(response);
            }
        },errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        this.request.setRetryPolicy(requstTimeOut());
        requestQueue.add(request);
    }

    public DefaultRetryPolicy requstTimeOut(){
        return new DefaultRetryPolicy(15000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
