package com.ag.common.js;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AGChromeClient extends InjectedChromeClient {

    private Activity mActivity;

    public AGChromeClient(String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }

    public AGChromeClient(String injectedName, Class injectedCls, boolean mGeolocationPermission) {
        super(injectedName, injectedCls,mGeolocationPermission);
    }

    public void setActivity(Activity activity){
        this.mActivity=activity;
    }

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

//    private ValueCallback<Uri> mUploadMessage;
//    public static int FILECHOOSER_RESULTCODE = 1;
//    private String mCameraFilePath;

    //<=4.4
    public ValueCallback<Uri> mUploadMessage;

    //5.0 upload file params
    public static final int INPUT_FILE_NEW_REQUEST_CODE = 33;
    public static final int INPUT_FILE_REQUEST_CODE = 22;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    public ValueCallback<Uri[]> mFilePathCallback;
    public String mCameraPhotoPath;
    private static final String TAG="zq";

    //The undocumented magic method override
    //Eclipse will swear at you if you try to put @Override here
    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "请选择"), INPUT_FILE_REQUEST_CODE);

    }

    // For Android 3.0+
    public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(
                Intent.createChooser(i, "请选择"),
                INPUT_FILE_REQUEST_CODE);
    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "请选择"), INPUT_FILE_REQUEST_CODE);
    }

    /**
     * 5.0
     * @param webView
     * @param filePathCallback
     * @param fileChooserParams
     * @return
     */
    public boolean onShowFileChooser(
            WebView webView, ValueCallback<Uri[]> filePathCallback,
            FileChooserParams fileChooserParams) {
        if(mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent[] intentArray;
        if(takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "请选择");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        mActivity.startActivityForResult(chooserIntent, INPUT_FILE_NEW_REQUEST_CODE);

        return true;
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

}
