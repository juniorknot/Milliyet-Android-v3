package com.mobilike.preroll;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

public class PreRollManager extends BaseObject
{
	public static final int REQUESTCODE_PREROLLACTIVITY = 907;
	private static final String URL_XMLDOCUMENT_DEFAULT = "http://mobworkz.com/video/mehmetcan_test.xml";
	
	/***************************************
	 * Singleton reference
	 */
	private static PreRollManager sharedInstance = null;
	
	/***************************************
	 * Constructors & instance providers
	 */
	private PreRollManager(){/*No public constructor*/}

	public static PreRollManager sharedInstance()
	{
		if(PreRollManager.sharedInstance == null)
		{
			PreRollManager.sharedInstance = new PreRollManager();
		}
		
		return PreRollManager.sharedInstance;
	}
	
	/***************************************
	 * -
	 */
	public void showPreRoll(final Activity activity, String url)
	{
		if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
		{
			Intent preRollIntent = new Intent(activity, PreRollActivity.class);
			
			url = (TextUtils.isEmpty(url))?(URL_XMLDOCUMENT_DEFAULT):(url);
			
			preRollIntent.putExtra(PreRollActivity.PREROLLACTIVITY_XMLDOCUMENTURL_KEY, url);
			
			activity.startActivityForResult(preRollIntent, REQUESTCODE_PREROLLACTIVITY);
		}
		else
		{
			log("Provided activity is rotten!");
		}
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
		return "PreRollManager";
	}
}
