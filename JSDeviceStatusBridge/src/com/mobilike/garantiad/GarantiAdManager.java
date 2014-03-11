package com.mobilike.garantiad;

import java.lang.ref.WeakReference;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mobilike.jsdevicestatusbridge.HttpIO;
import com.mobilike.jsdevicestatusbridge.HttpIO.ResponseListener;

public class GarantiAdManager
{
	private static final String JSON_URL_KEY = "adUrl";
	private static final String JSON_ACTIVE_KEY = "active";
	private static final String JSON_CAPPING_KEY = "capping";
	private static final String JSON_BUNDLEID_KEY = "bundleId";
	
	private static final String PREFERENCES_CAPCOUNTER_KEY = "cap.counter";
	
	private static WeakReference<AdDialogFragment> adDialogWeak = null;
	
	public static void loadAd(final Activity activity, final String providerUrl)
	{
		HttpIO.getJsonObject(activity, providerUrl, new ResponseListener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				
				if(response == null)
				{
					// Failed
				}
				else
				{
					final Ad ad = new Ad(response);
					
					int capCounter = getCapCounter(activity);
					
					Log.d("Cap", "cap: " + capCounter + "/" + ad.capping);
					
					if(	activity != null && ad.isActive() && 
						!TextUtils.isEmpty(ad.getUrl()) && 
						capCounter == 0 && 
						!isAppInstalled(activity, ad.getBundleId()))
					{					    
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								if(!activity.isFinishing())
								{
									Intent adIntent = new Intent(activity, AdActivity.class);
									adIntent.putExtra(AdActivity.BUNDLE_AD_KEY, ad);
									
									activity.startActivity(adIntent);
								}
							}
						});
					}
					else
					{
						Log.d("Ad Manager", "Current ad not displayable!");
					}
					
					capCounter++;
					capCounter %= ad.getCapping();
					setCapCounter(activity, capCounter);
				}
			}
		});
	}
	
	public static void loadAd(final FragmentActivity activity, final String providerUrl, final AdListener listener)
	{
		AdDialogFragment dialogFragment = getAdDialog();
		
		if(dialogFragment == null || (dialogFragment.getDialog() != null && !dialogFragment.getDialog().isShowing()))
		{
			HttpIO.getJsonObject(activity, providerUrl, new ResponseListener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					
					if(response == null)
					{
						if(listener != null)
						{
							listener.onError();
						}
					}
					else
					{
						final Ad ad = new Ad(response);
						
						int capCounter = getCapCounter(activity);

						Log.d("Cap", "cap: " + capCounter + "/" + ad.capping);
						
						if(ad.isActive() && !TextUtils.isEmpty(ad.getUrl()) && capCounter == 0 && !isAppInstalled(activity, ad.getBundleId()))
						{					    
							activity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									// If currently no advertisement presented
									if(!activity.isFinishing())
									{
										// DialogFragment.show() will take care of adding the fragment
									    // in a transaction.  We also want to remove any currently showing
									    // dialog, so make our own transaction and take care of that here.
//									    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//									    Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(AdDialogFragment.FRAGMENT_TAG);
//									    if (prev != null) {
//									        ft.remove(prev);
//									    }
//									    ft.addToBackStack(null);

									    // Create and show the dialog.
										AdDialogFragment dialog = AdDialogFragment.newInstance(ad, listener);
										setAdDialog(dialog);
										
										try
										{
											dialog.show(activity.getSupportFragmentManager(), AdDialogFragment.FRAGMENT_TAG);
										} catch (Exception e){}
									}
								}
							});
						}
						else
						{
							Log.d("Ad Manager", "Current ad not displayable!");
						}
						
						capCounter++;
						capCounter %= ad.getCapping();
						setCapCounter(activity, capCounter);
					}
				}
			});
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
	public static boolean isAppInstalled(Context context, String packageName)
	{
		boolean installed = false;
	    final PackageManager packageManager = context.getPackageManager();
	    
	    try
	    {
	    	packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
	    	
	    	installed = true;
		}
	    catch (Exception e) { /** */ }
	    
	    return installed;
	}
	
	public static int getCapCounter(final Context context)
	{
		int counter = 0;
		
		if(context != null)
		{
			SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_CAPCOUNTER_KEY, Context.MODE_PRIVATE);
			counter = preferences.getInt(PREFERENCES_CAPCOUNTER_KEY, 0);
		}
		
		return counter;
	}
	
	public static void setCapCounter(final Context context, final int counter)
	{
		if(context != null)
		{
			context.getSharedPreferences(PREFERENCES_CAPCOUNTER_KEY, Context.MODE_PRIVATE)
			.edit()
			.putInt(PREFERENCES_CAPCOUNTER_KEY, counter)
			.commit();
		}
	}
	
	private static AdDialogFragment getAdDialog()
	{
		return (adDialogWeak == null)?(null):(adDialogWeak.get());
	}
	
	private static void setAdDialog(AdDialogFragment dialog)
	{
		if(dialog == null)
		{
			adDialogWeak = null;
		}
		else
		{
			adDialogWeak = new WeakReference<AdDialogFragment>(dialog);
		}
	}
	
	
	/**********************************************
	 * Minions
	 */
	
	public interface AdListener
	{
		public void onLoad();
		public void onError();
	}
	
	protected static class Ad implements Parcelable {
		private String url = null;
		private boolean active = false;
		private int capping = 0;
		private String bundleId = null;

		protected Ad(JSONObject jsonObject)
		{
			if(jsonObject != null)
			{
				try
				{ setActive(jsonObject.getBoolean(JSON_ACTIVE_KEY)); }
				catch(JSONException exception) {}
				
				try
				{ setCapping(jsonObject.getInt(JSON_CAPPING_KEY)); }
				catch(JSONException exception) {}
				
				try
				{ setUrl(jsonObject.getString(JSON_URL_KEY)); }
				catch(JSONException exception) {}
				
				try
				{ setBundleId(jsonObject.getString(JSON_BUNDLEID_KEY)); }
				catch(JSONException exception) {}
			}
			else
			{
				Log.d("", "Received null JSONObject Ad constructor!");
			}
		}
		
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public int getCapping() {
			return capping;
		}

		public void setCapping(int capping) {
			this.capping = capping;
		}
		
		public String getBundleId() {
			return bundleId;
		}

		public void setBundleId(String bundleId) {
			this.bundleId = bundleId;
		}
	
    protected Ad(Parcel in) {
        url = in.readString();
        active = in.readByte() != 0x00;
        capping = in.readInt();
        bundleId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeByte((byte) (active ? 0x01 : 0x00));
        dest.writeInt(capping);
        dest.writeString(bundleId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ad> CREATOR = new Parcelable.Creator<Ad>() {
	        @Override
	        public Ad createFromParcel(Parcel in) {
	            return new Ad(in);
	        }
	
	        @Override
	        public Ad[] newArray(int size) {
	            return new Ad[size];
	        }
	    };
	}
}
