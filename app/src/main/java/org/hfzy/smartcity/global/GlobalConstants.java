package org.hfzy.smartcity.global;

public interface  GlobalConstants {
	static final String TAG = "TAG";
	static final String PREF_GUDIE = "gudie";	//是否进入过向导页面
	static final String PREF_HAS_READ_NEWS = "has_read_news";	//已经读取过的新闻
	static final String HOST = "http://123.206.176.226:8080/zhbj";	//服务器主机地址
	static final String NEWSCENTER_URL =HOST+ "/categories.json";	//新闻中心的地址

	//新闻中心组图地址
	public final static String NEWSCENTER_GROUP_PHOTO_URL = HOST + "/photos/photos_1.json";
}
