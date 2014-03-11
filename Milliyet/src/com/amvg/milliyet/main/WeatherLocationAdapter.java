package com.amvg.milliyet.main;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class WeatherLocationAdapter extends ArrayAdapter<String>
{
	private Context context;
	private ViewHolder holder=new ViewHolder();
	private boolean haveBanner;
	private int bannerK;
	private ArrayList<DataModelWeatherLocation> DataList;
	public int ValuesLength;
	public int year;
	
	
	public WeatherLocationAdapter(Context context, String[] values, ArrayList<DataModelWeatherLocation> dataList, Context contextDialog)
	{
		super(context, R.layout.news_items, values);
		this.DataList=dataList;
		this.context=context;
		haveBanner=false;
		bannerK=0;
		this.ValuesLength=values.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
		// TODO Auto-generated constructor stub
	}

//	public WeatherLocationAdapter(Context context, String[] values, ArrayList<DataModelWeatherLocation> dataList, Context contextDialog) 
//	{
//	
//	}
	
	private class ViewHolder {
        TextView saatTitle;
        TextView haberTitle;	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText("Hava Durumu");
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
//		else
//		{
//			if (position-bannerK==0) 
//			{
//				convertView = inflater.inflate(R.layout.son_dakika_title, null);
//				holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
//				holder.saatTitle.setText("Hava Durumu");
//			}
			else
			{
				convertView = inflater.inflate(R.layout.activity_deneme_list_without_icon, null);
				holder.haberTitle = (TextView) convertView.findViewById(R.id.empty);
				holder.haberTitle.setText(DataList.get(position-bannerK-1).getName());
			    ((TextView)convertView.findViewById(R.id.CID)).setText(DataList.get(position-bannerK-1).getID());
			    
			    if (position%2==0) {
		        	convertView.setBackgroundResource(R.color.listview_selector_grey);
				}else{
					convertView.setBackgroundResource(R.color.listview_selector_white);
				}	
			}
			
			  
			
		
		   
		    
//		}
		convertView.setTag(holder);
		return convertView;
	}
}
