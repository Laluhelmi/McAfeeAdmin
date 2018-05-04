package com.example.l.macprojectadmin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Request.MyVolley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_cari_lokasi.*
import com.google.android.gms.maps.model.LatLngBounds
import org.json.JSONObject


class CariLokasi : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var mapstring:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_lokasi)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        carikota.setOnClickListener(View.OnClickListener {
            var kataKunci = carilokasi.text.toString()
            if(!kataKunci.equals(""))
                cariLokasi(kataKunci)
        })
        mapstring = intent.getStringExtra("map")

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        r1.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                toast("fdf")
                return true
            }
        })
        var latlangAwal = LatLng(-7.805868,
                110.369475)
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlangAwal, 0f))
       // var marker = mMap.addMarker(MarkerOptions().position(latlangAwal).title(""))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlangAwal))
        var previousZoomLevel:Float? = -1.0f
        var pcoordinate = mMap.cameraPosition.target
        pilihkotanya.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        mMap.setOnCameraMoveListener {
            /*var currentZoom = mMap.cameraPosition.zoom
            var coordinate = mMap.cameraPosition.target
            var latLngBounds:LatLngBounds

            if(previousZoomLevel != currentZoom){
                latLngBounds = LatLngBounds(
                        coordinate, coordinate)
            }else{
                latLngBounds = googleMap.projection
                        .visibleRegion.latLngBounds
            }
            mMap.setMinZoomPreference(0f)
            mMap.setLatLngBoundsForCameraTarget(latLngBounds)
            previousZoomLevel = mMap.cameraPosition.zoom*/
        }
    }
    fun toast(string:String){
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }/*
    val ADELAIDE = LatLngBounds(
            coordinate, coordinate)
    mMap.setLatLngBoundsForCameraTarget(ADELAIDE)*/
    fun cariLokasi(alamat:String){
            MyVolley(this).ayoRequestMap("https://maps.googleapis.com/maps" +
                "/api/geocode/json?address=$alamat&key=AIzaSyCQAVMFNOv6ba8M-ojdmY_5LY-aLagEkGE",
                MyVolley.GET, object : AyoReqestListener{
            override fun lakukanSesuatu(response: String?) {
                try {
                    var json =JSONObject(response)
                    var alamats = json.getJSONArray("results")
                            .getJSONObject(0).getString("formatted_address")
                    var geo = json.getJSONArray("results")
                            .getJSONObject(0).getJSONObject("geometry")
                    var lat = geo.getJSONObject("location").getString("lat")
                    var lang = geo.getJSONObject("location").getString("lng")
                    var newLatlang = LatLng(lat.toDouble(),lang.toDouble())
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatlang, 14.5f))
                    if(mapstring == "tambah"){
                        var intent = Intent("tambah_toko_map")
                        intent.putExtra("lat",lat)
                        intent.putExtra("lang",lang)
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    }else{
                        var intent = Intent("update_toko_map")
                        intent.putExtra("lat",lat)
                        intent.putExtra("lang",lang)
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    }
                }catch (e:Exception){
                    toast(e.message!!)
                }
            }
        },null)
    }
}
