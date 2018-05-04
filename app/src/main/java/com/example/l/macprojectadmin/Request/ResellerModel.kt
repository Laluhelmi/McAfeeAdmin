package com.example.l.macprojectadmin.Request

import android.content.Context
import android.util.Log
import com.example.l.macprojectadmin.Entity.Reseller
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import org.json.JSONObject

/**
 * Created by L on 04/01/18.
 */
class ResellerModel(context: Context) {
    private lateinit var context:Context

    init {
        this.context = context
    }
    public fun getData(listener: MoreDatListener){

        MyVolley(context).ayoRequest("reseller",MyVolley.GET,object : AyoReqestListener {
            override fun lakukanSesuatu(response: String?) {
                listener.setelahAmbilData(response.toString())
            }
        },null)

    }
    public fun loadMoreData(listener: MoreDatListener,offset:String){
        MyVolley(context).ayoRequest("reseller/bylimitOffset/$offset",MyVolley.GET,
                AyoReqestListener {
                    response ->
                    var dataReseller:MutableList<Reseller>
                    dataReseller = mutableListOf()
                    try {
                        var json = JSONObject(response)
                        var array = json.getJSONArray("data")
                        for(i in 0..array.length()-1){
                            var json2 = array.getJSONObject(i)
                            var reseller = Reseller()
                            reseller.nam_user = json2.getString("nama_user")
                            reseller.j_kelamin = json2.getString("j_kelamin")
                            reseller.no_rek = json2.getString("no_rek")
                            reseller.telp = json2.getString("telp")
                            reseller.nama_toko = json2.getString("nama_toko")
                            reseller.email = json2.getString("email")
                            reseller.alamat = json2.getString("alamat")
                            reseller.status = json2.getString("status")
                            dataReseller.add(reseller)
                        }

                    }catch (e:Exception){
                        Log.d("error json",e.message)
                    }
                    listener.setelahAmbilData(dataReseller)

                },null)
    }
    public fun hapusReseller(email:String,listener: MoreDatListener){
        MyVolley(context).ayoRequest("reseller/hapus/$email",MyVolley.GET, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)
        },null)

    }
    public fun aktifkanOrNonatifkan(email:String,listener: MoreDatListener,status:String){
        var data : MutableMap<String,String> = mutableMapOf()
        data.put("status",status)
        data.put("email",email)
        MyVolley(context).ayoRequest("reseller/nonaktif",MyVolley.POST, AyoReqestListener {
            response ->
            listener.setelahAmbilData(response)
        },data)

    }
}