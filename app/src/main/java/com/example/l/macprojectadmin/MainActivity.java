package com.example.l.macprojectadmin;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.macprojectadmin.Adapter.ValidasiAdapter;
import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.Entity.ValidasiEntity;
import com.example.l.macprojectadmin.Interface.AdapterInterface;
import com.example.l.macprojectadmin.Interface.OnGetNotifikasi;
import com.example.l.macprojectadmin.MyAnimation.ResizeAnimation;
import com.example.l.macprojectadmin.Request.CountNotifRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView notifcount,cliam,cliam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(!new AppSession(this).isLogin()){
            startActivity(new Intent(this,LoginAdmin.class));
            finish();
        }
        ((ImageView)findViewById(R.id.profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Provil.class));
            }
        });
        notifcount = (TextView)findViewById(R.id.notifcount);
        cliam = (TextView)findViewById(R.id.claimcount);
        cliam2 = (TextView)findViewById(R.id.claimcount);
        ((RelativeLayout)findViewById(R.id.toko)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Toko.class));
            }
        });
        ((RelativeLayout)findViewById(R.id.rvalidasi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Validasi.class));
            }
        });

        ((RelativeLayout)findViewById(R.id.rnotif)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Claim.class));
            }
        });
        getData();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("notif"));
        ((RelativeLayout)findViewById(R.id.produk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Produk.class));
            }
        });
        ((RelativeLayout)findViewById(R.id.iklan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Iklan.class));
            }
        });
        ((RelativeLayout)findViewById(R.id.reseller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Reseller.class));
            }
        });
        notifcount.setAlpha(0);
        cliam.setAlpha(0);
        permission();

    }

    public void getData(){
        CountNotifRequest request = new CountNotifRequest(this);
        request.getAllNotif(new OnGetNotifikasi() {
            @Override
            public void getNotif(String validasi, String claim) {
                makeAnimation(validasi,claim);
                int jumlahNotif = Integer.parseInt(validasi) + Integer.parseInt(claim);
                new AppSession(getApplicationContext()).setBadgeCount(jumlahNotif);
                setBadge(getApplicationContext(),jumlahNotif);
            }
        });
    }

    public void makeAnimation(String v,String c){
        if(Integer.parseInt(v) > 0) notifcount.setAlpha(1);
        if(Integer.parseInt(c) > 0) cliam.setAlpha(1);
        ScaleAnimation fade_in =
                new ScaleAnimation(0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(700);
        fade_in.setFillAfter(true);
        if(v.equals("0")){
            notifcount.setAlpha(0);
        }else{
            notifcount.setText(v);
            notifcount.setAnimation(fade_in);
        }
        if(c.equals("0")){
           cliam.setAlpha(0);
        }else{
            cliam.setAnimation(fade_in);
            cliam.setText(c);
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("type").equals("validasi")){
               getData();
            }
            else if(intent.getStringExtra("type").equals("claim")){
               getData();
            }
        }
    };

    private ScaleAnimation animation(){
        ScaleAnimation fade_in =
                new ScaleAnimation(0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
        fade_in.setDuration(700);
        fade_in.setFillAfter(true);
        return fade_in;
    }
    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    public void permission(){
   /*     if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }*/
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Yakin Keluar ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}
