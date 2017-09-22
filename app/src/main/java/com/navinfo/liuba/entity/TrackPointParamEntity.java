package com.navinfo.liuba.entity;

import com.navinfo.liuba.enity.TrackEnity;

import java.util.List;

/**
 * Created by zhangdezhi1702 on 2017/9/19.
 */

public class TrackPointParamEntity {
    private List<TrackEnity> data;

    public List<TrackEnity> getData() {
        return data;
    }

    public void setData(List<TrackEnity> data) {
        this.data = data;
    }
}
