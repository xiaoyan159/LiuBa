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

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.litesuits.common.io.FileUtils;
import com.navinfo.liuba.util.SystemConstant;
import com.navinfo.liuba.view.ProvinceAndCity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(RegisterActivity.this, requestCode, resultCode, data);
//        if (requestCode == TAKE_PICTURE) {
//            if (resultCode == RESULT_OK) {
//                Bitmap bm = (Bitmap) data.getExtras().get("data");
//                img_petImage.setImageBitmap(bm);//想图像显示在ImageView视图上，private ImageView img;
//                File myCaptureFile = new File(SystemConstant.rootPath + SystemConstant.herderJpgPath);
//                try {
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//                    /* 采用压缩转档方法 */
//                    bm.compress(Bitmap.CompressFormat.JPEG, 85, bos);
//                    /* 调用flush()方法，更新BufferStream */
//                    bos.flush();
//                    /* 结束OutputStream */
//                    bos.close();
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
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
}
