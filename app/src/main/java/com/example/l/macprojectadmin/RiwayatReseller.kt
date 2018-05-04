package com.example.l.macprojectadmin

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.l.macprojectadmin.Fragment.RiwayatClaim
import com.example.l.macprojectadmin.Fragment.RiwayatScan
import com.example.l.macprojectadmin.Interface.MoreDatListener
import com.example.l.macprojectadmin.Request.ResellerRiwayat
import kotlinx.android.synthetic.main.activity_riwayat_reseller.*
import org.json.JSONObject
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.DatePicker
import java.util.*


class RiwayatReseller : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_reseller)
        var email = intent.getStringExtra("email_user")
        ResellerRiwayat(this).getAllRiwayat(email,object : MoreDatListener{
            override fun setelahAmbilData(data: Any) {
                loading.visibility = View.GONE
                try {
                    var json = JSONObject(data.toString())
                    var rScan = json.getJSONObject("data")
                            .getJSONArray("riwayat_scan").toString()
                    var rClaim = json.getJSONObject("data")
                            .getJSONArray("riwayat_klaim").toString()

                    viewpager.adapter = Adapter(supportFragmentManager,rScan,rClaim)
                    tab.setupWithViewPager(viewpager)

                }catch (e:Exception){}
            }
        })
        excel.setOnClickListener(View.OnClickListener {

            var dialog = Dialog(this@RiwayatReseller)
            dialog.setContentView(R.layout.dialog_excel)
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            var dari:EditText = dialog.findViewById(R.id.dari)
            var ke  :EditText = dialog.findViewById(R.id.ke)
            var kirim         = dialog.findViewById<Button>(R.id.importexcel)

            val calendar = Calendar.getInstance(TimeZone.getDefault())
            dari.setOnClickListener(View.OnClickListener {
                val dialog = DatePickerDialog(this@RiwayatReseller, R.style.DialogTheme,
                            setDate(dari),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                dialog.show()
            })
            dari.setOnFocusChangeListener(object : View.OnFocusChangeListener{
                override fun onFocusChange(p0: View?, p1: Boolean) {
                    if(p1 == true){
                        val dialog = DatePickerDialog(this@RiwayatReseller, R.style.DialogTheme,
                                setDate(dari),
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH))
                        dialog.show()
                    }
                }
            })
            ke.setOnClickListener(View.OnClickListener {
                val dialog = DatePickerDialog(this@RiwayatReseller, R.style.DialogTheme,
                        setDate(ke),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                dialog.show()
            })
            ke.setOnFocusChangeListener(object : View.OnFocusChangeListener{
                override fun onFocusChange(p0: View?, p1: Boolean) {
                    if(p1 == true){
                        val dialog = DatePickerDialog(this@RiwayatReseller, R.style.DialogTheme,
                                setDate(ke),
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH))
                        dialog.show()
                    }
                }
            })
            kirim.setOnClickListener(View.OnClickListener {
                ResellerRiwayat(applicationContext).sendRequestExcel(dari.text.toString(),
                        ke.text.toString(),email)
                dialog.dismiss()
            })
            dialog.show()
        })
    }
    public fun setDate(editText: EditText) : DatePickerDialog.OnDateSetListener{
        var listener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) { //editText
                var date = p1.toString() +"-"+ (p2+1).toString() +"-"+ p3.toString()
                editText.setText(date)
            }
        }
        return listener
    }
    class Adapter : FragmentStatePagerAdapter{
        private lateinit var scan:String
        private lateinit var klaim:String
        constructor(fm:FragmentManager,scan:String,klaim:String):super(fm){
            this.klaim = klaim
            this.scan  = scan
        }

        override fun getCount(): Int {
            return 2


        }

        override fun getPageTitle(position: Int): CharSequence {
            if(position == 0){
                return "Riwayat Hadiah"
            }else{
                return "Riwayat Scan"
            }
        }

        override fun getItem(position: Int): Fragment {
            if(position == 0){
                var riwayat = RiwayatClaim()
                var bundle = Bundle()
                bundle.putString("data",klaim)
                riwayat.arguments = bundle

                return riwayat
            }else{
                var riwayat = RiwayatScan()
                var bundle = Bundle()
                bundle.putString("data",scan)
                riwayat.arguments = bundle
                return riwayat

            }
        }
    }
}
