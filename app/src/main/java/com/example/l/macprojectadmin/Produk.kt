package com.example.l.macprojectadmin

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.l.macprojectadmin.Adapter.ValidasiAdapter
import com.example.l.macprojectadmin.Entity.Jenis_Produk
import com.example.l.macprojectadmin.Entity.Produk
import com.example.l.macprojectadmin.Interface.AdapterInterface
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.MyLib.UploadFile
import com.example.l.macprojectadmin.Request.ProdukModel
import kotlinx.android.synthetic.main.activity_produk.*
import org.json.JSONObject

class Produk : AppCompatActivity() {
    private lateinit var listviewAdapter:ValidasiAdapter
    private var dataSelectedPosition:Int = -1
    private lateinit var alldata:MutableList<Produk>
    private var isDataHabis:Boolean = false
    private var loadMoreoffset:Int = 20
    private lateinit var dataJenis_produk:MutableList<Jenis_Produk>
    private var sedanMencari:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk)
        getAllProduk(null)
        tambahtoko.setOnClickListener(View.OnClickListener {
           // addProduk()
           var dialog  = BottomSheetDialog(this@Produk)

           dialog.setContentView(R.layout.addproductdialog)
           dialog.findViewById<RelativeLayout>(R.id.excellinear)
                   ?.setOnClickListener(View.OnClickListener {
                       val intent      = Intent()
                       intent.type     = "application/vnd.ms-excel"
                       intent.action   = Intent.ACTION_GET_CONTENT
                       startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1)
           })
           dialog.findViewById<RelativeLayout>(R.id.manual)?.setOnClickListener(View.OnClickListener
           {
               addProduk()
           })
           dialog.show()
        })
        listproduk.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                var body = view?.findViewById<LinearLayout>(R.id.body)
                if(p2 == dataSelectedPosition){
                    /* body?.setBackgroundColor(Color.parseColor("#ffffff"))
                    */
                    alldata.get(dataSelectedPosition).isSelected = false
                    listviewAdapter.notifyDataSetChanged()
                    dataSelectedPosition = -1
                    showDeleteAndDetail()
                    if(sedanMencari == true){
                        r1.visibility = View.GONE

                        inputcariproduk.visibility = View.VISIBLE
                    }
                }else if(dataSelectedPosition > -1 ){
                    alldata.get(dataSelectedPosition).isSelected = false
                   // body?.setBackgroundColor(Color.parseColor("#d1d1d1"))
                  //  showToast(p0?.childCount.toString())
                 /*   var body2 = p0?.getChildAt(dataSelectedPosition)
                                ?.findViewById<LinearLayout>(R.id.body)
                        body2?.setBackgroundColor(Color.parseCo lor("#ffffff"))
                 */   dataSelectedPosition = p2
                    alldata.get(dataSelectedPosition).isSelected = true
                    listviewAdapter.notifyDataSetChanged()

                }else{
                    //body?.setBackgroundColor(Color.parseColor("#d1d1d1"))
                    dataSelectedPosition = p2
                    alldata.get(dataSelectedPosition).isSelected = true
                    listviewAdapter.notifyDataSetChanged()
                    showDeleteAndDetail()
                    if(sedanMencari == true){
                        // sedanMencari = false
                        r1.visibility = View.VISIBLE

                        inputcariproduk.visibility = View.GONE
                    }
                }


            }
        })
        lihat.setOnClickListener(View.OnClickListener {
            munculDialog(R.layout.dialog_tambah_prodcut,object :Something{
                override fun lakukan(dialog: Dialog) {
                    var jenis        = dialog.findViewById<EditText>(R.id.jenis)
                    var serialnumber = dialog.findViewById<EditText>(R.id.serialnumber)
                    var kode_scan    = dialog.findViewById<EditText>(R.id.kode_scan)
                    var point        = dialog.findViewById<EditText>(R.id.point)
                    var status       = dialog.findViewById<EditText>(R.id.status)

                    jenis       .setText(alldata.get(dataSelectedPosition).nama_produk)
                    serialnumber.setText(alldata.get(dataSelectedPosition).serial_number)
                    kode_scan   .setText(alldata.get(dataSelectedPosition).kode_scan)
                    point       .setText(alldata.get(dataSelectedPosition).point)
                    status      .setText(alldata.get(dataSelectedPosition).status)

                    var ubah = dialog.findViewById<Button>(R.id.simpan)
                    ubah.setText("Ubah")
                    ubah.setOnClickListener(View.OnClickListener {
                        view ->
                        var data:MutableMap<String,String> = mutableMapOf()
                        data.put("id",alldata.get(dataSelectedPosition).idproduk)
                        data.put("nama_produk",dialog.findViewById<EditText>(R.id.jenis)
                                .text.toString())
                        data.put("serial_number",dialog.findViewById<EditText>(R.id.serialnumber)
                                .text.toString())
                        data.put("kode_scan",dialog.findViewById<EditText>(R.id.kode_scan)
                                .text.toString())
                        data.put("point", dialog.findViewById<EditText>(R.id.point)
                                .text.toString())
                        ProdukModel(this@Produk).editProduk(data,object : MoreDatListener{
                            override fun setelahAmbilData(data: Any) {
                                var produk:Produk       = alldata.get(dataSelectedPosition)
                                produk.nama_produk      = jenis.text.toString()
                                produk.serial_number    = serialnumber.text.toString()
                                produk.kode_scan        = kode_scan.text.toString()
                                produk.point            = point.text.toString()
                                alldata.set(dataSelectedPosition,produk)
                                listviewAdapter.notifyDataSetChanged()
                                dialog.dismiss()
                            }
                        })

                    })
                    jenis.setOnClickListener(View.OnClickListener {
                        /*munculDialog(R.layout.dialog_jenis_produk,object : Something{
                            override fun lakukan(dialog: Dialog) {
                                var p1:RelativeLayout = dialog.findViewById(R.id.p1)
                                var p2:RelativeLayout = dialog.findViewById(R.id.p2)
                                var onclick = object : View.OnClickListener{
                                    override fun onClick(p0: View?) {
                                        if(p0?.id == R.id.p1){
                                            jenis.setText("McAfee Internet Security Student Pack")
                                        }else{
                                            jenis.setText("McAfee Total Protection Student Pack")
                                        }
                                        dialog.dismiss()
                                    }
                                }
                                p1.setOnClickListener(onclick)
                                p2.setOnClickListener(onclick)
                            }
                        })*/
                    })
                }
            })
        })
        hapus.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@Produk)
            builder.setMessage("Apakah Anda Yakin Menghapus Produk ?")
            builder.setPositiveButton("Ya") { dialogInterface, i ->
                ProdukModel(this@Produk).deleteProduk(alldata.get(dataSelectedPosition).idproduk,
                        object :MoreDatListener{
                            override fun setelahAmbilData(data: Any) {
                                alldata.removeAt(dataSelectedPosition)
                                listviewAdapter.notifyDataSetChanged()
                                showToast("Produk Sudah dihapus")
                                listviewAdapter.notifyDataSetChanged()
                            }
                        })
            }
            builder.setNegativeButton("Tidak") { dialogInterface, i ->
                dialogInterface.cancel()
            }
            builder.create().show()
        })
        getProdukJenis(object :jenisbaruInterface{
            override fun lakukan() {
            }
        })
        cariproduk.setOnClickListener(View.OnClickListener {
            r1.visibility = View.GONE
            sedanMencari = true
            inputcariproduk.visibility = View.VISIBLE
        })
        inputcariproduk.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               if(p0.toString() == ""){
                   getAllProduk(null)
               }else{
                   getAllProduk(p0.toString())
               }
            }
        })
    }

    override fun onBackPressed() {
        if(sedanMencari == true){
            sedanMencari = false;
            r1.visibility = View.VISIBLE
            inputcariproduk.visibility = View.GONE
        }else{
            super.onBackPressed()

        }
    }
    public fun getProdukJenis(jenisbaruInterface: jenisbaruInterface){
        ProdukModel(this).getJenisProduk(object :MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                dataJenis_produk = data as MutableList<Jenis_Produk>
                var data = Jenis_Produk()
                data.nama = "Tambah Jenis Produk"
                data.id = "tidf"
                dataJenis_produk.add(data)
                jenisbaruInterface.lakukan()
            }
        })
    }
    public fun parsingJson(response:String) : MutableList<Produk>{
        var listProduk:MutableList<Produk> = mutableListOf()
        try {
            var json  = JSONObject(response)
            var array = json.getJSONArray("data")
            for(i in 0..array.length()-1){
                var produkJson     = array.getJSONObject(i)
                var produk         = Produk()
                produk.idproduk    = produkJson.getString("id_produk")
                produk.nama_produk = produkJson.getString("nama_produk")
                produk.url         = produkJson.getString("url")
                produk.kode_scan   = produkJson.getString("kode_scan")
                produk.point       = produkJson.getString("point")
                produk.serial_number= produkJson.getString("serial_number")
                var status       = produkJson.getString("status")
                if(status.equals("1")){
                    produk.status = "Sudah Discan"
                }else produk.status = "Belum Discan"
                produk.isSelected = false
                listProduk.add(produk)
            }
            total.setText("Total : "+json.getString("total")+" Produk")
            //listener.setelahAmbilData(listProduk)
        }catch (e:Exception){
            Log.d("error Json",e.message)
        }
        return listProduk
    }
    public fun getAllProduk(kode_scan:String?){
        var isDataHabis:Boolean = false
        var isLoadedFinish:Boolean = true
        var listener = object :MoreDatListener{
            override fun setelahAmbilData(data: Any) {
              alldata = mutableListOf()
              alldata = parsingJson(data.toString())
              if(kode_scan != null) {
                  isDataHabis = true
              }else{
                  var itemLoading = Produk()
                  alldata.add(itemLoading)
              }

                listviewAdapter = ValidasiAdapter(this@Produk,alldata)
              listviewAdapter.setAdapterInterface(object : AdapterInterface{
                  override fun setView(view: View?, position: Int) {
                    var serialNumber:TextView? = view?.findViewById(R.id.serialnumber)
                    var jenis       :TextView? = view?.findViewById(R.id.jenis)
                    var qr          :TextView? = view?.findViewById(R.id.kode_scan)
                    var point       :TextView? = view?.findViewById(R.id.point)
                    var status      :TextView? = view?.findViewById(R.id.status)

                    serialNumber?.setText(alldata.get(position).serial_number)
                    jenis       ?.setText(alldata.get(position).nama_produk)
                    qr          ?.setText(alldata.get(position).kode_scan)
                    point       ?.setText(alldata.get(position).point)
                    status      ?.setText(alldata.get(position).status)
                    if(alldata.get(position).isSelected == true){
                        view?.findViewById<LinearLayout>(R.id.body)?.
                                setBackgroundColor(Color.parseColor("#d1d1d1"))
                    }else{
                        view?.findViewById<LinearLayout>(R.id.body)?.
                                setBackgroundColor(Color.parseColor("#ffffff"))
                    }

                    if(alldata.get(position).idproduk == null && isDataHabis == false
                            && isLoadedFinish == true){
                        view?.findViewById<ProgressBar>(R.id.loadingbar)?.visibility = View.VISIBLE
                        view?.findViewById<CardView>   (R.id.card_view) ?.visibility = View.GONE
                        isLoadedFinish = false
                        var offset:String = loadMoreoffset.toString()
                        ProdukModel(this@Produk).getLoadMore(offset,object : MoreDatListener{
                            override fun setelahAmbilData(data: Any) {
                                alldata.removeAt(position)
                                data as MutableList<Produk>
                                if(data.size > 0) {
                                    alldata.addAll(data)
                                    var produk = Produk()
                                    alldata.add(produk)
                                }
                                else {
                                    isDataHabis = true
                                }
                                isLoadedFinish = true
                                loadMoreoffset += 20
                                listviewAdapter.notifyDataSetChanged()
                            }
                        })
                    }
                  }
              },R.layout.item_list_produk)

              listproduk.adapter = listviewAdapter
              loading.visibility = View.GONE
            }
        }
        var produk   = ProdukModel(this@Produk).getAllData(listener,kode_scan)
    }
    public fun addProduk(){
        munculDialog(R.layout.dialog_tambah_prodcut,object :Something{
            override fun lakukan(dialog: Dialog) {
               var data:MutableMap<String,String> = mutableMapOf()
               //pilih jenis produk
               var jenis  = dialog.findViewById<EditText>(R.id.jenis)
               var simpan = dialog.findViewById<Button>(R.id.simpan)
               var serial = dialog.findViewById<EditText>(R.id.serialnumber)
               var kodeqr = dialog.findViewById<EditText>(R.id.kode_scan)
               var point  = dialog.findViewById<EditText>(R.id.point)
               var loading= dialog.findViewById<ProgressBar>(R.id.loading)
                var idProdukdikirm = ""
               jenis.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                   override fun onFocusChange(p0: View?, p1: Boolean) {
                      if(p1 == true){
                          var listenerJenisProduk = object : Something{
                              override fun lakukan(dialog: Dialog) {
                                  var listJenis = dialog.findViewById<ListView>(R.id.jenis)
                                  var adapter = ValidasiAdapter(this@Produk,dataJenis_produk)

                                  adapter.setAdapterInterface(object : AdapterInterface{
                                      override fun setView(view: View?, position: Int) {
                                          Log.d("tes tes",dataJenis_produk.get(position).nama)
                                          var nama = view?.findViewById<TextView>(R.id.namajenisk)
                                          nama?.setText(dataJenis_produk.get(position).nama)
                                          var r1 = view?.findViewById<RelativeLayout>(R.id.p1)
                                          r1?.setOnClickListener(View.OnClickListener {
                                             if(position != dataJenis_produk.size-1){
                                                 jenis.setText(dataJenis_produk.get(position).nama)
                                                 idProdukdikirm = dataJenis_produk.get(position).nama
                                                 dialog.dismiss()
                                             }else{
                                                 var listener = object : Something{
                                                     override fun lakukan(dialog5: Dialog) {
                                                        var jenisbaru =
                                                                dialog5.findViewById<EditText>(R.id.jenisproduk)
                                                         dialog5.findViewById<Button>(R.id.tambahjenisbaru)
                                                                 .setOnClickListener(View.OnClickListener {
                                                                    dialog5.dismiss()
                                                                    ProdukModel(this@Produk)
                                                                            .tambahjenisProduk(
                                                                                    jenisbaru.text.toString()
                                                                            ,object : MoreDatListener{
                                                                                override fun setelahAmbilData(data: Any) {
                                                                                    getProdukJenis(object :jenisbaruInterface{
                                                                                        override fun lakukan() {
                                                                                            adapter.notifyDataSetChanged()
                                                                                        }
                                                                                    })
                                                                                    }
                                                                                }
                                                                            )
                                                                 })
                                                     }
                                                 }
                                                 munculDialog(R.layout.input_jenis_produk_dialog,
                                                         listener)
                                             }

                                          })
                                      }
                                  },R.layout.item_jenis_produk)
                                  listJenis.adapter = adapter
                              }
                          }

                          munculDialog(R.layout.dialog_jenis_produk,listenerJenisProduk)
                      }
                   }
               })
               simpan.setOnClickListener(View.OnClickListener {
                   loading.visibility = View.VISIBLE
                   simpan .visibility = View.GONE
                   data.put("nama_produk",idProdukdikirm)
                   data.put("serial_number",serial.text.toString())
                   data.put("kode_scan",kodeqr.text.toString())
                   data.put("point",kodeqr.text.toString())
                   var listener = object : MoreDatListener{
                       override fun setelahAmbilData(data: Any) {
                          // showToast(data.toString())
                           loading.visibility = View.GONE
                           dialog.dismiss()
                           getAllProduk(null)
                       }
                   }
                   ProdukModel(this@Produk).addProduk(data,listener)
               })
            }
        })

    }
    public fun munculDialog(layout:Int,listener:Something){
        var dialog = Dialog(this@Produk)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
       //dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        listener.lakukan(dialog)
        dialog.show()
    }

    public fun showDeleteAndDetail(){
        if(dataSelectedPosition >= 0){
            lihat.visibility = View.VISIBLE
            hapus.visibility = View.VISIBLE
        }else{
            lihat.visibility = View.GONE
            hapus.visibility = View.GONE
        }
    }

    public fun showToast(pesan:String){
        Toast.makeText(this@Produk,pesan,Toast.LENGTH_SHORT).show()
    }

    interface Something{
        public fun lakukan(dialog:Dialog)
    }
    interface jenisbaruInterface{
        public fun lakukan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && data?.data != null){
            var mydata = data?.data
            var upload = UploadFile(this@Produk,mydata)
            upload.lakukanUpload(object : MoreDatListener{
                override fun setelahAmbilData(data: Any) {
                    showToast(data.toString())

                }
            })
        }
    }
}
