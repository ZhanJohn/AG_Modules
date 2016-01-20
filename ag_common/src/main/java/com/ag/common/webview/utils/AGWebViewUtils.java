package com.ag.common.webview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ag.common.R;
import com.ag.common.createview.AGViewBarCreate;
import com.ag.common.createview.CustomerBarResult;
import com.ag.common.js.enums.JSMethodEnum;
import com.ag.common.other.AGActivity;
import com.ag.common.webview.BaseWebViewActivity;
import com.google.gson.Gson;

import java.io.Serializable;


public class AGWebViewUtils {

    public static void startWebViewActivity(Context context,Class toActivity, String url, String title, boolean isNewTask, boolean addReqeustHeader){
        CustomerBarResult result= AGViewBarCreate.getCustomerBarResult(context,url,"icon_fanhui", JSMethodEnum.Back.getMethodValue(),null,title,null,null,null);
        startWebViewActivity(context,toActivity,url,title,result,isNewTask,addReqeustHeader);
    }

    public static void startWebViewActivity(Context context,Class toActivity,String url,String title,CustomerBarResult result,boolean isNewTask,boolean addReqeustHeader){
        if(toActivity==null)
            return;

        Intent intent = new Intent(context, toActivity);
        intent.putExtra("obj", result);
        intent.putExtra(BaseWebViewActivity.Add_Request_Header_Key, addReqeustHeader);
        //
        if (isNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //
        if (context instanceof Activity) {
            ((Activity) context).startActivity(intent);
            AGActivity.LeftIn((Activity) context);
        } else {
            context.startActivity(intent);
        }
    }

    public static void startWebViewActivity(Context context,Class toActivity,CustomerBarResult result){
        if(toActivity==null)
            return;

        Intent intent=new Intent(context, toActivity);
        intent.putExtra("obj", (Serializable) result);
        if(context instanceof Activity){
            ((Activity)context).startActivity(intent);
            AGActivity.LeftIn((Activity)context);
        }else{
            context.startActivity(intent);
        }
    }

    public static void startSearchActivity(Context context,Class toActivity,String url){
        if(toActivity==null)
            return;

        Intent intent=new Intent(context,toActivity);
        Gson gson=new Gson();
        CustomerBarResult result=gson.fromJson(context.getString(R.string.app_topbar_search)
                .replace("{0}",url),CustomerBarResult.class);
        intent.putExtra("obj", (Serializable) result);
        //
        if(context instanceof Activity){
            ((Activity)context).startActivity(intent);
            AGActivity.LeftIn((Activity)context);
        }else{
            context.startActivity(intent);
        }
    }

}
