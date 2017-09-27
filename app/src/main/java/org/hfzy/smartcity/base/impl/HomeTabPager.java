package org.hfzy.smartcity.base.impl;

import android.content.Context;
import android.view.View;

import org.hfzy.smartcity.base.BaseHomeTabPager;

/**
 * Created by fengbao on 2017/6/19.
 */

public class HomeTabPager extends BaseHomeTabPager {


    public HomeTabPager(Context context) {
        super(context);
    }

    @Override
    public void switchState() {

    }

    @Override
    public void initView() {
        ibMenu.setVisibility(View.INVISIBLE);
        ibSlidingMenu.setVisibility(View.INVISIBLE);
        tvTitle.setText("主页");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void processData(String result) {

    }

    @Override
    public void bindDataToView() {

    }

    @Override
    public void addView(View view) {

    }

    @Override
    public void setTilte(int position) {

    }

    @Override
    public void addView(int position) {

    }
}
