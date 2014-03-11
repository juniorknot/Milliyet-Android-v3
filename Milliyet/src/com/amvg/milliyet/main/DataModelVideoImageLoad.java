package com.amvg.milliyet.main;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DataModelVideoImageLoad 
{
	private LinearLayout ItemLayout;
	private ImageView NewsImage;
	private ImageView PlayIcon;
	private String ImageURL;
	private boolean IsShown;
	
	public void DataModelVideo()
	{
		IsShown=false;
	}
	
	public LinearLayout getItemLayout()
	{
		return ItemLayout;
	}
	public void setItemLayout(LinearLayout itemLayout)
	{
		this.ItemLayout=itemLayout;
	}
	
	public ImageView getNewsImage()
	{
		return NewsImage;
	}
	public void setNewsImage(ImageView newsImage)
	{
		this.NewsImage=newsImage;
	}
	
	public ImageView getPlayIcon()
	{
		return PlayIcon;
	}
	public void setPlayIcon(ImageView playIcon)
	{
		this.PlayIcon=playIcon;
	}
	
	public String getImageURL()
	{
		return this.ImageURL;
	}
	public void setImageURL(String imageURL)
	{
		this.ImageURL=imageURL;
	}
	
	public boolean getIsShown()
	{
		return this.IsShown;
	}
	public void setIsShown(boolean isShown)
	{
		this.IsShown=isShown;
	}
	
}
