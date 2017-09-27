package org.hfzy.smartcity.domain;

import java.util.List;

/**
 *组图实体类
 */
public class GroupPhotoInfo {
	
	public GroupPhotoDataInfo data;
	public String retcode;
	public class GroupPhotoDataInfo{
		public String countcommenturl;
		public String more;
		public List<NewsInfo> news;
		public String title;
		public List topic;
	}
	
	/*
	 * 图片信息
	 */
	public class NewsInfo{
		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String largeimage;
		public String listimage;//图片地址
		public String pubdate;
		public String smallimage;
		public String title;//标题
		public String type;
		public String url;
	}
	
}
