package com.example.l.macprojectadmin

import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.android.volley.Request
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Entity.Provinsi
import com.example.l.macprojectadmin.Entity.TokoEntity
import com.example.l.macprojectadmin.Entity.Wilayah
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.AyoReqestListener
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Interface.PilihWilayaListener
import com.example.l.macprojectadmin.Request.MyVolley
import com.example.l.macprojectadmin.Request.SetelahSimpanData
import com.example.l.macprojectadmin.Request.TambahTokoRequest
import com.example.l.macprojectadmin.Request.TokoModel
import kotlinx.android.synthetic.main.activity_toko.*
import org.json.JSONArray
import org.json.JSONObject

class Toko : AppCompatActivity() {

    private var totalData:Int = 0
    private var idWilayahDipilih:String = ""
    private var listWilayah:MutableList<DataWilayah> = mutableListOf()
    private var listProvinsi:MutableList<Provinsi>   = mutableListOf()
    private lateinit var data:MutableList<TokoEntity>
    private lateinit var adapter:ValidasiAdapter
    private var nonInput:Boolean = false
    private var wilayahIdselected: String=""
    var posisiClikc:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_toko)
       // window.statusBarColor = resources.getColor(R.color.colorPrimary)
        cari.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Toko,CariToko::class.java))
        })
        loadData("")
        tambahtoko.setOnClickListener(object :View.OnClickListener{
                    override fun onClick(view: View?) {
                        nonInput = false
                        if(lihat.visibility == View.VISIBLE){
                            var toko = data.get(posisiClikc)
                            dialog(toko,dialogUpdateListener(),R.layout.dialog_toko)
                        }else{
                            dialog(null,dialogTambahListener(),R.layout.dialog_toko)
                        }
                    }
                })
        listtoko.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(posisiClikc != p2){
                    posisiClikc = p2
                    adapter.notifyDataSetChanged()
                    showButtonToolbar(true)
                }else{
                    showButtonToolbar(false)
                    posisiClikc = -1
                    adapter.notifyDataSetChanged()
                }
            }
        })
        hapus.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@Toko)
            builder.setMessage("Apakah Anda Yakin Menghapus Toko?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                TambahTokoRequest(this@Toko).deleteToko(data.get(posisiClikc).idtoko,
                        object :MoreDatListener{
                            override fun setelahAmbilData(datas: Any) {
                                data.removeAt(posisiClikc)
                                posisiClikc = -1
                                adapter.notifyDataSetChanged()
                                showToast("Toko Sudah Dihapus")
                            }
                        })
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.cancel() }
            builder.create().show()

        })
        lihat.setOnClickListener(View.OnClickListener {
            dialog(data.get(posisiClikc),object :Dosomething{
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
                                var intent = Intent(this@Toko,DetailToko::class.java)
                                intent.putExtra("namatoko",data.get(posisiClikc).nama_toko)
                                intent.putExtra("id_toko",data.get(posisiClikc).idtoko)
                                startActivity(intent)
                            })
                }
            },R.layout.dialog_liat_toko)
        })
        navkota.setOnClickListener(View.OnClickListener {
            nonInput = true
           DialogProvinsi("")
        })
    }
    public fun showToast(kata:String){
        Toast.makeText(this@Toko,kata,
                Toast.LENGTH_SHORT).show()
    }
    public fun showButtonToolbar(param : Boolean){
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    public fun dialog(data: Any?, doit: Dosomething,layout:Int){
        var dialog = Dialog(this@Toko)
        //dialog.setCancelable()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        doit.doit(data,dialog)

        dialog.show()
    }
    private lateinit var wilayahTextvie:TextView
    public fun dialogUpdateListener():Dosomething{
        return object : Dosomething{
            override fun doit(param: Any?, dialog: Dialog) {
                var broadcastReceiver = object :BroadcastReceiver(){
                    override fun onReceive(p0: Context?, p1: Intent?) {
                        var lat = p1!!.getStringExtra("lat")
                        var lang= p1!!.getStringExtra("lang")
                        dialog.findViewById<TextView>(R.id.latitude).setText(lat)
                        dialog.findViewById<TextView>(R.id.longitude).setText(lang)
                    }
                }
                var toko : TokoEntity = param as TokoEntity
                dialog.findViewById<TextView>(R.id.namatokos).setText(toko.nama_toko)
                dialog.findViewById<TextView>(R.id.alamat).setText(toko.alamat)
                dialog.findViewById<TextView>(R.id.notelp).setText(toko.no_telp)
                dialog.findViewById<TextView>(R.id.wilayah).setText(toko.nama_wilayah)
                dialog.findViewById<TextView>(R.id.latitude).setText(toko.lat)
                dialog.findViewById<TextView>(R.id.longitude).setText(toko.lang)
                dialog.findViewById<TextView>(R.id.bukamap).setOnClickListener(View.OnClickListener {
                    var intentFilter = IntentFilter("update_toko_map")
                    LocalBroadcastManager.getInstance(this@Toko).registerReceiver(broadcastReceiver
                            ,intentFilter)
                    var i = Intent(this@Toko,CariLokasi::class.java)
                    i.putExtra("map","update")
                    startActivity(i)
                })
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
                    TambahTokoRequest(this@Toko)
                            .updateToko(toko.idtoko,datas,object : MoreDatListener{
                                override fun setelahAmbilData(datany: Any) {
                                    data.get(posisiClikc).idtoko = toko.idtoko
                                    data.get(posisiClikc).nama_toko =
                                            dialog.findViewById<TextView>(R.id.namatokos).
                                                    text.toString()
                                    data.get(posisiClikc).alamat =
                                            dialog.findViewById<TextView>(R.id.alamat).
                                                    text.toString()
                                    data.get(posisiClikc).no_telp =
                                            dialog.findViewById<TextView>(R.id.notelp).
                                                    text.toString()
                                    data.get(posisiClikc).idwilayah = wilayahTextvie.text.toString()
                                    data.get(posisiClikc).lang = longitude
                                    data.get(posisiClikc).lat = latitude
                                    data.get(posisiClikc).idwilayah = idWilayahDipilih
                                    adapter.notifyDataSetChanged()
                                    dialog.dismiss()
                                }
                            })
                })
            }
        }
    }
    public fun dialogTambahListener():Dosomething{

        return object : Dosomething{
            override fun doit(param: Any?, dialog: Dialog) {
                var namaToko = dialog.findViewById<TextView>(R.id.namatokos)
                wilayahTextvie = dialog.findViewById(R.id.wilayah)
                var intent = IntentFilter("tambah_toko_map")
                var broadcastReceiver = object :BroadcastReceiver(){
                    override fun onReceive(p0: Context?, p1: Intent?) {
                        var lat = p1!!.getStringExtra("lat")
                        var lang= p1!!.getStringExtra("lang")
                        dialog.findViewById<TextView>(R.id.latitude).setText(lat)
                        dialog.findViewById<TextView>(R.id.longitude).setText(lang)
                    }
                }
                dialog.findViewById<Button>(R.id.bukamap).setOnClickListener(View.OnClickListener {
                    LocalBroadcastManager.getInstance(this@Toko).registerReceiver(broadcastReceiver
                    ,intent)
                    var i = Intent(this@Toko,CariLokasi::class.java)
                        i.putExtra("map","tambah")
                    startActivity(i)
                })

                dialog.findViewById<Button>(R.id.simpan)
                        .setOnClickListener(View.OnClickListener {
                            var obj = TokoEntity()
                            obj.alamat = dialog.findViewById<TextView>(R.id.alamat).text.toString()
                            obj.no_telp = dialog.findViewById<TextView>(R.id.notelp).text.toString()
                            obj.idwilayah = wilayahTextvie.text.toString()
                            obj.lat    = dialog.findViewById<TextView>(R.id.latitude).text.toString()
                            obj.lang    = dialog.findViewById<TextView>(R.id.longitude).text.toString()
                            obj.nama_toko = namaToko.text.toString()
                            var datas : MutableMap<String,String> = mutableMapOf()
                            datas.put("nama_toko",namaToko.text.toString().toUpperCase())
                            datas.put("alamat",obj.alamat)
                            datas.put("telp",obj.no_telp)
                            datas.put("id_wilayah",idWilayahDipilih)
                            datas.put("lat",obj.lat)
                            datas.put("lng",obj.lang)

                            var loadings = dialog.findViewById<ProgressBar>(R.id.loadingsimpan)
                            dialog.findViewById<Button>(R.id.simpan).visibility = View.GONE
                            loadings.visibility = View.VISIBLE
                            TambahTokoRequest(this@Toko).simpanData(datas,
                                    object : SetelahSimpanData{
                                        override fun setelahSimpan() {
                                           /* loadings.visibility = View.GONE
                                            if(obj.idwilayah == wilayahIdselected || wilayahIdselected == ""){
                                                data.add(0,obj)
                                            }*/
                                            showToast("Toko sudah ditambahkan")
                                            loadData("")
                                           // adapter.notifyDataSetChanged()
                                            dialog.dismiss()
                                        }
                                    })
                        })
                dialog.findViewById<EditText>(R.id.wilayah)
                        .setOnClickListener(View.OnClickListener {
                            DialogProvinsi("")
                        })
            }
        }
    }
    public interface Dosomething{
        public fun doit(param : Any?,dialog: Dialog)
    }

    public fun ambilWilayah(listener : PilihWilayaListener<DataWilayah>) : MutableList<DataWilayah>{
        var data : MutableList<DataWilayah> = mutableListOf()
        var request = MyVolley(this).ayoRequest("wilayah"
                     ,Request.Method.GET, AyoReqestListener {
                        response ->
                        try {
                            var array = JSONArray(response)
                            for(i in 0 .. array.length()-1){
                                var obj = array.getJSONObject(i)
                                var wilayah = DataWilayah(obj.getString("nama_wilayah"),
                                                      obj.getString("id_wilayah"))
                                data.add(wilayah)
                            }
                            listener.ambilWilayah(data)
                        }   catch (e: Exception){
                            Log.d("error json",e.message)
                        }

                    },null)
        return data
    }
    private fun OnItemClick(dialog: Dialog):AdapterView.OnItemClickListener{
        return object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getwilayahFromProvensi(listProvinsi.get(p2).id,dialog)
            }
        }
    }
    public fun DialogProvinsi(listView: ListView,loading:ProgressBar){
        TokoModel(this@Toko).getProvinsi(object :MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                data as MutableList<Provinsi>
                listProvinsi = data
                val jkAdapter = ArrayAdapter<String>(this@Toko,
                        R.layout.support_simple_spinner_dropdown_item,
                        provinsiToString(listProvinsi))
                listView.adapter = jkAdapter
                loading.visibility = View.GONE
            }
        })
    }
    public fun DialogProvinsi(kosng:String){
        dialog(listProvinsi,object : Dosomething{
            override fun doit(param: Any?, dialog: Dialog) {
                var listview:ListView = dialog.findViewById(R.id.listwilayah)
                var loading:ProgressBar = dialog.findViewById(R.id.loading)
                listview.setOnItemClickListener(OnItemClick(dialog))
                if(listProvinsi.size == 0){
                    DialogProvinsi(listview,loading)
                }else{
                    loading.visibility = View.GONE
                    val jkAdapter = ArrayAdapter<String>(this@Toko,
                            R.layout.support_simple_spinner_dropdown_item,
                            provinsiToString(listProvinsi))
                    listview.adapter = jkAdapter
                }
            }
        },R.layout.dialog_wilayah)
    }

    public fun buatDialogWilayah(){
        var dialog = Dialog(this@Toko)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wilayah)
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
        var list = dialog.findViewById<ListView>(R.id.listwilayah)
        if(listWilayah.size == 0){
            ambilWilayah(object:PilihWilayaListener<DataWilayah>{
                override fun ambilWilayah(data: MutableList<DataWilayah>) {
                    listWilayah = data
                    val jkAdapter = ArrayAdapter<String>(this@Toko,
                            R.layout.support_simple_spinner_dropdown_item, listToArray(data))
                    list.adapter = jkAdapter
                    dialog.findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
                    list.setOnItemClickListener(AdapterView.
                            OnItemClickListener { adapterView, view, i, l ->
                                idWilayahDipilih = data.get(i).id
                                wilayahTextvie.setText(data.get(i).nama)
                                dialog.dismiss()
                            })
                }
            })
        }else{
            dialog.findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
            val jkAdapter = ArrayAdapter<String>(this@Toko,
                    R.layout.support_simple_spinner_dropdown_item, listToArray(listWilayah))
            list.adapter = jkAdapter
            list.setOnItemClickListener(AdapterView.
                    OnItemClickListener { adapterView, view, i, l ->
                        idWilayahDipilih = listWilayah.get(i).id
                        wilayahTextvie.setText(listWilayah.get(i).nama)
                        dialog.dismiss()

                    })
        }
        dialog.show()


    }

    public fun getwilayahFromProvensi(provinsi:String,dialogParent: Dialog){
        var data:MutableList<DataWilayah> = mutableListOf()
        dialog(data,object : Dosomething{
            override fun doit(param: Any?, dialog: Dialog) {
                var lisview:ListView = dialog.findViewById(R.id.listwilayah)
                var loading:ProgressBar = dialog.findViewById(R.id.loading)
                TokoModel(this@Toko).getWilayah(provinsi,object : MoreDatListener{
                    override fun setelahAmbilData(wilayahlist: Any) {
                        data = wilayahlist as MutableList<DataWilayah>
                        loading.visibility = View.GONE
                        var dataArray = listToArray(data)
                        dataArray.add("Tambah Kota")
                        val jkAdapter = ArrayAdapter<String>(this@Toko,
                                R.layout.support_simple_spinner_dropdown_item,
                                dataArray)
                        //tambah kata tambah toko
                        lisview.adapter = jkAdapter
                        lisview.setOnItemClickListener(object : AdapterView.OnItemClickListener{
                            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                if(p2 == dataArray.size - 1){
                                dialog(null,object : Dosomething{
                                    override fun doit(param: Any?, dialog: Dialog) {
                                        var namakota = dialog.findViewById<TextView>(R.id.namakota)
                                        var button   = dialog.findViewById<Button>(R.id.simpankota)
                                        var loadingtmbah   = dialog.findViewById<ProgressBar>(R.id.loadintambah)
                                        button.setOnClickListener(View.OnClickListener {
                                            button.visibility = View.GONE
                                            loadingtmbah.visibility = View.VISIBLE
                                            TokoModel(this@Toko).tambahKota(namakota.text.toString(),
                                                    object : MoreDatListener{
                                                        override fun setelahAmbilData(d2: Any) {
                                                            TokoModel(this@Toko).getWilayah(provinsi,
                                                                    object :MoreDatListener{
                                                                        override fun setelahAmbilData(d: Any) {
                                                                            data = d as MutableList<DataWilayah>
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
                                   if(nonInput == false){
                                       wilayahTextvie.setText(data.get(p2).nama)
                                       dialog.dismiss()
                                       dialogParent.dismiss()
                                       idWilayahDipilih = data.get(p2).id
                                   }else{
                                       dialog.dismiss()
                                       dialogParent.dismiss()
                                       pilihkota.setText(data.get(p2).nama)
                                       loadData(data.get(p2).id)
                                       wilayahIdselected = data.get(p2).id
                                       nonInput = false
                                        }
                                    }

                            }
                        })
                    }
                })
            }
        },R.layout.dialog_wilayah)
    }
    public fun listToArray(data : MutableList<DataWilayah>): MutableList<String>{
        var hasil : MutableList<String> = mutableListOf()
        for(i in 0.. data.size-1){
            hasil.add(data.get(i).nama)
        }

        return hasil
    }
    public fun provinsiToString(data:MutableList<Provinsi>):MutableList<String>{
        var hasil : MutableList<String> = mutableListOf()
        for(i in 0.. data.size-1){
            hasil.add(data.get(i).nama)
        }
        return hasil
    }
    class DataWilayah(nama:String,id:String){
        public var nama:String=""

        public var id  :String=""
        init {
            this.nama = nama
            this.id   = id
        }
    }
    public fun loadData(idkota:String){
        var url = "toko/$idkota"
        var myRequest = MyVolley(this).ayoRequest(url,MyVolley.GET,
                AyoReqestListener { response ->
                    try {
                        var json = JSONArray(response)
                        data = mutableListOf()
                        for (i in 0..json.length()-1){
                            var jsonObj = json.getJSONObject(i)
                            var toko = TokoEntity()
                            toko.idtoko = jsonObj.getString("id_toko")
                            toko.nama_toko = jsonObj.getString("nama_toko")
                            toko.alamat = jsonObj.getString("alamat")
                            toko.nama_wilayah = jsonObj.getString("nama_wilayah")
                            toko.no_telp = jsonObj.getString("no_telp")
                            toko.idwilayah = jsonObj.getString("id_wilayah")
                            toko.lat = jsonObj.getString("lat")
                            toko.lang = jsonObj.getString("lng")
                            data.add(toko)
                        }
                        var offset = 21
                        var isDataHabis : Boolean = false
                        var isLoadedFinish: Boolean = true
                        if(idkota != "") {
                            isDataHabis = true
                        }else{
                            var lasttoko =  TokoEntity()
                            data.add(lasttoko)
                        }
                        adapter = ValidasiAdapter(this@Toko,data)
                        adapter.setAdapterInterface(
                                AdapterInterface { view, position ->
                                    var toko = view.findViewById<TextView>(R.id.namatoko)
                                    toko.setText(data.get(position).nama_toko)
                                    var alamat = view.findViewById<TextView>(R.id.alamattoko)
                                    alamat.setText(data.get(position).alamat)
                                    if(posisiClikc == position){
                                       view.findViewById<RelativeLayout>(R.id.body1)
                                                .setBackgroundColor(Color.parseColor("#e5e5e5"))
                                    }
                                    if(data.get(position).alamat == null && isDataHabis == false &&
                                            isLoadedFinish == true){
                                        isLoadedFinish = false
                                        view.findViewById<CardView>(R.id.card_view).visibility = View.GONE
                                        view.findViewById<ProgressBar>(R.id.loadingdata).visibility = View.VISIBLE
                                        //untuk mengambil data lagi
                                        var tokoRequest = TambahTokoRequest(this@Toko)
                                        tokoRequest.getMoreToko(offset.toString(),
                                                object : MoreDatListener{
                                                    override fun setelahAmbilData(datanya: Any) {
                                                        data.removeAt(position)
                                                        try {
                                                            var rs    = datanya.toString()
                                                            var array = JSONArray(rs)
                                                            if(array.length() > 0){
                                                                for(i in 0..array.length()-1){
                                                                    var json = array.getJSONObject(i)
                                                                    var toko = TokoEntity()
                                                                    toko.nama_toko = json.getString("nama_toko")
                                                                    toko.lang = json.getString("lng")
                                                                    toko.lat  = json.getString("lat")
                                                                    toko.nama_wilayah = json.getString("nama_wilayah")
                                                                    toko.idwilayah = json.getString("id_wilayah")
                                                                    toko.idtoko = json.getString("id_toko")
                                                                    toko.no_telp = json.getString("no_telp")
                                                                    toko.alamat = json.getString("alamat")
                                                                    data.add(toko)
                                                                }

                                                                offset += 20
                                                                var t = TokoEntity()
                                                                data.add(t)
                                                            }else{
                                                                isDataHabis = true
                                                                view.visibility = View.GONE
                                                            }
                                                            adapter.notifyDataSetChanged()
                                                            isLoadedFinish = true
                                                        }catch (e:Exception){
                                                            Log.d("error disni",e.message)
                                                        }
                                                        Log.d("panjang li",data.size.toString())
                                                    }
                                                })
                                    }
                                },R.layout.item_toko
                        )
                        listtoko.adapter = adapter
                        totalData = data.size
                        listtoko.setOnScrollListener(object : AbsListView.OnScrollListener{
                            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
                                var lastView =  p1 + p2
                                if(posisiClikc < p1 || posisiClikc > lastView){
                                    posisiClikc = -1
                                    showButtonToolbar(false)
                                }
/*
                                Log.d("last view",listtoko.getChildAt(0).top
                                        .toString())
*/
                            }
                            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
                                    Log.d("selesai","fdf")
                                    Log.d("tinggi",listtoko.getChildAt(0).height.toString())
                            }
                        })

                    }catch (e : Exception){
                        Log.d("ada error",e.message)
                    }
                    loadingnya.visibility = View.GONE
                },null)
    }

}
