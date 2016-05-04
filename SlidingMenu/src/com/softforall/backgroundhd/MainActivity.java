package com.softforall.backgroundhd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.adapter.NavDrawerListAdapter;
import com.softforall.backgroundhd.model.NavDrawerItem;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.StartAppNativeAd;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private Context mContext;

	private TextView titleHeader;

	Fragment frag = null;
	private TextView mTitleTextView;
	private ImageView btnOpenMenu;

	private Dialog dPoint;

	// ADS

	/** StartAppAd object declaration */
	private StartAppAd startAppAd;

	/** StartApp Native Ad declaration */
	private StartAppNativeAd startAppNativeAd;
	private NativeAdDetails nativeAd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		mContext = MainActivity.this;

		// AD
		startAppAd = new StartAppAd(mContext);
		startAppNativeAd = new StartAppNativeAd(mContext);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.header_listview, mDrawerList,
				false);
		titleHeader = (TextView) header.findViewById(R.id.name);
		titleHeader.setTypeface(Utils.getTypefaceRegular(mContext));
		mDrawerList.addHeaderView(header, null, false);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
//				.getResourceId(1, -1)));
//		// Photos
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
//				.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// What's hot, We will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1), true, "50+"));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		// getActionBar().setDisplayUseLogoEnabled(false);
		// getActionBar().setDisplayShowHomeEnabled(false);
		// getActionBar().setDisplayHomeAsUpEnabled(false);
		// getActionBar().setHomeButtonEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		btnOpenMenu = (ImageView) mCustomView.findViewById(R.id.btn_open);
		mTitleTextView.setText("Khám phá");
		getActionBar().hide();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_menu, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				getActionBar()/* or getSupportActionBar() */.setTitle(
						Html.fromHtml("<font color=\"#ffffff\">" + mTitle
								+ "</font>"));
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				getActionBar()/* or getSupportActionBar() */.setTitle(
						Html.fromHtml("<font color=\"#ffffff\">" + mTitle
								+ "</font>"));
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(1);
		}

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Log.i("DATA", "Pos click: "+position);
			// Toast.makeText(mContext, "Pos: "+position,
			// Toast.LENGTH_SHORT).show();
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;

		// Log.i("DATA", "Fragment: "+position);
		switch (position - 1) {
		case 0:
			fragment = new DiscoveryFragment();
			break;
//		case 1:
//			fragment = new FavouriteFragment();
//			break;
//		case 2:
//			fragment = new HistoryFragment();
//			break;
		case 1:
			mDrawerList.setItemChecked(1, true);
			mDrawerList.setSelection(1);
			mDrawerLayout.closeDrawer(mDrawerList);
			showCustomPointdialog();
			break;
		case 2:
			mDrawerList.setItemChecked(1, true);
			mDrawerList.setSelection(1);
			mDrawerLayout.closeDrawer(mDrawerList);
			sendEmail();
			break;
		case 3:
			mDrawerList.setItemChecked(1, true);
			mDrawerList.setSelection(1);
			mDrawerLayout.closeDrawer(mDrawerList);
			gotoSettings();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// android.support.v4.app.FragmentTransaction fragmentTransaction =
			// MainActivity.this.getSupportLoaderManager().be
			// fragmentTransaction.replace(R.id.frame_container, frag);
			// fragmentTransaction.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position - 1]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	private void gotoSettings() {
		Intent t = new Intent(mContext, ActivitySettingsActivity.class);
		startActivity(t);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getActionBar().setTitle(mTitle);
		getActionBar()/* or getSupportActionBar() */.setTitle(
				Html.fromHtml("<font color=\"#ffffff\">" + mTitle + "</font>"));

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurwhite.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// receiver khi click tran thai tai trang chu
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	};

	@Override
	protected void onResume() {
		LocalBroadcastManager.getInstance(mContext).registerReceiver(
				mMessageReceiver, new IntentFilter("FROM_MENU"));

		super.onResume();

		startAppAd.onResume();
	}

	@Override
	public void onPause() {
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
				mMessageReceiver);
		super.onPause();
		startAppAd.onPause();
	}

	// show dialog
	private void showCustomPointdialog() {

		dPoint = new Dialog(MainActivity.this);
		dPoint.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dPoint.setContentView(R.layout.dialog_point);

		Button btnCancel = (Button) dPoint.findViewById(R.id.btn_cancel);
		Button btnConfirm = (Button) dPoint.findViewById(R.id.btn_confirm);

		TextView txtTitle = (TextView) dPoint.findViewById(R.id.txt_title);
		TextView txtContent = (TextView) dPoint.findViewById(R.id.txt_content);

		txtTitle.setTypeface(Utils.getTypefaceBlack(mContext), Typeface.BOLD);
		txtContent.setTypeface(Utils.getTypefaceRegular(mContext),
				Typeface.BOLD);
		txtContent.setTextColor(getResources()
				.getColor(R.color.color_gray_less));
		btnCancel.setTypeface(Utils.getTypefaceBold(mContext), Typeface.BOLD);
		btnConfirm.setTypeface(Utils.getTypefaceBold(mContext), Typeface.BOLD);

		dPoint.show();
		dPoint.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dPoint.setCanceledOnTouchOutside(true);
		dPoint.getWindow().setGravity(Gravity.CENTER);
		dPoint.show();

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dPoint.dismiss();
			}
		});

		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dPoint.dismiss();
			}
		});

	}

	// send email
	private void sendEmail() {
		String address = "thientvse@gmail.com";/* Your email address here */
		String subject = "[v/v Background HD]"; /* Your subject here */
		String body = getString(R.string.request);/* Your body here */
		String chooserTitle = ""; /* Your chooser title here */

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
				Uri.parse("mailto:" + address));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, body);
		// emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are
		// using HTML in your body text

		startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
	}

	private void sendIntent() {
		String[] nameOfAppsToShareWith = new String[] { "facebook", "twitter",
				"gmail" };
		String[] blacklist = new String[] { "com.any.package",
				"net.other.package" };
		// your share intent
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.request));
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[BackgroundHD]");
		// ... anything else you want to add invoke custom chooser
		startActivity(generateCustomChooserIntent(intent, blacklist));
	}

	private Intent generateCustomChooserIntent(Intent prototype,
			String[] forbiddenChoices) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
		Intent chooserIntent;

		Intent dummy = new Intent(prototype.getAction());
		dummy.setType(prototype.getType());
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
				dummy, 0);

		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				if (resolveInfo.activityInfo == null
						|| Arrays.asList(forbiddenChoices).contains(
								resolveInfo.activityInfo.packageName))
					continue;
				// Get all the posible sharers
				HashMap<String, String> info = new HashMap<String, String>();
				info.put("packageName", resolveInfo.activityInfo.packageName);
				info.put("className", resolveInfo.activityInfo.name);
				String appName = String.valueOf(resolveInfo.activityInfo
						.loadLabel(getPackageManager()));
				info.put("simpleName", appName);
				// Add only what we want
				if (Arrays.asList("BackgroundHD").contains(
						appName.toLowerCase())) {
					intentMetaInfo.add(info);
				}
			}

			if (!intentMetaInfo.isEmpty()) {
				// sorting for nice readability
				Collections.sort(intentMetaInfo,
						new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> map,
									HashMap<String, String> map2) {
								return map.get("simpleName").compareTo(
										map2.get("simpleName"));
							}
						});

				// create the custom intent list
				for (HashMap<String, String> metaInfo : intentMetaInfo) {
					Intent targetedShareIntent = (Intent) prototype.clone();
					targetedShareIntent.setPackage(metaInfo.get("packageName"));
					targetedShareIntent.setClassName(
							metaInfo.get("packageName"),
							metaInfo.get("className"));
					targetedShareIntents.add(targetedShareIntent);
				}
				String shareVia = getString(R.string.offer_share_via);
				String shareTitle = shareVia.substring(0, 1).toUpperCase()
						+ shareVia.substring(1);
				chooserIntent = Intent.createChooser(targetedShareIntents
						.remove(targetedShareIntents.size() - 1), shareTitle);
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				return chooserIntent;
			}
		}

		return Intent.createChooser(prototype,
				getString(R.string.offer_share_via));
	}

	@Override
	public void onBackPressed() {
		startAppAd.onBackPressed();
		super.onBackPressed();
	}

}
