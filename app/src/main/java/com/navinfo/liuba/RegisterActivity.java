package com.navinfo.liuba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.navinfo.liuba.location.GeoPoint;
import com.navinfo.liuba.location.GeometryTools;
import com.navinfo.liuba.util.BaseRequestParams;
import com.navinfo.liuba.util.CheckResult;
import com.navinfo.liuba.util.DefaultHttpUtil;
import com.navinfo.liuba.util.LiuBaApplication;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.AddressSuggestPopup;
import com.navinfo.liuba.view.BaseToast;
import com.navinfo.liuba.view.ProvinceAndCity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    @ViewInject(R.id.spn_register_userType)
    private Spinner spn_userType;

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
                    final RegisterUser registerUser = getRegiestData();
                    RegisterRequestEntity registerRequestEntity = new RegisterRequestEntity();
                    registerRequestEntity.setData(registerUser);
                    if (registerUser != null) {//开始向服务器请求数据
                        BaseRequestParams registerParams = new BaseRequestParams(SystemConstant.userInfoCreate);
                        registerParams.setMultipart(true);
                        registerParams.setParamerJson(JSON.toJSONString(registerRequestEntity));

                        DefaultHttpUtil.postMethod(RegisterActivity.this, registerParams, new DefaultHttpUtil.HttpCallback() {
                            @Override
                            public void onSuccess(String result) {
                                //服务器返回数据成功，记录用户id
                                if (result != null) {
                                    BaseResponse<String> reponse = JSON.parseObject(result, new TypeReference<BaseResponse<String>>() {
                                    }.getType());
                                    if (reponse.getErrcode() >= 0) {//注册成功
                                        try {
                                            FileUtils.moveFile(new File(SystemConstant.herderJpgPath), new File(SystemConstant.herderJpgDir + registerUser.getUserRealName() + ".jpg"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //注册成功，自动跳回登录界面
                                        Intent registerOkIntent = new Intent();
                                        registerOkIntent.putExtra(SystemConstant.BUNDLE_USER_INFO, registerUser);
                                        setResult(RESULT_OK, registerOkIntent);
                                        RegisterActivity.this.finish();
                                    } else {
                                        BaseToast.makeText(RegisterActivity.this, reponse.getErrmsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
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

        //设置用户身份的可选项
        String[] userTypeArray = getResources().getStringArray(R.array.userType);
        ArrayAdapter userTypeAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, userTypeArray);
        sexAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        spn_userType.setAdapter(userTypeAdapter);

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

            Bitmap cropBitmap = getRoundBitmap(getBitmapFromUri(imageUri), 64);
            if (cropBitmap != null) {
                saveBitmap(imageUri.getPath(), cropBitmap);
            }

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
                    .setRequestedSize(128, 128)
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
        //获取位置信息
        GeoPoint currentGeoPoint = null;
        if (edt_address.getTag(SystemConstant.CURRENT_LOCATION) != null) {
            currentGeoPoint = (GeoPoint) edt_address.getTag(SystemConstant.CURRENT_LOCATION);
        } else {
            currentGeoPoint = ((LiuBaApplication) getApplication()).getCurrentGeoPoint();
        }
        if (currentGeoPoint != null) {
            currentLocation = GeometryTools.createGeometry(currentGeoPoint).toString();
        } else {
            return new CheckResult(false, "获取不到该地址的位置信息!");
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
        registerUser.setUserType(spn_userType.getSelectedItemPosition());

        //获取位置信息
        GeoPoint currentGeoPoint = null;
        if (edt_address.getTag() != null) {
            currentGeoPoint = (GeoPoint) edt_address.getTag();
        } else {
            currentGeoPoint = ((LiuBaApplication) getApplication()).getCurrentGeoPoint();
        }
        if (currentGeoPoint != null) {
            registerUser.setLocation(GeometryTools.createGeometry(currentGeoPoint).toString());
        }
        return registerUser;
    }

    /**

     * 获取裁剪后的圆形图片

     * @param radius 半径

     */
    private Bitmap getRoundBitmap (Bitmap bitmap ,int radius) {
        Bitmap squareBitmap;
        int diameter = radius * 2;//直径
        int x = 0, y = 0;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int squareWidth = 0, squareHeight = 0;
        //以bitmap的短的边的标准，截取一个以bitmap的中心为中心的正方形
        if (bitmapWidth > bitmapHeight) {
            x = bitmapWidth / 2 - bitmapHeight / 2;
            y = 0;
            squareHeight = squareWidth = bitmapHeight;
        } else {
            y = bitmapHeight / 2 - bitmapWidth / 2;
            x = 0;
            squareHeight = squareWidth = bitmapWidth;
        }
        squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareWidth, squareHeight);
        //将squareBitmap缩放成diameter的大小
        squareBitmap = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, false);
        //新建一个bitmap resultbitmap，大小为squareBitmap的大小
        Bitmap resultbitmap = Bitmap.createBitmap(squareBitmap.getWidth(),
                squareBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultbitmap);
        Paint paint = new Paint();
        //新建一个矩形区域位置为0, 0, squareBitmap.getWidth(),squareBitmap.getHeight()
        Rect rect = new Rect(0, 0, squareBitmap.getWidth(),squareBitmap.getHeight());
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setFilterBitmap(true);//对位图进行滤波处理
        paint.setDither(true);//设置防抖动
        canvas.drawARGB(0, 0, 0, 0);//画背景颜色为透明
        //以（squareBitmap.getWidth() / 2,squareBitmap.getHeight() / 2）为圆心，squareBitmap.getWidth() / 2为半径画圆
        canvas.drawCircle(squareBitmap.getWidth() / 2,
                squareBitmap.getHeight() / 2,
                squareBitmap.getWidth() / 2,
                paint);
        //精髓，因为这句话，canvas原先画的透明背景圆和接下来的squareBitmap重叠部分显示出来
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //在画布中将squareBitmap画出来
        canvas.drawBitmap(squareBitmap, rect, rect, paint);
        bitmap = null ;
        squareBitmap = null;
        //将画布中的内容返回
        return resultbitmap;
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveBitmap(String bitName, Bitmap mBitmap) {
        File f = new File(bitName);
        try {
            f.createNewFile();
        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
