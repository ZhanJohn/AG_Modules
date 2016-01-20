package com.ag.common.webview.sqlite;

import android.content.Context;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheDao {

	private AGOrmliteHelper helper=null;

	public CacheDao(Context context){
		helper= AGOrmliteHelper.getHelper(context);
	}

	public boolean Add(AGCacheInfo data) {
		AGCacheInfo tmpInfo=GetModel(data.getStartUrl());
		if(tmpInfo!=null){
			try {
				helper.getCacheDao().deleteById(tmpInfo.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		int count=0;
		try {
			count=helper.getCacheDao().create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("添加数据="+count);
		return count>0?true:false;
	}

	public AGCacheInfo GetModel(String startUrl) {
		Map<String, Object> map=new HashMap<String, Object>();
		System.out.println("请求参数="+startUrl);
		map.put("starturl", startUrl);
		AGCacheInfo info=null;
		try {
			List<AGCacheInfo> list=helper.getCacheDao().queryForFieldValues(map);
			if(list!=null && list.size()>0){
				for(AGCacheInfo zc:list){
					System.out.println("item="+zc.getStartUrl()+"--"+zc.getRedirectUrl());
				}
				info=list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

}
