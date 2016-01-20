package com.ag.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ag.common.other.NetworkStateUtils;
import com.ag.common.update.IAlertDialogResult;
import com.ag.common.update.MyAlertDialog;

public class ZQDialogUtil {

	public static void ShowDialog(Activity activity, String title, String ok,
			String cancel, final IAlertDialogResult iDialogResult) {
		ShowDialog(activity, title, ok, cancel, null, null, null, null, 18, 18,
				iDialogResult);
	}
	
	public static void ShowDialog(Activity activity, String title, String ok,
			String cancel,float textsize, final IAlertDialogResult iDialogResult) {
		ShowDialog(activity, title, ok, cancel, null, null, null, null, textsize, textsize,
				iDialogResult);
	}

	/**
	 * 
	 * @param activity
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
	public static void ShowDialog(Activity activity, String title, String ok,
			String cancel, String positiveButtonTextColor,
			String negativeButtonTextColor, String positiveButtonStyle,
			String negativeButtonStyle, float positiveButtonTextSize,
			float negativeButtonTextSize, final IAlertDialogResult iDialogResult) {
		final MyAlertDialog dialog = new MyAlertDialog(activity);
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
									iDialogResult.ResultCancel();
							}
						});
			} else {
				dialog.setPositiveButton(cancel, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (iDialogResult != null)
							iDialogResult.ResultCancel();
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
									iDialogResult.ResultOK();
							}
						});
			} else {
				dialog.setNegativeButton(ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (iDialogResult != null)
							iDialogResult.ResultOK();
					}
				});
			}
		}
		dialog.show();
	}

	public static void ShowDialogLeft(Activity activity, String title,
			String ok, String cancel, final IAlertDialogResult iDialogResult) {
		final MyAlertDialog dialog = new MyAlertDialog(activity);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessageLeft(title);
		if (!TextUtils.isEmpty(cancel)) {
			dialog.setPositiveButton(cancel, new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (iDialogResult != null)
						iDialogResult.ResultCancel();
				}
			});
		}
		if (!TextUtils.isEmpty(ok)) {
			dialog.setNegativeButton(ok, new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (iDialogResult != null)
						iDialogResult.ResultOK();
				}
			});
		}

		dialog.show();
	}

	public static void ShowDialogAndFinish(final Activity activity, String msg) {
		final MyAlertDialog dialog = new MyAlertDialog(activity);
		dialog.setCancelable(false);
		dialog.setMessage(msg);
		dialog.setNegativeButton("确定", new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();
			}
		});
	}

	/**
	 * 接口返回数据为空，检查是否有网络
	 * 
	 * @param activity
	 */
	public static void DataNullAndCheckNetDialog(Activity activity) {
		DataNullAndCheckNetDialog(activity, true);
	}

	public static void DataNullAndCheckNetDialog(final Activity activity,
			final boolean isFinish) {
		if (!NetworkStateUtils.isNetworkConnected(activity)) {
			if (isFinish)
				activity.finish();
			return;
		}

		final MyAlertDialog dialog = new MyAlertDialog(activity);
		dialog.setCancelable(false);
		dialog.setMessage("请检查网络是否可用！");
		dialog.setNegativeButton("确定", new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (isFinish)
					activity.finish();
			}
		});
	}

	public static void SetEdittextPosition(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	public static Drawable GetDrawableById(Context context, int imgid) {
		Resources res = context.getResources();
		Drawable img_off = res.getDrawable(imgid);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_off.setBounds(0, 0, img_off.getMinimumWidth(),
				img_off.getMinimumHeight());
		return img_off;
	}

	/**
	 * result为空,只有一个关闭按钮，关闭当前界面
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void alertNullDialog(final Activity activity, String msg,
			final IDialogResult iDialogResult) {
		final MyAlertDialog alertDialog = new MyAlertDialog(activity);
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
				// application.finishActivity(activity);
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
	}

}
