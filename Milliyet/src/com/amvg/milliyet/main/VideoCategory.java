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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoCategory extends FragmentActivity {
	private String categoryNameText;
	private String categoryIDtext;
	private ListView listView;
	private String ActivityName;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_category);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
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
		
		final String[] in={"in"};
		final AppMap appmap=new AppMap(getApplicationContext(),VideoCategory.this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			categoryNameText = extras.getString("categoryName");
			categoryIDtext=extras.getString("categoryID");
			ActivityName=extras.getString("ActivityName");
		    //categoryName.setText(categoryNameText);  //kategori adını burada yakalıyoruz.
		}
		listView=(ListView)findViewById(R.id.list);
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		if (ActivityName.equals("MilliyetTV"))
		{ 
			logoImage.setImageResource(R.drawable.navigation_bar_logo_milliyettv);
		}
		else if(ActivityName.equals("SkorerTV"))
		{
			logoImage.setImageResource(R.drawable.navigation_bar_logo_skorertv);
		}
		else
		{
			logoImage.setImageResource(R.drawable.navigation_bar_logo);
		}
		logoImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ActivityName.equals("MilliyetTV"))
				{
					appmap.RunActivity("MilliyetTV", "", "","");
				}
				else if(ActivityName.equals("SkorerTV"))
				{
					appmap.RunActivity("SkorerTV", "", "","");
				}
				else
				{
					appmap.RunActivity("Home", "", "","");
				}
				
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		
		ImageView backImage=(ImageView)findViewById(R.id.backImage);
		backImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Video.setClickableVideo(true);
//					AppMap.DownloadBannerData.bannerEnabled="false";
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					Video.backgroungLayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO: handle exception
				}
				finish();
			}
		});
		
		ImageView refreshImage=(ImageView)findViewById(R.id.refreshImage);
		refreshImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ActivityName.equals("MilliyetTV") || ActivityName.equals("SkorerTV")) 
				{
					VideoCategoryAccessData accessData=new VideoCategoryAccessData(getApplicationContext(), listView, categoryNameText, categoryIDtext,ActivityName, VideoCategory.this);
					accessData.execute(in);
				}
					
				else
				{
					GalleryCategoryAccessData accessData=new GalleryCategoryAccessData(getApplicationContext(), listView, categoryNameText, categoryIDtext,ActivityName, VideoCategory.this);
					accessData.execute(in);
				}
			}
		});
		
		if (ActivityName.equals("MilliyetTV") || ActivityName.equals("SkorerTV")) 
		{
			final VideoCategoryAccessData accessData=new VideoCategoryAccessData(getApplicationContext(), listView, categoryNameText, categoryIDtext,ActivityName, VideoCategory.this);
			
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
		}
		else
		{
			final GalleryCategoryAccessData accessData=new GalleryCategoryAccessData(getApplicationContext(), listView, categoryNameText, categoryIDtext,ActivityName, VideoCategory.this);
			
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
		}
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				try
				{
					appmap.RunActivity(((TextView)arg1.findViewById(R.id.contentType)).getText().toString(), ActivityName, ((TextView)arg1.findViewById(R.id.ID)).getText().toString(), ((TextView)arg1.findViewById(R.id.haberText)).getText().toString());
				} 
				catch (Exception e)
				{
					// TODO: handle exception
				}
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

	    // Send a screwen view when the Activity is displayed to the user.
	    if (ActivityName.equals("MilliyetTV"))
		{ 
	    	mGaTracker.sendView("MilliyetTVCategory - "+categoryIDtext);
	    	mGaTrackerGlobal.sendView("MilliyetTVCategory - "+categoryIDtext);
//			logoImage.setImageResource(R.drawable.navigation_bar_logo_milliyettv);
		}
		else if(ActivityName.equals("SkorerTV"))
		{
			mGaTracker.sendView("SkorerTVCategory - "+categoryIDtext);
	    	mGaTrackerGlobal.sendView("SkorerTVCategory - "+categoryIDtext);
//			logoImage.setImageResource(R.drawable.navigation_bar_logo_skorertv);
		}
		else
		{
			mGaTracker.sendView("GalleryCategory - "+categoryIDtext);
	    	mGaTrackerGlobal.sendView("GalleryCategory - "+categoryIDtext);
//			logoImage.setImageResource(R.drawable.navigation_bar_logo);
		}
	    
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	    
	    
//	    if (ActivityName.equals("MilliyetTV"))
//		{
//	    	mGaTracker.sendView("/VideoCategory - "+categoryNameText);
//		}
//	    else
//	    {
//	    	mGaTracker.sendView("/GalleryCategory - "+categoryNameText);
//	    }
	  }
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_category, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		try {
//			AppMap.DownloadBannerData.bannerEnabled="false";
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			Video.setClickableVideo(true);
			Video.backgroungLayout.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}
}
