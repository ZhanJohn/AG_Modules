package com.ag.common.other;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.ag.common.res.AGPackage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 */
@SuppressLint({ "DefaultLocale", "NewApi"})
public class CommonUtil {
	
	public static boolean flag = false;
	public static int imageWidth = 240;
	public static int imageHeight = 180;
	public static String newVersion = "";
	public static String CACHE_FILE = "";
	private static final int MB = 1024*1024;
	private static int MIN_SIZE_MB = 10;
	
	static BroadcastReceiver downloadReceiver = null;
	
	/** 获得缓存目录 **/
    public static String getDirectory() {
        String dir = getSDPath() + CACHE_FILE;
        File mfile=new File(dir);
        if(!mfile.exists()){
			mfile.mkdir();
        }
        return dir;
    }
    
    /**
	 * 获取手机状态栏高度
	 * @param context
	 * @return
	 */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
    
    public static boolean isFileExists(String filename){
    	File file = new File(getDirectory()+"/"+filename);
    	return file.exists();
    }
    
    public static Bitmap getBitmap(String filename){
    	String path=getDirectory() + "/" + filename;
    	File mfile=new File(path);
        if (mfile.exists()){
        	System.out.println(path);
        	System.out.println("文件存在");
        	return BitmapFactory.decodeFile(path);
        }
        System.out.println("文件不存在");
        return null;
    }

    /** 保存方法 */
    public static void saveBitmap(String filename,Bitmap bmp) {
    	String path = getDirectory() + "/" + filename.replace("/", "_");
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			boolean flag=bmp.compress(CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			System.out.println("文件保存成功"+flag+";"+path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * 检测Sdcard是否存在
	 *
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
                                                                
    /** 取SD卡路径 **/
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
	
	/**
	 * 截取图片的名称
	 * @param url
	 * @return
	 */
	public static String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1];
        //return strs[strs.length - 1] + WHOLESALE_CONV;
    }
	
	@SuppressLint("NewApi")
	public static void download(Context context, String url) {  
		final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);;
		final String fileName = url.substring(url.lastIndexOf("/"));
		
		downloadReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				
				if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){  
					
					long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);       
	                
					Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downId));
					c.moveToFirst();
					String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
					
					System.out.println("刚下载好的文件： id = " + downId + "  文件名   = " + filePath);
					
	                Intent i = new Intent(Intent.ACTION_VIEW); 
	                i.setDataAndType(Uri.parse(filePath),"application/vnd.android.package-archive"); 
	                context.startActivity(i);
	                
	                if(downloadReceiver != null)
	                	context.unregisterReceiver(downloadReceiver);  
				}
			}
		};

