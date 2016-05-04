package com.softforall.backgroundhd.dcfragment;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.backgroundhd.data.Image_management;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.softforall.backgroundhd.ActivityImageDetailActivity;
import com.softforall.backgroundhd.AppController;
import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.UI.ProgressWheel;
import com.softforall.backgroundhd.Utils.AppConst;
import com.softforall.backgroundhd.Utils.PrefManager;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.adapter.GridViewAdapter;
import com.softforall.backgroundhd.adapter.ItemLibraryAdapter;
import com.softforall.backgroundhd.model.ObjectImage;
 
public class GridFragment extends Fragment {
    private static final String TAG = GridFragment.class.getSimpleName();
    private Utils utils;
    private GridViewAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private static final String bundleAlbumId = "albumId";
    private String selectedAlbumId;
    private List<ObjectImage> photosList;
    private PrefManager pref;
    
    private int offset = 0;
    private int pageSize = 10;
    
    public static GridFragment newInstance() {
    	GridFragment f = new GridFragment();
		return f;
	}
 
    // Picasa JSON response node keys
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
            TAG_T = "$t";
 
    public GridFragment() {
    }
 
    public static GridFragment newInstance(String albumId) {
        GridFragment f = new GridFragment();
        Bundle args = new Bundle();
        args.putString(bundleAlbumId, albumId);
        f.setArguments(args);
        return f;
    }
    
    
    private boolean isLoadMore = true;
	private int preLast;
    
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {
		
		@Override
		public void run() {
			progressWheel.setVisibility(View.VISIBLE);
			getData(pageSize, offset);
		}
	};
	
	private ProgressWheel progressWheel;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	photosList = new ArrayList<ObjectImage>();
        pref = new PrefManager(getActivity());
 
        View rootView = inflater.inflate(R.layout.fragment_grid, container,
                false);
        
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progressBar);
        
        progressWheel.setBarWidth(5);
 
        // Hiding the gridview and showing loader image before making the http
        // request
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setVisibility(View.VISIBLE);
 
        utils = new Utils(getActivity());
        
        // Gridview adapter
        adapter = new GridViewAdapter(getActivity(), photosList, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);
        // Grid item select listener
        
        
        gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (photosList.size() >= pageSize && isLoadMore) {
					if (lastItem == totalItemCount) {
						if (preLast != lastItem) {
							int last_pos = gridView
									.getLastVisiblePosition();
							if (last_pos >= photosList.size() - 1) {
//								pageSize++;
								offset = offset+pageSize;
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
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
            	Intent t = new Intent(getActivity(), ActivityImageDetailActivity.class);
            	t.putExtra("OBJ", photosList.get(position));
            	startActivity(t);
            	
            }
        });
        
        return rootView;
    }
    
    
    
    @Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHandler.postDelayed(r, 100);
		
	}

	// get data
    private void getData(int pageIndex, final int offset) {
    	Log.i("DATA", "Goi lan: "+pageIndex+ " "+offset);
    	BackendlessDataQuery query = new BackendlessDataQuery();
        query.setOffset(offset);
        query.setPageSize(pageIndex);
		Image_management.findAsync(query, new AsyncCallback<BackendlessCollection<Image_management>>() {

			@Override
			public void handleFault(BackendlessFault arg0) {
				
			}

			@Override
			public void handleResponse(
					BackendlessCollection<Image_management> reponse) {
				progressWheel.setVisibility(View.GONE);
				if (offset==reponse.getTotalObjects()){
					isLoadMore = false;
				}
				resultCollection = reponse;
				Log.i("DATA", "reponse: "+reponse.getTotalObjects());
				initList();
				
			}
		});
		
	}
    
    // get data
    private static void advancedPagingAsync() throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        final CountDownLatch latch = new CountDownLatch( 1 );
        final int PAGESIZE = 100;

        final AsyncCallback<BackendlessCollection<Image_management>> callback = new AsyncCallback<BackendlessCollection<Image_management>>()
        {
            private int offset = 0;
            private boolean firstResponse = true;

            @Override
            public void handleResponse(BackendlessCollection<Image_management> restaurants )
            {
                if( firstResponse )
                {
                    System.out.println( "Total restaurants - " + restaurants.getTotalObjects() );
                    firstResponse = false;
                }

                int size  = restaurants.getCurrentPage().size();
                System.out.println( "Loaded " + size + " restaurants in the current page" );

                if( size > 0 )
                {
                    offset+=restaurants.getCurrentPage().size();
                    restaurants.getPage( PAGESIZE, offset, this );
                }
                else
                {
                    latch.countDown();
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault)
            {

            }
        };

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(PAGESIZE);
        Backendless.Data.of(Image_management.class).find( dataQuery, callback );
        latch.await();
        System.out.println( "Total time (ms) - " + (System.currentTimeMillis() - startTime ));
    }
    
    private void getJSon() {
    	// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
         
        String url = "https://api.backendless.com/v1/data/Image_management";
                 
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();     
                 
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
         
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                pDialog.hide();
                            }
                        }, new Response.ErrorListener() {
         
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                pDialog.hide();
                            }
                        }) {
         
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pageSize", String.valueOf(pageSize));
                        params.put("offset", String.valueOf(offset));
                        return params;
                    }

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						HashMap<String, String> headers = new HashMap<String, String>();
		                headers.put("Content-Type", "application/json");
		                headers.put("application-id", AppConst.YOUR_APP_ID);
		                headers.put("secret_key", AppConst.YOUR_SECRET_KEY);	
		                return headers;
					}
                    
                    
         
                };
         
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}
 
 	
 
    /**
     * Method to calculate the grid dimensions Calculates number columns and
     * columns width in grid
     * */
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConst.GRID_PADDING, r.getDisplayMetrics());
 
        // Column width
        columnWidth = (int) ((utils.getScreenWidth() - ((pref
                .getNoOfGridColumns() + 1) * padding)) / pref
                .getNoOfGridColumns());
 
        // Setting number of grid columns
        gridView.setNumColumns(pref.getNoOfGridColumns());
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
 
        // Setting horizontal and vertical padding
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
    
    private BackendlessCollection resultCollection;
//	private String[] items;
	private String type;
	private String table = "Image_management";
	private String property = "image_url_thumb";
    
    private void initList() {
//	    items = new String[ resultCollection.getCurrentPage().size() ];
	    
	    if (resultCollection.getCurrentPage().size()==0){
	    	
	    } else {
	    	for (int i = 0; i < resultCollection.getCurrentPage().size(); i++) {
				ObjectImage objectImage = new ObjectImage();
				
				String objId = String.valueOf(((Image_management) resultCollection
						.getCurrentPage().get(i)).getObjectId());
				String created = String.valueOf(((Image_management) resultCollection
						.getCurrentPage().get(i)).getCreated());
				String image_title = String.valueOf(((Image_management) resultCollection
						.getCurrentPage().get(i)).getImage_title());
				String image_url_thumb = String.valueOf(((Image_management) resultCollection
						.getCurrentPage().get(i)).getImage_url_thumb());
				
				String image_count = String.valueOf(((Image_management) resultCollection
						.getCurrentPage().get(i)).getImage_count_view());
				
				String tag = String
						.valueOf(((Image_management) resultCollection.getCurrentPage().get(i)).getOwnerId());
				
				objectImage.setTag(tag);
				objectImage.setImageIcon(image_url_thumb);
				objectImage.setTitle(image_title);
				objectImage.setCountTilte(image_count);
				photosList.add(objectImage);
				
			}
			
			adapter.notifyDataSetChanged();
		}
	    
	  }
 
}