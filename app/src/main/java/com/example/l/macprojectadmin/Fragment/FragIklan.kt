package com.example.l.macprojectadmin.Fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.MyLib.UbahOrentasi
import com.example.l.macprojectadmin.MyLib.UploadFile
import com.example.l.macprojectadmin.R
import com.example.l.macprojectadmin.Request.MyVolley
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FragIklan : Fragment(){

    private lateinit var tombol:Button
    private lateinit var image:ImageView
    lateinit var gambarPath:String
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.activity_frag_iklan,container,false)
         gambarPath = arguments.getString("gambarpath")
        image = view!!.findViewById(R.id.foto)
        image.setImageBitmap(loadGambar(gambarPath))

        var hapus = view?.findViewById<Button>(R.id.hapus)
        hapus.setOnClickListener(View.OnClickListener {
            var file = File(context.cacheDir.absolutePath+"/gambar",gambarPath)
            if(file.delete()){}
            hapusIklanRequest()
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("update_iklan"))
        })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && data != null){
            val selectedImage = data.data
            var file = File(selectedImage.path)
            var bitmap = UbahOrentasi(context).ubahOrenatasi(data.data)
            image.setImageBitmap(bitmap)
            simpanGambar(bitmap,gambarPath)
        }
    }
    public fun loadGambar(iklanKe: String) : Bitmap? {
        try {
            var file = File(context.cacheDir.absolutePath+"/gambar",iklanKe)
            var bitmap = BitmapFactory.decodeStream(FileInputStream(file))

            return bitmap
        }catch (e : Exception){
            Log.d("error file",e.message)
        }
        return null
    }
    public fun simpanGambar(bitmap: Bitmap,iklanKe: String){
        var cacheFolder = context.cacheDir
        var folder = File(cacheFolder.absolutePath+"/gambar")
        if(folder.exists() == false)
            folder.mkdirs()

        var file = File(cacheFolder.absolutePath+"/gambar",iklanKe)
        val out = FileOutputStream(file)
        try {

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
           // bitmapdata = bitmap

        }catch (e:Exception){
            e.printStackTrace()
        }
       // UploadFile(applicationContext).uploadIklan(iklanKe,bitmapdata)
    }
    public fun hapusIklanRequest(){
        Log.d("iklan diahpus",gambarPath)
        MyVolley(context).ayoRequest("upload_iklan/hapusiklan/$gambarPath",MyVolley.GET,AyoReqestListener{
        response ->
//            Toast.makeText(context,"Iklan Telah dihapus",Toast.LENGTH_SHORT).show()

        },null)
    }
}