//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//		context.registerReceiver(downloadReceiver, intentFilter);

		Uri uri = Uri.parse(url);
		Request request = new Request(uri);

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以  
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE| Request.NETWORK_WIFI);

		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION  
		// request.setShowRunningNotification(false);  

		// true,显示在下载界面中,false,不显示，默认是true  
		request.setVisibleInDownloadsUi(true);
		// 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，下载后的文件在/mnt/sdcard/Android/data/packageName/files目录下面，，不设置，下载后的文件在/cache这个  目录下面
		request.setDestinationInExternalFilesDir(context, null, fileName);

		long id = downloadManager.enqueue(request);
		// 把id保存好，在接收者里面要用，最好保存在Preferences里面
		SharedPreferences sPreferences = context.getSharedPreferences("downloadcomplete", 0);
		sPreferences.edit().putLong("refernece", id).commit();

	}
	
	public static Date StringToDate(String datestr){
		datestr = datestr.replace("/", "-");
		Date date = new Date();  
		//注意format的格式要与日期String的格式相匹配  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		try {  
			date = sdf.parse(datestr);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return date;
	}


	/**
	 * 为了释放内存，先自杀，推送服务会重启
	 */
	public static void clearMemory(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 获取某个应用是否正在运行
	 * @param c
	 * @param packName
	 * @return
	 */
	public static boolean getTaskRunning(Context c, String packName){
		ActivityManager am = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1000);
		for(RunningTaskInfo info : list){
			
			if(info.baseActivity.getPackageName().equals(packName)
//				&& !info.topActivity.getClassName().equals(QRCStartApp.class.getName())
//				&& !info.topActivity.getClassName().equals(BackSocketService.class.getName())
//				&& !info.topActivity.getClassName().equals(NotificationService.class.getName())
//				&& !info.topActivity.getClassName().equals(IsRunningService.class.getName())
//				&& !info.topActivity.getClassName().equals(BootUpService.class.getName())
				){
				
				Log.i("CommonUtil 存在", "PACKNAME:" + info.baseActivity.getPackageName() + " CLASSNAME:" + info.topActivity.getClassName());
				
				return true;
			}else {
				Log.i("CommonUtil 不存在", "PACKNAME:" + info.baseActivity.getPackageName() + " CLASSNAME:" + info.topActivity.getClassName());
			}
		}
		return false;
	}
	
	/**
	 * 判断某界面是否存在
	 * @param c
	 * @param packName
	 * @param className
	 * @return
	 */
	public static boolean getActivityExistTask(Context c, String packName, String className){
		ActivityManager am = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1000);
		for(RunningTaskInfo info : list){
			
			if(info.baseActivity.getPackageName().equals(packName)
				&& info.topActivity.getClassName().equals(className)
				){
				
				Log.i("CommonUtil 存在", "PACKNAME:" + info.baseActivity.getPackageName() + " CLASSNAME:" + info.topActivity.getClassName());
				
				return true;
			}else {
				Log.i("CommonUtil 不存在", "PACKNAME:" + info.baseActivity.getPackageName() + " CLASSNAME:" + info.topActivity.getClassName());
			}
		}
		return false;
	}
	
	/**
     * 判断当前应用程序处于前台还是后台
     * @param context
     * @return    
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
	
	/*
	 * 根据包名打开应用
	 */
	public static void startAppByPackageName(Context context, String packageName){
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		 
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);
		 
		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
		 
		ResolveInfo ri = apps.iterator().next();
		if (ri != null ) {
			String packageName1 = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;
			 
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			 
			ComponentName cn = new ComponentName(packageName1, className);
			 
			intent.setComponent(cn);
			context.startActivity(intent);
		}
		
	}
	
	/**
	 * 检测apk在本机是否已安装
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkApkExist(Context context, String packageName){

		if (packageName == null || "".equals(packageName)){
			return false;
		}
		try{
			context.getPackageManager().getApplicationInfo(
					packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		}
		catch (NameNotFoundException e){
			return false;
		}
	}
	
	/**
	 * 获取sdcard上的图片文件夹目录
	 * @return
	 */
	public static String GetSdcardPath(){
		return Environment.getExternalStorageDirectory()+CACHE_FILE;
	}
	
	/**
	 * 获取sdcard系统路径
	 * @return
	 */
	public static String GetSdcardSystemPath(){
		String rootpath = "";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			rootpath = Environment.getExternalStorageDirectory().toString();
		}
		return rootpath;
	}
	
	/**
	 * 检查sdcard是否存在
	 * @return
	 */
	public static boolean checkSDCard()
	{
	    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/** 计算sdcard上的剩余空间 **/
    public static long getFreeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return stat.getAvailableBlocks()*stat.getBlockSize();
    }
    
    /**
     * 查找sdcard是否有足够的空间
     * @return
     */
    public static boolean isAvaiableSpace(){
    	int size = (int)getFreeSpaceOnSd()/MB;
    	return size>MIN_SIZE_MB;
    }
    
    /**
     * 获取bitmap所占的空间大小
     * @return
     */
    public static long getBitmapSize(Bitmap bitmap){
    	long size = 0;
    	if(bitmap!=null){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				return bitmap.getByteCount();
			}else{
				// Pre HC-MR1
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
    	}
    	return size;
    }
	
	/**
	 * 获取图片缓存在sdcard的路径
	 * @return
	 */
	public static String GetCacheFilePath() {
		String filePath="";
		String rootpath = GetSdcardSystemPath();
		filePath = rootpath + CACHE_FILE;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}
	
	/**
	 * 判断sdcard中的该文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean IsExistsFile(String path){
		return new File(path).exists();
	}
	
	/**
	 * 从图片路径中获取图片后缀名
	 * @param name
	 * @return
	 */
	public static CompressFormat GetCompressFormat(String name){
		String extend = name.substring(name.lastIndexOf(".")+1).toLowerCase();
		if(extend.equals("png")){
			return CompressFormat.PNG;
		}
		return CompressFormat.JPEG;
	}
	
	/**
	 * 创建文件夹
	 * @param path
	 * @return
	 */
	public static boolean CreateDicretoryExists(String path){
		File file = new File(path);
		if(!file.exists()){
			System.out.println("文件夹创建完成！"+path);
			return file.mkdirs();
		}
		return true;
	}
	
	/**
	 * 压缩图片
	 * @param path 图片路径
	 */
	public static Bitmap compressImage(String path,int dstWidth,int dstHeight){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		//计算缩放比
		int w = (int)(options.outWidth/(float)dstWidth+0.8);
		int h = (int)(options.outHeight/(float)dstHeight+0.8);
		//System.out.println("宽的缩放比为:"+w+";高的缩放比为:"+h);
		//如果超出指定大小，缩放相应比例
		int temp=(w>1 || h>1)?(w>h?w:h):1;
		options.inSampleSize = temp;
		options.inJustDecodeBounds = false;
		//重新读入图片
		bmp = BitmapFactory.decodeFile(path, options);
		return bmp;
	}
	
	/**
	 * 获取本地分享图标
	 * @param context
	 * @param imgName shareicon.png
	 * @param imgId R.raw.shareicon
	 * @return
	 */
	public static String getAssetsResource(Context context, String imgName, int imgId){
		CACHE_FILE=String.format("/Android/data/%s/cache/", AGPackage.getPackageName(context));
		if (!new File(Environment.getExternalStorageDirectory() + CACHE_FILE + imgName).exists()) {
			InputStream is = context.getResources().openRawResource(imgId);
			Bitmap bmpBitmap = BitmapFactory.decodeStream(is);
			storeInSD(bmpBitmap,GetCacheFilePath(),imgName);
		}
		return Environment.getExternalStorageDirectory() + CACHE_FILE + imgName;
	}
	
	/**
	 * InputStream转String
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static boolean write2SDFromString(String path,String fileName,String input){
		boolean flag=true;
		File file = null;
		OutputStream output = null;
		try{
			file = new File(path + fileName);
			file.createNewFile();
//			creatSDDir(path);
			//file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer [] = input.getBytes();
			output.write(buffer, 0, buffer.length);
			output.flush();
		}
		catch(Exception e){
			return false;
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * 将图片保存至sdcard目录下
	 * @param bitmap 图片源
	 * @param path sdcard文件夹
	 * @param fileName 文件名
	 */
	public static void storeInSD(Bitmap bitmap,String path,String fileName) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		File imageFile = new File(file, fileName);
		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			//System.out.println("图片保存成功！");
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取网络图片
	 * @param url 网络图片路径
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null; 
        InputStream is=null;
        HttpURLConnection conn=null;
        try{
        	if(!url.startsWith("http://"))
        		url="http://"+url;
            myFileURL = new URL(url);
            //获得连接
            conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            //conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            is = conn.getInputStream();
           
            //解析得到图片
           /* BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
            opts.inJustDecodeBounds = false;*/
            try {
            	bitmap = BitmapFactory.decodeStream(is);
            } catch (OutOfMemoryError err) {}

//          bitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (null != is) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.disconnect();
				conn = null;
			}
        }
        return bitmap;
    }
	
	/**
	 * 图片加载与保存
	 * @param list
	 */
	public static void imageLoadAndSave(List<String> list){
		//判断sdcard是否存在，不存在时不下载图片
		if(CommonUtil.checkSDCard()){
			Bitmap bitmap=null;
			for (String s : list) {
				bitmap = CommonUtil.getHttpBitmap(s);
	            String fileName = s.substring(s.lastIndexOf("/")+1);
	            CommonUtil.storeInSD(bitmap, CommonUtil.GetCacheFilePath(), fileName);
			}
			if(bitmap!=null && !bitmap.isRecycled()){
				bitmap.recycle();
			}
		}
	}
	
	/**
	 * 判断输入的是否是汉字
	 * @param value
	 * @return
	 */
	public static boolean isChinese(String value)
	{
		//Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		//return pattern.matcher(value).matches();
		for(int i=0;i<value.length();i++)
		{
			char c  = value.charAt(i);
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	             || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	            //return true;
	        }else{
	        	return false;
	        }
		}
		return true;
	}
	
	public static boolean CheckUserName(String name)
	{
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(name);
		if (m.matches()) {

		}
		p = Pattern.compile("[a-zA-Z]");
		m = p.matcher(name);
		if (m.matches()) {

		}
		return true;
	}

	/**
	 * 处理溢出问题
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}

}
