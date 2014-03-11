package com.amvg.milliyet.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amvg.milliyet.main.Skorer.AnimParams;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobilike.garantiad.GarantiAdManager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Video extends FragmentActivity implements AnimationListener
{
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	TestFragmentAdapterVideo mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	public float dpHeight;
	private Display display;
	private DisplayMetrics outMetrics;
	private String DirName="/sdcard/Milliyet/video/";
	public static TextView weatherDate;
	public static TextView weatherInfo;
	public static ImageView weatherImage;
	public int YatayVideoSay;
	private View BannerDivider;
	private LinearLayout FeaturedLayout;
	private LinearLayout CategoriesLayout;
	private LayoutInflater LayInfalater;
	public HorizontalScrollViewExt ScrollViewHorizontal;
	public static Context context;
	public View menu;
	public ViewUtils viewUtils;
	public View app;
	public boolean menuOut = false;
	public AnimParams animParams = new AnimParams();
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	public ListView listView_menu;
	public AppMap appmap;
	public static ArrayList<DataModelVideoImageLoad> Array_FeaturedVideo_Load;
	private String controller;
	public static LinearLayout backgroungLayout;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	public float scale;
	public int videoRowShowCount=0;
	public boolean IsVideo;
	private boolean AutoRefresh;
	
	public boolean checkInternetConnection()
	{
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected())
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Log.e("bas", "bas");
			System.out.println("onClick " + new Date());
			Video me = Video.this;
			context = me;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			Log.e("Menu Genisligi=", Integer.toString(left));
			if (!menuOut)//slide menünün açılışı
			{
				anim = new TranslateAnimation(0, left, 0, 0);
				menu.setVisibility(View.VISIBLE);
				animParams.init(left, 0, left + w, h);
			} 
			else
			{ // slide menünün kapanışı
				anim = new TranslateAnimation(0, -left, 0, 0);
				animParams.init(0, 0, w, h);
			}
			anim.setDuration(500);
			anim.setAnimationListener(me);
			anim.setFillAfter(true);
			app.startAnimation(anim);
		}
	}
	
	private String activityName(String catName)
	{
		if (controller.toLowerCase().equals("dummymilliyettv") || controller.toLowerCase().equals("milliyettv")) 
		{
			return "MilliyetTV";
		}
		else if (controller.toLowerCase().equals("dummyskorertv") || controller.toLowerCase().equals("skorertv")) 
		{
			return "SkorerTV";
		}
		else
		{
			return "Gallery";
		}
	}
	
	private void runCategory(String catName, String catID, Context context, Context contextDialog)
	{
		if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv")) 
		{
			appmap.RunActivity("VideoCategory", catName, "1", catID);
		}
		else
		{
			appmap.RunActivity("VideoCategory", catName, "0", catID);
		}
	}
	private void runGallery(String ID, Context context, Context contextDialog)
	{
		if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv"))
		{
			appmap.RunActivity("VideoClip", "1", ID, "");
		}
		else
		{
			appmap.RunActivity("PhotoGallery", "", ID, "");
		}
	}
	
	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		AutoRefresh=false;
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");
		viewUtils = new ViewUtils();
		menu = findViewById(R.id.menu);
		app = findViewById(R.id.app);
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
		listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
		listView_menu.invalidateViews();
		MainMenuAccessData mainMenuAccessData = new MainMenuAccessData(getApplicationContext(), listView_menu);
		mainMenuAccessData.execute("");
		viewUtils.initListView(this, listView_menu, "Item ", 30,R.id.list_View_Menu, getApplicationContext(), Video.this);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
											 //UA-15581378-12
		Array_FeaturedVideo_Load=new ArrayList<DataModelVideoImageLoad>();
		mAdapter=new TestFragmentAdapterVideo(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		backgroungLayout=(LinearLayout)findViewById(R.id.background);
		BannerDivider=(View)findViewById(R.id.BannerDivider);
//		final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		scale = this.getResources().getDisplayMetrics().density;
		videoRowShowCount=0;
		final ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
		
    	GarantiAdManager.loadAd(this, providerUrl, new GarantiAdManager.AdListener() {
			
			@Override
			public void onLoad()
			{
				Log.d("Foo", "LOADED ADVERTISEMENT");
			}
			
			@Override
			public void onError()
			{
				Log.d("Foo", "YOU FAILED!");
			}
		});
		
//		mPager.setOnTouchListener(new View.OnTouchListener() 
//		{
//			@Override
//			public boolean onTouch(View v, MotionEvent event) 
//			{
//				sv.requestDisallowInterceptTouchEvent(true);
//				return false;
//			}
//	    });
		
		mPager.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.getParent().requestDisallowInterceptTouchEvent(true);
	            return false;
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
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageSelected(int arg0) 
			{
				if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) 
				{
                    sv.requestDisallowInterceptTouchEvent(true);
                }
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		LayInfalater = getLayoutInflater();
		final String[] in={"in"};
		appmap=new AppMap(getApplicationContext(),Video.this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
		     controller = extras.getString("controller");
		}
		if (controller.toLowerCase().equals("milliyettv") || controller.toLowerCase().equals("dummymilliyettv"))
		{
			DirName="/sdcard/Milliyet/milliyettv/";
			((TextView)findViewById(R.id.GaleriTitle)).setVisibility(View.GONE);
			this.IsVideo=true;
		}
		else if(controller.toLowerCase().equals("skorertv") || controller.toLowerCase().equals("dummyskorertv") || controller.equals("DummySkorerTV"))
		{
			DirName="/sdcard/Milliyet/skorertv/";
			((LinearLayout)findViewById(R.id.PagerLayout)).getLayoutParams().height=(int) (198 * scale + 0.5f);
			((TextView)findViewById(R.id.GaleriTitle)).setVisibility(View.GONE);
			this.IsVideo=true;
		}
		else
		{
			DirName="/sdcard/Milliyet/gallery/";
			((LinearLayout)findViewById(R.id.PagerLayout)).getLayoutParams().height=(int) (223 * scale + 0.5f);
			((TextView)findViewById(R.id.GaleriTitle)).setVisibility(View.VISIBLE);
			this.IsVideo=false;
		}
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		if (controller.toLowerCase().equals("milliyettv") || this.controller.toLowerCase().equals("dummymilliyettv"))
		{
			logoImage.setImageResource(R.drawable.navigation_bar_logo_milliyettv);
		}
		else if (controller.toLowerCase().equals("skorertv") || this.controller.toLowerCase().equals("dummyskorertv"))
		{
			logoImage.setImageResource(R.drawable.navigation_bar_logo_skorertv);
		}
		else
		{
			logoImage.setImageResource(R.drawable.navigation_bar_logo);
		}
		logoImage.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		ImageView backImage=(ImageView)findViewById(R.id.menuImage);
		backImage.setOnClickListener(new ClickListener());
		display = getWindowManager().getDefaultDisplay();
		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		dpHeight = outMetrics.widthPixels / (getResources().getDisplayMetrics().density);
		YatayVideoSay=(int) Math.ceil(dpHeight/122.0);
		FeaturedLayout=(LinearLayout)findViewById(R.id.VideoFeaturedLayout);
		CategoriesLayout=(LinearLayout)findViewById(R.id.VideoCategoriesLayout);
		ImageView refreshImage=(ImageView)findViewById(R.id.refreshImage);
		refreshImage.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub 
				if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv") || controller.equals("MilliyetTV")) 
				{
					VideoAccessData refreshData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
					refreshData.execute(in);
				}
				else if (controller.toLowerCase().equals("dummyskorertv") || controller.toLowerCase().equals("skorertv"))
				{
					VideoAccessData refreshData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
					refreshData.execute(in);
				}
				else
				{
					GalleryAccessData refreshData=new GalleryAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
					refreshData.execute(in);
				}
				ScrollViewHorizontal.scrollTo(0, 0);
				Array_FeaturedVideo_Load.clear();
				videoRowShowCount=0;
			}
		});
		ScrollViewHorizontal=(HorizontalScrollViewExt)findViewById(R.id.HorizontalScroll);
		ScrollViewHorizontal.setScrollViewListener(0, new OnScrollViewHorizontalListener()
		{
			@Override
			public void onScrollChanged(HorizontalScrollViewExt scrollView, int x,int y, int oldx, int oldy, int index)
			{
				// TODO Auto-generated method stub
				boolean flag = true;
				flag = true;
				if ((x / scale) / 122 > videoRowShowCount && YatayVideoSay + videoRowShowCount < Array_FeaturedVideo_Load.size()) //&& dikeyVideoSay + videoRowShowCount < Home.Array_TopHeadlinesC_Load.size()
				{
					String[] urlAddress;
					urlAddress = Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle dolduru	lmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						if (!(new File(DirName + urlAddress[urlAddress.length - 1])).exists())
						{
							Log.e("Downlaod Image","Download Image");
							new DownloadImageTaskVideo(Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getNewsImage(),Array_FeaturedVideo_Load.get(YatayVideoSay+videoRowShowCount).getPlayIcon(),urlAddress[urlAddress.length - 1], DirName, Array_FeaturedVideo_Load.get( YatayVideoSay + videoRowShowCount).getIsShown(),IsVideo).execute(Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getImageURL());
						} 
						else
						{
							Log.e("Set Image","Set Image");
							Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							if (IsVideo)
							{
								Array_FeaturedVideo_Load.get(YatayVideoSay+videoRowShowCount).getPlayIcon().setVisibility(View.VISIBLE);
							}
							Array_FeaturedVideo_Load.get( YatayVideoSay + videoRowShowCount).setIsShown(true);
						}
					} 
					catch (Exception e)
					{
						flag = false;
					}
					if (flag)
					{
						videoRowShowCount++;
					}
				}
			}
		});
		weatherDate = (TextView) findViewById(R.id.date);
		weatherInfo = (TextView) findViewById(R.id.weatherData);
		weatherImage = (ImageView) findViewById(R.id.image);
		if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.toLowerCase().equals("dummymilliyettv") || controller.toLowerCase().equals("dummyskorertv") || controller.equals("MilliyetTV") || controller.equals("SkorerTV"))  {
			VideoAccessData accessData=new VideoAccessData(getApplicationContext(), this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater, dpHeight,controller);
			try 
			{
				if (accessData.areFilesOK()) 
				{
					accessData.readData();
					Video.backgroungLayout.setVisibility(View.VISIBLE);
				}
				else 
				{
					accessData.execute(in);
					
				}
			} 
			catch (IOException e) 
			{
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				e.printStackTrace();
			} 
			catch (ParseException e) 
			{
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				e.printStackTrace();
			}
		}
		else
		{
			GalleryAccessData accessData=new GalleryAccessData(getApplicationContext(), this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
			try 
			{
				if (accessData.areFilesOK()) 
				{
					accessData.readData();
					Video.backgroungLayout.setVisibility(View.VISIBLE);
				}
				else 
				{
					accessData.execute(in);
					
				}
			} 
			catch (IOException e) 
			{
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				e.printStackTrace();
			} 
			catch (ParseException e) 
			{
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onResume() 
	{
        super.onResume();
        // Notify comScore about lifecycle usage
        comScore.onEnterForeground();
        
        
        if (AutoRefresh && checkInternetConnection())
		{
        	if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv") || controller.equals("MilliyetTV")) 
			{
				VideoAccessData refreshData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
				try
				{
					if (refreshData.areFilesOK()) 
					{
//						accessData.readData();
					} 
					else
					{
						refreshData.execute("");
						ScrollViewHorizontal.scrollTo(0, 0);
						Array_FeaturedVideo_Load.clear();
						videoRowShowCount=0;
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (controller.toLowerCase().equals("dummyskorertv") || controller.toLowerCase().equals("skorertv"))
			{
				VideoAccessData refreshData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
				try
				{
					if (refreshData.areFilesOK()) 
					{
//						accessData.readData();
					} 
					else
					{
						refreshData.execute("");
						ScrollViewHorizontal.scrollTo(0, 0);
						Array_FeaturedVideo_Load.clear();
						videoRowShowCount=0;
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				GalleryAccessData refreshData=new GalleryAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
				try
				{
					if (refreshData.areFilesOK()) 
					{
//						accessData.readData();
					} 
					else
					{
						refreshData.execute("");
						ScrollViewHorizontal.scrollTo(0, 0);
						Array_FeaturedVideo_Load.clear();
						videoRowShowCount=0;
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        AutoRefresh=true;
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
	    
	    mGaTracker.sendView(activityName(controller));
	    mGaTrackerGlobal.sendView(activityName(controller));
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	    
	    
//	    if (controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv"))
//		{
//	    	mGaTracker.sendView("/AAA-Video");
//		}
//	    else
//	    {
//	    	mGaTracker.sendView("/AAA-Gallery");
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
	public void onBackPressed() 
	{
		try 
		{
			Home.setClickable(true);
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}
	public static void setClickableVideo(boolean value)
	{
	}
	
	void layoutApp(boolean menuOut)
	{
		System.out.println("layout [" + animParams.left + "," + animParams.top+ "," + animParams.right + "," + animParams.bottom + "]");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)app.getLayoutParams();
		if (menuOut)
		{
			
			params.setMargins(animParams.left, 0, -animParams.left, 0); 	
			params.gravity=Gravity.TOP;
		}
		else
		{
			params.setMargins(0, 0, 0, 0); 
		}
		app.setLayoutParams(params);
		app.clearAnimation();
	}
	
	@Override
	public void onAnimationEnd(Animation animation)
	{
		// TODO Auto-generated method stub
		Log.e("onAnimationEnd","onAnimationEnd");
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
		menuOut = !menuOut;
		if (!menuOut)
		{
			menu.setVisibility(View.INVISIBLE);
			listView_menu.setEnabled(true);
			if (Controller!=null)
			{
            	if (!Controller.endsWith(activityName(controller)) && IsMenuClicked)
    			{
    				appmap.RunActivity(Controller,CategoryName, CategoryID,"");
    				IsMenuClicked=false;
    			}
			}
		}
		layoutApp(menuOut);
	}
	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void onAnimationStart(Animation animation)
	{
		// TODO Auto-generated method stub
	}
	class ViewUtils
	{
		private ViewUtils()
		{

		}

		public void setViewWidths(View view, View[] views)
		{
			int w = view.getWidth();
			int h = view.getHeight();
			for (int i = 0; i < views.length; i++)
			{
				View v = views[i];
				v.layout((i + 1) * w, 0, (i + 2) * w, h);
				printView("view[" + i + "]", v);
			}
		}

		public void printView(String msg, View v)
		{
			System.out.println(msg + "=" + v);
			if (null == v)
			{
				return;
			}
			System.out.print("[" + v.getLeft());
			System.out.print(", " + v.getTop());
			System.out.print(", w=" + v.getWidth());
			System.out.println(", h=" + v.getHeight() + "]");
			System.out.println("mw=" + v.getMeasuredWidth() + ", mh=" + v.getMeasuredHeight());
			System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY() + "]");
		}

		public void initListView(Context context, final ListView listView, String prefix, int numItems, int layout, final Context contextGetApplication, final Context contextThis)
		{
			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					listView.setEnabled(false);
					IsMenuClicked=true;
					Controller=((TextView) view.findViewById(R.id.controller)).getText().toString();
					CategoryName=((TextView) view.findViewById(R.id.empty)).getText().toString();
					CategoryID=((TextView) view.findViewById(R.id.CID)).getText().toString();
					itemClickMenuCloser();
				}
			});
		}

		public void itemClickMenuCloser()
		{
			Context context = Video.context;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			anim = new TranslateAnimation(0, -left, 0, 0);
			animParams.init(0, 0, w, h);
			anim.setDuration(500);
			anim.setAnimationListener((AnimationListener) context);
			anim.setFillAfter(true);
			app.startAnimation(anim);
		}
	}
}
