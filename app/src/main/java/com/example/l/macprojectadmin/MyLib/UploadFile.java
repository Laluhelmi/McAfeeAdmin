package com.example.l.macprojectadmin.MyLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.l.macprojectadmin.App.Http;
import com.example.l.macprojectadmin.Interface.MoreDatListener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by L on 29/12/17.
 */

public class UploadFile {

    public String uploadKeServer(Context context, Uri uri,String file){
        HttpURLConnection conn = null;
        String tipefile = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String respon = null;
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 512000;
        try {
            URL url = new URL(Http.url+"produk/tesupload/?X-API-KEY=istimewa");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());
            //ByteArrayInputStream fileInputStream=null;
            //fileInputStream = bitmapToStream(bitmap);
            FileInputStream fileInputStream = (FileInputStream)
                    context.getContentResolver().openInputStream(uri);
            Log.d("ukuran gambar",String.valueOf(fileInputStream.available()));
            String param = "type="+tipefile;
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"fileupload\";filename=\""
                    +file+"\""+lineEnd+"");

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            Log.d("ukuran buffer ",String.valueOf(bufferSize));
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //Log.d("ukuran buffer -- ",String.valueOf(bufferSize)+" - "+String.valueOf(bytesAvailable));
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                //Log.d("byte read",String.valueOf(bytesRead));
            }
            //Log.d("ukuran buffer terakhir ",String.valueOf(fileInputStream.available()));
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"tipe\""+lineEnd+"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("df");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"idpesan\""+lineEnd+"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("dfddfdfafd");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens);

            fileInputStream.close();
            dos.flush();
            dos.close();
            BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = buf.readLine()) != null) {
                sb.append(line);
            }
            buf.close();
            respon = sb.toString();
        }catch (Exception e){
            respon = e.getMessage();
        }
        return respon;
    }
    public String uploadIklan(String file, Context context, Bitmap bitmap){
        HttpURLConnection conn = null;
        String tipefile = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String respon = null;
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 512000;
        try {
            URL url = new URL(Http.url+"upload_iklan/simpan/?X-API-KEY=istimewa");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());
            ByteArrayInputStream fileInputStream=null;
            File sourceFile = new File(Environment.getExternalStorageDirectory()+file);
            fileInputStream = bitmapToStream(bitmap);
            Log.d("ukuran gambar",String.valueOf(fileInputStream.available()));
            String param = "type="+tipefile;
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"iklan\";filename=\""
                    +file+"\""+lineEnd+"");

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            Log.d("ukuran buffer ",String.valueOf(bufferSize));
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //Log.d("ukuran buffer -- ",String.valueOf(bufferSize)+" - "+String.valueOf(bytesAvailable));
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                //Log.d("byte read",String.valueOf(bytesRead));
            }
            //Log.d("ukuran buffer terakhir ",String.valueOf(fileInputStream.available()));
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"tipe\""+lineEnd+"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("df");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"idpesan\""+lineEnd+"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("dfddfdfafd");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens);

            fileInputStream.close();
            dos.flush();
            dos.close();
            BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = buf.readLine()) != null) {
                sb.append(line);
            }
            buf.close();
            respon = sb.toString();
        }catch (Exception e){
            respon = "error "+e.getMessage();
        }
        return respon;
    }
    public ByteArrayInputStream bitmapToStream(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap = bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }
    private Context context;
    private Uri uri;
    public UploadFile(Context context,Uri uri){
        this.context = context;
        this.uri     = uri;
    }
    public UploadFile(Context context){
        this.context = context;
    }
    public void uploadIklan(final String file, final Bitmap bitmap, final MoreDatListener listener){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return uploadIklan(file+".jpeg",context,bitmap);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.setelahAmbilData(s);
                Toast.makeText(context, "Iklan sudah  diupload", Toast.LENGTH_SHORT).show();

            }
        }.execute();
    }

    public void lakukanUpload(final MoreDatListener listener){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return uploadKeServer(context,uri,uri.toString());
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.setelahAmbilData("sukses");
            }
        }.execute();
    }
    public void loadIklanFromserver(final String iklan, final ImageView imageView){
        new AsyncTask<Void,Void,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return getBitmapFromURL(Http.url+"template/iklan/"+iklan);
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                try {
                    File ff =new File(context.getCacheDir()+"/gambar");
                    if(ff.exists() == false){
                        ff.mkdirs();
                    }
                    File file = new File(context.getCacheDir()+"/gambar",iklan);
                    FileOutputStream stream = new FileOutputStream(file);
                    //  Bitmap bitmapsimpan =  getResizedBitmap(s)
                    s.compress(Bitmap.CompressFormat.JPEG,100,stream);
                   // new AppDetail(context).setIklan(true,iklan);
                    imageView.setImageBitmap(s);
                }catch (Exception e){
                }
            }
        }.execute();

    }
    public void loadIklanFromserver(final String iklan, final MoreDatListener listener){
        new AsyncTask<Void,Void,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return getBitmapFromURL(Http.url+"template/iklan/"+iklan);
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                try {
                    File folderUtama = new File(context.getCacheDir().getAbsolutePath()+"/gambar");
                    if(folderUtama.exists()==false){
                        folderUtama.mkdirs();
                    }

                    File file = new File(context.getCacheDir()+"/gambar",iklan);
                    FileOutputStream stream = new FileOutputStream(file);
                    //  Bitmap bitmapsimpan =  getResizedBitmap(s)
                    s.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    listener.setelahAmbilData(s.toString());
                    // new AppDetail(context).setIklan(true,iklan);
                }catch (Exception e){
                    Log.d("e",e.getMessage());
                }
            }
        }.execute();
    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String tes(String urldownload){
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
          //  String urls = URLEncoder.encode("k","UTF-8");
            URL url = new URL("http://kimsmembers.com/template/file_excel/softskill.pdf");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-agent", System.getProperty("http.agent"));
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            File folderDownload = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/kimsfile/");
            if(folderDownload.exists() == false){
                folderDownload.mkdirs();
            }
            output = new FileOutputStream(folderDownload+"/eee.pdf");

            byte data[] = new byte[14096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                  //  publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();

            input.close();
        } catch (Exception e) {
            Log.d("error http",e.getMessage());
            return e.toString();
        } /*finally {
            try {
                if (output != null)
                if (input != null)
            } catch (IOException ignored) {
                return ignored.getMessage();
            }

            if (connection != null)
                connection.disconnect();
        }*/
        return null;
    }
    public String downloadFile(String urldownload){
        try {
            URL url = new URL("http://kimsmembers.com/template/file_excel/"+urldownload);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            File folderDownload = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/kimsfile/");
            if(folderDownload.exists() == false){
                folderDownload.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(folderDownload+"/"+urldownload);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[4096];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (IOException e) {
            Log.e("Abhan", "Error: " + e);
        }
        Log.i("Abhan", "Check Your File.");
        return  null;
    }

}
