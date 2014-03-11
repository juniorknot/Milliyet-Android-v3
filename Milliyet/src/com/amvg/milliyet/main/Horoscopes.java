package com.amvg.milliyet.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.amvg.milliyet.main.Home.AnimParams;
import com.amvg.milliyet.main.Weather.ViewUtils;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Horoscopes extends Activity implements AnimationListener
{
	public View view;
	public LayoutInflater layInflator;
	public static LinearLayout YatayLayout1;
	public static LinearLayout YatayLayout2;
	public static ImageView RefreshImage;
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	private AppMap appMap;
	private View BannerDivider;
	public View menu;
	public View app;
	public ViewUtils viewUtils;
	public boolean menuOut = false;
	public static Context context; 
	public AnimParams animParams = new AnimParams();
	public  ListView listView_menu;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	class ClickListener implements OnClickListener 
	{
        @Override
        public void onClick(View v) 
        {
            System.out.println("onClick " + new Date());
            Horoscopes me = Horoscopes.this;
            context = me;
            Animation anim;
            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (getResources().getDisplayMetrics().density*255);
            if (!menuOut) 
            {//slide menünün açılışı
                anim = new TranslateAnimation(0, left, 0, 0);
                menu.setVisibility(View.VISIBLE);
                animParams.init(left, 0, left + w, h);
            } 
            else 
            { //slide menünün kapanışı
                anim = new TranslateAnimation(0, -left, 0, 0);
                animParams.init(0, 0, w, h);
            }
            anim.setDuration(500);
            anim.setAnimationListener(me);
            anim.setFillAfter(true);
            app.startAnimation(anim);
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horoscopes);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		YatayLayout1=(LinearLayout)findViewById(R.id.yatay1);
		YatayLayout2=(LinearLayout)findViewById(R.id.yatay2);
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		RefreshImage=(ImageView)findViewById(R.id.refreshImage);
		layInflator=getLayoutInflater();
		viewUtils=new ViewUtils();
		menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);
		appMap=new AppMap(getApplicationContext(), Horoscopes.this);
		viewUtils.printView("menu", menu);
        viewUtils.printView("app", app);
        listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
        listView_menu.invalidateViews();
	    MainMenuAccessData mainMenuAccessData=new MainMenuAccessData(getApplicationContext(), listView_menu);
	    mainMenuAccessData.execute("");
	    viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), Horoscopes.this);
		HoroscopesAccessData downloadData=new HoroscopesAccessData(layInflator, getApplicationContext(),Horoscopes.this);
		try
		{
			if (downloadData.isFileOK())
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
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		RefreshImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				RefreshImage.setClickable(false);
				HoroscopesAccessData download =new HoroscopesAccessData(layInflator, getApplicationContext(),Horoscopes.this);
				download.execute("in");
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
		getMenuInflater().inflate(R.menu.horoscopes, menu);
		return true;
	}
	
	@Override
	public void onStart() 
	{
	    super.onStart();
	    mGaTracker.sendView("Horoscopes");
	    mGaTrackerGlobal.sendView("Horoscopes");
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	void layoutApp(boolean menuOut) 
	{
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","+ animParams.bottom + "]");
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
            	if (!Controller.endsWith("Horoscopes") && IsMenuClicked)
    			{
    				appMap.RunActivity(Controller,CategoryName, CategoryID,"");
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
	        Context context = Horoscopes.context;
	        Animation anim;
	        int w = app.getMeasuredWidth();
	        int h = app.getMeasuredHeight();
	        int left = (int) (getResources().getDisplayMetrics().density*255);
	        anim = new TranslateAnimation(0, -left, 0, 0);
	        animParams.init(0, 0, w, h);
	        anim.setDuration(500);
	        anim.setAnimationListener((AnimationListener) context);
	        anim.setFillAfter(true);
	        app.startAnimation(anim);
	    }
	}
}
