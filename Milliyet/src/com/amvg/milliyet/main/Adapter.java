package com.amvg.milliyet.main; //cAcAcqqq

import java.util.Calendar;

import de.madvertise.android.sdk.MadvertiseView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap; //adapter
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Adapter extends ArrayAdapter<String> 
{
	private final Context context;
	private String[][][] realValues;
	private int breakingNewsCount;
	private TextView typeText;
	private TextView newsArticle;
	private TextView ID;
	private ViewHolder holder;
	private int bannerK;
	public boolean haveBanner;
	public int ValuesLength;
	public int year;
 
	public Adapter(Context context, String[] values, String[][][] realValues, int breakingNewsCount) 
	{
		super(context, R.layout.news_items, values);
		this.context = context;
		this.realValues=realValues;
		this.breakingNewsCount=breakingNewsCount;
		this.holder=new ViewHolder();
		bannerK=0;
		haveBanner=false;
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
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText("Son Dakika");
		}
		else if (position==1 && Home.HasBanner)
		{
			convertView=inflater.inflate(R.layout.banner_item,null);
			BreakingNews.MadView=(MadvertiseView)convertView.findViewById(R.id.madad);
			BreakingNews.ConvertView=convertView;
			bannerK=1; 
			haveBanner=true;
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
			if (realValues[position-bannerK][0][1]==null && realValues[position-bannerK][0][2]==null) 
			{
				if (position-bannerK<=breakingNewsCount) 
				{
					if (realValues[position-bannerK][4][0].equals("")) 
					{
						convertView = inflater.inflate(R.layout.news_items, null);
						holder.saatTitle = (TextView)convertView.findViewById(R.id.saatText);
						holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
						holder.saatTitle.setText(realValues[position-bannerK][2][0]);
						holder.haberTitle.setText(realValues[position-bannerK][3][0]);
					    typeText=(TextView)convertView.findViewById(R.id.type);
					    typeText.setText("1"); //haber için 1 set ediyoruz
					    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0][0]);
					    ID=(TextView)convertView.findViewById(R.id.ID);
					    ID.setText(realValues[position-bannerK][1][0]);
					}
					else
					{
						convertView = inflater.inflate(R.layout.news_items_with_image, null);
						holder.saatTitle = (TextView)convertView.findViewById(R.id.saatText);
						holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
						holder.saatTitle.setText(realValues[position-bannerK][2][0]);
						holder.haberTitle.setText(realValues[position-bannerK][3][0]);
						ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
					    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/breakingnews/"+realValues[position-bannerK][4][0]);
					    jpgView.setImageBitmap(bitmap);
					    typeText=(TextView)convertView.findViewById(R.id.type);
					    typeText.setText("1"); //haber için 1 set ediyoruz
					    ID=(TextView)convertView.findViewById(R.id.ID);
					    ID.setText(realValues[position-bannerK][1][0]);
					    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0][0]);
					}
				}
				else
				{
					convertView = inflater.inflate(R.layout.breaking_news_other_categories, null);
					holder.saatTitle = (TextView)convertView.findViewById(R.id.saatText);
					holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
					holder.saatTitle.setText(realValues[position-bannerK][2][0]);
					holder.haberTitle.setText(realValues[position-bannerK][3][0]);
					typeText=(TextView)convertView.findViewById(R.id.type);
				    typeText.setText("1"); //haber için 1 set ediyoruz
				    ID=(TextView)convertView.findViewById(R.id.ID);
				    ID.setText(realValues[position-bannerK][1][0]);
				    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0][0]);
				}
		        if (position%2==0) 
		        {
		        	convertView.setBackgroundResource(R.color.listview_selector_grey);
				}else
				{
					convertView.setBackgroundResource(R.color.listview_selector_white);
				}
			}
			else
			{
				if (realValues[position-bannerK][0][1]==null) 
				{
					convertView = inflater.inflate(R.layout.show_all, null);
					holder.saatTitle = (TextView)convertView.findViewById(R.id.showAll);
					holder.saatTitle.setText("tümünü göster");
					typeText=(TextView)convertView.findViewById(R.id.type);
					typeText.setText("0"); //haber için 1 set ediyoruz
					newsArticle=(TextView)convertView.findViewById(R.id.categoryName);
					newsArticle.setText(realValues[position-bannerK][0][2]);
					((TextView)convertView.findViewById(R.id.categoryId)).setText(realValues[position-bannerK][0][3]);
				}
				else
				{
					convertView = inflater.inflate(R.layout.seperator, null);
					holder.saatTitle = (TextView)convertView.findViewById(R.id.seperator);
					holder.saatTitle.setText(realValues[position-bannerK][0][1]);
					typeText=(TextView)convertView.findViewById(R.id.type);
				    typeText.setText("0"); //haber için 1 set ediyoruz
				    newsArticle=(TextView)convertView.findViewById(R.id.categoryName);
				    newsArticle.setText(realValues[position-bannerK][0][1]);
				    ((TextView)convertView.findViewById(R.id.categoryId)).setText(realValues[position-bannerK][0][3]);
				}
			}
			convertView.setTag(holder);
		}
	return convertView;
	}
}
