package com.softforall.backgroundhd;

import java.util.ArrayList;
import java.util.List;

import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.backgroundhd.data.Image_management;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.softforall.backgroundhd.UI.ProgressWheel;
import com.softforall.backgroundhd.Utils.KeyboardHelper;
import com.softforall.backgroundhd.Utils.PrefManager;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.adapter.GridViewAdapter;
import com.softforall.backgroundhd.model.ObjectImage;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

public class ActivitySearchActivity extends BHActivity implements OnClickListener{
	
	private Context mContext;

    private TextView titleText;
    private ImageView btnBack;
    private ImageView btnSearch;
    
    
	private Utils utils;
	private GridViewAdapter adapter;
	private GridView gridView;
	private int columnWidth;
	private List<ObjectImage> photosList;
	private TextView title;

	private BackendlessCollection resultCollection;
	private int offset = 0;
	private int pageSize = 10;

	private ObjectImage ObjectImage = new ObjectImage();

	private String  tagImage= "";

	private boolean isLoadMore = true;
	private int preLast;
	
	private PrefManager pref;

	Handler mHandler = new Handler();
	Runnable r = new Runnable() {

		@Override
		public void run() {
			progressWheel.setVisibility(View.VISIBLE);
			getData(pageSize, offset, tagImage);
		}
	};

	private ProgressWheel progressWheel;
	
	// cau lenh tim kieems gan dung
	private String TAG_IMAGE = "ownerId like ";
	
	// tag truyen sang tu chi tiet
	private String tag = "";

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
        setContentView(R.layout.activity_search);
        
        mContext = ActivitySearchActivity.this;
        photosList = new ArrayList<ObjectImage>();
        pref = new PrefManager(mContext);
        
        tag = getIntent().getStringExtra("TAGS");

        titleText = (TextView) findViewById(R.id.title_text);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnSearch = (ImageView) findViewById(R.id.btn_search);
        init();
        
        progressWheel = (ProgressWheel) findViewById(R.id.progressBar);
		progressWheel.setBarWidth(5);

		// Hiding the gridview and showing loader image before making the http
		// request
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setVisibility(View.VISIBLE);

		utils = new Utils(ActivitySearchActivity.this);
		// Gridview adapter
		adapter = new GridViewAdapter((Activity) mContext, photosList, columnWidth);
		// setting grid view adapter
		gridView.setAdapter(adapter);
		// Grid item select listener

		ObjectImage = getIntent().getParcelableExtra("OBJ");
		
		
		if (!tag.equals("")){
			getEdtSearch().setText(tag);
			tagImage = tag;
			mHandler.removeCallbacks(r);
			mHandler.postDelayed(r, 100);
		}
        
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
    }
    
    private void init() {
    	btnBack.setOnClickListener(this);
    	btnSearch.setOnClickListener(this);
    	getEdtSearch().setTypeface(Utils.getTypefaceRegular(mContext));
	}

    private EditText getEdtSearch(){
        return (EditText) findViewById(R.id.edt_search);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			closeActivity();
			break;
		case R.id.btn_search:
			KeyboardHelper.hideSoftKeyboard(getEdtSearch(), mContext);
			progressWheel.setVisibility(View.VISIBLE);
			getData(pageSize, offset, getEdtSearch().getText().toString());
			break;

		default:
			break;
		}
	}
	
	private void getData(int pageIndex, final int offset, String tag) {
		Log.i("DATA", "Goi lan: " + pageIndex + " " + offset);
		BackendlessDataQuery query = new BackendlessDataQuery();
		query.setWhereClause(TAG_IMAGE + "'%"+tag+"%'");
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

				objectImage.setImageIcon(image_url_thumb);
				objectImage.setTitle(image_title);
				objectImage.setCountTilte(image_count);
				photosList.add(objectImage);

			}

			adapter.notifyDataSetChanged();
		}

	}
}