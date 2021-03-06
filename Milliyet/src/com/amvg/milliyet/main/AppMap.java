package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mobilike.garantiad.GarantiAdManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public final class AppMap extends FragmentActivity{
	public static Intent myIntent; //hello
	private Context context;
	private String urlAddress[];
	public static Context appContex;
	public static String controller;
	public static String categoryName;
	public static String ID;
	public static String categoryID;
	public Context contextDialog;
	
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	
	public static void setContext(Context context)
	{
		appContex=context;
	}
	
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
	
	@Override
	public void onCreate(Bundle cicle) {
		super.onCreate(cicle);
		GarantiAdManager.loadAd(this, providerUrl,
				new GarantiAdManager.AdListener() { 		
			@Override
			public void onLoad()
			{
			}
			
			@Override
			public void onError()
			{
			}			
		});
	}
	
	public AppMap(Context context, Context contextDialog)
	{
		this.context=context;
		myIntent = new Intent(context, Home.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		urlAddress = new String[1];
		this.contextDialog=contextDialog;
	}
    public void RunActivity(String controller, String categoryName, String ID, String categoryID)
    {
//    	if (checkInternetConnection())
//		{
    		this.controller=controller;
            this.categoryName=categoryName;
            this.ID=ID;
            this.categoryID=categoryID;
            urlAddress[0]=controller;
            if (controller.equals("NewsGallery")) 
            {
    			DownloadDataNewsGallery newsgallery=new DownloadDataNewsGallery();
    			newsgallery.execute(urlAddress);
    		}
            if ((controller.equals("VideoCategory") || controller.equals("videocategory")) && ID.equals("0")) 
            {
    			urlAddress[0]="GalleryCategory";
    		}
            setContext(this.context);
            if (controller.equals("Home") || controller.equals("home")) 
    		{
    			AppMap.myIntent.setClass(appContex, Home.class);
    			appContex.startActivity(AppMap.myIntent);
    		} 
    		else if (controller.equals("BreakingNews") || controller.equals("breakingnews"))
    		{
    			AppMap.myIntent.setClass(appContex, BreakingNews.class);
            	appContex.startActivity(AppMap.myIntent);
    		} 
    		else if (controller.equals("NewsCategory") || controller.equals("newscategory"))   // 
    		{
    			AppMap.myIntent.setClass(appContex, NewsCategory.class);
    			AppMap.myIntent.putExtra("categoryName", categoryName);
    			AppMap.myIntent.putExtra("categoryID", ID);
    			appContex.startActivity(AppMap.myIntent);
    		} 
    		else if (controller.equals("Skorer") || controller.equals("skorer"))
    		{
    			AppMap.myIntent.setClass(appContex, Skorer.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("MilliyetTV") || controller.equals("SkorerTV") || controller.equals("DummySkorerTV") || controller.equals("DummyMilliyetTV"))
    		{
    			AppMap.myIntent.setClass(appContex, Video.class);
    			AppMap.myIntent.putExtra("controller", controller);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("NewsCategories") || controller.equals("newscategories")) 
    		{
    			AppMap.myIntent.setClass(appContex, NewsCategories.class);
    			appContex.startActivity(AppMap.myIntent);
    		} 
    		else if (controller.equals("Columnists") || controller.equals("columnists")) 
    		{
    			AppMap.myIntent.setClass(appContex, Columnists.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("NewsArticle") || controller.equals("newsarticle") || controller.toLowerCase().equals("advertorial")) 
    		{
    			if (controller.toLowerCase().equals("advertorial")) 
    			{
    				new AdvertorialCount().execute("http://secure.milliyet.com.tr/redirect/Default.aspx?z=94&i=1&l=http%3A%2F%2Fm.milliyet.com.tr%2FNews%2FArticle%3FID%3D"+ID); //reklam sayım kodu için bu servisi çalıştırıyoruz
    			}
    			AppMap.myIntent.setClass(appContex, NewsDetail.class);
    			AppMap.myIntent.putExtra("id", ID);
    			appContex.startActivity(AppMap.myIntent);
    		} 
    		else if(controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory"))
    		{
    			AppMap.myIntent.setClass(appContex, BreakingNewsCategory.class);
    			AppMap.myIntent.putExtra("categoryName", categoryName);
    			AppMap.myIntent.putExtra("categoryID", categoryID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("NewsCategories")) 
    		{
    			AppMap.myIntent.setClass(appContex, NewsCategories.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if(controller.equals("ColumnistsOthers") || controller.equals("columnistsothers"))
    		{
    			AppMap.myIntent.setClass(appContex, ColumnistsOthers.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if(controller.equals("ColumnistArticle") || controller.equals("columnistarticle"))
    		{
    			AppMap.myIntent.setClass(appContex, ColumnistsDetail.class);
    			AppMap.myIntent.putExtra("id", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if(controller.equals("ColumnistArticles") || controller.equals("ColumnistArticles"))
    		{
    			AppMap.myIntent.setClass(appContex, ColumnistsWritings.class);
    			AppMap.myIntent.putExtra("id", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if(controller.equals("columnistarticles"))
    		{
    			AppMap.myIntent.setClass(appContex, ColumnistsWritings.class);
    			AppMap.myIntent.putExtra("id", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if(controller.equals("Video") || controller.equals("video") || controller.equals("dummyvideo") || controller.equals("dummymilliyettv") || controller.equals("dummyskorertv"))
    		{
    			AppMap.myIntent.setClass(appContex, Video.class);
    			AppMap.myIntent.putExtra("controller", controller);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("VideoCategory") || controller.equals("videocategory")) 
    		{
    			AppMap.myIntent.setClass(appContex, VideoCategory.class); //ID parametresi hangi sayfaya ait kateogrinin açılacağını belirlemek için kullanılıyor. (Video Category=1/Galeri Category=0)
    			AppMap.myIntent.putExtra("categoryName", categoryName);
    			AppMap.myIntent.putExtra("categoryID", categoryID);
    			AppMap.myIntent.putExtra("ActivityName", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("VideoClip") || controller.equals("videoclip")) // VideoClipByID mi yoksa VideoClipByCode mu olduğunu isID değeri ile belirledik. //categoryName, tvName olarak kullanıldı.
    		{
    			AppMap.myIntent.setClass(appContex, VideoDetail.class);
    			AppMap.myIntent.putExtra("id", ID);
    			AppMap.myIntent.putExtra("isID", "1");
    			AppMap.myIntent.putExtra("TVname", categoryName);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("VideoClipByCode") || controller.equals("videoclipbycode")) 
    		{
    			AppMap.myIntent.setClass(appContex, VideoDetail.class);
    			AppMap.myIntent.putExtra("id", ID);
    			AppMap.myIntent.putExtra("isID", "0");
    			appContex.startActivity(AppMap.myIntent); 
    		}
    		else if (controller.equals("Gallery") || controller.equals("gallery") || controller.toLowerCase().equals("dummygallery"))
    		{
    			AppMap.myIntent.setClass(appContex, Video.class);
    			AppMap.myIntent.putExtra("controller", controller);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("PhotoGallery") || controller.equals("photogallery")) //categoryID değişkenini galleryTitle'ı göndermek için kullandık.
    		{
    			AppMap.myIntent.setClass(appContex, PhotoGallery.class);
    			AppMap.myIntent.putExtra("id", ID);
    			AppMap.myIntent.putExtra("categoryName", categoryName);
    			AppMap.myIntent.putExtra("galleryTitle", categoryID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("VideoPlay")) //categoryName değişkeni url'i göndermek için, ID değişkeni de code'u göndermek için kullanıldı.
    		{
    			AppMap.myIntent.setClass(appContex, VideoPlay.class);
    			AppMap.myIntent.putExtra("url", categoryName);
    			AppMap.myIntent.putExtra("code", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("DummySkorer")) 
    		{
    			AppMap.myIntent.setClass(appContex, NewsCategory.class);
    			AppMap.myIntent.putExtra("categoryName", "Spor");
    			AppMap.myIntent.putExtra("categoryID", "14");
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("DummyMilliyetTV")) 
    		{
    			AppMap.myIntent.setClass(appContex, Video.class);
    			AppMap.myIntent.putExtra("controller", "Video");
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("Weather")) 
    		{
    			AppMap.myIntent.setClass(appContex, Weather.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("Horoscopes")) 
    		{
    			AppMap.myIntent.setClass(appContex, Horoscopes.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("HoroscopesArticle")) 
    		{
    			AppMap.myIntent.setClass(appContex, HoroscopesArticle.class);
    			AppMap.myIntent.putExtra("ID", ID);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("WeatherLocation")) 
    		{
    			AppMap.myIntent.setClass(appContex, WeatherLocation.class);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.equals("VideoLive"))
    		{
    			AppMap.myIntent.setClass(appContex, VideoLive.class);
    			AppMap.myIntent.putExtra("url", categoryName);
    			appContex.startActivity(AppMap.myIntent);
    		}
    		else if (controller.toLowerCase().equals("advertoriallink"))
    		{
    			
    			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://secure.milliyet.com.tr/redirect/Default.aspx?z=94&i=1&l="+URLEncoder.encode(ID).toString()));
    			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			appContex.startActivity(browserIntent);
    		}
    		else if (controller.toLowerCase().equals("weblink"))
    		{
    			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(categoryID)); //categoryID parametresi olarak yönlendirilecek olan web sayfasının URL'ini aldık.
    			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			appContex.startActivity(browserIntent);
    		}
//		}
//    	else
//    	{ 
//    		Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
//    	}
        
    }
    
    public class SearchResponseNewsGallery {
	    public List<ResultNewsGallery> root;
	}

	public class ResultNewsGallery {
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
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("Description")
	    public String Description;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	
	}
	
	public class DownloadDataNewsGallery extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... urlAddress) 
		{
			String jString = null;
			URL url;
			try {
				
				url = new URL("http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsArticle&ArticleID="+AppMap.ID);
				URLConnection ucon = url.openConnection();	
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);	 
				ByteArrayBuffer baf = new ByteArrayBuffer(50);		
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				
				Gson gson = new Gson();
				SearchResponseNewsGallery response = gson.fromJson(jString, SearchResponseNewsGallery.class);
				List<ResultNewsGallery> results = response.root;
				for (ResultNewsGallery result : results) 
				{
					AppMap.categoryName=result.Category;
					AppMap.categoryID=result.Title;
				}
				
				AppMap.myIntent.setClass(appContex, PhotoGallery.class);
				AppMap.myIntent.putExtra("id", ID+"-#");
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("galleryTitle", categoryID);
				appContex.startActivity(AppMap.myIntent);
				
				return jString;
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				e.printStackTrace();
				return "hata2";
			}
		}
	}
    
    public class SearchResponse {
	    public Result root;
	}

	public class Result {
	    @SerializedName("Enabled")
	    public String Enabled;
	
	    @SerializedName("Width")
	    public String Width;
	    
	    @SerializedName("Height")
	    public String Height;
	    
	    @SerializedName("ShowCloseButton")
	    public String ShowCloseButton;
	    
	    @SerializedName("URL")
	    public String URL;
	    
	    @SerializedName("HTML")
	    public String HTML;
	
	}
	
	public static String CategoryAdvertisementBanner(String controller)
	{
		if (controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory") || controller.equals("NewsCategory") || controller.equals("newscategory") || controller.equals("VideoCategory") || controller.equals("videocategory")) 
		{
			return "&CategoryID="+categoryID;
		}
		else
		{
			return "";
		}
		
	}
	public static String CategoryAdvertisementInterstitial(String controller)
	{
		if (controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory") || controller.equals("NewsCategory") || controller.equals("newscategory") || controller.equals("VideoCategory") || controller.equals("videocategory")) 
		{
			return "&CategoryID="+categoryID;
		}
		else
		{
			return "";
		}
	}
}

