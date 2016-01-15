package com.ag.scancode.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageUtils {

	/**
	 * 缩放图片
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getZoomImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 读入图片，此时把options.inJustDecodeBounds 设回true
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);

		newOpts.inJustDecodeBounds = false;
		// 图片的宽
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		int hh = 800;// 这里设置高度
		int ww = 480;// 这里设置宽度

		// 缩放由于是固定比例缩 只用高或者宽其中数据进行计算即可
		int be = 1;//be=1表示不缩
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据高度固定大小缩
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;

		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}
}
