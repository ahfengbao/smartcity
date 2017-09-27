package org.hfzy.smartcity.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
    @ViewInject(R.id.ib_menu)
    public ImageButton ibMenu;


    public BaseHomeTabPager(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        //完成整体布局加载
        root = View.inflate(context, R.layout.base_home_tab_pager, null);
        ViewUtils.inject(this,root,true);
        frame_content= (FrameLayout) root.findViewById(R.id.tab_frame);
        //显示侧滑菜单
        ibSlidingMenu = (ImageButton) root.findViewById(R.id.ib_slidingmenu);
        ibSlidingMenu.setOnClickListener(this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        initView();
        setListener();
    }


    @Override
    public void onClick(View v) {
        ((HomeActivity) context).getSlidingMenu().toggle();
    }

    private static  final  int LIST_STATE=1;
    private static  final  int GRID_START=2;
    private int state = LIST_STATE;

    //切换按钮状态
    @OnClick(R.id.ib_menu)
    private void switchListState(View view){
        if (state==LIST_STATE){
            state=GRID_START;
            ibMenu.setBackgroundResource(R.drawable.icon_pic_list_type);
        }else{
            state=LIST_STATE;
            ibMenu.setBackgroundResource(R.drawable.icon_pic_grid_type);
        }
        switchState();
    }

    //抽象方法
    public abstract void switchState();


}
