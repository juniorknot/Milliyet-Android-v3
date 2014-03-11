package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ColumnistsDetail extends FragmentActivity implements OnTouchListener, Handler.Callback
{
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	static public String[] values;
	static public String dirName;
	static public String fileName;
	static public Global global;
	static public WebView webView;
	private String ID;
	private String customHtml;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	JavaScriptInterface JSInterface;
	
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	private static final int CLICK_ON_WEBVIEW = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_columnists_detail);
		
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
		
		this.webView=(WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setOnTouchListener(this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
		     ID = extras.getString("id");
		}
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		this.dirName="/sdcard/Milliyet/columnistsarticles";
		this.fileName=this.dirName+"/"+ID+".txt";
		global=new Global();
		String urlAdress[]={"http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_ColumnistArticle&ArticleID="+ID};
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
		} 
		catch (IOException e) {
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
		final AppMap appmap=new AppMap(getApplicationContext(),ColumnistsDetail.this);
		webView.setWebViewClient(new WebViewClient(){
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	Global.parseURLrequest(url, getApplicationContext(), ColumnistsDetail.this,"");
		        return true;
		    }
		});
		
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				// TODO Auto-generated method stub
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				try 
				{
					Home.setClickable(true);
					Home.setClickable(true);
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
				sharingIntent.setType("text/*");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(customHtml));
				startActivity(Intent.createChooser(sharingIntent,"Share using"));
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
	
	
//	int eventGetActionBeforeKont=-1;
//	boolean eventGetActionBeforeBoolean=false;
	
