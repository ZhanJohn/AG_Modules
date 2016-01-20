package com.ag.common.webview.upload;


public interface IWebViewUploadListener {

    /**
     * 开始上传，可以在此显示自定义processDialog
     */
    void onUploadStart();

    /**
     * 上传失败，如果之前有调用了showDialog，在此取消它，并作相应的失败提示
     */
    void onUploadError();

    /**
     * 上传图片成功的回调，需要自已处理js的回调
     */
    void onUploadSuccess(String imgUrl);

    /**
     * 调用webview上传前显示自定义的dialog
     * 在自定义的dialog可以显示相册和相机的选择项
     * openGalleryOrCamera():打开相册或相机
     */
    void onUploadShowDialog();

}
