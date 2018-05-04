package com.example.l.macprojectadmin.MyLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Created by L on 04/01/18.
 */

public class UbahOrentasi {
    private Context context;
    public UbahOrentasi(Context context){
        this.context = context;
    }


    public Bitmap ubahOrenatasi(Uri uri){
        android.support.media.ExifInterface exifInterface = null;
        Bitmap bitmap = null;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            exifInterface = new android.support.media.ExifInterface(context.
                    getContentResolver().openInputStream(uri));

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
            Matrix matrix = new Matrix();
            int lebar = bitmap.getWidth();
            int tinggi= bitmap.getHeight();

            int setTinggi = lebar;
            int setLebar  = tinggi;
            int scalelebar;
            int scaletinggi;

            //matrix.setScale(scalelebar,scaletinggi);
            if(orientation == 6){
                matrix.postRotate(90);
            }else if(orientation == 3){
                matrix.postRotate(180);
            }else if (orientation == 8){
                matrix.postRotate(270);
            }
            scalelebar = 648;scaletinggi = 1152;
            bitmap = Bitmap.createBitmap(bitmap,0,0,lebar,tinggi,matrix,true);
            if(lebar > (tinggi+200)){
                int temp = scalelebar;
                scalelebar = scaletinggi;
                scaletinggi = temp;
                Log.d("hasil","lebih gede lebar "+String.valueOf(lebar)+" "+String.valueOf(tinggi));
            }
            bitmap = Bitmap.createScaledBitmap(bitmap,scalelebar,scaletinggi,true);


        }catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
