package com.example.administrator.day03testfragment.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.day03testfragment.sqlite.MySqlite;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MyUsersSqlite {

    public static MySqlite help;
    public static SQLiteDatabase db ;

    public static void initUsersdb(Context context)
    {
        help = new MySqlite(context, "usersdb");
        db = help.getReadableDatabase();
    }
}
