package com.amvg.milliyet.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.amvg.milliyet.main.Home.AnimParams;
import com.amvg.milliyet.main.NewsCategory.ViewUtils;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class Weather extends Activity implements AnimationListener
{
//	LinearLayout layout;
//	View view;
	
	public static TextView CityText;
	public static TextView DateText;
	public static TextView TemperatureText;
	public static TextView WeatherText;
	public static TextView FeelsLikeText;
	public static TextView HumidityText;
	public static TextView WindDirectionText;
	public static TextView WindSpeedText;
	public static TextView VisibilityText;
	public static TextView PressureText;
	public static TextView DewPointText;
	public static ImageView WeatherIcon;
	public static LinearLayout GunlukLayout;
	public static LinearLayout SaatlikLayot;
	public static LinearLayout YasamLayout;
	public static int LocationID;
	public static ImageView RefreshImage;
	private LayoutInflater layInflater;
	public static ToggleButton GunlukButton;
	public static ToggleButton SaatlikButton;
	public static HorizontalScrollView GunlukScroll;
	public static HorizontalScrollView SaatlikScroll;
	private AppMap appmap;
	private View BannerDivider;
	private Button ButtonCityChange;
	private boolean IsDailiyShown=false;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	
	public View menu;
	public View app;
	public ViewUtils viewUtils;
	public boolean menuOut = false;
	public static Context context; 
	public AnimParams animParams = new AnimParams();
	public  ListView listView_menu;
	
	class ClickListener implements OnClickListener {
    	
    	
        @Override
        public void onClick(View v) {
        	Log.e("bas","bas");
            System.out.println("onClick " + new Date());
            Weather me = Weather.this;
           context = me;
            Animation anim;
            
            
            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (getResources().getDisplayMetrics().density*255);
            if (!menuOut) {//slide menünün açılışı
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
                anim = new TranslateAnimation(0, left, 0, 0);
                menu.setVisibility(View.VISIBLE);
                animParams.init(left, 0, left + w, h);
            } else { //slide menünün kapanışı
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in_80);
                anim = new TranslateAnimation(0, -left, 0, 0);
                animParams.init(0, 0, w, h);
            }

            anim.setDuration(500);
            anim.setAnimationListener(me);
            //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
            anim.setFillAfter(true);


            // Only use fillEnabled and fillAfter if we don't call layout ourselves.
            // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
            // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
            // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
            // work.
            // anim.setFillEnabled(true);
            // anim.setFillAfter(true);

            app.startAnimation(anim);
        }
       
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		Log.e("ID=",Integer.toString(LocationID));
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");
		
		viewUtils=new ViewUtils();
		
		menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        viewUtils.printView("menu", menu);
        viewUtils.printView("app", app);
		
        listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
        listView_menu.invalidateViews();
	    MainMenuAccessData mainMenuAccessData=new MainMenuAccessData(getApplicationContext(), listView_menu);
	    mainMenuAccessData.execute("");
	    viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), Weather.this);
	    
	    appmap=new AppMap(getApplicationContext(), Weather.this);
	    
//	    Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//		     LocationID = Integer.parseInt(extras.getString("ID"));
//		}
		
		layInflater=getLayoutInflater();
		CityText=(TextView)findViewById(R.id.City);
		DateText=(TextView)findViewById(R.id.Date);
		TemperatureText=(TextView)findViewById(R.id.Temperature);
		WeatherText=(TextView)findViewById(R.id.WeatherText);
		FeelsLikeText=(TextView)findViewById(R.id.FeelsLike);
		HumidityText=(TextView)findViewById(R.id.Humidity);
		WindDirectionText=(TextView)findViewById(R.id.WindDirection);
		WindSpeedText=(TextView)findViewById(R.id.WindSpeed);
		VisibilityText=(TextView)findViewById(R.id.Visibility);
		PressureText=(TextView)findViewById(R.id.Pressure);
		DewPointText=(TextView)findViewById(R.id.DewPoint);
		GunlukLayout=(LinearLayout)findViewById(R.id.GunlukLayout);
		SaatlikLayot=(LinearLayout)findViewById(R.id.SaatlikLayout);
		YasamLayout=(LinearLayout)findViewById(R.id.YasamLayout);
		GunlukButton=(ToggleButton)findViewById(R.id.GunlukButton);
		SaatlikButton=(ToggleButton)findViewById(R.id.SaatlikButton);
		GunlukScroll=(HorizontalScrollView)findViewById(R.id.GunlukScroll);
		SaatlikScroll=(HorizontalScrollView)findViewById(R.id.SaatlikScroll);
		WeatherIcon=(ImageView)findViewById(R.id.WeatherIcon);
		RefreshImage=(ImageView)findViewById(R.id.refreshImage);
		GunlukButton.setChecked(false);
		SaatlikButton.setChecked(true);
		GunlukScroll.setVisibility(View.GONE);
		SaatlikScroll.setVisibility(View.VISIBLE);
		
		GunlukButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				// TODO Auto-generated method stub
				
				WeatherAccessDailyData accessDailiyData=new WeatherAccessDailyData(layInflater,getApplicationContext(),LocationID,Weather.this);
				try
				{
					if (!IsDailiyShown)
					{
						if (accessDailiyData.isFileOK())
						{
							Weather.GunlukLayout.removeAllViews();
							accessDailiyData.readData();
						}
						else
						{
							accessDailiyData.execute("");
						}
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
//				accessDailiyData.execute("");
				GunlukButton.setChecked(true);
				SaatlikButton.setChecked(false);
				GunlukScroll.setVisibility(View.VISIBLE);
				SaatlikScroll.setVisibility(View.GONE);
				IsDailiyShown=true;
			}
		});
		
		SaatlikButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				GunlukButton.setChecked(false);
				SaatlikButton.setChecked(true);
				GunlukScroll.setVisibility(View.GONE);
				SaatlikScroll.setVisibility(View.VISIBLE);
			}
		});
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		
		final WeatherAccessData downloadData=new WeatherAccessData(layInflater,getApplicationContext(),LocationID,this);
		try
		{
			if (downloadData.areFilesOK())
			{
				downloadData.readData();
			}
			else
			{
				downloadData.execute("in");
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
		
		((ImageView)findViewById(R.id.menuImage)).setOnClickListener(new ClickListener());
		
		RefreshImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				RefreshImage.setClickable(false);
				// TODO Auto-generated method stub
				WeatherAccessData downloadData=new WeatherAccessData(layInflater,getApplicationContext(),LocationID,Weather.this);
				downloadData.execute("in");
				
			}
		});
