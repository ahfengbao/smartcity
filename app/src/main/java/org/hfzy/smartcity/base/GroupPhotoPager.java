package org.hfzy.smartcity.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.domain.GroupPhotoInfo;
import org.hfzy.smartcity.global.GlobalConstants;

/**
 * 组图页面
 */

public class GroupPhotoPager {
    private Context context;
    //组图布局
    public View view;

    @ViewInject(R.id.lv)
    private ListView lv;
    private HttpUtils httpUtils;
    private Gson gson;
    private BitmapUtils bitmapUtils;
    private GroupPhotoInfo groupPhotoInfo;
    @ViewInject(R.id.gv)
    private GridView gv;

    public GroupPhotoPager(Context context) {
        this.context =context;
        init();
    }

    private void init() {
        httpUtils = new HttpUtils();
        gson = new Gson();
        bitmapUtils = new BitmapUtils(context);
        initView();
    }

    private void initView() {
        view = View.inflate(context, R.layout.group_photo_pager,null);
        ViewUtils.inject(this,view);
        loadData();
    }

    private void loadData() {
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.NEWSCENTER_GROUP_PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });
    }

    private void processData(String result) {
        groupPhotoInfo = gson.fromJson(result, GroupPhotoInfo.class);
        GroupPhotoListAdapter GroupPhotoListAdapter = new GroupPhotoListAdapter();
        lv.setAdapter(GroupPhotoListAdapter);
    }

    enum STATE{
        LIST,GRID
    }
    private STATE state =STATE.LIST;
    /**
     * 切换状态
     */
    public void switchState(){
        if (state==STATE.LIST){
            state =STATE.GRID;
            lv.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
            gv.setAdapter(new GroupPhotoListAdapter());
        }else {
            state=STATE.LIST;
            lv.setVisibility(View.VISIBLE);
            gv.setVisibility(View.GONE);
            lv.setAdapter(new GroupPhotoGridAdapter());
        }
    }

    /**
     * 组图网格状态
     */
    private class GroupPhotoGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return groupPhotoInfo.data.news.size();
        }

        @Override
        public Object getItem(int position) {
            return groupPhotoInfo.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView ==null){
                convertView = View.inflate(context,R.layout.group_photo_list_item,null);
                holder = new ViewHolder();
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            GroupPhotoInfo.NewsInfo info = groupPhotoInfo.data.news.get(position);
            //http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg
            String url = info.listimage.replace("http://10.0.2.2:8080/zhbj", GlobalConstants.HOST);
            bitmapUtils.display(holder.ivPhoto,url);
            holder.tvTitle.setText(info.title);
            return convertView;
        }
    }

    /**
     * 组图列表适配器
     */
    private class GroupPhotoListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return groupPhotoInfo.data.news.size();
        }

        @Override
        public Object getItem(int position) {
            return groupPhotoInfo.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView ==null){
                convertView = View.inflate(context,R.layout.group_photo_list_item,null);
                holder = new ViewHolder();
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            GroupPhotoInfo.NewsInfo info = groupPhotoInfo.data.news.get(position);
            //http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg
            String url = info.listimage.replace("http://10.0.2.2:8080/zhbj", GlobalConstants.HOST);
            bitmapUtils.display(holder.ivPhoto,url);
            holder.tvTitle.setText(info.title);
            return convertView;
        }
    }
    static class ViewHolder{
        ImageView ivPhoto;
        TextView tvTitle;
    }
}
