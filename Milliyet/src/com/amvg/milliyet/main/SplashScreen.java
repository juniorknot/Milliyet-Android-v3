package com.amvg.milliyet.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.http.util.ByteArrayBuffer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.text.format.Time;
import android.view.Display;
import android.view.Menu;

public class SplashScreen extends Activity 
{
	private String pathName;
	private String dirName;
	private String fileName;
	private Context context;
	private int updateTime;
	public static String version;
	public static String bundleId;
	public static String appId;
	public static String AdServerAPI;
	public static float scale;
	public static int width;
	public static int height;
	public static String Resolution;
	private String versionName; 
	private DownloadData downloadData;
	public AppMap appMap;
	public static String android_id;
	public static Activity splash;
	
	public boolean checkInternetConnection() 
	{
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	public AlertDialog alertDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.con_message).setTitle(R.string.con_title);
		builder.setPositiveButton(R.string.yeniden_dene, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				if (!checkInternetConnection()) 
				{
					alertDialog().show();
				}
				else
				{
					updateApp();
					
					try
					{
						if (!areFilesOK()) 
						{
							downloadData.execute("");
						}
						else
						{
							while (SplashScreen.bundleId==null) 
							{
								
							}
							appMap.RunActivity("Home", "", "", "Splash");
						}
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
	    });
		builder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				finish();
	        }
	    });
		AlertDialog dialog = builder.create();
		return dialog;
	}
	public void updateApp()
	{
		final JSon json=new JSon();
		versionName = null;
		String parseParam[]={"http://m2.milliyet.com.tr/AppConfigs/Milliyet-Android-v2/AppConfig.json","AppID","BundleID","Version"};
		try 
		{
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0); 
			versionName = pinfo.versionName;
		} 
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		json.MyPreloader(this,versionName);
		json.execute(parseParam);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		this.updateTime=600;
		this.fileName="";
		this.context=getApplicationContext();
		String[] in={"in"};
		this.dirName="/sdcard/Milliyet/home"; 
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!prefs.getBoolean("firstTime", false)) 
		{
			File dir = new File(dirName);
			if (dir.isDirectory()) 
			{
		        String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) 
		        {
		            new File(dir, children[i]).delete();
		        }
		    }
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("firstTime", true);
			editor.commit();
		}
		android_id = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID); 
		downloadData=new DownloadData();
		appMap =new AppMap(getApplicationContext(),SplashScreen.this);
		splash=this;
		if (!checkInternetConnection()) 
		{
			alertDialog().show();
		}
		if (checkInternetConnection()) 
		{
			updateApp();
			try 
			{
				Display display = getWindowManager().getDefaultDisplay();
				scale = this.context.getResources().getDisplayMetrics().density;
				final Point point = new Point();
				try 
				{
					display.getSize(point);
				} 
				catch (java.lang.NoSuchMethodError ignore) 
				{ // Older device
				    point.x = display.getWidth();
				    point.y = display.getHeight();
				}
				width = point.x;
				height = point.y;
				Resolution=Integer.toString((int) (width/scale))+"x"+Integer.toString((int) (height/scale));
				if (!areFilesOK()) 
				{
					downloadData.execute(in);
				}
				else
				{
					while (SplashScreen.bundleId==null) 
					{
						
					}
					appMap.RunActivity("Home", "", "", "Splash");
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
	private String selectFileName(int i)
	{
		if (i==0) 
		{
			return "weather";
		}
		else if (i==1) 
		{
			return "yellowband";
		}
		else if (i==2) 
		{
			return "topheadlinesa";
		}
		else if (i==3)
		{
			return "topheadlinesc";
		}
		else if (i==4)
		{
			return "financewidget";
		}
		else if (i==5) 
		{
			return "headlines";
		}
		else if (i==6) 
		{
			return "headlinesb";
		}
			return "a";
	}
	
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
	
	public boolean areFilesOK() throws IOException, ParseException
	{
		for (int i = 0; i < 6; i++) 
		{
			if (!haveFile("/sdcard/Milliyet/home/"+selectFileName(i)+".txt") || !haveFile("/sdcard/Milliyet/home/size.txt")) 
			{
				return false;
			}
			else
			{
				File file = new File("/sdcard/Milliyet/home/"+selectFileName(i)+".txt");
				java.sql.Time lastTime=new java.sql.Time(file.lastModified());
				Date lastModDate = new Date(file.lastModified());
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        final Calendar c = Calendar.getInstance();
		        c.setTime(df.parse(lastModDate.toString()));
		        if (!isUpdateTimeOK(lastTime, c)) 
		        {
					return false;
				}
			}
		}
		return true;
	}
	
	public class DownloadData extends AsyncTask<String, String, String>
	{
		private String selectFileName(int i)
		{
			if (i==0) 
			{
				return "weather";
			}
			else if (i==1) 
			{
				return "yellowband";
			}
			else if (i==2) 
			{
				return "topheadlinesa";
			}
			else if (i==3)
			{
				return "topheadlinesc";
			}
			else if (i==4)
			{
				return "financewidget";
			}
			else if (i==5) 
			{
				return "headlines";
			}
			else if (i==6) 
			{
				return "headlinesb";
			}
				return "a";
		}
		
		@Override
		protected String doInBackground(String... arg0) 
		{
			int mainHeadlinesSize=0;
			File dir = new File(dirName);
			if (dir.isDirectory()) 
			{
		        String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) 
		        {
		            new File(dir, children[i]).delete();
		        }
		    }
			String urlAdress=pathName;
			String jString = null;
			for (int i = 0; i < 6; i++) 
			{
				if (i==0) 
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherCurrent&LocationID=290";
					fileName=selectFileName(i);
				}
				else if (i==1) 
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_YellowBand";
					fileName=selectFileName(i);
				}
				else if (i==2) 
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_HomeHeadlines";
					fileName=selectFileName(i);
				}
				else if (i==3)
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_TopHeadlinesC";
					fileName=selectFileName(i);
				}
				else if (i==4)
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_FinanceWidgetV2";
					fileName=selectFileName(i);
				}
				else if (i==5) 
				{
					urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_HomePage";
					fileName=selectFileName(i);
				}
				URL url;
				try 
				{
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
					{  //yazmaya yarıyor
						File root=new File("/sdcard/Milliyet");
						root.mkdir();
						File directory=new File("/sdcard/Milliyet/home");
						directory.mkdir();
						File myFile = new File("/sdcard/Milliyet/home/"+fileName+".txt");
						myFile.createNewFile();
						FileOutputStream fOut = new FileOutputStream(myFile);
						OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
						myOutWriter.append(jString);
						myOutWriter.close();
						fOut.close();
					} 
					catch (Exception e) {
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
			}
			File noMediaFile = new File("/sdcard/Milliyet/home/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
			File sizeFile= new File("/sdcard/Milliyet/home/size.txt");
			try 
			{
				noMediaFile.createNewFile();
				sizeFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(sizeFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append(Integer.toString(mainHeadlinesSize));
				myOutWriter.close();
				fOut.close();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jString;
		}
	
		protected void onPostExecute(String result) 
		{ 
	       appMap.RunActivity("Home", "", "", "Splash");
	    }
	}
}
