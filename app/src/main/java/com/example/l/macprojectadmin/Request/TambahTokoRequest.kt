package com.example.l.macprojectadmin.Request

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.l.macprojectadmin.Entity.TokoEntity
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by L on 21/12/17.
 */
class TambahTokoRequest(c : Context) {
    private lateinit var context:Context

    init {
        this.context = c
    }

    public fun simpanData(data: MutableMap<String,String>,listener:SetelahSimpanData){
        MyVolley(context).ayoRequest("toko",MyVolley.POST,AyoReqestListener{
            response ->
            listener.setelahSimpan()

        },data)
    }
    public fun getMoreToko(offset:String,listener: MoreDatListener){
        MyVolley(context).ayoRequest("toko/gettoko/$offset",MyVolley.GET, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)

        },null)

    }
    public fun deleteToko(id:String,listener: MoreDatListener){
        MyVolley(context).ayoRequest("toko/hapus/$id",MyVolley.GET, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)

        },null)
    }
    public fun updateToko(id:String,data:MutableMap<String,String>,listener:MoreDatListener){
        MyVolley(context).ayoRequest("edit_toko",MyVolley.POST, AyoReqestListener {
            response ->
                try {
                    listener.setelahAmbilData(response)
                    Log.d("errrrr",response)
                }catch (e:Exception){
                    Log.d("error",e.message)
                }
        },data)
    }
}