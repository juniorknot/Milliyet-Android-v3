package com.mobilike.preroll;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;

class ViewUtilities extends BaseObject
{
	/***************************************
	 * Singleton reference
	 */
	private static ViewUtilities sharedInstance = null;
	
	/***************************************
	 * Constructors & instance providers
	 */
	private ViewUtilities(){/*No public constructor*/}

	protected static ViewUtilities sharedInstance()
	{
		if(ViewUtilities.sharedInstance == null)
		{
			ViewUtilities.sharedInstance = new ViewUtilities();
		}
		
		return ViewUtilities.sharedInstance;
	}

	
	/***************************************
	 * ???
	 */
	
	int getViewWidth(Activity activity, int resourceId)
	{
		// Fetch view object with given id (Try at least)
		View view = null;
		
		// If context not garbage collected, and will not be collected
		if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
		{
			view = activity.findViewById(resourceId);
		}
		else
		{
			log("Provided context is rotten!");
		}
		
		return getViewWidth(view);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	int getViewWidth(View view)
	{
		int viewWidth = 0;

		// If view parameter is valid (not null)
		if(view != null)
		{
			// Fetch width value in pixel.
			viewWidth = view.getWidth();
			
			// Patch dimension bug (getWidth()/getHeight() returning 0)
			// Detected at;
			//	
			//	- Samsung Note Tab	(4.0.4 - Ice Cream Sandwich)
			//	- Samsung SIII		(4.1.1 - Jelly Bean)
			//
			// Patch is only valid for Honeycomb and newer versions.
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			{
				if(viewWidth == 0)
				{
					viewWidth = view.getMeasuredWidthAndState() & 0x00ffffff;
				}
			}
		}
		
		return viewWidth;
	}
	
	int getViewHeight(Activity activity, int resourceId)
	{
		// Fetch view object with given id (Try at least)
		View view = null;
		
		// If context not garbage collected, and will not be collected
		if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
		{
			view = activity.findViewById(resourceId);
		}
		else
		{
			log("Provided context is rotten!");
		}
		
		return getViewHeight(view);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	int getViewHeight(View view)
	{
		int viewHeight = 0;

		// If view parameter is valid (not null)
		if(view != null)
		{
			// Fetch height value in pixel.
			viewHeight = view.getHeight();
			
			// Patch dimension bug (getWidth()/getHeight() returning 0)
			// Detected at;
			//	
			//	- Samsung Note Tab	(4.0.4 - Ice Cream Sandwich)
			//	- Samsung SIII		(4.1.1 - Jelly Bean)
			//
			// Patch is only valid for Honeycomb and newer versions.
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			{
				if(viewHeight == 0)
				{
					viewHeight = view.getMeasuredHeightAndState() & 0x00ffffff;
				}
			}
		}
		
		return viewHeight;
	}
	
	
	/***************************************
	 * Log
	 */
	
	@Override
	protected boolean isLogEnabled()
	{
		return true;
	}

	@Override
	protected String getLogTag()
	{
		return "ViewUtilities";
	}
}
