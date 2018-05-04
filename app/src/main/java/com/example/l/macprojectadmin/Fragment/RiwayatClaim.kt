package com.example.l.macprojectadmin.Fragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.R
import kotlinx.android.synthetic.main.activity_riwayat_claim.*
import org.json.JSONArray
import org.json.JSONObject

class RiwayatClaim : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.activity_riwayat_claim,container,false)
        var listview =
                view?.findViewById<ListView>(R.id.list)
        var data = arguments.getString("data")
        try {
            var array = JSONArray(data)
            var hadiahListL:MutableList<Hadiah> = mutableListOf()
            for(i in 0..array.length()-1){
                var json = array.getJSONObject(i)
                var hadiah = Hadiah()
                hadiah.jenis = json.getString("tipe")
                hadiah.nominal = json.getString("nama_hadiah")
                hadiah.tanggal = json.getString("tgl_klaim")
                hadiahListL.add(hadiah)
            }
            if(hadiahListL.size == 0){
                var no = view?.findViewById<TextView>(R.id.noclaim)
                no?.visibility = View.VISIBLE

            }
            var adapter = ValidasiAdapter(context,hadiahListL)
            adapter.setAdapterInterface(object :AdapterInterface{
                override fun setView(view: View?, position: Int) {
                    view?.findViewById<TextView>(R.id.jenis)?.setText(hadiahListL[position].jenis)
                    view?.findViewById<TextView>(R.id.nominal)?.setText(hadiahListL[position].nominal)
                    view?.findViewById<TextView>(R.id.tggal)?.setText(hadiahListL[position].tanggal)
                }
            },R.layout.item_riwayat_hadiah)
            listview?.adapter = adapter
        }catch (e:Exception){
            Log.d("duh error",e.message)

        }


        return  view
    }
    class Hadiah{
        public lateinit var jenis:String
        lateinit var nominal:String
        lateinit var tanggal:String
    }

}
