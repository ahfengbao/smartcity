package org.hfzy.smartcity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.hfzy.smartcity.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {


    private static final String TAG = "GuideActivity";
    private ViewPager vpGuide;
    private int imgs[] = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> list;
    private Button btnEnterHome;
    private LinearLayout llPoint;
    private View redPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        btnEnterHome = (Button) findViewById(R.id.btn_enterhome);
        llPoint = (LinearLayout) findViewById(R.id.ll_point);
        redPoint = (View) findViewById(R.id.red_point);
        list = new ArrayList<ImageView>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            list.add(iv);
        }
        vpGuide.setAdapter(new MyPagerAdapter());


        //初始化灰色的点
        for (int resId : imgs) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.rightMargin = 20;//指定右边距
            view.setBackgroundResource(R.drawable.shape_point_gray);
            llPoint.addView(view, params);
        }
        MyViewPagerLinster myViewPagerLinster =new MyViewPagerLinster();
        vpGuide.setOnPageChangeListener(myViewPagerLinster);

    }

    //开始体验
    public void enterhome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }


    //两点之间的距离
    private int width = 0;

    //ViewPager监听器
    private class MyViewPagerLinster implements ViewPager.OnPageChangeListener {

        //滑动监听
        //int position, 当前选中的页面下标
        //float positionOffset,   页面滑动偏移量的比率  0 - 1
        //int positionOffsetPixels  页面偏移的像素
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Log.i(TAG,"position"+position+"positionOffset"+positionOffset+"positionOffsetPixels"+positionOffsetPixels);
            //Log.i(TAG,llPoint.getChildAt(1).getLeft()+"");
            //Log.i(TAG,llPoint.getChildAt(0).getLeft()+"");
            if (width == 0) {
                width = llPoint.getChildAt(1).getLeft() - llPoint.getChildAt(0).getLeft();
                //Log.i(TAG,"width:"+width);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(10, 10);
            params.leftMargin = (int) (position * width + positionOffset * width);
            //Log.i(TAG,"left:"+params.leftMargin+"");
            redPoint.setLayoutParams(params);
        }

        //当前页面选择
        @Override
        public void onPageSelected(int position) {
            int visibility = View.GONE;
            if (position == imgs.length - 1) {
                visibility = View.VISIBLE;
                btnEnterHome.setVisibility(visibility);
            } else {
                visibility = View.GONE;
                btnEnterHome.setVisibility(visibility);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //ViewPager适配器
    private class MyPagerAdapter extends PagerAdapter {
        //实例化条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            vpGuide.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = list.get(position);
            vpGuide.removeView(view);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
