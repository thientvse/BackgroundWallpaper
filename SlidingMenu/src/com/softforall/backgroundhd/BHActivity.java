package com.softforall.backgroundhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class BHActivity extends Activity {
	private ProgressDialog mProgressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}



	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	protected void showAlert(String msg) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.app_name))
				.setMessage(msg).setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				}).create().show();
	}
	
	protected void closeActivity() {
		finish();
//		overridePendingTransition(0, R.anim.trans_right_out);
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}

	@Override
	public void onBackPressed() {
		closeActivity();
	}

}