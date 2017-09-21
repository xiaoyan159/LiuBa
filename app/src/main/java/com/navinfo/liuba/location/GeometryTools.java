package com.navinfo.liuba.location;

import android.graphics.Point;
import android.util.Log;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author qj 几何工具类
 */
public class GeometryTools {

    static final double PI = 3.14159216;
    private static volatile GeometryTools mInstance;

    public static GeometryTools getInstance() {

        if (mInstance == null) {
            synchronized (GeometryTools.class) {
                if (mInstance == null) {
                    mInstance = new GeometryTools();
                }
            }
        }
        return mInstance;
    }

    private GeometryTools() {
        super();
    }


    /**
     * 返回矩形点
     *
     * @param
     * @param dis  -距离
     * @param unit ：单位 1：pixels像素 2： meters 米
     */
    public Coordinate[] getEnvelope(GeoPoint gp, double dis, SNAP_TYPE unit) {
        if (gp == null)
            return null;
        Coordinate[] coords = new Coordinate[5];
        switch (unit) {
            case SNAP_METER:

                double minLat = 0.0;
                double minLng = 0.0;
                double maxLat = 0.0;
                double maxLng = 0.0;

                double latitude = gp.getLat();
                double longitude = gp.getLon();
                double degree = (24901 * 1609) / 360.0;

                double raidusMile = dis;

                double dpmLat = 1 / degree;
                double radiusLat = dpmLat * raidusMile;

                minLat = latitude - radiusLat;
                maxLat = latitude + radiusLat;

                double mpdLng = degree * Math.cos(latitude * (3.1415926 / 180));
                double dpmLng = 1 / mpdLng;
                double radiusLng = dpmLng * raidusMile;
                minLng = longitude - radiusLng;
                maxLng = longitude + radiusLng;

                coords[0] = new Coordinate(minLng, minLat);
                coords[1] = new Coordinate(maxLng, minLat);
                coords[2] = new Coordinate(maxLng, maxLat);
                coords[3] = new Coordinate(minLng, maxLat);
                coords[4] = new Coordinate(minLng, minLat);
                break;
        }
        return coords;
    }

    /**
     * 返回点几何
     *
     * @param gp
     * @return Geometry
     */
    public static Geometry createGeometry(GeoPoint gp) {
        if (gp != null) {
            Coordinate coordinate = new Coordinate(FMStringUtils.formatDouble5(gp.getLon()), FMStringUtils.formatDouble5(gp.getLat()));
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createPoint(coordinate);
            return geo;
        }

        return null;

    }

    /**
     * 返回点几何
     *
     * @param gp
     * @param formatDouble5
     * @return Geometry
     */
    public static Geometry createGeometry(GeoPoint gp, boolean formatDouble5) {
        if (gp != null) {
            Coordinate coordinate = null;
            if (formatDouble5) {
                coordinate = new Coordinate(FMStringUtils.formatDouble5(gp.getLon()), FMStringUtils.formatDouble5(gp.getLat()));
            } else {
                coordinate = new Coordinate(gp.getLon(), gp.getLat());
            }
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createPoint(coordinate);
            return geo;
        }

        return null;

    }

    public static Geometry createGeometry(double[] coor) {
        if (coor != null && coor.length == 2) {
            Coordinate coordinate = new Coordinate(FMStringUtils.formatDouble5(coor[0]), FMStringUtils.formatDouble5(coor[1]));
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createPoint(coordinate);
            return geo;
        }

        return null;

    }

    /**
     * 获取多面
     *
     * @param polygons
     * @return
     */
    public static MultiPolygon createMultiPolygon(Polygon[] polygons) {
        if (polygons == null || polygons.length == 0)
            return null;
        MultiPolygon multiPolygon = null;
        GeometryFactory factory = new GeometryFactory();

        try {

            multiPolygon = factory.createMultiPolygon(polygons);

        } catch (Exception e) {

        }

        return multiPolygon;
    }

