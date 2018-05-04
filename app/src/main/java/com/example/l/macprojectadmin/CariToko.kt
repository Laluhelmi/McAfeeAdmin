package com.example.l.macprojectadmin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Entity.Provinsi
import com.example.l.macprojectadmin.Entity.TokoEntity
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Request.MyVolley
import com.example.l.macprojectadmin.Request.TambahTokoRequest
import com.example.l.macprojectadmin.Request.TokoModel
import kotlinx.android.synthetic.main.activity_cari_toko.*
import kotlinx.android.synthetic.main.activity_toko.*
import kotlinx.android.synthetic.main.dialog_excel.*
import org.json.JSONObject

class CariToko : AppCompatActivity() {
    lateinit var data:MutableList<TokoEntity>
    var itemposisi:Int = -1
    var idWilayahDipilih:String = ""
    private var listProvinsi:MutableList<Provinsi>   = mutableListOf()
    private lateinit var wilayahTextvie:TextView
    private var currentKey:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_toko)
        caritoko.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loading.visibility = View.VISIBLE
                Log.d("tulisan",p0.toString())
                    getData(p0.toString())
                currentKey = p0.toString()
            }
        })
        lihattoko.setOnClickListener(View.OnClickListener {
            dialog(data.get(itemposisi),object : Dosomething {
                override fun doit(param: Any?, dialog: Dialog) {
                    param as TokoEntity
                    dialog.findViewById<TextView>(R.id.namaToko).setText(param.nama_toko)
                    dialog.findViewById<TextView>(R.id.alamat).setText(param.alamat)
                    dialog.findViewById<TextView>(R.id.notelp).setText(param.no_telp)
                    dialog.findViewById<TextView>(R.id.wilayah).setText(param.nama_wilayah)
                    dialog.findViewById<TextView>(R.id.latitude).setText(param.lat)
                    dialog.findViewById<TextView>(R.id.longitude).setText(param.lang)
                    dialog.findViewById<TextView>(R.id.detail)
                            .setOnClickListener(View.OnClickListener {
                                var intent = Intent(applicationContext,DetailToko::class.java)
                                intent.putExtra("namatoko",data.get(itemposisi).nama_toko)
                                intent.putExtra("id_toko",data.get(itemposisi).idtoko)
                                startActivity(intent)
                            })
                }

            },R.layout.dialog_liat_toko)
        })
        edittoko.setOnClickListener(View.OnClickListener { 
            dialog(data.get(itemposisi),dialogUpdateListener(),R.layout.dialog_toko)
        })
        hapustoko.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@CariToko)
            builder.setMessage("Apakah Anda Yakin Menghapus Toko?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                TambahTokoRequest(applicationContext).deleteToko(data.get(itemposisi).idtoko,
                        object :MoreDatListener{
                            override fun setelahAmbilData(datas: Any) {
                                getData(currentKey)
                                //showToast("Toko Sudah Dihapus")
                            }
                        })
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.cancel() }
            builder.create().show()

        })

    }
    public fun showButtonToolbar(param : Boolean){
        if(param){
            hapustoko.visibility = View.VISIBLE
            lihattoko.visibility = View.VISIBLE
            caritoko.visibility = View.GONE
            edittoko.visibility = View.VISIBLE
        }else{
            edittoko.visibility = View.GONE
            caritoko.visibility = View.VISIBLE
            hapustoko.visibility = View.GONE
            lihattoko.visibility = View.GONE
        }
    }

    fun getData(key:String){
        loading.visibility = View.VISIBLE
        var param:MutableMap<String,String> = mutableMapOf()
        param.put("key",key)
        MyVolley(this).ayoRequest("toko/cari",MyVolley.POST,
                AyoReqestListener {
                    response ->
                      data = mutableListOf()
                      loading.visibility = View.GONE
                    Log.d("resp",response)
                    try {
                        var json = JSONObject(response)
                        var array= json.getJSONArray("data")
                        for(i in 0..array.length()-1){
                            var json2 = array.getJSONObject(i)
                            var toko = TokoEntity()
                            toko.idtoko = json2.getString("id_toko")
                            toko.nama_toko = json2.getString("nama_toko")
                            toko.alamat = json2.getString("alamat")
                            toko.nama_wilayah = json2.getString("nama_wilayah")
                            toko.no_telp = json2.getString("no_telp")
                            toko.lat = json2.getString("lat")
                            toko.idwilayah = json2.getString("id_wilayah")
                            toko.lang = json2.getString("lng")
                            //t/oko.nama_wilayah =
                            data.add(toko)
                        }
                    var adapter = ValidasiAdapter(applicationContext,data)
                        adapter.setAdapterInterface(object : AdapterInterface{
                            override fun setView(view: View?, position: Int) {
                                var namatoko = view?.findViewById<TextView>(R.id.namatoko)
                                var alamat    = view?.findViewById<TextView>(R.id.alamattoko)
                                namatoko?.setText(data.get(position).nama_toko)
                                alamat?.setText(data.get(position).alamat)
                                if(position == itemposisi){
                                    view?.findViewById<RelativeLayout>(R.id.body1)
                                            ?.setBackgroundColor(Color.parseColor("#e5e5e5"))
                                }
                            }
                        },R.layout.item_toko)
                        hasil.adapter = adapter
                        hasil.setOnItemClickListener(object : AdapterView.OnItemClickListener{
                            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                               /* var intent = Intent(applicationContext,DetailToko::class.java)
                                intent.putExtra("namatoko",data.get(p2).nama_toko)
                                intent.putExtra("id_toko",data.get(p2).idtoko)
                                startActivity(intent)*/

                                if(itemposisi != p2){
                                    showButtonToolbar(true)
                                    itemposisi = p2
                                }else{
                                    showButtonToolbar(false)
                                    itemposisi = -1
                                }
                                adapter.notifyDataSetChanged()
                            }
                        })

                    }catch (e:Exception){
                    Toast.makeText(applicationContext,e.message,Toast.LENGTH_SHORT).show()
                    }

                },param)


    }
    public fun dialog(data: Any?, doit: Dosomething, layout:Int){
        var dialog = Dialog(this)
        //dialog.setCancelable()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        doit.doit(data,dialog)

        dialog.show()
    }
    public interface Dosomething{
        public fun doit(param : Any?,dialog: Dialog)
    }
    public fun dialogUpdateListener(): Dosomething {
        return object : Dosomething {
            override fun doit(param: Any?, dialog: Dialog) {
                var toko : TokoEntity = param as TokoEntity
                dialog.findViewById<TextView>(R.id.namatokos).setText(toko.nama_toko)
                dialog.findViewById<TextView>(R.id.alamat).setText(toko.alamat)
                dialog.findViewById<TextView>(R.id.notelp).setText(toko.no_telp)
                dialog.findViewById<TextView>(R.id.wilayah).setText(toko.nama_wilayah)
                dialog.findViewById<TextView>(R.id.latitude).setText(toko.lat)
                dialog.findViewById<TextView>(R.id.longitude).setText(toko.lang)
                idWilayahDipilih = toko.idwilayah
                wilayahTextvie = dialog.findViewById(R.id.wilayah)
                dialog.findViewById<EditText>(R.id.wilayah).setOnClickListener(View.OnClickListener {
                    DialogProvinsi("")
                })
                dialog.findViewById<Button>(R.id.simpan).setOnClickListener(View.OnClickListener {
                    var datas : MutableMap<String,String> = mutableMapOf()
                    datas.put("id",toko.idtoko)
                    datas.put("nama_toko",dialog.findViewById<TextView>(R.id.namatokos).text.toString())
                    datas.put("alamat",dialog.findViewById<TextView>(R.id.alamat).text.toString())
                    datas.put("telp",dialog.findViewById<TextView>(R.id.notelp).text.toString())
                    datas.put("lat",dialog.findViewById<TextView>(R.id.latitude).text.toString())
                    datas.put("lng",dialog.findViewById<TextView>(R.id.longitude).text.toString())
                    datas.put("id_wilayah",idWilayahDipilih)
                    var longitude = dialog.findViewById<TextView>(R.id.longitude).text.toString()
                    var latitude = dialog.findViewById<TextView>(R.id.latitude).text.toString()
                    TambahTokoRequest(applicationContext)
                            .updateToko(toko.idtoko,datas,object : MoreDatListener {
                                override fun setelahAmbilData(datany: Any) {
                                  //
                                    getData(currentKey)
                                    dialog.dismiss()
                                }
                            })
                })
            }
        }
    }
    public fun DialogProvinsi(kosng:String){
        dialog(listProvinsi,object : Dosomething {
            override fun doit(param: Any?, dialog: Dialog) {
                var listview:ListView = dialog.findViewById(R.id.listwilayah)
                var loading:ProgressBar = dialog.findViewById(R.id.loading)
                listview.setOnItemClickListener(OnItemClick(dialog))
                if(listProvinsi.size == 0){
                    DialogProvinsi(listview,loading)
                }else{
                    loading.visibility = View.GONE
                    val jkAdapter = ArrayAdapter<String>(this@CariToko,
                            R.layout.support_simple_spinner_dropdown_item,
                            provinsiToString(listProvinsi))
                    listview.adapter = jkAdapter
                }
            }
        },R.layout.dialog_wilayah)
    }
    public fun DialogProvinsi(listView: ListView,loading:ProgressBar){
        TokoModel(applicationContext).getProvinsi(object :MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                data as MutableList<Provinsi>
                listProvinsi = data
                val jkAdapter = ArrayAdapter<String>(this@CariToko,
                        R.layout.support_simple_spinner_dropdown_item,
                        provinsiToString(listProvinsi))
                listView.adapter = jkAdapter
                loading.visibility = View.GONE
            }
        })
    }
    public fun provinsiToString(data:MutableList<Provinsi>):MutableList<String>{
        var hasil : MutableList<String> = mutableListOf()
        for(i in 0.. data.size-1){
            hasil.add(data.get(i).nama)
        }
        return hasil
    }
    private fun OnItemClick(dialog: Dialog):AdapterView.OnItemClickListener{
        return object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getwilayahFromProvensi(listProvinsi.get(p2).id,dialog)
            }
        }
    }
    public fun getwilayahFromProvensi(provinsi:String,dialogParent: Dialog){
        var data:MutableList<Toko.DataWilayah> = mutableListOf()
        dialog(data,object : Dosomething {
            override fun doit(param: Any?, dialog: Dialog) {
                var lisview:ListView = dialog.findViewById(R.id.listwilayah)
                var loading:ProgressBar = dialog.findViewById(R.id.loading)
                TokoModel(applicationContext).getWilayah(provinsi,object : MoreDatListener{
                    override fun setelahAmbilData(wilayahlist: Any) {
                        data = wilayahlist as MutableList<Toko.DataWilayah>
                        loading.visibility = View.GONE
                        var dataArray = listToArray(data)
                        dataArray.add("Tambah Kota")
                        val jkAdapter = ArrayAdapter<String>(this@CariToko,
                                R.layout.support_simple_spinner_dropdown_item,
                                dataArray)
                        //tambah kata tambah toko
                        lisview.adapter = jkAdapter
                        lisview.setOnItemClickListener(object : AdapterView.OnItemClickListener{
                            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                if(p2 == dataArray.size - 1){
                                    dialog(null,object : Dosomething {
                                        override fun doit(param: Any?, dialog: Dialog) {
                                            var namakota = dialog.findViewById<TextView>(R.id.namakota)
                                            var button   = dialog.findViewById<Button>(R.id.simpankota)
                                            var loadingtmbah   = dialog.findViewById<ProgressBar>(R.id.loadintambah)
                                            button.setOnClickListener(View.OnClickListener {
                                                button.visibility = View.GONE
                                                loadingtmbah.visibility = View.VISIBLE
                                                TokoModel(applicationContext).tambahKota(namakota.text.toString(),
                                                        object : MoreDatListener{
                                                            override fun setelahAmbilData(d2: Any) {
                                                                TokoModel(applicationContext).getWilayah(provinsi,
                                                                        object :MoreDatListener{
                                                                            override fun setelahAmbilData(d: Any) {
                                                                                data = d as MutableList<Toko.DataWilayah>
                                                                                dataArray.clear()
                                                                                dataArray.addAll(listToArray(data))
                                                                                dataArray.add("Tambah Kota")
                                                                                jkAdapter.notifyDataSetInvalidated()
                                                                                dialog.dismiss()
                                                                            }
                                                                        })
                                                            }
                                                        },provinsi)
                                            })

                                        }
                                    },R.layout.dialog_tambah_kota)

                                }else{
                                        wilayahTextvie.setText(data.get(p2).nama)
                                        dialog.dismiss()
                                        dialogParent.dismiss()
                                        idWilayahDipilih = data.get(p2).id
                                }

                            }
                        })
                    }
                })
            }
        },R.layout.dialog_wilayah)
    }
    public fun listToArray(data : MutableList<Toko.DataWilayah>): MutableList<String>{
        var hasil : MutableList<String> = mutableListOf()
        for(i in 0.. data.size-1){
            hasil.add(data.get(i).nama)
        }

        return hasil
    }
}
