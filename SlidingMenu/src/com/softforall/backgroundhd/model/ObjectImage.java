package com.softforall.backgroundhd.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectImage implements Parcelable {
	private String imageIcon;
	private String title;
	private String countTilte;
	private String url;
	private String type;

	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ObjectImage(String imageIcon, String title, String countTilte) {
		super();
		this.imageIcon = imageIcon;
		this.title = title;
		this.countTilte = countTilte;
	}

	public ObjectImage() {
		super();
	}

	public ObjectImage(Parcel in) {
		imageIcon = in.readString();
		title = in.readString();
		countTilte = in.readString();
		url = in.readString();
		type = in.readString();
		tag = in.readString();
	}

	public String getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(String imageIcon) {
		this.imageIcon = imageIcon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCountTilte() {
		return countTilte;
	}

	public void setCountTilte(String countTilte) {
		this.countTilte = countTilte;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(imageIcon);
		dest.writeString(title);
		dest.writeString(countTilte);
		dest.writeString(url);
		dest.writeString(type);
		dest.writeString(tag);
	}

	public static final Parcelable.Creator<ObjectImage> CREATOR = new Parcelable.Creator<ObjectImage>() {
		public ObjectImage createFromParcel(Parcel in) {
			return new ObjectImage(in);
		}

		public ObjectImage[] newArray(int size) {
			return new ObjectImage[size];
		}
	};

}
