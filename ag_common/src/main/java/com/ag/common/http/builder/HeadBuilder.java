package com.ag.common.http.builder;


import com.ag.common.http.OkHttpUtils;
import com.ag.common.http.request.OtherRequest;
import com.ag.common.http.request.RequestCall;

public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
