package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer; 
import org.json.JSONException;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName; 
import com.mobilike.garantiad.GarantiAdManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NewsDetail extends FragmentActivity implements OnTouchListener, Handler.Callback
{
	
	private static final int CLICK_ON_WEBVIEW = 1;
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	static public String[] values;
	static public String dirName;
	static public String fileName;
	static public Global global;
	static public WebView webView;
	public ImageView logo;
	private String ID;
	static private String customHtml;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		this.webView=(WebView)findViewById(R.id.webView);
		this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setJavaScriptEnabled(true);
		this.webView.setHorizontalScrollBarEnabled(false);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		}
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		
		
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
		
		this.dirName="/sdcard/Milliyet/newsarticles";
		this.fileName=this.dirName+"/"+ID+".txt";
		final AppMap appmap=new AppMap(getApplicationContext(),NewsDetail.this);
		logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		global=new Global();
		String urlAdress[]={"http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsArticle&ArticleID="+ID};
		DownloadData downloadData=new DownloadData();
		try 
		{
			if (global.isFileOK(this.fileName)) 
			{
				this.parseAndShowData(global.readData(this.fileName));
			}
			else
			{
				downloadData.execute(urlAdress);
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
		
//		webView.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Log.e("Bas","Bas");
//			}
//		});
		
		webView.setWebViewClient(new WebViewClient()
		{
			
			
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	String a="a";
		    	//url=URLDecoder.decode(url);
		    	Global.parseURLrequest(url, getApplicationContext(), NewsDetail.this,"");
		        return true;
		    }
		});
		
		webView.setOnTouchListener(this);
		
//		webView.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Log.e("clikc","click");
//			}
//		});
		
		
		
//		webView.setOnTouchListener(new OnTouchListener()
//		
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event)
//			{ 
//				Log.e("Bastı","Bastı"); 
//				// TODO Auto-generated mwethod stub
//				return false;
//			}
//		});
//		if (ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey())
//		{
//			Log.e("hasPermanentMenuKey()","YOK");
//		}
//		else
//		{
//			Log.e("hasPermanentMenuKey()","VAR");
//		}
		
		
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				try
				{
					Home.setClickable(true);
					Skorer.homeMainLayout.setVisibility(View.VISIBLE);
					Skorer.setClickable(true);
				} 
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
//					AppMap.DownloadBannerData.bannerEnabled="false";
				} catch (Exception e) {
					// TODO: handle exception 
				}
				finish();
			}
		});
		
		((ImageView)findViewById(R.id.SharButton)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.milliyet.com.tr/d/t.aspx?ID="+ID); 
				startActivity(Intent.createChooser(sharingIntent,"Share via"));
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
	
	int eventGetActionBeforeKont=-1;
	boolean eventGetActionBeforeBoolean=false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) { 
//		Log.e("Bastı","Bastı");
		
		if (event.getAction()==0)
		{
//			eventGetActionBeforeKont=0;
			eventGetActionBeforeBoolean=true;
		}
		else if (event.getAction()==2)
		{
			eventGetActionBeforeBoolean=false;
		}
		else if (event.getAction()==1 && eventGetActionBeforeBoolean && v.getId() == R.id.webView)
		{
			Log.e("Girdi","Girdi");
	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
		}
			
		
//		Log.e("eventGetaction:",Integer.toString(event.getAction()));
//	    if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_MOVE){
//	    	Log.e("Girdi","Girdi");
//	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
//	    }
	    return false;
	}

	@Override
	public void onStart() 
	{
	    super.onStart();
	    mGaTracker.sendView("NewsArticle - "+ID);
	    mGaTrackerGlobal.sendView("NewsArticle - "+ID);
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.layout.share_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
// 
//        switch (item.getItemId())
//        {
//	        case R.id.shareButton:
//	        	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//				sharingIntent.setType("text/plain");
//				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.milliyet.com.tr/d/t.aspx?ID="+this.ID);
//				startActivity(Intent.createChooser(sharingIntent,"Share via"));
//				return true;
//	        default:
//	        	return super.onOptionsItemSelected(item);
//        }
//    } 

	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("CategoryID")
	    public String CategoryID;
	    
	    @SerializedName("Category")
	    public String Category;
	    
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("Source")
	    public String Source;
	    
	    @SerializedName("Title") 
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("Description")
	    public String Description;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	}
	
	private String createHTML(String Category, String PublishTime, String Title, String Spot, String Description, String ImageURL, String Source)
	{
		
		customHtml="";
		customHtml+="<!DOCTYPE HTML>";
		customHtml+="<html>";
		customHtml+="<head>";
		customHtml+="<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">";
		customHtml+="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml+="<link rel=\"stylesheet\" href=\"file:///android_asset/css/NewsArticleAndroid.css\" />";
		customHtml+="</head>";
		customHtml+="<body data-role=\"page\">";
		customHtml+="<div class=\"HeaderBar\">"+Category+"</div>";
		customHtml+="<div class=\"NewsArticle\">";
		customHtml+="<div class=\"Title\">"+Title+"</div>";
		customHtml+="<div class=\"Spot\">"+Spot+"</div>";
		if (!ImageURL.equals("")) 
		{
			customHtml+="<div class=\"Image\"><img src=\""+ImageURL+"\" /></div>";
		}
		
		customHtml+="<div class=\"PublishTime\">"+PublishTime+"</div>";
		customHtml+="<div class=\"Source\">"+Source+"</div>";
		customHtml+="<div class=\"Description\">"+Description+"</div>";
		customHtml+="</div>";
		customHtml+="<div class=\"Footer\">";
		customHtml+="<p class=\"Copyright\"><b>Copyright &copy; 2014 Milliyet</b></p>";
		customHtml+="</div>";
		customHtml+="</body>";
		customHtml+="</html>";
		return customHtml;
	}
	
	private void parseAndShowData(String JsonString) throws JSONException
	{
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		values=new String[9];
		for (Result result : results)
		{
			if (result.Category.equals("Spor - Skorer"))
			{
				logo.setImageResource(R.drawable.navigation_bar_logo_skorer);
			}
			else
			{
				logo.setImageResource(R.drawable.navigation_bar_logo);
			}
			webView.loadDataWithBaseURL("http://www.google.com", createHTML(result.Category, result.PublishTime, result.Title, result.Spot, result.Description, result.ImageURL,result.Source), "text/html", "UTF-8", "");
		}
	}
	
	public class DownloadData extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... urlAddress) 
		{
			String jString = null;
			URL url;
			try 
			{
				url = new URL(urlAddress[0]);
				URLConnection ucon = url.openConnection();			
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);			
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) 
				{				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				return jString;
			} 
			catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				e.printStackTrace();
				return "hata2";
			}
		}
		protected void onPostExecute(String result) 
		{ 
			try 
			{
				global.writeFile(result, dirName, fileName);
				parseAndShowData(global.readData(fileName));
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	@Override
	public void onBackPressed() 
	{
		try 
		{
			Home.setClickable(true);
			Skorer.homeMainLayout.setVisibility(View.VISIBLE);
			Skorer.setClickable(true);
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		Log.e("handleMessage","HandleMessage");
		// TODO Auto-generated method stub
	    if (msg.what == CLICK_ON_WEBVIEW){
	    	if (ShowShareButton)
			{
	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.GONE);
	    		ShowShareButton=false;
			}
	    	else
	    	{
	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.VISIBLE);
	    		ShowShareButton=true;
	    	}
	    	
//	    	openOptionsMenu();
//	        Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
	        return true;
	    }
		return false;
	}
}
