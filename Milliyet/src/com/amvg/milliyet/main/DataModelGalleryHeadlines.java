package com.amvg.milliyet.main;

public class DataModelGalleryHeadlines
{
	private String ContentType;
	private String ID;
	private String Title;
	private String ImageURL;
	
	public String getContentType()
	{
		return ContentType;
	}
	public void setContentType(String contentType)
	{
		this.ContentType=contentType;
	}
	public String getID()
	{
		return this.ID;
	}
	public void setID(String id)
	{
		this.ID=id;
	}
	public String getTitle()
	{
		return this.Title;
	}
	public void setTitle(String title)
	{
		this.Title=title;
	}
	public String getImageURL()
	{
		return this.ImageURL;
	}
	public void setImageURL(String imageURL)
	{
		this.ImageURL=imageURL;
	}
	
}
