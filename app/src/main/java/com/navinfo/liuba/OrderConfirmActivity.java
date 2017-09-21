package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.common.assist.Check;
import com.navinfo.liuba.entity.OrderInfo;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.location.GeoPoint;
import com.navinfo.liuba.location.GeometryTools;
import com.navinfo.liuba.util.LiuBaApplication;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.AddressSuggestPopup;
import com.navinfo.liuba.view.BaseToast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhangdezhi1702 on 2017/9/13.
 */
@ContentView(R.layout.activity_order_confirm)
public class OrderConfirmActivity extends BaseActivity {
    @ViewInject(R.id.layer_order_confirm_confirm)
    private View layer_confirm;//确定按钮
    @ViewInject(R.id.edt_order_confirm_name)
    private EditText edt_userName;//用户名
    @ViewInject(R.id.edt_order_confirm_phone)
    private EditText edt_userPhone;//电话
    @ViewInject(R.id.edt_order_confirm_address)
    private EditText edt_userAddress;//地址
    @ViewInject(R.id.tv_order_confirm_edtAddress)
    private TextView tv_editAddress;//修改地址
    @ViewInject(R.id.edt_order_confirm_orderInfo)
    private EditText edt_oderInfo;//订单详情
    @ViewInject(R.id.edt_order_confirm_feeTotal)
    private EditText edt_feeTotal;//费用合计
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderInfo = (OrderInfo) getIntent().getSerializableExtra(SystemConstant.BUNDLE_ORDER_INFO);

        initView();
    }

    private void initView() {
        //显示用户信息
        RegisterUser userInfo = ((LiuBaApplication) getApplication()).getCurrentUser();
        if (userInfo == null) {
            BaseToast.makeText(OrderConfirmActivity.this, getResources().getString(R.string.relogin_toast), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrderConfirmActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        edt_userName.setText(userInfo.getUserRealName());
        edt_userPhone.setText(userInfo.getUserPhone());
        edt_userAddress.setText(userInfo.getUserAddress());
        edt_userAddress.setTag(userInfo.getLocation());
        orderInfo.setUserId(userInfo.getUserId());
        if (orderInfo != null) {
            StringBuilder orderInfoStr = new StringBuilder();
            orderInfoStr.append("日期:");
            orderInfoStr.append(orderInfo.getAppointDate() + "\n");
            orderInfoStr.append("时间:");
            orderInfoStr.append(orderInfo.getAppointTime() + "\n");
            orderInfoStr.append("时长:");
            orderInfoStr.append(orderInfo.getAppointDuration() + "小时\n");
            orderInfoStr.append("范围:");
            orderInfoStr.append(orderInfo.getAppointScope() + "千米\n");
            edt_oderInfo.setText(orderInfoStr);

            //计算结果
            int orderCost = (int) (orderInfo.getAppointDuration() * 30);
            edt_feeTotal.setText(orderCost + " 元");
            orderInfo.setOrderCost(orderCost);
        }
        //用户点击修改
        tv_editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_userAddress.isEnabled()) {
                    edt_userAddress.setEnabled(true);
                    tv_editAddress.setText("确定");
                } else {
                    if (!Check.isEmpty(edt_userAddress.getText())) {
                        edt_userAddress.setEnabled(false);
                        tv_editAddress.setText("修改");
                        orderInfo.setAppointAddress(edt_userAddress.getText().toString().trim());
                        //获取位置信息
                        GeoPoint currentGeoPoint = null;
                        if (edt_userAddress.getTag(SystemConstant.CURRENT_LOCATION) != null) {
                            currentGeoPoint = (GeoPoint) edt_userAddress.getTag(SystemConstant.CURRENT_LOCATION);
                        } else {
                            currentGeoPoint = ((LiuBaApplication) getApplication()).getCurrentGeoPoint();
                        }
                        if (currentGeoPoint != null) {
                            orderInfo.setLocation(GeometryTools.createGeometry(currentGeoPoint).toString());
                        }
                    }

                }
            }
        });
        //设置在线地址建议
        AddressSuggestPopup.getInstance().addSuggestEditText(OrderConfirmActivity.this, edt_userAddress, "西安市");

        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定
                if (orderInfo != null) {
                    Intent intent = new Intent(OrderConfirmActivity.this, PaymentSelect.class);
                    intent.putExtra(SystemConstant.BUNDLE_ORDER_INFO, orderInfo);
                    startActivity(intent);
                }
            }
        });
    }
}
