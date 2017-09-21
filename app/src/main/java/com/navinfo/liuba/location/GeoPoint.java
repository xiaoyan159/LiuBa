package com.navinfo.liuba.location;

import java.io.Serializable;

/**
 * 地理坐标定义，包括使用double和int两种形式，int值为double乘以1E6取整
 * 
 * @author hb
 *
 */
public class GeoPoint implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5085279167301841838L;
    // 经度
    private double mdLon;
    // 纬度
    private double mdLat;

    public GeoPoint(double lon, double lat) {
        mdLon = lon;
        mdLat = lat;
    }

    public GeoPoint(int lonE6, int latE6) {
        mdLon = (lonE6) / 1E6;
        mdLat = (latE6) / 1E6;
    }

    public void setLon(double lon) {
        mdLon = lon;
    }

    public void setLat(double lat) {
        mdLat = lat;
    }

    public double getLon() {
        return mdLon;
    }

    public double getLat() {
        return mdLat;
    }

    public int getLonE6() {
        return (int) (mdLon * 1E6);
    }

    public int getLatE6() {
        return (int) (mdLat * 1E6);
    }

    public void writeToParcel(android.os.Parcel out, int arg1) {

    }

	@Override
	public String toString() {
		return "GeoPoint [mdLon=" + mdLon + ", mdLat=" + mdLat + "]";
	}
    
	@Override
	public boolean equals(Object o){
		// TODO Auto-generated method stub
		if(o==null)
			return false;
		if(o.getClass()!=getClass())
			return false;
		if((((GeoPoint)o).getLatE6() == this.getLatE6()) && (((GeoPoint)o).getLonE6() == this.getLonE6()) )
			return true;
		return false;
	}

	@Override
	public int hashCode(){
		// TODO Auto-generated method stub
		return toString().hashCode();
	}
}
