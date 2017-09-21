package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderByUserEntity implements Serializable {

    /**
     * userId : 21
     * ”orderClerkId” : 0
     */

    private int userId;
    private int orderClerkId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderClerkId() {
        return orderClerkId;
    }

    public void setOrderClerkId(int orderClerkId) {
        this.orderClerkId = orderClerkId;
    }
}
