package com.example.administrator.day03testfragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/23.
 */

public class ContentFragment extends BaseFragment{

    LinearLayout container_layout;
   // Boolean firstFlag,secondFlag,thirdFlag,fourthFlag,fifthFlag;

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view=inflater.inflate(R.layout.fragment_content,container,false);
        container_layout= (LinearLayout) view.findViewById(R.id.container_layout);
        return view;
    }

    @Override
    public void initData() {
        int position = getArguments().getInt("position", -1);
        switch (position)
        {
            case 0:
                if (FirstFragment.getInstance().isAdded())
                {
                    getChildFragmentManager().beginTransaction().hide(FourthFragment.getInstance()).show(FirstFragment.getInstance()).commit();
                }
                else
                {
                    getChildFragmentManager().beginTransaction().add(R.id.container_layout,new FirstFragment()).commit();

                }
               // container_layout.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                break;
            case 2:
                container_layout.setBackgroundColor(Color.RED);
                break;
            case 3:
                if (FourthFragment.getInstance().isAdded())
                {
                    getChildFragmentManager().beginTransaction().hide(FirstFragment.getInstance()).show(FourthFragment.getInstance()).commit();
                }
                else
                {
                    getChildFragmentManager().beginTransaction().add(R.id.container_layout,FourthFragment.getInstance()).commit();
                }
                break;
            case 4:
                container_layout.setBackgroundColor(Color.GRAY);
                break;
           default:
               break;
        }
    }
}
