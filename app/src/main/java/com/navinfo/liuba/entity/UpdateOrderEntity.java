package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class UpdateOrderEntity implements Serializable{

    /**
     * orderId : 21
     * orderClerkId : 22
     */

    private int orderId;
    private int orderClerkId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderClerkId() {
        return orderClerkId;
    }

    public void setOrderClerkId(int orderClerkId) {
        this.orderClerkId = orderClerkId;
    }
}
