package com.navinfo.liuba;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.OrderResponseEntity;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.LiuBaApplication;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.BaseToast;
import com.navinfo.liuba.view.OrderConfirmAdapter;
import com.navinfo.liuba.view.OrderFinishAdapter;
import com.navinfo.liuba.view.OrderWaitAdapter;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shizhefei.view.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangdezhi1702 on 2017/9/14.
 */

public class MyOrderListActivity extends BaseActivity {
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    public static final String WAIT = "待处理";
    public static final String CONFIRM = "已确认";
    public static final String FINISH = "已完成";
    private String[] orderTypeTitles = {WAIT, CONFIRM, FINISH};

    //三个界面分别需要的数据、adapter和listview组件
    private List<OrderResponseEntity> waitList, confirmList, finishList;
    private OrderWaitAdapter waitAdapter;
    private OrderConfirmAdapter confirmAdapter;
    private OrderFinishAdapter finishAdapter;
    private ListView lv_wait, lv_confirm, lv_finish;

    private RegisterUser currentUser;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        currentUser = ((LiuBaApplication) getApplication()).getCurrentUser();

        waitList = new ArrayList<>();
        confirmList = new ArrayList<>();
        finishList = new ArrayList<>();
        waitAdapter = new OrderWaitAdapter(MyOrderListActivity.this, waitList, currentUser);
        confirmAdapter = new OrderConfirmAdapter(MyOrderListActivity.this, confirmList, currentUser);
        finishAdapter = new OrderFinishAdapter(MyOrderListActivity.this, finishList, currentUser);

        SViewPager viewPager = (SViewPager) findViewById(R.id.order_type_viewPager);
        FixedIndicatorView indicator = (FixedIndicatorView) findViewById(R.id.order_type_indicator);
        // 将viewPager和indicator使用
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(MyOrderListActivity.this);
        OrderTypeViewAdapter orderTypeViewAdapter = new OrderTypeViewAdapter(MyOrderListActivity.this, orderTypeTitles);
        indicatorViewPager.setAdapter(orderTypeViewAdapter);

