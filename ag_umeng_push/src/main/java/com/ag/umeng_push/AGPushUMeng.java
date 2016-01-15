package com.ag.umeng_push;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by ZXR on 2016/1/15.
 */
public class AGPushUMeng {

    private static AGPushUMeng agPushUMeng=new AGPushUMeng();
    private AGPushUMeng(){}

    public static AGPushUMeng getInstance(){
        return agPushUMeng;
    }

    private Context mContext;
    private PushAgent mPushAgent;
    private IUMengPushReceiver iuMengPushReceiver;

    public void initialize(Context context,IUMengPushReceiver iuMengPushReceiver){
        this.mContext=context;
        this.iuMengPushReceiver=iuMengPushReceiver;
        mPushAgent=PushAgent.getInstance(context);
        mPushAgent.setDebugMode(true);
        //开启友盟推送
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String registrationId) {
                System.out.println("onRegistered=="+registrationId);
                //String device_token = UmengRegistrar.getRegistrationId(mContext);
            }
        });

        //设置消息显示和处理
        mPushAgent.setMessageHandler(umengMessageHandler);
        mPushAgent.setNotificationClickHandler(umengNotificationClickHandler);

    }

    public void disable(){
        if(mPushAgent==null)
            return;
        //关闭友盟推送
        mPushAgent.disable(new IUmengUnregisterCallback() {
            @Override
            public void onUnregistered(String registrationId) {

            }
        });
    }

    /**
     * 友盟推送消息处理
     */
    private UmengMessageHandler umengMessageHandler=new UmengMessageHandler(){

        /**
         * 参考集成文档的1.6.3
         * http://dev.umeng.com/push/android/integration#1_6_3
         * */
        @Override
        public void dealWithCustomMessage(Context context,UMessage uMessage) {
            Toast.makeText(mContext, "dealWithCustomMessage=="+uMessage.custom, Toast.LENGTH_LONG).show();
        }

        /**
         * 参考集成文档的1.6.4
         * http://dev.umeng.com/push/android/integration#1_6_4
         * */
        @Override
        public Notification getNotification(Context context, UMessage uMessage) {
            switch (uMessage.builder_id) {
                case 1:
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, uMessage.title);
                    myNotificationView.setTextViewText(R.id.notification_text, uMessage.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, uMessage));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, uMessage));
                    builder.setContent(myNotificationView);
                    builder.setContentTitle(uMessage.title)
                            .setContentText(uMessage.text)
                            .setTicker(uMessage.ticker)
                            .setAutoCancel(true);
                    Notification mNotification = builder.build();
                    //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                    mNotification.contentView = myNotificationView;
                    return mNotification;
                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, uMessage);
            }
        }
    };

    /**
     * 友盟推送消息点击处理
     */
    private UmengNotificationClickHandler umengNotificationClickHandler=new UmengNotificationClickHandler(){
        @Override
        public void dealWithCustomAction(Context context, UMessage uMessage) {
            if(iuMengPushReceiver!=null){
                iuMengPushReceiver.onReceive(uMessage.custom);
            }
//            Toast.makeText(context, "dealWithCustomAction=="+uMessage.custom, Toast.LENGTH_LONG).show();
        }
    };

}
