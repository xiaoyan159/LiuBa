package com.navinfo.liuba.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.xutils.common.Callback;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class LoadingView {
    private static LoadingView instance;
    private ProgressBar pb;
    private DialogPlus dp;

    private Callback.Cancelable cancelable;

    private LoadingView(Context context) {
        pb = new ProgressBar(context);
        dp = DialogPlus.newDialog(context).setContentHolder(new ViewHolder(pb)).setContentBackgroundResource(android.R.color.transparent).setGravity(Gravity.CENTER)/*.setMargin(100,0,100,0)*/.create();
    }

    public static LoadingView getInstance(Context app) {
        if (instance == null) {
            instance = new LoadingView(app);
        }
        return instance;
    }

    public void show() {
        if (dp != null && !dp.isShowing()) {
            dp.show();
        }
    }

    public void dismiss() {
        if (dp != null && dp.isShowing()) {
            dp.dismiss();
        }
        if (cancelable != null && !cancelable.isCancelled()) {
            cancelable.cancel();
        }
    }

    public void setCancelable(Callback.Cancelable cancelable) {
        this.cancelable = cancelable;
    }
}
