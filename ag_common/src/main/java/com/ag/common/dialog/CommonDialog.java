package com.ag.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ag.common.other.StringUtils;
import com.ag.common.res.AGResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 内容是一个list，带有标示选中状态
 *
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

	ListView listView;
	int activityIndex;
	int defaultID;
	int position;
	Context activityContext;
	LinearLayout layout_content;
	List<DialogEntity> list=new ArrayList<DialogEntity>();
	String productid;
	
	ICommonDialogResultListener dialogResultListener;
	
	private boolean isCard=false;
	private HashMap<String, Integer> map;
	private HashMap<String, Double> dmap;
	
	public CommonDialog(Context context,int theme,int activityIndex,int defaultID) {
		super(context,theme);
		this.activityIndex=activityIndex;
		activityContext=context;
		this.defaultID=defaultID;
	}
	
	public CommonDialog(Context context,int theme,int activityIndex,int defaultID,HashMap<String, Integer> map,HashMap<String, Double> dmap,boolean isCard) {
		super(context,theme);
		this.activityIndex=activityIndex;
		activityContext=context;
		this.defaultID=defaultID;
		this.map=map;
		this.dmap=dmap;
		this.isCard=isCard;
	}
	
	public void SetDialogListener(ICommonDialogResultListener resultListener){
		this.dialogResultListener = resultListener;
	}
	
	public void AddData(List<DialogEntity> dialogEntities){
		list.addAll(dialogEntities);
	}
	
	public void AddData(List<DialogEntity> dialogEntities,String productid){
		list.addAll(dialogEntities);
		this.productid = productid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(AGResource.getLayoutByName(activityContext, "common_dialog_layout"));
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		InitControlsAndBind();
	}
	
	private void InitControlsAndBind()
	{
		//listView=(ListView)findViewById(AGResource.getIdByName(this, "").dialog_listview);
		layout_content=(LinearLayout)findViewById(AGResource.getIdByName(activityContext, "layout_content"));
		/*DialogAdapter adapter=new DialogAdapter(activityContext);
		adapter.AddMoreData(list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);*/
		
		//DataBind
		LayoutInflater inflater = LayoutInflater.from(activityContext);
		for(DialogEntity data : list){
			
			++position;
			
			if(isCard){//易惠卡不显示项
				if(StringUtils.SafeInt(data.getId(), -1)!=defaultID){
					if(map!=null && map.containsKey(data.getId()) && map.get(data.getId())==0){
						continue;
					}
					if(data.getUsetype().equals("3") && dmap!=null && dmap.containsKey(data.getId()) && dmap.get(data.getId())==0){
						continue;
					}
				}
			}
			LinearLayout layout=(LinearLayout)inflater.inflate(AGResource.getLayoutByName(activityContext, "common_dialog_item"), null,false);
			RelativeLayout layoutdialog=(RelativeLayout)layout.findViewById(AGResource.getIdByName(activityContext, "layout_dialog"));
			TextView tvName=(TextView)layout.findViewById(AGResource.getIdByName(activityContext, "item_tv_name"));
			TextView tvLine=(TextView)layout.findViewById(AGResource.getIdByName(activityContext, "item_tv_line")); 
			ImageView img=(ImageView)layout.findViewById(AGResource.getIdByName(activityContext, "item_img"));
			ImageView right=(ImageView)layout.findViewById(AGResource.getIdByName(activityContext, "item_right"));
			
			tvName.setText(data.getValue());
			
			if(isCard){
				TextView tvInfo=(TextView) layout.findViewById(AGResource.getIdByName(activityContext, "item_tv_info"));
				tvInfo.setVisibility(View.VISIBLE);
				int useType=StringUtils.SafeInt(data.getUsetype(),0);
				switch(useType){
				case 1://折扣
					String info=StringUtils.subZeroString(data.getRebate())+"折";
					if("-1".equals(data.getFrequency()))//无限
						tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_green", "color")));
					else{
						info+=" 剩余"+ ((map!=null && map.containsKey(data.getId())) ? StringUtils.SafeString(map.get(data.getId())) : data.getFrequency())+"次";
						tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_pink", "color")));
					}
					tvInfo.setText(info);
					break;
				case 2://次卡
					tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_purple", "color")));
					if(!"-1".equals(data.getFrequency())){
						tvInfo.setText("剩余"+((map!=null && map.containsKey(data.getId())) ? StringUtils.SafeString(map.get(data.getId())) : data.getFrequency())+"次");
						tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_purple", "color")));
					}
					break;
					
				case 3://充值卡
				case 4://现金券
					if("-1".equals(data.getFrequency())){
						tvInfo.setText(StringUtils.subZeroString(((dmap!=null && dmap.containsKey(data.getId()))?dmap.get(data.getId()).toString():data.getMoney()))+"元");
						tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_blue", "color")));
					}
					else{
						tvInfo.setText(StringUtils.subZeroString(data.getMoney())+"元  剩余"+((map!=null && map.containsKey(data.getId())) ? StringUtils.SafeString(map.get(data.getId())) : data.getFrequency())+"次");
						tvInfo.setTextColor(activityContext.getResources().getColor(AGResource.getValueIntByName(activityContext, "preferential_orange", "color")));
					}
					break;
				default:
					tvInfo.setVisibility(View.GONE);
					break;
				}
			}
			
			/*if(defaultID<=0 && data.isChecked()){
				img.setVisibility(View.VISIBLE);
			}else if(defaultID>0 && defaultID==Integer.parseInt(data.getId())){
				img.setVisibility(View.VISIBLE);
			}*/
			
			if(defaultID==Integer.parseInt(data.getId())){
				img.setVisibility(View.VISIBLE);
			}
			
			if (list.size() == position) {
				Log.e("cd", "gone");
				tvLine.setVisibility(View.GONE);
			}else {
				Log.e("cd", "visible");
				tvLine.setVisibility(View.VISIBLE);
			}
			if(activityIndex==4||activityIndex==5){
				right.setVisibility(View.VISIBLE);
				img.setVisibility(View.GONE);
			}
			layoutdialog.setTag(data);
			layoutdialog.setOnClickListener(this);
			layout_content.addView(layout);
		}
		
		position = 0;
	}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		if(activityIndex==1){
//			DialogEntity data=(DialogEntity)arg1.getTag();
//			((ConfirmOrderActivity)activityContext).UpdateCustomerView(data,productid);
//		}else if(activityIndex==2){
//			DialogEntity data=(DialogEntity)arg1.getTag();
//			((CarAddActivity)activityContext).UpdateCustomerView(data);
//		}
//		else if(activityIndex==3)
//		{
//			DialogEntity data=(DialogEntity)arg1.getTag();
//			((UserInfoActivity)activityContext).UpdateCustomerView(data);
//		}
//		
//		dismiss();
//	}
	
//	class DialogAdapter extends BaseAdapter{
//
//		private LayoutInflater layoutInflater;
//		List<DialogEntity> adapterlist=new ArrayList<DialogEntity>();
//		
//		public DialogAdapter(Context context){
//			layoutInflater = LayoutInflater.from(context);
//		}
//		
//		public void AddMoreData(List<DialogEntity> data){
//			adapterlist.addAll(data);
//		}
//		
//		@Override
//		public int getCount() {
//			return adapterlist.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return adapterlist.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView==null){
//				convertView = layoutInflater.inflate(R.layout.common_dialog_item, parent,false);
//			}
//			TextView tvName=(TextView)convertView.findViewById(AGResource.getIdByName(this, "").item_tv_name);
//			ImageView img=(ImageView)convertView.findViewById(AGResource.getIdByName(this, "").item_img);
//			
//			DialogEntity data=adapterlist.get(position);
//			tvName.setText(data.getValue());
//			
//			
//			
//			if(data.isChecked()){
//				img.setVisibility(View.VISIBLE);
//			}
//			
//			convertView.setTag(data);
//			return convertView;
//		}
//	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == AGResource.getIdByName(activityContext, "layout_dialog")) {
			DialogEntity data=(DialogEntity)v.getTag();
			if(data!=null && dialogResultListener!=null){
				dialogResultListener.returnDialogResult(data);
			}
			/*if(activityIndex==1){
				DialogEntity data=(DialogEntity)v.getTag();
				((ConfirmOrderActivity)activityContext).UpdateCustomerView(data,productid);
			}else if(activityIndex==2){
				DialogEntity data=(DialogEntity)v.getTag();
//				((CarAddActivity)activityContext).UpdateCustomerView(data);
			}
			else if(activityIndex==3)
			{
				DialogEntity data=(DialogEntity)v.getTag();
//				((UserInfoActivity)activityContext).UpdateCustomerView(data);
			}else if(activityIndex==4)
			{
				DialogEntity data=(DialogEntity)v.getTag();
				//((DrawDetailFragment)activityContext).UpdateCustomerView(data);
			}else if(activityIndex==5)
			{
				DialogEntity data=(DialogEntity)v.getTag();
				((CollectActivity)activityContext).UpdateCustomerView(data);
			}*/
			dismiss();
		}
	}
}
