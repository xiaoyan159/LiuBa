package com.navinfo.liuba;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.litesuits.common.assist.Check;
import com.navinfo.liuba.entity.OrderInfo;
import com.navinfo.liuba.util.CheckResult;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.BaseToast;

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

    @ViewInject(R.id.edt_order_input_timeLong)
    private TextView spn_time_long;//遛狗时长
    @ViewInject(R.id.edt_order_input_distance)
    private TextView spn_distance;//距离限制

    private SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

    private final String[] TIME_LONG_ARRAY = {"0.5", " 1 ", "1.5", " 2 ", "2.5", " 3 "};
    private final String[] DISTANCE_ARRAY = {"不限", " 1 ", " 2 ", " 3 ", " 4 ", " 5 "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOrderInputCheckResult().isSuccess()) {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo = getOrderInfo(orderInfo);
                    //用户点击确定
                    Intent intent = new Intent(OrderInputActivity.this, OrderConfirmActivity.class);
                    intent.putExtra(SystemConstant.BUNDLE_ORDER_INFO, orderInfo);
                    startActivity(intent);
                }
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
                        .setThemeLight();
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
                        .setThemeLight();
                rtpd.show(getSupportFragmentManager(), "SelectTime");
            }
        });

        spn_time_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter timeLongAdapter = new ArrayAdapter<String>(OrderInputActivity.this,
                        android.R.layout.simple_list_item_1, TIME_LONG_ARRAY);
                AlertDialog.Builder timeLongBuilder = new AlertDialog.Builder(OrderInputActivity.this);
                timeLongBuilder.setTitle("选择时长");
                timeLongBuilder.setAdapter(timeLongAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        spn_time_long.setText(TIME_LONG_ARRAY[i]);
                    }
                });
                timeLongBuilder.show();
            }
        });
        spn_time_long.setText(TIME_LONG_ARRAY[0]);

        spn_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter distanceAdapter = new ArrayAdapter<String>(OrderInputActivity.this,
                        android.R.layout.simple_list_item_1, DISTANCE_ARRAY);
                AlertDialog.Builder distanceBuilder = new AlertDialog.Builder(OrderInputActivity.this);
                distanceBuilder.setTitle("选择距离");
                distanceBuilder.setAdapter(distanceAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        spn_distance.setText(DISTANCE_ARRAY[i]);
                    }
                });
                distanceBuilder.show();
            }
        });
        spn_distance.setText(DISTANCE_ARRAY[0]);
    }

    private CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, monthOfYear);
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
            Date date = new Date();
            if (date.after(calendar.getTime())) {
                BaseToast.makeText(OrderInputActivity.this, "您是想让遛狗师穿越回去吗(￣ェ￣;)", Toast.LENGTH_SHORT).show();
            } else {
                String dateStr = sdf_date.format(calendar.getTime());
                edt_date.setText(dateStr);
            }
        }
    };

    private RadialTimePickerDialogFragment.OnTimeSetListener timeSetListener = new RadialTimePickerDialogFragment.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 59);
            Date date = new Date();
            if (date.after(calendar.getTime())) {
                BaseToast.makeText(OrderInputActivity.this, "您是想让遛狗师穿越回去吗(￣ェ￣;)", Toast.LENGTH_SHORT).show();
            } else {
                String dateStr = sdf_time.format(calendar.getTime());
                edt_time.setText(dateStr);
            }
        }
    };

    private CheckResult getOrderInputCheckResult() {
        if (Check.isEmpty(edt_date.getText())) {
            return new CheckResult(false, "请选择预约日期");
        }
        if (Check.isEmpty(edt_time.getText())) {
            return new CheckResult(false, "请选择开始时间");
        }
        return CheckResult.RESULT_SUCCESS;
    }

    private OrderInfo getOrderInfo(OrderInfo orderInfo) {
        if (orderInfo != null) {
            orderInfo.setAppointDate(edt_date.getText().toString());
            orderInfo.setAppointTime(edt_time.getText().toString());
            orderInfo.setAppointDuration(Double.parseDouble(spn_time_long.getText().toString().trim()));
            orderInfo.setAppointScope(spn_distance.getText().toString().trim());
        }
        return orderInfo;
    }
}
