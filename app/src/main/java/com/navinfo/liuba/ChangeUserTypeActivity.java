package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhangdezhi1702 on 2017/9/13.
 */
@ContentView(R.layout.activity_change_user_type)
public class ChangeUserTypeActivity extends BaseActivity {
    @ViewInject(R.id.layerchange_userType_confirm)
    private View layer_confirm;//确定按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册成功，自动跳回登录界面
                Intent changeUserTypeOkIntent = new Intent();
                setResult(RESULT_OK, changeUserTypeOkIntent);
                ChangeUserTypeActivity.this.finish();
            }
        });
    }
}
