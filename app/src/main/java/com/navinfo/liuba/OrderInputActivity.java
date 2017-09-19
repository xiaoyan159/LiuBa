package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.litesuits.common.assist.Check;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ContentView(R.layout.activity_order_input)
public class OrderInputActivity extends BaseActivity {
    @ViewInject(R.id.layer_order_input_confirm)
    private View layer_confirm;//确定按钮
    @ViewInject(R.id.edt_order_input_date)
    private TextView edt_date;//遛狗日期
    @ViewInject(R.id.edt_order_input_time)
    private TextView edt_time;//遛狗时间

    private SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户点击确定
                Intent intent = new Intent(OrderInputActivity.this, OrderConfirmActivity.class);
                startActivity(intent);
            }
        });
        edt_date.setInputType(InputType.TYPE_NULL);
        edt_time.setInputType(InputType.TYPE_NULL);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                if (!Check.isEmpty(edt_date.getText().toString())) {
                    try {
                        Date date = sdf_date.parse(edt_date.getText().toString());
                        currentDate.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(dateSetListener)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))
                        .setDoneText("确定")
                        .setCancelText("取消")
                        .setThemeDark();
                cdp.show(getSupportFragmentManager(), "SelectDate");
            }
        });

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(timeSetListener)
                        .setStartTime(10, 10)
                        .setDoneText("确定")
                        .setCancelText("取消")
                        .setThemeDark();
                rtpd.show(getSupportFragmentManager(), "SelectTime");
            }
        });
    }

    private CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String dateStr = sdf_date.format(calendar.getTime());
            edt_date.setText(dateStr);
        }
    };

    private RadialTimePickerDialogFragment.OnTimeSetListener timeSetListener = new RadialTimePickerDialogFragment.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String dateStr = sdf_time.format(calendar.getTime());
            edt_time.setText(dateStr);
        }
    };
}