        //设置tab的显示样式
        indicator.setSplitMethod(FixedIndicatorView.SPLITMETHOD_EQUALS);
        indicator.setScrollBar(new ColorBar(MyOrderListActivity.this, getResources().getColor(R.color.colorPrimary), (int) (getResources().getDimension(R.dimen.item_padding_vertical)), ScrollBar.Gravity.BOTTOM));
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColorId(MyOrderListActivity.this, R.color.colorPrimary, R.color.colorPrimaryDisable));
        indicator.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        viewPager.setCanScroll(true);
        indicatorViewPager.setPageOffscreenLimit(orderTypeTitles.length);
        //获取订单数据
        initData();

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEventMainThread(Message msg) {
        if (msg.what == SystemConstant.EVENT_WHAT_REFRESH_ORDER_LIST) {
            int arg1 = msg.arg1;//使用arg1获取当前viewpager自动跳转到哪一页
            String msgStr = (String) msg.obj;//使用obj获取提示信息
            indicatorViewPager.setCurrentItem(arg1, true);
            indicatorViewPager.notifyDataSetChanged();
            //重新获取数据
            initData();
        }else if (msg.what==SystemConstant.EVENT_WHAT_START_TASK){
            //开始遛狗，跳转到主界面
            Intent mainIntent=new Intent(MyOrderListActivity.this,MainActivity.class);
        }
    }

    class OrderTypeViewAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        private String[] titles;
        private LayoutInflater layoutInflater;
        private Context mContext;

        public OrderTypeViewAdapter(Context mContext, String[] titles) {
            this.titles = titles;
            this.mContext = mContext;
            this.layoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public View getViewForTab(int i, View view, ViewGroup viewGroup) {
            View titleView = layoutInflater.inflate(R.layout.tab_text_center, viewGroup, false);
            TextView tvTitle = (TextView) titleView.findViewById(R.id.tv_tab_text);
            tvTitle.setText(titles[i]);
            tvTitle.setTextColor(getResources().getColor(android.R.color.black));
            return titleView;
        }

        @Override
        public View getViewForPage(int i, View view, ViewGroup viewGroup) {
            View pagerView = null;
            if (WAIT.equals(titles[i])) {//待处理
                pagerView = layoutInflater.inflate(R.layout.layer_order_wait, null);
                lv_wait = (ListView) pagerView.findViewById(R.id.lv_order_wait);
                lv_wait.setAdapter(waitAdapter);
                initWaitViewPage(pagerView);
            } else if (CONFIRM.equals(titles[i])) {//已确认
                pagerView = layoutInflater.inflate(R.layout.layer_order_confirm, null);
                lv_confirm = (ListView) pagerView.findViewById(R.id.lv_order_confirm);
                lv_confirm.setAdapter(confirmAdapter);
                initConfirmViewPage(pagerView);
            } else if (FINISH.equals(titles[i])) {//已完成
                pagerView = layoutInflater.inflate(R.layout.layer_order_finish, null);
                lv_finish = (ListView) pagerView.findViewById(R.id.lv_order_finish);
                lv_finish.setAdapter(finishAdapter);
                initFinishViewPage(pagerView);
            }
            return pagerView;
        }
    }

    private void initData() {
        //获取所有的待处理订单
        BaseRequestParams waitParams = new BaseRequestParams(SystemConstant.oederListByStatus);
        Map waitOpParamsMap = new HashMap();
        waitOpParamsMap.put("status", 1);
        waitParams.setParamerJson(JSON.toJSONString(waitOpParamsMap));
        DefaultHttpUtil.postMethod(MyOrderListActivity.this, waitParams, new DefaultHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    BaseResponse<List<OrderResponseEntity>> response = JSON.parseObject(result, new TypeReference<BaseResponse<List<OrderResponseEntity>>>() {
                    }.getType());
                    if (response.getErrcode() >= 0) {//请求并执行成功
                        List<OrderResponseEntity> orderResponseEntityList = response.getData();
                        if (orderResponseEntityList != null && !orderResponseEntityList.isEmpty()) {
                            waitList.clear();
                            waitList.addAll(orderResponseEntityList);
                            waitAdapter.notifyDataSetChanged();
                        }
                    } else {
                        BaseToast.makeText(MyOrderListActivity.this, "出错了，要不再点下试试...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //获取当前用户所有的相关订单
        BaseRequestParams userOrderParams = new BaseRequestParams(SystemConstant.oederListByUser);
        if (currentUser != null) {
            Map userOrderMap = new HashMap();
            if (currentUser.getUserType() == 0) {//宠物主人
                userOrderMap.put("userId", currentUser.getUserId());
                userOrderMap.put("orderClerkId", 0);
            } else {//遛狗师
                userOrderMap.put("orderClerkId", currentUser.getUserId());
                userOrderMap.put("userId", 0);
            }
            userOrderParams.setParamerJson(JSON.toJSONString(userOrderMap));
        }
        DefaultHttpUtil.postMethod(MyOrderListActivity.this, userOrderParams, new DefaultHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    BaseResponse<List<OrderResponseEntity>> response = JSON.parseObject(result, new TypeReference<BaseResponse<List<OrderResponseEntity>>>() {
                    }.getType());
                    if (response.getErrcode() >= 0) {//请求并执行成功
                        //循环遍历与当前用户相关的订单
                        List<OrderResponseEntity> orderResponseEntityList = response.getData();
                        if (orderResponseEntityList != null && !orderResponseEntityList.isEmpty()) {
                            Iterator<OrderResponseEntity> iterator = orderResponseEntityList.iterator();
                            confirmList.clear();
                            finishList.clear();
                            while (iterator.hasNext()) {
                                OrderResponseEntity orderResponseEntity = iterator.next();
                                if (orderResponseEntity.getStatus() == 2 || orderResponseEntity.getStatus() == 3) {//======================================已确认或进行中
                                    confirmList.add(orderResponseEntity);
                                } else if (orderResponseEntity.getStatus() == 4) {//======================================已完成
                                    finishList.add(orderResponseEntity);
                                }
                            }
                            confirmAdapter.notifyDataSetChanged();
                            finishAdapter.notifyDataSetChanged();
                        }
                    } else {
                        BaseToast.makeText(MyOrderListActivity.this, "出错了，要不再点下试试...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void initWaitViewPage(View pagerView) {
    }

    private void initConfirmViewPage(View pagerView) {
    }

    private void initFinishViewPage(View pagerView) {
    }
}
