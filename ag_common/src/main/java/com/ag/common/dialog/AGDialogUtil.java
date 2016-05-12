package com.ag.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.ag.common.other.NetworkStateUtils;
import com.ag.common.update.IAlertDialogResult;
import com.ag.common.update.MyAlertDialog;

public class AGDialogUtil {

	public static void showDialog(Context context, String title, String ok,
			String cancel, final IAlertDialogResult iDialogResult) {
		showDialog(context, title, ok, cancel, null, null, null, null, 18, 18,
				iDialogResult);
	}
	
	public static void showDialog(Context context, String title, String ok,
			String cancel,float textsize, final IAlertDialogResult iDialogResult) {
		showDialog(context, title, ok, cancel, null, null, null, null, textsize, textsize,
				iDialogResult);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param ok
	 * @param cancel
	 * @param positiveButtonTextColor
	 * @param negativeButtonTextColor
	 * @param positiveButtonStyle
	 * @param negativeButtonStyle
	 * @param positiveButtonTextSize
	 * @param negativeButtonTextSize
	 * @param iDialogResult
	 */
	public static void showDialog(Context context, String title, String ok,
			String cancel, String positiveButtonTextColor,
			String negativeButtonTextColor, String positiveButtonStyle,
			String negativeButtonStyle, float positiveButtonTextSize,
			float negativeButtonTextSize, final IAlertDialogResult iDialogResult) {
		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessage(title);
		if (!TextUtils.isEmpty(positiveButtonTextColor)) {
			dialog.setPositiveButtonTextColor(positiveButtonTextColor);
		}
		if (!TextUtils.isEmpty(negativeButtonTextColor)) {
			dialog.setNegativeButtonTextColor(negativeButtonTextColor);
		}
		if (!TextUtils.isEmpty(positiveButtonStyle)) {
			dialog.setPositiveButtonStyle(positiveButtonStyle);
		}
		if (!TextUtils.isEmpty(negativeButtonStyle)) {
			dialog.setNegativeButtonStyle(negativeButtonStyle);
		}
		if (!TextUtils.isEmpty(cancel)) {
			if (positiveButtonTextSize > 0) {
				// 如果传递的字体大小参数大于0则设置，否则根据默认值
				dialog.setPositiveButton(cancel, positiveButtonTextSize,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
								if (iDialogResult != null)
									iDialogResult.resultCancel();
							}
						});
			} else {
				dialog.setPositiveButton(cancel, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (iDialogResult != null)
							iDialogResult.resultCancel();
					}
				});
			}
		}
		if (!TextUtils.isEmpty(ok)) {
			if (negativeButtonTextSize > 0) {
				dialog.setNegativeButton(ok, negativeButtonTextSize,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
								if (iDialogResult != null)
									iDialogResult.resultOK();
							}
						});
			} else {
				dialog.setNegativeButton(ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (iDialogResult != null)
							iDialogResult.resultOK();
					}
				});
			}
		}
		dialog.show();
	}

	public static void showDialogLeft(Context context, String title,
			String ok, String cancel, final IAlertDialogResult iDialogResult) {
		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessageLeft(title);
		if (!TextUtils.isEmpty(cancel)) {
			dialog.setPositiveButton(cancel, new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (iDialogResult != null)
						iDialogResult.resultCancel();
				}
			});
		}
		if (!TextUtils.isEmpty(ok)) {
			dialog.setNegativeButton(ok, new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (iDialogResult != null)
						iDialogResult.resultOK();
				}
			});
		}

		dialog.show();
	}

	public static void showDialogAndFinish(final Context context, String msg) {
		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage(msg);
		dialog.setNegativeButton("确定", new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (context instanceof Activity) {
					((Activity)context).finish();
				}
			}
		});
	}

	/**
	 * 接口返回数据为空，检查是否有网络
	 * 
	 * @param context
	 */
	public static void dataNullAndCheckNetDialog(Context context) {
		dataNullAndCheckNetDialog(context, true);
	}

	public static void dataNullAndCheckNetDialog(final Context context,
			final boolean isFinish) {
		if (!NetworkStateUtils.isNetworkConnected(context)) {
			if (isFinish && context instanceof Activity) {
				((Activity)context).finish();
			}
			return;
		}

		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("请检查网络是否可用！");
		dialog.setNegativeButton("确定", new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (isFinish && context instanceof Activity) {
					((Activity)context).finish();
				}
			}
		});
	}

	/**
	 * result为空,只有一个关闭按钮，关闭当前界面
	 * 
	 * @param context
	 * @param msg
	 */
	public static void alertNullDialog(final Context context, String msg,
			final IDialogResult iDialogResult) {
		final MyAlertDialog alertDialog = new MyAlertDialog(context);
		if (msg != null) {
			alertDialog.setMessage(msg);
		} else {
			alertDialog.setMessage("");
		}
		alertDialog.setNegativeButton("关闭", new OnClickListener() {
			public void onClick(View arg0) {
				alertDialog.dismiss();
				if (iDialogResult != null) {
					iDialogResult.onSuccess(true);
				}
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
	}

}
