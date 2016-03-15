package com.ag.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ag.common.res.AGResource;

import java.util.ArrayList;
import java.util.List;


/**
 * 内容是一个list，带有标示选中状态
 *
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

	private int defaultID;
	private int position;
	private Context activityContext;
	private List<DialogEntity> list=new ArrayList<DialogEntity>();
	
	ICommonDialogResultListener dialogResultListener;
	
	public CommonDialog(Context context,int theme,int defaultID) {
		super(context,theme);
		activityContext=context;
		this.defaultID=defaultID;
	}
	
	public void setDialogListener(ICommonDialogResultListener resultListener){
		this.dialogResultListener = resultListener;
	}
	
	public void addData(List<DialogEntity> dialogEntities){
		list.addAll(dialogEntities);
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
		LinearLayout layout_content=(LinearLayout)findViewById(AGResource.getIdByName(activityContext, "layout_content"));
		
		//DataBind
		LayoutInflater inflater = LayoutInflater.from(activityContext);
		for(DialogEntity data : list){
			
			++position;

			LinearLayout layout=(LinearLayout)inflater.inflate(AGResource.getLayoutByName(activityContext, "common_dialog_item"), null,false);
			RelativeLayout layoutdialog=(RelativeLayout)layout.findViewById(AGResource.getIdByName(activityContext, "layout_dialog"));
			TextView tvName=(TextView)layout.findViewById(AGResource.getIdByName(activityContext, "item_tv_name"));
			TextView tvLine=(TextView)layout.findViewById(AGResource.getIdByName(activityContext, "item_tv_line")); 
			ImageView img=(ImageView)layout.findViewById(AGResource.getIdByName(activityContext, "item_img"));
			ImageView right=(ImageView)layout.findViewById(AGResource.getIdByName(activityContext, "item_right"));
			
			tvName.setText(data.getValue());

			
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
			layoutdialog.setTag(data);
			layoutdialog.setOnClickListener(this);
			layout_content.addView(layout);
		}
		
		position = 0;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == AGResource.getIdByName(activityContext, "layout_dialog")) {
			DialogEntity data=(DialogEntity)v.getTag();
			if(data!=null && dialogResultListener!=null){
				dialogResultListener.returnDialogResult(data);
			}
			dismiss();
		}
	}
}
