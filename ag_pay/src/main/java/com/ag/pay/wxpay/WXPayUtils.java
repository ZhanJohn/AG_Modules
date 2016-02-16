package com.ag.pay.wxpay;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付
 * Created by ZXR on 2016/1/13.
 */
public class WXPayUtils {

    private static String TAG="WXPayUtils";
    private volatile static WXPayUtils instance;
    private Context mContext;
    private IWXAPI mIWXAPI;
    private IWXPayResult iwxPayResult;

    private WXPayUtils(){}

    public static WXPayUtils getInstance(){
        if(instance==null){
            synchronized (WXPayUtils.class){
                if(instance==null){
                    instance=new WXPayUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 微信支付初始化
     * @param context
     * @param intent 处理Handler回调，传getIntent()即可
     * @param wxAppId 微信开放平台注册的AppID
     * @param iwxPayResult 微信支付结果接口
     */
    public void initialize(Context context, Intent intent, String wxAppId,IWXPayResult iwxPayResult){
        this.mContext=context;
        this.iwxPayResult=iwxPayResult;
        mIWXAPI= WXAPIFactory.createWXAPI(context,wxAppId);
        mIWXAPI.registerApp(wxAppId);
        mIWXAPI.handleIntent(intent,iwxapiEventHandler);
    }

    private IWXAPIEventHandler iwxapiEventHandler=new IWXAPIEventHandler() {

        /**
         * 微信发送请求到第三方应用时，会回调到该方法
         * @param baseReq
         */
        @Override
        public void onReq(BaseReq baseReq) {
            switch (baseReq.getType()) {
                case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                    goToGetMsg();
                    break;
                case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                    goToShowMsg((ShowMessageFromWX.Req) req);
                    break;
                case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
//                    Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        /**
         * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
         * @param baseResp
         */
        @Override
        public void onResp(BaseResp baseResp) {
            if(iwxPayResult==null)
                return;

            if(baseResp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX)
                return;

            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    iwxPayResult.paySuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    iwxPayResult.payCancel();
                    break;
                default:
                    String errorMsg=String.format("errCode:%d;errStr::%s",baseResp.errCode,baseResp.errStr);
                    Log.d(TAG,errorMsg);
                    iwxPayResult.payError(errorMsg);
                    break;
            }
        }
    };

    /**
     * 解绑微信
     */
    public void destroy(){
        if(mIWXAPI == null)
            return;
        mIWXAPI.unregisterApp();
    }

    /**
     * 调用微信支付，在调用pay()之前必须保证在App启动时有调用initialize()进行初始化工作
     * @param payParams
     */
    public void pay(WXPayParams payParams){
        if(mContext==null || mIWXAPI==null)
            return;

        try {
            PayReq payReq = new PayReq();
            payReq.appId = payParams.getAppId();
            payReq.nonceStr = payParams.getNonceStr();
            payReq.packageValue = payParams.getPackageValue();
            payReq.prepayId = payParams.getPrepayId();
            payReq.partnerId = payParams.getPartnerId();
            payReq.sign = payParams.getSign();
            payReq.timeStamp = payParams.getTimeStamp();
            mIWXAPI.sendReq(payReq);
        }catch (Exception e){
            e.printStackTrace();
            iwxPayResult.payError(e.getMessage());
        }
    }

    /**
     * 微信支付结果监听
     */
    public interface IWXPayResult{
        /**
         * 支付成功
         * 展示成功页面
         */
        void paySuccess();

        /**
         * 取消支付
         * 无需处理。发生场景：用户不支付了，点击取消，返回APP。
         */
        void payCancel();

        /**
         * 支付出错
         * 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
         */
        void payError(String errorMsg);
    }

}
