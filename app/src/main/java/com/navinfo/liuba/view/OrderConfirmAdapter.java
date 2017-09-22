package com.navinfo.liuba.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.navinfo.liuba.MainActivity;
import com.navinfo.liuba.MyScretActivity;
import com.navinfo.liuba.R;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.OrderResponseEntity;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.SystemConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderConfirmAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderResponseEntity> orderList;
    private RegisterUser currentUser;
    private LayoutInflater layoutInflater;

    public OrderConfirmAdapter(Context mContext, List<OrderResponseEntity> orderList, RegisterUser currentUser) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.currentUser = currentUser;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_order_layer, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_orderId = (TextView) view.findViewById(R.id.tv_order_item_orderId);
            viewHolder.tv_userName = (TextView) view.findViewById(R.id.tv_order_item_userName);
            viewHolder.tv_userPhone = (TextView) view.findViewById(R.id.tv_order_item_userPhone);
            viewHolder.tv_userAddress = (TextView) view.findViewById(R.id.tv_order_item_userAddress);
            viewHolder.tv_orderDate = (TextView) view.findViewById(R.id.tv_order_item_date);
            viewHolder.tv_orderTime = (TextView) view.findViewById(R.id.tv_order_item_time);
            viewHolder.tv_orderTimeLone = (TextView) view.findViewById(R.id.tv_order_item_timelone);
            viewHolder.tv_orderDistance = (TextView) view.findViewById(R.id.tv_order_item_distance);
            viewHolder.tv_orderFee = (TextView) view.findViewById(R.id.tv_order_item_fee);
            viewHolder.tv_operate = (TextView) view.findViewById(R.id.tv_order_item_operate);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final OrderResponseEntity order = orderList.get(i);
        viewHolder.tv_orderId.setText(order.getOrderId() + "");
        viewHolder.tv_userName.setText(currentUser.getUserRealName());
        viewHolder.tv_userPhone.setText(currentUser.getUserPhone());
        viewHolder.tv_userAddress.setText(currentUser.getUserAddress());

        viewHolder.tv_orderDate.setText(order.getAppointDate());
        viewHolder.tv_orderTime.setText(order.getAppointTime());
        viewHolder.tv_orderTimeLone.setText(order.getAppointDuration());
        viewHolder.tv_orderDistance.setText(order.getAppointScope());
        viewHolder.tv_orderFee.setText(order.getOrderCost() + "");

        //判断当前用户的身份，如果是遛狗师，显示右侧的操作按钮，并且可以通过点击该按钮开始订单
        if (currentUser.getUserType() == 1) {
            viewHolder.tv_operate.setText("溜吧");
            viewHolder.tv_operate.setVisibility(View.VISIBLE);
            viewHolder.tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //遛狗师接单，点击后更新当前订单状态
                    BaseRequestParams updateParams = new BaseRequestParams(SystemConstant.startOrder);
                    Map updateMap = new HashMap();
                    updateMap.put("orderId", order.getOrderId());
                    updateParams.setParamerJson(JSON.toJSONString(updateMap));
                    DefaultHttpUtil.postMethod(mContext, updateParams, new DefaultHttpUtil.HttpCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result != null) {
                                BaseResponse<String> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<String>>() {
                                }.getType());
                                if (reponse.getErrcode() >= 0) {//执行成功，通知界面刷新
                                    Intent startIntent = new Intent(mContext, MainActivity.class);
                                    startIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                    startIntent.putExtra(SystemConstant.BUNDLE_ORDER_INFO,order);
                                    startIntent.putExtra(SystemConstant.ORDER_ACTION, 1);
                                    mContext.startActivity(startIntent);
                                } else {
                                    BaseToast.makeText(mContext, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            });
        } else {
            viewHolder.tv_operate.setText("密码锁");
            if (order.getOrderClerkId() != 0) {
                viewHolder.tv_operate.setVisibility(View.VISIBLE);
                viewHolder.tv_operate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //直接跳转到密码锁界面
                        Intent screateIntent = new Intent(mContext, MyScretActivity.class);
                        mContext.startActivity(screateIntent);
                    }
                });
            }
        }

        return view;
    }

    private class ViewHolder {
        TextView tv_orderId;
        TextView tv_userName;
        TextView tv_userPhone;
        TextView tv_userAddress;
        TextView tv_orderDate;
        TextView tv_orderTime;
        TextView tv_orderTimeLone;
        TextView tv_orderDistance;
        TextView tv_orderFee;
        TextView tv_operate;
    }
}
