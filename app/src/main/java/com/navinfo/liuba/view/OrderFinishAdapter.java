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
import com.navinfo.liuba.R;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.OrderResponseEntity;
import com.navinfo.liuba.entity.OrderTrackEntity;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.SystemConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class OrderFinishAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderResponseEntity> orderList;
    private RegisterUser currentUser;
    private LayoutInflater layoutInflater;

    public OrderFinishAdapter(Context mContext, List<OrderResponseEntity> orderList, RegisterUser currentUser) {
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
            viewHolder.layer_orderInfo = view.findViewById(R.id.layer_order_item_info);
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
        viewHolder.layer_orderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击某个订单，查询该订单对应的轨迹数据
                BaseRequestParams listParams = new BaseRequestParams(SystemConstant.orderTrackList);
                Map listMap = new HashMap();
                listMap.put("orderId", order.getOrderId());
                listParams.setParamerJson(JSON.toJSONString(listMap));
                DefaultHttpUtil.postMethod(mContext, listParams, new DefaultHttpUtil.HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            BaseResponse<ArrayList<OrderTrackEntity>> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<ArrayList<OrderTrackEntity>>>() {
                            }.getType());
                            if (reponse.getErrcode() >= 0) {//执行成功，跳转到主界面查看轨迹
                                ArrayList<OrderTrackEntity> orderTrackEntityList = reponse.getData();
                                if (orderTrackEntityList != null && !orderTrackEntityList.isEmpty()) {
                                    Intent mainIntent = new Intent(mContext, MainActivity.class);
                                    mainIntent.putExtra(SystemConstant.BUNDLE_ORDER_INFO, order);
                                    mainIntent.putExtra(SystemConstant.BUNDLE_TRACK_LIST, orderTrackEntityList);
                                    mainIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                    mainIntent.putExtra(SystemConstant.ORDER_ACTION, 2);
                                    mContext.startActivity(mainIntent);
                                } else {
                                    BaseToast.makeText(mContext, "这个订单没有轨迹呢...", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                BaseToast.makeText(mContext, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

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
        View layer_orderInfo;
    }
}