    /**
     * 获取多线
     *
     * @param lineStrings
     * @return
     */
    public static MultiLineString createMultiLine(LineString[] lineStrings) {
        if (lineStrings == null || lineStrings.length == 0)
            return null;
        MultiLineString multiLineString = null;
        GeometryFactory factory = new GeometryFactory();

        try {

            multiLineString = factory.createMultiLineString(lineStrings);

        } catch (Exception e) {

        }

        return multiLineString;
    }

    /**
     * 创建集合
     *
     * @param coords []
     * @return Geometry
     */
    public MultiPoint createMultiPoint(Coordinate[] coords) {

        if (coords == null || coords.length == 0)
            return null;

        MultiPoint createMultiPoint = null;

        GeometryFactory factory = new GeometryFactory();

        try {

            createMultiPoint = factory.createMultiPoint(coords);

        } catch (Exception e) {

        }

        return createMultiPoint;
    }

    /**
     * 创建集合
     *
     * @param list []
     * @return Geometry
     */
    public static MultiPoint createMultiPoint(List<GeoPoint> list) {

        if (list == null || list.size() == 0)
            return null;

        MultiPoint createMultiPoint = null;

        GeometryFactory factory = new GeometryFactory();

        try {
            Coordinate[] coords = new Coordinate[list.size()];

            for (int i = 0; i < list.size(); i++) {
                coords[i] = new Coordinate(list.get(i).getLon(), list.get(i).getLat());
            }

            createMultiPoint = factory.createMultiPoint(coords);

        } catch (Exception e) {

        }

        return createMultiPoint;
    }


