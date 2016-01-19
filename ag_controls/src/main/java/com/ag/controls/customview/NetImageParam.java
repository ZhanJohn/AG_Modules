package com.ag.controls.customview;

import java.io.Serializable;

/**
 * Image's params
 */
public class NetImageParam implements Serializable{

    private int maxWidth;
    private int maxHeight;
    private float widthScale;
    private int defaultImg;
    private int defaultLoadBG;
    private String rootUrl;
    private String imgUrl;
    private int borderColor;
    private int borderWidth;

    public NetImageParam() {
    }

    public NetImageParam(int defaultImg, int defaultLoadBG, String rootUrl, String imgUrl) {
        this.rootUrl = rootUrl;
        this.imgUrl = imgUrl;
        this.defaultImg = defaultImg;
        this.defaultLoadBG = defaultLoadBG;
    }

    public NetImageParam(int defaultImg, int defaultLoadBG, String rootUrl, String imgUrl, int borderColor, int borderWidth) {
        this(defaultImg, defaultLoadBG, rootUrl, imgUrl);
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 宽高比
     *
     * @return
     */
    public float getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(float widthScale) {
        this.widthScale = widthScale;
    }

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public int getDefaultLoadBG() {
        return defaultLoadBG;
    }

    public void setDefaultLoadBG(int defaultLoadBG) {
        this.defaultLoadBG = defaultLoadBG;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
}