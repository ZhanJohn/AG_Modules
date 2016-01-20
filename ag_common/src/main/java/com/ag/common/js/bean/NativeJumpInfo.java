package com.ag.common.js.bean;

import java.io.Serializable;


public class NativeJumpInfo implements Serializable{
    private String uiid;
    private String isKeepCurrentNaviStack;

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public String getIsKeepCurrentNaviStack() {
        return isKeepCurrentNaviStack;
    }

    public void setIsKeepCurrentNaviStack(String isKeepCurrentNaviStack) {
        this.isKeepCurrentNaviStack = isKeepCurrentNaviStack;
    }
}
