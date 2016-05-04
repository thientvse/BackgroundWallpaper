package com.softforall.backgroundhd.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardHelper {
	// close keyboard
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isAcceptingText()) {
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		}
	}
	
	 public static void hideSoftKeyboard(EditText editText, Context context) {
	        InputMethodManager imm = (InputMethodManager) context
	                .getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	    }

	// open softkeyoard
	public static void displaySoftKeyboard(Context context, EditText edt) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			edt.requestFocus();
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					0);
		}
	}

	public static void setupUI(final Activity activity, View view) {
		try {
			if (!(view instanceof EditText)) {
				view.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {
						hideSoftKeyboard(activity);
						return false;
					}

				});
				view.setFocusableInTouchMode(true);
				view.setFocusable(true);
				view.setClickable(true);
			} else {

			}

			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					View innerView = ((ViewGroup) view).getChildAt(i);
					setupUI(activity, innerView);
				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
