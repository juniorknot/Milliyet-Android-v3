package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;
import android.os.AsyncTask;

public class AdvertorialCount extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... urlAddress) 
	{
		String jString = null;
		URL url;
		try 
		{
			url = new URL(urlAddress[0]);
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
			return jString;
		} 
		catch (MalformedURLException e1) 
		{
			e1.printStackTrace();
			return "hata1";
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return "hata2";
		}
}
	protected void onPostExecute(String result) 
	{ 
		
    }
}