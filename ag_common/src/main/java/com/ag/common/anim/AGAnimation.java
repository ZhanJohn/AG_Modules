package com.ag.common.anim;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 动画效果工具类
 *
 */
public class AGAnimation {
	/**
     * 定义从右侧进入的动画效果
     * @return
     */
    public static Animation inFromRightAnimation()
    {
    	Animation inFromRight = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, +1.0f,
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f);
    	inFromRight.setDuration(500);
    	inFromRight.setInterpolator(new AccelerateInterpolator());
    	return inFromRight;
    }
    /**
     * 定义从左侧退出的动画效果
     * @return
     */
    public static Animation outToLeftAnimation()
    {
    	Animation outtoLeft = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, 0.0f,
    			Animation.RELATIVE_TO_PARENT, -1.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f);
    	outtoLeft.setDuration(500);
    	outtoLeft.setInterpolator(new AccelerateInterpolator());
    	return outtoLeft;
    }
    /**
     * 定义从左侧进入的动画效果
     * @return
     */
    public static Animation inFromLeftAnimation()
    {
    	Animation inFromLeft = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, -1.0f,
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f);
    	inFromLeft.setDuration(500);
    	inFromLeft.setInterpolator(new AccelerateInterpolator());
    	return inFromLeft;
    }
    /**
     * 定义从右侧退出时的动画效果
     * @return
     */
    public static Animation outToRightAnimation()
    {
    	Animation outtoRight = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, 0.0f,
    			Animation.RELATIVE_TO_PARENT, +1.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f, 
    			Animation.RELATIVE_TO_PARENT, 0.0f);
    	outtoRight.setDuration(500);
    	outtoRight.setInterpolator(new AccelerateInterpolator());
    	return outtoRight;
    }
    
    /**
     * 缩放动画 显示
     * @return
     */
    public static Animation showViewAnimation()
    {
    	Animation showAction = new ScaleAnimation(
    			1.0f, 1.0f, 
    			0.0f, 1.0f,
    			Animation.RELATIVE_TO_SELF, 0.0f,
    			Animation.RELATIVE_TO_SELF, 0.0f);
    	showAction.setDuration(500);
    	return showAction;
    }
    
    /**
     * 缩放动画 隐藏
     * @return
     */
    public static Animation hidentViewAnimation()
    {
    	Animation hideAction = new ScaleAnimation(
    			1.0f, 1.0f, 1.0f, 0.0f,
    			Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,
    			0.0f);
    	hideAction.setDuration(500);
    	return hideAction;
    }
    
    /**
     * 渐现
     * @return
     */
    public static Animation showAlphaAnimation(){
    	AlphaAnimation animation = new AlphaAnimation(0, 1); 
    	animation.setDuration(1000);//设置动画持续时间 
    	return animation;
    }
    
    /**
     * 渐隐
     * @return
     */
    public static Animation hidentAlphaAnimation(){
    	AlphaAnimation animation = new AlphaAnimation(1, 0); 
    	animation.setDuration(1000);//设置动画持续时间 
    	return animation;
    }
    
    public static void SetTopAndBottomAnimation(View view,int range,int time){
    	TranslateAnimation anim = new TranslateAnimation(0, 0, 0, range);  
    	anim.setDuration(time);  
    	anim.setRepeatCount(Animation.INFINITE);
    	anim.setRepeatMode(Animation.REVERSE);
		view.setAnimation(anim);  
		anim.start();  
    }
    
}
