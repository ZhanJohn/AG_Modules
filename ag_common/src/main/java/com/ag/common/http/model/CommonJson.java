package com.ag.common.http.model;

import java.io.Serializable;

/**
 * 公共实体类，所有http response的实体类皆继承它
 * data必须是单个对象
 */
public class CommonJson<T> implements Serializable {

    private int code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
