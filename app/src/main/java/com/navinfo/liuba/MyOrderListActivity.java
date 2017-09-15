package com.navinfo.liuba;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shizhefei.view.viewpager.SViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

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
                initWaitViewPage(pagerView);
            } else if (CONFIRM.equals(titles[i])) {//已确认
                pagerView = layoutInflater.inflate(R.layout.layer_order_confirm, null);
                initConfirmViewPage(pagerView);
            } else if (FINISH.equals(titles[i])) {//已完成
                pagerView = layoutInflater.inflate(R.layout.layer_order_finish, null);
                initFinishViewPage(pagerView);
            }
            return pagerView;
        }
    }

    private void initWaitViewPage(View pagerView) {
    }

    private void initConfirmViewPage(View pagerView) {
    }

    private void initFinishViewPage(View pagerView) {
    }
}
