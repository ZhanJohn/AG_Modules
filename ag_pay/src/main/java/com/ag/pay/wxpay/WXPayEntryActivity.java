package com.ag.pay.wxpay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 将此页面拷贝到项目包名的wxapi目录下，并把onCreate中的wexin appid替换为微信开放平台申请的appId
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, "wexin appid");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp baseResp) {

		if(baseResp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX) {
			finish();
			return;
		}

		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				WXPayUtils.getInstance().paySuccess();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				WXPayUtils.getInstance().payCancel();
				break;
			default:
				String errorMsg=String.format("errCode:%d;errStr::%s",baseResp.errCode,baseResp.errStr);
				WXPayUtils.getInstance().payError(errorMsg);
				break;
		}
		finish();
	}
}