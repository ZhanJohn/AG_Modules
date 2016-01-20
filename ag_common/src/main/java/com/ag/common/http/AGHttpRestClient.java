package com.ag.common.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ag.common.other.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AGHttpRestClient {

    private static final String TAG="AGHttpRestClient";
    private static String BASE_URL = "";
    private static int TimeOut = 60 * 1000;
    private String errorMsg="网络有问题或者服务器数据异常";
    private static AGHttpRestClient httpRestClient = new AGHttpRestClient();

    public static void initialize(String baseUrl, int timeOut) {
        BASE_URL = baseUrl;
        TimeOut = timeOut;
    }

    public static AGHttpRestClient getInstance(){
        return httpRestClient;
    }

    public <T> void get(final Context context, String url,
                               RequestParams params, final Class<T> cc,
                               final IHttpResult<T> iHttpResult) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(TimeOut);

        //
        Log.d(TAG,getAbsoluteUrl(url));
        Log.d(TAG,params.toString());

        client.get(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG,"AsyncHttpClient-onFailure-JSONObject==");
                throwable.printStackTrace();
                iHttpResult.onHttpSuccess(null);
                toastErrorMsg(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG,"AsyncHttpClient-onFailure-String==");
                throwable.printStackTrace();
                iHttpResult.onHttpSuccess(null);
                toastErrorMsg(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("AsyncHttpClient-onFailure-JSONArray==");
                throwable.printStackTrace();
                iHttpResult.onHttpSuccess(null);
                toastErrorMsg(context);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG,"AsyncHttpClient-onSuccess");
                Log.d(TAG,"jsonobject=" + response.toString());
                Gson gson = new Gson();
                T obj = null;
                try {
                    obj = gson.fromJson(response.toString(), cc);
                } catch (Exception e) {
                    e.printStackTrace();
                    obj = null;
                }
                iHttpResult.onHttpSuccess(obj);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(TAG,"AsyncHttpClient-onRetry");
                super.onRetry(retryNo);
            }

            @Override
            public void onStart() {
                Log.d(TAG,"AsyncHttpClient-onStart");
                super.onStart();
            }
        });
    }

    public <T> void post(final Context context, String url,
                                RequestParams params, final Class<T> cc,
                                final IHttpResult<T> iHttpResult) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(TimeOut);
        client.post(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG,"AsyncHttpClient-onFailure==");
                throwable.printStackTrace();
                iHttpResult.onHttpSuccess(null);
                toastErrorMsg(context);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG,"AsyncHttpClient-onSuccess");
                Log.d(TAG,"jsonobject=" + response.toString());
                Gson gson = new Gson();
                T obj = null;
                try {
                    obj = gson.fromJson(response.toString(), cc);
                } catch (Exception e) {
                    e.printStackTrace();
                    obj = null;
                }
                iHttpResult.onHttpSuccess(obj);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(TAG,"AsyncHttpClient-onRetry");
                super.onRetry(retryNo);
            }

            @Override
            public void onStart() {
                Log.d(TAG,"AsyncHttpClient-onStart");
                super.onStart();
            }
        });
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg=errorMsg;
    }

    private void toastErrorMsg(Context context){
        if(!TextUtils.isEmpty(errorMsg))
            Toast.ToastMessage(context, errorMsg);
    }

    public interface IHttpResult<T> {
        void onHttpSuccess(T t);
    }

}
