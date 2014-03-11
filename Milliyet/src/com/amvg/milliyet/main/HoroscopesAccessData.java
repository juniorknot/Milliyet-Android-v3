package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;



public class HoroscopesAccessData extends AsyncTask<String, String, String>
{
	private int updateTime=600;
	private String DirName="/sdcard/Milliyet/horoscopes/";
	private Global global=new Global();
	private String fileName;
	private LayoutInflater LayInflater;
	private View view;
	private Context context;
	private String dayOfTheWeek;
	private AppMap appMap;
	
	public HoroscopesAccessData(LayoutInflater layInflater, Context context, Context contextDialog)
	{
		// TODO Auto-generated constructor stub
		this.fileName="/sdcard/Milliyet/horoscopes/horoscopes.txt";
		LayInflater=layInflater;
		this.context=context;
		appMap=new AppMap(context, contextDialog);
	}
	
	public class SearchResponseCurrent {
	    public List<ResultCurrent> root;
	}

	public class ResultCurrent {
	    @SerializedName("ID")
	    public String ID;
	    @SerializedName("Name")
	    public String Name;
	    @SerializedName("Element")
	    public String Element;
	    @SerializedName("DateRange")
	    public String DateRange;
	}

	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.fileName);
	}
	
	public void readData()
	{
		String aBuffer;
		int k=0;
		String [] tarih;
		aBuffer = "";
		try 
		{ //okumaya yar�yor
			File myFile = new File(this.fileName);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null) 
			{
				aBuffer += aDataRow + "\n";
			}
			myReader.close();
		} 
		catch (Exception e) 
		{
		}
		Gson gson = new Gson();
		SearchResponseCurrent responseCurrent = gson.fromJson(aBuffer, SearchResponseCurrent.class);
		List<ResultCurrent> resultsCurrent = responseCurrent.root;
		for (final ResultCurrent resultCurrent : resultsCurrent) 
		{
			if (k%2==0)
			{
				view=this.LayInflater.inflate(R.layout.horoscopes_item, Horoscopes.YatayLayout1,false);
			}
			else
			{
				view=this.LayInflater.inflate(R.layout.horoscopes_item, Horoscopes.YatayLayout2,false);
			}
			if (Integer.parseInt(resultCurrent.ID)==150)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.koc);
			}
			else if (Integer.parseInt(resultCurrent.ID)==151)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.boga);
			}
			else if (Integer.parseInt(resultCurrent.ID)==152)
			{ 
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.ikizler);
			}
			else if (Integer.parseInt(resultCurrent.ID)==153)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.yengec);
			}
			else if (Integer.parseInt(resultCurrent.ID)==154)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.aslan);
			}
			else if (Integer.parseInt(resultCurrent.ID)==155)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.basak);
			}
			else if (Integer.parseInt(resultCurrent.ID)==156)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.terazi);
			}
			else if (Integer.parseInt(resultCurrent.ID)==157)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.akrep);
			}
			else if (Integer.parseInt(resultCurrent.ID)==158)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.yay);
			}
			else if (Integer.parseInt(resultCurrent.ID)==159)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.oglak);
			}
			else if (Integer.parseInt(resultCurrent.ID)==160)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.saka);
			}
			else if (Integer.parseInt(resultCurrent.ID)==161)
			{
				((ImageView)view.findViewById(R.id.HoroscopeIcon)).setImageResource(R.drawable.balik);
			}
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					appMap.RunActivity("HoroscopesArticle", "", resultCurrent.ID, "");
				}
			});
			((TextView)view.findViewById(R.id.HoroscopeName)).setText(resultCurrent.Name);
			((TextView)view.findViewById(R.id.HoroscopeElement)).setText(resultCurrent.Element);
			tarih=resultCurrent.DateRange.split("–");
			if (tarih.length!=2)
			{
				tarih=resultCurrent.DateRange.split("-");
			}
			((TextView)view.findViewById(R.id.HoroscopeTarihBas)).setText(tarih[0].replace("(", ""));
			((TextView)view.findViewById(R.id.HoroscopeTarihSon)).setText(tarih[1].replace(")", ""));
			if (k%2==0)
			{
				Horoscopes.YatayLayout1.addView(view);
			}
			else
			{
				Horoscopes.YatayLayout2.addView(view);
			}
			k++;
		}
		Horoscopes.RefreshImage.setClickable(true);
	}

	@Override
	protected String doInBackground(String... params)
	{
		Horoscopes.RefreshImage.setClickable(false);
		// TODO Auto-generated method stub
		File dir = new File(this.DirName);
		if (dir.isDirectory()) 
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) 
	        {
	            new File(dir, children[i]).delete();
	        }
	    }
		String urlAdress="";
		String jString = null;
		urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_HoroscopesSigns";
		URL url;
		try 
		{
			File root=new File("/sdcard/Milliyet");
			root.mkdir();
			File directory=new File(this.DirName);
			directory.mkdir();
			url = new URL(urlAdress);
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
			try 
			{  //yazmaya yar�yor
				File myFile = new File(this.fileName);
				myFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append(jString);
				myOutWriter.close();
				fOut.close();
			} 
			catch (Exception e) 
			{
			}
		}
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "hata1";
		}
		return jString;
	}
	protected void onPostExecute(String result) 
	{ 
	    try 
	    {
	    	this.global.writeFile(result, this.DirName, this.fileName);
	    	Horoscopes.RefreshImage.setClickable(true);
			readData();
		} 
	    catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
