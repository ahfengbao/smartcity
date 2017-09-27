package org.hfzy.smartcity.domain;

import java.util.List;

/**
 * Created by fengbao on 2017/6/22.
 */

public class NewsCenterNewsInfo {
    public NewsCenterNewsTabInfo data;
    public String retcode;

    public class NewsCenterNewsTabInfo {
        public String countcommenturl;
        public String more;
        public String title;
        public List<ListNewsInfo> news;
        public List<TopicInfo> topic;
        public List<TopicNewsInfo> topnews;
    }

    public class ListNewsInfo {
        public String comment;
        public String commentlist;
        public String countcommenturl;
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    public class TopicInfo{
        public String description;
        public String id;
        public String listimage;
        public String sort;
        public String title;
        public String url;
    }

    public class TopicNewsInfo{
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;
    }

}
