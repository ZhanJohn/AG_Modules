package com.ag.pay.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝处理类
 *
 */
public class AlipayUtils {

	private Context mContext=null;
	private volatile static AlipayUtils instance;
	public static AlipayUtils getInstance(){
		if(instance==null){
			synchronized (AlipayUtils.class){
				if(instance==null){
					instance=new AlipayUtils();
				}
			}
		}
		return instance;
	}

	private IAliPayResult iAliPayResult;

	public void initialize(Context context,IAliPayResult iAliPayResult){
		this.iAliPayResult=iAliPayResult;
		this.mContext=context;
	}

	public static final Map<String, String> sResultStatus;
	static {
		sResultStatus = new HashMap<String, String>();
		sResultStatus.put("9000", "操作成功");
		sResultStatus.put("4000", "系统异常");
		sResultStatus.put("4001", "数据格式不正确");
		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		sResultStatus.put("4004", "该用户已解除绑定");
		sResultStatus.put("4005", "绑定失败或没有绑定");
		sResultStatus.put("4006", "订单支付失败");
		sResultStatus.put("4010", "重新绑定账户");
		sResultStatus.put("6000", "支付服务正在进行升级操作");
		sResultStatus.put("6001", "用户中途取消支付操作");
		sResultStatus.put("7001", "商户没有开通移动快捷支付服务,请使用其他支付工具");
	}

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult resultObj = new PayResult((String) msg.obj);
					String resultStatus = resultObj.getResultStatus();

					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
//						Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
						iAliPayResult.paySuccess();
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(mContext, "支付结果确认中",
									Toast.LENGTH_SHORT).show();

						} else {
							String resultContent="支付失败";
							if(sResultStatus.containsKey(resultStatus)){
								resultContent=sResultStatus.get(resultStatus);
							}
							Toast.makeText(mContext, resultContent,
									Toast.LENGTH_SHORT).show();
							iAliPayResult.payError(resultContent);
						}
					}
					break;
				}
				case SDK_CHECK_FLAG: {
					Toast.makeText(mContext, "检查结果为：" + msg.obj,
							Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay(AlipayInfo obj) {
		if(mContext==null || iAliPayResult==null)
			return;

		String orderInfo = getOrderInfo(obj);

		// 对订单做RSA 签名
		String sign = sign(orderInfo, obj.getPrivatekey());
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		String sign = "";
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign(orderInfo,obj.getPrivatekey()), "UTF-8");
//		} catch (Exception ex) {
//			Toast.makeText(mContext, "调用支付宝失败",Toast.LENGTH_SHORT).show();
//			return;
//		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity)mContext);
				// 调用支付接口
				String result = alipay.pay(payInfo,true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 *
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask((Activity)mContext);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 *
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask((Activity)mContext);
		String version = payTask.getVersion();
		Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 *
	 */
	public String getOrderInfo(AlipayInfo info) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + info.getPartner() + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + info.getAccount() + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + info.getPayCode() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + info.getShowTitle() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + info.getBody() + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + info.getMoney() + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + info.getNotifyUrl()
				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
//		 orderInfo += "&paymethod=\"expressGateway\"";

		Log.i(this.getClass().getSimpleName(), orderInfo);

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content,String rsaPrivateKey) {
		return SignUtils.sign(content, rsaPrivateKey);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 支付结果监听
	 */
	public interface IAliPayResult{
		/**
		 * 支付成功
		 */
		void paySuccess();

		/**
		 * 取消支付
		 */
		void payCancel();

		/**
		 * 支付出错
		 */
		void payError(String errorMsg);
	}

}