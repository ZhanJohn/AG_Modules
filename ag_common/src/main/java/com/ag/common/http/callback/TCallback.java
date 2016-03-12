package com.ag.common.http.callback;


import android.util.Log;

import com.ag.common.http.model.CommonJson;
import com.ag.common.http.model.CommonJsonList;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class TCallback<T> extends Callback<T>{

    private IHttpResponseT iHttpResponseT;
    private Class<T> cc;
    private ModelEnum modelEnum=ModelEnum.Model;
    private TCallback(){}

    public TCallback(Class<T> cc,IHttpResponseT iHttpResponseT){
        this.cc=cc;
        this.iHttpResponseT=iHttpResponseT;
    }

    public TCallback(ModelEnum modelEnum,Class<T> cc,IHttpResponseT iHttpResponseT){
        this.cc=cc;
        this.iHttpResponseT=iHttpResponseT;
        this.modelEnum=modelEnum;
    }

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
        if(iHttpResponseT!=null){
            iHttpResponseT.onBefore();
        }
    }

    @Override
    public void onAfter() {
        super.onAfter();
        if(iHttpResponseT!=null){
            iHttpResponseT.onAfter();
        }
    }

    @Override
    public void inProgress(float progress) {
        super.inProgress(progress);
        if(iHttpResponseT!=null){
            iHttpResponseT.onProgress(progress);
        }
    }

    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        Gson gson = new Gson();
        String content=response.body().string();
        T obj = null;
        Type objectType =null;
        try {
            if(modelEnum == ModelEnum.Model){
                objectType = type(CommonJson.class, cc);
            }else{
                objectType = type(CommonJsonList.class, cc);
            }
            obj=gson.fromJson(content, objectType);
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }

    @Override
    public void onError(Call call, Exception e) {
        Log.d("TCallback",e.getMessage());
        if(iHttpResponseT!=null){
            iHttpResponseT.onError(e.getMessage());
        }
    }

    @Override
    public void onResponse(T response) {
        if(iHttpResponseT!=null){
            if(response!=null){
                iHttpResponseT.onSuccess(response);
            }else{
                iHttpResponseT.onError("response is null!");
            }
        }
    }

    public interface IHttpResponseT<T>{
        void onError(String errorMsg);
        void onSuccess(T t);
        void onBefore();
        void onAfter();
        void onProgress(float progress);
    }

    public enum ModelEnum{
        Model,List
    }

    private ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

}
