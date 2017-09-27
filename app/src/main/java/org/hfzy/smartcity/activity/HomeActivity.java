package org.hfzy.smartcity.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.base.BaseNewsCenterNewsTabPager;
import org.hfzy.smartcity.base.impl.NewsCenterTabPager;
import org.hfzy.smartcity.fragment.HomeFragment;
import org.hfzy.smartcity.fragment.HomeMenuFragment;
import org.hfzy.smartcity.global.GlobalConstants;
import org.hfzy.smartcity.utils.PrefUtils;

import java.util.List;

public class HomeActivity extends SlidingFragmentActivity {

    private SlidingMenu slidingMenu;
    public HomeFragment homeFragment;
    public HomeMenuFragment homeMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //指定主页面
        setContentView(R.layout.activity_home);
        setBehindContentView(R.layout.activity_home_menu);
        slidingMenu = getSlidingMenu();
        //设置菜单模式在左侧
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置全屏触摸滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //设置剩余空间
        slidingMenu.setBehindOffset(250);
        //渐入渐出
        //slidingMenu.setFadeDegree(0.35f);

        //记录入过向导页面了
        PrefUtils.setBoolean(getApplicationContext(), GlobalConstants.PREF_GUDIE, true);

        initFragment();
    }



    private void initFragment() {
        //替换主界面
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, homeFragment).commit();
        //替换菜单栏s
        homeMenuFragment = new HomeMenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_menu, homeMenuFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //当前选中页面为主题
       if ( homeMenuFragment.selectedPositon==0){
           //获取新闻中tab页
           NewsCenterTabPager newsCenterTabPager = (NewsCenterTabPager) homeFragment.list.get(1);
           if (newsCenterTabPager!=null){
               ViewPager viewPager = newsCenterTabPager.viewPager;
               if (viewPager!=null){
                   int currentItem = viewPager.getCurrentItem();
                   //获取当前的tab标签，停止当前tab标签下的ViewPager停止切换
                   List<BaseNewsCenterNewsTabPager> list = newsCenterTabPager.list;
                   if (list!=null){
                      list.get(currentItem).startSwitch();
                   }
               }
           }
       }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当前选中页面为主题
        if ( homeMenuFragment.selectedPositon==0){
            //获取新闻中tab页
            NewsCenterTabPager newsCenterTabPager = (NewsCenterTabPager) homeFragment.list.get(1);
            if (newsCenterTabPager!=null){
                ViewPager viewPager = newsCenterTabPager.viewPager;
                if (viewPager!=null){
                    int currentItem = viewPager.getCurrentItem();
                    //获取当前的tab标签，停止当前tab标签下的ViewPager停止切换
                    List<BaseNewsCenterNewsTabPager> list = newsCenterTabPager.list;
                    if (list!=null){
                        list.get(currentItem).stopSwitch();
                    }
                }
            }
        }
    }
}
