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

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class HoroscopesArticle extends Activity implements OnTouchListener, Handler.Callback
{
	private String ID;
	private View BannerDivider;
	private String DirName;
	private String FileName;
	private WebView webView;
	static public Global global;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	private static final int CLICK_ON_WEBVIEW = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horoscopes_article);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
		     ID = extras.getString("ID");
		}
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		this.DirName="/sdcard/Milliyet/horoscopesarticle";
		this.FileName=this.DirName+"/"+ID+".txt";
		global=new Global();
		String urlAdress[]={"http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_HoroscopesForecast&SignID="+ID};
		webView=(WebView)findViewById(R.id.WebView);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setOnTouchListener(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new WebViewClient(){
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	Global.parseURLrequest(url, getApplicationContext(), HoroscopesArticle.this,"");
		        return true;
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
		DownloadData downloadData=new DownloadData();
		try
		{
			if (global.isFileOK(this.FileName)) 
			{
				this.parseAndShowData(global.readData(this.FileName));
			}
			else
			{
				downloadData.execute(urlAdress);
			}
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		((ImageView)findViewById(R.id.SharButton)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://astroloji.milliyet.com.tr/Astroloji/Default.aspx?aType=BurcDetay&ARTICLETYPEID=6&parCATID="+ID);
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
	
	@Override
	public void onStart() 
	{
	    super.onStart();
	    mGaTracker.sendView("HoroscopesForecast - "+ID);
	    mGaTrackerGlobal.sendView("HoroscopesForecast - "+ID);
	    FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Name")
	    public String Name;
	    
	    @SerializedName("Element")
	    public String Element;
	    
	    @SerializedName("DateRange")
	    public String DateRange;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Description")
	    public String Description;
	
	}
	
	private String createHtml(String id, String name, String element, String dataRange, String title, String description)
	{
		String imageName;
		if (Integer.parseInt(id)==150)
		{
			imageName="Koc";
		}
		else if (Integer.parseInt(id)==151)
		{
			imageName="Boga";
		}
		else if (Integer.parseInt(id)==152)
		{
			imageName="Ikizler";
		}
		else if (Integer.parseInt(id)==153)
		{
			imageName="Yengec";
		}
		else if (Integer.parseInt(id)==155)
		{
			imageName="Basak";
		}
		else if (Integer.parseInt(id)==159)
		{
			imageName="Oglak";
		}
		else if (Integer.parseInt(id)==161)
		{
			imageName="Balik";
		}
		else
		{
			imageName=name;
		}
		String customHtml="";
		customHtml=customHtml+"<!DOCTYPE HTML>";
		customHtml=customHtml+"<html>";
		customHtml=customHtml+"<head>";
		customHtml=customHtml+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
		customHtml=customHtml+"<meta name=\"viewport\" content=\"widqth=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"file:///android_asset/css/Horoscopes.css\" />";
		customHtml=customHtml+"</head>";
		customHtml=customHtml+"<body>";
		
		customHtml+="<div id=\"fb-root\"></div>";
		customHtml+="<script>(function(d, s, id) {";
		customHtml+="var js, fjs = d.getElementsByTagName(s)[0];";
		customHtml+="if (d.getElementById(id)) return;";
		customHtml+="js = d.createElement(s); js.id = id;";
		customHtml+="js.src = \"http://connect.facebook.net/tr_TR/all.js#xfbml=1\";";
		customHtml+=" fjs.parentNode.insertBefore(js, fjs);";
		customHtml+="}(document, \'script\', \'facebook-jssdk\'));</script>";
		
		customHtml=customHtml+"<div class=\"HeaderBar\">"+name+" - Astroloji</div>";
		
		customHtml+="<div class=\"Horoscope\">";
		customHtml+="<div class=\"Image\"><img src=\"http://m.milliyet.com.tr/Images/Horoscopes/"+imageName+".png\" alt=\"\"></div>";
		customHtml+="<div class=\"Name\">"+name+"</div>";
		customHtml+="<div class=\"Element\">"+element+"</div>";
		customHtml+="<div class=\"DateRange\">"+dataRange+"</div>";
		customHtml+="<div class=\"Title\">"+name.toUpperCase()+" Günlük Burç Yorumları</div>";
		customHtml+=" <div class=\"Description\">"+description+"</div>"; 
		
//		customHtml+="<div class=\"SocialMedia\">";
//		customHtml+="<div class=\"fb-like\" data-href=\"http://astroloji.milliyet.com.tr/Astroloji/Default.aspx?aType=BurcDetay&ARTICLETYPEID=6&parCATID="+ID+"\" data-send=\"false\" data-layout=\"button_count\" data-width=\"0\" data-show-faces=\"false\"></div>";
//		customHtml+="<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-url=\"http://astroloji.milliyet.com.tr/Astroloji/Default.aspx?aType=BurcDetay&ARTICLETYPEID=6&parCATID="+ID+"\" data-lang=\"tr\" style=\"display:inline-block\">Tweet</a>";
//		customHtml+="<script>!function (d, s, id) { var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? \'http\' : \'https\'; if (!d.getElementById(id)) { js = d.createElement(s); js.id = id; js.src = p + \'://platform.twitter.com/widgets.js\'; fjs.parentNode.insertBefore(js, fjs); } }(document, \'script\', \'twitter-wjs\');</script>";
//		customHtml+="<div class=\"g-plusone\" data-size=\"medium\" data-href=\"http://astroloji.milliyet.com.tr/Astroloji/Default.aspx?aType=BurcDetay&ARTICLETYPEID=6&parCATID="+ID+"\"></div>";
//		customHtml+="<script type=\"text/javascript\">";
//		customHtml+="window.___gcfg = { lang: \'tr\' };";
//		customHtml+="(function () {";
//		customHtml+="var po = document.createElement(\'script\'); po.type = \'text/javascript\'; po.async = true;";
//		customHtml+="po.src = \'https://apis.google.com/js/plusone.js\';";
//		customHtml+="var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(po, s);";
//		customHtml+="})();";
//		customHtml+="</script>";
//		customHtml+="</div>";
		
		customHtml+="</div>";
		customHtml+="<div class=\"Footer\">";
		customHtml+="<p class=\"Copyright\"><b>Copyright &copy; 2013 Milliyet</b></p>";
		customHtml+="</div>";
		
		customHtml=customHtml+"</body>";
		customHtml=customHtml+"</html>";
		return customHtml;
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
//				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://astroloji.milliyet.com.tr/Astroloji/Default.aspx?aType=BurcDetay&ARTICLETYPEID=6&parCATID="+this.ID);
//				startActivity(Intent.createChooser(sharingIntent,"Share via"));
//				return true;
//	        default:
//	        	return super.onOptionsItemSelected(item);
//        }
//    } 
	
	private void parseAndShowData(String JsonString) throws JSONException
	{
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		for (Result result : results) 
		{
			Log.e("HTML",createHtml(result.ID, result.Name, result.Element, result.DateRange, result.Title, result.Description));
			webView.loadDataWithBaseURL("http://www.google.com", createHtml(result.ID, result.Name, result.Element, result.DateRange, result.Title, result.Description), "text/html", "UTF-8", "");
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
				global.writeFile(result, DirName, FileName);
				parseAndShowData(global.readData(FileName));
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
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
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
	
	int eventGetActionBeforeKont=-1;
	boolean eventGetActionBeforeBoolean=false;

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
//		Log.e("Girdi","Girdi");
		Log.e("eventGetAction",Integer.toString(event.getAction()));
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
		else if (event.getAction()==1 && eventGetActionBeforeBoolean && v.getId() == R.id.WebView)
		{
			Log.e("Girdi","Girdi");
	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
		}
		// TODO Auto-generated method stub
		return false;
	}

}
