package org.hfzy.smartcity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fengbao on 2017/6/19.
 * 1. 不带滑动的Viewpager
 */

public class NoScollViewpager extends LazyViewPager {
    public NoScollViewpager(Context context) {
        super(context);
    }

    public NoScollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;        //不拦截事件，让事件可以传递
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;       //不处理事件
    }
}
