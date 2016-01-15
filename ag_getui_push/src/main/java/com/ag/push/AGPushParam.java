package com.ag.push;

import java.io.Serializable;

/**
 * Getui push bind params
 */
public class AGPushParam implements Serializable{

    private int sysId;
    private String userName="";
    private String nickName="";
    private String md5Key="";

    public AGPushParam(){}

    /**
     * nickName=userName
     * @param sysId
     * @param userName
     * @param md5Key
     */
    public AGPushParam(int sysId, String userName, String md5Key){
        this.sysId=sysId;
        this.userName=userName;
        this.nickName=userName;
        this.md5Key=md5Key;
    }

    /**
     * nickName=userName
     * @param sysId 平台Id
     * @param userName 登录账号
     * @param nickName 用户昵称
     * @param md5Key 请求加密key
     */
    public AGPushParam(int sysId, String userName, String nickName, String md5Key){
        this.sysId=sysId;
        this.userName=userName;
        this.nickName=nickName;
        this.md5Key=md5Key;
    }

    public int getSysId() {
        return sysId;
    }

    /**
     * 平台Id
     * @param sysId
     */
    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMd5Key() {
        return md5Key;
    }

    /**
     * md5Key
     * @param md5Key
     */
    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }
}
