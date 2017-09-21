package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderResponseEntity implements Serializable {

    /**
     * orderId : 1
     * userId : 1
     * creatDate : 2017-09-19 14:52:00
     * endDate : 2017-09-19 14:52:00
     * length : 1
     * isShit : 1
     * isPee : 1
     * status : 1
     * orderClerkId : 1
     * appointDate :
     * appointTime :
     * appointDuration :
     * appointAddress :
     * appointScope :
     * orderCost : 33.0
     */
    private int orderId;
    private int userId;
    private String creatDate;
    private String endDate;
    private int length;
    private int isShit;
    private int isPee;
    private int status;
    private int orderClerkId;
    private String appointDate;
    private String appointTime;
    private String appointDuration;
    private String appointAddress;
    private String appointScope;
    private double orderCost;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderClerkId() {
        return orderClerkId;
    }

    public void setOrderClerkId(int orderClerkId) {
        this.orderClerkId = orderClerkId;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getAppointDuration() {
        return appointDuration;
    }

    public void setAppointDuration(String appointDuration) {
        this.appointDuration = appointDuration;
    }

    public String getAppointAddress() {
        return appointAddress;
    }

    public void setAppointAddress(String appointAddress) {
        this.appointAddress = appointAddress;
    }

    public String getAppointScope() {
        return appointScope;
    }

    public void setAppointScope(String appointScope) {
        this.appointScope = appointScope;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }
}
