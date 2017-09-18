package com.navinfo.liuba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TopMenuHeader topMenuHeader = new TopMenuHeader(BaseActivity.this, getTitle().toString());
    }

}
