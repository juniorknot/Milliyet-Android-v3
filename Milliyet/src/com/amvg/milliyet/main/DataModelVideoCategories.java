package com.amvg.milliyet.main;

public class DataModelVideoCategories
{
	private String ID;
	private String Name;
	private String SortOrder;
	private String Featured;
	private String Website;
	
	public String getID()
	{
		return this.ID;
	}
	public void setID(String id)
	{
		this.ID=id;
	}
	public String getName()
	{
		return this.Name;
	}
	public void setName(String name)
	{
		this.Name=name;
	}
	public String getSortOrder()
	{
		return this.SortOrder;
	}
	public void setSortOrder(String sortOrder)
	{
		this.SortOrder=sortOrder;
	}
	public String getFeatured()
	{
		return this.Featured;
	}
	public void setFeatured(String featured)
	{
		this.Featured=featured;
	}
	public String getWebsite()
	{
		return this.Website;
	}
	public void setWebsite(String website)
	{
		this.Website=website;
	}
}
