package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PaymentConfirm extends BaseActivity {
    private View layer_confirm;//确定按钮
    private View layer_orderList;//我的订单按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);

        initView();
    }

    private void initView() {
        layer_confirm = findViewById(R.id.layer_my_order_confirm);
        layer_orderList = findViewById(R.id.layer_my_order_list);
        layer_orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击订单按钮
                Intent intent = new Intent(PaymentConfirm.this, MyOrderListActivity.class);
                startActivity(intent);
            }
        });
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定按钮
                Intent intent = new Intent(PaymentConfirm.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
