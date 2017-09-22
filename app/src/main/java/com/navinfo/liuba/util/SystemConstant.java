package com.navinfo.liuba.util;

/**
 * Created by zhangdezhi1702 on 2017/9/18.
 */

public class SystemConstant {
    public static final String rootPath = LiuBaApplication.rootPath + "/Liuba";
    public static final String IMESSAGE_USER_PREFIX = "Liuba";
    public static final String herderJpgDir = rootPath + "/avater/";
    public static final String herderJpgPath = herderJpgDir + "/avater.jpg";
    public static final String CONFIG_SPF = "CONFIG_SPF";//常规设置的sharedpreference文件名
    public static final String CONFIG_SPF_USERNAME = "CONFIG_SPF_USERNAME";//常规设置的sharedpreference文件中记录的上次登录的用户名
    public static final int CURRENT_LOCATION = 0x101;//用来记录当前位置的键
    public static final int RETURN_FROM_MAIN = 0x102;
    public static final int LOGIN_2_MAIN = 0x103;
    public static final int MAIN_2_REGISTER = 0x104;

    //配置URL
//    private static final String BASE_URL = "http://192.168.4.110:8199/liuba/";
    private static final String BASE_URL = "http://124.89.91.119:8181/liuba/";
    public static final String queryByPhone = BASE_URL + "/userInfo/queryByPhone/";//根据电话查询用户信息
    public static final String userInfoCreate = BASE_URL + "/userInfo/create/";//创建用户
    public static final String trackCreate = BASE_URL + "/trackPoint/create/";//新建轨迹
    public static final String oederCreate = BASE_URL + "/orderInfo/create/";//创建订单
    public static final String oederListByStatus = BASE_URL + "/orderInfo/listByStatus/";//根据订单状态查询订单数据
    public static final String oederListByUser = BASE_URL + "/orderInfo/listByUserIdOrOrderClerkId/";//根据客户id或者接单人id查询订单接口
    public static final String acceptOrder = BASE_URL + "/orderInfo/updateOrderStatus/";//遛狗师接单的接口
    public static final String startOrder = BASE_URL + "/orderInfo/updateOrderStatusByOrderId/";//遛狗师接单的接口
    public static final String orderTrackList = BASE_URL + "/trackPoint/listByOrderId/";//根据订单id查询轨迹点接口
    public static final String finishOrder = BASE_URL + "/orderInfo/finish/";//订单完成接口


    //intent中bundle对应的key
    public static final String BUNDLE_USER_INFO = "BUNDLE_USER_INFO";
    public static final String BUNDLE_ORDER_INFO = "BUNDLE_ORDER_INFO";
    public static final String BUNDLE_TRACK_LIST = "BUNDLE_TRACK_LIST";
    public static final String ORDER_ACTION = "ORDER_ACTION";

    //EventBus的what值
    public static final int EVENT_WHAT_REFRESH_ORDER_LIST = 0x201;
    public static final int EVENT_WHAT_START_TASK = 0x202;

    public static String getJPushUserName(String userId) {
        String jPushUserName = IMESSAGE_USER_PREFIX + userId;
        return jPushUserName;
    }

    public static String getJPushPWD(String userId) {
        String jPushUserName = IMESSAGE_USER_PREFIX + userId;
        return jPushUserName;
    }
}
