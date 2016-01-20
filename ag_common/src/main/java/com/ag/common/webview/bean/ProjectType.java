package com.ag.common.webview.bean;


public enum ProjectType {

    One(1);

    private final int type;

    private ProjectType(int type) {
        this.type = type;
    }

    public int GetEnumValue() {
        return type;
    }

}
