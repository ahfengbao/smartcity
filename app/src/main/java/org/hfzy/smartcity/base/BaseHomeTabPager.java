package org.hfzy.smartcity.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.activity.HomeActivity;

/**
 * Created by fengbao on 2017/6/19.
 *
 */

public abstract class BaseHomeTabPager implements BaseOperator, View.OnClickListener {

    public Context context;
    public HttpUtils httpUtils;
    public BitmapUtils bitmapUtils;
    public Gson gson;
    public View root;
    public FrameLayout frame_content;//占位  标签页的内容  后面进行处理
    public TextView tvTitle;
    public ImageButton ibSlidingMenu;
    public ImageButton ibMenu;


    public BaseHomeTabPager(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        //完成整体布局加载
        root = View.inflate(context, R.layout.base_home_tab_pager, null);
        frame_content= (FrameLayout) root.findViewById(R.id.tab_frame);
        //显示侧滑菜单
        ibSlidingMenu = (ImageButton) root.findViewById(R.id.ib_slidingmenu);
        ibMenu = (ImageButton) root.findViewById(R.id.ib_menu);
        ibSlidingMenu.setOnClickListener(this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        initView();
        setListener();
    }


    @Override
    public void onClick(View v) {
        ((HomeActivity) context).getSlidingMenu().toggle();
    }


}
