package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class WeatherLocationAccessData extends AsyncTask<String, String, String>
{
	private DataModelWeatherLocation dataWeatherLocation;
	private ArrayList<DataModelWeatherLocation> dataArray_WeatherLocation=new ArrayList<DataModelWeatherLocation>();
	private Context context;
	private Context ContextDialog;
	private String[] array;
	private ListView listView;
	private boolean[] itemToggled;
	private Global global=new Global();
	private String FileName;
	private String DirName="/sdcard/Milliyet/columnists";
	private String PathName;
	
	public WeatherLocationAccessData(Context context, ListView listView)
	{
		this.context=context;
		this.listView=listView;
		this.FileName=DirName+"/weatherlocation.txt";
		this.PathName="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherLocations";
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ID")
	    public String ID;
	
	    @SerializedName("Name")
	    public String Name;
	    
	    @SerializedName("City")
	    public String City;
	}
	
	private ArrayList<DataModelWeatherLocation> parseData(String JsonString) throws JSONException{
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		for (Result result : results) {
			dataWeatherLocation=new DataModelWeatherLocation();
			dataWeatherLocation.setID(result.ID);
			dataWeatherLocation.setName(result.Name);
			dataWeatherLocation.setCity(result.City);
			dataArray_WeatherLocation.add(dataWeatherLocation);
		}
		return dataArray_WeatherLocation;
//		return values;
	}
	private void fillListView(){
		if (Home.HasBanner) 
		{
			array= new String[dataArray_WeatherLocation.size()+3];
		}
		else
		{
			array= new String[dataArray_WeatherLocation.size()+2];
		}
//		String[] array= new String[dataSayisi];
		
		WeatherLocationAdapter adapter=new WeatherLocationAdapter(this.context, array, dataArray_WeatherLocation, this.ContextDialog);
		this.listView.setAdapter(adapter);
        itemToggled = new boolean[array.length];
        Arrays.fill(itemToggled, false);
	}
	public void fillData() throws IOException, ParseException, JSONException{
		parseData(this.global.readData(this.FileName));
		fillListView();
	}
	@Override
	protected String doInBackground(String... params)
	{
		// TODO Auto-generated method stub
		File dir = new File(this.DirName);
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
		
		String urlAdress=this.PathName;
		String jString = null;
		URL url;
		try {
			File root=new File("/sdcard/Milliyet");
			root.mkdir();
			File directory=new File(this.DirName);
			directory.mkdir();
			url = new URL(urlAdress);
			URLConnection ucon = url.openConnection();			
			InputStream is = ucon.getInputStream();			
			BufferedInputStream bis = new BufferedInputStream(is);			
			ByteArrayBuffer baf = new ByteArrayBuffer(500);			
			int current = 0;
			try {
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
					
				}	
			} catch (Exception e) {
				// TODO: handle exception
			}
					
			jString = new String(baf.toByteArray());
			
			
		return jString;
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "hata1";
		}
	}
	protected void onPostExecute(String result) { 
	       try {
	    	   this.global.writeFile(result, this.DirName, this.FileName);
				//writeFile(result);
				fillData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
}
