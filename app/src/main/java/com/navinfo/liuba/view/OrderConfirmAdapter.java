package com.navinfo.liuba.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.navinfo.liuba.R;
import com.navinfo.liuba.entity.OrderResponseEntity;
import com.navinfo.liuba.entity.RegisterUser;

import java.util.List;

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

        OrderResponseEntity order = orderList.get(i);
        viewHolder.tv_orderId.setText(order.getOrderId()+"");
        viewHolder.tv_userName.setText(currentUser.getUserRealName());
        viewHolder.tv_userPhone.setText(currentUser.getUserPhone());
        viewHolder.tv_userAddress.setText(currentUser.getUserAddress());

        viewHolder.tv_orderDate.setText(order.getAppointDate());
        viewHolder.tv_orderTime.setText(order.getAppointTime());
        viewHolder.tv_orderTimeLone.setText(order.getAppointDuration());
        viewHolder.tv_orderDistance.setText(order.getAppointScope());
        viewHolder.tv_orderFee.setText(order.getOrderCost() + "");

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
