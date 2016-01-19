package com.ag.controls.customview;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class NetImageView extends ImageView {
    private final String TAG = "NetImageView";

    Resources res;
    Context context;
    private NetImageParam imageParam;

    public NetImageView(Context context) {
        super(context);
        res = context.getResources();
        this.context = context;
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
        this.context = context;
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        res = context.getResources();
        this.context = context;
    }

    public void setImageUrl(NetImageParam imageParam) {
        getBitmap(imageParam);
    }

    public void getBitmap(NetImageParam imageParam) {
        if (imageParam == null)
            return;

        if (TextUtils.isEmpty(imageParam.getImgUrl())) {
            System.out.println("NetImageView is null !!!");
            this.setImageResource(imageParam.getDefaultImg());
            return;
        }

        String imgUrl = "";
        if (!imageParam.getImgUrl().startsWith("http://")) {
            imgUrl = imageParam.getRootUrl() + imageParam.getImgUrl();
        } else {
            imgUrl = imageParam.getImgUrl();
        }

        System.out.println("NetImageView=" + imgUrl);

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