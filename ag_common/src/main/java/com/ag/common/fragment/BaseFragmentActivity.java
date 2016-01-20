package com.ag.common.fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;


public class BaseFragmentActivity extends FragmentActivity {
	
	protected FragmentManager fMgr;
	private List<String> cacheFragments;
	private IFragmentUpdate iFragmentUpdate;
	private int fragmentId;
	private boolean cacheFragment;
	
	public void InitFragment(List<String> cacheFragments,int fragmentId,boolean cacheFragment,IFragmentUpdate iFragmentUpdate){
		this.cacheFragments=cacheFragments;
		//获取FragmentManager实例
		fMgr = getSupportFragmentManager();
		this.iFragmentUpdate=iFragmentUpdate;
		this.fragmentId=fragmentId;
		this.cacheFragment=cacheFragment;
	}
	
	public Drawable getDrawableByUser(int imgid){
		Resources res = getResources();
		Drawable img_off = res.getDrawable(imgid);
		//调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
		return img_off;
	}
	
	public void TurnFragment(Fragment fragment, String name, Bundle bundle){
		fragment.setArguments(bundle);
		SwitchFragment(fragment);
	}
	
	/**
	 * 隐藏Tab
	 * @param ft
	 */
	public void HideFragments(FragmentTransaction ft){
		for (String name : cacheFragments) {
			Fragment ff=fMgr.findFragmentByTag(name);
			if(ff!=null){
				ft.hide(ff);
				Log.i("HideFragments", ff.getClass().getSimpleName());
			}
		}
	}
	
	public void HideAllFragments(FragmentTransaction ft){
		if(fMgr.getBackStackEntryCount()==0)
			return;
		for (Fragment ff : fMgr.getFragments()) {
			if(ff!=null){
				ft.hide(ff);
				Log.i("HideAllFragments", ff.getClass().getSimpleName());
			}
		}
	}
	
	/**
	 * 检查当前Fragment是否存在和可见
	 * @param fragmentName
	 * @return
	 */
	public boolean GetFragmentVisible(String fragmentName){
		Fragment ff = fMgr.findFragmentByTag(fragmentName);
		if(ff!=null && ff.isVisible())
			return true;
		return false;
	}
	
	/**
	 * 检查当前Fragment是否已添加
	 * @param fragmentName
	 * @return
	 */
	public boolean GetFragmentAdd(String fragmentName){
		Fragment ff = fMgr.findFragmentByTag(fragmentName);
		if(ff!=null && ff.isAdded())
			return true;
		return false;
	}
	
	/**
	 * 跳转主页5个Tab
	 * @param fragmentName
	 * @param newFragment
	 */
	public void TurnTabFragment(String fragmentName,Fragment newFragment){
		if(GetFragmentVisible(fragmentName)){
			Log.i("SameTab", fragmentName);
			return;
		}
		
		Log.i("TurnTabFragment", "open="+fragmentName);
		popAllFragmentsExceptTheBottomOne(fragmentName);
		
		FragmentTransaction ft = fMgr.beginTransaction();
		HideFragments(ft);

		Fragment to = fMgr.findFragmentByTag(fragmentName);
		if (to==null || !to.isAdded()) {
			Log.i("TurnTabFragment", fragmentName+"=null");
			ft.add(fragmentId, newFragment, fragmentName)
			.addToBackStack(fragmentName).commitAllowingStateLoss();
//			if((zqFragmentEnum== ZQFragmentEnum.CompanyFragment && !ZQConfig.IsCacheCompanyTabs) ||
//					(zqFragmentEnum == ZQFragmentEnum.IndexFragment && !ZQConfig.IsCacheIndexTabs))
			if(!cacheFragment)
				fromFragment = newFragment;
		} else {
			Log.i("TurnTabFragment", fragmentName+" is show");
			ft.show(to).commitAllowingStateLoss();
//			if((zqFragmentEnum== ZQFragmentEnum.CompanyFragment && !ZQConfig.IsCacheCompanyTabs) ||
//					(zqFragmentEnum == ZQFragmentEnum.IndexFragment && !ZQConfig.IsCacheIndexTabs))
			if(!cacheFragment)
				fromFragment=to;
		}

//		if((zqFragmentEnum== ZQFragmentEnum.CompanyFragment && ZQConfig.IsCacheCompanyTabs) ||
//				(zqFragmentEnum == ZQFragmentEnum.IndexFragment && ZQConfig.IsCacheIndexTabs))
		if(cacheFragment)
			fromFragment=null;
	}
	
	Fragment fromFragment;
	
	public void SetFromFragment(Fragment ff){
		this.fromFragment=ff;
	}
	
	public void SwitchFragment(String toName){
		Fragment to=fMgr.findFragmentByTag(toName);
		SwitchFragment(to, toName);
	}
	
	public void SwitchFragment(Fragment to, String toName){
		if(iFragmentUpdate!=null)
			iFragmentUpdate.onUpdateBar();
		
		FragmentTransaction transaction = fMgr.beginTransaction();
//		HideFragments(transaction);
		HideAllFragments(transaction);

		/*if(fromFragment!=null && fromFragment!=to && !cacheFragments.contains(toName)){
			transaction.hide(fromFragment);
			Log.i("SwitchFragment-hide", fromFragment.getClass().getSimpleName());
		}*/
		
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.add(fragmentId, to,toName)
            .addToBackStack(toName)
            .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            Log.i("SwitchFragment", toName+" isAdded");
        } else {
            transaction.show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            Log.i("SwitchFragment",toName+ " show");
        }
        fromFragment=to;
	}
	
	/**
	 * Fragment切换，不会重新实例化
	 * @param to 跳转到的Fragment
	 */
	public void SwitchFragment(Fragment to) {
		SwitchFragment(to, to.getClass().getSimpleName());
    }
	
	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public void popAllFragmentsExceptTheBottomOne(String tabName) {
		if(fMgr==null || fMgr.getBackStackEntryCount()==0)
			return;
		
		for (int i = 0; i < fMgr.getBackStackEntryCount(); i++) {
			String name=fMgr.getBackStackEntryAt(i).getName();
			if(cacheFragments.contains(name))
				continue;
			if(!TextUtils.isEmpty(tabName) && tabName.equals(name))
				continue;
			
			Log.i("popBackStack", name);
			fMgr.popBackStackImmediate();//.popBackStack();
		}
	}
	
}
