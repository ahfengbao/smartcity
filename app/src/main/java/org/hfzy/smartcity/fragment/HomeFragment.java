package org.hfzy.smartcity.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.activity.HomeActivity;
import org.hfzy.smartcity.base.BaseHomeTabPager;
import org.hfzy.smartcity.base.impl.GovaffairsTabPager;
import org.hfzy.smartcity.base.impl.HomeTabPager;
import org.hfzy.smartcity.base.impl.NewsCenterTabPager;
import org.hfzy.smartcity.base.impl.SettingTabPager;
import org.hfzy.smartcity.base.impl.SmartServiceTabPager;
import org.hfzy.smartcity.view.NoScollViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengbao on 2017/6/18.
 * 主界面
 * 1.viewpager
 * 2.radiobuttn
 */

public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "HomeFragment";
    private NoScollViewpager vpContent;
    public List<BaseHomeTabPager> list;
    private RadioGroup rg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //添加数据
        list = new ArrayList<BaseHomeTabPager>();
        list.add(new HomeTabPager(getActivity()));
        list.add(new NewsCenterTabPager(getActivity()));
        list.add(new SmartServiceTabPager(getActivity()));
        list.add(new GovaffairsTabPager(getActivity()));
        list.add(new SettingTabPager(getActivity()));

        //viewpager内容页面
        vpContent = (NoScollViewpager) getActivity().findViewById(R.id.vp_content);
        MyPagerAdapter adapter = new MyPagerAdapter();
        vpContent.setAdapter(adapter);

        //设置RadioGroup监听
        rg = (RadioGroup) getActivity().findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);
    }

    //内容页面的切换，根据RadioButtn的选择
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int item = 0;
        SlidingMenu slidingMenu = ((HomeActivity) getActivity()).getSlidingMenu();
        //Log.i(TAG, "SHOW");
        switch (checkedId) {
            case R.id.rb_home:
                item = 0;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
            case R.id.rb_news_center:
                item = 1;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_smart_service:
                item = 2;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_govaffairs:
                item = 3;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_setting:
                item = 4;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
            default:
                break;
        }
        vpContent.setCurrentItem(item);     //页面切换
    }

    //操作标题
    public void operatorTitle(int position){
        //获取当前页面的下标
        int currentItem = vpContent.getCurrentItem();
        //获取当前页面
        BaseHomeTabPager baseHomeTabPager = list.get(currentItem);
        baseHomeTabPager.setTilte(position);

        //关闭侧滑菜单
        ((HomeActivity)getActivity()).getSlidingMenu().toggle();

        //操作
        baseHomeTabPager.frame_content.removeAllViews();
        baseHomeTabPager.addView(position);

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position).root;
            vpContent.addView(view);
            list.get(position).loadData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = list.get(position).root;
            vpContent.removeView(view);
        }

    }
}
