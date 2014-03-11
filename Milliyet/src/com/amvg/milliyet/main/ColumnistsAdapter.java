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

public class ColumnistsAdapter extends ArrayAdapter<String> 
{
	private final Context context;
	private String[][] realValues;
	private TextView typeText;
	private TextView ID;
	private ViewHolder holder;
	private int kont;
	private int bannerK;
	private boolean haveBanner;
	public int ValuesLength;
	public int year;
 
	public ColumnistsAdapter(Context context, String[] values, String[][] realValues, int kont) 
	{
		super(context, R.layout.activity_columnists_lists, values);
		this.context = context;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		this.kont=kont;
		bannerK=0;
		haveBanner=false;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
		this.ValuesLength=values.length;
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
//			convertView = inflater.inflate(R.layout.son_dakika_title, null);
//			convertView.setBackgroundResource(R.color.listview_selector_grey);
//			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
//			holder.saatTitle.setText(realValues[0][0]);
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText("Yazarlar");
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
		else if(realValues[position-bannerK][0]!="diğer yazarlar")
		{
			convertView = inflater.inflate(R.layout.activity_columnists_lists, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.columnistName);
			holder.haberTitle = (TextView) convertView.findViewById(R.id.articleName);
			holder.saatTitle.setText(realValues[position-bannerK][3]);
			holder.haberTitle.setText(realValues[position-bannerK][4]);
			typeText=(TextView)convertView.findViewById(R.id.type);
		    typeText.setText("1"); //haber i�in 1 set ediyoruz
			ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
			if (this.kont==1) 
			{
				if (realValues[position-bannerK][6].equals("")) 
				{
					jpgView.setImageResource(R.drawable.thumb);
				}
				else
				{
					Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/columnistsother/"+realValues[position-bannerK][6]);
				    jpgView.setImageBitmap(bitmap);
				}
			}
			else
			{
			    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/columnists/"+realValues[position-bannerK][6]);
			    jpgView.setImageBitmap(bitmap);
			}
		    ID=(TextView)convertView.findViewById(R.id.ID);
		    ID.setText(realValues[position-bannerK][1]);
		    if (position%2==0) 
		    {
	        	convertView.setBackgroundResource(R.color.listview_selector_grey);
			}
		    else
		    {
				convertView.setBackgroundResource(R.color.listview_selector_white);
			}
		}
		else
		{
			convertView = inflater.inflate(R.layout.show_all, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.showAll);
			holder.saatTitle.setText("diğer yazarlar");
			typeText=(TextView)convertView.findViewById(R.id.type);
		    typeText.setText("0");
		}
		convertView.setTag(holder);
	    return convertView;
	}
}
