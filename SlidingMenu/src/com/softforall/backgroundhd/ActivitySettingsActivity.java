package com.softforall.backgroundhd;

import java.util.ArrayList;

import com.softforall.backgroundhd.Utils.AppConst;
import com.softforall.backgroundhd.Utils.Utils;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CheckBox;

public class ActivitySettingsActivity extends BHActivity implements
		OnClickListener {

	private Context mContext;

	private TextView titleText;
	private ImageView btnBack;
	private TextView txtTitle;
	private TextView txtTongquan;
	private LinearLayout xoaCache;
	private TextView txtTitleXoaCache;
	private TextView txtContentXoaCache;
	private LinearLayout nhanThonbao;
	private TextView txtNhanThonbao;
	private TextView txtContentNhanThonbao;
	private TextView txtName;
	private LinearLayout phienban;
	private TextView txtPhienban;
	private TextView valueVersion;

	private TextView txtGioithieu;
	private TextView txtGioithieuFriend;

	// ADS

	/** StartAppAd object declaration */
	private StartAppAd startAppAd;

	/** StartApp Native Ad declaration */
	private StartAppNativeAd startAppNativeAd;
	private NativeAdDetails nativeAd = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
		setContentView(R.layout.activity_settings);

		mContext = ActivitySettingsActivity.this;

		// AD
		startAppAd = new StartAppAd(mContext);
		startAppNativeAd = new StartAppNativeAd(mContext);

		titleText = (TextView) findViewById(R.id.txt_title);
		btnBack = (ImageView) findViewById(R.id.btn_back);
		txtTongquan = (TextView) findViewById(R.id.txt_tongquan);
		xoaCache = (LinearLayout) findViewById(R.id.xoa_cache);
		txtTitleXoaCache = (TextView) findViewById(R.id.txt_title_xoa_cache);
		txtContentXoaCache = (TextView) findViewById(R.id.txt_content_xoa_cache);
		nhanThonbao = (LinearLayout) findViewById(R.id.nhan_thonbao);
		txtNhanThonbao = (TextView) findViewById(R.id.txt_nhan_thonbao);
		txtContentNhanThonbao = (TextView) findViewById(R.id.txt_content_nhan_thonbao);
		txtName = (TextView) findViewById(R.id.txt_name);
		phienban = (LinearLayout) findViewById(R.id.phienban);
		txtPhienban = (TextView) findViewById(R.id.txt_phienban);
		valueVersion = (TextView) findViewById(R.id.value_version);

		txtGioithieu = (TextView) findViewById(R.id.txt_gui_phan_hoi);
		txtGioithieuFriend = (TextView) findViewById(R.id.content_gui_phanhoi);

		init();

		startAppNativeAd.loadAd(
				new NativeAdPreferences().setAdsNumber(3)
						.setAutoBitmapDownload(true)
						.setImageSize(NativeAdBitmapSize.SIZE340X340),
				nativeAdListener);
	}

	/** Native Ad Callback */
	private AdEventListener nativeAdListener = new AdEventListener() {

		@Override
		public void onReceiveAd(Ad ad) {

			// Get the native ad
			ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd
					.getNativeAds();
			if (nativeAdsList.size() > 0) {
				nativeAd = nativeAdsList.get(0);
			}

			// Verify that an ad was retrieved
			if (nativeAd != null) {

				// When ad is received and displayed - we MUST send impression
				nativeAd.sendImpression(ActivitySettingsActivity.this);
			}
		}

		@Override
		public void onFailedToReceiveAd(Ad ad) {

		}
	};

	private void init() {
		btnBack.setOnClickListener(this);
		titleText.setTypeface(Utils.getTypefaceBlack(mContext), Typeface.BOLD);

		txtTongquan.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtTitleXoaCache.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtContentXoaCache.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtNhanThonbao.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtContentNhanThonbao.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtName.setTypeface(Utils.getTypefaceMedium(mContext), Typeface.BOLD);
		txtPhienban.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		valueVersion.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);

		txtGioithieu.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);
		txtGioithieuFriend.setTypeface(Utils.getTypefaceMedium(mContext),
				Typeface.BOLD);

		nhanThonbao.setOnClickListener(this);

	}

	private CheckBox getChecbox() {
		return (CheckBox) findViewById(R.id.checbox);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			closeActivity();
			break;
		case R.id.nhan_thonbao:
			if (getChecbox().isChecked()) {
				getChecbox().setChecked(false);
				AppConst.REVEICE_NOTIFY = 0;
			} else {
				getChecbox().setChecked(true);
				AppConst.REVEICE_NOTIFY = 1;
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

		startAppAd.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		startAppAd.onPause();
	}
}