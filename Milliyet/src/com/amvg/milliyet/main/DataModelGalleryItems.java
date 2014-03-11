package com.amvg.milliyet.main;

public class DataModelGalleryItems
{
	private String ContentType;
	private String ID;
	private String Description;
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
	public String getDescription()
	{
		return this.Description;
	}
	public void setDescription(String description)
	{
		this.Description=description;
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
