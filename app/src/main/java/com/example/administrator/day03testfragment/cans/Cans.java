package com.example.administrator.day03testfragment.cans;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/12/6.
 */

public class Cans {
    //记录用户登录状态
    public static boolean hasLogined;
    //记录用户名
    public static String userName;
    //记录用户登录密码
    public static String userPwd;
    //用户头像
    public static String userIcon;
    //默认用户头像
    public static String userIconDefault="http://pic61.nipic.com/file/20150304/20245617_095937129615_2.jpg";

    public static String getDefaultUsersIconFile()
    {
        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+
                File.separator+"huibx"/*+System.currentTimeMillis()+".jpg"*/;
        return path;
    }
}
