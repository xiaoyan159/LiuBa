package com.navinfo.liuba;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.navinfo.liuba.view.BaseToast;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by zhangdezhi1702 on 2017/9/13.
 */
@ContentView(R.layout.activity_change_user_type)
public class ChangeUserTypeActivity extends BaseActivity {
    @ViewInject(R.id.layerchange_userType_confirm)
    private View layer_confirm;//确定按钮
    @ViewInject(R.id.img_change_user_sZheng)
    private ImageView edt_sZheng;
    @ViewInject(R.id.img_change_user_sFan)
    private ImageView edt_sFan;
    @ViewInject(R.id.img_change_user_yZheng)
    private ImageView edt_yZheng;
    @ViewInject(R.id.img_change_user_zZheng)
    private ImageView edt_zZheng;

    private DialogPlus dialogPlus;

    private int takePhotoState = 0;//0:身份证正面===1：身份证反面===2：银行卡正面===3：资产证明正面

    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePicker();
        imagePicker.setCropImage(true);
        initView();
    }

    private void initView() {

        layer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //注册成功，自动跳回登录界面
//                Intent changeUserTypeOkIntent = new Intent();
//                setResult(RESULT_OK, changeUserTypeOkIntent);
//                ChangeUserTypeActivity.this.finish();
                BaseToast.makeText(ChangeUserTypeActivity.this, "材料审核中，请等待平台联系", Toast.LENGTH_SHORT).show();
            }
        });

        //身份证正面拍照
        edt_sZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoState = 0;
                takePhotoOrPickPhoto();
            }
        });
        //身份证反面拍照
        edt_sFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoState = 1;
                takePhotoOrPickPhoto();
            }
        });
        //银行卡正面
        edt_yZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoState = 2;
                takePhotoOrPickPhoto();
            }
        });
        //资产证明正面
        edt_zZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoState = 3;
                takePhotoOrPickPhoto();
            }
        });
    }

    private void takePhotoOrPickPhoto(){
        //由用户选择是拍照还是从相册选择
        ArrayAdapter<String> pickAdapter = new ArrayAdapter<String>(ChangeUserTypeActivity.this, android.R.layout.select_dialog_item, android.R.id.text1, new String[]{"拍照", "从相册选择"});
        dialogPlus = DialogPlus.newDialog(ChangeUserTypeActivity.this).setAdapter(pickAdapter).setContentHolder(new ListHolder()).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                if (position == 0) {//拍照
                    imagePicker.startCamera(ChangeUserTypeActivity.this, pickImageCallback);
                } else {//从相册选择
                    imagePicker.startGallery(ChangeUserTypeActivity.this, pickImageCallback);
                }
                dialogPlus.dismiss();
            }
        }).create();
        dialogPlus.show();
    }

    private ImagePicker.Callback pickImageCallback = new ImagePicker.Callback() {
        @Override
        public void onPickImage(Uri imageUri) {
        }

        @Override
        public void onCropImage(Uri imageUri) {
            super.onCropImage(imageUri);
            if (takePhotoState==0){
                x.image().bind(edt_sZheng,imageUri.getPath());
            }else if (takePhotoState==1){
                x.image().bind(edt_sFan,imageUri.getPath());
            }else if (takePhotoState==2){
                x.image().bind(edt_yZheng,imageUri.getPath());
            }else if (takePhotoState==3){
                x.image().bind(edt_zZheng,imageUri.getPath());
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
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    // 调整裁剪后的图片最终大小（单位：px）
                    .setRequestedSize(340, 480)
                    // 裁剪框宽高比
                    .setAspectRatio(17, 24);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(ChangeUserTypeActivity.this, requestCode, resultCode, data);
    }
}
