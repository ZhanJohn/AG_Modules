package com.ag.common.update;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.ag.common.res.AGResource;
import com.ag.common.screen.ScreenUtils;
import com.ag.common.ui.LayoutEnum;
import com.ag.common.ui.UIHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MyAlertDialog {
	Context context;
	android.app.AlertDialog ad;
	Builder dialogBuilder;
	// TextView titleView;
	TextView title;
	TextView messageView;
	LinearLayout buttonLayout;
	LinearLayout layout_content;
	Button btnOK, btnCancel;
	private ImageView layout_img_title;
	private RelativeLayout layout_img;

	ProgressBar update_progress;

	public MyAlertDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialogBuilder = new Builder(context);
		ad = dialogBuilder.create();
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(AGResource
				.getLayoutByName(context, "alertdialog"));
		// titleView=(TextView)window.findViewById(AGResource.getIdByName(context,
		// "message").title);
		messageView = (TextView) window.findViewById(AGResource.getIdByName(
				context, "message"));
		title = (TextView) window.findViewById(AGResource.getIdByName(context,
				"title"));
		layout_content = (LinearLayout) window.findViewById(AGResource
				.getIdByName(context, "layout_content"));
		buttonLayout = (LinearLayout) window.findViewById(AGResource
				.getIdByName(context, "buttonLayout"));
		update_progress = (ProgressBar) window.findViewById(AGResource
				.getIdByName(context, "update_progress"));
		if (window.findViewById(AGResource.getIdByName(context,
				"layout_img_title")) != null)
			layout_img_title = (ImageView) window.findViewById(AGResource
					.getIdByName(context, "layout_img_title"));
		if (window.findViewById(AGResource.getIdByName(context, "layout_img")) != null) {
			layout_img = (RelativeLayout) window.findViewById(AGResource
					.getIdByName(context, "layout_img"));
		}
	}

	public void show() {
		ad.show();
	}

	public boolean isShowing() {
		return ad.isShowing();
	}

	public void setTitle(int resId) {
		this.title.setVisibility(View.VISIBLE);
		this.title.setText(resId);
	}

	public void setTitle(String title) {
		this.title.setVisibility(View.VISIBLE);
		this.title.setText(title);
	}

	public void setMsgImage(int imgRes) {
		if (layout_img != null) {
			layout_img.setVisibility(View.VISIBLE);
		}
		if (layout_img_title != null) {
			layout_img_title.setImageResource(imgRes);
		}
	}

	public void setMessage(Spanned spannable) {
		messageView.setText(spannable, BufferType.SPANNABLE);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
		messageView.setGravity(Gravity.CENTER);
	}

	public void setMessageLeft(String message) {
		messageView.setText(message);
		messageView.setGravity(Gravity.LEFT);
	}

	public ProgressBar getProgressBar() {
		return update_progress;
	}

	public void showProgressBar() {
		update_progress.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
	}

	// 不要乱动布局，修改的话记得我的addView中的2(用来放时间选择控件)
	public void setView(View view, int resId) {
		layout_content.setBackgroundResource(resId);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(ScreenUtils.dip2px(context, 30), // 左
				ScreenUtils.dip2px(context, 0), // 上
				ScreenUtils.dip2px(context, 30), // 右
				ScreenUtils.dip2px(context, 20));// 下
		view.setLayoutParams(layoutParams);
		layout_content.addView(view, 2);
	}

	/**
	 * 设置白色按钮
	 * 
	 * @param text
	 * @param listener
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		Resources r = context.getResources();
		// 根据XML资源的ID获取解析该资源的解析器
		// XmlResourceParser是XmlPullParser的子类
		XmlResourceParser xrp = context.getResources().getXml(
				AGResource.getDrawbleByName(context, positiveButtonTextColor));
		ColorStateList cl;
		try {
			cl = ColorStateList.createFromXml(context.getResources(), xrp);

			Button button = new Button(context);
			LayoutParams params = new LayoutParams(
					ScreenUtils.dip2px(context, 100),
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			button.setLayoutParams(params);
			button.setBackgroundResource(AGResource.getDrawbleByName(context,
					positiveButtonStyle));
			button.setText(text);
			button.setTextColor(cl);
			button.setTextSize(positiveButtonTextSize);// 默认12sp
			button.setOnClickListener(listener);
			params.setMargins(24, 0, 24, 0);
			button.setLayoutParams(params);
			int ten = ScreenUtils.dip2px(context, 10);
			button.setPadding(0, ten, 0, ten);
			UIHelper.SetLayoutParams(context, button, LayoutEnum.Magin,
					UIHelper.Base640);
			buttonLayout.addView(button, buttonLayout.getChildCount());

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 新增方法，增加字体
	 * 
	 * @param text
	 * @param textSize
	 *            按钮字体大小 sp单位
	 * @param listener
	 */
	public void setPositiveButton(String text, float textSize,
			final View.OnClickListener listener) {
		Resources r = context.getResources();
		// 根据XML资源的ID获取解析该资源的解析器
		// XmlResourceParser是XmlPullParser的子类
		XmlResourceParser xrp = context.getResources().getXml(
				AGResource.getDrawbleByName(context, positiveButtonTextColor));
		ColorStateList cl;
		try {
			cl = ColorStateList.createFromXml(context.getResources(), xrp);

			Button button = new Button(context);
			LayoutParams params = new LayoutParams(
					ScreenUtils.dip2px(context, 100),
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			button.setLayoutParams(params);
			button.setBackgroundResource(AGResource.getDrawbleByName(context,
					positiveButtonStyle));
			button.setText(text);
			button.setTextColor(cl);
			button.setTextSize(textSize);
			button.setOnClickListener(listener);
			params.setMargins(24, 0, 24, 0);
			button.setLayoutParams(params);
			int ten = ScreenUtils.dip2px(context, 10);
			button.setPadding(0, ten, 0, ten);
			UIHelper.SetLayoutParams(context, button, LayoutEnum.Magin,
					UIHelper.Base640);
			buttonLayout.addView(button, buttonLayout.getChildCount());

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 设置蓝色按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LayoutParams params = new LayoutParams(ScreenUtils.dip2px(
				context, 100), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		button.setBackgroundResource(AGResource.getDrawbleByName(context,
				negativeButtonStyle));
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setTextSize(negativeButtonTextSize);// 默认12sp
		button.setOnClickListener(listener);
		params.setMargins(24, 0, 24, 0);
		button.setLayoutParams(params);

		int ten = ScreenUtils.dip2px(context, 10);
		button.setPadding(0, ten, 0, ten);
		UIHelper.SetLayoutParams(context, button, LayoutEnum.Magin,
				UIHelper.Base640);
		buttonLayout.addView(button, buttonLayout.getChildCount());
	}

	/**
	 * 新增的方法
	 * 
	 * @param text
	 * @param textSize
	 * @param listener
	 */
	public void setNegativeButton(String text, float textSize,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LayoutParams params = new LayoutParams(ScreenUtils.dip2px(
				context, 100), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		button.setBackgroundResource(AGResource.getDrawbleByName(context,
				negativeButtonStyle));
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setTextSize(textSize);
		button.setOnClickListener(listener);
		params.setMargins(24, 0, 24, 0);
		button.setLayoutParams(params);

		int ten = ScreenUtils.dip2px(context, 10);
		button.setPadding(0, ten, 0, ten);
		UIHelper.SetLayoutParams(context, button, LayoutEnum.Magin,
				UIHelper.Base640);
		buttonLayout.addView(button, buttonLayout.getChildCount());
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}

	/**
	 * 点击空白处|返回键是否要关掉对话框
	 * 
	 * @param value
	 *            true关掉|false不关
	 */
	public void setCancelable(boolean value) {
		ad.setCancelable(value);
	}

	/**
	 * 点击空白处是否要消失
	 */
	public void setCanceledOnTouchOutside(boolean value) {
		ad.setCanceledOnTouchOutside(value);
	}

	private String negativeButtonStyle = "btn_circle_style";
	private String positiveButtonStyle = "btn_circle_wht_stytle";
	private String positiveButtonTextColor = "text_style";
	private String negativeButtonTextColor = "text_style";
	private float positiveButtonTextSize = 18;// 按钮文字大小
	private float negativeButtonTextSize = 18;// 同上

	public void setNegativeButtonStyle(String value) {
		this.negativeButtonStyle = value;
	}

	public void setPositiveButtonStyle(String value) {
		this.positiveButtonStyle = value;
	}

	public String getPositiveButtonTextColor() {
		return positiveButtonTextColor;
	}

	public void setPositiveButtonTextColor(String positiveButtonTextColor) {
		this.positiveButtonTextColor = positiveButtonTextColor;
	}

	public String getNegativeButtonTextColor() {
		return negativeButtonTextColor;
	}

	public void setNegativeButtonTextColor(String negativeButtonTextColor) {
		this.negativeButtonTextColor = negativeButtonTextColor;
	}
}