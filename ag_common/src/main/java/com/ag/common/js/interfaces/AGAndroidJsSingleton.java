package com.ag.common.js.interfaces;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class AGAndroidJsSingleton {

    private static AGAndroidJsSingleton jsSingleton=new AGAndroidJsSingleton();
    private IAndroidJs iAndroidJs;
    private Map<String,String> tempJsValues = new HashMap<>();

    private AGAndroidJsSingleton(){}

    public static AGAndroidJsSingleton getInstance(){
        return jsSingleton;
    }

    public void initialize(IAndroidJs iAndroidJs){
        this.iAndroidJs=iAndroidJs;
    }

    public IAndroidJs getIAndroidJs(){
        return iAndroidJs;
    }

    public void addJsValue(String key,String value){
        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
            return;
        tempJsValues.put(key,value);
    }

    public String getJsValue(String key){
        return tempJsValues.get(key);
    }

    public void clearJsValue(){
        tempJsValues.clear();
    }

    public void removeJsValue(String key){
        tempJsValues.remove(key);
    }

}
