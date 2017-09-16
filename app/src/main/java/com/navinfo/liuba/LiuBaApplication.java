package com.navinfo.liuba;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by zhangdezhi1702 on 2017/9/16.
 */

public class LiuBaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图组件
        SDKInitializer.initialize(getApplicationContext());
        //极光IM的初始化服务
        JMessageClient.init(getApplicationContext(), false);
        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
