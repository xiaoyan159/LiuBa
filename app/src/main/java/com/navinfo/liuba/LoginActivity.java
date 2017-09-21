package com.navinfo.liuba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.litesuits.common.assist.Check;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.LiuBaApplication;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.BaseToast;
import com.navinfo.liuba.view.LoadingView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.btn_login_login)
    private TextView loginButton;
    @ViewInject(R.id.btn_login_register)
    private TextView registerButton;
    @ViewInject(R.id.edt_login_user_name)
    private EditText edt_userName;
    @ViewInject(R.id.edt_login_user_phone)
    private EditText edt_phone;
    @ViewInject(R.id.img_login_head)
    private ImageView img_header;

    private SharedPreferences spf_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //首先读取缓存的用户名并显示在当前界面
        spf_config = ((LiuBaApplication) getApplication()).getSpf_config();
        if (spf_config != null) {
            String lastUserName = spf_config.getString(SystemConstant.CONFIG_SPF_USERNAME, null);
            if (!Check.isEmpty(lastUserName)) {
                edt_userName.setText(lastUserName);
                x.image().bind(img_header, SystemConstant.herderJpgDir + lastUserName + ".jpg");
            }
        }

        edt_userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !Check.isEmpty(edt_userName.getText())) {
                    File herderFile = new File(SystemConstant.herderJpgDir + edt_userName.getText() + ".jpg");
                    if (herderFile.exists()) {
                        x.image().bind(img_header, SystemConstant.herderJpgDir + edt_userName.getText() + ".jpg");
                    } else {
                        img_header.setImageDrawable(getResources().getDrawable(R.mipmap.liuba_icon));
                    }
                } else {
                    img_header.setImageDrawable(getResources().getDrawable(R.mipmap.liuba_icon));
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Check.isEmpty(edt_userName.getText())) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空哦...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Check.isEmpty(edt_phone.getText())) {
                    Toast.makeText(LoginActivity.this, "电话不能为空哦...", Toast.LENGTH_SHORT).show();
                    return;
                }
                //请求服务，获取当前该用户信息并校验
                LoadingView.getInstance(LoginActivity.this).show();
                Map<String, String> queryUserInfoMap = new HashMap<String, String>();
                queryUserInfoMap.put("userPhone", edt_phone.getText().toString());

                BaseRequestParams requestParams = new BaseRequestParams(SystemConstant.queryByPhone);
                requestParams.setParamerJson(JSON.toJSONString(queryUserInfoMap));
                Callback.Cancelable cancelable = x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        //请求成功，读取获取到的数据
                        if (!Check.isEmpty(result)) {
                            BaseResponse<RegisterUser> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<RegisterUser>>() {
                            }.getType());
                            if (reponse.getErrcode() >= 0) {//访问成功，服务返回所需结果
                                RegisterUser registerUser = reponse.getData();
                                if (registerUser != null) {
                                    if (edt_userName.getText().toString().equals(registerUser.getUserRealName())) {//登录成功，自动跳转到主界面
                                        //登录成功后自动缓存当前用户的信息
                                        ((LiuBaApplication) getApplication()).setCurrentUser(registerUser);
                                        //在配置文件中记录最后一次用户的登录名
                                        if (spf_config!=null){
                                            spf_config.edit().putString(SystemConstant.CONFIG_SPF_USERNAME,edt_userName.getText().toString()).commit();
                                        }
                                        BaseToast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                                        Intent successIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(successIntent);
                                    } else {//用户名与手机号不匹配
                                        BaseToast.makeText(LoginActivity.this, "用户名与手机号不匹配，请检查后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //自动跳转到注册界面
                                    BaseToast.makeText(LoginActivity.this, "还未注册，请先注册吧...", Toast.LENGTH_SHORT).show();
                                    intentToRegister();
                                }
                            } else {
                                BaseToast.makeText(LoginActivity.this, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        BaseToast.makeText(LoginActivity.this, "出错了，要不再点下试试...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                        LoadingView.getInstance(LoginActivity.this).dismiss();
                    }
                });
                LoadingView.getInstance(LoginActivity.this).setCancelable(cancelable);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToRegister();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x101) {
            if (resultCode == RESULT_OK) {
                //用户注册成功返回
                if (data != null) {
                    RegisterUser registerUser= (RegisterUser) data.getSerializableExtra(SystemConstant.BUNDLE_USER_INFO);
                    if (registerUser != null && !Check.isEmpty(registerUser.getUserRealName())) {
                        edt_userName.setText(registerUser.getUserRealName());
                        if (!Check.isEmpty(registerUser.getUserPhone())) {
                            edt_phone.setText(registerUser.getUserPhone());
                        }
                    }
                }
            }
        } else if (requestCode == 0x102) {
            LoginActivity.this.finish();
        }
    }

    private void intentToRegister() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(registerIntent, 0x101);
    }
}
