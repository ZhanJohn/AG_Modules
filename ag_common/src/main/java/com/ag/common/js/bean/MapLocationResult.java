package com.ag.common.js.bean;

import java.io.Serializable;


public class MapLocationResult implements Serializable {

    /**
     *
     */
    private String ret;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     *坐标类型：0原始坐标  1百度地图坐标，暂时只支持1
     */
    private String type;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MapLocationResult(){}

    public MapLocationResult(String ret,String longitude,String latitude,String type){
        this.ret=ret;
        this.longitude=longitude;
        this.latitude=latitude;
        this.type=type;
    }

    public MapLocationResult(String longitude,String latitude){
        this.ret="0";
        this.type="1";
        this.longitude=longitude;
        this.latitude=latitude;
    }
}
