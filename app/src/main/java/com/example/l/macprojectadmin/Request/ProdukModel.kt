package com.example.l.macprojectadmin.Request

import android.content.Context
import android.util.Log
import com.example.l.macprojectadmin.Entity.Jenis_Produk
import com.example.l.macprojectadmin.Entity.Produk
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import org.json.JSONObject

/**
 * Created by L on 22/12/17.
 */
class ProdukModel (context:Context){
    private lateinit var context:Context

    init {
        this.context = context
    }
    public fun getAllData(listener:MoreDatListener,kode_scan:String?){
        var url = ""
        if(kode_scan == null){
            url = "produk"
        }else{
            url = "produk/$kode_scan"
        }

        MyVolley(context).ayoRequest(url,MyVolley.GET,
                AyoReqestListener {
                    response ->
                   listener.setelahAmbilData(response)
                },null)
    }
    public fun addProduk(data:MutableMap<String,String>,listener:MoreDatListener){
        MyVolley(context).ayoRequest("produk",MyVolley.POST, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)

        },data)
    }
    public fun deleteProduk(id:String,listener: MoreDatListener){
        var data:MutableMap<String,String> = mutableMapOf()
        data.put("id_produk",id)
        MyVolley(context).ayoRequest("produk/hapus",MyVolley.POST,
                AyoReqestListener {
                    response ->
                    listener.setelahAmbilData(response)

                },data)
    }
    public fun editProduk(data:MutableMap<String,String>,listener:MoreDatListener){

        MyVolley(context).ayoRequest("edit_produk",MyVolley.POST, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)
        },data)

    }
    public fun getLoadMore(offset:String,listener: MoreDatListener){
        MyVolley(context).ayoRequest("produk/bylimitoffset/$offset",MyVolley.GET,
                AyoReqestListener {
                    response ->
                    try {
                        var json  = JSONObject(response)
                        var array = json.getJSONArray("data")
                        var listProduk:MutableList<Produk> = mutableListOf()
                        for(i in 0..array.length()-1){
                            var produkJson     = array.getJSONObject(i)
                            var produk         = Produk()
                            produk.idproduk    = produkJson.getString("id_produk")
                            produk.nama_produk = produkJson.getString("nama_produk")
                            produk.url         = produkJson.getString("url")
                            produk.kode_scan   = produkJson.getString("kode_scan")
                            produk.point       = produkJson.getString("point")
                            var status     = produkJson.getString("status")
                            if(status.equals("0")){
                                produk.status = "Belum discan";
                            }else{
                                produk.status = "Sudah discan";
                            }
                            produk.serial_number= produkJson.getString("serial_number")
                            produk.isSelected = false
                            listProduk.add(produk)
                        }
                        listener.setelahAmbilData(listProduk)

                    }catch (e:Exception){
                        Log.d("error",e.message)
                    }

                    //listener.setelahAmbilData(response)

                  },null)
    }
    public fun getJenisProduk(listener: MoreDatListener){
        MyVolley(context).ayoRequest("produk/jenisproduk",MyVolley.GET, AyoReqestListener {
            response ->
                    var list:MutableList<Jenis_Produk> = mutableListOf()
                    try {
                        var json = JSONObject(response)
                        var data = json.getJSONArray("data")
                        for (i in 0..data.length()-1){
                            var jenis = data.getJSONObject(i)
                            var obj = Jenis_Produk()
                            obj.id = jenis.getString("id_jenis")
                            obj.nama = jenis.getString("nama_jenis")
                            list.add(obj)
                        }

                        listener.setelahAmbilData(list)
                    }catch (e:Exception){

                    }

        },null)
    }
    public fun tambahjenisProduk(nama:String,listener: MoreDatListener){
        var data : MutableMap<String,String> = mutableMapOf()
        data.put("jenis_produk",nama)
        MyVolley(context).ayoRequest("produk/tambahjenis",MyVolley.POST, AyoReqestListener {
            response ->
            Log.d("tambah sudah ",response)
            listener.setelahAmbilData(response)

        },data)
    }
}