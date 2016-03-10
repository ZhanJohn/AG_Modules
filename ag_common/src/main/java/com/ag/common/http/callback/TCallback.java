package com.ag.common.http.callback;


import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class TCallback<T> extends Callback<T>{

    private IHttpResponseT iHttpResponseT;
    private Class<T> cc;
    private TCallback(){}

    private TCallback(Class<T> cc,IHttpResponseT iHttpResponseT){
        this.cc=cc;
        this.iHttpResponseT=iHttpResponseT;
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
        T obj = null;
        try {
            obj = gson.fromJson(response.body().toString(), cc);
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

}
