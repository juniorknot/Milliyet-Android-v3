package com.amvg.milliyet.main;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobilike.garantiad.GarantiAdManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoGallery extends FragmentActivity {
	private String ID;
	private String galleryTitleText;
	public static LinearLayout linearLayout;
	public static int width;
	public static int height;
	public static TextView categoryName;
	public static TextView galleryTitle;
	private TestFragmentAdapterPhotoGallery mAdapter;
	private ViewPager mPager;
	private PhotoGalleryAccessData accessData;
	public static boolean stopThread;
	private String ID_for_Tracker;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_gallery);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		Display display = getWindowManager().getDefaultDisplay();
		final Point point = new Point();
	    try 
	    {
	        display.getSize(point);
	    } 
	    catch (java.lang.NoSuchMethodError ignore) 
	    { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
		width = point.x;
		height = point.y;
		linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
		//linearLayout.getLayoutParams().width=width;
		categoryName=(TextView)findViewById(R.id.date);
		mAdapter=new TestFragmentAdapterPhotoGallery(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		
		mPager.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.getParent().requestDisallowInterceptTouchEvent(true);
	            return false;
	        }
	    });
		
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

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

	        @Override
	        public void onPageSelected(int arg0) {
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	            mPager.getParent().requestDisallowInterceptTouchEvent(true);
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    });
		
		mPager.setOnTouchListener(new View.OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
	    });
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		     ID_for_Tracker=ID;
		     galleryTitleText=extras.getString("galleryTitle");
		     
		     if (ID.contains("#")) 
		     {
		    	 PhotoGallery.categoryName.setText(extras.getString("categoryName"));
		    	 String[] ID_parse;
		 	     ID_parse = ID.split("#");
		 	     ID_for_Tracker=ID_parse[0];
		     }
		}
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		logoImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppMap appmap=new AppMap(getApplicationContext(),PhotoGallery.this);
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		ImageView backImage=(ImageView)findViewById(R.id.backImage);
		backImage.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
			   try 
			   {
				   Video.backgroungLayout.setVisibility(View.VISIBLE);
			   } 
			   catch (Exception e) {
					// TODO: handle exceptionÅ�
			   }
			   try 
			   {
				   Home.setClickable(true);
			   } 
			   catch (Exception e) {
					// TODO: handle exception
			   }
			   finish();
			}
		});
		
		galleryTitle=(TextView)findViewById(R.id.galleryTitle);
		PhotoGallery.galleryTitle.setText(galleryTitleText);
		final String[] in={"in"};
		accessData= new PhotoGalleryAccessData(getApplicationContext(), ID, mAdapter, mPager,PhotoGallery.this);
		accessData.execute(in);
		setTab();
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
	public void onStart() 
	{
	    super.onStart();
	    mGaTracker.sendView("PhotoGallery - "+ID_for_Tracker);
	    mGaTrackerGlobal.sendView("PhotoGallery - "+ID_for_Tracker);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
	    {
	    	stopThread=true;
	    	finish();
	    	try 
	    	{
	    		TestFragmentVideo.image.setClickable(true);
			} 
	    	catch (Exception e) {
				// TODO: handle exception
			}
	    	try 
	    	{
	    		Home.setClickable(true);
			} 
	    	catch (Exception e) {
				// TODO: handle exception
			}
	    	try 
	    	{
	    		Video.backgroungLayout.setVisibility(View.VISIBLE);
			} 
	    	catch (Exception e) {
				// TODO: handle exception
			}
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.photo_gallery, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
		try 
		{
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	    try 
	    {
	    	Video.backgroungLayout.setVisibility(View.VISIBLE);
		} 
	    catch (Exception e) {
			// TODO: handle exception
		}
		   try {
			   Home.setClickable(true);
		} catch (Exception e) {
			// TODO: handle exception
		} 
	   finish();
	} 
	
	private void setTab()
	{
		mPager.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageSelected(int arg0) 
			{
				// TODO Auto-generated method stub
				try 
				{
					PhotoGallery.linearLayout.getLayoutParams().height=(int) ((60+Global.calculateTextHeight(PhotoGalleryAccessData.text[arg0], PhotoGalleryAccessData.textLength[arg0], PhotoGallery.width))*getApplication().getResources().getDisplayMetrics().density+(int) Math.round((int) (320 * getResources().getDisplayMetrics().density + 0.5f)*PhotoGalleryAccessData.bitmapHeight[arg0]/PhotoGalleryAccessData.bitmapWidth[arg0]));
				} 
				catch (Exception e) {
					// TODO: handle exception
					PhotoGallery.linearLayout.getLayoutParams().height=850;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
}
