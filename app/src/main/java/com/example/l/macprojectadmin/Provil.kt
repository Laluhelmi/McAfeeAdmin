package com.example.l.macprojectadmin

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.l.macprojectadmin.App.AppSession
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Request.MyVolley
import kotlinx.android.synthetic.main.activity_provil.*
import kotlinx.android.synthetic.main.dialog_ubah_password.*
import kotlinx.android.synthetic.main.item_claim.*
import org.json.JSONObject

class Provil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provil)
        username.setText(AppSession(this).nama)
        email.setText(AppSession(this).username)
        keluar.setOnClickListener(View.OnClickListener {
            AppSession(applicationContext).keluar()
            startActivity(Intent(applicationContext,LoginAdmin::class.java))
            finish()
        })
        ubah.setOnClickListener(View.OnClickListener {
            var nama = username.text.toString()
            AppSession(applicationContext).nama = nama
        })
        ubahpassword.setOnClickListener(View.OnClickListener {
            var dialog = Dialog(this@Provil)
            //dialog.setCancelable()
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_ubah_password)
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            var passwrodlama = dialog.findViewById<EditText>(R.id.passwordlama)
            var passwordbaru = dialog.findViewById<EditText>(R.id.passwordbaru)
            var konfirmasipasswordbaru = dialog.findViewById<EditText>(R.id.konfirmasipasswordbaru)
            var simpan = dialog.findViewById<Button>(R.id.simpanubah)
            simpan.setOnClickListener(View.OnClickListener {
                if(passwordbaru.text.toString().equals(konfirmasipasswordbaru.text.toString())){
                    var getpasslama = passwrodlama.text.toString()
                    var getpassbaru = passwordbaru.text.toString()
                    gantiPassword(getpasslama,getpassbaru)
                    dialog.dismiss()
//                    gantiPassword(passwordlama.text.toString(),passwordbaru.text.toString())
                }else{
                    Toast.makeText(applicationContext,"Password Baru Tidak sama",Toast.LENGTH_SHORT).show()
                }

            })
            dialog.show()
        })
    }
    public fun gantiPassword(passwordlama:String,passwordBaru:String){
        var data:MutableMap<String,String> = mutableMapOf()
            data.put("pass_lama",passwordlama)
            data.put("password",passwordBaru)
            data.put("email"    ,AppSession(this).email)

        MyVolley(this).ayoRequest("forgot/gantipass",MyVolley.POST, AyoReqestListener {
            response ->try {
                    var json = JSONObject(response)
                    if(json.getString("status").equals("sukses")){
                        Toast.makeText(applicationContext,"Password Berhasil diUbah",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,json.getString("status"),Toast.LENGTH_SHORT).show()
                    }

            }  catch (e:Exception){}

        },data)
    }
}
