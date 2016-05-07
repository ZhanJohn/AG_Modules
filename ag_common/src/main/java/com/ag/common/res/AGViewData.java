package com.ag.common.res;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ag.common.other.StringUtils;
import com.ag.common.screen.ScreenUtils;

import java.util.List;

public class AGViewData {
	
	public interface IMenuResult{
		void turnItem(int position);
	}
	
	public static void setBottomMenu(final Context context,final LinearLayout parentView,int selectPos,List<XmlBottomMenu> list,final IMenuResult iMenuResult){
		int itemLayoutID=AGResource.getLayoutByName(context, "bottom_menu_item");
		setBottomMenu(context, parentView,itemLayoutID, selectPos, list, iMenuResult, false);
	}

	public static void setBottomMenu(final Context context,final LinearLayout parentView,int selectPos,List<XmlBottomMenu> list,final IMenuResult iMenuResult,final boolean isSameReturn){
		int itemLayoutID=AGResource.getLayoutByName(context, "bottom_menu_item");
		setBottomMenu(context, parentView,itemLayoutID, selectPos, list, iMenuResult, isSameReturn);
	}

	public static void setBottomMenu(final Context context,final LinearLayout parentView,int itemLayoutID,int selectPos,List<XmlBottomMenu> list,final IMenuResult iMenuResult,final boolean isSameReturn){
		if(parentView!=null){
			parentView.removeAllViews();
		}
		
		int itemWidth= ScreenUtils.getScreenWidth(context)/list.size();
		LayoutInflater inflater=LayoutInflater.from(context);
		//选中项
		parentView.setTag(selectPos);
		//
		for(XmlBottomMenu obj:list){
			RelativeLayout layout=(RelativeLayout)inflater.inflate(itemLayoutID, null);
			LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.width=itemWidth;
			layout.setLayoutParams(params);
			//
			ImageView item_img=(ImageView)layout.findViewById(AGResource.getIdByName(context, "item_img"));
			TextView item_tv_name=(TextView)layout.findViewById(AGResource.getIdByName(context, "item_tv_name"));
			if(item_img==null || item_tv_name==null){
				System.out.println("item_img is null");
				continue;
			}
			//
			if(obj.Type==selectPos){
				item_img.setImageResource(AGResource.getMipMapByName(context, obj.SelectImageRes));
				item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, obj.SelectTextColor, "color")));
			}else{
				item_img.setImageResource(AGResource.getMipMapByName(context, obj.ImageRes));
				item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, obj.TextColor, "color")));
			}
			
			item_tv_name.setText(obj.Title);
			layout.setTag(obj);
			//
			
			//
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					XmlBottomMenu menu=(XmlBottomMenu)v.getTag();
					if(menu==null)
						return;
					int select= StringUtils.SafeInt(parentView.getTag(), 1);
					if(select==menu.Type){
						if (isSameReturn && iMenuResult != null) {
							iMenuResult.turnItem(menu.Type);
						}
						return;
					}
					
					for(int i=0;i<parentView.getChildCount();i++){
						View view=parentView.getChildAt(i);
						XmlBottomMenu xbm=(XmlBottomMenu)view.getTag();
						ImageView item_img=(ImageView)view.findViewById(AGResource.getIdByName(context, "item_img"));
						TextView item_tv_name=(TextView)view.findViewById(AGResource.getIdByName(context, "item_tv_name"));
						item_img.setImageResource(AGResource.getMipMapByName(context, xbm.ImageRes));
						item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, xbm.TextColor, "color")));
					}
					
					//选中项
					parentView.setTag(menu.Type);
					//改变当前选中背景和文本颜色
					ImageView item_img=(ImageView)v.findViewById(AGResource.getIdByName(context, "item_img"));
					TextView item_tv_name=(TextView)v.findViewById(AGResource.getIdByName(context, "item_tv_name"));
					item_img.setImageResource(AGResource.getMipMapByName(context, menu.SelectImageRes));
					item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, menu.SelectTextColor, "color")));
					//
					if(iMenuResult!=null){
						iMenuResult.turnItem(menu.Type);
					}
				}
			});
			
			parentView.addView(layout);
		}
		
	}
	
	public static void setXmlSettingView(final Context context,final LinearLayout parentView,List<XmlSettingMenu> list,final IMenuResult iMenuResult){
		if(parentView!=null){
			parentView.removeAllViews();
		}
		LayoutInflater inflater=LayoutInflater.from(context);
		//
		for(int i=0;i<list.size();i++){
			XmlSettingMenu obj=list.get(i);
			if(obj.MarginTop>0 && !TextUtils.isEmpty(obj.MarginColor)){
				//构建一个TextView
				TextView textView=new TextView(context);
				textView.setBackgroundResource(AGResource.getValueIntByName(context, obj.MarginColor, "color"));
				LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(context, obj.MarginTop));
				textView.setLayoutParams(params);
				parentView.addView(textView);
			}
			//
			RelativeLayout layout=(RelativeLayout)inflater.inflate(AGResource.getLayoutByName(context, "setting_item"), null);
			TextView item_tv_name=(TextView)layout.findViewById(AGResource.getIdByName(context, "item_tv_name"));
			TextView item_tv_right=(TextView)layout.findViewById(AGResource.getIdByName(context, "item_tv_right"));
			ImageView img_red=(ImageView)layout.findViewById(AGResource.getIdByName(context, "img_red"));
			ImageView item_img_right=(ImageView)layout.findViewById(AGResource.getIdByName(context, "item_img_right"));
			if(item_tv_name==null || item_img_right==null){
				continue;
			}
			//
			item_tv_name.setText(obj.Title);
			if(obj.RedDot==1){
				img_red.setVisibility(View.VISIBLE);
			}else{
				img_red.setVisibility(View.GONE);
			}
			
			layout.setTag(obj.Type);
			
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int type=StringUtils.SafeInt(v.getTag(), 0);
					if(type==0)
						return;
					if(iMenuResult!=null){
						iMenuResult.turnItem(type);
					}
					//
				}
			});
			
			parentView.addView(layout);
			//
			if(obj.MarginBottom>0){
				TextView textView=new TextView(context);
				textView.setBackgroundResource(AGResource.getValueIntByName(context, obj.MarginColor, "color"));
				LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(context, obj.MarginBottom));
				textView.setLayoutParams(params);
				parentView.addView(textView);
			}
		}
		//
	}


	/**
	 * 设置BottomMenu项的选中与不选中的样式
	 * @param context
	 * @param layout_bottom_menu BottomMenu的父View
	 * @param pos 选中某一项
	 */
	public static void setItemViewStyle(Context context,LinearLayout layout_bottom_menu,int pos){
		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
			ImageView item_img = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img"));
			TextView item_tv_name = (TextView) item_layout.findViewById(AGResource.getIdByName(context, "item_tv_name"));
			XmlBottomMenu obj = (XmlBottomMenu) item_layout.getTag();
			if (pos == i + 1) {
				layout_bottom_menu.setTag(pos);
				//选中
				item_img.setImageResource(AGResource.getMipMapByName(context, obj.SelectImageRes));
				item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, obj.SelectTextColor, "color")));
			} else {
				//未选中
				item_img.setImageResource(AGResource.getMipMapByName(context, obj.ImageRes));
				item_tv_name.setTextColor(context.getResources().getColor(AGResource.getValueIntByName(context, obj.TextColor, "color")));
			}
		}
	}

	/**
	 * 清除某个项的红点
	 * @param context
	 * @param layout_bottom_menu
	 * @param pos
     */
	public static void clearItemRedDot(Context context,LinearLayout layout_bottom_menu,int pos){
		setItemRedDot(context,layout_bottom_menu,pos,View.GONE);
	}

	/**
	 * 清除所有红点
	 * @param context
	 * @param layout_bottom_menu
     */
	public static void clearAllItemRedDots(Context context,LinearLayout layout_bottom_menu){
		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
			ImageView item_img_red = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img_red"));
			item_img_red.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置某些项的红点显示
	 * @param context
	 * @param layout_bottom_menu
	 * @param pos
     */
	public static void setItemRedDot(Context context,LinearLayout layout_bottom_menu,int... pos){
		if(pos.length==0 || pos.length>5)
			return;

		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
			ImageView item_img_red = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img_red"));
			for(int kk:pos){
				if (kk == i + 1) {
					item_img_red.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * 设置某项的红点显示或隐藏
	 * @param context
	 * @param layout_bottom_menu
	 * @param pos
     * @param visible View.VISIBLE或View.GONE
     */
	public static void setItemRedDot(Context context,LinearLayout layout_bottom_menu,int pos,int visible){
		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
			ImageView item_img_red = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img_red"));
			if (pos == i + 1) {
				item_img_red.setVisibility(visible);
			}
		}
	}

	/**
	 * 设置某些项的文本
	 * @param context
	 * @param layout_bottom_menu
	 * @param pos
     * @param text
     */
	public static void setItemTextView(Context context,LinearLayout layout_bottom_menu,int pos,String text){
		if(TextUtils.isEmpty(text))
			return;
		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			if (pos == i + 1) {
				RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
				ImageView item_img=(ImageView)item_layout.findViewById(AGResource.getIdByName(context, "item_img"));
				TextView item_tv_name=(TextView)item_layout.findViewById(AGResource.getIdByName(context, "item_tv_name"));
				ImageView item_img_red = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img_red"));

				item_tv_name.setText(text);
			}
		}
	}

	/**
	 * 设置某些项的图片
	 * @param context
	 * @param layout_bottom_menu
	 * @param pos
     * @param imgRes
     */
	public static void setItemImageView(Context context,LinearLayout layout_bottom_menu,int pos,int imgRes){
		for (int i = 0; i < layout_bottom_menu.getChildCount(); i++) {
			if (pos == i + 1) {
				RelativeLayout item_layout = (RelativeLayout) layout_bottom_menu.getChildAt(i);
				ImageView item_img=(ImageView)item_layout.findViewById(AGResource.getIdByName(context, "item_img"));
				TextView item_tv_name=(TextView)item_layout.findViewById(AGResource.getIdByName(context, "item_tv_name"));
				ImageView item_img_red = (ImageView) item_layout.findViewById(AGResource.getIdByName(context, "item_img_red"));

				item_img.setImageResource(imgRes);
			}
		}
	}
	
}
