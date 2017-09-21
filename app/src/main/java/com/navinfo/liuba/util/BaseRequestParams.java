package com.navinfo.liuba.util;

import org.xutils.http.RequestParams;

/**
 * Created by zhangdezhi1702 on 2017/9/19.
 */

public class BaseRequestParams extends RequestParams {
    private String paramerJson = null;
    private String url;

    public BaseRequestParams(String uri) {
        super(uri);
        this.url = uri;
    }

    public void setParamerJson(String paramerJson) {
        this.addQueryStringParameter("parameter", paramerJson);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
