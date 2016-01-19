package com.ag.common.res;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.ag.common.other.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AGXml {

	public static List<XmlMenu> getXmlMenu(Context context,String xmlName){
		return getXmlMenu(context, xmlName, "DevInfor", "Menu");
	}
	
	public static List<XmlBottomMenu> getXmlBottomMenu(Context context,String xmlName){
		return getXmlBottomMenu(context, xmlName, "DevInfor", "Menu");
	}
	
	public static List<XmlSettingMenu> getXmlSettingMenu(Context context,String xmlName){
		return getXmlSettingMenu(context, xmlName, "DevInfor", "Menu");
	}
	
	public static List<XmlMenu> getXmlMenu(Context context,String xmlName,String rootName,String menuName){
		List<XmlMenu> infos=new ArrayList<XmlMenu>();
		
		List<Map<String, String>> map;
		try {
			map = getXmlListValue(AGAssets.getFromAssetsFoString(context, xmlName),rootName,menuName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(map==null || map.size()==0)
			return null;
		
		for(int i=0;i<map.size();i++){
			Map<String, String> tmpMap=map.get(i);
			XmlMenu info=new XmlMenu();
			if(tmpMap.containsKey("LinkUrl"))
				info.LinkUrl=tmpMap.get("LinkUrl");
			if(tmpMap.containsKey("TextColor"))
				info.TextColor=tmpMap.get("TextColor");
			if(tmpMap.containsKey("ImageRes"))
				info.ImageRes=tmpMap.get("ImageRes");
			if(tmpMap.containsKey("Title"))
				info.Title=tmpMap.get("Title");
			if(tmpMap.containsKey("Type"))
				info.Type= StringUtils.SafeInt(tmpMap.get("Type"),1);
			infos.add(info);
		}

		return infos;
	}
	
	public static List<XmlSettingMenu> getXmlSettingMenu(Context context,String xmlName,String rootName,String menuName){
		List<XmlSettingMenu> infos=new ArrayList<XmlSettingMenu>();
		
		List<Map<String, String>> map;
		try {
			map = getXmlListValue(AGAssets.getFromAssetsFoString(context, xmlName),rootName,menuName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(map==null || map.size()==0)
			return null;
		
		for(int i=0;i<map.size();i++){
			Map<String, String> tmpMap=map.get(i);
			XmlSettingMenu info = new XmlSettingMenu();
			info.TextColor = tmpMap.get("TextColor");
			info.RedDot = StringUtils.SafeInt(tmpMap.get("RedDot"),0);
			info.MarginColor = tmpMap.get("MarginColor");
			info.MarginTop = StringUtils.SafeFloat(tmpMap.get("MarginTop"),0);
			info.MarginBottom = StringUtils.SafeFloat(tmpMap.get("MarginBottom"),0);
			info.Title = tmpMap.get("Title");
			info.Type = StringUtils.SafeInt(tmpMap.get("Type"), 1);
			infos.add(info);
		}

		return infos;
	}
	
	public static List<XmlBottomMenu> getXmlBottomMenu(Context context,String xmlName,String rootName,String menuName){
		List<XmlBottomMenu> infos=new ArrayList<XmlBottomMenu>();
		
		List<Map<String, String>> map;
		try {
			map = getXmlListValue(AGAssets.getFromAssetsFoString(context, xmlName),rootName,menuName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(map==null || map.size()==0)
			return null;
		
		for(int i=0;i<map.size();i++){
			Map<String, String> tmpMap=map.get(i);
			XmlBottomMenu info = new XmlBottomMenu();
			info.TextColor = tmpMap.get("TextColor");
			info.SelectTextColor = tmpMap.get("SelectTextColor");
			info.ImageRes = tmpMap.get("ImageRes");
			info.SelectImageRes = tmpMap.get("SelectImageRes");
			info.Title = tmpMap.get("Title");
			info.Type = StringUtils.SafeInt(tmpMap.get("Type"), 1);
			infos.add(info);
		}

		return infos;
	}
	
	public static List<Map<String, String>> getXmlListValue(Context context,String xmlName,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
		String value= AGAssets.getFromAssetsFoString(context, xmlName);
		return getXmlListValue(value, docParentName, itemParentName);
	}
	
	public static List<Map<String, String>> getXmlListValue(String value,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
        
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
					Map<String, String> map=new HashMap<String, String>();
					for (int i = 0; i < parser.getAttributeCount(); i++) {
						map.put(parser.getAttributeName(i), parser.getAttributeValue(i));
					}
					list.add(map);
                }
                break;  
            case XmlPullParser.END_TAG:  
                
                break;  
            }  
            eventType = parser.next();  
        }
        return list;  
	}
	
	public static Map<String, String> getXmlValue(Context context,String xmlName,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
		String value= AGAssets.getFromAssetsFoString(context, xmlName);
		return getXmlValue(value, docParentName, itemParentName);
	}
	
	public static Map<String, String> getXmlValue(String value,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
		Map<String, String> map = new HashMap<String, String>();
        
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
						map.put(parser.getAttributeName(i), parser.getAttributeValue(i));
					}
                }
                break;  
            case XmlPullParser.END_TAG:  
                
                break;  
            }  
            eventType = parser.next();  
        }  
        return map;  
	}
	
	public static String getFromAssetsFoString(Context context, String fileName){ 
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
	
	public static List<String> getXmlList(String value,String docParentName,String itemParentName) throws XmlPullParserException, IOException{
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
	
}
