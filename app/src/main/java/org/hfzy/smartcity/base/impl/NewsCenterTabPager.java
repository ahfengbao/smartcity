package org.hfzy.smartcity.base.impl;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.TabPageIndicator;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.activity.HomeActivity;
import org.hfzy.smartcity.base.BaseHomeTabPager;
import org.hfzy.smartcity.base.BaseNewsCenterNewsTabPager;
import org.hfzy.smartcity.base.GroupPhotoPager;
import org.hfzy.smartcity.domain.NewsCenterInfo;
import org.hfzy.smartcity.global.GlobalConstants;
import org.hfzy.smartcity.utils.CacheUtils;
import org.hfzy.smartcity.utils.Md5Utils;
import org.hfzy.smartcity.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻中心Tab页
 */

public class NewsCenterTabPager extends BaseHomeTabPager implements ViewPager.OnPageChangeListener {


    public ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    public List<BaseNewsCenterNewsTabPager> list;
    private NewsCenterInfo newsCenterInfo;
    private Map<Integer, View> views;
    private GroupPhotoPager groupPhotoPager;

    public NewsCenterTabPager(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        ibMenu.setVisibility(View.INVISIBLE);
        ibSlidingMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("新闻");

        //主界面
        View view = View.inflate(context, R.layout.news_center_tab, null);
        viewPager = (ViewPager) view.findViewById(R.id.vp_news_info);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tap_page);
        ImageButton ibNext = (ImageButton) view.findViewById(R.id.ib_next);
        //下一页
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == viewPager.getAdapter().getCount() - 1) {
                    //已经在最后
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                viewPager.setCurrentItem(currentItem);
            }
        });
        addView(view);

        views = new HashMap<Integer, View>();
        //加载的界面放在集合里
        views.put(0, view);
        tabPageIndicator.setOnPageChangeListener(this);
    }


    @Override
    public void setListener() {

    }

    @Override
    public void loadData() {
        final String name = Md5Utils.encode(GlobalConstants.NEWSCENTER_URL);
        httpUtils = new HttpUtils(3000);
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.NEWSCENTER_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                CacheUtils.saveCache(context, name, result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showToast(context, "获取数据失败");
                String result = CacheUtils.readCache(context, name);
                processData(result);
            }
        });
    }

    @Override
    public void processData(String result) {
        gson = new Gson();
        newsCenterInfo = gson.fromJson(result, NewsCenterInfo.class);
        //Log.i(GlobalConstants.TAG, newsCenterInfo.toString());
        //两块  1.菜单界面    2.主页界面
        //菜单界面
        ((HomeActivity) context).homeMenuFragment.setList(newsCenterInfo.data);


        //绑定数据
        bindDataToView();
    }

    @Override
    public void bindDataToView() {
        //新闻标题
        list = new ArrayList<BaseNewsCenterNewsTabPager>();
        for (NewsCenterInfo.NewsCenterNewsInfo info : newsCenterInfo.data.get(0).children) {
            list.add(new BaseNewsCenterNewsTabPager(context));
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    public void addView(View view) {
        frame_content.addView(view);
    }

    @Override
    public void setTilte(int position) {
        tvTitle.setText(newsCenterInfo.data.get(position).title);
    }

    @Override
    public void addView(int position) {
        View view = views.get(position);//获取原来存储的view
        if (view != null) {
            frame_content.addView(view);
        } else {
            //加载布局
            groupPhotoPager = new GroupPhotoPager(context);
            views.put(position, groupPhotoPager.view);
            frame_content.addView(groupPhotoPager.view);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        SlidingMenu slidingMenu = ((HomeActivity) context).getSlidingMenu();
        //Log.i(GlobalConstants.TAG,list.get(viewPager.getCurrentItem()).vp.getCurrentItem()+"");
        if (viewPager.getCurrentItem() == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    /**
     * 新闻tap标题适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (list == null)
                return 0;
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return newsCenterInfo.data.get(0).children.get(position).title;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position).view;
            //加载对应新闻中心新tab的数据
            //1 获取对应的数据的url  2 加载数据
            //服务器返回的路径/10007/list_1.json
            String path = newsCenterInfo.data.get(0).children.get(position).url;
            String url = GlobalConstants.HOST + path;

            list.get(position).loadData(url);
            viewPager.addView(view);
            //Log.i(GlobalConstants.TAG,url);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = list.get(position).view;
            viewPager.removeView(view);
        }
    }

}
