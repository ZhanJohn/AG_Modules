package com.ag.common.ui;

import android.app.Activity;
import android.content.Context;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ag.common.other.StringUtils;
import com.ag.common.screen.DisplayUtil;
import com.ag.common.screen.ScreenUtils;

public class UIHelper {
	
	public static final int Base640=640;
	public static final int Base720=720;
	public static final int Base480=480;
	public static final int Base1080=1080;
	
	public static UIHelper Instance(){
		return new UIHelper();
	}
	
	public static void SetLayoutParams(Context context, View view,LayoutEnum layoutEnum,int basePx){
		if(layoutEnum == LayoutEnum.Width){
			SetLayoutParamsWidth(context,view,basePx);
		}else if(layoutEnum == LayoutEnum.Height){
			SetLayoutParamsHeight(context,view,basePx);
		}else if(layoutEnum == LayoutEnum.WidthAndHeight){
			SetLayoutParams(context,view,basePx);
		}else if(layoutEnum == LayoutEnum.WidthHeightAndMargin){
			SetMarginAndLayoutParams(context,view,basePx);
		}else if(layoutEnum == LayoutEnum.Magin){
			SetLayoutMargin(context,view,basePx);
		}
	}
	
	private static void SetLayoutParamsWidth(Context context, View view,int basePx){
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = DisplayUtil.ToTruePx(context, layoutParams.width,basePx);
	}
	
	private static void SetLayoutParamsHeight(Context context, View view,int basePx){
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = DisplayUtil.ToTruePx(context, layoutParams.height,basePx);
	}
	
	private static void SetLayoutParams(Context context, View view,int basePx){
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = DisplayUtil.ToTruePx(context, layoutParams.width,basePx);
		layoutParams.height = DisplayUtil.ToTruePx(context, layoutParams.height,basePx);
	}
	
	private static void SetLayoutMargin(Context context, View view,int basePx){
		if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams)view.getLayoutParams();
			layoutParams.leftMargin = DisplayUtil.ToTruePx(context, layoutParams.leftMargin,basePx);
			layoutParams.rightMargin = DisplayUtil.ToTruePx(context, layoutParams.rightMargin,basePx);
			layoutParams.topMargin = DisplayUtil.ToTruePx(context, layoutParams.topMargin,basePx);
			layoutParams.bottomMargin = DisplayUtil.ToTruePx(context, layoutParams.bottomMargin,basePx);
			//view.setLayoutParams(layoutParams);
		}else if(view.getLayoutParams() instanceof RelativeLayout.LayoutParams){
			RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)view.getLayoutParams();
			layoutParams.leftMargin = DisplayUtil.ToTruePx(context, layoutParams.leftMargin,basePx);
			layoutParams.rightMargin = DisplayUtil.ToTruePx(context, layoutParams.rightMargin,basePx);
			layoutParams.topMargin = DisplayUtil.ToTruePx(context, layoutParams.topMargin,basePx);
			layoutParams.bottomMargin = DisplayUtil.ToTruePx(context, layoutParams.bottomMargin,basePx);
			//view.setLayoutParams(layoutParams);
		}
	}
	
	private static void SetMarginAndLayoutParams(Context context, View view,int basePx){
		//SetMargin
		if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams)view.getLayoutParams();
			layoutParams.width = DisplayUtil.ToTruePx(context, layoutParams.width,basePx);
			layoutParams.height = DisplayUtil.ToTruePx(context, layoutParams.height,basePx);
			layoutParams.leftMargin = DisplayUtil.ToTruePx(context, layoutParams.leftMargin,basePx);
			layoutParams.rightMargin = DisplayUtil.ToTruePx(context, layoutParams.rightMargin,basePx);
			layoutParams.topMargin = DisplayUtil.ToTruePx(context, layoutParams.topMargin,basePx);
			layoutParams.bottomMargin = DisplayUtil.ToTruePx(context, layoutParams.bottomMargin,basePx);
			view.setLayoutParams(layoutParams);
			Log.i("zzz", view.getId()+"--LinearLayout.LayoutParams");
		}else if(view.getLayoutParams() instanceof RelativeLayout.LayoutParams){
			RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)view.getLayoutParams();
			layoutParams.width = DisplayUtil.ToTruePx(context, layoutParams.width,basePx);
			layoutParams.height = DisplayUtil.ToTruePx(context, layoutParams.height,basePx);
			layoutParams.leftMargin = DisplayUtil.ToTruePx(context, layoutParams.leftMargin,basePx);
			layoutParams.rightMargin = DisplayUtil.ToTruePx(context, layoutParams.rightMargin,basePx);
			layoutParams.topMargin = DisplayUtil.ToTruePx(context, layoutParams.topMargin,basePx);
			layoutParams.bottomMargin = DisplayUtil.ToTruePx(context, layoutParams.bottomMargin,basePx);
			view.setLayoutParams(layoutParams);
			Log.i("zzz", view.getId()+"--RelativeLayout.LayoutParams");
		}
	}
	
	/**
	 * 设置光标的位置在最后
	 * @param editText
	 */
	public static void setCursor(EditText editText){
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable)text;
			Selection.setSelection(spanText, text.length());
		}
	}
	
	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}
	
	/**
	 * 设置商品价格
	 * @param textView
	 * @param price
	 */
	public static void SetTextViewPrice(TextView textView,String price){
		
		if(StringUtils.isEmpty(price)){
			textView.setVisibility(View.GONE);
			return;
		}
		double p = StringUtils.SafeDouble(price.replace(",", ""),0);
		
		textView.setVisibility(View.VISIBLE);
		if(p>0){
			textView.setText(String.format("￥%.2f", p));
		}else{
			textView.setVisibility(View.GONE);
		}
		
	}
	
	public static LinearLayout.LayoutParams SetLinearLayoutParams(int width,int height){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, height);
		params.width = width;
		params.height=height;
		return params;
	}
	
	public static RelativeLayout.LayoutParams SetRelativeLayoutParams(int width,int height){
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width, height);
		params.width = width;
		params.height=height;
		return params;
	}
	
	/**
	 * 设置GridView的高度
	 * @param gridView
	 * @param cantUse	不可用的高度（dp）
	 */
	public static void setGridViewHeight(Context context, AbsListView gridView, int cantUse) {
		//com.zq.goodshantou.utils.Toast.ToastMessage(context, "sdfs");
		// 真实的屏幕高度（除去标题栏，如果虚拟按键，也去了）
		int realHeight = ScreenUtils.getScreenRect(context)[1] - ScreenUtils.getStatusBarHeight(context);
		
		// 减去APP中的标题、导航栏等的高度，剩下可用的内容展示页
		int canUseHeighy = realHeight - ScreenUtils.dip2px(context, cantUse);
		
		gridView.getLayoutParams().height = canUseHeighy;
		
		System.out.println("setGridViewHeight realHeight = " + realHeight + " canUseHeighy = " + canUseHeighy + " height = " + gridView.getLayoutParams().height);
	}
	
}
