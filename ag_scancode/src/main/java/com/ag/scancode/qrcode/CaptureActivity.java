package com.ag.scancode.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ag.scancode.camera.CameraManager;
import com.ag.scancode.encode.BitmapLuminanceSource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

public abstract class CaptureActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
			EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
					ResultMetadataType.SUGGESTED_PRICE,
					ResultMetadataType.ERROR_CORRECTION_LEVEL,
					ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	//	private TextView statusView;
//	private View resultView;
	private Result lastResult;
	private boolean hasSurface;
	private boolean copyToClipboard;
	private IntentSource source;
	private String sourceUrl;
	//	private ScanFromWebPageManager scanFromWebPageManager;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType,?> decodeHints;
	private String characterSet;
	//	private HistoryManager historyManager;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;

	private boolean hasLight;
	private ImageButton btnLight, mImageButton, btnBack;
	public ImageButton btnScanLog;
	private ImageView layout_img_scan_bg;
	private static final int PICTURE=22;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(ResourceUtil.getLayoutByName(this, "activity_capture"));
		layout_img_scan_bg=(ImageView)findViewById(ResourceUtil.getIdByName(this,"layout_img_scan_bg"));
		btnBack = (ImageButton) findViewById(ResourceUtil.getIdByName(this, "btn_back"));
		btnLight = (ImageButton) findViewById(ResourceUtil.getIdByName(this, "btn_light"));
		btnScanLog = (ImageButton) findViewById(ResourceUtil.getIdByName(this, "btn_scanlog"));
		mImageButton = (ImageButton) findViewById(ResourceUtil.getIdByName(this, "button_function"));

		btnLight.setOnClickListener(this);
		btnScanLog.setOnClickListener(this);
		mImageButton.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);

		PreferenceManager.setDefaultValues(this,ResourceUtil.getValueIntByName(this, "preferences", "xml"), false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(TAG, "onResume");

		onPause();

		// CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
		// want to open the camera driver and measure the screen size if we're going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(ResourceUtil.getIdByName(this, "viewfinder_view"));
		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		lastResult = null;

		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(ResourceUtil.getIdByName(this, "preview_view"));
		SurfaceHolder surfaceHolder = surfaceView.getHolder();

		// android2.3以下版本需要setType
		if(Build.VERSION.SDK_INT < 11)
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

		Intent intent = getIntent();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		copyToClipboard = prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
				&& (intent == null || intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true));
	}

	@Override
	protected void onPause() {

		Log.i(TAG, "onPause");

		if (isOpen) {
			isOpen = false;
			btnLight.setImageResource(ResourceUtil.getMipmapByName(this, "icon48x48_sweep_1c"));
			cameraManager.setTorch(false);
		}

		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}

		if (inactivityTimer != null) {
			inactivityTimer.onPause();
		}

		if (ambientLightManager != null) {
			ambientLightManager.stop();
		}

		if (cameraManager != null) {
			cameraManager.closeDriver();
		}

		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(ResourceUtil.getIdByName(this, "preview_view"));
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			if(Build.VERSION.SDK_INT < 11)
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			surfaceHolder.removeCallback(this);
		}

		super.onPause();
	}


	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if(inactivityTimer!=null)
			inactivityTimer.shutdown();

		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:

				Log.i(TAG, "KEYCODE_BACK");

				if (source == IntentSource.NATIVE_APP_INTENT) {
					setResult(RESULT_CANCELED);
					Log.i(TAG, "RESULT_CANCELED");
				}

				if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
					restartPreviewAfterDelay(0L);
					Log.i(TAG, "restartPreviewAfterDelay");
				}

				setResult(120, getIntent());
				finish();

				break;
			// Handle these events so they don't launch the Camera app
			case KeyEvent.KEYCODE_FOCUS:
			case KeyEvent.KEYCODE_CAMERA:
				return false;
			// Use volume up/down to turn on light
