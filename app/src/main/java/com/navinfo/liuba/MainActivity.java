package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImgUserCenter;
    private ImageView mImgGps;
    private View btnWalk;
    private View btnAppoint;
    private View mView;
    private LinearLayout mCompeleteWalkTheDog;
    private LinearLayout mLinearWalkAppoint;
    private LinearLayout mLinearComplete;

    private View layer_user_menu;//用户菜单的布局，点击左上角按钮显示
    private TextView tv_userInfo, tv_orderInfo, tv_secret, tv_quite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(MainActivity.this, R.layout.activity_main, null);
        setContentView(mView);
        mImgUserCenter = (ImageView) mView.findViewById(R.id.user_center);
        mImgGps = (ImageView) mView.findViewById(R.id.gps_location);
        btnWalk = mView.findViewById(R.id.walk_the_dog);
        btnAppoint = mView.findViewById(R.id.make_an_appointment);
        mImgUserCenter.setOnClickListener(this);
        mImgGps.setOnClickListener(this);
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
        mLinearWalkAppoint = (LinearLayout) findViewById(R.id.linear_activity_walk_appoint);
        mLinearComplete = (LinearLayout) findViewById(R.id.linear_complete);
        mCompeleteWalkTheDog.setOnClickListener(this);
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
            case R.id.gps_location:

                break;
            case R.id.walk_the_dog:
                mLinearWalkAppoint.setVisibility(View.GONE);
                mCompeleteWalkTheDog.setVisibility(View.VISIBLE);
                break;
            case R.id.make_an_appointment:
                Intent intent = new Intent(MainActivity.this, OrderInputActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_main_secret:
                Intent secretIntent = new Intent(MainActivity.this, MyScretActivity.class);
                startActivity(secretIntent);
                break;

            case R.id.complete_walk_the_dog:
                mLinearWalkAppoint.setVisibility(View.VISIBLE);
                mCompeleteWalkTheDog.setVisibility(View.GONE);
                break;

        }
    }
}
