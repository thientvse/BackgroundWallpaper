package com.softforall.backgroundhd;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.UI.PagerSlidingTabStrip;
import com.softforall.backgroundhd.UI.ProgressWheel;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.dcfragment.GlobseOfDayFragment;
import com.softforall.backgroundhd.dcfragment.GlosbeFragment;
import com.softforall.backgroundhd.dcfragment.GridFragment;
import com.softforall.backgroundhd.dcfragment.LibraryFragment;
import com.softforall.backgroundhd.dcfragment.RecentFragment;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.startapp.android.publish.splash.SplashConfig;
import com.startapp.android.publish.splash.SplashConfig.Theme;

public class DiscoveryFragment extends Fragment implements OnPageChangeListener {

	public static DiscoveryFragment newInstance() {
		DiscoveryFragment f = new DiscoveryFragment();
		return f;
	}
	
	private Context mContext;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private LinearLayout mTabsLinearLayout;

	private ImageView btnOpenMenu;
	private ImageView btnSearch;
	
	private TextView title_text;
	
	// ADS
	
	/** StartAppAd object declaration */
	private StartAppAd startAppAd;

	/** StartApp Native Ad declaration */
	private StartAppNativeAd startAppNativeAd;
	private NativeAdDetails nativeAd = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_discovery,
				container, false);
		
		mContext = getActivity();
		
		// AD
		startAppAd = new StartAppAd(mContext);
		startAppNativeAd = new StartAppNativeAd(mContext);
		
		/** Create Splash Ad **/
//		StartAppAd.showSplash(getActivity(), savedInstanceState, new SplashConfig()
//				.setTheme(Theme.SKY).setLogo(R.drawable.header_image)
//				.setAppName(getString(R.string.app_name)));

		/** Add Slider **/
//		StartAppAd.showSlider(getActivity());

		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		pager = (ViewPager) rootView.findViewById(R.id.pager);
		btnOpenMenu = (ImageView) rootView.findViewById(R.id.btn_open);
		btnSearch = (ImageView) rootView.findViewById(R.id.btn_search);
		title_text = (TextView) rootView.findViewById(R.id.title_text);
		adapter = new MyPagerAdapter(getFragmentManager());
		
		title_text.setTypeface(Utils.getTypefaceBlack(mContext), Typeface.BOLD);
		title_text.setText("Khám phá");

		pager.setAdapter(adapter);
		setUpTabStrip();
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);

		tabs.setOnPageChangeListener(this);
		// set gia tri mac dinh
		pager.setCurrentItem(3);
		
		btnOpenMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(mContext, "Click icon", Toast.LENGTH_SHORT).show();
				sendBroadCast();
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent t = new Intent(mContext, ActivitySearchActivity.class);
				t.putExtra("TAGS", "");
				startActivity(t);
				((Activity) mContext).overridePendingTransition(
						R.anim.open_next, R.anim.close_main);
			}
		});
		
		/**
		 * Load Native Ad with the following parameters: 1. Only 1 Ad 2.
		 * Download ad image automatically 3. Image size of 150x150px
		 */
		startAppNativeAd.loadAd(
				new NativeAdPreferences().setAdsNumber(3)
						.setAutoBitmapDownload(true)
						.setImageSize(NativeAdBitmapSize.SIZE340X340),
				nativeAdListener);
		
		return rootView;
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
				nativeAd.sendImpression(getActivity());
			}
		}

		@Override
		public void onFailedToReceiveAd(Ad ad) {

		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		startAppAd.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		startAppAd.onPause();
	}
	
	private void sendBroadCast() {
		Intent intent = new Intent("FROM_MENU");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		private final String[] TITLES = { getString(R.string.str_library),
				getString(R.string.str_recent), getString(R.string.str_favour),
				getString(R.string.str_history) };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return LibraryFragment.newInstance();
			case 1:
				return GridFragment.newInstance();
			case 2:
				return GlosbeFragment.newInstance();
			case 3:
				return GlobseOfDayFragment.newInstance();
			default:
				return LibraryFragment.newInstance();
			}
		}

	}

	public void setUpTabStrip() {

		// your other customizations related to tab strip...blahblah
		// Set first tab selected
		mTabsLinearLayout = ((LinearLayout) tabs.getChildAt(0));
		for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
			TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
			tv.setTypeface(Utils.getTypefaceRegular(getActivity()));
			tv.setTextSize(14L);
			if (i == 0) {
				tv.setTextColor(Color.WHITE);
			} else {
				tv.setTextColor(Color.GRAY);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
			TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
			tv.setTypeface(Utils.getTypefaceRegular(getActivity()));
			tv.setTextSize(14L);
			if (i == position) {
				tv.setTextColor(Color.WHITE);
			} else {
				tv.setTextColor(Color.GRAY);
			}
		}
	}

}
