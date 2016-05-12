package com.ag.common.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.ag.common.R;
import com.ag.common.createview.AGViewBarCreate;
import com.ag.common.createview.CustomerBarAction;
import com.ag.common.createview.CustomerBarConfig;
import com.ag.common.createview.CustomerBarController;
import com.ag.common.createview.CustomerBarResult;
import com.ag.common.createview.ISearchListener;
import com.ag.common.dialog.AGDialogUtil;
import com.ag.common.js.AGChromeClient;
import com.ag.common.js.bean.PopLevel;
import com.ag.common.js.enums.JSMethodEnum;
import com.ag.common.other.StringUtils;
import com.ag.common.other.Toast;
import com.ag.common.res.AGResource;
import com.ag.common.shake.ShakeHelper;
import com.ag.common.update.IAlertDialogResult;
import com.ag.common.webview.bean.WebLoginResult;
import com.ag.common.webview.bean.WebUser;
import com.ag.common.webview.core.AGWebViewControl;
import com.ag.common.webview.upload.WebViewImageUploadHelper;
import com.ag.common.webview.utils.AGCookies;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseWebViewActivity extends Activity {

    protected WebUser currentUser;
    private WebView webView;
    private AGWebViewControl viewControl;
    protected CustomerBarResult customerBarResult;

    /**
     * 是否刷新当前View
     */
    protected boolean mRefreshView=false;
    protected int payState=0;//支付状态，0为不是支付，1为支付界面，2为支付成功
    protected boolean isBackRefresh=false;
    private boolean mActivityResult=false;
    public static final Integer LOGIN_SUCCESS=100;
    public static final Integer LOGIN_FAIL=110;
    public static final String Add_Request_Header_Key="add_request_header";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        initApplication();
        currentUser= AGCookies.getUserCookie();
    }

    protected void setCustomerBarResult(CustomerBarResult barResult){
        if(barResult==null)
            return;
        this.customerBarResult=barResult;
        //是否为支付界面
        String payType=customerBarResult.getSpecialPageType();
        setPayPage(TextUtils.isEmpty(payType)?0:payType.equals("payment")?1:payType.equals("payed")?2:0);
        setBackRefresh(StringUtils.SafeInt(customerBarResult.getIsNeed2FreshParentUI(),0)==1?true:false);

        //
        if(customerBarResult.getWndCfg()==null){
            CustomerBarConfig cfg=new CustomerBarConfig();
            List<CustomerBarController> controllers=new ArrayList<>();
            controllers.add(AGViewBarCreate.getCustomerBarController(this));
            cfg.setController(controllers);
            customerBarResult.setWndCfg(cfg);
            return;
        }

        //如果没有Button，默认加BackButton
        List<CustomerBarController> controllers=customerBarResult.getWndCfg().getController();
        boolean flag=false;
        //
        if(controllers!=null && controllers.size()>0) {
            for (CustomerBarController cc : controllers) {
                if (cc.getType().equals("button")) {
                    flag = true;
                    break;
                }
            }
        }
        //
        if(!flag){
            if(controllers==null) controllers=new ArrayList<>();
            controllers.add(AGViewBarCreate.getCustomerBarController(this));
            customerBarResult.getWndCfg().setController(controllers);
        }
    }

    protected boolean getPullRefreshEnable(){
        if(customerBarResult==null)
            return true;
        //是否开启刷新
        int refresh=StringUtils.SafeInt(customerBarResult.getIsCanPullToRefresh(),1);
        return refresh==1?true:false;
    }

    protected void setWebViewParams(WebView webView,AGWebViewControl webViewControl){
        this.webView=webView;
        this.viewControl=webViewControl;
        if(viewControl!=null){
            viewControl.setWebViewAndLoading();
        }
    }

    public void refreshWebView(){
        if(viewControl!=null)
            viewControl.refreshWebView(true);
    }

    public void refreshWebView(boolean showProcessBar){
        if(viewControl!=null)
            viewControl.refreshWebView(showProcessBar);
    }

    protected View.OnClickListener baseClickListener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId() == AGResource.getIdByName(BaseWebViewActivity.this,"layout_btn_back")){
                finishActivity();
            }else{
                if(v.getTag()==null)
                    return;
                CustomerBarAction action=null;
                try {
                    action = (CustomerBarAction) v.getTag();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(action==null)
                    return;
                String func=StringUtils.SafeString(action.getFunName());
                if(func.equalsIgnoreCase(JSMethodEnum.Back.getMethodValue())){
                    back();
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return back();
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(webView==null || viewControl==null)
            return;

        //检测登录状态是否有改变，有就刷新
        WebUser mUser=AGCookies.getUserCookie();
        if ((currentUser == null && mUser != null) || (currentUser != null && mUser == null)
                || (currentUser != null && mUser != null && !currentUser.getToken().equals(mUser.getToken()))) {
            currentUser=mUser;
            mRefreshView=true;
        }
        //
        webView.onResume();
        //检测登录状态是否有改变，有就刷新
        if(mRefreshView && !mActivityResult){
            viewControl.refreshWebView(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(webView==null || viewControl==null)
            return;
        webView.onPause();
        mRefreshView=false;
        mActivityResult=false;
    }

    public void setPayPage(int payState){
        this.payState=payState;
    }

    public void setBackRefresh(boolean isBackRefresh){ this.isBackRefresh=isBackRefresh; }

    public void showPayPage(){
        //弹出支付窗口
        AGDialogUtil.showDialog(this, "是否确定放弃付款", "确定", "取消", new IAlertDialogResult() {
            @Override
            public void resultCancel() {

            }

            @Override
            public void resultOK() {
                turnOrderPage();
            }
        });
    }

    protected boolean back(){
        if(payState==0){
            if(!isBackRefresh)
                finishActivity();
            else{
                popActivity(new PopLevel(1,1));
            }
            return false;
        }else if(payState==1){
            //支付界面
            showPayPage();
        }else if(payState==2){
            //支付成功或失败
            turnOrderPage();
        }

        return true;
    }

    /**
     * Init Back's Button
     */
    protected void initBackView(){
        View backView=findViewById(AGResource.getIdByName(this,"layout_btn_back"));
        if(backView!=null){
            backView.setOnClickListener(baseClickListener);
        }
    }

    protected ISearchListener searchListener=new ISearchListener() {
        @Override
        public void onSearchContent(String content) {
            if(TextUtils.isEmpty(content.trim())){
                Toast.ToastMessage(getApplicationContext(),getString(R.string.str_search_toolip));
                return;
            }
            //SearchByKeyword(strKeyword)
            webView.loadUrl("javascript:SearchByKeyword('" + content.replace("'", "") + "')");
//			Toast.ToastMessage(ZQWebviewActivity.this,content);
        }
    };

    @Override
    public void onDestroy() {
        if (webView != null) {
            //
            WebViewImageUploadHelper.clearObject();
            ShakeHelper.stopShake();
            //
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == LOGIN_SUCCESS){
            mActivityResult=true;
            //登录成功，js回调
            String method= StringUtils.SafeString(webView.getTag());
            WebLoginResult loginResult=(WebLoginResult)data.getSerializableExtra("obj");
            if(!TextUtils.isEmpty(method) && loginResult!=null){
                String loadJs=String.format("javascript:%s('%s')", method, (new Gson()).toJson(loginResult));
                System.out.println("loadJs="+loadJs);
                webView.loadUrl(loadJs);
            }
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }else if(resultCode == LOGIN_FAIL){
//			application.finishActivity();
            //登录失败，js回调
            String method=StringUtils.SafeString(webView.getTag());
            if(!TextUtils.isEmpty(method)){
                webView.loadUrl(String.format("javascript:%s('%s')", method,"{\"ret\":\"-1\"}"));
            }
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        //

        //>=4.4.3的回调
        if(requestCode == AGChromeClient.INPUT_FILE_NEW_REQUEST_CODE){
            if(viewControl.chromeClient.mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if(resultCode == Activity.RESULT_OK) {
                if(data == null) {
                    // If there is not data, then we may have taken a photo
                    if(viewControl.chromeClient.mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(viewControl.chromeClient.mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            viewControl.chromeClient.mFilePathCallback.onReceiveValue(results);
            viewControl.chromeClient.mFilePathCallback = null;
            return;
        }
        //<4.4
        if(requestCode==AGChromeClient.INPUT_FILE_REQUEST_CODE)
        {
            if (null == viewControl.chromeClient.mUploadMessage){
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null
                    : data.getData();
            viewControl.chromeClient.mUploadMessage.onReceiveValue(result);
            viewControl.chromeClient.mUploadMessage = null;
            return;
        }


        if(resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        //>=4.4 && <5.0
        if ( requestCode == WebViewImageUploadHelper.KITKAT_FILECHOOSER ) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

            WebViewImageUploadHelper.getInstance(this).updateContent(result);
        } else if ( requestCode == WebViewImageUploadHelper.KITKAT_CAMERA) {
            WebViewImageUploadHelper.getInstance(this).updateContent();
        }
        //
    }

    public void setNetworkView(){
        if(viewControl==null)
            return;
        viewControl.setInternetErrorView();
    }

    public abstract void initApplication();

    public abstract void finishActivity();

    public abstract void turnOrderPage();

    public abstract void popActivity(PopLevel popLevel);


}
