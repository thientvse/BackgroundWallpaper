package com.softforall.backgroundhd.dcfragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.backgroundhd.data.Image_library;
import com.backendless.backgroundhd.data.Image_management;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.ObjectProperty;
import com.softforall.backgroundhd.ActivityDetailListImageActivity;
import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.UI.ProgressWheel;
import com.softforall.backgroundhd.adapter.ItemLibraryAdapter;
import com.softforall.backgroundhd.model.ObjectImage;

public class LibraryFragment extends Fragment {
	
	private Context mContext;
	private List<ObjectImage> objectImages = new ArrayList<ObjectImage>();
	private ItemLibraryAdapter adapter;
	private ListView lstLibrary;
	
	Handler mHandler = new Handler();
	
	Runnable r = new Runnable() {
		
		@Override
		public void run() {
			Backendless.Persistence.describe("Image_library",
					new AsyncCallback<List<ObjectProperty>>() {
						@Override
						public void handleResponse(
								List<ObjectProperty> objectProperties) {
							Log.i("DATA", "objectProperties: " + objectProperties);
						}

						@Override
						public void handleFault(BackendlessFault backendlessFault) {
						}
					});

			// synchronous call
			Backendless.Persistence.describe("Image_library");	
		}
	};
	
	private BackendlessCollection resultCollection;
//	private String[] items;
	private String type;
	private String table = "Image_library";
	private String property = "image_url_thumb";
	private ArrayList<ObjectImage> items = new ArrayList<ObjectImage>();

	
	private ProgressWheel progressWheel;
	
	public static LibraryFragment newInstance() {
		LibraryFragment f = new LibraryFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_library, container,
				false);
		
		lstLibrary = (ListView) rootView.findViewById(R.id.lst_library);
		progressWheel = (ProgressWheel) rootView.findViewById(R.id.progressBar);
		progressWheel.setBarWidth(5);
		
		
		
		lstLibrary.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
				Intent t = new Intent(mContext, ActivityDetailListImageActivity.class);
				t.putExtra("OBJ", objectImages.get(pos));
				startActivity(t);
				
			}
			
		});
		
		return rootView;
	}
	
	
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getData();
	}

	private void getData() {
		
		progressWheel.setVisibility(View.VISIBLE);
		
		BackendlessDataQuery query = new BackendlessDataQuery();
		Image_library.findAsync(query, new AsyncCallback<BackendlessCollection<Image_library>>() {

			@Override
			public void handleFault(BackendlessFault arg0) {
				
			}

			@Override
			public void handleResponse(
					BackendlessCollection<Image_library> reponse) {
				progressWheel.setVisibility(View.GONE);
				resultCollection = reponse;
//				Image_management fImage_management = reponse.getCurrentPage().get(0);
				Log.i("DATA", "reponse: "+reponse.toString());
				initList();
				
			}
		});
	}
	
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		

	}

	 private void initList() {
//	    items = new String[ resultCollection.getCurrentPage().size() ];
	    
		for (int i = 0; i < resultCollection.getCurrentPage().size(); i++) {
			ObjectImage objectImage = new ObjectImage();
			
			String image_title = String.valueOf(((Image_library) resultCollection
					.getCurrentPage().get(i)).getName_type_image());
			String image_url_thumb = String.valueOf(((Image_library) resultCollection
					.getCurrentPage().get(i)).getUrl_icon());
			
			String count = String.valueOf(((Image_library) resultCollection
					.getCurrentPage().get(i)).getCount());
			
			String type = String.valueOf(((Image_library) resultCollection
					.getCurrentPage().get(i)).getType_image());
			
			objectImage.setImageIcon(image_url_thumb);
			objectImage.setTitle(image_title);
			objectImage.setCountTilte(count);
			objectImage.setType(type);
			
			objectImages.add(objectImage);
			
		}
		adapter = new ItemLibraryAdapter(mContext, objectImages);
		lstLibrary.setAdapter(adapter);
	    Log.i("DATA", "resultCollection: "+items.toString());
	    
//
//	    if( table.equals( "Image_management" ) )
//	    {
//	      if( property.equals( "objectId" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getObjectId() );
//	        }
//	      }
//	      else if( property.equals( "created" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getCreated() );
//	        }
//	      }
//	      else if( property.equals( "image_type" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getImage_type() );
//	        }
//	      }
//	      else if( property.equals( "image_url_thumb" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getImage_url_thumb() );
//	        }
//	      }
//	      else if( property.equals( "image_title" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getImage_title() );
//	        }
//	      }
//	      else if( property.equals( "updated" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getUpdated() );
//	        }
//	      }
//	      else if( property.equals( "ownerId" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getOwnerId() );
//	        }
//	      }
//	      else if( property.equals( "image_id" ) )
//	      {
//	        for( int i = 0; i < resultCollection.getCurrentPage().size(); i++ )
//	        {
//	          items[ i ] = String.valueOf( ((Image_management) resultCollection.getCurrentPage().get( i )).getImage_id() );
//	        }
//	      }
//	    }
//	    
//	    for (int i = 0; i < items.length; i++) {
//			Log.i("DATA", "Image url: "+items[i]);
//		}

//	    ListAdapter adapter = new ArrayAdapter( this, android.R.layout.simple_list_item_1, items );
//	    entitiesView.setAdapter( adapter );
	  }
	
	
	
	
}
