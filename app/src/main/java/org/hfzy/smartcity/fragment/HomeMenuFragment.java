package org.hfzy.smartcity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.hfzy.smartcity.R;
import org.hfzy.smartcity.activity.HomeActivity;
import org.hfzy.smartcity.domain.NewsCenterInfo;

import java.util.List;

/**
 * Created by fengbao on 2017/6/18.
 * 侧滑菜单页面
 */

public class HomeMenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_menu,null);
    }

    private List<NewsCenterInfo.NewsCenterMenuInfo> list;           //菜单数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = (ListView) getActivity().findViewById(R.id.lv);
        adapter = new MyAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    public void setList(List<NewsCenterInfo.NewsCenterMenuInfo> list) {
        this.list = list;
        //Log.i(GlobalConstants.TAG,list.size()+"");
        adapter.notifyDataSetChanged();     //刷新数据
    }

    public   int selectedPositon = 0;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(selectedPositon == position){
            //原来选中的条目就是现在点击的条目
            ((HomeActivity)getActivity()).getSlidingMenu().toggle();
            return;
        }
        selectedPositon = position;
        adapter.notifyDataSetChanged();

        //修改主界面标题的显示
        switchTitle(position);
    }

    private void switchTitle(int position) {
        ((HomeActivity)getActivity()).homeFragment.operatorTitle(position);
    }

    private  class  MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (list==null){
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder =null;
            View view = null;
            if (convertView==null){
                view = View.inflate(getActivity(),R.layout.fragment_home_menu_item,null);
                viewHolder = new ViewHolder();
                viewHolder.iv = (ImageView) view.findViewById(R.id.iv);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            }else{
                view =convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_title.setText(list.get(position).title);

            if(position == selectedPositon){
                viewHolder.iv.setEnabled(false);
                viewHolder.tv_title.setEnabled(false);
            }else{
                viewHolder.iv.setEnabled(true);
                viewHolder.tv_title.setEnabled(true);
            }
            return view;
        }
    }
    static  class ViewHolder{
        ImageView iv;
        TextView tv_title;
    }
}
