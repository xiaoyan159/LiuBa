package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.OrderInfo;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.BaseToast;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PaymentSelect extends BaseActivity {
    private View layer_confirm;//确定按钮

    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_select);

        orderInfo = (OrderInfo) getIntent().getSerializableExtra(SystemConstant.BUNDLE_ORDER_INFO);

        initView();

    }

    private void initView() {
        layer_confirm = findViewById(R.id.layer_payment_select_confirm);
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderInfo != null) {
                    BaseRequestParams requestParams = new BaseRequestParams(SystemConstant.oederCreate);
                    requestParams.setParamerJson(JSON.toJSONString(orderInfo));
                    DefaultHttpUtil.postMethod(PaymentSelect.this, requestParams, new DefaultHttpUtil.HttpCallback() {

                        @Override
                        public void onSuccess(String result) {
                            if (result != null) {
                                BaseResponse<String> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<String>>() {
                                }.getType());
                                if (reponse.getErrcode() >= 0) {//订单创建成功
                                    //跳转到支付成功界面
                                    BaseToast.makeText(PaymentSelect.this, "订单创建成功，已通知附近遛狗师接单", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentSelect.this, PaymentConfirm.class);
                                    startActivity(intent);
                                } else {
                                    BaseToast.makeText(PaymentSelect.this, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });
    }
}
