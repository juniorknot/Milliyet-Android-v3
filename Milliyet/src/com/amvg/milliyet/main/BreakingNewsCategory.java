package com.amvg.milliyet.main;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobilike.garantiad.GarantiAdManager;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BreakingNewsCategory extends FragmentActivity {

	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	ListView listView;
	String categoryNameText;
	String categoryIDtext;
	Global global;
	TextView IDtext;
	String ID;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breaking_news_category);
		
		GarantiAdManager.loadAd(this, providerUrl, new GarantiAdManager.AdListener() {
			
			@Override
			public void onLoad()
			{
			}
			
			@Override
			public void onError()
			{
			}
		});
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		final String[] in={"in"};
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			categoryNameText = extras.getString("categoryName");
			categoryIDtext = extras.getString("categoryID");
		}
		listView=(ListView)findViewById(R.id.list);
		BreakingNewsCategoryAccessData accessData=new BreakingNewsCategoryAccessData(getApplicationContext(), listView, categoryNameText,categoryIDtext);
		
		try { 
			if (accessData.isFileOK()) {
				accessData.fillData();
			}else{
				accessData.execute(in);
			}
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final AppMap appmap=new AppMap(getApplicationContext(),BreakingNewsCategory.this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	try
				{
		    		IDtext=(TextView)view.findViewById(R.id.ID);
					ID=IDtext.getText().toString();
					appmap.RunActivity("NewsArticle", "", ID,"");
				} 
		    	catch (Exception e)
				{
					// TODO: handle exception
				}
				
						
		    }
		});
		
		ImageView refresh=(ImageView)findViewById(R.id.refreshImage);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BreakingNewsCategoryAccessData refreshData=new BreakingNewsCategoryAccessData(getApplicationContext(), listView, categoryNameText,categoryIDtext);
				refreshData.execute(in);
			}
		});
		
		
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				// TODO Auto-generated method stub
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
	  public void onStart() {
	    super.onStart();

	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("BreakingNewsCategory - "+categoryIDtext);
	    mGaTrackerGlobal.sendView("BreakingNewsCategory - "+categoryIDtext);
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	  }
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	@Override
	public void onBackPressed() {
	   finish();
	}
	

}
