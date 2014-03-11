package com.amvg.milliyet.main;

import java.util.Calendar;

import de.madvertise.android.sdk.MadvertiseView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NewsCategoriesAdapter extends ArrayAdapter<String> 
{
	private final Context context;
	private final String[][] values;
	private int bannerK;
	private boolean haveBanner;
	public int ValuesLength;
	public int year;
	public ViewHolder holder;
 
	public NewsCategoriesAdapter(Context context, String[][] values, String[] vals) 
	{
		super(context, R.layout.news_categories_items, vals);
		this.context = context;
		this.values = values;
		bannerK=0;
		haveBanner=false;
		this.holder=new ViewHolder();
		this.ValuesLength=vals.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
	}
	
	private class ViewHolder 
	{
        ImageView imageView;
        TextView txtTitle;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		ViewHolder holder = new ViewHolder();
//		if ((Home.HasBanner || haveBanner) && position==0)
//		{
//			haveBanner=true;
//			convertView=inflater.inflate(R.layout.banner_item, null);
//			bannerK=1;
//		}
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.txtTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.txtTitle.setText("Tüm Kategoriler");
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
			if (position-bannerK<6) 
			{
				convertView = inflater.inflate(R.layout.news_categories_items, null);
				holder.txtTitle = (TextView) convertView.findViewById(R.id.empty);
		        holder.txtTitle.setText(values[position-bannerK-1][1]);
		        ((TextView) convertView.findViewById(R.id.CID)).setText(values[position-bannerK-1][0]);
		        holder.imageView = (ImageView) convertView.findViewById(R.id.logo);
		        if (values[position-bannerK-1][1].equals("Siyaset")) 
		        {
		        	holder.imageView.setImageResource(R.drawable.politics_selector);
				}
		        else if (values[position-bannerK-1][1].equals("Ekonomi")) 
		        {
		        	holder.imageView.setImageResource(R.drawable.economics_selector);
				}
		        else if (values[position-bannerK-1][1].equals("Dünya")) 
		        {
		        	holder.imageView.setImageResource(R.drawable.world_selector);
				}
		        else if (values[position-bannerK-1][1].equals("Gündem")) 
		        {
		        	holder.imageView.setImageResource(R.drawable.journal_selector);
				}
		        else if (values[position-bannerK-1][1].equals("Spor - Skorer"))
		        {
		        	holder.imageView.setImageResource(R.drawable.sports_selector);
				}
		        else if (values[position-bannerK-1][1].equals("Magazin")) 
		        {
		        	holder.imageView.setImageResource(R.drawable.magazine_selector);
		        }
		        convertView.setTag(holder);
			}
			else
			{
				convertView = inflater.inflate(R.layout.activity_deneme_list_without_icon, null);
				holder.txtTitle = (TextView) convertView.findViewById(R.id.empty);
		        holder.txtTitle.setText(values[position-bannerK-1][1]);
		        ((TextView) convertView.findViewById(R.id.CID)).setText(values[position-bannerK-1][0]);
		        convertView.setTag(holder);
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
        return convertView;
	}
}
