package com.navinfo.liuba.enity;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author CC
 * @version V1.0
 * @Date 2017/9/21 14:36
 * @Description: ${todo} (用一句话描述该文件做什么)
 */
public class TrackEnity {
    private int isShit;
    private int isPee;
    private String geometry;
    private int orderId = -1;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
}
