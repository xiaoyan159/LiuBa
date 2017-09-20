package com.navinfo.liuba;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.litesuits.common.assist.Check;
import com.litesuits.common.io.FileUtils;
import com.navinfo.liuba.entity.BaseResponse;
import com.navinfo.liuba.entity.RegisterRequestEntity;
import com.navinfo.liuba.entity.RegisterUser;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.CheckResult;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.AddressSuggestPopup;
import com.navinfo.liuba.view.BaseToast;
import com.navinfo.liuba.view.LoadingView;
import com.navinfo.liuba.view.ProvinceAndCity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

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
    private EditText edt_address;
    @ViewInject(R.id.edt_register_petNickName)
    private EditText edt_petNickName;
    @ViewInject(R.id.spn_register_petSex)
    private Spinner spn_petSex;
    @ViewInject(R.id.edt_register_petAge)
    private EditText edt_petAge;
    @ViewInject(R.id.spn_register_petKind)
    private Spinner spn_petKind;
    @ViewInject(R.id.edt_register_petHabit)
    private EditText edt_petHabit;
    @ViewInject(R.id.img_register_petImage)
    private ImageView img_petImage;
    @ViewInject(R.id.btn_register_confirm)
    private View btn_register_confirm;

    //    private static final int TAKE_PICTURE = 0x1001;
    private ImagePicker imagePicker;
    private DialogPlus dialogPlus;

    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_register_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckResult result = getRegiestCheckResult();
                if (result.isSuccess()) {
                    //检查通过，开始获取数据
                    final RegisterUser registerUser=getRegiestData();
                    RegisterRequestEntity registerRequestEntity=new RegisterRequestEntity();
                    registerRequestEntity.setData(registerUser);
                    if (registerUser!=null){//开始向服务器请求数据
                        LoadingView.getInstance(RegisterActivity.this).show();
                        BaseRequestParams registerParams=new BaseRequestParams(SystemConstant.userInfoCreate);
                        registerParams.setMultipart(true);
                        registerParams.setParamerJson(JSON.toJSONString(registerRequestEntity));
                        Callback.Cancelable cancelable=x.http().post(registerParams, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                //服务器返回数据成功，记录用户id
                                if (!Check.isEmpty(result)){
                                    BaseResponse<String> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<String>>() {
                                    }.getType());
                                    if (reponse.getErrcode()>=0){//注册成功
                                        //注册成功，自动跳回登录界面
                                        Intent registerOkIntent=new Intent();
                                        registerOkIntent.getExtras().putSerializable(RegisterUser.class.getName(),registerUser);
                                        setResult(RESULT_OK,registerOkIntent);
                                        RegisterActivity.this.finish();
                                    }else {
                                        BaseToast.makeText(RegisterActivity.this, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                BaseToast.makeText(RegisterActivity.this,"出错了，要不再点下试试...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                LoadingView.getInstance(RegisterActivity.this).dismiss();
                            }

                            @Override
                            public void onFinished() {
                                LoadingView.getInstance(RegisterActivity.this).dismiss();
                            }
                        });
                        LoadingView.getInstance(RegisterActivity.this).setCancelable(cancelable);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, result.getDescript(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //初始化省市联动控件
        ProvinceAndCity provinceAndCity = new ProvinceAndCity(RegisterActivity.this, spn_userProvince, spn_userCity);
        //设置性别的可选项
        String[] sexArray = getResources().getStringArray(R.array.sexArray);
        ArrayAdapter sexAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, sexArray);
        sexAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        spn_petSex.setAdapter(sexAdapter);
        spn_petSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置宠物种类的可选项
        String[] kindArray = getResources().getStringArray(R.array.kindArray);
        ArrayAdapter kindAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, kindArray);
        kindAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        spn_petKind.setAdapter(kindAdapter);

        //用户点击拍照
        imagePicker = new ImagePicker();
        imagePicker.setCropImage(true);
        img_petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由用户选择是拍照还是从相册选择
                ArrayAdapter<String> pickAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.select_dialog_item, android.R.id.text1, new String[]{"拍照", "从相册选择"});
                dialogPlus = DialogPlus.newDialog(RegisterActivity.this).setAdapter(pickAdapter).setContentHolder(new ListHolder()).setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        if (position == 0) {//拍照
                            imagePicker.startCamera(RegisterActivity.this, pickImageCallback);
                        } else {//从相册选择
                            imagePicker.startGallery(RegisterActivity.this, pickImageCallback);
                        }
                        dialogPlus.dismiss();
                    }
                }).create();
                dialogPlus.show();
            }
        });

        //设置在线地址建议
        if (provinceAndCity != null && !Check.isEmpty(provinceAndCity.getCityName()))
            AddressSuggestPopup.getInstance().addSuggestEditText(RegisterActivity.this, edt_address, provinceAndCity.getCityName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(RegisterActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(RegisterActivity.this, requestCode, permissions, grantResults);
    }

    private ImagePicker.Callback pickImageCallback = new ImagePicker.Callback() {
        @Override
        public void onPickImage(Uri imageUri) {

        }

        @Override
        public void onCropImage(Uri imageUri) {
            super.onCropImage(imageUri);
            x.image().bind(img_petImage, imageUri.toString());
            try {
                FileUtils.copyFile(new File(imageUri.getPath()), new File(SystemConstant.herderJpgPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults) {
            super.onPermissionDenied(requestCode, permissions, grantResults);
        }

        @Override
        public void cropConfig(CropImage.ActivityBuilder builder) {
            super.cropConfig(builder);
            builder
                    // 是否启动多点触摸
                    .setMultiTouchEnabled(false)
                    // 设置网格显示模式
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    // 圆形/矩形
                    .setCropShape(CropImageView.CropShape.OVAL)
                    // 调整裁剪后的图片最终大小（单位：px）
                    .setRequestedSize(640, 640)
                    // 裁剪框宽高比
                    .setAspectRatio(9, 9);
        }
    };

    private CheckResult getRegiestCheckResult() {
        if (Check.isEmpty(edt_userName.getText())) {
            return new CheckResult(false, "用户姓名不能为空");
        }
        if (Check.isEmpty(edt_userPhone.getText())) {
            return new CheckResult(false, "用户手机不能为空");
        }
        if (Check.isEmpty(edt_address.getText())) {
            return new CheckResult(false, "用户地址不能为空");
        }
        if (Check.isEmpty(edt_petNickName.getText())) {
            return new CheckResult(false, "宠物昵称不能为空");
        }
        if (Check.isEmpty(edt_petAge.getText())) {
            return new CheckResult(false, "宠物昵称不能为空");
        }
        if (img_petImage.getDrawable() == null) {
            return new CheckResult(false, "为宠物拍个照吧!");
        }
        return CheckResult.RESULT_SUCCESS;
    }

    private RegisterUser getRegiestData() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setUserRealName(edt_userName.getText().toString());
        registerUser.setUserPhone(edt_userPhone.getText().toString());
        registerUser.setUserAddress(spn_userProvince.getSelectedItem().toString() + spn_userCity.getSelectedItem().toString() + edt_address.getText().toString());
        registerUser.setPetNickName(edt_petNickName.getText().toString());
        registerUser.setPetSex(spn_petSex.getSelectedItemPosition());
        registerUser.setPetAge(Integer.parseInt(edt_petAge.getText().toString()));
        registerUser.setPetBreed(spn_petKind.getSelectedItem().toString());
        registerUser.setPetHabit(edt_petHabit.getText().toString());
        return registerUser;
    }
}
