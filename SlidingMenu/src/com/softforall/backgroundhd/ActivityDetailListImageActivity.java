package com.softforall.backgroundhd;

import java.util.ArrayList;
import java.util.List;

import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.backgroundhd.data.Image_management;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.softforall.backgroundhd.UI.ProgressWheel;
import com.softforall.backgroundhd.Utils.AppConst;
import com.softforall.backgroundhd.Utils.PrefManager;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.adapter.GridViewAdapter;
import com.softforall.backgroundhd.model.ObjectImage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityDetailListImageActivity extends BHActivity implements OnClickListener{

	private Context mContext;
	private Utils utils;
	private GridViewAdapter adapter;
	private GridView gridView;
	private int columnWidth;
	private List<ObjectImage> photosList;
	private ImageView btnBack;
	private TextView title;

	private BackendlessCollection resultCollection;
	private int offset = 0;
	private int pageSize = 10;

	private ObjectImage ObjectImage = new ObjectImage();

	private String typeImage = "";

	private boolean isLoadMore = true;
	private int preLast;
	
	private PrefManager pref;

	Handler mHandler = new Handler();
	Runnable r = new Runnable() {

		@Override
		public void run() {
			progressWheel.setVisibility(View.VISIBLE);
			getData(pageSize, offset, typeImage);
		}
	};

	private ProgressWheel progressWheel;

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
		setContentView(R.layout.activity_detail_list_image);
		
		
		int primary = getResources().getColor(R.color.primaryDark);
        int secondary = getResources().getColor(R.color.red_500);

		mContext = ActivityDetailListImageActivity.this;
		
		photosList = new ArrayList<ObjectImage>();
        pref = new PrefManager(mContext);

		btnBack = (ImageView) findViewById(R.id.btn_back);
		title = (TextView) findViewById(R.id.txt_title);

		progressWheel = (ProgressWheel) findViewById(R.id.progressBar);
		progressWheel.setBarWidth(5);

		// Hiding the gridview and showing loader image before making the http
		// request
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setVisibility(View.VISIBLE);

		utils = new Utils(ActivityDetailListImageActivity.this);
		// Gridview adapter
		adapter = new GridViewAdapter((Activity) mContext, photosList, columnWidth);
		// setting grid view adapter
		gridView.setAdapter(adapter);
		// Grid item select listener
		
		InitilizeGridLayout();

		ObjectImage = getIntent().getParcelableExtra("OBJ");
		typeImage = ObjectImage.getType();

		Log.i("DATA", "Type: " + typeImage);

		getData(pageSize, offset, typeImage);

		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (photosList.size() >= pageSize && isLoadMore) {
					if (lastItem == totalItemCount) {
						if (preLast != lastItem) {
							int last_pos = gridView.getLastVisiblePosition();
							if (last_pos >= photosList.size() - 1) {
								// pageSize++;
								offset = offset + pageSize;
								mHandler.removeCallbacks(r);
								mHandler.postDelayed(r, 200);
							}
							preLast = lastItem;
						}

					}

				}

			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent t = new Intent(mContext, ActivityImageDetailActivity.class);
				t.putExtra("OBJ", photosList.get(position));
				startActivity(t);

			}
		});

		init();

	}

	private void init() {
		title.setText(ObjectImage.getTitle());
		title.setTypeface(Utils.getTypefaceRegular(mContext));
		btnBack.setOnClickListener(this);
	}

	private void getData(int pageIndex, final int offset, String id) {
		Log.i("DATA", "Goi lan: " + pageIndex + " " + offset);
		BackendlessDataQuery query = new BackendlessDataQuery();
		query.setWhereClause("image_type = " + id);
		query.setOffset(offset);
		query.setPageSize(pageIndex);
		Image_management.findAsync(query, new AsyncCallback<BackendlessCollection<Image_management>>() {

			@Override
			public void handleFault(BackendlessFault arg0) {

			}

			@Override
			public void handleResponse(BackendlessCollection<Image_management> reponse) {
				progressWheel.setVisibility(View.GONE);
				if (offset == reponse.getTotalObjects()) {
					isLoadMore = false;
				}
				resultCollection = reponse;
				Log.i("DATA", "reponse: " + reponse.getTotalObjects());
				initList();

			}
		});

	}

	private void initList() {
		// items = new String[ resultCollection.getCurrentPage().size() ];

		if (resultCollection.getCurrentPage().size() == 0) {

		} else {
			for (int i = 0; i < resultCollection.getCurrentPage().size(); i++) {
				ObjectImage objectImage = new ObjectImage();

				String objId = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getObjectId());
				String created = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getCreated());
				String image_title = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getImage_title());
				String image_url_thumb = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getImage_url_thumb());

				String image_count = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getImage_count_view());
				String tag = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getOwnerId());

				objectImage.setImageIcon(image_url_thumb);
				objectImage.setTitle(image_title);
				objectImage.setCountTilte(image_count);
				objectImage.setTag(tag);
				photosList.add(objectImage);

			}

			adapter.notifyDataSetChanged();
		}

	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConst.GRID_PADDING,
				r.getDisplayMetrics());

		// Column width
		columnWidth = (int) ((utils.getScreenWidth() - ((pref.getNoOfGridColumns() + 1) * padding))
				/ pref.getNoOfGridColumns());

		// Setting number of grid columns
		gridView.setNumColumns(pref.getNoOfGridColumns());
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);

		// Setting horizontal and vertical padding
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			closeActivity();
			break;

		default:
			break;
		}
	}

}