package org.hfzy.smartcity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.hfzy.smartcity.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hfzy.smartcity.R.id.iv_arrow;
import static org.hfzy.smartcity.R.id.tv_state;
import static org.hfzy.smartcity.R.id.tv_time;

/**
 * 下拉刷新的ListView
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = RefreshListView.class.getSimpleName();
    private int lvLocationOnScreenY; //ListView在屏幕Y轴上的坐标
    private int footerHeight;
    private View footer;

    public interface OnRefreshListViewListener {
        //加载头信息
        public void loadHeadDataInfo();

        //加载脚信息
        public void loadFootDataInfo();
    }

    private OnRefreshListViewListener onRefreshListViewListener;

    public void setOnRefreshListViewListener(OnRefreshListViewListener onRefreshListViewListener) {
        this.onRefreshListViewListener = onRefreshListViewListener;
    }

    private LinearLayout root;
    @ViewInject(R.id.pb)
    private ProgressBar pb;

    @ViewInject(iv_arrow)
    private ImageView ivArrow;

    @ViewInject(tv_state)
    private TextView tvState;
    @ViewInject(tv_time)
    private TextView tvTime;

    @ViewInject(R.id.ll_head)
    public LinearLayout llHead;//ListView本身的头
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    private int llHeadHeight;


    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeader();
        initFooter();
        initAnimation();
        //给ListView设置滑动监听
        setOnScrollListener(this);
    }

    private void initFooter() {
        footer = View.inflate(getContext(), R.layout.listview_footer, null);
        footer.measure(0, 0);
        footerHeight = footer.getMeasuredHeight();
        footer.setPadding(0, -footerHeight, 0, 0);
        addFooterView(footer);
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(200);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(200);
        upAnimation.setFillAfter(true);
    }

    /**
     * 初始化头
     */
    private void initHeader() {
        root = (LinearLayout) View.inflate(getContext(), R.layout.listview_header, null);
        ViewUtils.inject(this, root);
        llHead.measure(0, 0);    //测量
        //获取测量的高度
        llHeadHeight = llHead.getMeasuredHeight();
        //填充使头隐藏
        root.setPadding(0, -llHeadHeight, 0, 0);
        //添加头
        addHeaderView(root);

    }

    private View extraView;

    /**
     * 添加轮播图
     *
     * @param view
     */
    public void addExtraView(View view) {
        root.addView(view);
        extraView = view;
    }

    private boolean isLoading = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && !isLoading
                && (getLastVisiblePosition() == getAdapter().getCount() - 1)
                ) {
            if (onRefreshListViewListener != null) {
                onRefreshListViewListener.loadFootDataInfo();
            } else {
                hideFooter();
            }
        }
    }

    public void hideFooter() {
        footer.setPadding(0, -footerHeight, 0, 0);
        isLoading = false;
    }

    private int firstVisibleItem;//屏幕上显示的ListView的第一个条目的下标

    //只有firstVisibleItem == 0 的时候就处理ListView的触摸事件
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    /**
     * ListView状态
     */
    private enum ListState {
        PULL_DOWN_REFRESH,     //释放刷新
        RELEASE_REFRESH,       //下拉刷新
        REFRESHING              //刷新中
    }

    private ListState state = ListState.PULL_DOWN_REFRESH;//默认是下拉刷新
    private int startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                //计算偏移量
                int distanceY = moveY - startY;
                if (lvLocationOnScreenY == -1) {
                    //获取ListView在屏幕上的位置
                    int[] location = new int[2];
                    getLocationOnScreen(location);
                    lvLocationOnScreenY = location[1];
                }
                if (extraView != null) {
                    int[] location = new int[2];
                    extraView.getLocationOnScreen(location);
                    int extraViewLocationOnScreen = location[1];
                    if (extraViewLocationOnScreen < lvLocationOnScreenY) {
                        break;
                    }
                }
                if (firstVisibleItem == 0 && distanceY > 0) {//自己处理触摸事件   只处理下拉事件
                    int top = -llHeadHeight + distanceY;
                    if (top > 0 && state == ListState.PULL_DOWN_REFRESH) {
                        Log.d(TAG, "释放刷新");
                        //切换为释放刷新
                        ivArrow.startAnimation(upAnimation);
                        tvState.setText("释放刷新");
                        state = ListState.RELEASE_REFRESH;
                    } else if (top <= 0 && state == ListState.RELEASE_REFRESH) {
                        Log.d(TAG, "下拉刷新");
                        //切换为下拉刷新
                        ivArrow.startAnimation(downAnimation);
                        tvState.setText("下拉刷新");
                        state = ListState.PULL_DOWN_REFRESH;
                    }
                    root.setPadding(0, top, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == ListState.PULL_DOWN_REFRESH) {
                    //把头隐藏
                    root.setPadding(0, -llHeadHeight, 0, 0);
                } else if (state == ListState.RELEASE_REFRESH) {
                    //正在加载
                    state = ListState.REFRESHING;
                    tvState.setText("正在加载");
                    ivArrow.clearAnimation();
                    ivArrow.setVisibility(View.INVISIBLE);
                    pb.setVisibility(View.VISIBLE);
                    root.setPadding(0, 0, 0, 0);
                }
                if (onRefreshListViewListener != null) {
                    onRefreshListViewListener.loadHeadDataInfo();
                } else {
                    //隐藏头
                    hideHeader();
                }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 隐藏头
     */
    public void hideHeader() {
        ivArrow.setVisibility(INVISIBLE);
        pb.setVisibility(INVISIBLE);
        tvState.setText("下拉刷新");
        state = ListState.RELEASE_REFRESH;
        //刷新时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long millis = System.currentTimeMillis();
        String date = sdf.format(new Date(millis));
        tvTime.setText(date);

        //设置回缩
        root.setPadding(0, -llHeadHeight, 0, 0);
    }

}
