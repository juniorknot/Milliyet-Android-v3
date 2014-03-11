package com.amvg.milliyet.main;

import java.io.File;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherLocation extends Activity
{
	private ListView listView;
	private AppMap appMap;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_location);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		appMap=new AppMap(getApplicationContext(), WeatherLocation.this);
		listView=(ListView)findViewById(R.id.list); 
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub 
				File dir = new File("/sdcard/Milliyet/weather/");
				if (dir.isDirectory()) {
			        String[] children = dir.list();
			        for (int i = 0; i < children.length; i++) {
			            new File(dir, children[i]).delete();
			        }
			    }
				try
				{
					Weather.LocationID=Integer.parseInt(((TextView)arg1.findViewById(R.id.CID)).getText().toString());
					appMap.RunActivity("Weather", "", "", "");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				
			}
		});
		
		((ImageView)findViewById(R.id.backImage)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		listView.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Weather.LocationID=Integer.parseInt(((TextView)v.findViewById(R.id.CID)).getText().toString());
//				appMap.RunActivity("Weather", "", "", "");
//			}
//		});
		WeatherLocationAccessData downloaddata=new WeatherLocationAccessData(getApplicationContext(), listView);
		downloaddata.execute("in");
	}
	
	@Override
	public void onResume() 
	{
        super.onResume();
        // Notify comScore about lifecycle usage
        comScore.onEnterForeground();
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		// Notify comScore about lifecycle usage
		comScore.onExitForeground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather_location, menu);
		return true;
	}
	
	@Override
	  public void onStart() {
	    super.onStart();

	    // Send a screen view when the Activity is displayed to the user.
	    
	    	mGaTracker.sendView("WeatherLocations");
	    	mGaTrackerGlobal.sendView("WeatherLocations");
	    	
	    	FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	  }
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}

}