//	@Override
//	public boolean onTouch(View v, MotionEvent event) { 
////		Log.e("Bastı","Bastı");
//		
//		if (event.getAction()==0)
//		{
////			eventGetActionBeforeKont=0;
//			eventGetActionBeforeBoolean=true;
//		}
//		else if (event.getAction()==2)
//		{
//			eventGetActionBeforeBoolean=false;
//		}
//		else if (event.getAction()==1 && eventGetActionBeforeBoolean && v.getId() == R.id.webView)
//		{
//			Log.e("Girdi","Girdi");
//	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
//		}
//			
//		
////		Log.e("eventGetaction:",Integer.toString(event.getAction()));
////	    if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_MOVE){
////	    	Log.e("Girdi","Girdi");
////	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
////	    }
//	    return false;
//	}

	
	@Override
	public void onStart() 
	{
	    super.onStart();
	    mGaTracker.sendView("ColumnistArticle - "+ID);
	    mGaTrackerGlobal.sendView("ColumnistArticle - "+ID);
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
//        switch (item.getItemId())
//        {
//        case R.id.shareButton:
//        	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//			sharingIntent.setType("text/*");
//			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(customHtml));
//			startActivity(Intent.createChooser(sharingIntent,"Share using"));
//            return true;
//        default:
//            return super.onOptionsItemSelected(item);
//        }
//    } 
	
	public class SearchResponse 
	{
	    public List<Result> root;
	}

	public class Result 
	{
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("ColumnistID")
	    public String ColumnistID;
	    
	    @SerializedName("Columnist")
	    public String Columnist;
	    
	    @SerializedName("CornerName")
	    public String CornerName;
	    
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("Description")
	    public String Description;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	}
	
	public class JavaScriptInterface
	{
        Context mContext;
        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        public void changeActivity()
        {
            finish();
        }
    }
	
	private String createHTML(String ColumnistID, String Columnist, String CornerName, String PublishTime, String Title, String Spot, String Description, String ImageURL){
		
		customHtml = "";
		customHtml += "<!DOCTYPE HTML>";
		customHtml += "<html>";
		customHtml += "<head>";
		customHtml += "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">";
		customHtml += "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"file:///android_asset/css/ColumnistArticleAndroid.css\" />";
//		customHtml += "<link rel=\"stylesheet\" href=\"ColumnistArticleAndroid.css\" />";
		customHtml += "</head>";
		customHtml += "<body data-role=\"page\">";
		customHtml += "<div class=\"HeaderBar\">"+Columnist+" - Yazarlar</div>";
		customHtml += "<div class=\"ColumnistArticle\">";
		if (!ImageURL.equals("")) 
		{
			customHtml=customHtml+"<div class=\"Image\"><img src=\""+ImageURL+"\" /></div>";
		}
		customHtml += "<div class=\"Columnist\">"+Columnist+"</div>";
		customHtml += "<div class=\"CornerName\">"+CornerName+"</div>";
//		customHtml += "<div class=\"ColumnistArticles\"><a href=\"ColumnistArticles://?ID=390\" target=\"_top\">Tüm Yazıları</a></div>";
		customHtml += "<div class=\"ColumnistArticles\"><a href=\"ColumnistArticles://?P="+URLEncoder.encode("{\"ColumnistID\":\""+ColumnistID+"\"}") +"\" target=\"_top\">Tüm Yazıları</a></div>";
		String[] time=PublishTime.split(" ");
		customHtml += "<div class=\"PublishDate\">"+time[0]+"</div>";
		customHtml += "<div class=\"Title\">"+Title+"</div>";
		customHtml += "<div class=\"Spot\">"+Spot+"</div>";
		customHtml += "<div class=\"Description\">"+Description+"</div>";
		customHtml += "</div>";
		customHtml += "<div class=\"Footer\">";
		customHtml += "<p class=\"Copyright\"><b>Copyright &copy; 2014 Milliyet</b></p>";
		customHtml += "</div>";
		customHtml += "</body>";
		customHtml += "</html>";
		
//		customHtml = "";
//		customHtml=customHtml+"<!DOCTYPE HTML>";
//		customHtml=customHtml+"<html>";
//		customHtml=customHtml+"<head>";
//		customHtml=customHtml+"<script type=\"text/javascript\">";
//		customHtml=customHtml+"function displaymessage()";
//		customHtml=customHtml+"{";
//		customHtml=customHtml+"JSInterface.changeActivity();";
//		customHtml=customHtml+"}";
//		customHtml=customHtml+"</script>";
//		customHtml=customHtml+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
//		customHtml=customHtml+"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
//		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"file:///android_asset/css/ColumnistArticleAndroid.css\" />";
//		customHtml=customHtml+"</head>";
//		customHtml=customHtml+"<body>";
//		customHtml+="<div id=\"fb-root\"></div>"; 
//		customHtml+="<script>(function(d, s, id) {";
//		customHtml+="var js, fjs = d.getElementsByTagName(s)[0];";
//		customHtml+="if (d.getElementById(id)) return;";
//		customHtml+="js = d.createElement(s); js.id = id;";
//		customHtml+="js.src = \"http://connect.facebook.net/tr_TR/all.js#xfbml=1\";";
//		customHtml+=" fjs.parentNode.insertBefore(js, fjs);";
//		customHtml+="}(document, \'script\', \'facebook-jssdk\'));</script>";
//		customHtml=customHtml+"<div class=\"HeaderBar\">"+Columnist+" - Yazarlar</div>";
//		customHtml=customHtml+"<div class=\"Content\">";
//		if (!ImageURL.equals("")) {
//			
//			customHtml=customHtml+"<div class=\"Image\"><img src=\""+ImageURL+"\" /></div>";
//		}
//		customHtml=customHtml+"<div class=\"Columnist\">"+Columnist+"</div>";
//		customHtml=customHtml+"<div class=\"CornerName\">"+CornerName+"</div>";
//		customHtml=customHtml+"<div class=\"ColumnistArticles\"><a href=\"ColumnistArticles://?P="+URLEncoder.encode("{\"ColumnistID\":\""+ColumnistID+"\"}") +"\">Tüm Yazıları</a></div>";
//		String[] time=PublishTime.split(" ");
//		customHtml=customHtml+"<div class=\"PublishDate\">"+time[0]+"</div>";
//		customHtml+="<div class=\"SocialMedia\">";
//		customHtml+="<div class=\"fb-like\" data-href=\"http://www.milliyet.com.tr/d/t.aspx?ID="+ID+"\" data-send=\"false\" data-layout=\"button_count\" data-width=\"0\" data-show-faces=\"false\"></div>";
//		customHtml+="<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-url=\"http://www.milliyet.com.tr/d/t.aspx?ID="+ID+"\" data-lang=\"tr\" style=\"display:inline-block\">Tweet</a>";
//		customHtml+="<script>!function (d, s, id) { var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? \'http\' : \'https\'; if (!d.getElementById(id)) { js = d.createElement(s); js.id = id; js.src = p + \'://platform.twitter.com/widgets.js\'; fjs.parentNode.insertBefore(js, fjs); } }(document, \'script\', \'twitter-wjs\');</script>";
//		customHtml+="<div class=\"g-plusone\" data-size=\"medium\" data-href=\"http://www.milliyet.com.tr/d/t.aspx?ID="+ID+"\"></div>";
//		customHtml+="<script type=\"text/javascript\">";
//		customHtml+="window.___gcfg = { lang: \'tr\' };";
//		customHtml+="(function () {";
//		customHtml+="var po = document.createElement(\'script\'); po.type = \'text/javascript\'; po.async = true;";
//		customHtml+="po.src = \'https://apis.google.com/js/plusone.js\';";
//		customHtml+="var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(po, s);";
//		customHtml+="})();";
//		customHtml+="</script>";
//		customHtml+="</div>";
//		customHtml=customHtml+"<div class=\"Title\">"+Title+"</div>";
//		customHtml=customHtml+"<div class=\"Spot\">"+Spot+"</div>";
//		customHtml=customHtml+"<div class=\"Description\">"+Description+"</div>";
//		customHtml=customHtml+"</div>";
//		customHtml=customHtml+"</body>";
//		customHtml=customHtml+"</html>";
		return customHtml;
	}
	
	private void parseAndShowData(String JsonString) throws JSONException{
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		values=new String[9];
		for (Result result : results) {
			webView.loadDataWithBaseURL("http://www.google.com", createHTML(result.ColumnistID, result.Columnist, result.CornerName, result.PublishTime, result.Title, result.Spot, result.Description, result.ImageURL), "text/html", "UTF-8", "");
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
			} catch (MalformedURLException e1) {
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
			} catch (IOException e) {
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
			Home.setClickable(true);
		} 
		catch (Exception e) 
		{
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
		// TODO Auto-generated method stub
		return false;
	}

	int eventGetActionBeforeKont=-1;
	boolean eventGetActionBeforeBoolean=false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		Log.e("Girdi","Girdi");
		// TODO Auto-generated method stub
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
	
//	@Override
//	public boolean handleMessage(Message msg)
//	{
//		Log.e("handleMessage","HandleMessage");
//		// TODO Auto-generated method stub
//	    if (msg.what == CLICK_ON_WEBVIEW){
//	    	if (ShowShareButton)
//			{
//	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.GONE);
//	    		ShowShareButton=false;
//			}
//	    	else
//	    	{
//	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.VISIBLE);
//	    		ShowShareButton=true;
//	    	}
//	    	
////	    	openOptionsMenu();
////	        Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
//	        return true;
//	    }
//		return false;
//	}

}
