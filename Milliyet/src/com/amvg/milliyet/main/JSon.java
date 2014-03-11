package com.amvg.milliyet.main;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class JSon extends AsyncTask<String, String, String>
{
	
	 private Context context;
	 private String versionName;
	 public String bundleID;
	 
	 public void MyPreloader(Context context, String versionName)
	 {
	     this.context = context;
	     this.versionName=versionName;
	 }
	 
	 public class SearchResponse 
	 {
		    public List<Result> root;
	 }
	
	 public class Result 
	 {
		 @SerializedName("AppID")
		 public String AppID;
		
		 @SerializedName("BundleID")
		 public String BundleID;
		
		 @SerializedName("Version")
		 public String Version;
		    
		 @SerializedName("AdServerAPI")
		 public String AdServerAPI;
	 }
	
	public static void setInfo(String versionName, String bundleId, String appId, String adServerAPI)
	{
		SplashScreen.version=versionName;
		SplashScreen.bundleId=bundleId;
		SplashScreen.appId=appId;
		SplashScreen.AdServerAPI=adServerAPI;
	}
	 
	protected String doInBackground(String... parseParam) 
	{
		String jString = null;
		URL url;
		try 
		{
			url = new URL(parseParam[0]);
			URLConnection ucon = url.openConnection();	
			InputStream is = ucon.getInputStream();		
			BufferedInputStream bis = new BufferedInputStream(is);	
			ByteArrayBuffer baf = new ByteArrayBuffer(50);		
			int current = 0;
			while ((current = bis.read()) != -1) 
			{				
				baf.append((byte) current);
			}			
			jString = new String(baf.toByteArray());	
			Gson gson = new Gson();
			jString="{\"root\":["+jString+"]}";
			char[] ek=jString.toCharArray();
			jString="";
			for (int i = 0; i < ek.length; i++) 
			{
				if (i!=9) 
				{
					jString=jString+ek[i];
				}
			}
			SearchResponse response = gson.fromJson(jString, SearchResponse.class);
			List<Result> results = response.root;
			String[] values = new String[4];
			for (Result result : results) 
			{
				values[0]=result.AppID;
				values[1]=result.BundleID;
				values[2]=result.Version;
				values[3]=result.AdServerAPI;
			}
			setInfo(values[2], values[1], values[0], values[3]);
			this.bundleID=values[1];
			return values[2];
		} 
		catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			e.printStackTrace();
			e.toString();
			Log.e("hata2",e.toString());
			return "hata2";
		}
	}
	
	protected void onPostExecute(String result) 
	{ 
		String Version[] =versionName.split("\\.");
        String newVersion[] =result.split("\\.");
        if(Integer.parseInt(Version[0])<Integer.parseInt(newVersion[0]) || (Version[0].equals(newVersion[0]) && Integer.parseInt(Version[1])<Integer.parseInt(newVersion[1])))
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(context);
      		builder.setMessage(R.string.update_message).setTitle(R.string.update_title);
      		builder.setPositiveButton(R.string.update_simdi, new DialogInterface.OnClickListener()
      		{
	      	    public void onClick(DialogInterface dialog, int id) 
	      	    {
	      	    	context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+bundleID)));
	      	    }
      		});
	      	builder.setNegativeButton(R.string.update_dahasonra, new DialogInterface.OnClickListener() 
	      	{
	      		public void onClick(DialogInterface dialog, int id) 
	      		{
	      	              
	      	    }
	      	});
	      	AlertDialog dialogAlert = builder.create();
	      	dialogAlert.show();
		}
    }
}
