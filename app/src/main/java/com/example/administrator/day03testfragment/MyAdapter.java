package com.example.administrator.day03testfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MyAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> list;
    private String [] title;

    public MyAdapter(FragmentManager fm, ArrayList<Fragment> list, String[] title) {
        super(fm);
        this.list = list;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
