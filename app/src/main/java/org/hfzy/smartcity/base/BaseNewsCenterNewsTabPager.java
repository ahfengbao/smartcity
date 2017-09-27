package org.hfzy.smartcity.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.activity.HomeActivity;
import org.hfzy.smartcity.activity.NewsDetailActivity;
import org.hfzy.smartcity.domain.NewsCenterNewsInfo;
import org.hfzy.smartcity.global.GlobalConstants;
import org.hfzy.smartcity.utils.CacheUtils;
import org.hfzy.smartcity.utils.Md5Utils;
import org.hfzy.smartcity.utils.PrefUtils;
import org.hfzy.smartcity.utils.StringUtils;
import org.hfzy.smartcity.utils.ToastUtils;
import org.hfzy.smartcity.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengbao on 2017/6/22.
 */

public class BaseNewsCenterNewsTabPager implements ViewPager.OnPageChangeListener, View.OnTouchListener, AdapterView.OnItemClickListener, RefreshListView.OnRefreshListViewListener {
    private Context context;
    public View view;
    public HttpUtils httpUtils;
    public Gson gson;
    @ViewInject(R.id.vp)
    public ViewPager vp;
    @ViewInject(R.id.tv_title)
    public TextView tvTitle;
    public BitmapUtils bitmapUtils;
    public NewsCenterNewsInfo newsCenterNewsInfo;
    private List<ImageView> list;  //轮播图片
    @ViewInject(R.id.ll_point)
    private LinearLayout llPiont;
    private MyBaseAdapter myBaseAdapter;
    @ViewInject(R.id.lv)
    private RefreshListView lv;

