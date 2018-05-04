package com.example.l.macprojectadmin.Request

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.example.l.macprojectadmin.App.Http
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.MyLib.UploadFile
import com.example.l.macprojectadmin.Produk
import com.example.l.macprojectadmin.R
import org.json.JSONObject
import android.support.v4.content.ContextCompat.startActivity
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.content.FileProvider
import java.io.File
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build


/**
 * Created by L on 06/01/18.
 */
class ResellerRiwayat {

    private lateinit var contxt: Context

    constructor(context: Context) {
        this.contxt = context
    }

    public fun getAllRiwayat(email: String, listener: MoreDatListener) {
        MyVolley(contxt).ayoRequest("reseller/reselleractivity/$email",
                MyVolley.GET, AyoReqestListener { response ->
            try {
                listener.setelahAmbilData(response)
            } catch (e: Exception) {

            }

        }, null)
    }

    public fun sendRequestExcel(dari: String, ke: String, emailUser: String) {
        var data: MutableMap<String, String> = mutableMapOf()
        data.put("tmulai", dari)
        data.put("takhir", ke)
        data.put("email", emailUser)

        MyVolley(contxt).ayoRequest("produk/uploadbarang", MyVolley.POST, AyoReqestListener { response ->
            try {
                var json = JSONObject(response)
                var namafile = json.getString("nama_file")
                downloadFileExcel(namafile)
            } catch (e: Exception) {
            }
        }, data)
    }

    public fun downloadFileExcel(file: String) {
        var asintask = object : AsyncTask<Void, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                Toast.makeText(contxt, "Sedang Mendownload File", Toast.LENGTH_SHORT).show()
            }

            override fun doInBackground(vararg p0: Void?): String? {
                return UploadFile(contxt).downloadFile(file)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                notif(file)
                Toast.makeText(contxt, "Download Selesai ", Toast.LENGTH_SHORT).show()
            }
        }
        asintask.execute()
    }

    public fun notif(fileExcel: String) {
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/kimsfile/$fileExcel")
        val intent = Intent(Intent.ACTION_VIEW)
        var authoriti = contxt.applicationContext.packageName + ".com.example.l.macprojectadmin.provider"
        intent.setDataAndType(FileProvider.getUriForFile(contxt, authoriti,
                file)
                , "application/vnd.ms-excel")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        var intd = Intent.createChooser(intent, "Buka Excel")
        val pendingIntent = PendingIntent.getActivity(contxt, 0 /* Request code */, intd,
                PendingIntent.FLAG_CANCEL_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = contxt.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager!!.createNotificationChannel(notificationChannel);
        }
        val notificationBuilder = NotificationCompat.Builder(contxt)
                .setContentTitle("Download Selesai")
                .setContentText("File Excel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setChannelId("channelId")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)


        notificationManager!!.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}