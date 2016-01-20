package com.ag.common.webview.upload;


public enum UploadTypeEnum {

    /**
     * 相册
     */
    Gallery(1),

    /**
     * 相机
     */
    Camera(2);

    private final int type;
    private UploadTypeEnum(int type){
        this.type=type;
    }
    public int getTypeValue(){
        return type;
    }

}
