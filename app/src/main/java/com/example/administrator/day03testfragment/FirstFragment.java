package com.example.administrator.day03testfragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Administrator on 2016/11/23.
 */

public class FirstFragment extends BaseFragment {

    AutoScrollViewPager autoscroll;
    LinearLayout dot_container;
    int prePosition;
    public String[] imgurl = new String[]{
            "http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg"
    };
    ArrayList<View> imgList = new ArrayList<>();
    ArrayList<View> dotList = new ArrayList<>();
    private static FirstFragment firstFragment;

    public static FirstFragment getInstance() {
        if (firstFragment == null) {
            firstFragment = new FirstFragment();
        }
        return firstFragment;
    }

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        autoscroll = (AutoScrollViewPager) view.findViewById(R.id.autoscroll);
        dot_container = (LinearLayout) view.findViewById(R.id.dot_container);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData() {
        for (int i = 0; i < imgurl.length; i++) {
            ImageView imageView = new ImageView(mContext);
            Picasso.with(mContext).load(imgurl[i]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgList.add(imageView);
            ImageView dotView = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
            lp.leftMargin = 10;
            dotView.setLayoutParams(lp);
            dotList.add(dotView);
            dot_container.addView(dotView);
            if (i == 0) {
                dotList.get(i).setBackground(mContext.getResources().getDrawable(R.drawable.dot_selected));
            }
            else
            {
                dotList.get(i).setBackground(mContext.getResources().getDrawable(R.drawable.dot_unselected));
            }
        }
        AdAdapter adAdapter = new AdAdapter(imgList);
        autoscroll.setAdapter(adAdapter);
        autoscroll.startAutoScroll();
        autoscroll.setBorderAnimation(false);
        autoscroll.setInterval(3000);
        autoscroll.setCycle(true);
        autoscroll.setStopScrollWhenTouch(true);

        autoscroll.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                dotList.get(prePosition).setBackground(mContext.getResources().getDrawable(R.drawable.dot_unselected));
                dotList.get(position).setBackground(mContext.getResources().getDrawable(R.drawable.dot_selected));
                prePosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
