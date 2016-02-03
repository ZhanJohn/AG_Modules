package com.ag.common.update;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ag.common.other.AGAppControl;
import com.ag.common.other.CommonUtil;
import com.ag.common.url.ParseXmlService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 程序更新管理器
 */
public class UpdateManager {
	/* 下载中 */  
    private static final int DOWNLOAD = 1;  
    /* 下载游戏中 */  
    private static final int DOWNLOAD_GAME = 3;  
    /* 下载结束 */  
    private static final int DOWNLOAD_FINISH = 2;  
    /* 下载停止 */
    private static final int DOWNLOAD_STOP = 4;
    /* 保存解析的XML信息 */  
    HashMap<String, String> mHashMap;  
    /* 下载保存路径 */  
    private String mSavePath;  
    /* 记录进度条数量 */  
    private int progress;  
    /* 是否取消更新 */  
    public boolean cancelUpdate = false;  
  
    private Context mContext;  
    /* 更新进度条 */  
    private ProgressBar mProgress;  
    public MyAlertDialog mDownloadDialog;
    
    private String packageName="";
    private String versionUrl="";
    public String downApkUrl="";//下载的url
    public String newVersionName="";//新版本的名称
    public String versionExplain="";//更新说明

    public boolean IsMustUpdate=false;//是否需要强制更新
    public boolean ThirdDownload=false;//第三方下载
    
//    private MyApplication application;
  
    private Handler mHandler = new Handler()  
    {  
        public void handleMessage(Message msg)  
        {  
        	switch (msg.what)  
            {  
            // 正在下载  
            case DOWNLOAD:  
                // 设置进度条位置  
                mProgress.setProgress(progress);  

                break;  
            
            case DOWNLOAD_FINISH: 
            	Bundle bundle=msg.getData(); 
            	/*if(application!=null && application.getManagers()!=null){//通知游戏下载完成
            		if(application.getManagers().containsKey(bundle.getString("url")))
            			application.getManagers().remove(bundle.getString("url"));
            	}*/
            	// 安装文件  
                installApk(bundle.getString("name"));  
                break;  
            
            case DOWNLOAD_GAME:  
            	bundle=msg.getData();
            	//((Button)msg.obj).setText(progress + "%");
//            	if(application!=null){//通知游戏
//            		/*UserGameDetailActivity detail=application.getUserGameDetailActivity();
//            		if(detail!=null){
//            			detail.setProgress(bundle.getString("url"), progress + "%");
//            			//return;
//            		}
//            		UserGameActivity userGame=application.getUserGameActivity();
//            		if(userGame!=null){
//            			userGame.setProgress(bundle.getString("url"), progress + "%");
//            		}*/
//            	}
                break;  
           
            case DOWNLOAD_STOP:
            	bundle=msg.getData();
//            	if(application!=null){//通知游戏
//            		/*UserGameDetailActivity detail=application.getUserGameDetailActivity();
//            		if(detail!=null){
//            			detail.setProgress(bundle.getString("url"), "下载");
//            			//return;
//            		}
//            		UserGameActivity userGame=application.getUserGameActivity();
//            		if(userGame!=null){
//            			userGame.setProgress(bundle.getString("url"), "下载");
//            		}*/
//            	}
            	cancelUpdate=false;
            	/*if (msg.obj != null) {
					((Button)msg.obj).setText("下载");
					cancelUpdate = false;
				}*/
            	
            	break;
            default:  
                break;  
            }  
        };  
    };  
  
    public UpdateManager(Context context,Application application)  
    {  
        this.mContext = context;  
//        this.application=(MyApplication) application;
    }  
    
    public UpdateManager(Context context)  
    {  
        this.mContext = context;  
    }  
  
    /** 
     * 检测软件更新 
     */  
    public void checkUpdate()  
    {  
        if (isUpdate())  
        {  
            // 显示提示对话框  
            showNoticeDialog();  
        } else  
        {  
            Toast.makeText(mContext, "当前版本为最新版本", Toast.LENGTH_LONG).show();  
        }  
    }  
  
