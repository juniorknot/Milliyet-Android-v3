package com.amvg.milliyet.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;

import com.amvg.milliyet.main.Home.AnimParams;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobilike.garantiad.GarantiAdManager;

import de.madvertise.android.sdk.MadvertiseView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BreakingNews extends FragmentActivity implements AnimationListener
{
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	ListView listView;
	TextView typeText;
	TextView newsArticleText;
	TextView idText;
	TextView categoryIdText;
	TextView IDtext;
	String type;
	String newsArticle;
	String ID;
	String categoryId;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	public static MadvertiseView MadView;
	public static View ConvertView;
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	public  View menu;
	public  View app;
	public ViewUtils viewUtils;
    public boolean menuOut = false;
	public static Context context; 
	public  AnimParams animParams = new AnimParams();
	public ListView listView_menu;
	public AppMap appmap;
	
	class ClickListener implements OnClickListener 
	{
        @Override
        public void onClick(View v) 
        {
        	if (Home.HasBanner)
			{
				MadView.setVisibility(View.GONE);
				ConvertView.setVisibility(View.GONE);
			}
            System.out.println("onClick " + new Date());
            BreakingNews me = BreakingNews.this;
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
            anim.setRepeatCount(0);
            app.startAnimation(anim);
        }
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breaking_news);
		Log.e("hello","hi");
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		viewUtils=new ViewUtils();
		
		menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        viewUtils.printView("menu", menu);
        viewUtils.printView("app", app);
        
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
		
        listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
        listView_menu.invalidateViews();
	    MainMenuAccessData mainMenuAccessData=new MainMenuAccessData(getApplicationContext(),listView_menu);
	    mainMenuAccessData.execute("");
        viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), BreakingNews.this);
		
		listView=(ListView)findViewById(R.id.list);
		
		final GlobalBreakingNews breakingNews=new GlobalBreakingNews(getApplicationContext(), listView, this);
		appmap=new AppMap(getApplicationContext(), BreakingNews.this);
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "","", "");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.menuImage);
		menuLogo.setOnClickListener(new ClickListener());
		final String[] in={"TopCount=6"};
		ImageView refreshLogo=(ImageView)findViewById(R.id.refreshImage);
		refreshLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GlobalBreakingNews refreshBreakingNews =new GlobalBreakingNews(getApplicationContext(), listView, BreakingNews.this);
				refreshBreakingNews.execute(in);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		    {
		    	if(position!=0) // || position !=2
		    	{
		    		try
					{
		    			typeText=(TextView)view.findViewById(R.id.type);
			            type=typeText.getText().toString();
			            if (type.equals("0")) { //0 ise kategori ismine tıkladı. kategori sayfasını açmalı.
			            	categoryIdText=(TextView)view.findViewById(R.id.categoryId);//category Name
							categoryId=categoryIdText.getText().toString();
			            	appmap.RunActivity("BreakingNewsCategory", ((TextView)view.findViewById(R.id.categoryName)).getText().toString(), "",((TextView)view.findViewById(R.id.categoryId)).getText().toString());
						}
			            else
			            {
							appmap.RunActivity(((TextView)view.findViewById(R.id.contentType)).getText().toString(), "",((TextView)view.findViewById(R.id.ID)).getText().toString() ,"");
						}
					} catch (Exception e)
					{
						// TODO: handle exception
					}
		    	}
		    }
		});
		try { 
			if (breakingNews.areFilesOK()) {
				breakingNews.readData();
			}else{
				breakingNews.execute(in);
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
	    Log.e("onStart","SAY");
	    mGaTracker.sendView("BreakingNews");
	    mGaTrackerGlobal.sendView("BreakingNews");
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
		Log.e("onBackPressed","onBackPressed");
	   finish();
	}

	void layoutApp(boolean menuOut) {
		Log.e("layoutApp","layoutApp");
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
        app.clearAnimation();
    }
	
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationEnd","onAnimationEnd");
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
        menuOut = !menuOut;
        if (!menuOut) {
            menu.setVisibility(View.INVISIBLE);
            listView_menu.setEnabled(true);
            if (Controller!=null)
			{
            	if (!Controller.endsWith("BreakingNews") && IsMenuClicked)
    			{
    				appmap.RunActivity(Controller,CategoryName, CategoryID,"");
    				IsMenuClicked=false;
    			}
			}
        }
        layoutApp(menuOut);
	}


	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationRepeat","onAnimationRepeat");
	}


	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationStart","onAnimationStart");
	}
	
class ViewUtils {
		
	    private ViewUtils() {
	    	Log.e("ViewUtils","ViewUtils");
	    }

	    public void setViewWidths(View view, View[] views) {
	    	Log.e("setViewWidths","setViewWidths");
	        int w = view.getWidth();
	        int h = view.getHeight();
	        for (int i = 0; i < views.length; i++) {
	            View v = views[i];
	            v.layout((i + 1) * w, 0, (i + 2) * w, h);
	            printView("view[" + i + "]", v);
	        }
	    }

	    public void printView(String msg, View v) {
	    	Log.e("printView","printView");
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
	        Log.e("initListView","initListView");
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
	    	Log.e("itemClickMenuCloser","itemClickMenuCloser");
	        Context context = BreakingNews.context;
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