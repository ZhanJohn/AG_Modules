package com.ag.push;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.params.HttpClientParams;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by ZXR on 2015/12/16.
 */
public class AGPush {

    private final String TAG = this.getClass().getSimpleName();
    private static AGPush zqPush;
    public IPushReceiver iPushReceiver;
    private AGPushParam pushParam;
    private String pushUrl = "";
    private int timeOut = 60 * 1000;
    private String mTicks = "";
    private String clientId="";

    private AGPush() {
    }

    public static AGPush getInstance() {
        if (zqPush == null)
            zqPush = new AGPush();
        return zqPush;
    }

    public void setiPushReceiver(IPushReceiver iPushReceiver) {
        this.iPushReceiver = iPushReceiver;
    }

    public void setTimeOut(int timeOut) {
        if (timeOut <= 60 * 1000)
            return;
        this.timeOut = timeOut;
    }

    /**
     * initialize Getui push
     *
     * @param context
     */
    public void initialize(Context context,String pushUrl) {
        this.pushUrl=pushUrl;
        PushManager.getInstance().initialize(context);
    }

    /**
     * initialize Getui push and receiver's listener
     *
     * @param context
     * @param iPushReceiver
     */
    public void initialize(Context context, IPushReceiver iPushReceiver) {
        PushManager.getInstance().initialize(context);
        this.iPushReceiver = iPushReceiver;
    }

    public void setClientId(String cid){
        if(TextUtils.isEmpty(cid))
            return;
        this.clientId=cid;
    }

    /**
     * Getui bind
     * User login or Auto Login must bindApp
     *
     * @param pushParam
     */
    public void bindApp(Context context, AGPushParam pushParam) {
        if (pushParam == null) {
            Log.d(TAG,"pushParam is null");
            return;
        }
        this.pushParam = pushParam;
        if(TextUtils.isEmpty(clientId)){
            clientId = PushManager.getInstance().getClientid(context);
        }
        doHttpPost(clientId, 1);
    }

    /**
     * Getui unBind
     * User logout must unBindApp
     */
    public void unBindApp() {
        if (pushParam == null)
            return;

        doHttpPost(" ", 2);
    }

    private synchronized void doHttpPost(final String cid, final int type) {

        new AsyncTask<Void,Integer,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                return getHttpPost(cid);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (type == 1) {
                    iPushReceiver.onBindResult(result);
                } else {
                    iPushReceiver.onUnBindResult(result);
                }
            }
        }.execute();
    }

    /**
     * Get params's sign
     *
     * @param cid
     * @return
     */
    private String getSignString(String cid) {
        if (pushParam == null)
            return null;
        StringBuffer sb = new StringBuffer();
        sb.append("AppId").append("2");
        sb.append("ClientId").append(cid);
        sb.append("KeyMD5").append(pushParam.getMd5Key());
        sb.append("LoginName").append(pushParam.getUserName());
        sb.append("Nick").append(pushParam.getUserName());
        sb.append("SysId").append(pushParam.getSysId());
        sb.append("Ticks").append(mTicks);
        
        Log.d(TAG, "signString==" + sb.toString());
        String md5Sign = getMD5String(sb.toString());
        Log.d(TAG, "md5Sign==" + md5Sign);
        return md5Sign;
    }

    private final static String getMD5String(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpParams httpParams;
    private HttpClient httpClient;
    public HttpClient getHttpClient() {
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）

        this.httpParams = new BasicHttpParams();

        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小

        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);

        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);

        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

        // 设置重定向，缺省为 true
        HttpClientParams.setRedirecting(httpParams, true);

        // 创建一个 HttpClient 实例

        // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

        // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

        httpClient = new DefaultHttpClient(httpParams);

        return httpClient;
    }

    private boolean getHttpPost(String cid){
        mTicks = String.valueOf(System.currentTimeMillis());
         /* 建立HTTPPost对象 */
        HttpPost httpRequest = new HttpPost(pushUrl);
        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("AppId", "2"));
        params.add(new BasicNameValuePair("ClientId", cid));
        params.add(new BasicNameValuePair("LoginName", pushParam.getUserName()));
        params.add(new BasicNameValuePair("Nick", pushParam.getUserName()));
        params.add(new BasicNameValuePair("SysId", String.valueOf(pushParam.getSysId())));
        params.add(new BasicNameValuePair("Ticks", mTicks));
        params.add(new BasicNameValuePair("Sign", getSignString(cid)));

        httpClient=getHttpClient();

        String strResult = "doPostError";

        try {
            /* 添加请求参数到请求对象 */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                String responseString = EntityUtils.toString(httpResponse.getEntity());

                Log.d(TAG, "onSuccess:"+responseString);
                if (iPushReceiver != null) {
                    int code = -1;
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        code = jsonObject.getInt("Code");
                        String message = jsonObject.getString("Message");
                        String result = jsonObject.getString("Result");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return code == 0 ? true : false;
                }
            } else {
                strResult = "Error Response: "
                        + httpResponse.getStatusLine().toString();
            }
        } catch (ClientProtocolException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (IOException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (Exception e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }
        Log.d(TAG, "onFail:"+strResult);
        return false;
    }



}