//		((ImageView)findViewById(R.id.refreshImage)).setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		((Button)findViewById(R.id.ButtonCityChange)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("WeatherLocation", "", "", "");
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}
	
	@Override
	  public void onStart() {
	    super.onStart();

	    // Send a screen view when the Activity is displayed to the user.
	    
	    	mGaTracker.sendView("Weather");
	    	mGaTrackerGlobal.sendView("Weather");
	    	
	    	FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	  }
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
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
//        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
        //Now that we've set the app.layout property we can clear the animation, flicker avoided :)
        app.clearAnimation();

    }

	@Override
	public void onAnimationEnd(Animation animation)
	{
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
        menuOut = !menuOut;
        
        Log.e("1","1");
        if (!menuOut) 
        {
        	Log.e("2","2");
            menu.setVisibility(View.INVISIBLE);
            listView_menu.setEnabled(true);
            if (Controller!=null)
			{
            	Log.e("3",Controller);
            	if (!Controller.endsWith("Weather") && IsMenuClicked)
    			{
            		Log.e("4","4");
    				appmap.RunActivity(Controller,CategoryName, CategoryID,"");
    				IsMenuClicked=false;
    			}
			}
        }
        layoutApp(menuOut);
		// TODO Auto-generated method stub
		
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
	
class ViewUtils {
		
	    private ViewUtils() {
	    	
	    }

	    public void setViewWidths(View view, View[] views) {
	        int w = view.getWidth();
	        int h = view.getHeight();
	        for (int i = 0; i < views.length; i++) {
	            View v = views[i];
	            v.layout((i + 1) * w, 0, (i + 2) * w, h);
	            printView("view[" + i + "]", v);
	        }
	    }

	    public void printView(String msg, View v) {
	        System.out.println(msg + "=" + v);
	        if (null == v) {
	            return;
	        }
	        System.out.print("[" + v.getLeft());
	        System.out.print(", " + v.getTop());
	        System.out.print(", w=" + v.getWidth());
	        System.out.println(", h=" + v.getHeight() + "]");
	        System.out.println("mw=" + v.getMeasuredWidth() + ", mh=" + v.getMeasuredHeight());
	        System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY() + "]");
	    }

	    public void initListView(Context context, final ListView listView, String prefix, int numItems, int layout, final Context contextGetApplication, final Context contextThis) {
	        
	    
	        listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	listView.setEnabled(false);
	            	IsMenuClicked=true;
					Controller=((TextView) view.findViewById(R.id.controller)).getText().toString();
					CategoryName=((TextView) view.findViewById(R.id.empty)).getText().toString();
					CategoryID=((TextView) view.findViewById(R.id.CID)).getText().toString();
	                itemClickMenuCloser();
	            }
	        });
	    }
	    
	    public void itemClickMenuCloser(){
	    	
	        Context context = Weather.context;
	        Animation anim;
	        
	        int w = app.getMeasuredWidth();
	        int h = app.getMeasuredHeight();
	        int left = (int) (getResources().getDisplayMetrics().density*255);
	        
	        anim = new TranslateAnimation(0, -left, 0, 0);
	        animParams.init(0, 0, w, h);
	        
	        anim.setDuration(500);
	        anim.setAnimationListener((AnimationListener) context);
	        //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
	        anim.setFillAfter(true);
	        app.startAnimation(anim);
	    }
	}
}
