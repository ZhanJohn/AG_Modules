package com.ag.common.webview.upload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.ag.common.image.ImageUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.extras.Base64;

/**
 *webview调用系统相册或相机进行上传图片
 */
public class WebViewImageUploadHelper {

	private final static int INTENT_CALL_GALLERY = 3001;
	private final static int INTENT_CALL_CAMERA = 4001;
	public static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory() + File.separator + "upload";
	public static final String DIRECTORY_PHOTO_PATH = DIRECTORY_PATH + File.separator + "photo";
	public static final String DIRECTORY_VOICE_PATH = DIRECTORY_PATH + File.separator + "voice";
	public static final String DIRECTORY_DAMAGE_PATH = DIRECTORY_PATH + File.separator + "damage";
	

	public static final int KITKAT_FILECHOOSER = 10002;
	public static final int KITKAT_CAMERA = 10003;

	private static WebViewImageUploadHelper mHelper;
	private Context mContext;
	private static HashMap<String, File> mContent;
//
	private String mKey="img";
//	private String mThumbnailId;
//	private String callbackMethod;
	private String uploadUrl;
//	private WebView mWebView;
	private File mTempFile;

//	private OnClickListener mDialogCallbackListener;
	//
	private IWebViewUploadListener iWebViewUploadListener;
	private WebViewUploadParams uploadParams;

	private WebViewImageUploadHelper(Context context) {
		mContext = context;
//		mDialogCallbackListener = new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//				if (which == 0) {
//					callCamera(INTENT_CALL_CAMERA);
//				} else if (which == 1) {
//					callGallery(INTENT_CALL_GALLERY);
//				}
//			}
//		};
	}

	public static final WebViewImageUploadHelper getInstance(Context context) {

		if (mHelper == null) {
			mHelper = new WebViewImageUploadHelper(context);
			mContent = new HashMap<String, File>();
		}
		return mHelper;

	}

	/**
	 * Activity onDestroy时要调用
	 */
	public static final void clearObject(){
		if(mHelper!=null){
			mHelper=null;
		}
	}

	/**
	 *打开相册上传图片
     */
	public synchronized void open(String uploadUrl,WebViewUploadParams uploadParams, IWebViewUploadListener iWebViewUploadListener) {
		if(TextUtils.isEmpty(uploadUrl) || uploadParams==null || iWebViewUploadListener==null)
			return;

		this.iWebViewUploadListener=iWebViewUploadListener;
		this.uploadUrl=uploadUrl;
		this.uploadParams=uploadParams;
		//
		this.iWebViewUploadListener.onUploadShowDialog();
//		callGallery();
		
//		mKey = key;
//		mThumbnailId = thumbnailId;

//		String[] items = null;
//		items = new String[] { "拍照", "相册" } ;
//
//		mDialog.showListDialog(null, items, mDialogCallbackListener);

	}

	public void openGalleryOrCamera(UploadTypeEnum uploadTypeEnum){
		if(uploadTypeEnum.getTypeValue() == UploadTypeEnum.Gallery.getTypeValue()){
			callGallery();
		}else if(uploadTypeEnum.getTypeValue() == UploadTypeEnum.Camera.getTypeValue()){
			callCamera();
		}
	}

