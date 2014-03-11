package com.amvg.milliyet.main;

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

public class NewsCategoryAdapter extends ArrayAdapter<String> 
{
	private final Context context;
	private String[][] realValues;
	private TextView ID;
	private ViewHolder holder;
	private boolean haveBanner;
	private int bannerK;
	public int ValuesLength;
	public int year;
 
	public NewsCategoryAdapter(Context context, String[] values, String[][] realValues) 
	{
		super(context, R.layout.news_items, values);
		this.context = context;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		haveBanner=false;
		bannerK=0;
		this.ValuesLength=values.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
	}
	
	private class ViewHolder 
	{
        TextView saatTitle;
        TextView haberTitle;	
	}
   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText(realValues[0][0]);
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
		else
		{
			if (position-bannerK==0) 
			{
				convertView = inflater.inflate(R.layout.son_dakika_title, null);
				holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
				holder.saatTitle.setText(realValues[0][0]);
			}
			else if (!realValues[position-bannerK][3].equals("")) 
			{
				convertView = inflater.inflate(R.layout.news_items_with_image_without_publishtime, null);
				holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
				holder.haberTitle.setText(realValues[position-bannerK][2]);
				ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
			    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/newscategory/"+realValues[position-bannerK][3]);
			    jpgView.setImageBitmap(bitmap);
			    ID=(TextView)convertView.findViewById(R.id.ID);
			    ID.setText(realValues[position-bannerK][1]);
			}
			else
			{
				convertView = inflater.inflate(R.layout.news_items_without_publishtime, null);
				holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
				holder.haberTitle.setText(realValues[position-bannerK][2]);
			    ID=(TextView)convertView.findViewById(R.id.ID);
			    ID.setText(realValues[position-bannerK][1]);
			}
			if (position%2==0) 
			{
		        convertView.setBackgroundResource(R.color.listview_selector_grey);
			}
			else
			{
				convertView.setBackgroundResource(R.color.listview_selector_white);
			}	
		}
		convertView.setTag(holder);
		return convertView;
	}
}
