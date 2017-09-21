package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/21.
 */

public class OrderTrackEntity implements Serializable {

    /**
     * id : 1
     * orderId : 1
     * creatDate : 2017-09-19 14:52:00
     * geometry : POINT (116.27354 39.91491)
     * photo : null
     * isShit : 1
     * isPee : 1
     */

    private int id;
    private int orderId;
    private String creatDate;
    private String geometry;
    private Object photo;
    private int isShit;
    private int isPee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
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
}
