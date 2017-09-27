package org.hfzy.smartcity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fengbao on 2017/6/23.
 */

public class HorizontalSrcollViewPager extends ViewPager {
    public HorizontalSrcollViewPager(Context context) {
        super(context);
    }

    public HorizontalSrcollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int startX;
    int startY;

    //分发触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true); //请求父窗体不拦截事件
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY= (int) ev.getY();
                //计算差值
                int disX=startX-moveX;
                int disY=startY-moveY;
                //只是处理水平滑动
                if (Math.abs(disX)>Math.abs(disY)){
                    if (getCurrentItem()==getAdapter().getCount()-1&&disX<0){
                        getParent().requestDisallowInterceptTouchEvent(false); //垂直滑动事件不要处理
                    }else if (getCurrentItem()==0&&disX>0){
                        getParent().requestDisallowInterceptTouchEvent(false); //垂直滑动事件不要处理
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true); //请求父窗体不拦截事件
                    }

                }else{
                    getParent().requestDisallowInterceptTouchEvent(false); //垂直滑动事件不要处理
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
