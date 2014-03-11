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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.viewpagerindicator.PageIndicator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class SkorerAccessData extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private Context context;
	private Context contextDialog;
	private String dirName;
	private final String advertorialContentName="advertoriallink";
	private LinearLayout TopHeadlinesC_layout;
	private View HomeItems_view;
	private LinearLayout HomePage_layout;
	private LayoutInflater LayInflater;
	private String DirName="/sdcard/Milliyet/skorer/";
	private Global global;
	private String[] valuesWeather;
	private String[] valuesYellowBand;
	private String[][] valuesFinance;
	private int updateTime;
	private ProgressDialog Dialog;
	private SkorerFragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private String[] parseData;
	private DataModelHomeHeadlines dataHomeHeadlines;
	private ArrayList<DataModelHomeHeadlines> dataArray_HomeHeadlines;
	private ArrayList<DataModelHomeHeadlines> dataArray_TopHeadlinesC;
	private ArrayList<DataModelHomeHeadlines> dataArray_HomePage;
	private DataModelNewsImageLoad NewsImageLoad;
	private android.support.v4.app.FragmentManager Fragment_Manager;
	private float DpHeight;
	private static AppMap appMap;
	private int i;
	
	public SkorerAccessData(Context context, ListView lv, Context contextDialog, android.support.v4.app.FragmentManager fragmentManager, TestFragmentAdapter mAdapter, ViewPager mPager, PageIndicator mIndicator, LinearLayout topHeadlinesC_layout, LinearLayout homePage_layout, LayoutInflater layInflater, float dpHeight){
		this.global=new Global();
		dataArray_HomeHeadlines = new ArrayList<DataModelHomeHeadlines>();
		dataArray_TopHeadlinesC = new ArrayList<DataModelHomeHeadlines>();
		dataArray_HomePage=new ArrayList<DataModelHomeHeadlines>();
		this.context=context;
		this.dirName="/sdcard/Milliyet/skorer";
		this.updateTime=600;
		this.contextDialog=contextDialog;
		this.Fragment_Manager=fragmentManager;
		this.mPager=mPager;
		this.mIndicator=mIndicator;
		this.parseData=new String[2];
		this.TopHeadlinesC_layout=topHeadlinesC_layout;
		this.HomePage_layout=homePage_layout;
		this.LayInflater=layInflater;
		this.DpHeight=dpHeight;
		this.appMap=new AppMap(this.context, this.contextDialog);
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
			if (!haveFile("/sdcard/Milliyet/skorer/"+selectFileName(i)+".txt") || !haveFile("/sdcard/Milliyet/skorer/size.txt")) 
			{
				return false;
			}
			else
			{
				File file = new File("/sdcard/Milliyet/skorer/"+selectFileName(i)+".txt");
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
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	    
	    @SerializedName("ThumbURL")
	    public String ThumbURL;
	    
	    @SerializedName("Link")
	    public String Link;
	}
	
	public class SearchResponseWeather {
	    public List<ResultWeather> root;
	}

	public class ResultWeather {
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
	    
	    @SerializedName("Temperature")
	    public String Temperature;
	    
	    @SerializedName("FeelsLike")
	    public String FeelsLike;
	    
	    @SerializedName("High")
	    public String High;
	    
	    @SerializedName("Low")
	    public String Low;
	    
	    @SerializedName("WeatherText")
	    public String WeatherText;
	    
	    @SerializedName("Humidity")
	    public String Humidity;
	    
	    @SerializedName("WindDirection")
	    public String WindDirection;
	    
	    @SerializedName("WindSpeed")
	    public String WindSpeed;
	    
	    @SerializedName("Visibility")
	    public String Visibility;
	    
	    @SerializedName("Precipitation")
	    public String Precipitation;
	    
	    @SerializedName("Pressure")
	    public String Pressure;
	    
	    @SerializedName("DewPoint")
	    public String DewPoint;
	}
	
	public class SearchResponseFinance {
	    public List<ResultFinance> root;
	}

	public class ResultFinance {
	    @SerializedName("Symbol")
	    public String Symbol;
	
	    @SerializedName("Value")
	    public String Value;
	    
	    @SerializedName("Percent")
	    public String Percent;
	}
	
	public class SearchResponseYellowBand {
	    public List<ResultYellowBand> root;
	}

	public class ResultYellowBand {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Title")
	    public String Title;
	}
	
	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.fileName);
	}

	
	
	
	public void readData() throws MalformedURLException, IOException
	{
		String aBuffer = "";
		String empty="{\"root\" : null}\n";
		try 
		{ //okumaya yar�yor
			File myFile = new File("/sdcard/Milliyet/skorer/size.txt");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null) 
			{
				aBuffer += aDataRow + "\n";
			}
			myReader.close();
		} 
		catch (Exception e) {
		}
		int k=0;
		valuesWeather = new String[18]; 
		aBuffer=aBuffer.trim();
		valuesFinance=new String[4][3];
		valuesYellowBand=new String[3];
		for (int i = 0; i < 7; i++) 
		{
			aBuffer = "";
			try 
			{ //okumaya yar�yor
				File myFile = new File("/sdcard/Milliyet/skorer/"+selectFileName(i)+".txt");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) 
				{
					aBuffer += aDataRow + "\n";
				}
				myReader.close();
			} 
			catch (Exception e) {
			}
			if (i==0)
			{
				Gson gson = new Gson();
				SearchResponseWeather responseWeather = gson.fromJson(aBuffer, SearchResponseWeather.class);
				List<ResultWeather> resultsWeather = responseWeather.root;
				for (ResultWeather resultWeather : resultsWeather) 
				{
					valuesWeather[0]=resultWeather.LocationID;
					valuesWeather[1]=resultWeather.Location;
					valuesWeather[2]=resultWeather.City;
					valuesWeather[3]=resultWeather.Date;
					valuesWeather[4]=resultWeather.Icon;
					valuesWeather[5]=resultWeather.IconName;
					valuesWeather[6]=resultWeather.Temperature;
					valuesWeather[7]=resultWeather.FeelsLike;
					valuesWeather[8]=resultWeather.High;
					valuesWeather[9]=resultWeather.Low;
					valuesWeather[10]=resultWeather.WeatherText;
					valuesWeather[11]=resultWeather.Humidity;
					valuesWeather[12]=resultWeather.WindDirection;
					valuesWeather[13]=resultWeather.WindSpeed;
					valuesWeather[14]=resultWeather.Visibility;
					valuesWeather[15]=resultWeather.Precipitation;
					valuesWeather[16]=resultWeather.Pressure;
					valuesWeather[17]=resultWeather.DewPoint;
				}
			}
			else if (i==1 && !aBuffer.equals(empty)) 
			{
				Gson gson = new Gson();
				SearchResponseYellowBand responseYellowBand = gson.fromJson(aBuffer, SearchResponseYellowBand.class);
				List<ResultYellowBand> resultsYellowBand = responseYellowBand.root;
				for (ResultYellowBand resultYellowBand : resultsYellowBand) 
				{
					valuesYellowBand[0]=resultYellowBand.ContentType;
					valuesYellowBand[1]=resultYellowBand.ID;
					valuesYellowBand[2]=resultYellowBand.Title;
				}
			}
			else if (i==2) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(aBuffer, SearchResponse.class);
				List<Result> results = response.root;
				for (Result result : results) 
				{
					dataHomeHeadlines=new DataModelHomeHeadlines();
					if (result.ContentType.toLowerCase().equals(advertorialContentName)) 
					{
						dataHomeHeadlines.setID(result.Link);
					}
					else
					{
						dataHomeHeadlines.setID(result.ID);
					}
					dataHomeHeadlines.setContentType(result.ContentType);
					dataHomeHeadlines.setTitle(result.Title);
					dataHomeHeadlines.setImageURL(result.ImageURL);
					dataHomeHeadlines.setThumbURL(result.ThumbURL);
					dataHomeHeadlines.setLink(result.Link);
					dataArray_HomeHeadlines.add(dataHomeHeadlines);
					k++;
				}
				mAdapter=new SkorerFragmentAdapter(this.Fragment_Manager, dataArray_HomeHeadlines);
				SkorerFragmentAdapter.setContent(context, contextDialog);
		       	mPager.setAdapter(mAdapter);
		       	mIndicator.setViewPager(mPager);
		       	mIndicator.setCurrentItem(0);
			}
			else if (i==3) 
			{
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(aBuffer, SearchResponse.class);
				List<Result> results = response.root;
				for (Result result : results) 
				{
					dataHomeHeadlines=new DataModelHomeHeadlines();
					if (result.ContentType.toLowerCase().equals(advertorialContentName)) 
					{
						dataHomeHeadlines.setID(result.Link);
					}
					else
					{
						dataHomeHeadlines.setID(result.ID);
					}
					dataHomeHeadlines.setContentType(result.ContentType);
					dataHomeHeadlines.setTitle(result.Title);
					dataHomeHeadlines.setImageURL(result.ImageURL);
					dataHomeHeadlines.setThumbURL(result.ThumbURL);
					dataHomeHeadlines.setLink(result.Link);
					dataArray_TopHeadlinesC.add(dataHomeHeadlines);
				}
			}
			else if (i==4) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponseFinance responseFinance = gson.fromJson(aBuffer, SearchResponseFinance.class);
				List<ResultFinance> resultsFinance = responseFinance.root;
				for (ResultFinance resultFinance : resultsFinance) 
				{
					valuesFinance[k][0]=resultFinance.Symbol;
					valuesFinance[k][1]=resultFinance.Value;
					valuesFinance[k][2]=resultFinance.Percent;
					k++;
				}
			}
			else if (i==5) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(aBuffer, SearchResponse.class);
				List<Result> results = response.root;
				for (Result result : results) 
				{
					dataHomeHeadlines=new DataModelHomeHeadlines();
					if (result.ContentType.toLowerCase().equals(advertorialContentName)) 
					{
						dataHomeHeadlines.setID(result.Link);
					}
					else
					{
						dataHomeHeadlines.setID(result.ID);
					}
					dataHomeHeadlines.setContentType(result.ContentType);
					dataHomeHeadlines.setTitle(result.Title);
					dataHomeHeadlines.setImageURL(result.ImageURL);
					dataHomeHeadlines.setThumbURL(result.ThumbURL);
					dataHomeHeadlines.setLink(result.Link);
					dataArray_HomePage.add(dataHomeHeadlines);
				}
			}
		}
	    mIndicator.setCurrentItem(0); 
	    parseData=valuesWeather[3].split(" ");
	    Skorer.weatherDate.setText(parseData[0]);
	    Skorer.weatherInfo.setText(valuesWeather[2].substring(0, 1).toUpperCase()+valuesWeather[2].substring(1, valuesWeather[2].length()).toLowerCase()+" "+valuesWeather[6].substring(0, valuesWeather[6].length()-1)+" / "+valuesWeather[7].substring(0, valuesWeather[7].length()-1)+" C\u00B0  ");
	    int resID = this.context.getResources().getIdentifier(valuesWeather[5] , "drawable", this.context.getPackageName());
	    Skorer.weatherImage.setImageResource(resID);
	    Home.financeImkbPuan.setText(valuesFinance[0][1]);
	    Home.financeImkbYuzde.setText(valuesFinance[0][2]+" ");
	    if (valuesFinance[0][2].charAt(0)=='-') 
	    { 
	    	Home.financeImkbYuzde.setTextColor(Color.parseColor("#CC0000"));
	    	Home.financeImkbIcon.setImageResource(R.drawable.finance_down);
	    }
	    else if (valuesFinance[0][2].equals("0,00")) 
	    {
	    	Home.financeImkbYuzde.setTextColor(Color.parseColor("#00ffff"));
			Home.financeImkbIcon.setImageResource(R.drawable.finance_zero);
		}
	    else
	    {
			Home.financeImkbYuzde.setTextColor(Color.parseColor("#00cc33"));
			Home.financeImkbIcon.setImageResource(R.drawable.finance_up);
		}
	    Home.financeEuroPuan.setText(valuesFinance[1][1]);
	    Home.financeEuroYuzde.setText(valuesFinance[1][2]+" ");
	    if (valuesFinance[1][2].charAt(0)=='-') 
	    { 
	    	Home.financeEuroYuzde.setTextColor(Color.parseColor("#CC0000"));
	    	Home.financeEuroIcon.setImageResource(R.drawable.finance_down);
		}
	    else if (valuesFinance[1][2].equals("0,00"))
	    {
			Home.financeEuroYuzde.setTextColor(Color.parseColor("#00ffff"));
			Home.financeEuroIcon.setImageResource(R.drawable.finance_zero);
	    }
	    else
	    {
			Home.financeEuroYuzde.setTextColor(Color.parseColor("#00cc33"));
			Home.financeEuroIcon.setImageResource(R.drawable.finance_up);
		}
	    Home.financeUsdPuan.setText(valuesFinance[2][1]);
	    Home.financeUsdYuzde.setText(valuesFinance[2][2]+" ");
	    if (valuesFinance[2][2].charAt(0)=='-') 
	    { 
	    	Home.financeUsdYuzde.setTextColor(Color.parseColor("#CC0000"));
	    	Home.financeUsdIcon.setImageResource(R.drawable.finance_down);
		}
	    else if (valuesFinance[2][2].equals("0,00")) 
	    {
	    	Home.financeUsdYuzde.setTextColor(Color.parseColor("#00ffff"));
			Home.financeUsdIcon.setImageResource(R.drawable.finance_zero);
		}
	    else
	    {
			Home.financeUsdYuzde.setTextColor(Color.parseColor("#00cc33"));
			Home.financeUsdIcon.setImageResource(R.drawable.finance_up);
		}
	    Home.financeAltinPuan.setText(valuesFinance[3][1]);
	    Home.financeAltinYuzde.setText(valuesFinance[3][2]+" ");
	    if (valuesFinance[3][2].charAt(0)=='-') 
	    { 
	    	Home.financeAltinYuzde.setTextColor(Color.parseColor("#CC0000"));
	    	Home.financeAltinIcon.setImageResource(R.drawable.finance_down);
		}
	    else if (valuesFinance[3][2].equals("0,00")) 
	    {
	    	Home.financeAltinYuzde.setTextColor(Color.parseColor("#00ffff"));
			Home.financeAltinIcon.setImageResource(R.drawable.finance_zero);
		}
	    else
	    {
			Home.financeAltinYuzde.setTextColor(Color.parseColor("#00cc33"));
			Home.financeAltinIcon.setImageResource(R.drawable.finance_up);
		}
	    Skorer.dpHeight_hp=DpHeight-120-89*dataArray_TopHeadlinesC.size();
	    Skorer.dikeyVideoSay=(int) Math.ceil(DpHeight/89.0);
	    String[] urlAddress=null;
	    TopHeadlinesC_layout.removeAllViews();
		for (i = 0; i < dataArray_TopHeadlinesC.size(); i++)
		{
			HomeItems_view=LayInflater.inflate(R.layout.skorer_item,TopHeadlinesC_layout,false);
			((TextView)HomeItems_view.findViewById(R.id.haberText0)).setText(dataArray_TopHeadlinesC.get(i).getTitle());
			if (i%2==0) 
			{
				((RelativeLayout)HomeItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_white);
			}
			else
			{
				((RelativeLayout)HomeItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_grey);
			} 
			NewsImageLoad=new DataModelNewsImageLoad(); 
			NewsImageLoad.setItemLayout((LinearLayout)HomeItems_view.findViewById(R.id.itemLayout0));
			NewsImageLoad.setImageURL(dataArray_TopHeadlinesC.get(i).getImageURL());
			NewsImageLoad.setNewsImage((ImageView)HomeItems_view.findViewById(R.id.newsImage0)); 
			Skorer.Array_TopHeadlinesC_Load.add(NewsImageLoad);
			((LinearLayout)HomeItems_view.findViewById(R.id.itemLayout0)).setOnClickListener(new ClickListeneNewsItem(dataArray_TopHeadlinesC.get(i).getContentType(), dataArray_TopHeadlinesC.get(i).getID(), dataArray_TopHeadlinesC.get(i).getLink()));
			urlAddress=dataArray_TopHeadlinesC.get(i).getImageURL().split("/"); 
			if (i<Math.ceil(DpHeight/89.0)) 
			{
				if (!(new File(DirName+urlAddress[urlAddress.length-1])).exists())
				{
					(new DownloadImageTask((ImageView)HomeItems_view.findViewById(R.id.newsImage0), urlAddress[urlAddress.length-1], DirName,Skorer.Array_TopHeadlinesC_Load.get(i).getIsShown())).execute(dataArray_TopHeadlinesC.get(i).getImageURL());
				}
				else
				{
					((ImageView)HomeItems_view.findViewById(R.id.newsImage0)).setImageURI(Uri.fromFile(new  File(DirName+urlAddress[urlAddress.length-1])));
					Skorer.Array_TopHeadlinesC_Load.get(i).setIsShown(true);
				}
			}
			TopHeadlinesC_layout.addView(HomeItems_view); 
		}
		HomePage_layout.removeAllViews();
		for (int i = 0; i < dataArray_HomePage.size(); i++) 
		{
			HomeItems_view=LayInflater.inflate(R.layout.skorer_homepage_item,HomePage_layout,false);
			((TextView)HomeItems_view.findViewById(R.id.haberText0)).setText(dataArray_HomePage.get(i).getTitle());
			if (i%2==0) 
			{
			    ((RelativeLayout)HomeItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_white);
			}
			else
			{
				((RelativeLayout)HomeItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_grey);
			} 
			NewsImageLoad=new DataModelNewsImageLoad();
			NewsImageLoad.setItemLayout((LinearLayout)HomeItems_view.findViewById(R.id.itemLayout0));
			NewsImageLoad.setImageURL(dataArray_HomePage.get(i).getImageURL());
			NewsImageLoad.setNewsImage((ImageView)HomeItems_view.findViewById(R.id.newsImage0));
			Skorer.Array_HomePage_Load.add(NewsImageLoad);
			((LinearLayout)HomeItems_view.findViewById(R.id.itemLayout0)).setOnClickListener(new ClickListeneNewsItem(dataArray_HomePage.get(i).getContentType(), dataArray_HomePage.get(i).getID(), dataArray_HomePage.get(i).getLink()));
			HomePage_layout.addView(HomeItems_view); 
		}
	}
	
	@Override
    protected void onPreExecute() 
	{
       Dialog = new ProgressDialog(this.contextDialog);
       Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       Dialog.setMessage("Yükleniyor...");
       Dialog.setCancelable(false);
       Dialog.show();
    }
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		int mainHeadlinesSize=0;
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
		for (int i = 0; i < 6; i++) 
		{
			if (i==0) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherCurrent&LocationID=290";
				this.fileName=selectFileName(i);
			}
			else if (i==1) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_YellowBand";
				this.fileName=selectFileName(i);
			}
			else if (i==2) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_SkorerHeadlines";
				this.fileName=selectFileName(i);
			}
			else if (i==3)
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_SkorerTopHeadlines";
				this.fileName=selectFileName(i); 
			}
			else if (i==4)
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_FinanceWidgetV2";
				this.fileName=selectFileName(i);
			}
			else if (i==5) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_SkorerHomePage";
				this.fileName=selectFileName(i);
			}
			URL url;
			try 
			{
				File root=new File("/sdcard/Milliyet");
				root.mkdir();
				File directory=new File("/sdcard/Milliyet/skorer");
				directory.mkdir();
				url = new URL(urlAdress);
				URLConnection ucon = url.openConnection();
				ucon.setDoOutput(false);
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
					File myFile = new File("/sdcard/Milliyet/skorer/"+this.fileName+".txt");
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
				Log.e("Hata",e.toString());
				return "hata1";
			}
		}
		File noMediaFile = new File("/sdcard/Milliyet/skorer/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
		File sizeFile= new File("/sdcard/Milliyet/skorer/size.txt");
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
	   try 
	   { 
		readData();
	   } 
	   catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   Dialog.dismiss();
       Dialog = null;
    }
	
	class ClickListeneNewsItem implements OnClickListener
	{
		private String ContentType;
		private String Id;
		private String Link;
		
		public ClickListeneNewsItem(String contentType, String id, String link) 
		{
			// TODO Auto-generated constructor stub
			this.ContentType=contentType;
			this.Id=id;
			this.Link=link;
		}

		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			Skorer.setClickable(false);
			appMap.RunActivity(this.ContentType, "", this.Id, this.Link);
		}
	}
}
