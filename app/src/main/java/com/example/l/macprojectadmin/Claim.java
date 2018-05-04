package com.example.l.macprojectadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.macprojectadmin.Adapter.ValidasiAdapter;
import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Entity.ClaimEntity;
import com.example.l.macprojectadmin.Entity.ValidasiEntity;
import com.example.l.macprojectadmin.Interface.AdapterInterface;
import com.example.l.macprojectadmin.Interface.AyoReqestListener;
import com.example.l.macprojectadmin.Request.MyVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Claim extends AppCompatActivity {

    private ListView listView;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        listView = (ListView)findViewById(R.id.listclaim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        loading = (ProgressBar)findViewById(R.id.loadingbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
       ambilData();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("notif"));
    }

    public void ambilData(){
        MyVolley myVolley = new MyVolley(this);
        myVolley.ayoRequest("notif_klaim", MyVolley.GET, new AyoReqestListener() {
            @Override
            public void lakukanSesuatu(String response) {
                final List<ClaimEntity> entityList = new ArrayList<>();
                loading.setVisibility(View.GONE);
                try {
                    JSONArray object = new JSONArray(response);
                    for(int i =0; i < object.length();i++){
                        JSONObject obj = object.getJSONObject(i);
                        ClaimEntity entity = new ClaimEntity();
                        entity.setIdClaim(obj.getString("id_klaim"));
                        entity.setNama(obj.getString("nama_user"));
                        entity.setEmail(obj.getString("email"));
                        entity.setNo_rek(obj.getString("no_rek"));
                        entity.setTelp(obj.getString("telp"));
                        entity.setAtas_nama(obj.getString("atas_nama"));
                        entity.setNama_toko( obj.getString("nama_toko"));
                        entity.setAlamat(obj.getString("alamat"));
                        entity.setTipe(obj.getString("tipe"));
                        entity.setNominal(obj.getString("nama_hadiah"));
                        entity.setTggl(obj.getString("tgl_klaim"));
                        entity.setPoint(obj.getString("point"));
                        entity.setIdhadiah(obj.getString("id_hadiah"));
                        entity.setCabang(obj.getString("cabang"));
                        entity.setJnis_bank(obj.getString("jenis_bank"));
                        entityList.add(entity);
                    }
                    if(object.length() == 0) {
                        ((TextView)findViewById(R.id.nodata)).setVisibility(View.VISIBLE);
                    }else{
                        ((TextView)findViewById(R.id.nodata)).setVisibility(View.GONE);
                    }
                    ValidasiAdapter adapter = new ValidasiAdapter(
                            Claim.this,entityList);
                    adapter.setAdapterInterface(new AdapterInterface() {
                        @Override
                        public void setView(View view, int position) {
                            Button button = (Button)view.findViewById(R.id.lihat);
                            TextView ktp,nama,jenis,nominal;
                            ktp  = (TextView)view.findViewById(R.id.ktp);
                            nama = (TextView)view.findViewById(R.id.nama);
                            jenis = (TextView)view.findViewById(R.id.jenis);
                            nominal = (TextView)view.findViewById(R.id.nominal);
                            ktp.setText(entityList.get(position).getEmail());
                            nama.setText(entityList.get(position).getNama());
                            jenis.setText(entityList.get(position).getTipe());
                            nominal.setText(entityList.get(position).getNominal());
                            button.setOnClickListener(tombolListener(entityList.get(position)));
                        }
                    },R.layout.item_claim);
                    listView.setAdapter(adapter);
                    setRefresh();
                    setRefresh();
                }catch (Exception e){
                    Log.d("error jfff",e.getMessage());
                }
            }
        },null);
    }

    public void setRefresh(){
        if(listView.getChildCount() > 0){
            listView.setSelection(1);
            listView.smoothScrollToPosition(0);
        }
    }
    private View.OnClickListener tombolListener(final ClaimEntity entity){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Claim.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_claim);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                createDialogWidget(dialog,entity);
                dialog.show();
            }
        };
    }
    public void createDialogWidget(Dialog dialog,ClaimEntity entity){
        ((TextView)dialog.findViewById(R.id.nama)).setText(entity.getNama());
        ((TextView)dialog.findViewById(R.id.email)).setText(entity.getEmail());
        ((TextView)dialog.findViewById(R.id.telp)).setText(entity.getTelp());
        ((TextView)dialog.findViewById(R.id.namatoko)).setText(entity.getNama_toko());
        ((TextView)dialog.findViewById(R.id.alamat)).setText(entity.getAlamat());
        ((TextView)dialog.findViewById(R.id.tipe)).setText(entity.getTipe());
        ((TextView)dialog.findViewById(R.id.norek)).setText(entity.getNo_rek());
        ((TextView)dialog.findViewById(R.id.nominal)).setText(entity.getNominal());
        ((TextView)dialog.findViewById(R.id.point)).setText(entity.getPoint());
        ((TextView)dialog.findViewById(R.id.jnisbankg)).setText(entity.getJnis_bank());
        ((TextView)dialog.findViewById(R.id.cabang)).setText(entity.getCabang());
        ((TextView)dialog.findViewById(R.id.atasnama)).setText(entity.getAtas_nama().toUpperCase());
        ((TextView)dialog.findViewById(R.id.Tanggalclaim)).setText(entity.getTggl());
        View.OnClickListener listner
                = onClickListener(entity.getEmail(),entity.getIdhadiah(),entity.getIdClaim(),dialog);
        ((Button)dialog.findViewById(R.id.konfirmasi)).setOnClickListener(listner);

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("type").equals("claim")){
                ambilData();
            }
        }
    };
    private View.OnClickListener onClickListener(String email, String id, String idklaim
                                                 , final Dialog dialog){

        final Map<String,String> map = new HashMap<>();
        map.put("email",email);
        map.put("id",id);
        map.put("id_klaim",idklaim);
        map.put("email_admin",new AppSession(this).getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu Sebentar ...");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                new MyVolley(getApplicationContext()).ayoRequest("aksi_klaim"
                      , MyVolley.POST, new AyoReqestListener() {
                          @Override
                          public void lakukanSesuatu(String response) {
                              try {
                                  JSONObject data = new JSONObject(response);
                                  if(data.getString("status").equals("confirmed")){
                                      Toast.makeText(Claim.this,
                                              data.getString("pesan"), Toast.LENGTH_SHORT).show();
                                        ambilData();
                                  }

                              }catch (Exception e){
                                  e.printStackTrace();
                              }
                              progressDialog.dismiss();
                            AppSession session  = new AppSession(getApplicationContext());
                            session.setBadgeCount(session.getBadgeCount()-1);
                            Intent intent = new Intent("notif");
                            intent.putExtra("type","claim");
                            intent.putExtra("tambah",false);
                            LocalBroadcastManager.getInstance(Claim.this
                                    ).sendBroadcast(intent);
                            MainActivity.setBadge(getApplicationContext(),session.getBadgeCount());
                          }
                      },map);
            }
        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
