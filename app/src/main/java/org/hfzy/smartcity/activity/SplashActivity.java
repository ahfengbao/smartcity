package org.hfzy.smartcity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.global.GlobalConstants;
import org.hfzy.smartcity.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlSplash;
    private Handler mHander = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        rlSplash = (RelativeLayout) findViewById(R.id.activity_splash);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //创建动画，参数表示他的子动画是否共用一个插值器
        AnimationSet animationSet = new AnimationSet(true);

        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);


        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f,1f,0.1f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);

        //添加动画集合
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        //启动动画
        rlSplash.startAnimation(animationSet);

       animationSet.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) {

           }

           @Override
           public void onAnimationEnd(Animation animation) {
               mHander.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       if (PrefUtils.getBoolean(getApplicationContext(), GlobalConstants.PREF_GUDIE,false)){
                           enterHome();
                       }else{
                           enterGuide();
                       }
                   }
               },2000);
           }

           @Override
           public void onAnimationRepeat(Animation animation) {

           }
       });
    }

    //进入主界面
    private void enterHome() {
        startActivity(new Intent(this,HomeActivity.class));
        this.finish();
    }

    //进入向导界面
    private void enterGuide(){
        startActivity(new Intent(this,GuideActivity.class));
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHander.removeCallbacksAndMessages(null);
    }
}
