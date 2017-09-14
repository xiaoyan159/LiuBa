package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OrderInputActivity extends BaseActivity {

    private View layer_confirm;//确定按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_input);

        initView();
    }

    private void initView(){
        layer_confirm=findViewById(R.id.layer_order_input_confirm);
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定
                Intent intent=new Intent(OrderInputActivity.this,OrderConfirmActivity.class);
                startActivity(intent);
            }
        });
    }
}
