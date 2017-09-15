package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by zhangdezhi1702 on 2017/9/13.
 */

public class OrderConfirmActivity extends BaseActivity{
    private View layer_confirm;//确定按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        initView();
    }

    private void initView(){
        layer_confirm=findViewById(R.id.layer_order_confirm_confirm);
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定
                Intent intent=new Intent(OrderConfirmActivity.this,MyOrderListActivity.class);
                startActivity(intent);
            }
        });
    }
}
