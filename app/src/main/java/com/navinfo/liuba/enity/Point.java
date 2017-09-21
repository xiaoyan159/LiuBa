package com.navinfo.liuba.enity;

/**
 * @author CC
 * @version V1.0
 * @Date 2017/9/21 15:22
 * @Description: ${todo} (用一句话描述该文件做什么)
 */
public class Point {
    double longitude;
    double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getPoint() {
        return "POINT(" + latitude + " " + longitude + " )";
    }

}
