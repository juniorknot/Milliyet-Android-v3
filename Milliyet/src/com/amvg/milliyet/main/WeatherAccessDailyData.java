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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;



public class WeatherAccessDailyData extends AsyncTask<String, String, String>
{
	private int updateTime=600;
	private String DirName="/sdcard/Milliyet/weather/";
	private Global global=new Global();
	private String fileName="daily.txt";
	private LayoutInflater LayInflater;
	private View view;
	private Context context;
	private String dayOfTheWeek;
	private static int LocationID;
	private ProgressDialog Dialog;
	private Context ContextDialog;
	
	public WeatherAccessDailyData(LayoutInflater layInflater, Context context, int locationID, Context contextDialog)
	{
		this.ContextDialog=contextDialog;
		// TODO Auto-generated constructor stub
		LayInflater=layInflater;
		this.context=context;
		if (locationID==0)
		{
			this.LocationID=290;
		}
		else
		{
			this.LocationID=locationID;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date(0);
		
		dayOfTheWeek = sdf.format(d);
		Log.e("GUN",dayOfTheWeek);
	}
	
	
	
	public class SearchResponseDaily {
	    public List<ResultDaily> root;
	}

	public class ResultDaily {
	    @SerializedName("LocationID")
	    public String LocationID;
	    @SerializedName("Location")
	    public String Location;
	    @SerializedName("City")
	    public String City;
	    @SerializedName("Date")
	    public String Date;
	    @SerializedName("Icon")
	    public String Icon;
	    @SerializedName("IconName")
	    public String IconName;
	    @SerializedName("High")
	    public String High;
	    @SerializedName("Low")
	    public String Low;
	    @SerializedName("WeatherText")
	    public String WeatherText;
	    @SerializedName("WindDirection")
	    public String WindDirection;
	    @SerializedName("WindSpeed")
	    public String WindSpeed;
	}
	
	
	
//	private String selectFileName(int i)
//	{
//		
//		if (i==0) 
//		{
//			return "current";
//		}
//		else if (i==1) 
//		{
//			return "daily";
//		}
//		else if(i==2)
//		{
//			return "hourly";
//		}
//		else
//		{
//			return "index";
//		}
//	}
	
	private boolean haveFile(String fileName) throws IOException
	{
		File file = new File(fileName);
		if (file.exists()) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isUpdateTimeOK(java.sql.Time lastTime, Calendar fileUpdateDate)
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int nowSeconds=0;
		int fileSeconds=0;
		if (fileUpdateDate.get(Calendar.YEAR)==today.year && fileUpdateDate.get(Calendar.MONTH)==today.month && fileUpdateDate.get(Calendar.DAY_OF_MONTH)==today.monthDay) 
		{
			nowSeconds=today.hour*3600+today.minute*60+today.second;
			fileSeconds=lastTime.getHours()*3600+lastTime.getMinutes()*60+lastTime.getSeconds();
			if (nowSeconds-fileSeconds<this.updateTime) 
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
//	public boolean areFilesOK() throws IOException, ParseException
//	{
//		for (int i = 0; i < 4; i++) 
//		{
//			if (!haveFile(this.DirName+selectFileName(i)+".txt")) 
//			{
//				return false;
//			}
//			else
//			{
//				File file = new File(this.DirName+selectFileName(i)+".txt");
//				java.sql.Time lastTime=new java.sql.Time(file.lastModified());
//				Date lastModDate = new Date(file.lastModified());
//				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		        final Calendar c = Calendar.getInstance();
//		        c.setTime(df.parse(lastModDate.toString()));
//		        if (!isUpdateTimeOK(lastTime, c)) 
//		        {
//		        	return false;
//				}
//			}
//		}
//		return true;
//	}
	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.DirName+this.fileName);
	}
	
	public void readData()
	{
		String aBuffer;
		int k=0;
	
//		for (int i = 0; i < 4; i++) 
//		{
			aBuffer = "";
			
			try 
			{ //okumaya yarıyor
				File myFile = new File(this.DirName+this.fileName);
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
			
			
				k=0;
				Gson gson = new Gson();
				SearchResponseDaily responseDaily = gson.fromJson(aBuffer, SearchResponseDaily.class);
				List<ResultDaily> resultsDaily = responseDaily.root;
				for (ResultDaily resultDaily : resultsDaily) 
				{
					view=this.LayInflater.inflate(R.layout.weather_gunluk_item, Weather.GunlukLayout,false);
					((TextView)view.findViewById(R.id.Date)).setText(resultDaily.Date.substring(0, 10));
					((ImageView)view.findViewById(R.id.WeatherIcon)).setImageResource(this.context.getResources().getIdentifier(resultDaily.IconName , "drawable", this.context.getPackageName()));
					((TextView)view.findViewById(R.id.High)).setText(resultDaily.High.substring(0, resultDaily.High.length()-1));
					((TextView)view.findViewById(R.id.WeatherText)).setText(resultDaily.WeatherText);
					((TextView)view.findViewById(R.id.Low)).setText(resultDaily.Low.substring(0, resultDaily.Low.length()-1));
					((ImageView)view.findViewById(R.id.NightIcon)).setImageResource(this.context.getResources().getIdentifier(resultDaily.IconName+"_night" , "drawable", this.context.getPackageName()));
					((TextView)view.findViewById(R.id.WindDirection)).setText(resultDaily.WindDirection);
					((TextView)view.findViewById(R.id.WindSpeed)).setText(resultDaily.WindSpeed.replace("Kph", " km/h"));
					if (k%2==1)
					{
						((LinearLayout)view.findViewById(R.id.GunlukItem)).setBackgroundColor(Color.parseColor("#efefef"));
					}
					Weather.GunlukLayout.addView(view);
					k++;
				}
			
			
			
//		}
		Weather.RefreshImage.setClickable(true);
	}
	
	@Override
    protected void onPreExecute() 
	{
        Dialog = new ProgressDialog(this.ContextDialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setMessage("Yükleniyor...");
        Dialog.setCancelable(false);
        Dialog.show();
    }

	@Override
	protected String doInBackground(String... params)
	{
		Log.e("DOWNLOAD","DOWNLOAD");
		// TODO Auto-generated method stub
		File dir = new File(this.DirName);
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
		
		String urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherForecastDaily&LocationID="+this.LocationID+"&DayCount=15";
		String jString = null;
		
//		for (int i = 0; i < 4; i++) 
//		{
//			if (i==0) 
//			{
//				Log.e("LocationID",Integer.toString(this.LocationID));
//				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherCurrent&LocationID="+this.LocationID;
//				this.fileName=selectFileName(i);
//			}
//			else if (i==1) 
//			{
//				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherForecastDaily&LocationID="+this.LocationID+"&DayCount=15";
//				this.fileName=selectFileName(i);
//			}
//			else if (i==2) 
//			{
//				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherForecastHourly&LocationID="+this.LocationID;
//				this.fileName=selectFileName(i);
//			}
//			else if (i==3) 
//			{
//				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherIndexList&LocationID="+this.LocationID;
//				this.fileName=selectFileName(i);
//			}
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
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) 
				{				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				
				try {  //yazmaya yarıyor
					
					File myFile = new File(this.DirName+this.fileName);
					
					
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(jString);
					myOutWriter.close();
					fOut.close();
					
				} catch (Exception e) {
				}
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
//		}
		return jString;
	}
	protected void onPostExecute(String result) { 
	       try {
	    	   this.global.writeFile(result, this.DirName, this.DirName+this.fileName);
	    	   
				//writeFile(result);
				readData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       Dialog.dismiss();
	       Dialog = null;
			
	    }
}
