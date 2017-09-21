package com.navinfo.liuba.entity;


import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderFinishEntity implements Serializable{

    /**
     * orderId : 21
     * length : 200
     * ”isShit” : 1
     * ”isPee” : 1
     * ”orderCost” : 23.5
     */

    private int orderId;
    private int length;
    private int isShit;
    private int isPee;
    private double orderCost;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIsShit() {
        return isShit;
    }

    public void setIsShit(int isShit) {
        this.isShit = isShit;
    }

    public int getIsPee() {
        return isPee;
    }

    public void setIsPee(int isPee) {
        this.isPee = isPee;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }
}
