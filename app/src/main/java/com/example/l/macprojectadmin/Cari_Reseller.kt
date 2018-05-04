package com.example.l.macprojectadmin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Entity.Reseller
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Request.MyVolley
import com.example.l.macprojectadmin.Request.ResellerModel
import kotlinx.android.synthetic.main.activity_cari__reseller.*
import kotlinx.android.synthetic.main.activity_reseller.*
import kotlinx.android.synthetic.main.activity_toko.*
import org.json.JSONObject

class Cari_Reseller : AppCompatActivity() {
    var posisi:Int = -1
    lateinit var  data:MutableList<Reseller>
    lateinit var adapter:ValidasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari__reseller)
        riwayat3.setOnClickListener(View.OnClickListener {
            var intent = Intent(applicationContext,RiwayatReseller::class.java)
            intent.putExtra("email_user",data.get(posisi).email);
            startActivity(intent)
        })
        carires.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cariReseller(p0.toString())
            }
        })
        lihat2.setOnClickListener(View.OnClickListener {
            tampilDetail(posisi)
        })
        aktif2.setOnClickListener(View.OnClickListener {
            var email = data.get(posisi).email
            var status = data.get(posisi).status
            var statusPost:String = ""
            if(status.equals("Aktif")){
                statusPost = "Non-Aktif"
                aktif2.setText("Aktif")
            }else{
                aktif2.setText("Nonaktifkan")
                statusPost = "Aktif"
            }
            ResellerModel(applicationContext).aktifkanOrNonatifkan(email,object : MoreDatListener {
                override fun setelahAmbilData(datad: Any) {

                    data.get(posisi).status = statusPost
                    adapter.notifyDataSetChanged()

                }
            },statusPost)
        })
        hapus2.setOnClickListener(View.OnClickListener {
            view ->
            val builder = AlertDialog.Builder(this@Cari_Reseller)
            builder.setMessage("Apakah Anda Yakin ?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                var email = data.get(posisi).email
                ResellerModel(applicationContext).hapusReseller(email,object : MoreDatListener{
                    override fun setelahAmbilData(datad: Any) {
                        data.removeAt(posisi)
                        posisi = -1
                        showNavigator(false)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.cancel() }
            builder.create().show()
        })
    }
  /*  public fun showButtonToolbar(param : Boolean){
        if(param){
            hapus.visibility = View.VISIBLE
            lihat.visibility = View.VISIBLE
            cari.visibility = View.GONE
            ///tambahtoko.setText("Ubah")
        }else{
            cari.visibility = View.VISIBLE
            hapus.visibility = View.GONE
            lihat.visibility = View.GONE
        }
    }*/
    fun cariReseller(key:String){
        loading2.visibility = View.VISIBLE
        MyVolley(this).ayoRequest("reseller/cari/$key",MyVolley.GET,
                AyoReqestListener {
                    response ->
                    loading2.visibility = View.GONE
                     data = mutableListOf()
                    try {
                        var json = JSONObject(response)
                        var array = json.getJSONArray("data")
                        for(i in 0..array.length()-1){
                            var reseller = Reseller()
                            json = array.getJSONObject(i)
                            reseller.alamat = json.getString("alamat")
                            reseller.nam_user = json.getString("nama_user")
                            reseller.telp = json.getString("telp")
                            reseller.j_kelamin = json.getString("j_kelamin")
                            reseller.email = json.getString("email")
                            reseller.nama_toko = json.getString("nama_toko")
                            reseller.status = json.getString("status")
                            data.add(reseller)
                        }
                        adapter = ValidasiAdapter(applicationContext,data)
                        adapter.setAdapterInterface(object : AdapterInterface{
                            override fun setView(view: View?, position: Int) {
                                var card: CardView? = view?.findViewById(R.id.card)
                                var nama: TextView? = view?.findViewById(R.id.nama)
                                var email: TextView? = view?.findViewById(R.id.email)
                                nama?.setText(data.get(position).nam_user)
                                email?.setText(data.get(position).email)
                                view?.findViewById<ProgressBar>(R.id.loadingitem)?.visibility = View.GONE
                                if(position == posisi){
                                    var card:CardView? = view?.findViewById(R.id.card)
                                    card?.setBackgroundColor(Color.parseColor("#d1d1d1"))
                                }else{
                                    card?.setBackgroundColor(Color.parseColor("#ffffff"))
                                }
                            }
                        },R.layout.reseller_item)
                    listreseller1.adapter = adapter
                        listreseller1.setOnItemClickListener(object :AdapterView.OnItemClickListener{
                            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                if(posisi != p2){
                                    posisi = p2
                                    showNavigator(true)
                                    var status = data.get(posisi).status
                                    if(status.equals("Aktif")){
                                        aktif2.setText("Nonaktifkan")
                                    }else{
                                        aktif2.setText("Aktifkan")
                                    }
                                    adapter.notifyDataSetChanged()
                                }else{
                                    adapter.notifyDataSetChanged()
                                    posisi = -1
                                    showNavigator(false)

                                }
                            }
                        })
                    }catch (e:Exception){
                    Toast.makeText(applicationContext,e.message,Toast.LENGTH_SHORT).show()
                    }
                },null)
    }
    public fun showNavigator(aktif:Boolean){
        if(aktif == false){
            navigator2.visibility = View.GONE
            carires.visibility = View.VISIBLE
        }else{
            carires.visibility = View.GONE
            navigator2.visibility = View.VISIBLE
        }
    }
    public fun tampilDetail(position:Int){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_reseller)
        //dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        //dialog.findViewById<TextView>(R.id.noktp).setText(data.get(position).no_ktp)
        dialog.findViewById<TextView>(R.id.nama).setText(data.get(position).nam_user)
        dialog.findViewById<TextView>(R.id.jeniskelamin).setText(data.get(position).j_kelamin)
        dialog.findViewById<TextView>(R.id.no_rekening).setText(data.get(position).no_rek)
        if(data.get(position).no_rek == null)
            dialog.findViewById<TextView>(R.id.no_rekening).setText("-")
        dialog.findViewById<TextView>(R.id.telp).setText(data.get(position).telp)
        dialog.findViewById<TextView>(R.id.email).setText(data.get(position).email)
        dialog.findViewById<TextView>(R.id.namatoko).setText(data.get(position).nama_toko)
        dialog.findViewById<Button>(R.id.riwayat).setOnClickListener(View.OnClickListener {
            var intent = Intent(applicationContext,RiwayatReseller::class.java)
            intent.putExtra("email_user",data.get(position).email);
            startActivity(intent)
        })
        dialog.show()
    }
}
