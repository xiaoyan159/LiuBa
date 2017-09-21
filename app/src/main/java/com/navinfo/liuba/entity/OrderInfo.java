package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderInfo implements Serializable {

    /**
     * userId : 21
     * appointDate : 2017-09-19
     * appointTime : 11:11
     * appointDuration : 2.5
     * appointAddress : 塔克拉玛干
     * appointScope : 新疆
     * orderCost : 33
     */
    private int id;
    private int userId;
    private String appointDate;
    private String appointTime;
    private double appointDuration;
    private String appointAddress;
    private String appointScope;
    private int orderCost;
    private String location;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public double getAppointDuration() {
        return appointDuration;
    }

    public void setAppointDuration(double appointDuration) {
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

    public int getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(int orderCost) {
        this.orderCost = orderCost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
