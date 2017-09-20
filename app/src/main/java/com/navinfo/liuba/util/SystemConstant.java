package com.navinfo.liuba.util;

/**
 * Created by zhangdezhi1702 on 2017/9/18.
 */

public class SystemConstant {
    public static final String rootPath = LiuBaApplication.rootPath + "/Liuba";
    public static final String herderJpgDir = rootPath + "/avater/";
    public static final String herderJpgPath = herderJpgDir + "/avater.jpg";
    public static final String CONFIG_SPF = "CONFIG_SPF";//常规设置的sharedpreference文件名
    public static final String CONFIG_SPF_USERNAME = "CONFIG_SPF_USERNAME";//常规设置的sharedpreference文件中记录的上次登录的用户名
    public static final String CURRENT_LOCATION = "CURRENT_LOCATION";//用来记录当前位置的键

    //配置URL
    private static final String BASE_URL="http://192.168.4.110:8199/liuba/";
    public static final String queryByPhone=BASE_URL+"/userInfo/queryByPhone/";
    public static final String userInfoCreate=BASE_URL+"/userInfo/create/";
}
