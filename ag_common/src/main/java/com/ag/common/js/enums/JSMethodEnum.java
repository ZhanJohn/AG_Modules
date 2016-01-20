package com.ag.common.js.enums;


public enum JSMethodEnum {

    /**
     * 返回方法
     */
    Back("back"),

    /**
     * 分享方法
     */
    Share("share"),

    /**
     * 设置方法
     */
    Setting("setting"),

    /**
     * 扫码方法
     */
    Scan("scancode"),

    /**
     * 查找方法
     */
    Search("search");

    private final String value;

    private JSMethodEnum(String value){
        this.value=value;
    }

    public String getMethodValue(){
        return value;
    }


}
