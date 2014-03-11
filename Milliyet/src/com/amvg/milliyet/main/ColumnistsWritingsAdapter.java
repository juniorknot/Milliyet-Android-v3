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

public class ColumnistsWritingsAdapter extends ArrayAdapter<String> {
	private final Context context;
	private String[][] realValues;
	private TextView ID;
	private ViewHolder holder;
	private String[] saat;
	private int bannerK;
	private boolean haveBanner;
	public int ValuesLength;
	public int year;
 
	public ColumnistsWritingsAdapter(Context context, String[] values, String[][] realValues) 
	{
		super(context, R.layout.activity_columnists_writings, values);
		this.context = context;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		saat=new String[2];
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
//			convertView = inflater.inflate(R.layout.son_dakika_title, null);
//			convertView.setBackgroundResource(R.color.listview_selector_grey);
//			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
//			holder.saatTitle.setText("Yazarlar - "+realValues[1][3]);
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText(realValues[1][3]+" - Yazarlar");
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
				convertView.setBackgroundResource(R.color.listview_selector_grey);
				holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
				holder.saatTitle.setText("Yazarlar - "+realValues[1][3]);
			}
			else 
			{
				convertView = inflater.inflate(R.layout.activity_columnists_lists, null);
				saat=realValues[position-bannerK][5].split(" ");
				holder.saatTitle = (TextView)convertView.findViewById(R.id.columnistName);
				holder.haberTitle = (TextView) convertView.findViewById(R.id.articleName);
				holder.saatTitle.setText(saat[0]);
				holder.haberTitle.setText(realValues[position-bannerK][4]);
				ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
				if (realValues[position-bannerK][6].equals("")) 
				{
					jpgView.setImageResource(R.drawable.thumb);
				}
				else
				{
					Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/columnistswritings/"+realValues[position-bannerK][6]);
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
		}
		convertView.setTag(holder);
	    return convertView;
	}
}
