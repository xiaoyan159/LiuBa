package com.navinfo.liuba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.litesuits.common.io.FileUtils;
import com.navinfo.liuba.enity.Point;
import com.navinfo.liuba.enity.TrackEnity;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.location.GeoPoint;
import com.navinfo.liuba.location.GeometryTools;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.LiuBaApplication;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.BaseToast;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.alibaba.fastjson.JSON.toJSONString;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImgUserCenter;
    private View btnWalk;
    private View btnAppoint;
    private View mView;
    private LinearLayout mCompeleteWalkTheDog;
    private LinearLayout mLinearWalkAppoint;
    private LinearLayout mLinearComplete;
    private LinearLayout mLinearStart;
    private LinearLayout mLinearGoEnd;
    private LinearLayout mLinearGoOn;
    private LinearLayout mLinearEnd;
    private Chronometer mTvTime;
    private TextView mTvMile;
    private Button btnStart;
    private RadioButton totalMile;
    private RadioButton totalTime;
    private RadioButton isShit;
    private RadioButton isPee;
    private ImageView imgShit;
    private ImageView imgPee;
    private int ifShit = 0;
    private int ifPee = 0;
    private boolean tagShit = false;
    private boolean tagPee = false;

    private View layer_user_menu;//用户菜单的布局，点击左上角按钮显示
    private TextView tv_userInfo, tv_orderInfo, tv_secret, tv_quite;

    //百度地图组件
    private MapView mMapView = null;

    private long pauseString;//暂停时字符串
    private List<TrackEnity> trackList;
    private LiuBaApplication liuBaApplication;
    private Timer timer;
    OverlayOptions ooPolyline;
    List<LatLng> points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(MainActivity.this, R.layout.activity_main, null);
        setContentView(mView);

        ooPolyline = new PolylineOptions();
        points = new ArrayList<>();

        mImgUserCenter = (ImageView) mView.findViewById(R.id.user_center);
        btnWalk = mView.findViewById(R.id.walk_the_dog);
        btnAppoint = mView.findViewById(R.id.make_an_appointment);

        mImgUserCenter.setOnClickListener(this);
        btnWalk.setOnClickListener(this);
        btnAppoint.setOnClickListener(this);

        layer_user_menu = findViewById(R.id.layer_user_menu);
        tv_userInfo = (TextView) findViewById(R.id.tv_main_userInfo);
        tv_orderInfo = (TextView) findViewById(R.id.tv_main_orderInfo);
        tv_secret = (TextView) findViewById(R.id.tv_main_secret);
        tv_quite = (TextView) findViewById(R.id.tv_main_quite);

        layer_user_menu.setOnClickListener(this);
        tv_userInfo.setOnClickListener(this);
        tv_orderInfo.setOnClickListener(this);
        tv_secret.setOnClickListener(this);
        tv_quite.setOnClickListener(this);

        mCompeleteWalkTheDog = (LinearLayout) findViewById(R.id.complete_walk_the_dog);
        mLinearWalkAppoint = (LinearLayout) findViewById(R.id.linear_activity_walk_appoint);//遛，约，布局
        mLinearComplete = (LinearLayout) findViewById(R.id.linear_complete);//完成界面
        mLinearStart = (LinearLayout) findViewById(R.id.linear_activity_start);//开始界面
        mTvTime = (Chronometer) findViewById(R.id.start_time);
        mTvMile = (TextView) findViewById(R.id.start_mile);
        btnStart = (Button) findViewById(R.id.btn_start);
        mLinearGoEnd = (LinearLayout) findViewById(R.id.linear_activity_go_on_end);
        mLinearGoOn = (LinearLayout) findViewById(R.id.linear_go_on);
        mLinearEnd = (LinearLayout) findViewById(R.id.linear_end);
        totalMile = (RadioButton) findViewById(R.id.total_mile);
        totalTime = (RadioButton) findViewById(R.id.total_time);
        isShit = (RadioButton) findViewById(R.id.is_shit);
        isPee = (RadioButton) findViewById(R.id.is_pee);
        imgShit = (ImageView) findViewById(R.id.img_shit_location);
        imgPee = (ImageView) findViewById(R.id.img_pee_location);

        mCompeleteWalkTheDog.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        mLinearGoOn.setOnClickListener(this);
        mLinearEnd.setOnClickListener(this);
        imgShit.setOnClickListener(this);
        imgPee.setOnClickListener(this);
        mLinearComplete.setOnClickListener(this);

        mMapView = (MapView) findViewById(R.id.bmapView);

        //获取当前的定位位置
        if (((LiuBaApplication) getApplication()).getCurrentLocation() != null) {
            // 开启定位图层
            mMapView.getMap().setMyLocationEnabled(true);
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder().latitude(((LiuBaApplication) getApplication()).getCurrentLocation().getLatitude())
                    .longitude(((LiuBaApplication) getApplication()).getCurrentLocation().getLongitude()).build();
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mMapView.getMap().setMyLocationData(locData);
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromPath(SystemConstant.herderJpgPath);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
            mMapView.getMap().setMyLocationConfiguration(config);
            //设置地图显示比例尺
            float f = mMapView.getMap().getMaxZoomLevel();//19.0 最小比例尺
            MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(f - 5);//大小按需求计算就可以
            mMapView.getMap().animateMapStatus(u);
        }
        trackList = new ArrayList<>();
        liuBaApplication = (LiuBaApplication) getApplication();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            if (msg.what == 0) {
                timer = new Timer();
                //这里可以进行UI操作，如Toast，Dialog等
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        BDLocation currentLocation = liuBaApplication.getCurrentLocation();
                        if (currentLocation.getLongitude() == currentLocation.getLatitude() && currentLocation.getLatitude() == 4.9e-324) {
                            Toast.makeText(MainActivity.this, "定位失败！", Toast.LENGTH_LONG).show();
                        } else {
                            TrackEnity trackEnity = new TrackEnity();
                            GeoPoint geopoint = new GeoPoint(currentLocation.getLongitude(), currentLocation.getLatitude());
                            trackEnity.setGeometry(GeometryTools.createGeometry(geopoint).toString());
                            trackEnity.setIsPee(ifPee);
                            trackEnity.setIsShit(ifShit);
                            trackList.add(trackEnity);
                            ifShit = 0;
                            ifPee = 0;
                            //地图绘制图形
                            // 构造折线点坐标
                            points.add(new LatLng(currentLocation.getLongitude(), currentLocation.getLatitude()));
                            if (points != null && points.size() > 1) {
                                double totalMiles = getTotalMiles(points);
//                                mTvMile.setText(totalMiles + "m");
                                drawTrackLine(points);
                            }
                        }
                    }
                }, 1000, 1000);//1秒之后，每隔2秒做一次run()操作
            }
            if (msg.what == 1) {
                timer.cancel();
            }
            if (msg.what == 3) {
                if (trackList != null && trackList.size() > 0) {
                    BaseRequestParams registerParams = new BaseRequestParams(SystemConstant.trackCreate);
                    String json = JSON.toJSONString(trackList);
                    registerParams.setParamerJson(json);

                    DefaultHttpUtil.postMethod(MainActivity.this, registerParams, new DefaultHttpUtil.HttpCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(MainActivity.this, "轨迹提交成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "无轨迹数据", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    /**
     * 绘制轨迹
     * * @param pointsList
     */
    public void drawTrackLine(List<LatLng> pointsList) {
        mMapView.getMap().clear();
        OverlayOptions ooPolyline = new PolylineOptions().width(10).points(pointsList).color(Integer.valueOf(Color.RED));
        //添加在地图中
        mMapView.getMap().addOverlay(ooPolyline);
    }

    /**
     * 计算路径
     *
     * @param pointsList
     * @return
     */
    public double getTotalMiles(List<LatLng> pointsList) {
        double totalDiatance = 0;
        for (int i = 1; i < pointsList.size(); i++) {
            double distance = DistanceUtil.getDistance(pointsList.get(i - 1), pointsList.get(i));
            totalDiatance = totalDiatance + distance;
        }
        return totalDiatance;
    }

    public String createGeometryPoint(double lattitude, double longtitude) {
        return "POINT(" + lattitude + " " + longtitude + ")";
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_center://用户点击用户图标，弹出用户设置界面
                if (layer_user_menu.isShown()) {
                    layer_user_menu.setVisibility(View.GONE);
                } else {
                    layer_user_menu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.walk_the_dog:
                mLinearWalkAppoint.setVisibility(View.GONE);
                mLinearStart.setVisibility(View.VISIBLE);
                break;
            case R.id.make_an_appointment:
                Intent intent = new Intent(MainActivity.this, OrderInputActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_main_secret:
                Intent secretIntent = new Intent(MainActivity.this, MyScretActivity.class);
                startActivity(secretIntent);
                break;

            case R.id.btn_start:
                if (btnStart.getText().toString().equals("开始")) {
                    mTvTime.start();
                    btnStart.setText("结束");
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.handleMessage(msg);
                    imgShit.setVisibility(View.VISIBLE);
                    imgPee.setVisibility(View.VISIBLE);
                } else {
                    pauseString = convertStrTimeToLong(mTvTime.getText().toString());
                    mTvTime.stop();
                    mLinearStart.setVisibility(View.GONE);
                    mLinearGoEnd.setVisibility(View.VISIBLE);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.handleMessage(msg);
                    imgShit.setVisibility(View.GONE);
                    imgPee.setVisibility(View.GONE);
                }

                break;

            case R.id.linear_go_on://继续
                mLinearGoEnd.setVisibility(View.GONE);
                mLinearStart.setVisibility(View.VISIBLE);
                mTvTime.setBase(pauseString);//设置成暂停时时间
                mTvTime.start();
                Message msg = new Message();
                msg.what = 0;
                mHandler.handleMessage(msg);
                imgShit.setVisibility(View.VISIBLE);
                imgPee.setVisibility(View.VISIBLE);
                break;

            case R.id.linear_end://结束
                mLinearGoEnd.setVisibility(View.GONE);
                mCompeleteWalkTheDog.setVisibility(View.VISIBLE);
                totalTime.setText(getChronometerSeconds(mTvTime) + "s");
                Message msgs = new Message();
                msgs.what = 1;
                mHandler.handleMessage(msgs);
                imgShit.setVisibility(View.GONE);
                imgPee.setVisibility(View.GONE);
                if (tagShit) {
                    isShit.setText("√");
                } else {
                    isShit.setText("×");
                }
                tagShit = false;
                if (tagPee) {
                    isPee.setText("√");
                } else {
                    isPee.setText("×");
                }
                tagPee = false;
                break;
            case R.id.img_shit_location:
                ifShit = 1;
                tagShit = true;
                break;
            case R.id.img_pee_location:
                ifPee = 1;
                tagPee = true;
                break;
            case R.id.linear_complete:
                mLinearWalkAppoint.setVisibility(View.VISIBLE);
                mCompeleteWalkTheDog.setVisibility(View.GONE);
                btnStart.setText("开始");
                mTvTime.setBase(SystemClock.elapsedRealtime());//计时器清零
                Message upLoadTrack = new Message();
                upLoadTrack.what = 3;
                mHandler.handleMessage(upLoadTrack);
                break;
        }

    }

    /**
     * 将String类型的时间转换成long,如：12:01:08
     *
     * @param strTime String类型的时间
     * @return long类型的时间
     */

    protected long convertStrTimeToLong(String strTime) {
        // TODO Auto-generated method stub
        String[] timeArry = strTime.split(":");
        long longTime = 0;
        if (timeArry.length == 2) {//如果时间是MM:SS格式
            longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 + Integer.parseInt(timeArry[1]) * 1000;
        } else if (timeArry.length == 3) {//如果时间是HH:MM:SS格式
            longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 * 60 + Integer.parseInt(timeArry[1])
                    * 1000 * 60 + Integer.parseInt(timeArry[0]) * 1000;
        }
        return SystemClock.elapsedRealtime() - longTime;
    }

    /**
     * @param cmt Chronometer控件
     * @return 小时+分钟+秒数  的所有秒数
     */
    public static String getChronometerSeconds(Chronometer cmt) {
        int totalss = 0;
        String string = cmt.getText().toString();
        if (string.length() == 7) {

            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours = hour * 3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins = min * 60;
            int SS = Integer.parseInt(split[2]);
            totalss = Hours + Mins + SS;
            return String.valueOf(totalss);
        } else if (string.length() == 5) {

            String[] split = string.split(":");
            String string3 = split[0];
            int min = Integer.parseInt(string3);
            int Mins = min * 60;
            int SS = Integer.parseInt(split[1]);

            totalss = Mins + SS;
            return String.valueOf(totalss);
        }
        return String.valueOf(totalss);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public String createTrackJson(List<HashMap<String, Object>> list) {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("geometry", list.get(i).get("geometry"));
            jsonObject.put("isShit", list.get(i).get("isShit"));
            jsonObject.put("isPee", list.get(i).get("isPee"));
            jsonArray.add(jsonObject);
        }
        object.put("data", jsonArray);
        return object.toString();
    }
}
