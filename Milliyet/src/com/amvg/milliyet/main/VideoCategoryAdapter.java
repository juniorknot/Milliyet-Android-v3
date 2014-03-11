package com.amvg.milliyet.main;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoCategoryAdapter extends ArrayAdapter<String> {
	private final Context context;
	private String[][] realValues;
	private TextView ID;
	private TextView Code;
	private ViewHolder holder;
	private boolean haveBanner;
	private int bannerK;
	private String ActivityName;
	private Context ContextDialog;
	public int ValuesLength;
	public int year;
 
	public VideoCategoryAdapter(Context context, String[] values, String[][] realValues, String ActivityName, Context contextDialog) {
		
		super(context, R.layout.news_items, values);
		this.context = context;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		this.ContextDialog=contextDialog;
		haveBanner=false;
		bannerK=0;
		this.ActivityName=ActivityName;
		this.ValuesLength=values.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
	}
	
	private class ViewHolder {
        TextView saatTitle;
        TextView haberTitle;
        WebView webView;
	}

   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			if (ActivityName.equals("MilliyetTV")) 
			{
				holder.saatTitle.setText(realValues[0][0]+" - MilliyetTV");
			}
			else if (ActivityName.equals("SkorerTV"))
			{
				holder.saatTitle.setText(realValues[0][0]+" - SkorerTV");
			}
			else
			{
				holder.saatTitle.setText("Galeri - "+realValues[0][0]);
			}
		}
		else if ((Home.HasBanner || haveBanner) && position==1)
		{
			haveBanner=true;
			convertView=inflater.inflate(R.layout.banner_item, null);
			bannerK=1;
		}
		else if (position==ValuesLength-1)
		{
			convertView=inflater.inflate(R.layout.copyright_item, null);
			((TextView)convertView.findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");
		}
//		else if ((AppMap.DownloadBannerData.bannerEnabled.equals("true") || haveBanner) && position==1) 
//		{
//			haveBanner=true;
//			convertView=inflater.inflate(R.layout.banner,null);
//			holder.webView=(WebView)convertView.findViewById(R.id.webView);
//			holder.webView.setWebViewClient(new WebViewClient(){
//			    @Override
//			    public boolean shouldOverrideUrlLoading(WebView wView, String url)
//			    {
//			    	Global.parseURLrequest(url, context, ContextDialog,"");
//			        return true;
//			    }
//			});
//			holder.webView.getLayoutParams().height=(int) ((Integer.parseInt(AppMap.DownloadBannerData.bannerHeight)+2)*SplashScreen.scale);
//			holder.webView.getSettings().setJavaScriptEnabled(true);
//			holder.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//			holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				holder.webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				holder.webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			bannerK=1;
//		}
		else 
		{
			convertView = inflater.inflate(R.layout.video_items, null);
			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
			if (ActivityName.equals("MilliyetTV") || ActivityName.equals("SkorerTV"))
			{
				holder.haberTitle.setText(realValues[position-bannerK][3]);
			}
			else
			{
				holder.haberTitle.setText(realValues[position-bannerK][4]);
			}
			
			ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
			if (ActivityName.equals("MilliyetTV") || ActivityName.equals("SkorerTV")) //video ise.
			{
				Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/videocategory/"+realValues[position-bannerK][4]);
			    jpgView.setImageBitmap(bitmap);
			}
			else
			{
				((ImageView)convertView.findViewById(R.id.playIcon)).setVisibility(View.INVISIBLE);
				Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/gallerycategory/"+realValues[position-bannerK][5]);
			    jpgView.setImageBitmap(bitmap);
			}
		    
		    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0]);
		    ID=(TextView)convertView.findViewById(R.id.ID);
		    ID.setText(realValues[position-bannerK][1]);
		    Code=(TextView)convertView.findViewById(R.id.Code);
		    Code.setText(realValues[position-bannerK][2]);
		    
		    if (position%2==0) {
	        	convertView.setBackgroundResource(R.color.listview_selector_grey);
			}else{
				convertView.setBackgroundResource(R.color.listview_selector_white);
			}	
		/*
			convertView = inflater.inflate(R.layout.news_items_without_publishtime, null);
			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
			holder.haberTitle.setText(realValues[position-bannerK][2]);
		    ID=(TextView)convertView.findViewById(R.id.ID);
		    ID.setText(realValues[position-bannerK][1]);*/
		}
		
		
		
			
//		if ((AppMap.DownloadBannerData.bannerEnabled.equals("true") || haveBanner) && position==0) 
//		{
//			haveBanner=true;
//			convertView=inflater.inflate(R.layout.banner,null);
//			holder.webView=(WebView)convertView.findViewById(R.id.webView);
//			holder.webView.getSettings().setJavaScriptEnabled(true);
//			holder.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//			holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				holder.webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				holder.webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			bannerK=1;
//		}
//		else
//		{
//		if (position-bannerK==0) 
//		{
//			convertView = inflater.inflate(R.layout.son_dakika_title, null);
//			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
//			if (ActivityName.equals("1")) 
//			{
//				holder.saatTitle.setText("Video - "+realValues[0][0]);
//			}
//			else
//			{
//				holder.saatTitle.setText("Galeri - "+realValues[0][0]);
//			}
//			
//		}
//		else 
//		{
//			convertView = inflater.inflate(R.layout.video_items, null);
//			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
//			if (ActivityName.equals("1")) 
//			{
//				holder.haberTitle.setText(realValues[position-bannerK][3]);
//			}
//			else
//			{
//				holder.haberTitle.setText(realValues[position-bannerK][4]);
//			}
//			
//			ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
//			if (ActivityName.equals("1")) //video ise.
//			{
//				Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/videocategory/"+realValues[position-bannerK][4]);
//			    jpgView.setImageBitmap(bitmap);
//			}
//			else
//			{
//				((ImageView)convertView.findViewById(R.id.playIcon)).setVisibility(View.INVISIBLE);
//				Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/gallerycategory/"+realValues[position-bannerK][5]);
//			    jpgView.setImageBitmap(bitmap);
//			}
//		    
//		    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0]);
//		    ID=(TextView)convertView.findViewById(R.id.ID);
//		    ID.setText(realValues[position-bannerK][1]);
//		    Code=(TextView)convertView.findViewById(R.id.Code);
//		    Code.setText(realValues[position-bannerK][2]);
//		/*
//			convertView = inflater.inflate(R.layout.news_items_without_publishtime, null);
//			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
//			holder.haberTitle.setText(realValues[position-bannerK][2]);
//		    ID=(TextView)convertView.findViewById(R.id.ID);
//		    ID.setText(realValues[position-bannerK][1]);*/
//		}
		  
		
		convertView.setTag(holder);
		return convertView;
	}
}
