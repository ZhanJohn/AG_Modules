package com.ag.common.webview.upload;

import com.ag.common.webview.bean.ProjectType;

import java.io.Serializable;


public class WebViewUploadParams implements Serializable{

    private String userId;
    private ProjectType projectType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public WebViewUploadParams(){}

    public WebViewUploadParams(String userId, ProjectType projectType){
        this.userId=userId;
        this.projectType=projectType;
    }

}
