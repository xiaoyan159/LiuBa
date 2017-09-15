package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PaymentSelect extends BaseActivity{
    private View layer_confirm;//确定按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_select);

        initView();
    }

    private void initView(){
        layer_confirm=findViewById(R.id.layer_payment_select_confirm);
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定
                Intent intent=new Intent(PaymentSelect.this,PaymentConfirm.class);
                startActivity(intent);
            }
        });
    }
}
