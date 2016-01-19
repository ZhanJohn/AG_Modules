package com.ag.common.update;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.ag.common.res.AGResource;


public class UpdateDialog {

	Context context;
	android.app.AlertDialog ad;
	Builder dialogBuilder;

	
	TextView layout_tv_title;
	TextView layout_tv_toolip;
	WebView layout_webView;
	Button btnOK,btnCancel;
	
	public UpdateDialog(Context context){
		// TODO Auto-generated constructor stub
		this.context=context.getApplicationContext();
		dialogBuilder = new Builder(context);
		ad = dialogBuilder.create();
		ad.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(AGResource.getLayoutByName(context, "update_layout"));

		layout_tv_title=(TextView)window.findViewById(AGResource.getIdByName(context, "layout_tv_title"));
		layout_tv_toolip=(TextView)window.findViewById(AGResource.getIdByName(context, "layout_tv_toolip"));
		layout_webView=(WebView)window.findViewById(AGResource.getIdByName(context, "layout_webView"));
		btnOK=(Button)window.findViewById(AGResource.getIdByName(context, "btnOK"));
		btnCancel=(Button)window.findViewById(AGResource.getIdByName(context, "btnCancel"));
	}
	
	public void setUpdateContent(String content){
		layout_webView.loadData(content, "text/html; charset=UTF-8", null);
	}
	
	public void setPositiveButton(String text,final View.OnClickListener listener) {
		btnOK.setOnClickListener(listener);
	}
	
	public void setNegativeButton(String text,final View.OnClickListener listener){
		btnCancel.setOnClickListener(listener);
	}
	
	public void show(){
		ad.show();
	}
	
	public boolean isShowing(){
		return ad.isShowing();
	}
	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
	
	/**
	 * 点击空白处|返回键是否要关掉对话框
	 * @param value true关掉|false不关
	 */
	public void setCancelable(boolean value){
		ad.setCancelable(value);
	}
	
	/**
	 * 点击空白处是否要消失
	 */
	public void setCanceledOnTouchOutside(boolean value){
		ad.setCanceledOnTouchOutside(value);
	}
	
}
