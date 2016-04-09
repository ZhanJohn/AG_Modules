package com.ag.common.createview;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ag.common.R;
import com.ag.common.js.enums.JSMethodEnum;
import com.ag.common.other.KeyboardUtils;
import com.ag.common.other.StringUtils;
import com.ag.common.res.AGResource;
import com.ag.common.screen.ScreenUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AGViewBarCreate {

	private static AGViewBarCreate control=new AGViewBarCreate();

	private AGViewBarCreate(){}

	public static AGViewBarCreate getInstance(){
		return control;
	}

	public void setCustomerBarView(final Context context,CustomerBarResult barResult,RelativeLayout layout_top,boolean bottomLine,View.OnClickListener clickListener,final ISearchListener searchListener){
		if(barResult==null || barResult.getWndCfg()==null)
			return;

		int ten= ScreenUtils.dip2px(context, 10);
		int four=ScreenUtils.dip2px(context,49);
		boolean parentLeft=false,parentRight=false;//最左和最右默认值为false
		int leftId=0x7f990010,rightId=0x7f990020;


		if(barResult.getWndCfg().getController()!=null && barResult.getWndCfg().getController().size()==1 &&
				barResult.getWndCfg().getController().get(0).getType().equals("button")){
			layout_top.setVisibility(View.GONE);
			RelativeLayout view=(RelativeLayout)layout_top.getParent();
			ImageView img=new ImageView(context);

			img.setPadding(ten,ten,ten,ten);

			MarginLayoutParams marginLayoutParams=new MarginLayoutParams(four, four);
			LayoutParams params=new LayoutParams(marginLayoutParams);

			img.setLayoutParams(params);

			CustomerBarController controller=barResult.getWndCfg().getController().get(0);
			CustomerBarBG bg=controller.getBgImg();
			if(bg!=null && !TextUtils.isEmpty(bg.getNormal())){
				img.setImageResource(AGResource.getMipMapByName(context, bg.getNormal()));
//					img.setImageResource(R.drawable.icon_previous);
			}

			view.addView(img);

			if(clickListener!=null){
				img.setTag(controller.getAction());
				img.setOnClickListener(clickListener);
			}

			return;
		}

		for(CustomerBarController controller:barResult.getWndCfg().getController()){
			String type=controller.getType();
			if(type.equals("button")){
				//Skip
				if(controller.getBgImg()==null || TextUtils.isEmpty(controller.getBgImg().getNormal())){
					continue;
				}

				ImageView img=new ImageView(context);

				img.setPadding(ten,ten,ten,ten);

				//根据marginleft或marginright设置
				double marginLeft= StringUtils.SafeDouble(controller.getRect().getMarginLeft(), 0);
				double marginRight=StringUtils.SafeDouble(controller.getRect().getMarginRight(), 0);
				MarginLayoutParams marginLayoutParams=new MarginLayoutParams(four, four);
				LayoutParams params=new LayoutParams(marginLayoutParams);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				//左对齐
				if(marginLeft>0){
					if(!parentLeft){
						img.setId(leftId);
						params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						parentLeft=true;
					}else{
						params.addRule(RelativeLayout.RIGHT_OF, leftId);
						img.setId(++leftId);
					}

					marginLayoutParams.setMargins(ScreenUtils.dip2px(context, (int)marginLeft), 0, 0, 0);
				}else if(marginRight>0){
					if(!parentRight){
						img.setId(rightId);
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						parentRight=true;
					}else{
						params.addRule(RelativeLayout.LEFT_OF, rightId);
						img.setId(++rightId);
					}

					marginLayoutParams.setMargins(0, 0, ScreenUtils.dip2px(context, (int)marginRight), 0);
				}

				img.setLayoutParams(params);

				CustomerBarBG bg=controller.getBgImg();
				if(bg!=null && !TextUtils.isEmpty(bg.getNormal())){
					img.setImageResource(AGResource.getMipMapByName(context, bg.getNormal()));
//					img.setImageResource(R.drawable.icon_previous);
				}

				layout_top.addView(img);

				if(clickListener!=null){
					img.setTag(controller.getAction());
					img.setOnClickListener(clickListener);
				}
			}else if(type.equals("label")){
				//Skip
//				if(TextUtils.isEmpty(controller.getText())){
//					continue;
//				}

				TextView layout_tv_title=new TextView(context);

				String iscenter=(controller.getRect()==null || TextUtils.isEmpty(controller.getRect().getIsCenter()))?"1":controller.getRect().getIsCenter();
				if(iscenter.equals("1")){
					MarginLayoutParams marginLayoutParams=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					marginLayoutParams.leftMargin=four;
					marginLayoutParams.rightMargin=four;
					LayoutParams params=new LayoutParams(marginLayoutParams);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					layout_tv_title.setLayoutParams(params);
				}else{
					//根据marginleft或marginright设置
					double marginLeft=StringUtils.SafeDouble(controller.getRect().getMarginLeft(), 0);
					double marginRight=StringUtils.SafeDouble(controller.getRect().getMarginRight(), 0);
					MarginLayoutParams marginLayoutParams=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					LayoutParams params=new LayoutParams(marginLayoutParams);
					//
					if(marginLeft>0){
						params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						marginLayoutParams.setMargins((int)marginLeft, 0, 0, 0);
					}else if(marginRight>0){
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						marginLayoutParams.setMargins(0, 0, (int)marginRight, 0);
					}
					layout_tv_title.setLayoutParams(params);
				}

				layout_tv_title.setTextColor(Color.parseColor(controller.getTextColor().replace("0x", "#").replace("0X", "#")));
				layout_tv_title.setTextSize(StringUtils.SafeFloat(controller.getTextFont(), 18.0f));
//				layout_tv_title.setSingleLine(true);
				layout_tv_title.setLines(1);
				layout_tv_title.setEllipsize(TextUtils.TruncateAt.END);
				layout_tv_title.setText(StringUtils.SafeString(controller.getText()));

				layout_top.addView(layout_tv_title);
			}else if(type.equals("searchbar")){
				//searchbar
				RelativeLayout searchLayout=(RelativeLayout)LayoutInflater.from(context).inflate(AGResource.getLayoutByName(context,"search_edittext"), null);
				final EditText layout_et_search=(EditText)searchLayout.findViewById(AGResource.getIdByName(context,"layout_et_search"));
				final ImageView btn_clear=(ImageView)searchLayout.findViewById(AGResource.getIdByName(context,"btn_clear"));
				MarginLayoutParams marginLayoutParams=new MarginLayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(context, 40));
				marginLayoutParams.setMargins(four, 0, ten, 0);
				LayoutParams params=new LayoutParams(marginLayoutParams);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				searchLayout.setLayoutParams(params);

				InputFilter[] filters = {new InputFilter.LengthFilter(30)};
				layout_et_search.setFilters(filters);
				layout_et_search.setHint(StringUtils.SafeString(controller.getText()));
				layout_et_search.setTextColor(Color.parseColor(controller.getTextColor().replace("0x", "#").replace("0X", "#")));
				layout_et_search.setTextSize(StringUtils.SafeFloat(controller.getTextFont(), 18.0f));

				//Bind
				layout_et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
				btn_clear.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						layout_et_search.setText("");
						btn_clear.setVisibility(View.GONE);
					}
				});
				layout_et_search.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (s.length() > 0) {
							btn_clear.setVisibility(View.VISIBLE);
						} else {
							btn_clear.setVisibility(View.GONE);
						}
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
				layout_et_search.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_ENTER) {
							//Search Content
							KeyboardUtils.hideKeyboard(context, layout_et_search);
							if (searchListener != null) {
								searchListener.onSearchContent(layout_et_search.getText().toString().trim());
							}
						}
						return false;
					}
				});
				searchLayout.findViewById(AGResource.getIdByName(context,"layout_btn_search")).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Search Content
						KeyboardUtils.hideKeyboard(context, layout_et_search);
						if (searchListener != null) {
							searchListener.onSearchContent(layout_et_search.getText().toString().trim());
						}
					}
				});
				layout_top.addView(searchLayout);
				KeyboardUtils.showKeyboard(context, layout_et_search);
			}

		}

		if(bottomLine){
			//最后加上灰色线条
			TextView layout_tv_line=new TextView(context);
			layout_tv_line.setBackgroundColor(context.getResources().getColor(R.color.ccc));
			LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(context, 1));
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layout_tv_line.setLayoutParams(params);
			layout_top.addView(layout_tv_line);
		}

		layout_top.getLayoutParams().height=ScreenUtils.dip2px(context, 49);
	}

	public static CustomerBarResult getCustomerBarResult(Context context,String url,String leftImg,String leftFunc,String leftParam,String title,String rightImg,String rightFunc,String rightParam){
		return getCustomerBarResult(context,url,leftImg,leftFunc,leftParam,title,"#606060",rightImg,rightFunc,rightParam);
	}

	public static CustomerBarResult getCustomerBarResult(Context context,String url,String leftImg,String leftFunc,String leftParam,String title,String titleColor,String rightImg,String rightFunc,String rightParam){
		CustomerBarResult result=(new Gson()).fromJson(context.getString(R.string.app_topbar)
				.replace("{0}", url)
				.replace("{1}", StringUtils.SafeString(title))
				.replace("{11}", StringUtils.SafeString(leftImg))
				.replace("{12}", StringUtils.SafeString(leftFunc))
				.replace("{13}", StringUtils.SafeString(leftParam))
				.replace("{21}", StringUtils.SafeString(rightImg))
				.replace("{22}", StringUtils.SafeString(rightFunc))
				.replace("{23}", StringUtils.SafeString(rightParam))
				.replace("{TitleColor}",TextUtils.isEmpty(titleColor)?"#606060":titleColor), CustomerBarResult.class);
		return result;
	}

	public static CustomerBarController getCustomerBarController(Context context){
		return (new Gson()).fromJson(context.getString(R.string.app_topbar_backbutton)
				.replace("{11}","icon_fanhui")
				.replace("{12}", JSMethodEnum.Back.getMethodValue())
				.replace("{13}",""),CustomerBarController.class);
	}

	public static CustomerBarResult getCustomerBarResult(String url,String title){
		CustomerBarResult result=new CustomerBarResult();
		result.setUrl(url);
		//
		CustomerBarConfig config=new CustomerBarConfig();
		List<CustomerBarController> list=new ArrayList<CustomerBarController>();
		CustomerBarController controller=new CustomerBarController();
		controller.setBgImg(new CustomerBarBG("icon_fanhui", null));
		controller.setRect(new CustomerBarRect("0", null, "0.1", null, null, null));
		list.add(controller);
		//
		controller=new CustomerBarController();
		controller.setText(title);
		controller.setTextColor("#000000");
		controller.setTextFont("18");
		controller.setRect(new CustomerBarRect("1", null, null, null, null, null));
		list.add(controller);

		config.setController(list);
		result.setWndCfg(config);
		return result;
	}


}
