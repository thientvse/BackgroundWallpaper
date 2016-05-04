package com.softforall.backgroundhd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.softforall.backgroundhd.UI.TagGroup;
import com.softforall.backgroundhd.UI.TagGroup.OnTagClickListener;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.model.ObjectImage;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;
import com.startapp.android.publish.nativead.StartAppNativeAd;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityImageDetailActivity extends BHActivity implements OnClickListener {

	private Context mContext;
    private KenBurnsView image;
    private ImageView btnBack;
    
    private ObjectImage objectImage = new ObjectImage();
    
    DisplayImageOptions options = new DisplayImageOptions.Builder()
	.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
	.bitmapConfig(Bitmap.Config.RGB_565).build();
    
    String url = "";
    
    private Button btn_dowload, btn_set_wall;
    
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {
		
		@Override
		public void run() {
//			downloadFile(url);
		}
	};
	
	 // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0; 
    
    private int type = 0; // 0 - download, 1- set luon hinh nen
    
    
	// ADS

	/** StartAppAd object declaration */
	private StartAppAd startAppAd;

	/** StartApp Native Ad declaration */
	private StartAppNativeAd startAppNativeAd;
	private NativeAdDetails nativeAd = null;
	
	
	// tag
	 private TagGroup mDefaultTagGroup;
	 private String[] tags;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
        setContentView(R.layout.activity_image_detail);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy); 
        
        mContext = ActivityImageDetailActivity.this;
        
     // AD
		startAppAd = new StartAppAd(mContext);
		startAppNativeAd = new StartAppNativeAd(mContext);
		
		/** Create Splash Ad **/
		
		Random rand = new Random(); 
		int value = rand.nextInt(5); 
		
		Log.i("DATA", "Value: "+value);
		
		if (value == 3){
			StartAppAd.showSplash(this, savedInstanceState);
		}
		
//		StartAppAd.showSplash(this, savedInstanceState, new SplashConfig()
//				.setAppName(getString(R.string.app_name)));
		
//		StartAppAd.showSplash(this, savedInstanceState);
		
        
        objectImage = getIntent().getParcelableExtra("OBJ");
        Log.i("DATA", "URL: "+objectImage.getImageIcon());
        
        url = objectImage.getImageIcon().replace("960", "1000");

        image = (KenBurnsView) findViewById(R.id.image);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btn_dowload = (Button) findViewById(R.id.btn_dowload);
        btn_set_wall = (Button) findViewById(R.id.btn_set_wall);
        mDefaultTagGroup = (TagGroup) findViewById(R.id.tag_group);
        
        btnBack.setOnClickListener(this);
        btn_dowload.setOnClickListener(this);
        btn_set_wall.setOnClickListener(this);
        
        btn_dowload.setTypeface(Utils.getTypefaceMedium(mContext));
        btn_set_wall.setTypeface(Utils.getTypefaceMedium(mContext));
        
        Log.i("DATA", "TAG: "+objectImage.getTag());
        
        if (objectImage.getTag() != null && !objectImage.getTag().equals("")){
        	tags = Utils.splitStringByTag(objectImage.getTag());
        	mDefaultTagGroup.setTags(tags);
        	Log.i("DATA", "STRING TAG: "+tags[0]);
        }
        
       
        
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
		.displayImage(url, image, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri,
							View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri,
							View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri,
							View view, Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri,
							View view, int current, int total) {
					}
				});
        
        
        startAppNativeAd.loadAd(
				new NativeAdPreferences().setAdsNumber(3)
						.setAutoBitmapDownload(true)
						.setImageSize(NativeAdBitmapSize.SIZE340X340),
				nativeAdListener);
        
        processActionTag();
    }
    
    /** xu li click vao tag thi chuyen sang trang tim kiem */
    private void processActionTag(){
    	 mDefaultTagGroup.setOnTagClickListener(new OnTagClickListener() {
			
			@Override
			public void onTagClick(String tag) {
				Intent t = new Intent(mContext, ActivitySearchActivity.class);
				t.putExtra("TAGS", tag);
				startActivity(t);
				((Activity) mContext).overridePendingTransition(
						R.anim.open_next, R.anim.close_main);
			}
		});
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
				nativeAd.sendImpression(ActivityImageDetailActivity.this);
			}
		}

		@Override
		public void onFailedToReceiveAd(Ad ad) {

		}
	};

    private View getView1(){
        return (View) findViewById(R.id.view1);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
		case R.id.btn_back:
			closeActivity();
			break;
		case R.id.btn_dowload:
			type = 0;
			new DownloadFileFromURL().execute(url);
			// TODO implement
			break;
		case R.id.btn_set_wall:
//			mHandler.removeCallbacks(r);
//			mHandler.postDelayed(r, 200);
			type = 1;
			new DownloadFileFromURL().execute(url);
			break;
        }
    }
    
    
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	
	// set wallpaper
	private void setWallpaper(String path){
//		 DisplayMetrics displayMetrics = new DisplayMetrics();
//		    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		    int height = displayMetrics.heightPixels;
//		    int width = displayMetrics.widthPixels << 1; // best wallpaper width is twice screen width
//		    
//		    Log.i("DATA", "Height: "+height+ " Width: "+width);
//
//		    // First decode with inJustDecodeBounds=true to check dimensions
//		    final BitmapFactory.Options options = new BitmapFactory.Options();
//		    options.inJustDecodeBounds = true;
//		    BitmapFactory.decodeFile(path, options);
//
//		    // Calculate inSampleSize
//		    options.inSampleSize = calculateInSampleSize(options, width, height);
//		    
//		    
//
//		    // Decode bitmap with inSampleSize set
//		    options.inJustDecodeBounds = false;
//		    Bitmap decodedSampleBitmap = BitmapFactory.decodeFile(path, options);
//
//		    WallpaperManager wm = WallpaperManager.getInstance(this);
//		    try {
//		        wm.setBitmap(decodedSampleBitmap);
//		        Toast.makeText(mContext, "Set wallpaper success", Toast.LENGTH_SHORT).show();
//		    } catch (IOException e) {
//		        Log.e("DATA", "Cannot set image as wallpaper", e);
//		    }
		    
		    
		    // set current screen
		    try{
	            DisplayMetrics metrics = new DisplayMetrics(); 
	            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	            windowManager.getDefaultDisplay().getMetrics(metrics);
	            int height = metrics.heightPixels; 
	            int width = metrics.widthPixels;
	            Bitmap tempbitMap = BitmapFactory.decodeFile(path);
	            Bitmap bitmap = Bitmap.createScaledBitmap(tempbitMap,width,height, true);
	            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext); 
	            wallpaperManager.setWallpaperOffsetSteps(1, 1);
	            wallpaperManager.suggestDesiredDimensions(width, height);
	            try {
	              wallpaperManager.setBitmap(bitmap);
	              Toast.makeText(mContext, "Set wallpaper success", Toast.LENGTH_SHORT).show();
	              } catch (IOException e) {
	              e.printStackTrace();
	            }
		    }catch(Exception e){
	    	Log.e("DATA", "Cannot set image as wallpaper", e);
		    }
		    
	}
	
	
	
