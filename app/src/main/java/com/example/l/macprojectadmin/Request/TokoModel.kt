package com.example.l.macprojectadmin.Request

import android.content.Context
import android.util.Log
import com.example.l.macprojectadmin.Entity.Provinsi
import com.example.l.macprojectadmin.Entity.Wilayah
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Toko
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by L on 05/01/18.
 */
class TokoModel (context: Context){
    private lateinit var context:Context
    init {
        this.context = context
    }

    public fun getProvinsi(listener: MoreDatListener){
        MyVolley(context).ayoRequest("provinsi",MyVolley.GET,object : AyoReqestListener{
            override fun lakukanSesuatu(response: String?) {
                var provinsis:MutableList<Provinsi> = mutableListOf()
                try {
                    var json = JSONArray(response)

                    for(i in 0 .. json.length()-1){
                       var provinsi = Provinsi()
                       var jsondata = json.getJSONObject(i)
                       provinsi.nama = jsondata.getString("nama")
                       provinsi.id   = jsondata.getString("id")
                       provinsis.add(provinsi)
                    }

                    listener.setelahAmbilData(provinsis)

                }catch (e:Exception){}


            }
        },null)
    }
    public fun getWilayah(provinsi:String,listener: MoreDatListener){
        MyVolley(context).ayoRequest("provinsi/wilayah/$provinsi",MyVolley.GET,
              AyoReqestListener{
                    response ->
                        var wilayahLIST:MutableList<Toko.DataWilayah> = mutableListOf()
                        try {
                            var json = JSONArray(response)
                            for(i in 0..json.length()-1){
                                var obj = json.getJSONObject(i)
                                var nama = obj.getString("nama_wilayah")
                                var id   = obj.getString("id_wilayah")
                                var wilayah = Toko.DataWilayah(nama,id)
                              //  wilayah.id = id;wilayah.nama = nama
                                wilayahLIST.add(wilayah)
                            }
                            listener.setelahAmbilData(wilayahLIST)
                        }catch (e :Exception){
                            Log.d("ada error",e.message)
                        }

                },null)
    }
    public fun tambahKota(namakota:String,listener: MoreDatListener,idprovinsi:String){
        var data = mutableMapOf<String,String>()
        data.put("nama_wilayah",namakota)
        data.put("id",idprovinsi)
        MyVolley(context).ayoRequest("wilayah",MyVolley.POST, AyoReqestListener {
            response ->
                try {
                    listener.setelahAmbilData(response)
                }catch (e:Exception){}

        },data)
    }

}