package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

public class NewsCategoryAccessData extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private ListView listview;
	private Context context;
	private boolean[] itemToggled;
	private int dataSayisi;
	private String dirName;
	private String categoryName;
	private Global global;
	
	public NewsCategoryAccessData(Context context, ListView lv, String categoryName, String categoryID)
	{
		this.global=new Global();
		this.context=context;
		this.listview=lv;
		this.dirName="/sdcard/Milliyet/newscategory";
		this.pathName="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsByCategory&CategoryID="+categoryID+"&TopCount=25";
		this.fileName=this.dirName+"/"+categoryName+".txt";
		this.categoryName=categoryName;
	}
	
	public class SearchResponse 
	{
	    public List<Result> root;
	}

	public class Result 
	{
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	}
	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.fileName);
	}
	
	private String[][] parseData(String JsonString) throws JSONException
	{
		int k=1;
		String[] urlAdress;
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		dataSayisi=results.size()+1;
		values=new String[dataSayisi][4];
		values[0][0]=this.categoryName;
		for (Result result : results) 
		{
			values[k][0]=result.ContentType;
			values[k][1]=result.ID;
			values[k][2]=result.Title;
			if (k>5) 
			{
				values[k][3]="";
			}
			else
			{
				urlAdress=result.ImageURL.split("/");
				values[k][3]=urlAdress[urlAdress.length-1];
		    }
			k++;
		}
		return values;
	}
	
	private void fillListView()
	{
		if (Home.HasBanner) 
		{
			dataSayisi=dataSayisi+1;
		}
		String[] array= new String[dataSayisi+1];
		NewsCategoryAdapter adapter=new NewsCategoryAdapter(this.context,array, values);
		this.listview.setAdapter(adapter);
        itemToggled = new boolean[array.length];
        Arrays.fill(itemToggled, false);
	}
	
	public void fillData() throws IOException, ParseException, JSONException
	{
		parseData(this.global.readData(this.fileName));
		fillListView();
	}
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		File dir = new File(this.dirName);
		if (dir.isDirectory()) 
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) 
	        {
	            new File(dir, children[i]).delete();
	        }
	    }
		String urlAdress=this.pathName;
		String jString = null;
		URL url;
		try 
		{
			File root=new File("/sdcard/Milliyet");
			root.mkdir();
			File directory=new File(this.dirName);
			directory.mkdir();
			url = new URL(urlAdress);
			URLConnection ucon = url.openConnection();			
			InputStream is = ucon.getInputStream();			
			BufferedInputStream bis = new BufferedInputStream(is);			
			ByteArrayBuffer baf = new ByteArrayBuffer(500);			
			int current = 0;
			try 
			{
				while ((current = bis.read()) != -1) 
				{				
					baf.append((byte) current);
				}	
			} 
			catch (Exception e) {
				// TODO: handle exception
			}
			jString = new String(baf.toByteArray());
			Gson gson = new Gson();
			SearchResponse response = gson.fromJson(jString, SearchResponse.class);
			List<Result> results = response.root;
			String[] urlAdress1=null;
			int dataCount=0;
			for (Result result : results) 
			{
				if (!result.ImageURL.equals("") && dataCount<5) 
				{
					urlAdress1=result.ImageURL.split("/");
					Global.DownloadFile(Global.resizeImageURL(result.ImageURL, "133", "0"), urlAdress1[urlAdress1.length-1],this.dirName);
				}
				dataCount++;
			}
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
	
	protected void onPostExecute(String result) 
	{ 
		try 
        {
			this.global.writeFile(result, this.dirName, this.fileName);
			fillData();
        } 
        catch (IOException e) {
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
