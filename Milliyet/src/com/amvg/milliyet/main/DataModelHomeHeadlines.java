package com.amvg.milliyet.main;

public class DataModelHomeHeadlines
{
	private String ContentType;
	private String ID;
	private String Title;
	private String ImageURL;
	private String ThumbURL;
	private String Link;
	
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
	public String getThumbURL()
	{
		return this.ThumbURL;
	}
	public void setThumbURL(String thumbURL)
	{
		this.ThumbURL=thumbURL;
	}
	public String getLink()
	{
		return this.Link;
	}
	public void setLink(String link)
	{
		this.Link=link;
	}
}
