package com.navinfo.liuba;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.litesuits.common.assist.Check;

/**
 * Created by xiaoyan on 2017/9/13.
 */

public class TopMenuHeader {

    // 顶部菜单左边按钮
    public TextView topMenuLeft;

    // 顶部菜单右边按钮
    public TextView topMenuRight;

    // 顶部菜单文字
    public TextView topMenuTitle;

    public TopMenuHeader(final Activity activity) {
        initTopMenu(activity);
    }

    public TopMenuHeader(final Activity activity, String titleText) {
        initTopMenu(activity);
        setTitleText(titleText);
    }

    private void initTopMenu(final Activity activity) {
        if (activity != null) {
            // 右边按钮
            topMenuRight = (TextView) activity.findViewById(R.id.tv_title_right);

            // 左边按钮
            topMenuLeft = (TextView) activity.findViewById(R.id.tv_title_left);

            // 顶部中间文字
            topMenuTitle = (TextView) activity.findViewById(R.id.tv_title_text);

            if (topMenuLeft != null) {
                topMenuLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.finish();
                    }
                });
            }
        }
    }

    public void setTitleText(String titleText) {
        if (!Check.isEmpty(titleText) && topMenuTitle != null) {
            topMenuTitle.setText(titleText);
        }
    }

    public void setLeftMenuClickListener(View.OnClickListener listener) {
        if (topMenuLeft != null && listener != null) {
            topMenuLeft.setOnClickListener(listener);
        }
    }

    public void setRightMenuClickListener(View.OnClickListener listener) {
        if (topMenuRight != null && listener != null) {
            topMenuRight.setOnClickListener(listener);
        }
    }

    public void setTopMenuTitleVisibile(int visibile) {
        if (topMenuTitle != null) {
            topMenuTitle.setVisibility(visibile);
        }
    }

    public void setTopMenuLeftVisibile(int visibile) {
        if (topMenuLeft != null) {
            topMenuLeft.setVisibility(visibile);
        }
    }

    public void setTopMenuRightVisibile(int visibile) {
        if (topMenuRight != null) {
            topMenuRight.setVisibility(visibile);
        }
    }
}
