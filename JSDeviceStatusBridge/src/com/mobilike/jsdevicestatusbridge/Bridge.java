package com.mobilike.jsdevicestatusbridge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.mobilike.garantiad.AdActivity;
import com.mobilike.garantiad.AdDialogFragment;

/**
 * TODO: Replace deprecated methods
 * 
 * @author gokhanbarisaker
 *
 */
public class Bridge
{
	public static final int OPERATION_SUCCEED = 133;
	public static final int OPERATION_CANCELLED = 132;
	public static final int OPERATION_FAILED = 131;
	
	private WeakReference<AdDialogFragment> adDialogWeak = null;
	private WeakReference<Context> activityContextWeak = null;
	private Context applicationContext = null;
	private String uniqueAppID = null;
	private String uniqueDeviceID = null;
	private static final String PREF_UNIQUE_ID = "bridge.udid";
	
	public Bridge(final Context context, final AdDialogFragment dialog)
	{
		if(context != null)
		{
			setActivityContext(context);
			this.applicationContext = context.getApplicationContext();
			setAdDialogFragment(dialog);
		}
		else
		{
			throw new IllegalArgumentException("Null context not accepted in Bridge constructor!");
		}
	}
	
	@JavascriptInterface 
	public void log()
	{	
		Log.d("Bar", "Fooo");
		
		Toast.makeText(applicationContext, "Fooo", Toast.LENGTH_SHORT).show();
    }
	
	@JavascriptInterface 
	public float getBatteryPercentage()
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = applicationContext.registerReceiver(null, ifilter);
		
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float)scale;
		
		return batteryPct;
	}
	
	@JavascriptInterface 
	public String getDeviceModel()
	{
		return android.os.Build.MODEL;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@JavascriptInterface 
	public long getTotalInternalMemory()
	{
	    StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
	    
	    long total = 0L;
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
	    {
	    	total = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / 1048576;
	    }
	    else
	    {
	    	total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	    }
	    
	    return total;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@JavascriptInterface 
	public long getFreeInternalMemory()
	{
	    StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
	    
	    long free = 0L;
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
	    {
	    	free = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / 1048576;
	    }
	    else
	    {
	    	free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	    }
	    
	    return free;
	}
	
	@JavascriptInterface 
	public long getBusyInternalMemory()
	{
	    long total = getTotalInternalMemory();
	    long free  = getFreeInternalMemory();
	    long busy  = total - free;
	
	    return busy;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@JavascriptInterface 
	public long getTotalExternalMemory()
	{
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	    
	    long total = 0L;
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
	    {
	    	total = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / 1048576;
	    }
	    else
	    {
	    	total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	    }
	    
	    return total;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@JavascriptInterface 
	public long getFreeExternalMemory()
	{
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	    
	    long free = 0L;
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
	    {
	    	free = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / 1048576;
	    }
	    else
	    {
	    	free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	    }
	    
	    return free;
	}
	
	@JavascriptInterface 
	public long getBusyExternalMemory()
	{    
	    long total = getTotalExternalMemory();
	    long free  = getFreeExternalMemory();
	    long busy  = total - free;
	
	    return busy;
	}
	
	@JavascriptInterface 
	public synchronized String getUniqueApplicationIdentifier()
	{
	    if (uniqueAppID == null)
	    {
	        SharedPreferences sharedPrefs = applicationContext.getSharedPreferences(
	                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
	        uniqueAppID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	        
	        if (TextUtils.isEmpty(uniqueAppID))
	        {
	            uniqueAppID = UUID.randomUUID().toString();
	            Editor editor = sharedPrefs.edit();
	            editor.putString(PREF_UNIQUE_ID, uniqueAppID);
	            editor.commit();
	        }
	    }
	    
	    return uniqueAppID;
	}
	
	@JavascriptInterface 
	public synchronized String getUniqueDeviceIdentifier()
	{
	    if (uniqueDeviceID == null)
	    {
	    	File directory = Environment.getExternalStoragePublicDirectory(".mobi");
	    	directory.mkdirs();
	    	File file = new File(directory, PREF_UNIQUE_ID);
	    	
	    	try {
				file.createNewFile();
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				uniqueDeviceID = br.readLine();
				br.close();
						
				if (TextUtils.isEmpty(uniqueDeviceID))
		        {
					uniqueDeviceID = UUID.randomUUID().toString();
		        	BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			    	bw.write(uniqueDeviceID);
			    	bw.close();
		        }
				
			} catch (IOException e) {
				e.printStackTrace();
				
				
			}
	    }
	    
	    return uniqueDeviceID;
	}
	
	@JavascriptInterface
	public int sendSMS(String recipient, String body)
	{
		Log.d("JS Bridge", "SMS send request received");
		
		int status = OPERATION_FAILED;
		
		// If context available
		if(applicationContext != null)
		{
			// If android device can send SMS
			if(applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY))
			{	
				// Make variables null safe
				recipient = (TextUtils.isEmpty(recipient))?(""):(recipient);
				body = (TextUtils.isEmpty(body))?(""):(body);
				
				// Create SSM
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "sms:" + recipient ));
				intent.putExtra("sms_body", body);
				
				Context context = getActivityContext();
				
				if(context != null)
				{
					// Start sms intent
					context.startActivity(intent);

					// Mark as shown
					status = OPERATION_SUCCEED;
				}
				else
				{
					Log.d("JS Bridge", "We lost ui context!");
				}
			}
			else
			{
				Log.d("JSBridge", "SMS not supported by this device");
			}
		}
		
		return status;
	}
	
	private Context getActivityContext()
	{
		return (this.activityContextWeak == null)?(null):(this.activityContextWeak.get());
	}
	
	private void setActivityContext(Context context)
	{
		if(context == null)
		{
			this.activityContextWeak = null;
		}
		else
		{
			this.activityContextWeak = new WeakReference<Context>(context);
		}
	}
	
	private AdDialogFragment getAdDialog()
	{
		return (this.adDialogWeak == null)?(null):(this.adDialogWeak.get());
	}
	
	private void setAdDialogFragment(AdDialogFragment dialog)
	{
		if(dialog == null)
		{
			this.adDialogWeak = null;
		}
		else
		{
			this.adDialogWeak = new WeakReference<AdDialogFragment>(dialog);
		}
	}
	
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	@JavascriptInterface
	public boolean isAppInstalled(String packageName)
	{
		boolean installed = false;
	    final PackageManager packageManager = applicationContext.getPackageManager();
	    
	    try
	    {
	    	packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
	    	
	    	installed = true;
		}
	    catch (Exception e) { /** */ }
	    
	    return installed;
	}
	
	@JavascriptInterface
	public void close()
	{
		Log.d("Foo", "Close prompted");
		
		final AdDialogFragment dialog = getAdDialog();
		
		if(dialog != null)
		{
			dialog.getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					dialog.dismiss();
				}
			});
		}
		else
		{
			Activity activity = (Activity) getActivityContext();
			
			if(activity instanceof AdActivity)
			{
				activity.finish();
			}
		}
	}
}
