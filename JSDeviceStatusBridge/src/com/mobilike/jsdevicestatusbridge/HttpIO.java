package com.mobilike.jsdevicestatusbridge;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class HttpIO
{
	private static final String TAG = "HttpIO";
	
	/**********************************************
	 * Networking
	 */
	
	public static void get(final Context context, final String url, final ResponseListener<String> listener)
	{
		if(listener != null)
		{
			new AsyncTask<Void, Void, String>(){

				@Override
				protected String doInBackground(Void... params)
				{
					String response = null;
					
					try
					{
					    HttpClient client = new DefaultHttpClient();
					    client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, getUserAgent(context));
					    HttpGet get = new HttpGet(url);
					    HttpResponse responseGet = client.execute(get);  
					    HttpEntity resEntityGet = responseGet.getEntity();  
					    
					    if (resEntityGet != null)
					    {
					        // do something with the response
					        response = EntityUtils.toString(resEntityGet, HTTP.UTF_8);
					        Log.i(TAG, "Received response:\n" + response);
					    }
					    else
					    {
					    	Log.i(TAG, "Received empty response");
					    }
					}
					catch (Exception e)
					{
						Log.i(TAG, "Received foo.bar.baz", e);
						
					}
					
					return response;
				}
				
				protected void onPostExecute(String result)
				{
					listener.onResponse(result);
				};
				
			}.execute();
		}
		else
		{
			Log.d("HttpIO", "Why bother if none cares!");
		}
	}

	public static void getJsonObject(final Context context, final String url, final ResponseListener<JSONObject> listener)
	{
		if(listener != null)
		{
			get(context, url, new ResponseListener<String>(){

				@Override
				public void onResponse(final String response){
					
					new AsyncTask<Void, Void, JSONObject>(){

						@Override
						protected JSONObject doInBackground(Void... params) {
							
							JSONObject jsonObject = null;
							
							// Parse response
							try
							{
								jsonObject = new JSONObject(response);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							
							return jsonObject;
						}
						
						protected void onPostExecute(JSONObject result) {
							
							listener.onResponse(result);
						};
						
					}.execute();
				}
			});
		}
		else
		{
			Log.d("HttpIO", "Why bother if none cares!");
		}
	}
	
	protected static Object getUserAgent(final Context context)
	{
		StringBuffer buffer = new StringBuffer();

		if(context != null)
		{
			buffer.append(getApplicationName(context))
			.append("/")
			.append(getApplicationVersion(context))
			.append(" (")
			.append(getDeviceName())
			.append("; Android ")
			.append(Build.VERSION.SDK_INT)
			.append("; Scale/")
			.append(String.format("%.2f",
					getScaleRatio(context))).append(")");
		}

		return buffer.toString();
	}
	
	public static String getApplicationName(Context context) {
		String applicationName = null;

		try
		{
			if (context != null) {
				int stringId = context.getApplicationInfo().labelRes;
				applicationName = context.getString(stringId);
			}
		}
		catch(Exception e)
		{
			applicationName = "?";
		}

		return applicationName;
	}

	public static String getApplicationVersion(Context context) {
		String applicationVersion = null;

		if (context != null) {
			PackageInfo pInfo = null;
			try {
				pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				applicationVersion = pInfo.versionName;
			} catch (NameNotFoundException e) {
			}
		}

		return applicationVersion;
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}

		char first = s.charAt(0);

		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	private static float getScaleRatio(Context context) {
		float scaleRatio = 1.0f; // on 160 dp

		if (context != null) {
			scaleRatio = context.getResources().getDisplayMetrics().density;
		}

		return scaleRatio;
	}
	
	
	/**********************************************
	 * Minions
	 */
	
	/**
	 * 
	 * @author gokhanbarisaker
	 *
	 * @param <E> Response Class
	 */
	public interface ResponseListener<E>
	{
		/**
		 * 
		 * 
		 * @param response It yields null value when request failed. Otherwise, response instance
		 */
		public void onResponse(final E response);
	}
}
