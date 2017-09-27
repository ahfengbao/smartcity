package org.hfzy.smartcity.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import org.hfzy.smartcity.R;
import org.hfzy.smartcity.global.GlobalConstants;
import org.hfzy.smartcity.utils.StringUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static org.hfzy.smartcity.R.id.webview;

/**
 * 新闻条目详情
 */
public class NewsDetailActivity extends AppCompatActivity {
    @ViewInject(webview)
    private WebView webView;

    @ViewInject(R.id.pb)
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_datail);
        ViewUtils.inject(this);

        String url = getIntent().getStringExtra("url");
        String path = GlobalConstants.HOST + StringUtils.getPath(url);

        webView.getSettings().setJavaScriptEnabled(true);       //设置允许运行JS脚本
        webView.loadUrl(path);
        //给WebView设置一个客户端
        webView.setWebViewClient(new WebViewClient() {
            //页面加载完毕调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void back(View v) {
        this.finish();
    }

    private int selectIndex=2;
    String[] items = new String[]{
            "超大号字体",
            "大号字体",
            "正常字体",
            "小号字体",
            "超小号字体"
    };

    private int[] textSize = new int[]{250,200,100,50,10};
    @OnClick(R.id.ib_textsize)
    public void setTextSize(View v) {
        //弹出设置字体对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("设置字体")
                .setSingleChoiceItems(items, selectIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectIndex=which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.getSettings().setTextZoom(textSize[selectIndex]);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @OnClick(R.id.ib_share)
    public void share(View v) {
        showShare();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://www.htc-iot.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("合职物联网");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        String path = Environment.getDataDirectory().getPath()+"/b2.jpg";
        try {
            String path = BitmapHelper.downloadBitmap(MobSDK.getContext(), "http://www.htc-iot.cn/wp-content/uploads/2017/09/huixinhu.jpg");
            oks.setImagePath(path);//确保SDcard下面存在此张图片
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.htc-iot.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("合职校园");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.htc-iot.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
