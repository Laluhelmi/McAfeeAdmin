package com.example.l.macprojectadmin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_iklan.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.os.Build
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.example.l.macprojectadmin.Adapter.ViewPagerAdapter
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.MyLib.UbahOrentasi
import com.example.l.macprojectadmin.MyLib.UploadFile
import com.example.l.macprojectadmin.Request.MyVolley
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class Iklan : AppCompatActivity() {

    private lateinit var listIklan:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iklan)
        getAllIklan()
        tambah.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                    0)
        })
        LocalBroadcastManager.getInstance(this).registerReceiver(br,IntentFilter("update_iklan"))
    }
    private var br = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            getAllIklan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && data != null){
            val selectedImage = data.data
            var file = File(selectedImage.path)

            var bitmap = UbahOrentasi(applicationContext).ubahOrenatasi(data.data)
            simpanGambar(bitmap,randomAlphaNumeric(10))
        }else if(requestCode == 1 && data!= null){
            val selectedImage = data.data
            var file = File(selectedImage.path)
            var bitmap = UbahOrentasi(applicationContext).ubahOrenatasi(data.data)
           // iklan1.setImageBitmap(bitmap)
            simpanGambar(bitmap,"iklan")
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected fun getBitmapSize(data: Bitmap): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            data.rowBytes * data.height
        } else {
            data.byteCount
        }
    }
    public fun tampiToast(kata:String){
        Toast.makeText(applicationContext,kata,Toast.LENGTH_SHORT).show()

    }

    private lateinit var bitmapdata:Bitmap
    public fun simpanGambar(bitmap: Bitmap,iklanKe: String){
        var cacheFolder = applicationContext.cacheDir
        var folder = File(cacheFolder.absolutePath+"/gambar")
        if(folder.exists() == false)
            folder.mkdirs()

        var file = File(cacheFolder.absolutePath+"/gambar",iklanKe+".jpeg")
        val out = FileOutputStream(file)
        try {
            if(getBitmapSize(bitmap) > 2000000){
               // var bitmap2 = Bitmap.createScaledBitmap(bitmap,648,1152,true)

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
                bitmapdata = bitmap
            }else{
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
                bitmapdata = bitmap
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        UploadFile(applicationContext).uploadIklan(iklanKe,bitmapdata,object : MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                getAllIklan()
            }
        })
    }
    public fun loadGambar(iklanKe: String) : Bitmap? {
        try {
            var file = File(application.cacheDir.absolutePath+"/gambar","$iklanKe.jpeg")
            var bitmap = BitmapFactory.decodeStream(FileInputStream(file))

            return bitmap
        }catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }
    public fun getLIstFolder():Array<File>{
        var file  = File(application.cacheDir.absolutePath+"/gambar")
        if(file.exists() == false){
            file.mkdirs()
        }
        var arrayFile = file.listFiles()
        return arrayFile
    }

    private val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    fun randomAlphaNumeric(count: Int): String {
        var count = count
        val builder = StringBuilder()
        while (count-- != 0) {
            val character = (Math.random() * ALPHA_NUMERIC_STRING.length).toInt()
            builder.append(ALPHA_NUMERIC_STRING[character])
        }
        return builder.toString()
    }
    fun getAllIklan(){
        var iklan:MutableList<String> = mutableListOf()
        listIklan = mutableListOf()
        MyVolley(this).ayoRequest("upload_iklan/getall",MyVolley.GET,
                AyoReqestListener {
                    response ->
                    try {
                        var json = JSONObject(response)
                        var aray = json.getJSONArray("data")
                        for(i in 0..aray.length()-1){
                            var js = aray.getJSONObject(i)
                            iklan.add(js.getString("url"))
                        }
                        //cocokkan
                        var isiFolder  = getLIstFolder()
                        var iklanskrng = isiFolder.toMutableList()
                        var iklanYangBelumAda:MutableList<String> = mutableListOf()
                        for(a in 0..iklan.size - 1){
                            var ketemu:Boolean = false
                            for(a2 in 0 .. isiFolder.size - 1){
                                if(isiFolder.get(a2).name.equals(iklan.get(a))){
                                    ketemu = true;
                                   // iklan.removeAt(a)
                                    var file  = File(
                                            application.cacheDir.absolutePath+"/gambar/"+
                                                    iklan.get(a))
                                    listIklan.add(file.name)
                                    break
                                }
                            }
                            if(ketemu == false)
                                iklanYangBelumAda.add(iklan.get(a))
                        }
                        for(hapus in 0..iklanskrng.size -1){

                        }
                        populate()
                        for(g in iklanYangBelumAda){
                            UploadFile(applicationContext).loadIklanFromserver(g,object : MoreDatListener{
                                override fun setelahAmbilData(data: Any) {
                                getAllIklan()
                                }
                            })
                        }
                       // hapusIklan(iklan,iklanskrng)

                    }catch (e:Exception){
                        Log.d("ada er",e.message)
                    }
                },null)
    }
    public fun populate(){
        var banyakPage = listIklan.size
        var myAdapter = ViewPagerAdapter(supportFragmentManager,banyakPage,
                listIklan);

        pager.adapter = myAdapter
        tablayout.setupWithViewPager(pager)
    }
    public fun hapusIklan(iklanServer:MutableList<String>,iklanLocal:MutableList<File>){
        for(a in 0.. iklanLocal.size - 1){
            var ketemu:Boolean = false
            for(a2 in 0 .. iklanServer.size - 1){
                if(iklanServer.get(a).equals(iklanLocal.get(a2).name)){
                    ketemu = true
                }
            }
            if(ketemu == false){
                iklanLocal.get(a).delete()
            }
        }
    }
}
