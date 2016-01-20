package com.ag.common.js.bean;

import java.io.Serializable;


public class MapNaviInfo implements Serializable{

    private String posType;//0,起始坐标（没有起始坐标的，默认起始为当前用户位置）; 1,终点坐标； 2，途经点
    private String posX; // 经度
    private String posY;// 纬度
    private String coordinateType; //0,原始的经纬度坐标;      1,百度地图中获取的坐标

    public String getPosType() {
        return posType;
    }

    public void setPosType(String posType) {
        this.posType = posType;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

}
