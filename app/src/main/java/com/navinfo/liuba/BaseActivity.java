package com.navinfo.liuba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TopMenuHeader topMenuHeader = new TopMenuHeader(BaseActivity.this, getTitle().toString());
    }

}
