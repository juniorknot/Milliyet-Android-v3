package com.mobilike.preroll;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreRoll extends BaseObject
{
	private static final String ELEMENTNAME_EXIST = "exist";
	private static final String ELEMENTNAME_VIDEOURL = "videourl";
	private static final String ELEMENTNAME_RESTRICTEDDURATION = "sec";
	private static final String ELEMENTNAME_TARGETURL = "camplink";
	
	private boolean exist = false;
	private String videoURLString = null;
	private int restrictedDuration = 0;
	private String targetURLString = null;
	
	public PreRoll(Document document)
	{
		String existValue = getNodeValue(document, ELEMENTNAME_EXIST);
		this.videoURLString = getNodeValue(document, ELEMENTNAME_VIDEOURL);
		String restrictedDurationValue = getNodeValue(document, ELEMENTNAME_RESTRICTEDDURATION);
		this.targetURLString = getNodeValue(document, ELEMENTNAME_TARGETURL);
		
		try
		{
			this.exist = (existValue == null)?(false):(Integer.parseInt(existValue) == 1);
		}
		catch(NumberFormatException e)
		{
			this.exist = false;
		}
		
		try
		{
			this.restrictedDuration = (restrictedDurationValue == null)?(0):(Integer.parseInt(restrictedDurationValue));
		}
		catch(NumberFormatException e)
		{
			this.restrictedDuration = 0;
		}
	}
	
	/***************************************
	 * Accessors
	 */
	
	private String getNodeValue(Document document, String nodeElementName)
	{
		String nodeValue = null;
		
		if(document != null)
		{
			NodeList nodeList = document.getElementsByTagName(nodeElementName);
			
			if(nodeList.getLength() > 0)
			{
				Node node = nodeList.item(0);
				
				nodeValue = node.getFirstChild().getTextContent();
			}
		}
		
		return nodeValue;
	}
	
	public boolean isExist()
	{
		return exist;
	}

	public String getVideoURLString()
	{
		return videoURLString;
	}

	public int getRestrictedDuration()
	{
		return restrictedDuration;
	}

	public String getTargetURLString()
	{
		return targetURLString;
	}

	
	/***************************************
	 * Log
	 */
	
	@Override
	protected boolean isLogEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getLogTag()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
