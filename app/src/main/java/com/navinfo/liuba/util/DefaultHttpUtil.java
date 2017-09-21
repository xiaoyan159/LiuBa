package com.navinfo.liuba.util;

import android.content.Context;
import android.widget.Toast;

import com.litesuits.common.assist.Check;
import com.navinfo.liuba.view.BaseToast;
import com.navinfo.liuba.view.LoadingView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by zhangdezhi1702 on 2017/9/20.
 */

public class DefaultHttpUtil {
    public abstract static class HttpCallback {
        public abstract void onSuccess(String result);

        public void onError(Throwable ex, boolean isOnCallback) {
        }
        public void onCancelled(Callback.CancelledException cex) {
        }
        public void onFinished() {
        }
    }

    public static Callback.Cancelable postMethod(final Context mContext, RequestParams params, final HttpCallback callback) {
        LoadingView.getInstance(mContext).show();
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!Check.isEmpty(result)) {
                    callback.onSuccess(result);
                } else {
                    this.onError(new Throwable("服务器开小差了..."), true);
                    this.onFinished();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                BaseToast.makeText(mContext, "出错了，要不再点下试试...", Toast.LENGTH_SHORT).show();
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                LoadingView.getInstance(mContext).dismiss();
                callback.onFinished();
            }
        });
        LoadingView.getInstance(mContext).setCancelable(cancelable);
        return cancelable;
    }
}
