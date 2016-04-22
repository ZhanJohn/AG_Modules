package com.ag.controls.customview;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class NetImageView extends ImageView {
    private final String TAG = "NetImageView";

    public NetImageView(Context context) {
        super(context);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(NetImageParam imageParam) {
        loadImageUrl(imageParam);
    }

    private void loadImageUrl(NetImageParam imageParam) {
        if (imageParam == null)
            return;

        if (TextUtils.isEmpty(imageParam.getImgUrl())) {
            Log.d(TAG,"NetImageView is null !!!");
            this.setImageResource(imageParam.getDefaultImg());
            return;
        }

        String imgUrl = imageParam.getImgUrl();

        if (!URLUtil.isHttpUrl(imgUrl)) {
            imgUrl = imageParam.getRootUrl() + imageParam.getImgUrl();
        }

        Log.d(TAG,"NetImageView=" + imgUrl);

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageParam.getDefaultLoadBG())
                .showImageForEmptyUri(imageParam.getDefaultImg())
                .showImageOnFail(imageParam.getDefaultImg())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoader.getInstance().displayImage(imgUrl, this, imageOptions);

    }
}