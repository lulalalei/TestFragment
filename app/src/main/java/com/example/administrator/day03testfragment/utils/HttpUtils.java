package com.example.administrator.day03testfragment.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.administrator.day03testfragment.cans.Cans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/6.
 */

public class HttpUtils {

    public static String iconPath="11111";
    /*
    * 将图片存到本地指定文件*/
    public static String downloadFileByConnection(final String strUrl, final Context context)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String parentPath=null;
                FileOutputStream fos=null;
                ByteArrayOutputStream baos=null;
                try {
                    URL url = new URL(strUrl);
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
            }
        }).start();
        return iconPath;
    }
}
