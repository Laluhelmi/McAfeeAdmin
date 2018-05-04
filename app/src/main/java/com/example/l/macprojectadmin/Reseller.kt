package com.example.l.macprojectadmin

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Entity.Reseller
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Request.ResellerModel
import com.example.l.macprojectadmin.Request.TambahTokoRequest
import kotlinx.android.synthetic.main.activity_reseller.*
import kotlinx.android.synthetic.main.activity_validasi.*
import org.json.JSONObject

class Reseller : AppCompatActivity() {

    private lateinit var adapter:ValidasiAdapter
    private lateinit var dataReseller:MutableList<Reseller>
    private var posisiClick:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reseller)
        mencari.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Reseller,Cari_Reseller::class.java))
        })
        riwayatt.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@Reseller,RiwayatReseller::class.java)
            intent.putExtra("email_user",dataReseller.get(posisiClick).email);
            startActivity(intent)
        })
        showNavigator(false)
        ResellerModel(this@Reseller).getData(object : MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                loading.visibility = View.GONE
                dataReseller = mutableListOf()
                try {
                    var json = JSONObject(data.toString())
                    var jsonArray = json.getJSONArray("hasil")

                    for( i in 0..jsonArray.length()-1){
                        var json = jsonArray.getJSONObject(i)
                        var reseller = Reseller()
                        reseller.alamat = json.getString("alamat")
                       // reseller.no_ktp = json.getString("no_ktp")
                        reseller.nam_user = json.getString("nama_user")
                        reseller.telp = json.getString("telp")
                        reseller.j_kelamin = json.getString("j_kelamin")
                        reseller.email = json.getString("email")
                        reseller.no_rek = json.getString("no_rek")
                        reseller.nama_toko = json.getString("nama_toko")
                        reseller.alamat = json.getString("alamat")
                        reseller.status = json.getString("status")
                        dataReseller.add(reseller)
                    }

                        var rs = Reseller()
                        dataReseller.add(rs)

                    buaListview()

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })

        listreseller.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(posisiClick != p2){
                    posisiClick = p2
                    showNavigator(true)
                    adapter.notifyDataSetChanged()
                    var status = dataReseller.get(posisiClick).status
                    if(status.equals("Aktif")){
                        aktif.setText("Non-Aktifkan")
                    }else{
                        aktif.setText("Aktifkan")
                    }
                }else{
                    aktif.setText("Aktifkan")
                    posisiClick = -1
                    showNavigator(false)
                    adapter.notifyDataSetChanged()
                }

            }
        })
        lihat.setOnClickListener(View.OnClickListener {
            tampilDetail(posisiClick)
        })
        hapus.setOnClickListener(View.OnClickListener {
            view ->
            val builder = AlertDialog.Builder(this@Reseller)
            builder.setMessage("Apakah Anda Yakin ?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                var email = dataReseller.get(posisiClick).email
                ResellerModel(this@Reseller).hapusReseller(email,object : MoreDatListener{
                    override fun setelahAmbilData(data: Any) {
                        dataReseller.removeAt(posisiClick)
                        posisiClick = -1
                        showNavigator(false)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.cancel() }
            builder.create().show()
        })
        aktif.setOnClickListener(View.OnClickListener {
            var email = dataReseller.get(posisiClick).email
            var status = dataReseller.get(posisiClick).status
            var statusPost:String = ""
            var pesan :String = ""
            if(status.equals("Aktif")){
                statusPost = "Non-Aktif"
                pesan = "Non-Aktifkan"
            }else{
                pesan = "Aktifkan"
                statusPost = "Aktif"
            }
            val builder = AlertDialog.Builder(this@Reseller)
            builder.setMessage("Apakah Anda Yakin $pesan Reseller ini?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                if(status.equals("Aktif")){
                    aktif.setText("Aktif")
                }else{
                    aktif.setText("Non-Aktifkan")
                }
                ResellerModel(this@Reseller).aktifkanOrNonatifkan(email,object : MoreDatListener{
                    override fun setelahAmbilData(data: Any) {
                        dataReseller.get(posisiClick).status = statusPost
                        adapter.notifyDataSetChanged()
                    }
                },statusPost)
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.cancel() }
            builder.create().show()
        })
    }

    public fun showToast(kata: String){
        Toast.makeText(this@Reseller,kata,Toast.LENGTH_SHORT).show()
    }
    public fun buaListview(){
        adapter = ValidasiAdapter(this@Reseller,dataReseller)
        var isDataHabis:Boolean = false
        var isLoadFinish = true
        var offset = 15
        adapter.setAdapterInterface(object : AdapterInterface{
            override fun setView(view: View?, position: Int) {
                var gambar:ImageView? = view?.findViewById(R.id.gambar)
                var nama:TextView? = view?.findViewById(R.id.nama)
                var email:TextView? = view?.findViewById(R.id.email)
                var card:CardView? = view?.findViewById(R.id.card)
                var loading:ProgressBar? = view?.findViewById(R.id.loadingitem)
                nama?.setText(dataReseller.get(position).nam_user)
                email?.setText(dataReseller.get(position).email)
                loading?.visibility = View.GONE
                if(position == posisiClick){
                    card?.setBackgroundColor(Color.parseColor("#d1d1d1"))
                }else{
                    card?.setBackgroundColor(Color.parseColor("#ffffff"))
                }
                if(isDataHabis == false && dataReseller[position].alamat == null && isLoadFinish
                    == true
                        ){
                    loading?.visibility = View.VISIBLE
                    gambar?.visibility = View.GONE
                    isLoadFinish = false
                    ResellerModel(this@Reseller).loadMoreData(object :MoreDatListener{
                        override fun setelahAmbilData(data: Any) {
                            dataReseller.removeAt(position)
                            data as MutableList<Reseller>
                        if(data.size > 0){
                            dataReseller.addAll(data)
                            var kosong = Reseller()
                            dataReseller.add(kosong)
                            offset += 15
                        }else{
                            isDataHabis = true
                        }
                            isLoadFinish = true
                        adapter.notifyDataSetChanged()
                        }
                    },offset.toString())
                }
            }
        },R.layout.reseller_item)
        listreseller.adapter = adapter
    }
    public fun showNavigator(aktif:Boolean){
        if(aktif == false){
            navigator.visibility = View.GONE
            mencari.visibility = View.VISIBLE
        }else{
            mencari.visibility = View.GONE
            navigator.visibility = View.VISIBLE
        }
    }
    public fun tampilDetail(position:Int){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_reseller)
        //dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
       // dialog.findViewById<TextView>(R.id.noktp).setText(dataReseller.get(position).no_ktp)
        dialog.findViewById<TextView>(R.id.nama).setText(dataReseller.get(position).nam_user)
        dialog.findViewById<TextView>(R.id.jeniskelamin).setText(dataReseller.get(position).j_kelamin)
        dialog.findViewById<TextView>(R.id.no_rekening).setText(dataReseller.get(position).no_rek)
        if(dataReseller.get(position).no_rek == null)
         dialog.findViewById<TextView>(R.id.no_rekening).setText(dataReseller.get(position).no_rek)
        dialog.findViewById<TextView>(R.id.telp).setText(dataReseller.get(position).telp)
        dialog.findViewById<TextView>(R.id.email).setText(dataReseller.get(position).email)
        dialog.findViewById<TextView>(R.id.namatoko).setText(dataReseller.get(position).nama_toko)
        dialog.findViewById<TextView>(R.id.ktp).setText(dataReseller.get(position).alamat)
        dialog.findViewById<Button>(R.id.riwayat).setOnClickListener(View.OnClickListener {
            var intent = Intent(this@Reseller,RiwayatReseller::class.java)
            intent.putExtra("email_user",dataReseller.get(position).email);
            startActivity(intent)
        })
        dialog.show()
    }
}
