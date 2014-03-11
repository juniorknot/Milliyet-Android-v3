package com.amvg.milliyet.main;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends BaseAdapter 
{
	private LayoutInflater mInflater;
	private static ArrayList<MainMenuDataModel> searchArrayList;
	private Context Context;
	//CONSTRUCTOR
	public MainMenuAdapter(Context context, ArrayList<MainMenuDataModel> results) 
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		this.Context=context;
	}

	@Override
	public int getCount() 
	{
		return searchArrayList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return searchArrayList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		convertView = mInflater.inflate(R.layout.activity_deneme_list, null);
		holder = new ViewHolder();
		holder.imageView=(ImageView)convertView.findViewById(R.id.logo);
		holder.tv_Controller = (TextView) convertView.findViewById(R.id.controller);
		holder.tv_Title = (TextView) convertView.findViewById(R.id.empty);
		holder.titleImage=(ImageView)convertView.findViewById(R.id.titleImage);
		holder.tv_CID = (TextView) convertView.findViewById(R.id.CID);
		convertView.setTag(holder);
		holder.tv_Controller.setText(searchArrayList.get(position).getController());
		if (searchArrayList.get(position).getController().equals("Skorer"))
		{
			holder.titleImage.setImageResource(R.drawable.skorer);
		}
		else if (searchArrayList.get(position).getController().equals("MilliyetTV"))
		{
			holder.titleImage.setImageResource(R.drawable.milliyettv);
		} 
		else if (searchArrayList.get(position).getController().equals("SkorerTV"))
		{
			holder.titleImage.setImageResource(R.drawable.skorertv);
		}
		else
		{
		 	holder.tv_Title.setText(searchArrayList.get(position).getTitle());
		}
		holder.tv_CID.setText(searchArrayList.get(position).getCID());
	    convertView.setBackgroundResource(R.color.listview_selector_slide_menu);
	    
	    Log.e("IconName:",searchArrayList.get(position).getIcon().toLowerCase().toString());
	    
	    int resID = this.Context.getResources().getIdentifier(searchArrayList.get(position).getIcon().toLowerCase().toString()+"_h" , "drawable", this.Context.getPackageName());
	    holder.imageView.setImageResource(resID);
	    
//		if (holder.tv_Title.getText().equals("Ana Sayfa")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.home_h);
//		}
//		else if (holder.tv_Title.getText().equals("Son Dakika")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.breaking_news_h);
//		}
//		else if (holder.tv_Title.getText().equals("Yazarlar")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.columnists_h);
//		}
//		else if (holder.tv_Title.getText().equals("Siyaset")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.politics_h);
//		}
//		else if (holder.tv_Title.getText().equals("Ekonomi"))
//		{
//	        holder.imageView.setImageResource(R.drawable.economics_h);
//		}
//		else if (holder.tv_Title.getText().equals("Dünya")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.world_h);
//		}
//		else if (holder.tv_Title.getText().equals("Gündem")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.journal_h);
//		}
//		else if (holder.tv_Title.getText().equals("Spor")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.sports_h);
//		}
//		else if (holder.tv_Title.getText().equals("Magazin")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.magazine_h);
//		}
//		else if (holder.tv_Title.getText().equals("Cadde")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.magazine_h);
//		}
//		else if (holder.tv_Title.getText().equals("Tüm Kategoriler")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.news_categories_h);
//		}
//		else if (holder.tv_Title.getText().equals("Galeri")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.gallery_h);
//		}
//		else if (holder.tv_Title.getText().equals("Video")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.video_h);
//		}
//		else if (holder.tv_Title.getText().equals("Hava Durumu")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.weather_h);
//		}
//		else if (holder.tv_Title.getText().equals("Astroloji")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.horoscopes_h);
//		}
//		else if (holder.tv_Controller.getText().equals("SkorerTV"))
//		{
//	        holder.imageView.setImageResource(R.drawable.tv_h);
//		}
//		else if (holder.tv_Controller.getText().equals("MilliyetTV"))
//		{
//	        holder.imageView.setImageResource(R.drawable.tv_h);
//		}
//		else if (holder.tv_Controller.getText().equals("Skorer")) 
//		{
//	        holder.imageView.setImageResource(R.drawable.sports_h);
//		}
//		else 
//			holder.imageView.setImageResource(R.drawable.ic_launcher);
		return convertView;
	}

	static class ViewHolder 
	{
		TextView tv_Controller;
		TextView tv_Title;
		TextView tv_CID;
		ImageView imageView;
		ImageView titleImage;
	}
}
