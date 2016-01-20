package com.ag.common.js.bean;

import java.io.Serializable;


public class PopLevel implements Serializable{
    private int level;
    private int isNeedFresh;//0不用刷新，1需要刷新

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsNeedFresh() {
        return isNeedFresh;
    }

    public void setIsNeedFresh(int isNeedFresh) {
        this.isNeedFresh = isNeedFresh;
    }

    public PopLevel(){}

    public PopLevel(int level,int isNeedFresh){
        this.level=level;
        this.isNeedFresh=isNeedFresh;
    }

}
