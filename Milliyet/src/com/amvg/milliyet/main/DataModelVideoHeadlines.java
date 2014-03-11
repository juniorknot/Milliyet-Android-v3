package com.amvg.milliyet.main;

public class DataModelVideoHeadlines
{
	private String ContentType;
	private String ID;
	private String Title;
	private String ImageURL;
	private String ThumbURL;
	private String Link;
	private String ViewCount;
	private String CommentCount;
	private String PositiveVoteCount;
	private String NegativeVoteCount;
	private String Duration;
	
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
	public String getViewCount()
	{
		return this.ViewCount;
	}
	public void setViewCount(String viewCount)
	{
		this.ViewCount=viewCount;
	}
	public String getCommentCount()
	{
		return this.CommentCount;
	}
	public void setCommentCount(String commentCount)
	{
		this.CommentCount=commentCount;
	}
	public String getPositiveVoteCount()
	{
		return this.PositiveVoteCount;
	}
	public void setPositiveVoteCount(String positiveVoteCount)
	{
		this.PositiveVoteCount=positiveVoteCount;
	}
	public String getNegativeVoteCount()
	{
		return this.NegativeVoteCount;
	}
	public void setNegativeVoteCount(String negativeVoteCount)
	{
		this.NegativeVoteCount=negativeVoteCount;
	}
	public String getDuration()
	{
		return this.Duration;
	}
	public void setDuration(String duration)
	{
		this.Duration=duration;
	}
}