//		case KeyEvent.KEYCODE_VOLUME_DOWN:
//			cameraManager.setTorch(false);
//			return false;
//		case KeyEvent.KEYCODE_VOLUME_UP:
//			cameraManager.setTorch(true);
//			return false;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(resultCode!=RESULT_OK)
			return;

		if(requestCode==PICTURE){
			if(intent==null){
				return;
			}

			Uri selectedImage = intent.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			// 获取图片并显示
//			Bitmap bmp = BitmapFactory.decodeFile(picturePath);
			Bitmap bmp = ImageUtils.getZoomImage(picturePath);
			if(bmp==null){
				return;
			}
			// 开始对图像资源解码

			Result rawResult = null;
			try {
				LuminanceSource lumin=new BitmapLuminanceSource(bmp);
				Binarizer bz=new HybridBinarizer(lumin);
				BinaryBitmap bb=new BinaryBitmap(bz);
				rawResult = new MultiFormatReader().decodeWithState(bb);
			} catch (NotFoundException e) {
				Log.i(this.getClass().getSimpleName(), "rawResult-NotFoundException="+e.getMessage());
				e.printStackTrace();
				rawResult=null;
			}catch(Exception e){
				Log.i(this.getClass().getSimpleName(), "rawResult-Exception="+e.getMessage());
				e.printStackTrace();
				rawResult=null;
			}

			if(rawResult==null){
				Toast.makeText(getApplicationContext(), "检测不到二维码图片,请重新选择", Toast.LENGTH_LONG).show();
				return;
			}

			handleDecode(rawResult, bmp, 0);

			if (!bmp.isRecycled()) {
				bmp.recycle();
				System.gc();
			}
		}
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler, ResourceUtil.getIdByName(this, "decode_succeeded"), savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * 处理扫描结果
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param rawResult The contents of the barcode.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param barcode   A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		handleDecode(rawResult.getText());
	}

	/**
	 * 请重写这个方法获取扫码结果
	 * @param scanResult
     */
	public abstract void handleDecode(String scanResult);

	/**
	 * 打开扫码历史
	 */
	public abstract void openScanHistory();

	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	 *
	 * @param barcode A bitmap of the captured image.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param rawResult The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(ResourceUtil.getValueIntByName(this, "result_points", "color")));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4 &&
					(rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
							rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(),
					scaleFactor * a.getY(),
					scaleFactor * b.getX(),
					scaleFactor * b.getY(),
					paint);
		}
	}

	private void sendReplyMessage(int id, Object arg, long delayMS) {
		if (handler != null) {
			Message message = Message.obtain(handler, id, arg);
			if (delayMS > 0L) {
				handler.sendMessageDelayed(message, delayMS);
			} else {
				handler.sendMessage(message);
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
//			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
//			displayFrameworkBugMessageAndExit();
		}
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(ResourceUtil.getIdByName(this, "restart_preview"), delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.GONE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	//	private Camera mCamera;
//	private Camera.Parameters mParam;
	private boolean isOpen;

	@Override
	public void onClick(View v) {
		if (v.getId() == ResourceUtil.getIdByName(this, "btn_back")) {
			finish();
		}

		else if (v.getId() == ResourceUtil.getIdByName(this, "btn_light")) {

			PackageManager pm = this.getPackageManager();
			FeatureInfo[] features = pm.getSystemAvailableFeatures();

			for(FeatureInfo f : features){
				//判断设备是否支持闪光灯
				if(PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
					hasLight = true;
					break;
				}else{
					hasLight = false;
				}
			}

			if (hasLight) {
				//　当前打开，改为关闭
				if (isOpen) {
					isOpen = false;
					btnLight.setImageResource(ResourceUtil.getMipmapByName(this, "icon48x48_sweep_1c"));
					cameraManager.setTorch(false);
				}
				//　当前关闭，改为打开
				else {
					isOpen = true;
					btnLight.setImageResource(ResourceUtil.getMipmapByName(this, "icon48x48_sweep_1"));
					cameraManager.setTorch(true);
				}
			}else {
				Toast.makeText(getApplicationContext(), "该设备不支持闪光灯", Toast.LENGTH_LONG).show();
			}
		}

		else if (v.getId() == ResourceUtil.getIdByName(this, "button_function")) {
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, PICTURE);
		}
		else if(v.getId() == ResourceUtil.getIdByName(this,"btn_scanlog")){
			openScanHistory();
		}

	}

}