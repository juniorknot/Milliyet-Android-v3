package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class VideoDetail extends FragmentActivity implements OnTouchListener, Handler.Callback
{
	static public String[] values;
	static public String dirName;
	static public String fileName;
	static public Global global;
	static public WebView webView;
	private String ID;
	static private String customHtml;
	private String  category;
	private String publishTime;
	private String title;
	private String spot;
	private String imageUrl;
	private String viewCount;
	private String code;
	private String webSite;
	private String isID;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	private String TVname;
	public ImageView logo;
	
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	private static final int CLICK_ON_WEBVIEW = 1;
	
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		comScore.setAppContext(this.getApplicationContext());
		
		this.webView=(WebView)findViewById(R.id.webView);
		this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setOnTouchListener(this);
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
	        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
	            Log.e("js ", message);
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
		
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		webView.getSettings().setAppCacheEnabled(true);
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		     isID=extras.getString("isID");
		     TVname=extras.getString("TVname");
		}
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.HasBanner)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		this.dirName="/sdcard/Milliyet/videodetail";
		this.fileName=this.dirName+"/"+ID+".txt";
		global=new Global();
		String urlAdress[]={"http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VideoClipByID&VideoClipID="+ID};
		if (isID.equals("0")) {
			urlAdress[0]="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VideoClipByCode&Code="+ID;
		}
		DownloadData downloadData=new DownloadData();
		
		try {
			if (global.isFileOK(this.fileName)) {
				this.parseAndShowData(global.readData(this.fileName));
			}else{
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
		
//		webViewBanner=(WebView)findViewById(R.id.webViewBanner);
//		webViewBanner.getSettings().setJavaScriptEnabled(true);
//		webViewBanner.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webViewBanner.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
//		{
//			
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				webViewBanner.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				webViewBanner.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//		}
//		else
//		{
//			webViewBanner.getLayoutParams().width=0;
//       		webViewBanner.getLayoutParams().height=0;
//		}
		webView.setWebViewClient(new WebViewClient(){  //aç aç
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    { 
		    	Log.e("request sent","request sent");
		    	Global.parseURLrequest(url, getApplicationContext(), VideoDetail.this,code);
		    	return true;
		    }
		});
		
		//downloadData.execute(urlAdress);
		final AppMap appmap=new AppMap(getApplicationContext(),VideoDetail.this);
		logo=(ImageView)findViewById(R.id.logoImage);
		if (!isID.equals("0"))
		{
			if (TVname.equals("MilliyetTV") )
			{
				logo.setImageResource(R.drawable.navigation_bar_logo_milliyettv);
			}
			else
			{
				logo.setImageResource(R.drawable.navigation_bar_logo_skorertv);
			}
		}
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TVname.equals("MilliyetTV") || TVname.equals("SkorerTV"))
				{
					appmap.RunActivity(TVname, "", "","");
				}
				else
				{
					appmap.RunActivity("Home", "", "", "");
				}
				
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				
				// TODO Auto-generated method stub
				//appmap.RunActivity(context, controller, ref, title, CID)
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Home.setClickable(true);
					Video.backgroungLayout.setVisibility(View.VISIBLE);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
//				AppMap.DownloadBannerData.bannerEnabled="false";
				finish();
			}
		});
		
		/*webView.setWebViewClient(new WebViewClient(){
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	url=URLDecoder.decode(url);
		    	webView.loadUrl(url);*/
		    	//Global.parseURLrequest(url, getApplicationContext());
		    	/*Log.e("URL",url);
		    	url=url.toLowerCase();
		    	Log.e("soru isareti","://?P=");
		    	String[] controller=new String[2];
		    	String[] ID=new String[2];
		    	controller=url.split("://");
		    	ID=controller[1].split("p=");
		    	appmap.RunActivity(controller[0], "", ID[1],"");*/
		        //return true;
		    //}
		//});
		
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
	

	@Override
	  public void onStart() {
	    super.onStart();

	    // Send a screen view when the Activity is displayed to the user.
	    
	    	mGaTracker.sendView("VideoClip - "+ID);
	    	mGaTrackerGlobal.sendView("VideoClip - "+ID);
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
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.video_detail, menu);
//		return true;
//	}
//	
//	@Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
// 
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
	
	@Override
	public void onBackPressed() {
//	   AppMap.DownloadBannerData.bannerEnabled="false";
	   try {
		   TestFragmentVideo.image.setClickable(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
	   try {
		   Home.setClickable(true);
		   Video.backgroungLayout.setVisibility(View.VISIBLE);
	   } catch (Exception e) {
		// TODO: handle exception
	   }
	   finish();
	}
	
	public static String DownloadText(String url){
	    StringBuffer result = new StringBuffer();
	    try{
	        URL jsonUrl = new URL(url);

	        InputStreamReader isr  = new InputStreamReader(jsonUrl.openStream());

	        BufferedReader in = new BufferedReader(isr);

	        String inputLine;

	        while ((inputLine = in.readLine()) != null){
	            result.append(inputLine);
	        }
	        in.close();
	        isr.close();
	    }catch(Exception ex){
	        result = new StringBuffer("TIMEOUT");
	        //Log.e(Util.AppName, ex.toString());
	    }
	        
	    return result.toString();
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Code") 
	    public String Code;
	    
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("CategoryID")
	    public String CategoryID;
	    
	    @SerializedName("Category")
	    public String Category;
	    
	    @SerializedName("Website")
	    public String  Website;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	    
	    @SerializedName("ViewCount")
	    public String ViewCount;
	    
	    @SerializedName("CommentCount")
	    public String CommentCount;
	    
	    @SerializedName("PositiveVoteCount")
	    public String PositiveVoteCount;
	    
	    @SerializedName("NegativeVoteCount")
	    public String NegativeVoteCount;
	
	}
	
	private String createHTML(String Category, String PublishTime, String Title, String Spot, String ImageURL, String VideoLink, String ViewCount, String Code){
		
		customHtml="";
		customHtml+="<!DOCTYPE HTML>";
		customHtml+="<html>";
		customHtml+="<head>";
		customHtml+="<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">";
		customHtml+="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml+="<link rel=\"stylesheet\" href=\"file:///android_asset/css/VideoClipAndroid.css\" />";
		customHtml+="</head>";
		customHtml+="<body data-role=\"page\">";
		customHtml+="<div class=\"HeaderBar\">"+Category+" - "+TVname+"</div>";
		if (!ImageURL.equals("")) 
		{
			String ImageResizeURL="http://im.milliyet.com.tr/320/0/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg";//http://video.milliyet.com.tr/d/h/IosMobile.ashx?VideoCode="+Code
			customHtml+="<div class=\"VideoClipImage\"><a href=\""+VideoLink+"\" target=\"_top\">";//<img src=\""+ImageResizeURL+"\" /><span class=\"VideoPlayIcon\"></span></a></div>";
			customHtml+="<div class=\"VideoImage\"><img src=\""+ImageResizeURL+"\" /><span class=\"VideoPlay\"></span></div></a></div>";
		}
//		customHtml+="<div class=\"VideoClipImage\"><a href= target=\"_top\">";
//		customHtml+="<div class=\"VideoImage\"><img src= /><span class=\"VideoPlay\"></span></div></a></div>";
		customHtml+="<div class=\"VideoClip\">";
		customHtml+="<div class=\"Title\">"+Title+"</div>";
		customHtml+="<div class=\"Spot\">"+Spot+"</div>";
		String[] time=PublishTime.split(" ");
		customHtml+="<div class=\"PublishTime\"><b>Yayınlanma Tarihi:</b> "+time[0]+"</div>";
		customHtml+="<div class=\"ViewCount\"><b>İzlenme Sayısı:</b>"+ViewCount+"</div>";
		customHtml+="</div>";
		customHtml+="<div class=\"Footer\">";
		customHtml+="<p class=\"Copyright\"><b>Copyright &copy; 2014 Milliyet</b></p>";
		customHtml+="</div>";
		customHtml+="</body>";
		customHtml+="</html>";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		customHtml="";
//		customHtml=customHtml+"<!DOCTYPE HTML>";
//		customHtml=customHtml+"<html>";
//		customHtml=customHtml+"<head>";
//		customHtml=customHtml+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
//		customHtml=customHtml+"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
//		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"file:///android_asset/css/VideoClipAndroid.css\" />";
//		customHtml=customHtml+"</head>";
//		customHtml=customHtml+"<body>";
//		
//		customHtml+="<div id=\"fb-root\"></div>";
//		customHtml+="<script>(function(d, s, id) {";
//		customHtml+="var js, fjs = d.getElementsByTagName(s)[0];";
//		customHtml+="if (d.getElementById(id)) return;";
//		customHtml+="js = d.createElement(s); js.id = id;";
//		customHtml+="js.src = \"http://connect.facebook.net/tr_TR/all.js#xfbml=1\";";
//		customHtml+=" fjs.parentNode.insertBefore(js, fjs);";
//		customHtml+="}(document, \'script\', \'facebook-jssdk\'));</script>";
//		
//		customHtml=customHtml+"<div class=\"HeaderBar\">"+Category+" - "+TVname+"</div>"; 
//		
//		
//		
//		if (!ImageURL.equals("")) {
//			String ImageResizeURL="http://im.milliyet.com.tr/320/0/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg";//http://video.milliyet.com.tr/d/h/IosMobile.ashx?VideoCode="+Code
//			customHtml=customHtml+"<div class=\"ContentImage\"><a href=\""+VideoLink+"\"><img src=\""+ImageResizeURL+"\" /><span class=\"VideoPlayIcon\"></span></a></div>";
//		}
//		customHtml=customHtml+"<div class=\"Content\">";
//		customHtml=customHtml+"<div class=\"Title\">"+Title+"</div>";
//		customHtml=customHtml+"<div class=\"Spot\">"+Spot+"</div>";
//		
//		customHtml+="<div class=\"SocialMedia\">";
//		customHtml+="<div class=\"fb-like\" data-href=\"http://www.milliyet.tv/video-izle/m-"+code+".html\" data-send=\"false\" data-layout=\"button_count\" data-width=\"0\" data-show-faces=\"false\"></div>";
//		customHtml+="<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-url=\"http://www.milliyet.tv/video-izle/m-"+code+".html\" data-lang=\"tr\" style=\"display:inline-block\">Tweet</a>";
//		customHtml+="<script>!function (d, s, id) { var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? \'http\' : \'https\'; if (!d.getElementById(id)) { js = d.createElement(s); js.id = id; js.src = p + \'://platform.twitter.com/widgets.js\'; fjs.parentNode.insertBefore(js, fjs); } }(document, \'script\', \'twitter-wjs\');</script>";
//		customHtml+="<div class=\"g-plusone\" data-size=\"medium\" data-href=\"http://www.milliyet.tv/video-izle/m-"+code+".html\"></div>";
//		customHtml+="<script type=\"text/javascript\">";
//		customHtml+="window.___gcfg = { lang: \'tr\' };";
//		customHtml+="(function () {";
//		customHtml+="var po = document.createElement(\'script\'); po.type = \'text/javascript\'; po.async = true;";
//		customHtml+="po.src = \'https://apis.google.com/js/plusone.js\';";
//		customHtml+="var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(po, s);";
//		customHtml+="})();";
//		customHtml+="</script>";
//		customHtml+="</div>";
//		
////		customHtml=customHtml+"<div class=\"SocialMedia\">";
////		customHtml=customHtml+"<div class=\"fb-like fb_edge_widget_with_comment fb_iframe_widget\" data-href=\"http://www.milliyet.tv/video-izle/m-kJkP5odFTVQX.html\" data-send=\"false\" data-layout=\"button_count\" data-width=\"0\" data-show-faces=\"false\" fb-xfbml-state=\"rendered\"><span style=\"height: 20px; width: 85px;\"><iframe id=\"f3a0e3b1bc\" name=\"f16aa9011\" scrolling=\"no\" title=\"Like this content on Facebook.\" class=\"fb_ltr\" src=\"http://www.facebook.com/plugins/like.php?api_key=&amp;channel_url=http%3A%2F%2Fstatic.ak.facebook.com%2Fconnect%2Fxd_arbiter.php%3Fversion%3D27%23cb%3Df25a8403a8%26domain%3Dm.milliyet.com.tr%26origin%3Dhttp%253A%252F%252Fm.milliyet.com.tr%252Ff35beefae4%26relation%3Dparent.parent&amp;colorscheme=light&amp;extended_social_context=false&amp;href=http%3A%2F%2Fwww.milliyet.tv%2Fvideo-izle%2Fm-kJkP5odFTVQX.html&amp;layout=button_count&amp;locale=tr_TR&amp;node_type=link&amp;sdk=joey&amp;send=false&amp;show_faces=false&amp;width=90\" style=\"border: none; overflow: hidden; height: 20px; width: 85px;\"></iframe></span></div>";
////		customHtml=customHtml+"<iframe id=\"twitter-widget-0\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"http://platform.twitter.com/widgets/tweet_button.1381275758.html#_=1381497368324&amp;count=horizontal&amp;id=twitter-widget-0&amp;lang=tr&amp;original_referer=http%3A%2F%2Fm.milliyet.com.tr%2FVideo%2FVideoClip%3FID%3D162892&amp;size=m&amp;text=Oturmaya%20m%C4%B1%20Geldik%2009.10.2013%20-%20Video%20-%20Milliyet&amp;url=http%3A%2F%2Fwww.milliyet.tv%2Fvideo-izle%2Fm-kJkP5odFTVQX.html\" class=\"twitter-share-button twitter-tweet-button twitter-count-horizontal\" title=\"Twitter Tweet Button\" data-twttr-rendered=\"true\" style=\"width: 122px; height: 20px;\"></iframe>";
////		customHtml=customHtml+"<script>!function (d, s, id) { var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? \'http\' : \'https\'; if (!d.getElementById(id)) { js = d.createElement(s); js.id = id; js.src = p + \'://platform.twitter.com/widgets.js\'; fjs.parentNode.insertBefore(js, fjs); } }(document, \'script\', \'twitter-wjs\');</script>";
////		customHtml=customHtml+"<div id=\"___plusone_0\" style=\"text-indent: 0px; margin: 0px; padding: 0px; background-color: transparent; border-style: none; float: none; line-height: normal; font-size: 1px; vertical-align: baseline; display: inline-block; width: 90px; height: 20px; background-position: initial initial; background-repeat: initial initial;\"><iframe frameborder=\"0\" hspace=\"0\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" style=\"position: static; top: 0px; width: 90px; margin: 0px; border-style: none; left: 0px; visibility: visible; height: 20px;\" tabindex=\"0\" vspace=\"0\" width=\"100%\" id=\"I0_1381497368288\" name=\"I0_1381497368288\" src=\"https://apis.google.com/u/0/_/+1/fastbutton?usegapi=1&amp;bsv=o&amp;size=medium&amp;hl=tr&amp;origin=http%3A%2F%2Fm.milliyet.com.tr&amp;url=http%3A%2F%2Fwww.milliyet.tv%2Fvideo-izle%2Fm-kJkP5odFTVQX.html&amp;gsrc=3p&amp;ic=1&amp;jsh=m%3B%2F_%2Fscs%2Fapps-static%2F_%2Fjs%2Fk%3Doz.gapi.tr.6vawIIHAgmA.O%2Fm%3D__features__%2Fam%3DAQ%2Frt%3Dj%2Fd%3D1%2Frs%3DAItRSTMn5TuwQ6i1RKGE-3MTeE22mtxR0Q#_methods=onPlusOne%2C_ready%2C_close%2C_open%2C_resizeMe%2C_renderstart%2Concircled%2Cdrefresh%2Cerefresh%2Conload&amp;id=I0_1381497368288&amp;parent=http%3A%2F%2Fm.milliyet.com.tr&amp;pfname=&amp;rpctoken=37869307\" data-gapiattached=\"true\" title=\"+1\"></iframe></div>";
////		customHtml=customHtml+"</div>";
//		String[] time=PublishTime.split(" ");
//		customHtml=customHtml+"<div class=\"PublishDate\"><b>Yayınlanma Tarihi:</b> "+time[0]+"</div>";
//		customHtml=customHtml+"<div class=\"ViewCount\"><b>İzlenme Sayısı:</b>"+ViewCount+"</div>";
////		customHtml=customHtml+"<div class=\"Category\"><b>Kategorisi:</b>"+Category+"</div>";
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
			//webView.loadUrl("http://milliyet-p.mncdn.com/web/video/2013/04/16/135465_360p.mp4");
			String[] url={"http://video.milliyet.com.tr/d/h/IosMobile.ashx?VideoCode="+result.Code};
			ReadURLData readURLdata=new ReadURLData();
			readURLdata.execute(url);
			category=result.Category;
			publishTime=result.PublishTime;
			title=result.Title;
			spot=result.Spot;
			imageUrl=result.ImageURL;
			viewCount=result.ViewCount;
			code=result.Code;
			webSite=result.Website;
			
			
			//webView.loadDataWithBaseURL("file:///android_asset/", createHTML(result.Category, result.PublishTime, result.Title, result.Spot, result.ImageURL, result.Code, result.ViewCount), "text/html", "UTF-8", "");
			
		}
		
	}
	
	public class ReadURLData extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			StringBuffer result = new StringBuffer();
		    try{
		        URL jsonUrl = new URL(params[0]);
		        InputStreamReader isr  = new InputStreamReader(jsonUrl.openStream());
		        BufferedReader in = new BufferedReader(isr);
		        String inputLine;
		        while ((inputLine = in.readLine()) != null){
		            result.append(inputLine);
		        }
		        in.close();
		        isr.close();
		    }catch(Exception ex){
		        result = new StringBuffer("TIMEOUT");
		        //Log.e(Util.AppName, ex.toString());
		    }
		    
		    return result.toString(); //code olarak gönderiliyor
		}
			
		protected void onPostExecute(String result) { 
			if (isID.equals("0"))
			{
				if (webSite.equals("Milliyet TV") )
				{
					TVname="MilliyetTV";
					logo.setImageResource(R.drawable.navigation_bar_logo_milliyettv);
				}
				else if(webSite.equals("Skorer TV"))
				{
					TVname="SkorerTV";
					logo.setImageResource(R.drawable.navigation_bar_logo_skorertv);
				}
				else if(webSite.equals("Nevidyo"))
				{
					TVname="Nevidyo";
				}
			}
			webView.loadDataWithBaseURL("http://www.google.com", createHTML(category, publishTime, title, spot, imageUrl, result, viewCount, code), "text/html", "UTF-8", null); // http://www.example.com //file:///android_asset/
			//global.writeFile(result, dirName, fileName);
			//parseAndShowData(global.readData(fileName));
	     }
		
	}
	
	public class DownloadData extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... urlAddress) {
			String jString = null;
			URL url;
			//InputStream is = null;
			
			
			try {
				url = new URL(urlAddress[0]);
				URLConnection ucon = url.openConnection();			
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);			
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
//				return baf.toByteArray().toString();
				jString = new String(baf.toByteArray());
				return jString;
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				Log.e("hata",e.toString());
				e.printStackTrace();
				Log.e("hata",e.toString());
				return e.toString();
			}
	}
		protected void onPostExecute(String result) { 
			
			try {
				Log.e("hata",result);
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
		// TODO Auto-generated method stub
		return false;
	}
}
