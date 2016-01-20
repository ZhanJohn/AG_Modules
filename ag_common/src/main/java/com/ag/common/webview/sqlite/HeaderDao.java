package com.ag.common.webview.sqlite;

import android.content.Context;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderDao {

	private AGOrmliteHelper helper=null;

	public HeaderDao(Context context){
		helper= AGOrmliteHelper.getHelper(context);
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public boolean AddWebViewCache(AGHeaderInfo data) {
		AGHeaderInfo tmpInfo=GetWebViewModel(data.getRequesturl());
		if(tmpInfo!=null){
			try {
				helper.getHeaderDao().deleteById(tmpInfo.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		int count=0;
		try {
			count=helper.getHeaderDao().create(data);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("添加数据="+count);
		return count>0?true:false;
	}

	/**
	 *
	 * @param requestUrl
	 * @return
	 */
	public AGHeaderInfo GetWebViewModel(String requestUrl) {
		Map<String, Object> map=new HashMap<String, Object>();
		System.out.println("请求参数="+requestUrl);
		map.put("requesturl", requestUrl);
		AGHeaderInfo info=null;
		try {
			List<AGHeaderInfo> list=helper.getHeaderDao().queryForFieldValues(map);
			if(list!=null && list.size()>0){
				for(AGHeaderInfo zc:list){
					System.out.println("item="+zc.getRequesturl()+"--"+zc.getCachecontrol());
				}
				info=list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return info;
	}

}