    public BaseNewsCenterNewsTabPager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        bitmapUtils = new BitmapUtils(context);
        httpUtils = new HttpUtils();
        gson = new Gson();
        initView();
    }

    private void initView() {
        view = View.inflate(context, R.layout.base_newscenter_news_tab, null);
        ViewUtils.inject(this, view);

        //动态的把轮播图加载
        View extraView = View.inflate(context, R.layout.news_topic_image, null);
        ViewUtils.inject(this, extraView);
        lv.addExtraView(extraView);

        //设置下拉刷新监听
        lv.setOnRefreshListViewListener(this);

    }

    public void loadData(final String url) {
        Log.e("aaa","加载数据");
        final String name = Md5Utils.encode(url);
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //Log.i(GlobalConstants.TAG,result);
                processData(result);
                CacheUtils.saveCache(context,name,result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showToast(context, "获取新闻信息失败");
                String cache = CacheUtils.readCache(context, name);
                Log.e("aaa","获取数据失败");
                processData(cache);
            }
        });
    }

    /**
     * 加载数据
     *
     * @param data
     */
    private void processData(String data) {
        newsCenterNewsInfo = gson.fromJson(data, NewsCenterNewsInfo.class);
        list = new ArrayList<ImageView>();
        //加载轮播图
        for (NewsCenterNewsInfo.TopicNewsInfo topicNewsInfo : this.newsCenterNewsInfo.data.topnews) {
            String path = topicNewsInfo.topimage;
            String url = GlobalConstants.HOST + StringUtils.getPath(path);
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充整个控件
            bitmapUtils.display(iv, url);
            iv.setTag(topicNewsInfo);
            list.add(iv);
            iv.setOnTouchListener(this);
        }
        //Log.i(GlobalConstants.TAG,data);
        MyPagerAdapter adapter = new MyPagerAdapter();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(this);
        //给标题设置初始值
        tvTitle.setText(newsCenterNewsInfo.data.topnews.get(0).title);
        //初始化小圆点
        llPiont.removeAllViews();
        for (NewsCenterNewsInfo.TopicNewsInfo info : newsCenterNewsInfo.data.topnews) {
            View view = new View(context);
            view.setBackgroundResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            params.rightMargin = 10;
            llPiont.addView(view, params);
        }
        llPiont.getChildAt(0).setBackgroundResource(R.drawable.shape_point_red);
        //开始切换
        startSwitch();

        //加载条目新闻
        myBaseAdapter = new MyBaseAdapter();
        lv.setAdapter(myBaseAdapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsCenterNewsInfo.ListNewsInfo info = (NewsCenterNewsInfo.ListNewsInfo) myBaseAdapter.getItem(position - 1);
        //点击条目进入如到新闻的详细页面
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("url", info.url);
        context.startActivity(intent);

        //记录已读新闻的
        //把新闻的唯一标示存在sp里面
        String has_read_news = PrefUtils.getString(context, GlobalConstants.PREF_HAS_READ_NEWS, "");
        if (!has_read_news.contains(info.id)) {
            //把该条新闻的id存储在sp里面
            has_read_news = "@" + info.id;
            PrefUtils.setString(context, GlobalConstants.PREF_HAS_READ_NEWS, has_read_news);
            myBaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadHeadDataInfo() {
        //加载更多
        String url = GlobalConstants.HOST + newsCenterNewsInfo.data.more;
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                NewsCenterNewsInfo info = gson.fromJson(result, NewsCenterNewsInfo.class);
                //获取列表新闻数据，并把数据添加到原来的集合中去。
                List<NewsCenterNewsInfo.ListNewsInfo> news = info.data.news;
                newsCenterNewsInfo.data.news.addAll(0, news);
                myBaseAdapter.notifyDataSetChanged();
                lv.hideHeader();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                lv.hideHeader();
            }
        });
    }

    @Override
    public void loadFootDataInfo() {
        //加载更多
        String url = GlobalConstants.HOST + newsCenterNewsInfo.data.more;
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                NewsCenterNewsInfo info = gson.fromJson(result, NewsCenterNewsInfo.class);
                //获取列表新闻数据，并把数据添加到原来的集合中去。
                List<NewsCenterNewsInfo.ListNewsInfo> news = info.data.news;
                newsCenterNewsInfo.data.news.addAll(news);
                myBaseAdapter.notifyDataSetChanged();
                lv.hideFooter();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                lv.hideFooter();
            }
        });
    }


    /**
     * 新闻条目适配器
     */
    private class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (newsCenterNewsInfo == null) {
                return 0;
            }
            return newsCenterNewsInfo.data.news.size();
        }

        @Override
        public Object getItem(int position) {
            return newsCenterNewsInfo.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {
                view = View.inflate(context, R.layout.base_news_info_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv);
                holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
                holder.ivComment = (ImageView) view.findViewById(R.id.iv_comment);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            NewsCenterNewsInfo.ListNewsInfo listNewsInfo = newsCenterNewsInfo.data.news.get(position);
            String hasReadNews = PrefUtils.getString(context, GlobalConstants.PREF_HAS_READ_NEWS, "");
            if (hasReadNews.contains(listNewsInfo.id)){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else{
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            holder.tvTitle.setText(listNewsInfo.title);
            holder.tvDate.setText(listNewsInfo.pubdate);
            String path = GlobalConstants.HOST + StringUtils.getPath(listNewsInfo.listimage);
            bitmapUtils.display(holder.iv, path);
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv;
        TextView tvTitle;
        TextView tvDate;
        ImageView ivComment;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvTitle.setText(newsCenterNewsInfo.data.topnews.get(position).title);
        int childCount = llPiont.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = llPiont.getChildAt(i);
            if (i == position) {
                view.setBackgroundResource(R.drawable.shape_point_red);
            } else {
                view.setBackgroundResource(R.drawable.shape_point_gray);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        SlidingMenu slidingMenu = ((HomeActivity) context).getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    //当按住图片的时候停止切换
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopSwitch();
                //Log.i(GlobalConstants.TAG,"按下");
                break;
            case MotionEvent.ACTION_MOVE:
                startSwitch();
                break;
            case MotionEvent.ACTION_UP:
                startSwitch();
                //Log.i(GlobalConstants.TAG,"抬起");;
                NewsCenterNewsInfo.TopicNewsInfo info = (NewsCenterNewsInfo.TopicNewsInfo) v.getTag();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url", info.url);
                context.startActivity(intent);
                break;
        }
        return true;        //自己处理事件
    }

    /**
     * 轮播图片适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (newsCenterNewsInfo == null) {
                return 0;
            }
            return newsCenterNewsInfo.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            vp.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            vp.removeView(list.get(position));
        }
    }

    private boolean isSwitch = false;    //是否在切换
    //处理点的切换
    private Handler mHandler = new Handler() {
    };

    private class SwitchTask implements Runnable {

        @Override
        public void run() {
            //切换点
            int currentItem = vp.getCurrentItem();
            if (currentItem == vp.getAdapter().getCount() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            vp.setCurrentItem(currentItem);
            mHandler.postDelayed(this, 3000);
        }
    }

    //开始点的切换
    public void startSwitch() {
        if (!isSwitch) {
            mHandler.postDelayed(new SwitchTask(), 3000);
            isSwitch = true;
        }

    }

    //停止点的切换
    public void stopSwitch() {
        if (isSwitch) {
            mHandler.removeCallbacksAndMessages(null);
            isSwitch = false;
        }

    }

}