//	void downloadFile(String fileUrl) {
//		URL myFileUrl = null;
//		try {
//			myFileUrl = new URL(fileUrl);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		Bitmap bmImg = null;
//		try {
//			HttpURLConnection conn = (HttpURLConnection) myFileUrl
//					.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			int length = conn.getContentLength();
//			InputStream is = conn.getInputStream();
//			bmImg = BitmapFactory.decodeStream(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			String filepath = Environment.getExternalStorageDirectory()
//					.getAbsolutePath();
//			FileOutputStream fos = new FileOutputStream(filepath + "/"
//					+ "output.jpg");
//			bmImg.compress(CompressFormat.JPEG, 95, fos);
//			fos.flush();
//			fos.close();
//			Context context = this.getBaseContext();
//			context.setWallpaper(bmImg);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	// dowload image
	/**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type: // we set this to 0
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
    }
 
    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
 
        private Bitmap bmImg;

		/**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
 
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
 
                // make folder
				File folder = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + "BackgroundHD");
				boolean success = true;
				if (!folder.exists()) {
					success = folder.mkdir();
				}
				if (success) {
					// Do something on success
				} else {
					// Do something else on failure
				}
                
                
                // Output stream
                OutputStream output = new FileOutputStream("/sdcard/BackgroundHD/downloadedfile.jpg");
 
                byte data[] = new byte[1024];
 
                long total = 0;
 
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
 
                    // writing data to file
                    output.write(data, 0, count);
                }
 
                // flushing output
                output.flush();
 
                // closing streams
                output.close();
                input.close();
 
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
 
            return null;
        }
 
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }
 
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @SuppressWarnings("deprecation")
		@Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
 
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            
            // setting downloaded into image view
            if (type == 0){
            	String imagePath = Environment.getExternalStorageDirectory().toString() + "/BackgroundHD/"+Utils.getCurrentTimeMillis()+".jpg";
            	
            	Toast.makeText(mContext, "Dowloaded and save "+imagePath, Toast.LENGTH_SHORT).show();
            	
            } else if (type == 1) {
            	String imagePath = Environment.getExternalStorageDirectory().toString() + "/BackgroundHD/downloadedfile.jpg";
            	setWallpaper(imagePath);	
			}
            
			
//			try {
//				 FileOutputStream fos = new FileOutputStream(imagePath + "/"
//							+ "output.jpg");
//					bmImg.compress(CompressFormat.JPEG, 95, fos);
//				fos.flush();
//				fos.close();
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
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
	
	// click tag
	class MyTagGroupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
        	
        }
    }
	
	
}