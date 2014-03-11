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
import android.util.Log;
import android.widget.ListView;

public class NewsCategoriesAccessData extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private ListView listview;
	private Context context;
	private boolean[] itemToggled;
	private int dataSayisi;
	private String dirName;
	private Global global;
	
	public NewsCategoriesAccessData(Context context, ListView lv)
	{
		this.global=new Global();
		this.context=context;
		this.listview=lv;
		this.dirName="/sdcard/Milliyet/othernewscategories";
		this.pathName="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsCategories";
		this.fileName=this.dirName+"/"+"kategoriler.txt";
	}
	
	public class SearchResponse 
	{
	    public List<Result> root;
	}

	public class Result 
	{
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Name")
	    public String Name;
	    
	    @SerializedName("SortOrder")
	    public String SortOrder;
	}
	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.fileName);
	}
	
	private String[][] parseData(String JsonString) throws JSONException
	{
		int k=0;
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		dataSayisi=results.size();
		values=new String[dataSayisi][3];
		for (Result result : results) 
		{
			values[k][0]=result.ID;
			values[k][1]=result.Name;
			values[k][2]=result.SortOrder;
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
		String[] array= new String[dataSayisi+2];
		NewsCategoriesAdapter adapter=new NewsCategoriesAdapter(this.context, values, array);
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
			catch (Exception e) 
			{
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
			Log.e("Hata::",e.toString());
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
