package com.amvg.milliyet.main;

import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BreakingNewsCategoryAdapter extends ArrayAdapter<String> {
	private final Context context;
	private String[][] realValues;
	private TextView typeText;
	private TextView ID;
	private ViewHolder holder;
	private int bannerK;
	private boolean haveBanner;
	public int ValuesLength;
	public int year;
	

 
	public BreakingNewsCategoryAdapter(Context context, String[] values, String[][] realValues) {
		
		super(context, R.layout.news_items, values);
		this.context = context;
		this.ValuesLength=values.length;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		bannerK=0;
		haveBanner=false;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
	}
	
	private class ViewHolder {
        TextView saatTitle;
        TextView haberTitle;
	}
   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null); 
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText("Son Dakika - "+realValues[0][0]);
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
			convertView=inflater.inflate(R.layout.breaking_news_other_categories, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.saatText);
			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
			holder.saatTitle.setText(realValues[position-bannerK][2]);
			holder.haberTitle.setText(realValues[position-bannerK][3]);
			typeText=(TextView)convertView.findViewById(R.id.type);
		    typeText.setText("1"); //haber için 1 set ediyoruz
		    ID=(TextView)convertView.findViewById(R.id.ID);
		    ID.setText(realValues[position-bannerK][1]);
		    if (position%2==0) {
	        	convertView.setBackgroundResource(R.color.listview_selector_grey);
			}else{
				convertView.setBackgroundResource(R.color.listview_selector_white);
			}
		}
		convertView.setTag(holder);
		return convertView;
	}
}
