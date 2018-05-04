package com.example.l.macprojectadmin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Request.MyVolley
import kotlinx.android.synthetic.main.activity_detail_toko.*
import org.json.JSONObject
import java.util.*

class DetailToko : AppCompatActivity() {
    lateinit var listBulan:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_toko)

        namaToko.setText(intent.getStringExtra("namatoko"))
        var idToko = intent.getStringExtra("id_toko")
        var cal = Calendar.getInstance()
        setListBulan()
        bulanjual.setText("Total Penjualan pada bulan "+listBulan.get(cal.get(Calendar.MONTH)))
        getData(idToko)
    }
    fun setListBulan(){
        listBulan = mutableListOf()
        listBulan.add("Januari")
        listBulan.add("Februari")
        listBulan.add("Maret")
        listBulan.add("April")
        listBulan.add("Mei")
        listBulan.add("Juni")
        listBulan.add("Juli")
        listBulan.add("Agustus")
        listBulan.add("September")
        listBulan.add("Oktober")
        listBulan.add("November")
        listBulan.add("Desember")

    }

    fun getData(idToko: String){
        MyVolley(this).ayoRequest("toko/tokodetail/$idToko",MyVolley.GET,AyoReqestListener{
            response ->
            try {
                var json = JSONObject(response)
                penjualan.setText(json.getString("total_penjualan"))
                penjualanbulanini.setText(json.getString("total_penjualan_bulan_ini"))

                var resellerList:MutableList<Reseller> = mutableListOf()
                for(i in 0..json.getJSONArray("reseller").length()-1){
                    var jsonarray = json.getJSONArray("reseller").
                                    getJSONObject(i)
                    var reseller = Reseller(jsonarray.getString("nama_user"),
                                            jsonarray.getString("total"),
                                            jsonarray.getString("email"))
                    resellerList.add(reseller)
                }
                if(resellerList.size == 0) {
                    nosales.visibility = View.VISIBLE
                }
                var adapter = ValidasiAdapter(applicationContext,resellerList)
                adapter.setAdapterInterface(object : AdapterInterface{
                    override fun setView(view: View?, position: Int) {
                        view?.findViewById<TextView>(R.id.nama_reseller)
                                ?.setText(resellerList.get(position).nama)
                        view?.findViewById<TextView>(R.id.total_penjualan)
                                ?.setText(resellerList.get(position).totalPejualan)

                    }
                },R.layout.item_detail_toko)
                listreseller.adapter = adapter
                listreseller.setOnItemClickListener(object : AdapterView.OnItemClickListener{
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        var intent = Intent(applicationContext,RiwayatReseller::class.java)
                        intent.putExtra("email_user",resellerList.get(p2).email)
                        startActivity(intent)
                    }
                })
            }catch (e:Exception){
                Log.d("terjadi errro broku",e.message)
            }

        },null)
    }
    class Reseller(nama:String,totalpenjualan:String,email:String){
         var nama:String =""
         var totalPejualan:String = ""
         var email:String = ""
        init {
            this.nama = nama
            this.totalPejualan = totalpenjualan
            this.email = email
        }
    }
}