    /**
     * 返回点几何
     *
     * @param wkt
     * @return Geometry
     */
    public static Geometry createGeometry(String wkt) {

        if (wkt == null || wkt.equals(""))
            return null;

        WKTReader reader = new WKTReader();

        Geometry geometry;
        try {

            geometry = reader.read(wkt);

            if (geometry != null) {
                return geometry;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // BaseToast.makeText(context, "初始化任务失败!!!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 创建多边形几何
     *
     * @param coords []
     * @return Geometry
     */
    public static String createPolygon(Coordinate[] coords) {
        GeometryFactory factory = new GeometryFactory();

        Polygon gon = factory.createPolygon(coords);

        if (gon == null)
            return null;
        return gon.toString();
    }

    /**
     * 创建多边形几何
     *
     * @param geoPointList
     * @return Polygon
     */
    public static Polygon createPolygon(List<GeoPoint> geoPointList) {
        if (geoPointList != null && geoPointList.size() >= 3) {
            Coordinate[] coordinates = new Coordinate[geoPointList.size()];
            for (int i = 0; i < geoPointList.size(); i++) {
                coordinates[i] = new Coordinate(geoPointList.get(i).getLon(), geoPointList.get(i).getLat());
            }
            GeometryFactory factory = new GeometryFactory();
            return factory.createPolygon(coordinates);
        }
        return null;
    }

/*	*//**
     * 创建线
     *
     * @param coords
     *            []
     * @return Geometry
     *
     * *//*
    public String createLineString(Coordinate[] coords) {
		LineString lineString = null;
		GeometryFactory factory = new GeometryFactory();
		lineString = factory.createLineString(coords);
		return lineString.toString();
	}
*/

    /**
     * 创建线
     *
     * @param coords []
     * @return Geometry
     */
    public LineString createLineString(Coordinate[] coords) {
        LineString lineString = null;
        GeometryFactory factory = new GeometryFactory();
        lineString = factory.createLineString(coords);
        return lineString;
    }

    /**
     * 创建点
     *
     * @param coord
     * @return Point
     */
    public com.vividsolutions.jts.geom.Point createPoint(Coordinate coord) {
        com.vividsolutions.jts.geom.Point point = null;
        GeometryFactory factory = new GeometryFactory();
        point = factory.createPoint(coord);
        return point;
    }

    /**
     * 工具接口
     *
     * @param gp
     * @param distance
     * @param unit     1像素   2米
     * @return
     */
    public String getWktBox(GeoPoint gp, int distance, SNAP_TYPE unit) {

        Coordinate[] coords = getEnvelope(gp, distance, unit);

        if (coords == null)
            return null;

        String wkt = createPolygon(coords);

        return wkt;
    }

    public Geometry getWktGemetry(GeoPoint gp, int distance, SNAP_TYPE unit) {
        Coordinate[] coords = getEnvelope(gp, distance, unit);
        Polygon gon = null;
        GeometryFactory factory = new GeometryFactory();
        gon = factory.createPolygon(coords);
        return gon;
    }

    public Coordinate[] getWktCoord(GeoPoint gp, int distance, SNAP_TYPE unit) {
        Coordinate[] coords = getEnvelope(gp, distance, unit);
        return coords;
    }


    /**
     * 点几何转换为GeoPoint
     *
     * @param geometry POINT (116.4087300000000056 39.9392050000000012)
     * @return GeoPoint
     */
    public static GeoPoint createGeoPoint(String geometry) {

        if (geometry == null || geometry.equals(""))
            return null;

        WKTReader reader = new WKTReader();

        Geometry geometrys;

        try {

            geometrys = reader.read(geometry);

            if (geometrys != null) {

                com.vividsolutions.jts.geom.Point point = geometrys
                        .getInteriorPoint();

                GeoPoint geoInteriorPoint = new GeoPoint(FMStringUtils.formatDouble5(point.getX()), FMStringUtils.formatDouble5(point.getY()));

                if (geometrys.getGeometryType().equalsIgnoreCase("Point")) {

                    Coordinate coordinate = geometrys.getCoordinate();

                    GeoPoint geo = new GeoPoint(FMStringUtils.formatDouble5(coordinate.x), FMStringUtils.formatDouble5(coordinate.y));

                    return geo;

                } else if (geometrys.getGeometryType().equalsIgnoreCase("LineString") || geometrys.getGeometryType().equalsIgnoreCase("MultiLineString")) {
                    Coordinate[] coordinates = geometrys.getCoordinates();
                    if (coordinates != null && coordinates.length > 0) {
                        GeoPoint geo = new GeoPoint(FMStringUtils.formatDouble5(coordinates[0].x), FMStringUtils.formatDouble5(coordinates[0].y));
                        return geo;
                    } else {
                        return geoInteriorPoint;
                    }

                } else {

                    return geoInteriorPoint;

                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // BaseToast.makeText(context, "初始化任务失败!!!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 计算两点距离
     *
     * @param startGeoPoint
     * @param endGeoPoint
     * @return double:单位 米
     */
    public static double distanceToDouble(GeoPoint startGeoPoint, GeoPoint endGeoPoint) {
        if (startGeoPoint != null && endGeoPoint != null) {
            Geometry startGeo = createGeometry(startGeoPoint);
            Geometry endGeo = createGeometry(endGeoPoint);
            double d = startGeo.distance(endGeo);
            return d * 100000;
        }
        return 0;

    }

    /**
     * LINESTRING (116.4206899999999933 39.9620999999999995,
     * 116.4184900000000056 39.9620600000000010)
     *
     * @param str
     * @return
     */
    public static List<GeoPoint> getGeoPoints(String str) {
        if (FMTextUtils.isEmpty(str))
            return null;
        List<GeoPoint> list = null;
        Geometry geometry = createGeometry(str);
        if (geometry != null) {
            Coordinate[] coordinates = geometry.getCoordinates();
            if (coordinates != null && coordinates.length > 0) {
                list = new ArrayList<GeoPoint>();
                for (Coordinate coor : coordinates) {
                    list.add(new GeoPoint(coor.x, coor.y));
                }
            }
        }
        return list;
    }

    public static Coordinate[] getGeoPoints2(String str) {
        Coordinate[] coordinates = null;
        if (FMTextUtils.isEmpty(str))
            return coordinates;
        Geometry geometry = createGeometry(str);
        if (geometry != null) {
            coordinates = geometry.getCoordinates();
        }
        return coordinates;
    }

    public static String getLineString(List<GeoPoint> list) {

        if (list != null && list.size() > 1) {
            int size = list.size();
            Coordinate[] coors = new Coordinate[size];
            for (int i = 0; i < size; i++) {
                GeoPoint gp = list.get(i);
                coors[i] = new Coordinate(FMStringUtils.formatDouble5(gp.getLon()), FMStringUtils.formatDouble5(gp.getLat()));
            }
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createLineString(coors);
            if (geo != null)
                return geo.toString();
        }
        return null;
    }

    public static String getLineStringNoDouble5(List<GeoPoint> list) {

        if (list != null && list.size() > 1) {
            int size = list.size();
            Coordinate[] coors = new Coordinate[size];
            for (int i = 0; i < size; i++) {
                GeoPoint gp = list.get(i);
                coors[i] = new Coordinate(gp.getLon(), gp.getLat());
            }
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createLineString(coors);
            if (geo != null)
                return geo.toString();
        }
        return null;
    }

    /**
     * 获取线型
     *
     * @param list
     * @return
     */
    public static LineString getLineStrinGeo(List<GeoPoint> list) {

        if (list != null && list.size() > 1) {
            int size = list.size();
            Coordinate[] coors = new Coordinate[size];
            for (int i = 0; i < size; i++) {
                GeoPoint gp = list.get(i);
                coors[i] = new Coordinate(FMStringUtils.formatDouble5(gp.getLon()), FMStringUtils.formatDouble5(gp.getLat()));
            }
            GeometryFactory factory = new GeometryFactory();
            LineString geo = factory.createLineString(coors);
            if (geo != null)
                return geo;
        }
        return null;
    }

    /**
     * 获取线型的外扩矩形
     *
     * @param list
     * @return
     */
    public static Polygon getPolygonEnvelope(List<GeoPoint> list) {
        LineString lineString = getLineStrinGeo(list);
        if (lineString != null) {
            Geometry geometry = lineString.getEnvelope();
            if (geometry != null && geometry.getGeometryType().equals("Polygon")) {
                return (Polygon) geometry;
            }
        }
        return null;

    }

    public static String getPolygonString(List<GeoPoint> list) {
        if (list != null && list.size() > 2) {
            GeoPoint frist = list.get(0);
            GeoPoint last = list.get(list.size() - 1);
            if (frist.getLon() != last.getLon() || frist.getLat() != last.getLat()) {
                return null;
            }
            Coordinate[] coors = new Coordinate[list.size()];

            for (int i = 0; i < list.size(); i++) {
                GeoPoint gp = list.get(i);
                coors[i] = new Coordinate(FMStringUtils.formatDouble5(gp.getLon()), FMStringUtils.formatDouble5(gp.getLat()));
            }
            GeometryFactory factory = new GeometryFactory();
            Geometry geo = factory.createPolygon(coors);
            if (geo != null)
                return geo.toString();
        }

        return null;

    }

    public static String getMultiLineString(List<List<GeoPoint>> list) {
        if (list != null && !list.isEmpty()) {
            StringBuffer result = new StringBuffer();
            result.append("MULTILINESTRING(");
            for (int i = 0; i < list.size(); ++i) {
                List<GeoPoint> pointList = list.get(i);
                if (pointList != null && !pointList.isEmpty()) {
                    result.append("(");
                    for (int j = 0; j < pointList.size(); ++j) {
                        result.append(pointList.get(j).getLon());
                        result.append(" ");
                        result.append(pointList.get(j).getLat());
                        if (j == pointList.size() - 1) {
                            result.append(")");
                        } else {
                            result.append(",");
                        }
                    }
                    if (i != list.size() - 1) {
                        result.append(",");
                    }
                }
            }
            result.append(")");
            return result.toString();
        }
        return null;
    }

    public static List<List<GeoPoint>> getGeoPointArrFromMultiLineString(String multiLintString) {
        if (FMTextUtils.isEmpty(multiLintString))
            return null;
        multiLintString = multiLintString.substring(multiLintString.indexOf("(") + 1,
                multiLintString.length() - 1);
        if (!FMTextUtils.isEmpty(multiLintString)) {
            List<List<GeoPoint>> resultList = new ArrayList<List<GeoPoint>>();
            while (!FMTextUtils.isEmpty(multiLintString)) {
                int startIndex = multiLintString.indexOf("(");
                int endIndex = multiLintString.indexOf(")");
                if (startIndex < endIndex) {
                    endIndex++;
                    String geometry = multiLintString.substring(startIndex, endIndex);
                    if (FMTextUtils.isEmpty(geometry))
                        continue;
                    if (geometry.startsWith("(")) {
                        geometry = geometry.substring(1, geometry.length());
                    }
                    if (geometry.endsWith(")")) {
                        geometry = geometry.substring(0, geometry.length() - 1);
                    }
                    String points[] = geometry.split(",");
                    List<GeoPoint> list = new ArrayList<GeoPoint>();
                    for (int i = 0; i < points.length; i++) {
                        String point[] = points[i].trim().split(" ");
                        if (point.length == 2) {
                            list.add(new GeoPoint(Double.parseDouble(point[0].trim()),
                                    Double.parseDouble(point[1].trim())));
                        }
                    }
                    if (list != null && !list.isEmpty()) {
                        resultList.add(list);
                    }
                    multiLintString = multiLintString.substring(endIndex);
                }
            }
            if (resultList != null && !resultList.isEmpty()) {
                return resultList;
            }
        }

        return null;
    }

    // [end]

    /**
     * 保留坐标小数5位
     *
     * @param geo geo
     * @return geo;
     */
    public GeoPoint formatDouble5(GeoPoint geo) {
        if (geo == null)
            return null;
        else {
            geo.setLat(FMStringUtils.formatDouble5(geo.getLat()));
            geo.setLon(FMStringUtils.formatDouble5(geo.getLon()));
            return geo;
        }
    }

    public Geometry focalPoint(Geometry geo1, Geometry geo2) {
        if (geo1 == null || geo2 == null)
            return null;

        Geometry geo = geo1.intersection(geo2);
        Log.i("geo", geo.toString() + "===1");
        geo = geo1.union(geo2);
        Log.i("geo", geo.toString() + "===2");
        geo = geo1.symDifference(geo2);
        Log.i("geo", geo.toString() + "===3");
        return null;
    }

    /**
     * 角度变换
     *
     * @param angle
     * @return angle;
     */
    public double angleSwap(double angle) {

        if (angle >= 180)
            angle = angle - 180;
        else
            angle = angle + 180;
        angle = formatDouble5(angle);
        return angle;
    }

    /**
     * 是否第一个点
     *
     * @param lineString
     * @param point
     * @return boolean;
     */
    public boolean isFirstPoint(String lineString, String point) {
        if (FMTextUtils.isEmpty(lineString) || FMTextUtils.isEmpty(point))
            return false;

        Geometry lineGeo = createGeometry(lineString);

        Geometry pGeo = createGeometry(point);

        if (lineGeo != null && lineGeo.getGeometryType().equals("LineString") && pGeo != null && pGeo.getGeometryType().equals("Point")) {
            Coordinate[] coords = lineGeo.getCoordinates();
            if (coords.length > 0) {
                if (coords[0].x == pGeo.getCoordinate().x && coords[0].y == pGeo.getCoordinate().y)
                    return true;
            }
        }

        return false;
    }

    /**
     * 是否最后点
     *
     * @param lineString
     * @param point
     * @return boolean;
     */
    public boolean isEndPoint(String lineString, String point) {

        if (FMTextUtils.isEmpty(lineString) || FMTextUtils.isEmpty(point))
            return false;

        Geometry lineGeo = createGeometry(lineString);

        Geometry pGeo = createGeometry(point);

        if (lineGeo != null && lineGeo.getGeometryType().equals("LineString") && pGeo != null && pGeo.getGeometryType().equals("Point")) {
            Coordinate[] coords = lineGeo.getCoordinates();
            if (coords.length > 0) {
                if (coords[coords.length - 1].x == pGeo.getCoordinate().x && coords[coords.length - 1].y == pGeo.getCoordinate().y)
                    return true;
            }
        }

        return false;

    }

    /**
     * 是否起点或终点
     *
     * @param lineString
     * @param point
     * @return boolean;
     */
    public boolean isFirstOrEndPoint(String lineString, String point) {


        if (FMTextUtils.isEmpty(lineString) || FMTextUtils.isEmpty(point))
            return false;

        Geometry lineGeo = createGeometry(lineString);

        Geometry pGeo = createGeometry(point);

        if (lineGeo != null && lineGeo.getGeometryType().equals("LineString") && pGeo != null && pGeo.getGeometryType().equals("Point")) {
            Coordinate[] coords = lineGeo.getCoordinates();
            if (coords.length > 0) {

                if (coords[coords.length - 1].x == pGeo.getCoordinate().x && coords[coords.length - 1].y == pGeo.getCoordinate().y)
                    return true;

                if (coords[coords.length - 1].x == pGeo.getCoordinate().x && coords[coords.length - 1].y == pGeo.getCoordinate().y)
                    return true;
            }
        }

        return false;

    }

    /**
     * 判断捕捉集合中是否包含传入的点位信息
     *
     * @param geoList
     * @return int;
     */
    public int isListContain(List<GeoPoint> geoList, GeoPoint mGeoPoint) {

        if (geoList != null && geoList.size() > 0 && mGeoPoint != null) {
            for (int i = 0; i < geoList.size(); i++) {
                if (geoList.get(i).equals(mGeoPoint)) {
                    return i;
                }
            }
        }

        return -1;
    }



    public int LocatePointInPath(List<GeoPoint> list, GeoPoint point) {
        int index = -1;

        if (list == null || list.size() < 2 || point == null) {
            return index;
        }
//        createPolygon(getEnvelope(new GeoPoint(formatDouble5(point.getLon()), formatDouble5(point.getLat())), 6, 2))
        Geometry geomerty = createGeometry(point);

        Coordinate[] coods = new Coordinate[2];
        HashMap<Integer, Double> distance = new HashMap<Integer, Double>();
        //遍历线节点，判断垂足在哪个线段上，并将垂足插入
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {

                coods[0] = new Coordinate(list.get(i).getLon(), list.get(i).getLat());

                coods[1] = new Coordinate(list.get(i + 1).getLon(), list.get(i + 1).getLat());

                LineString line = createLineString(coods);

                double dou = line.distance(geomerty);
                distance.put(i, dou);
            }
        }
        double temp = distance.get(0);
        for (int i = 1; i < distance.size(); i++) {
            if (temp > distance.get(i)) {
                temp = distance.get(i);
            }
        }
        for (int i = 0; i < distance.size(); i++) {
            if (temp == distance.get(i)) {
                return i;
            }
        }
        return index;
    }




    public static boolean isSimplePolygon(List<GeoPoint> list) {

        if (list != null && list.size() > 0) {
            Geometry polygon = createGeometry(getPolygonString(list));
            if (polygon != null) {
                return polygon.isSimple();
            }
        }

        return false;
    }

    public static boolean isSimplePolygon(List<Point> list, Point endPoint) {

        if (list != null && list.size() > 0) {

            if (list != null && list.size() > 2) {
                Point frist = list.get(0);
                Point last = list.get(list.size() - 1);
                if (frist.x != last.x || frist.y != last.y) {
                    return false;
                }
                int size = list.size();
                if(endPoint != null){
                    size ++;
                }
                Coordinate[] coors = new Coordinate[size];

                for (int i = 0; i < list.size(); i++) {
                    Point gp = list.get(i);
                    coors[i] = new Coordinate(gp.x, gp.y);
                }
                if(endPoint != null){
                    coors[coors.length - 1] = coors[coors.length - 2];
                    coors[coors.length - 2] = new Coordinate(endPoint.x, endPoint.y);
                }
                GeometryFactory factory = new GeometryFactory();
                Geometry geo = factory.createPolygon(coors);
                if (geo != null) {
                    return geo.isSimple();
                }
            }
        }

        return false;
    }

    /**
     * 小数点后四舍五入 精确到第5位
     *
     * @param d
     * @return
     */
    private double formatDouble5(double d) {
        BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 是否包含点
     *
     * @param //List<String> wkts
     * @param //GeoPoint     geoPoint
     * @return int
     */
    public int isContainsGeo(List<String> wkts, GeoPoint geoPoint) {

        if (wkts == null || wkts.size() == 0 || geoPoint == null) {
            return -1;
        }

        com.vividsolutions.jts.geom.Point point = createPoint(new Coordinate(geoPoint.getLon(), geoPoint.getLat()));

        for (int i = 0; i < wkts.size(); i++) {

            Geometry geometry = createGeometry(wkts.get(i));

            if (geometry != null && geometry.getGeometryType().equals("Polygon")) {

                boolean result = geometry.contains(point);

                if (result)
                    return i;
            }
        }

        return -1;
    }

    public int isContainsGeo(List<GeoPoint> pointList, GeoPoint clickPoint, int dis) {

        if (pointList == null || pointList.size() == 0 || clickPoint == null) {
            return -1;
        }

        String wkt = getWktBox(clickPoint, dis, SNAP_TYPE.SNAP_METER);

        Geometry geometry = createGeometry(wkt);

        Geometry clickGeometry = createGeometry(clickPoint);

        int index = -1;
        double min = -1;
        for (int i = 0; i < pointList.size(); i++) {
            com.vividsolutions.jts.geom.Point point = createPoint(new Coordinate(pointList.get(i).getLon(), pointList.get(i).getLat()));

            if (geometry != null && geometry.getGeometryType().equals("Polygon")) {

                boolean result = geometry.contains(point);
                if (result) {
                    double distance = clickGeometry.distance(point);
                    if (min < 0 || distance < min) {
                        index = i;
                        min = distance;
                    }
                }
            }
        }

        return index;
    }

    public static List<GeoPoint> getLineIntersectionGeoPoint(LineString lineString) {
        if (lineString != null) {

            Coordinate[] coords = lineString.getCoordinates();

            List<LineString> list = new ArrayList<LineString>();

            GeometryFactory factory = new GeometryFactory();

            for (int i = 0; i < coords.length; i++) {
                if (i != coords.length - 1) {
                    Coordinate[] coord = new Coordinate[2];
                    coord[0] = coords[i];
                    coord[1] = coords[i + 1];
                    LineString line = factory.createLineString(coord);
                    if (line != null)
                        list.add(line);
                }
            }

            List<GeoPoint> listGeoPoint = new ArrayList<GeoPoint>();

            if (list != null && list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    LineString line1 = list.get(i);

                    for (int j = 0; j < list.size(); j++) {
                        if (i != j) {
                            LineString line2 = list.get(j);
                            Geometry geometry = line1.intersection(line2);
                            if (geometry != null && !geometry.isEmpty() && geometry.getGeometryType().equalsIgnoreCase("Point")) {
                                if (!line2.getStartPoint().equals(geometry) && !line2.getEndPoint().equals(geometry)) {
                                    listGeoPoint.add(new GeoPoint(geometry.getCoordinate().x, geometry.getCoordinate().y));
                                }
                            }
                        }
                    }

                }

            }
            if (listGeoPoint != null && listGeoPoint.size() > 0) {
                return listGeoPoint;
            }
        }


        return null;
    }

    /**
     * 点与几何最近点位置
     *
     * @param list
     * @param geoPoint
     * @return geopoint
     * @author qiji
     */
    public static GeoPoint getZuijinGeoPoint(List<GeoPoint> list, GeoPoint geoPoint) {//MapManager.getInstance().getMap().getMapCenterGeoLocation()屏幕中心点

        if (list == null || list.size() == 0 || geoPoint == null)
            return null;

        double dis = 0;

        GeoPoint geo = null;

        for (GeoPoint geopoint : list) {

            double disTemp = distanceToDouble(geoPoint, geopoint);

            if (dis == 0 || dis < disTemp) {

                dis = disTemp;

                geo = geopoint;

            }

        }

        return geo;
    }

    public static String GeometryFormatDouble5(String geometry) {
        try{

            GeoPoint point = createGeoPoint(geometry);

            return createGeometry(point).toString();
        }catch (Exception e){

        }

        return "";
    }

    public static String mergeMutiLineString(List<String> list) {

        try {
            if (list != null && list.size() > 0) {

                LineMerger merger = new LineMerger();

                for (String line : list) {

                    merger.add(createGeometry(line));

                }

                Collection collection = merger.getMergedLineStrings();

                if (collection != null && collection.size() > 1) {

                    List<String> list1 = new ArrayList<String>();
                    if (collection.size() == 2) {

                        Iterator it = collection.iterator();

                        while (it.hasNext()) {

                            list1.add(it.next() + "");

                        }

                        if (list1.size() == 2) {

                            List<GeoPoint> list2 = getGeoPoints(list1.get(0));

                            List<GeoPoint> list3 = getGeoPoints(list1.get(1));

                            if (list2 != null && list3 != null) {

                                String line = "";

                                if (list2.get(0).equals(list3.get(list3.size() - 1))) {

                                    list3.addAll(list2);

                                    line = getLineStringNoDouble5(list3);

                                } else if (list2.get(0).equals(list3.get(0))) {

                                    List<GeoPoint> listTemp = new ArrayList<GeoPoint>();

                                    for (int i = list2.size() - 1; i >= 0; i--) {
                                        listTemp.add(list2.get(i));
                                    }

                                    listTemp.addAll(list3);

                                    line = getLineStringNoDouble5(listTemp);

                                } else if (list2.get(list2.size() - 1).equals(list3.get(list3.size() - 1))) {

                                    List<GeoPoint> listTemp = new ArrayList<GeoPoint>();

                                    for (int i = list2.size() - 1; i >= 0; i--) {

                                        listTemp.add(list2.get(i));

                                    }

                                    list3.addAll(listTemp);

                                    line = getLineStringNoDouble5(list3);

                                } else if (list2.get(list2.size() - 1).equals(list3.get(0))) {

                                    list2.addAll(list3);

                                    line = getLineStringNoDouble5(list2);

                                }

                                if (!FMTextUtils.isEmpty(line)) {

                                    Geometry geometry = createGeometry(line);

                                    if (geometry != null && !geometry.isEmpty() && geometry.getGeometryType().equalsIgnoreCase("LineString")) {

                                        return line;

                                    }
                                }

                            }

                        }


                    }

                    return null;

                }

                return collection.iterator().next().toString();
            }
        } catch (Exception e) {

        }


        return null;
    }

    public static double distance(String geo1, String geo2) {
        if (FMTextUtils.isEmpty(geo1) || FMTextUtils.isEmpty(geo2)) {
            return -1;
        }
        Geometry mGeo = createGeometry(geo1);

        Geometry mGeo2 = createGeometry(geo2);

        if (mGeo != null && mGeo2 != null) {

            return mGeo.distance(mGeo2);
        }
        return -1;
    }


    public enum SNAP_TYPE {
        SNAP_METER;
    }


    //经纬度转墨卡托
    public static GeoPoint lonLat2Mercator(GeoPoint lonLat) {

        double x = lonLat.getLon() * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lonLat.getLat()) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        GeoPoint mercator = new GeoPoint(x, y);
        return mercator;
    }

    //墨卡托转经纬度
    public static GeoPoint Mercator2lonLat(GeoPoint mercator) {

        double x = mercator.getLon() / 20037508.34 * 180;
        double y = mercator.getLat() / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        GeoPoint lonLat = new GeoPoint(x, y);
        return lonLat;
    }

}
