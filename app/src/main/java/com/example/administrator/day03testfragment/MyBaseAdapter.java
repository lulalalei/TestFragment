package com.example.administrator.day03testfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MyBaseAdapter extends BaseAdapter{

    ArrayList<String> list;
    Context mContext;
    LayoutInflater inflater;

    public MyBaseAdapter(ArrayList<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.fourth_item_layout,viewGroup,false);
            holder=new ViewHolder();
            holder.text_tv= (TextView) convertView.findViewById(R.id.text_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.text_tv.setText(list.get(position));
        return convertView;
    }
    static class ViewHolder{
        TextView text_tv;
    }
}
