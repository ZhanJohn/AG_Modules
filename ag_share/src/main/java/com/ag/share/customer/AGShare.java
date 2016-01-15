package com.ag.share.customer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.ag.share.OnekeyShare;
import com.ag.share.ReflectableShareContentCustomizeCallback;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import static com.mob.tools.utils.R.getBitmapRes;


public class AGShare {
	
	private final static String TAG = "AGShare";
	
	private static String getFromAssetsFoString(Context context, String fileName){
		System.out.println(fileName);
		try { 
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) ); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return null;
	}
	
	private static List<String> getXmlValue(String value,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
		if(TextUtils.isEmpty(value))
			return null;
		
		List<String> values=new ArrayList<String>();
        
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例  
        parser.setInput(new StringReader(value));
  
        int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType) {  
            case XmlPullParser.START_DOCUMENT:  
                
                break;  
            case XmlPullParser.START_TAG:  
            	
            	if (parser.getName().equals(docParentName)) {
            		eventType = parser.next(); 
				}else if (parser.getName().equals(itemParentName)) {
					for (int i = 0; i < parser.getAttributeCount(); i++) {
						Log.i("getXml", "xml="+parser.getAttributeValue(i));
						values.add(parser.getAttributeValue(i));
					}
                }
                break;  
            case XmlPullParser.END_TAG:  
                
                break;  
            }  
            eventType = parser.next();  
        }  
        return values;  
	}
	
	private static Platform[] GetSupportPlatform(Context context){
		List<String> platforms=null;
		try {
			platforms=getXmlValue(getFromAssetsFoString(context, "SharePlatform.xml"),"DevInfor","Platform");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(platforms!=null && platforms.size()>0){
			Platform[] plats=new Platform[platforms.size()];
			for(int i=0;i<platforms.size();i++){
				plats[i] = ShareSDK.getPlatform(platforms.get(i));
			}
			return plats;
		}
		return null;
	}

	public static void showOnekeyshare(ShareInfo info) {
		
		OnekeyShare oks = new OnekeyShare();
		oks.setTheme(info.getShareTheme());

		Platform[] plats = GetSupportPlatform(info.getContext());
		if(plats!=null){
			oks.setPlatforms(plats);
		}
		
		if(plats==null || plats.length==0)
			return;

		// 分享时Notification的图标和文字
//		oks.setNotification(info.getIconId(),info.getAppName());

		// address是接收人地址，仅在信息和邮件使用
//		oks.setAddress("12345678901");

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		if(!TextUtils.isEmpty(info.getShareTitle()))
			oks.setTitle(info.getShareTitle());

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		if(!TextUtils.isEmpty(info.getUrl()))
			oks.setTitleUrl(info.getUrl());

		// text是分享文本，所有平台都需要这个字段
		if(!TextUtils.isEmpty(info.getContent()))
			oks.setText(info.getContent());

		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// 网络图片优先于本地图片
		oks.setImagePath(info.getImgPath());

		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
		// 微信的两个平台、Linked-In支持此字段
		oks.setImageUrl(info.getImgUrl());

		// url仅在微信（包括好友和朋友圈）中使用
		if(!TextUtils.isEmpty(info.getUrl()))
			oks.setUrl(info.getUrl());

		// appPath是待分享应用程序的本地路劲，仅在微信中使用
		//        oks.setAppPath(MainActivity.TEST_IMAGE);

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(null);

		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(info.getAppName());

		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		if(!TextUtils.isEmpty(info.getWebUrl()))
			oks.setSiteUrl(info.getWebUrl());

		// venueName是分享社区名称，仅在Foursquare使用
		//        oks.setVenueName("Southeast in China");

		// venueDescription是分享社区描述，仅在Foursquare使用
		//        oks.setVenueDescription("This is a beautiful place!");

		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
		//        oks.setLatitude(23.122619f);

		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
		//        oks.setLongitude(113.372338f);

		// 是否直接分享（true则直接分享）
		oks.setSilent(info.isSilent());

		if(info.getPlatforms()!=null){
			for (CustomerPlatform cp : info.getPlatforms()) {
				Bitmap logo = BitmapFactory.decodeResource(info.getContext().getResources(), getBitmapRes(info.getContext(), cp.getCustomerLogo()));
				oks.setCustomerLogo(logo,logo, cp.getCustomerName(), cp.getCustomerListener());
			}
		}

		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		if (info.getPlatform() != null) {
			oks.setPlatform(info.getPlatform());
		}
		
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();

		// 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
		// oks.setCallback(new OneKeyShareCallback());
		//通过OneKeyShareCallback来修改不同平台分享的内容
		oks.setShareContentCustomizeCallback(new ReflectableShareContentCustomizeCallback(info.isWechatTitleToContent()));

		oks.setHasBottomButton(info.isHasButton());
		oks.setHasTopTitle(info.isHasTopTitle());
		oks.SetOrientation(info.getOrientation());
		oks.show(info.getContext());
	}
}
