package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

public class MainMenuAccessData extends AsyncTask<String, String, String> 
{
	private Context context;
	private MainMenuDataModel model;
	private ListView listView_menu;
	public static ArrayList<MainMenuDataModel> array_list;
	
	MainMenuAccessData(Context context, ListView listView_menu)
	{
		this.listView_menu=listView_menu;
		this.context=context;
	}
	
	@Override
	protected String doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		String jString = null;
		URL url;
		try 
		{
			array_list=new ArrayList<MainMenuDataModel>();
			url = new URL("http://m2.milliyet.com.tr/AppConfigs/Milliyet-Android-v3.0/MainMenu.json");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) 
			{				
				baf.append((byte) current);
			}
			jString = new String(baf.toByteArray());
			params[0]=jString;		
//			jString= "{\"root\":["+
//					 "{\"Controller\":\"Home\",\"Icon\":\"Home\",\"Title\":\"Ana Sayfa\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"BreakingNews\",\"Icon\":\"BreakingNews\",\"Title\":\"Son Dakika\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"Columnists\",\"Icon\":\"Columnists\",\"Title\":\"Yazarlar\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"NewsCategory\",\"Icon\":\"Politics\",\"Title\":\"Siyaset\",\"Image\":null,\"CID\":12},"+
//					 "{\"Controller\":\"NewsCategory\",\"Icon\":\"Economics\",\"Title\":\"Ekonomi\",\"Image\":null,\"CID\":11},"+
//					 "{\"Controller\":\"Skorer\",\"Icon\":\"Sports\",\"Title\":\"Skorer - Spor\",\"Image\":\"Skorer\",\"CID\":null},"+
//					 "{\"Controller\":\"NewsCategory\",\"Icon\":\"World\",\"Title\":\"Dünya\",\"Image\":null,\"CID\":19},"+ 
//					 "{\"Controller\":\"NewsCategory\",\"Icon\":\"Journal\",\"Title\":\"Gündem\",\"Image\":null,\"CID\":15},"+
//					 "{\"Controller\":\"NewsCategory\",\"Icon\":\"Magazine\",\"Title\":\"Magazin\",\"Image\":null,\"CID\":17},"+
//					 "{\"Controller\":\"NewsCategories\",\"Icon\":\"NewsCategories\",\"Title\":\"Tüm Kategoriler\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"Gallery\",\"Icon\":\"Gallery\",\"Title\":\"Galeri\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"MilliyetTV\",\"Icon\":\"TV\",\"Title\":\"Milliyet TV - Video\",\"Image\":\"MilliyetTV\",\"CID\":null},"+
//					 "{\"Controller\":\"SkorerTV\",\"Icon\":\"TV\",\"Title\":\"Skorer TV - Video - Spor\",\"Image\":\"SkorerTV\",\"CID\":null},"+
//					 "{\"Controller\":\"Weather\",\"Icon\":\"Weather\",\"Title\":\"Hava Durumu\",\"Image\":null,\"CID\":null},"+
//					 "{\"Controller\":\"Horoscopes\",\"Icon\":\"Horoscopes\",\"Title\":\"Astroloji\",\"Image\":null,\"CID\":null}"+
//					 "]}";
			Gson gson = new Gson();
			SearchResponse response = gson.fromJson(jString, SearchResponse.class);
			List<Result> results = response.root;
			for (Result result : results) 
			{
				model = new MainMenuDataModel();//burada model yapısına gelen datayı set yapıyoruz
				model.setController(result.Controller);
				model.setRef(result.Ref);
				model.setTitle(result.Title);
				model.setCID(result.CID);
				model.setIcon(result.Icon);
				array_list.add(model);
			}
			model = null;
			return "a";
		} 
		catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			e.printStackTrace();
			return "hata2";
		}
	}
	protected void onPostExecute(String result)
	{
		this.listView_menu.setAdapter(new MainMenuAdapter(context, array_list));
	}
	
	public class SearchResponse 
	{
	    public List<Result> root;
	}

	public class Result 
	{
	    @SerializedName("Controller")
	    public String Controller;
	
	    @SerializedName("Ref")
	    public String Ref;
	
	    @SerializedName("Title")
	    public String Title;
	
	    @SerializedName("CID")
	    public String CID;
	    
	    @SerializedName("Icon")
	    public String Icon;
	}
}
