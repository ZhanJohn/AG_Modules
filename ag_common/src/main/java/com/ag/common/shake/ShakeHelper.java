package com.ag.common.shake;

import android.content.Context;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class ShakeHelper {

    private Context context;
    private ShakeListener shakeListener;
    private IShakeListener iShakeListener;

    private static ShakeHelper mHelper;
    private boolean isShake=true;
    private Lock lock = new ReentrantLock();

    private ShakeHelper(Context context){
        this.context=context;
        shakeListener=new ShakeListener(context);
        shakeListener.setOnShakeListener(onShakeListener);
    }

    public static final ShakeHelper getInstance(Context context){
        if(mHelper==null){
            mHelper=new ShakeHelper(context);
        }
        return mHelper;
    }

    ShakeListener.OnShakeListener onShakeListener=new ShakeListener.OnShakeListener() {
        @Override
        public void onShake() {
            //加锁
            lock.lock();
            shakeListener.stop();
            if(!isShake)
                return;
            isShake=false;
            //触发摇一摇事件，回调js
            if(iShakeListener!=null)
                iShakeListener.onShakeSuccess();
//            webView.loadUrl(String.format("javascript:%s()", callbackJs));
            //解锁
            lock.unlock();
        }
    };

    public void startShake(IShakeListener iShakeListener){
        isShake=true;
        this.iShakeListener=iShakeListener;
        if(shakeListener==null){
            shakeListener=new ShakeListener(context);
        }
        shakeListener.start();
    }

    public static void stopShake(){
        if(mHelper!=null){
            mHelper.shakeListener.stop();
            mHelper=null;
        }
    }

}
