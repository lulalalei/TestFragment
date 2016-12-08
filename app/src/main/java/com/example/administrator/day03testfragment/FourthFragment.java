package com.example.administrator.day03testfragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/23.
 */

public class FourthFragment extends BaseFragment {

    PullToRefreshListView listView;
    ArrayList<String> list = new ArrayList<>();
    MyBaseAdapter adapter;
    int count;

    private static FourthFragment fourthFragment;
    public static FourthFragment getInstance()
    {
        if (fourthFragment==null)
        {
            fourthFragment=new FourthFragment();
        }
        return fourthFragment;
    }

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fourth_fragment, container, false);
        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void initData() {
        for (int i = 0; i < 100; i++) {
            count = i;
            list.add("android" + count);
        }
        adapter = new MyBaseAdapter(list, mContext);
        listView.setAdapter(adapter);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullDownMethod();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullUpMethod();
            }
        });
    }

    private void pullUpMethod() {
        for (int i = 0; i < 100; i++) {
            count ++;
            list.add("android" + count);
        }
        adapter.notifyDataSetChanged();
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.onRefreshComplete();
            }
        },3000);
    }

    private void pullDownMethod() {
        list.clear();
        count=0;
        for (int i = 0; i < 100; i++) {
            count = i;
            list.add("android" + count);
        }
        adapter.notifyDataSetChanged();
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.onRefreshComplete();
            }
        },3000);
    }
}
