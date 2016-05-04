package com.softforall.backgroundhd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.softforall.backgroundhd.AppController;
import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.UI.MyClickAbleImageView;
import com.softforall.backgroundhd.UI.SelectableRoundedImageView;
import com.softforall.backgroundhd.Utils.Utils;
import com.softforall.backgroundhd.adapter.ItemLibraryAdapter.ViewHolder;
import com.softforall.backgroundhd.model.ObjectImage;
import com.squareup.picasso.Picasso;

public class GridViewAdapter extends BaseAdapter {

	private Activity _activity;
	private Context mContext;
	private LayoutInflater inflater;
	private List<ObjectImage> wallpapersList = new ArrayList<ObjectImage>();
	private int imageWidth;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public GridViewAdapter(Activity activity, List<ObjectImage> wallpapersList,
			int imageWidth) {
		this._activity = activity;
		this.wallpapersList = wallpapersList;
		this.imageWidth = imageWidth;
		this.mContext = activity;
	}

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
	
	

	@Override
	public int getCount() {
		return this.wallpapersList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.wallpapersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) _activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.grid_item_photo, null);

			viewHolder.image0 = (MyClickAbleImageView) convertView
					.findViewById(R.id.imgLoader);
			viewHolder.txtCount = (TextView) convertView
					.findViewById(R.id.txt_count);
			convertView.setTag(viewHolder);
		}

		initializeViews((ObjectImage) getItem(position),
				(ViewHolder) convertView.getTag());

		// viewHolder.txtCount.setText(objectImage.getCountTilte());
		// viewHolder.txtCount.setTypeface(Utils.getTypefaceRegular(_activity));

		// if (imageLoader == null)
		// imageLoader = AppController.getInstance().getImageLoader();
		//
		// // Grid thumbnail image view
		// NetworkImageView thumbNail = (NetworkImageView) convertView
		// .findViewById(R.id.thumbnail);
		//
		// ObjectImage p = wallpapersList.get(position);
		//
		// thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// thumbNail.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth,
		// imageWidth));
		// thumbNail.setImageUrl(p.getUrl(), imageLoader);

		return convertView;
	}

	private void initializeViews(ObjectImage object, ViewHolder holder) {
		holder.txtCount.setText(object.getCountTilte());
		holder.txtCount.setTypeface(Utils.getTypefaceRegular(_activity));

		com.nostra13.universalimageloader.core.ImageLoader.getInstance()
				.displayImage(object.getImageIcon(), holder.image0, options,
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
		
		
	}

	protected class ViewHolder {
		private MyClickAbleImageView image0;
		private TextView txtCount;
	}

}