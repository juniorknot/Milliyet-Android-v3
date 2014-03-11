package com.mobilike.preroll; //qşza

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

final class ApplicationUtilities extends BaseObject
{
	private static final String PUSHAD_APPLICATIONID_KEY = "pushad.applicationid";
	
	/***************************************
	 * Singleton reference
	 */
	private static ApplicationUtilities sharedInstance = null;
	
	/***************************************
	 * Constructors & instance providers
	 */
	private ApplicationUtilities(){/*No public constructor*/}

	static ApplicationUtilities sharedInstance()
	{
		if(ApplicationUtilities.sharedInstance == null)
		{
			ApplicationUtilities.sharedInstance = new ApplicationUtilities();
		}
		
		return ApplicationUtilities.sharedInstance;
	}
	
	/**
	 * @return Application's name from the {@code PackageManager}.
	 */
	String getApplicationName(Context context)
	{
		String applicationName = null;

		if (context != null)
		{
			int stringId = context.getApplicationInfo().labelRes;
			applicationName = context.getString(stringId);
		}

		return applicationName;
	}

	/**
	 * @return Application's version name from the {@code PackageManager}.
	 */
	String getApplicationVersionName(Context context)
	{
		String applicationVersion = null;

		if (context != null)
		{
			PackageInfo packageInfo = null;
			
			try
			{
				packageInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				applicationVersion = packageInfo.versionName;
			}
			catch (NameNotFoundException e) {}
		}

		return applicationVersion;
	}
	
	/**
	 * @return Application's version number from the {@code PackageManager}.
	 */
	int getApplicationVersionNumber(Context context)
	{
		int applicationVersion = 0;

		if (context != null)
		{
			PackageInfo packageInfo = null;
			
			try
			{
				packageInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				applicationVersion = packageInfo.versionCode;
			}
			catch (NameNotFoundException e) {}
		}

		return applicationVersion;
	}
	
	String getPushAdApplicationId(final Activity activity)
	{
		String applicationId = null;
		
		if(activity != null)
		{
			try
			{
				Bundle data = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
				
				applicationId = data.getString(PUSHAD_APPLICATIONID_KEY);
			}
			catch (NameNotFoundException e)
			{
				// Failed to retrive application bundle
			}
		}
		
		return applicationId;
	}
	
	boolean isActivityAlive(Activity activity)
	{
		return ((activity != null) && (!activity.isFinishing()));
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
		return "ApplicationUtilities";
	}
}
