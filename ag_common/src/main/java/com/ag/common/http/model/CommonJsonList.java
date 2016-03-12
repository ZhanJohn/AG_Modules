package com.ag.common.http.model;

import java.io.Serializable;
import java.util.List;

/**
 * 公共实体类，所有http response的实体类皆继承它
 * data必须是列表
 */
public class CommonJsonList<T> implements Serializable {

    private int code;
    private String message;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
