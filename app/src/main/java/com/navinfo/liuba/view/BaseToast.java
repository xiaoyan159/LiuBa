package com.navinfo.liuba.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.android.log.Log;
import com.navinfo.liuba.R;

public class BaseToast extends Toast {
	final Context mContext;
	
	private static int height;
	static Toast result;
	public BaseToast(Context context) {
		super(context);
		mContext = context;
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		height = dm.heightPixels;
	}

	public static Toast makeText(Context context, CharSequence text,
                                 int duration) {
		try{
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			height = dm.heightPixels;
			result =result==null? new Toast(context):result;
			LayoutInflater inflate = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflate.inflate(R.layout.transient_notification, null);
			TextView tv = (TextView) v.findViewById(android.R.id.message);
			tv.setText(text);
			result.setView(v);
			duration = Toast.LENGTH_SHORT;
			result.setDuration(duration);
			result.setGravity(Gravity.CENTER, 0, height/6);
		}catch (Exception e){
			Log.w("makeText","异常",e.toString());
		}

		return result;
	}

	public static Toast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId),
				duration);
	}

//	public void setText(int resId) {
//		setText(mContext.getText(resId));
//	}

	/**
	 * Update the text in a Toast that was previously created using one of the
	 * makeText() methods.
	 * 
	 * @param s
	 *            The new text for the Toast.
	 */
//	public void setText(CharSequence s) {
//		if (mNextView == null) {
//			throw new RuntimeException(
//					"This Toast was not created with Toast.makeText()");
//		}
//		TextView tv = (TextView) mNextView
//				.findViewById(com.android.internal.R.id.message);
//		if (tv == null) {
//			throw new RuntimeException(
//					"This Toast was not created with Toast.makeText()");
//		}
//		tv.setText(s);
//	}

}

