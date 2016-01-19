package com.ag.controls.common.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.ag.controls.R;

/**
 * 简易对话框
 */
public class AGProgressDialog extends ProgressDialog {

    private Context mContext;
    private boolean mIsCloseTask;
    private String mMsg;
    private AsyncTask mAsyncTask;

    /**
     * 设置返回键效果
     *
     * @param asyncTask
     * @param isCloseTask false:取消异步类，不关闭界面 | true:取消异步类，且关闭界面
     */
    public void setBackClick(AsyncTask asyncTask, boolean isCloseTask) {
        mAsyncTask = asyncTask;
        mIsCloseTask = isCloseTask;
    }

    /**
     * 初始化
     *
     * @param context
     * @param msg     空:显示默认（加载中...）
     */
    public AGProgressDialog(Context context, String msg) {
        this(context, null, false, msg);
    }

    public AGProgressDialog(Context context, AsyncTask asyncTask, boolean isCloseTask) {
        this(context, asyncTask, isCloseTask, null);
    }

    public AGProgressDialog(Context context, AsyncTask asyncTask, boolean isCloseTask, String msg) {
        super(context);
        mContext = context;
        mAsyncTask = asyncTask;
        mIsCloseTask = isCloseTask;
        mMsg = msg;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
                mAsyncTask = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount = 0.2f;
        getWindow().setAttributes(params);

        TextView tv_message = (TextView) findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(mMsg)) {
            tv_message.setText(mMsg);
        }

        setIndeterminate(false);
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
    }
}