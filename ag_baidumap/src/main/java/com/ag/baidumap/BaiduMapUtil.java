package com.ag.baidumap;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by zxr on 2015/8/24.
 */
public class BaiduMapUtil {

    private Context mActivity;
    private IMapLocationResult iMapLocationResult;

    public void getMapLocation(Context activity,IMapLocationResult iMapLocationResult){
        this.mActivity=activity;
        this.iMapLocationResult=iMapLocationResult;
        openLocation();
    }

    private boolean isFirst=true;
    private LocationClient mLocClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private void openLocation(){
        if(mLocClient==null){
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，bd09ll
            option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

            // 定位初始化
            mLocClient = new LocationClient(mActivity);
            mLocClient.registerLocationListener(new MyLocationListenner());
            mLocClient.setLocOption(option);
        }
        if(!mLocClient.isStarted())
            mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (mLocClient != null)
                mLocClient.stop();

            if(isFirst){
                isFirst=false;
                if(iMapLocationResult!=null){
                    iMapLocationResult.onMapLocationResult(location.getLongitude(),location.getLatitude());
                }
            }

        }
    }

    public interface IMapLocationResult{
        void onMapLocationResult(double lng, double lat);
    }

}
