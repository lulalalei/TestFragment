package com.example.administrator.day03testfragment.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.day03testfragment.cans.Cans;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/12/6.
 */

public class ShareSPUtils {

    //记录用户登录状态
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;
    public static Context mContext;

    public static void initShareSP(Context context)
    {
        //记录用户登录状态---------------------放在欢迎页面
        sp = context.getSharedPreferences("usersinfo", MODE_PRIVATE);
        edit = sp.edit();
        mContext=context;
    }
    public static void readShareSP(TextView userName, ImageView userIcon,ImageView userIconToolbar,Context nContext)
    {
        if (sp!=null)
        {
            Cans.hasLogined=sp.getBoolean("hasLogined",false);
            if (!Cans.hasLogined)//用户未登录执行的逻辑
            {
                String parentPath= Cans.getDefaultUsersIconFile();
                File file = new File(parentPath);
                String usersIconPath=new File(file,Cans.userIconDefault.substring(Cans.userIconDefault.lastIndexOf("/")+1)).getAbsolutePath();
               /* if (BitmapFactory.decodeFile(usersIconPath)!=null)
                {*/
                    userIcon.setImageBitmap(BitmapFactory.decodeFile(usersIconPath));
                    userIconToolbar.setImageBitmap(BitmapFactory.decodeFile(usersIconPath));
                /*}*/
               /* //默认头像等
                userIcon.setImageBitmap(BitmapFactory.decodeFile(HttpUtils.iconPath));
                userIconToolbar.setImageBitmap(BitmapFactory.decodeFile(HttpUtils.iconPath));*/
               // userIcon.setImageResource(R.mipmap.ic_launcher);
                userName.setText("用户未登录显示的文本");
            }
            else //用户已登录执行的逻辑
            {
                Cans.userName=sp.getString("userName",null);
                Cans.userPwd=sp.getString("userPwd",null);
                Cans.userIcon=sp.getString("userIcon",null);
                //自定义头像等,,从本地文件加载图片
                userIcon.setImageBitmap(BitmapFactory.decodeFile(Cans.userIcon));
                userIconToolbar.setImageBitmap(BitmapFactory.decodeFile(Cans.userIcon));
                //userIcon.setImageResource(R.mipmap.ic_launcher);
                userName.setText("用户已经登录显示的文本:"+ Cans.userName);
            }
        }
        else
        {
            Toast.makeText(nContext,"sharePreference建立失败",Toast.LENGTH_SHORT).show();
        }
    }

    public static String writeShareSp(boolean loginFlag,String userName,String pwd)
    {
        String parentPath= Cans.getDefaultUsersIconFile();
        File file = new File(parentPath);
        String usersIconPath=new File(file,Cans.userIconDefault.substring(Cans.userIconDefault.lastIndexOf("/")+1)).getAbsolutePath();
        ShareSPUtils.edit.putBoolean("hasLogined",loginFlag);
        ShareSPUtils.edit.putString("userName",userName);
        ShareSPUtils.edit.putString("userPwd",pwd);
        ShareSPUtils.edit.putString("userIcon", usersIconPath);
        ShareSPUtils.edit.commit();
        return usersIconPath;
    }
    public static void resetShareSP()
    {
        if (sp!=null&&edit!=null)
        {
            /*edit.putBoolean("hasLogined",false);
            edit.putString("userName",null);
            edit.putString("userPwd",null);
            String parentPath= Cans.getDefaultUsersIconFile();
            File file = new File(parentPath);
            String usersIconPath=new File(file,Cans.userIconDefault.substring(Cans.userIconDefault.lastIndexOf("/")+1)).getAbsolutePath();
            edit.putString("userIcon", usersIconPath);
            edit.commit();*/
            writeShareSp(false,null,null);
        }
    }
}
