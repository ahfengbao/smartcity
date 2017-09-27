package org.hfzy.smartcity.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 缓存工具类
 */

public  final class CacheUtils {
    private static final String TAG =CacheUtils.class.getSimpleName();
    public static void saveCache(Context context,String name,String content){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存信息
     * @param context
     * @param name
     * @return
     */
    public static String readCache(Context context,String name){
        String content =null;
        try {
            FileInputStream fileInputStream = context.openFileInput(name);
            ByteArrayOutputStream baos  = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fileInputStream.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }
            content = baos.toString();
            baos.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d(TAG,content);
        return content;
    }
}
