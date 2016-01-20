package com.ag.common.js.bean;

import java.io.Serializable;
import java.util.List;


public class MapNaviResult implements Serializable{
    private List<MapNaviInfo> pos;

    public List<MapNaviInfo> getPos() {
        return pos;
    }

    public void setPos(List<MapNaviInfo> pos) {
        this.pos = pos;
    }
}
