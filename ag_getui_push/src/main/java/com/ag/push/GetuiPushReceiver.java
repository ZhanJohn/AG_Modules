package com.ag.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

public class GetuiPushReceiver extends BroadcastReceiver {
    private static String TAG="GetuiPushReceiver";
    public GetuiPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiPushReceiver", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                Log.d(TAG,"第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload == null) {
                    if(AGPush.getInstance().iPushReceiver!=null){
                        AGPush.getInstance().iPushReceiver.onReceive("");
                    }
                    break;
                }
                String data = new String(payload);
                Log.d(TAG, "Got Payload:" + data);
                if(AGPush.getInstance().iPushReceiver!=null){
                    AGPush.getInstance().iPushReceiver.onReceive(data);
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
//                String cc = PushManager.getInstance().getClientid(context);
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Log.d(TAG,"clientid=="+cid);
                AGPush.getInstance().setClientId(cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;
            default:
                break;
        }
    }
}