	/**
	 * 打开相册
     */
	private void callGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		// intent.setAction(Intent.ACTION_CHOOSER );
		// intent.setType( android.provider.MediaStore.Images.Media.CONTENT_TYPE
		// );
		((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "File Chooser"), KITKAT_FILECHOOSER);
	}

	/**
	 * 打开相机
	 */
	private void callCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File directory = new File(DIRECTORY_PHOTO_PATH);

		if (!directory.exists()) {
			directory.mkdir();
		}
		mTempFile = new File(directory, "photo_" + new Date().getTime() + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
		
		((Activity) mContext).startActivityForResult(intent, KITKAT_CAMERA);
	}

	/**
	 * 上传图片
	 * @param uri : file path
	 */
	public final void updateContent(Uri uri) {
		if (uri == null) {
			return;
		}
		
		File file = uriToFile(uri);
		
		String type = getMimeType(uri);

		mContent.put(mKey, file);

		updateImage(file, type);

	}

	/**
	 * 上传图片
	 */
	public final void updateContent() {
		Uri uri = Uri.fromFile(mTempFile);

		((Activity) mContext).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
		
		File file = mTempFile;
		
		String type = getMimeType(uri);
		
		mContent.put(mKey, file);

		updateImage(file, type);
		
		mTempFile = null;
		
	}


	/**
	 * 更新图片至服务器
	 * @param file
	 * @param type
	 */
	private void updateImage(File file,String type){
		//
		//上传至服务器，成功后回调图片url给JS

		iWebViewUploadListener.onUploadStart();

		//
		AsyncHttpClient client=new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		RequestParams params=new RequestParams();
		params.put("FileData", fileToString(file));
		params.put("PlatformId",String.valueOf(uploadParams.getProjectType().GetEnumValue()));
		params.put("userid",uploadParams.getUserId());

//		try {
//			params.put("FileData", file);
//		}catch (FileNotFoundException e){
//			e.printStackTrace();
//		}
		client.post(uploadUrl, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				iWebViewUploadListener.onUploadError();
//				com.zq.common.other.Toast.ToastMessage(mContext, "图片上传失败");
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
//				dialog.cancel();
				if(TextUtils.isEmpty(s)){
					iWebViewUploadListener.onUploadError();
//					com.zq.common.other.Toast.ToastMessage(mContext, "图片上传失败");
					return;
				}
//				Toast.ToastMessage(mContext, "onSuccess==" + s);
				Log.d("upload-img","onSuccess==" + s);
				clear();

				UploadInfo info=(new Gson()).fromJson(s,UploadInfo.class);
				if(info==null){
					iWebViewUploadListener.onUploadError();
					return;
				}

				iWebViewUploadListener.onUploadSuccess(info.getMessage());

//				UploadInfo info=(new Gson()).fromJson(s,UploadInfo.class);
//				if(info==null){
//					com.zq.common.other.Toast.ToastMessage(mContext, "图片上传失败");
//					return;
//				}
//				//
//				mWebView.loadUrl(String.format("javascript:%s('%s')", callbackMethod, info.getMessage()));

			}
		});


	}
	
	/**
	 *
	 * @param uri
	 * @return
	 */
	private String getMimeType(Uri uri) {
		ContentResolver cR = mContext.getContentResolver();
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String type = cR.getType(uri);
//		String type = mime.getExtensionFromMimeType(cR.getType(uri));
	    return type;
	}
	
	/**
	 *
	 * @param uri 
	 * @return
	 */
	@TargetApi(19)
	private File uriToFile ( Uri uri ) {
		
		String filePath = "";
		
		if ( uri.getPath().contains(":") ) {
	
			String wholeID = DocumentsContract.getDocumentId(uri);
	
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
	
			String[] column = { MediaStore.Images.Media.DATA };     
	
			// where id is equal to             
			String sel = MediaStore.Images.Media._ID + "=?";
	
			Cursor cursor = mContext.getContentResolver().
			                          query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
			                          column, sel, new String[]{ id }, null);
	
	
			int columnIndex = cursor.getColumnIndex(column[0]);
	
			if (cursor.moveToFirst()) {
			    filePath = cursor.getString(columnIndex);
			}   
	
			cursor.close();
			
		} else {
			String id = uri.getLastPathSegment(); 
		    final String[] imageColumns = {MediaStore.Images.Media.DATA };
		    final String imageOrderBy = null;
	
		    String selectedImagePath = "path";
		    String scheme = uri.getScheme();
		    if ( scheme.equalsIgnoreCase("content") ) {
		    	 Cursor imageCursor = mContext.getContentResolver().query(uri, imageColumns, null, null, null);
	
				    if (imageCursor.moveToFirst()) {
				    	filePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
				    }
		    } else {
		    	filePath = uri.getPath();
		    }
		}
	    
	    File file = new File( filePath );
	    
	    return file;
	}
	

	/**
	 *
	 */
	public final void clear() {
		mContent.clear();
//		mKey = null;
//		mThumbnailId = null;
	}

	/**
	 *
	 */
	public final void clearId() {
//		mKey = null;
//		mThumbnailId = null;
	}

	private String fileToString(File file) {

		//图片进行压缩
		Bitmap bitmap= ImageUtils.getZoomImage(file.getAbsolutePath());
		
		String fileString = new String();
//		FileInputStream inputStream = null;
//		ByteArrayOutputStream byteOutStream = null;

//		Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 800, 480, true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// CompressFormat set up to JPG, you can change to PNG or whatever you want;

		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		byte[] data = bos.toByteArray();

		fileString = new String(Base64.encode(data,0));

		return fileString;
	}

	private class UploadInfo implements Serializable {
		private boolean IsSuccess;
		private int StatusID;
		private String Message;
		private String Datas;

		public boolean isSuccess() {
			return IsSuccess;
		}

		public void setIsSuccess(boolean isSuccess) {
			IsSuccess = isSuccess;
		}

		public int getStatusID() {
			return StatusID;
		}

		public void setStatusID(int statusID) {
			StatusID = statusID;
		}

		public String getMessage() {
			return Message;
		}

		public void setMessage(String message) {
			Message = message;
		}

		public String getDatas() {
			return Datas;
		}

		public void setDatas(String datas) {
			Datas = datas;
		}
	}

}
