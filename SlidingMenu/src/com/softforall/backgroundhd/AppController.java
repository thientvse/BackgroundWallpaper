package com.softforall.backgroundhd;
 
import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.backendless.Backendless;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.softforall.backgroundhd.Utils.AppConst;
import com.softforall.backgroundhd.Utils.LruBitmapCache;
import com.softforall.backgroundhd.Utils.PrefManager;
import com.startapp.android.publish.StartAppSDK;
 
public class AppController extends Application {
 
    public static final String TAG = AppController.class.getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
 
    private static AppController mInstance;
    private PrefManager pref;

	private String version = "v1";
	
	private static Context sContext;
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = new PrefManager(this);
        sContext = getApplicationContext();
        
        StartAppSDK.init(sContext, AppConst.STARTAPP_ID, AppConst.STARTAPP_APP_ID, true); // TODO:
        
        Backendless.initApp( sContext, AppConst.YOUR_APP_ID, AppConst.YOUR_SECRET_KEY, version );
        
        
     // config image loader
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
         if (!cacheDir.exists())
            cacheDir.mkdir();
        
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
//              .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 70, null)
        .threadPoolSize(2) // default
        .threadPriority(Thread.NORM_PRIORITY - 1) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()
//      .memoryCache(new WeakMemoryCache())
        .memoryCache(new FIFOLimitedMemoryCache(15 * 1024 * 1024))
        .discCache(new TotalSizeLimitedDiscCache(cacheDir, 300 * 1024 * 1024))
//      .discCache(new UnlimitedDiscCache(cacheDir))
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
        .imageDecoder(new BaseImageDecoder(false)) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//      .writeDebugLogs()
        .build();
        
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        
    }
    
    /**
	 * Returns the application context
	 * 
	 * @return application context
	 */
	public static Context getContext() {
		return sContext;
	}
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public PrefManager getPrefManger() {
        if (pref == null) {
            pref = new PrefManager(this);
        }
 
        return pref;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }
 
        return this.mImageLoader;
    }
 
    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}