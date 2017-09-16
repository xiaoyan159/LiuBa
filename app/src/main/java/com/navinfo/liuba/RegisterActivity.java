package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.navinfo.liuba.view.ProvinceAndCity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.edt_register_userName)
    private EditText edt_userName;
    @ViewInject(R.id.edt_register_userPhone)
    private EditText edt_userPhone;
    @ViewInject(R.id.spn_register_province)
    private Spinner spn_userProvince;
    @ViewInject(R.id.spn_register_city)
    private Spinner spn_userCity;
    @ViewInject(R.id.edt_register_userAddress)
    private Spinner edt_address;
    @ViewInject(R.id.edt_register_petNickName)
    private Spinner edt_petNickName;
    @ViewInject(R.id.spn_register_petSex)
    private Spinner edt_petSex;
    @ViewInject(R.id.spn_register_petAge)
    private Spinner edt_petAge;
    @ViewInject(R.id.spn_register_petKind)
    private Spinner edt_petKind;
    @ViewInject(R.id.edt_register_petHabit)
    private Spinner edt_petHabit;
    @ViewInject(R.id.img_register_petImage)
    private Spinner img_petImage;
    @ViewInject(R.id.btn_register_confirm)
    private View btn_register_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_register_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //初始化省市联动控件
        ProvinceAndCity provinceAndCity = new ProvinceAndCity(RegisterActivity.this, spn_userProvince, spn_userCity);
        
    }


}
