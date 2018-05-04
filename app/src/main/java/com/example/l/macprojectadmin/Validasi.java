package com.example.l.macprojectadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.macprojectadmin.Adapter.ValidasiAdapter;
import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.Entity.ValidasiEntity;
import com.example.l.macprojectadmin.Interface.AdapterInterface;
import com.example.l.macprojectadmin.Interface.AfterValidate;
import com.example.l.macprojectadmin.Interface.ValidasiInterface;
import com.example.l.macprojectadmin.Request.UserValidasiReqeust;

import org.json.JSONObject;

import java.util.List;

public class Validasi extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validasi);
        listView = (ListView)findViewById(R.id.listvalidasi);
        setListView();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("notif"));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setListView();
        }
    };
    public void setListView(){
        new UserValidasiReqeust(this).getData(new ValidasiInterface() {
            @Override
            public void getData(final List<?> list) {
                progressDialog.dismiss();
                ValidasiAdapter adapter = new ValidasiAdapter(
                        Validasi.this,list
                );
                if(list.size() == 0) ((TextView)findViewById(R.id.nodata)).setVisibility(View.VISIBLE);
                adapter.setAdapterInterface(new AdapterInterface() {
                    @Override
                    public void setView(View view, int position) {
                        TextView ktp,email,nama;
                        email = (TextView)view.findViewById(R.id.email);
                        nama  = (TextView)view.findViewById(R.id.namuser);
                        final ValidasiEntity validasiEntity = (ValidasiEntity)list.get(position);
                        nama.setText(validasiEntity.getNama_user());
                        email.setText(validasiEntity.getEmail());
                        Button button = (Button)view.findViewById(R.id.lihat);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(Validasi.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_validasi);
                                tampilDialog(dialog,validasiEntity);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.show();
                            }
                        });
                    }
                },R.layout.validasi_item);
                listView.setAdapter(adapter);
            }
        });
    }
    public void tampilDialog(final Dialog dialog, final ValidasiEntity entity){;
        ((TextView)dialog.findViewById(R.id.namauser)).setText(entity.getNama_user());
        ((TextView)dialog.findViewById(R.id.jk)).setText(entity.getJ_kelamin());
        ((TextView)dialog.findViewById(R.id.telp)).setText(entity.getTelp());
        ((TextView)dialog.findViewById(R.id.email)).setText(entity.getEmail());
        ((TextView)dialog.findViewById(R.id.toko)).setText(entity.getNama_toko());
        ((TextView)dialog.findViewById(R.id.alamat)).setText(entity.getAlamat());
        ((Button)dialog.findViewById(R.id.konfirmasi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserValidasiReqeust(getApplicationContext())
                        .terimaValidasi(entity.getToken(), new AfterValidate() {
                            @Override
                            public void lakukaSesuatu(String pesan) {

                                try {
                                    JSONObject json = new JSONObject(pesan);
                                    if(json.getString("status").equals("validated")){
                                        Toast.makeText(Validasi.this,
                                                json.getString("pesan"), Toast.LENGTH_SHORT).show();
                                        setListView();
                                    }else{
                                        setListView();
                                    }

                                }catch (Exception e){

                                }
                                AppSession session  = new AppSession(getApplicationContext());
                                session.setBadgeCount(session.getBadgeCount()-1);
                                updateIconAngkaDiMainAcitivity();
                                MainActivity.setBadge(getApplicationContext(),session.getBadgeCount());

                                dialog.dismiss();
                            }
                        });
            }
        });
        ((Button)dialog.findViewById(R.id.tolak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new UserValidasiReqeust(getApplicationContext()).tolakValidasi(entity.getToken()
                        , new AfterValidate() {
                            @Override
                            public void lakukaSesuatu(String pesan) {
                                try {
                                    JSONObject json = new JSONObject(pesan);
                                    if(json.getString("status").equals("validated")){
                                        Toast.makeText(Validasi.this,
                                                json.getString("pesan"), Toast.LENGTH_SHORT).show();
                                        setListView();
                                    }else{
                                        setListView();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                AppSession session  = new AppSession(getApplicationContext());
                                session.setBadgeCount(session.getBadgeCount()-1);
                                updateIconAngkaDiMainAcitivity();
                                MainActivity.setBadge(getApplicationContext(),session.getBadgeCount());
                            }
                        });
            }
        });
    }
    public void updateIconAngkaDiMainAcitivity(){
        Intent intent = new Intent("notif");
        intent.putExtra("type","validasi");
        intent.putExtra("tambah",false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
