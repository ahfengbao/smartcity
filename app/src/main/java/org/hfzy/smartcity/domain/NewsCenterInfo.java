package org.hfzy.smartcity.domain;

import java.util.List;

/**
 * Created by fengbao on 2017/6/20.
 */

public class NewsCenterInfo {
    public List<NewsCenterMenuInfo> data;
    public List<String> extend;
    public  String retcode;

    @Override
    public String toString() {
        return "NewsCenterInfo{" +
                "data=" + data +
                ", extend=" + extend +
                ", retcode='" + retcode + '\'' +
                '}';
    }

    /**
     * 菜单对象
     */
    public class NewsCenterMenuInfo{
        public List<NewsCenterNewsInfo> children;
        public String id;
        public String title;
        public String type;

        @Override
        public String toString() {
            return "NewsCenterMenuInfo{" +
                    "children=" + children +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    /**
     * 新闻数据
     */
    public class NewsCenterNewsInfo{
        public String id;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsCenterNewsInfo{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
