package com.ag.common.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ag.common.encode.BTS;
import com.google.gson.Gson;

/**
 *
 */
public class AGSharedPreferences {

    /**
     * 根据KEY清除数据
     * @param context
     * @param key
     */
    public static void clearInfo(Context context,String key){
        SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 设置配置文件的值
     * @param name 配置的KEY
     * @param value 配置的值
     * @param mode 保存模式
     * @param context
     */
    public static void setSharePReferencesValue(String name,String value,int mode,Context context){
        SharedPreferences preferences = context.getSharedPreferences(name, mode);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, BTS.encodeBTS(value));
        editor.commit();
        System.out.println("写入配置文件完成！");
    }

    /**
     * 通过查看配置文件是否存在，而判断是不是第一次登录
     * @return
     */
    public static boolean sharePreferencesExists(String name,int mode, Context context){
        SharedPreferences preferences = context.getSharedPreferences(name,mode);
        String s = preferences.getString(name, null);
        Log.d("AGSharedPreferences","key值为：" + s);
        return s==null?false:true;
    }

    /**
     * 获取配置文件中的String
     */
    public static String getValueBySharePreference(String key, int mode, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(key,mode);
        String s = preferences.getString(key,null);
        if(s==null || s=="")
            return null;
        return BTS.decodeBTS(s);
    }

    public static <T> T getObjectFromJson(Context context,String key,Class<T> cls){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String s = preferences.getString(key, null);

        if(s == null || s == ""){
            return null;
        }

        try {
            String result=BTS.decodeBTS(s);
            Log.d("AGSharedPreferences","GetObjectFromJson=="+result);
            return (new Gson()).fromJson(result, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isIndexHtmlEnter(Context context){
        if (context == null) {
            return false;
        }
        String mResultStr = context.getSharedPreferences(
                "IndexHtml", Context.MODE_PRIVATE).getString(
                "IndexHtml", "");
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        return true;
    }

    public static void setFirstIndexHtmlEnter(Context context){
        SharedPreferences settings = context.getSharedPreferences("IndexHtml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IndexHtml", "false");
        editor.commit();
    }

}
