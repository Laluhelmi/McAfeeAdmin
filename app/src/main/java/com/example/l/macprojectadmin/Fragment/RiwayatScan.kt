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
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.R
import org.json.JSONArray

class RiwayatScan : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.activity_riwayat_claim,container,false)
        var data = arguments.getString("data")
        try {
            var json = JSONArray(data)
            var alldata:MutableList<Scan> = mutableListOf()
            for(i in 0..json.length()-1){
                var datajson = json.getJSONObject(i)
                var scan = Scan()
                scan.kodeqr = datajson.getString("kode_scan")
                scan.tanggal = datajson.getString("tgl_scan")
                alldata.add(scan)
            }
            if(alldata.size == 0){
                var no = view?.findViewById<TextView>(R.id.noclaim)
                no?.visibility = View.VISIBLE
                no?.setText("Tidak ada Riwayat Scan")
            }
            var adapter = ValidasiAdapter(context,alldata)
            adapter.setAdapterInterface(AdapterInterface { view, position ->
                view.findViewById<TextView>(R.id.kodeqr).setText(": "+alldata.get(position).kodeqr)
                view.findViewById<TextView>(R.id.tglscan).setText(": "+alldata.get(position).tanggal)
            },R.layout.riwayat_scan_item)
            view?.findViewById<ListView>(R.id.list)?.adapter = adapter

        }catch (e:Exception){
            Log.d("error terjadi",e.message)
        }


        return  view

    }
    class Scan{
        public lateinit var tanggal:String
        public lateinit var kodeqr:String
    }
}
