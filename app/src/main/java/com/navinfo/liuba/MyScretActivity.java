package com.navinfo.liuba;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhangdezhi1702 on 2017/9/15.
 */

public class MyScretActivity extends BaseActivity {
    private TextView tv_secret_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_secret);

        tv_secret_send = (TextView) findViewById(R.id.tv_secret_send);
        tv_secret_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyScretActivity.this.finish();
                Toast.makeText(MyScretActivity.this, "发送密码", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
