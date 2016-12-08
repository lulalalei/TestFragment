package com.example.administrator.day03testfragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.day03testfragment.R;
import com.example.administrator.day03testfragment.cans.Cans;
import com.example.administrator.day03testfragment.utils.HttpUtils;
import com.example.administrator.day03testfragment.utils.MyUsersSqlite;
import com.example.administrator.day03testfragment.utils.ShareSPUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {

    String iconPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

     /*   if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            String huiBXFile = Cans.getDefaultUsersIconFile();
            File file = new File(huiBXFile);
            if (!file.exists())
            {
                file.mkdirs();
            }
        }*/

        ShareSPUtils.initShareSP(this);
        MyUsersSqlite.initUsersdb(this);
        if (!ShareSPUtils.sp.getBoolean("hasLogined",false))
        {
            new MyAsynctask(this).execute(Cans.userIconDefault);

        }
        else {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class MyAsynctask extends AsyncTask<String,Void,String>{

        Context context;

        public MyAsynctask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String parentPath=null;
            FileOutputStream fos=null;
            ByteArrayOutputStream baos=null;
            try {
                URL url = new URL(Cans.userIconDefault);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if (conn.getResponseCode()==200)
                {
                    InputStream is = conn.getInputStream();
                    byte [] bytes=new byte[1024];
                    int len;
                    baos=new ByteArrayOutputStream();
                    while ((len=is.read(bytes))!=-1)
                    {
                        baos.write(bytes,0,len);
                        baos.flush();
                    }
                    byte[] array = baos.toByteArray();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        parentPath= Cans.getDefaultUsersIconFile();
                        File file = new File(parentPath);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        iconPath=new File(file,Cans.userIconDefault.substring(Cans.userIconDefault.lastIndexOf("/")+1)).getAbsolutePath();
                        //iconPath=parentPath+ File.separator+System.currentTimeMillis()+".jpg";
                        fos = new FileOutputStream(iconPath);
                        fos.write(array);
                        fos.close();
                    }
                    else
                    {
                        Toast.makeText(context,"请检查sdk",Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (baos!=null)
                {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return iconPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpUtils.iconPath=iconPath;
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
