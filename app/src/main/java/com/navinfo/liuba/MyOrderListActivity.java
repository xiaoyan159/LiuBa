package com.navinfo.liuba;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * Created by zhangdezhi1702 on 2017/9/14.
 */

public class MyOrderListActivity extends BaseActivity{
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        ViewPager viewPager = (ViewPager) findViewById(R.id.order_type_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.order_type_indicator);
        // 将viewPager和indicator使用
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        // 设置indicatorViewPager的适配器
//        indicatorViewPager.setAdapter(adapter);
    }


}
