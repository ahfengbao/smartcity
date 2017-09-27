package org.hfzy.smartcity.utils;

/**
 * Created by fengbao on 2017/6/22.
 */

public class StringUtils {
    //对网络数据的处理
    public static String getPath(String path){
        String[] split = path.split("/");
        return  "/"+split[split.length-2]+"/"+split[split.length-1];
    }

    //对网络数据的处理
    public static String getPhotoPath(String path){
        String[] split = path.split("/");
        return  "/"+split[split.length-2]+"/"+split[split.length-1];
    }
}
