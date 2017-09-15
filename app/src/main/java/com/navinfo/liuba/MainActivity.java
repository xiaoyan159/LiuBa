package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImgUserCenter;
    private ImageView mImgGps;
    private View btnWalk;
    private View btnAppoint;
    private View mView;

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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_center:
            case R.id.gps_location:

                break;
            case R.id.walk_the_dog:

                break;
            case R.id.make_an_appointment:
                Intent intent = new Intent(MainActivity.this, OrderInputActivity.class);
                startActivity(intent);
                break;
        }
    }
}