    /** 
     * 检查软件是否有更新版本 
     *  
     * @return 
     */  
    public boolean isUpdate()  
    {  
        // 获取当前软件版本  
        int versionCode = AGAppControl.getPackageCode(mContext); //getVersionCode(mContext);
        // 把version.xml放到网络上，然后获取文件信息  
        //InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");  
        // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析  
        ParseXmlService service = new ParseXmlService();
        try  
        {  
            URL url = new URL(versionUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setReadTimeout(10*1000);  
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();  
            mHashMap = service.parseXml(inStream);  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        	//return false;
        }  
        if (null != mHashMap)  
        {  
            int serviceCode = Integer.valueOf(mHashMap.get("version"));  
            // 版本判断  
            System.out.println(String.format("当前版本:%d;更新版本:%d;", versionCode,serviceCode));
            if (serviceCode > versionCode)  
            {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 获取软件版本号 
     *  
     * @param context 
     * @return 
     */  
    private int getVersionCode(Context context)  
    {  
        int versionCode = 0;  
        try  
        {  
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode  
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;  
        } catch (NameNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        return versionCode;  
    }  
	
	private ILaterUpdate laterUpdate;
	public void setILaterUpdate(ILaterUpdate laterUpdate){
		this.laterUpdate=laterUpdate;
	}
    
    /** 
     * 显示软件更新对话框 
     */  
    public void showNoticeDialog()  
    {
    	final UpdateDialog builder = new UpdateDialog(mContext);  
        //builder.setTitle(R.string.soft_update_title);
        //builder.setMessage("检测到新版本");
        builder.setCancelable(false);
        builder.setUpdateContent(versionExplain);

        builder.setPositiveButton("更新", new View.OnClickListener()  
        {
			@Override
			public void onClick(View v) {
				//builder.dismiss();
                // 显示下载对话框  
				cancelUpdate=false;
                showDownloadDialog();
			}
        });
        builder.setNegativeButton("稍后更新", new View.OnClickListener()  
        {
			@Override
			public void onClick(View v) {
				
				if(laterUpdate!=null){
					laterUpdate.onLaterUpdate();
				}
				
				builder.dismiss();
                if(IsMustUpdate){
                	CommonUtil.clearMemory();
                	return;
                }
			}  
        });  
    }  
    
    /**
     * 稍后更新接口
     * @author JohnZhan
     *
     */
    public interface ILaterUpdate{
    	public void onLaterUpdate();
    }
    
    
    private View.OnClickListener cancelListener;
    public void SetCancelOnclick(View.OnClickListener listener){
    	this.cancelListener=listener;
    }
    
    public void CancelDownload(){
    	mDownloadDialog.dismiss();
        cancelUpdate = true;
        if(IsMustUpdate){
        	CommonUtil.clearMemory();
        }
    }
  
    /** 
     * 显示软件下载对话框 
     */
    public void showDownloadDialog() {
        mDownloadDialog = new MyAlertDialog(mContext);
        mProgress = mDownloadDialog.getProgressBar();
        mDownloadDialog.showProgressBar();
        mDownloadDialog.setCancelable(false);
//        mDownloadDialog.setNegativeButton("取消", cancelListener);
        mDownloadDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadDialog.dismiss();
                cancelUpdate = true;
                if (IsMustUpdate) {
                    CommonUtil.clearMemory();
                    return;
                }
            }
        });
        downloadApk();
    }  
  
    /** 
     * 下载apk文件 
     */  
    private void downloadApk()  
    {  
        // 启动新线程下载软件  
    	if(!ThirdDownload){
    		new downloadApkThread().start();  
    	}else{
    		new downloadApkThread(mHashMap.get("url"),mHashMap.get("name")).start();  
    	}
    }  
    
    // 下载游戏
    public void downloadGame(String type, String url, String name)  
    {  
        // 启动新线程下载软游戏 
        new downloadApkThread(type, url, name).start();  
    }  
    
    public void DownloadApkByUrl(String url,String name){
    	mHashMap=new HashMap<String, String>();
    	mHashMap.put("url", url);
    	mHashMap.put("name", name);
    	ThirdDownload=true;
    	showDownloadDialog();
    }
  
    /** 
     * 下载文件线程 
     */  
    private class downloadApkThread extends Thread  {  
    	
    	String downloadUrl;
    	String name="";
    	String type;
    	
    	public downloadApkThread(String type, String downloadUrl, String name){
    		this.downloadUrl = downloadUrl;
    		this.type = type;
    		this.name = name;
    	}
    	
    	public downloadApkThread(){
    		name = "Goetui_"+newVersionName;
    		downloadUrl = downApkUrl;
    	}
    	
    	public downloadApkThread(String _url,String _name){
    		downloadUrl=_url;
    		name = _name;
    	}
    	
        @Override  
        public void run()  
        {  
            try  
            {  
                // 判断SD卡是否存在，并且是否具有读写权限  
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))  
                {  
                	
                	/*if (downloadUrl == null) {
                    	downloadUrl = mHashMap.get("url");
					}
                    if (name == null) {
                    	name = mHashMap.get("name");
					}*/
                	
                    // 获得存储卡的路径  
                    String sdpath = Environment.getExternalStorageDirectory() + "/";  
                    mSavePath = sdpath + "download";  
                    
                    URL url = new URL(downloadUrl);
                    // 创建连接  
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                    conn.setConnectTimeout(10 * 1000);
                    conn.setRequestMethod("GET");
                    //conn.setRequestProperty("Range", "bytes=" + downLength + "-" );
                    //conn.connect();  
                    // 获取文件大小  
                    int length = conn.getContentLength();  
                    // 创建输入流  
                    InputStream is = conn.getInputStream();  
  
                    File file = new File(mSavePath);  
                    // 判断文件目录是否存在  
                    if (!file.exists())  
                    {  
                        file.mkdir();  
                    }  
                    File apkFile = new File(mSavePath, name);  
                    FileOutputStream fos = new FileOutputStream(apkFile);  
                    int count = 0;  
                    // 缓存  
                    byte buf[] = new byte[1024];  
                    // 写入到文件中  
                    do  
                    {  
                        int numread = is.read(buf);  
                        count += numread;  
                        
                        // 游戏下载没有进度条，所以type为空的话则不是游戏下载
                        if (type == null) {
                        	 // 计算进度条位置  
                            progress = (int) (((float) count / length) * 100);  
                            // 更新进度  
                            mHandler.sendEmptyMessage(DOWNLOAD);  
						}else {
							progress = (int) (((float) count / length) * 100);  
							Message msg = new Message();
							msg.what = DOWNLOAD_GAME;	
                        	Bundle bundle=new Bundle();
                        	bundle.putString("name", name);
                        	bundle.putString("url", downloadUrl);
                        	msg.setData(bundle);
							mHandler.sendMessage(msg); 
						}

                        if (numread <= 0)  
                        {  
                            // 下载完成  
                        	Message msg = new Message();
                        	Bundle bundle=new Bundle();
                        	bundle.putString("name", name);
                        	bundle.putString("url", downloadUrl);
                        	msg.setData(bundle);
                        	msg.what = DOWNLOAD_FINISH;
                        	mHandler.sendMessage(msg); 
                            break;  
                        }  
                        // 写入文件  
                        fos.write(buf, 0, numread);  
                    } while (!cancelUpdate);// 点击取消就停止下载.  
                    
                    if (cancelUpdate) {
                    	Message msg = new Message();
                    	msg.what = DOWNLOAD_STOP;
                    	Bundle bundle=new Bundle();
                    	bundle.putString("name", name);
                    	bundle.putString("url", downloadUrl);
                    	msg.setData(bundle);
						mHandler.sendMessage(msg);
					}
                    
                    fos.close();  
                    is.close();  
                }  
            } catch (MalformedURLException e)  
            {  
                e.printStackTrace();  
            } catch (IOException e)  
            {  
                e.printStackTrace();  
            }finally{
            	if (mDownloadDialog != null) {
            		// 取消下载对话框显示  
                    mDownloadDialog.dismiss();
				}
            }
        }  
    };  
  
    /** 
     * 安装APK文件 
     */  
    private void installApk(String name)  
    {  
        File apkfile = new File(mSavePath, name);  
        if (!apkfile.exists())  
        {  
            return;  
        }  
        // 通过Intent安装APK文件  
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
        mContext.startActivity(i);  
    }
}
