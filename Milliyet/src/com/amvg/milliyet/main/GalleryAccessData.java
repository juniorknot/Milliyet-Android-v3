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
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GalleryAccessData extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private Context context;
	private Context contextDialog;
	private String dirName;
	private Global global;
	private String[][] valuesHeadlines;
	private String[][] valuesFeatured;
	private String[][] valuesCategories;
	private int updateTime;
	private ProgressDialog Dialog;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private int dataCountHeadlines;
	private int dataCountFeatured;
	private GalleryFragmentAdapter mAdapter;
	private float DpHeight;
	private LayoutInflater LayInflater;
	private FragmentManager Fragment_Manager;
	private LinearLayout FeaturedLayout;
	private LinearLayout CategoryLayout;
	private View GalleryItems_view;
	private DataModelGalleryHeadlines dataGalleryHeadlines;
	private DataModelGalleryCategories dataGalleryCategories;
	private ArrayList<DataModelGalleryHeadlines> dataArray_GalleryHeadlines;
	private ArrayList<DataModelGalleryHeadlines> dataArray_GalleryFeatured;
	private ArrayList<DataModelGalleryCategories> dataArray_GalleryCategories;
	private DataModelVideoImageLoad GalleryImageLoad;
	private int i;
	private String DirName="/sdcard/Milliyet/gallery/";
	private static AppMap appMap;
	
	public GalleryAccessData(Context context, Context contextDialog, FragmentManager fragmentManager, ViewPager mPager, PageIndicator mIndicator, LinearLayout featuredLayout, LinearLayout categoriesLayout, LayoutInflater layInflator, float dpHeight)
	{
		appMap=new AppMap(context, contextDialog);
		this.global=new Global();
		this.context=context;
		this.dirName="/sdcard/Milliyet/gallery";
		this.updateTime=600;
		this.contextDialog=contextDialog;
		this.mPager=mPager;
		this.mIndicator=mIndicator;
		this.dataCountFeatured=0;
		this.dataCountHeadlines=0;
		this.FeaturedLayout=featuredLayout;
		this.CategoryLayout=categoriesLayout;
		this.dataArray_GalleryCategories=new ArrayList<DataModelGalleryCategories>();
		this.dataArray_GalleryFeatured=new ArrayList<DataModelGalleryHeadlines>();
		this.dataArray_GalleryHeadlines=new ArrayList<DataModelGalleryHeadlines>();
		this.Fragment_Manager=fragmentManager; 
		this.LayInflater=layInflator;
		this.DpHeight=dpHeight;
	}
	
	private String selectFileName(int i)
	{
		String fileName="abc";
		if (i==0) 
		{
			fileName="headlines";
		}
		else if (i==1) 
		{
			fileName="featured";
		}
		else if(i==2)
		{
			fileName="categories";
		}
		return fileName;
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
		for (int i = 0; i < 3; i++) 
		{
			if (!haveFile("/sdcard/Milliyet/gallery/"+selectFileName(i)+".txt")) 
			{
				return false;
			}
			else
			{
				File file = new File("/sdcard/Milliyet/gallery/"+selectFileName(i)+".txt");
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
	
	public class SearchResponseHeadlinesFeatured {
	    public List<ResultHeadlinesFeatured> root;
	}

	public class ResultHeadlinesFeatured {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	
	}
	
	public class SearchResponseCategories {
	    public List<ResultCategories> root;
	}

	public class ResultCategories {
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

	public void readData()
	{
		String aBuffer = "";
		int k=0;
		try 
		{ //okumaya yar�yor
			File myFile = new File("/sdcard/Milliyet/gallery/size.txt");
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
		aBuffer=aBuffer.trim();
		valuesHeadlines=new String[Integer.parseInt(aBuffer)][6];
		valuesFeatured=new String[15][6];
		valuesCategories=new String[11][3];
		for (int i = 0; i < 6; i++) 
		{
			aBuffer = "";
			try 
			{ //okumaya yar�yor
				File myFile = new File("/sdcard/Milliyet/gallery/"+selectFileName(i)+".txt");
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
			if (i==0) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponseHeadlinesFeatured responseHeadlines = gson.fromJson(aBuffer, SearchResponseHeadlinesFeatured.class);
				List<ResultHeadlinesFeatured> resultsHeadlines = responseHeadlines.root;
				for (ResultHeadlinesFeatured resultHeadlines : resultsHeadlines) 
				{
					dataGalleryHeadlines=new DataModelGalleryHeadlines();
					dataGalleryHeadlines.setContentType(resultHeadlines.ContentType);
					dataGalleryHeadlines.setID(resultHeadlines.ID);
					dataGalleryHeadlines.setTitle(resultHeadlines.Title);
					dataGalleryHeadlines.setImageURL(resultHeadlines.ImageURL);
					dataArray_GalleryHeadlines.add(dataGalleryHeadlines);
				}
			}
			else if (i==1) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponseHeadlinesFeatured responseFeatured = gson.fromJson(aBuffer, SearchResponseHeadlinesFeatured.class);
				List<ResultHeadlinesFeatured> resultsFeatured = responseFeatured.root;
				for (ResultHeadlinesFeatured resultFeatured : resultsFeatured) 
				{
					dataGalleryHeadlines=new DataModelGalleryHeadlines();
					dataGalleryHeadlines.setContentType(resultFeatured.ContentType);
					dataGalleryHeadlines.setID(resultFeatured.ID);
					dataGalleryHeadlines.setImageURL(resultFeatured.ImageURL);
					dataGalleryHeadlines.setTitle(resultFeatured.Title);
					dataArray_GalleryFeatured.add(dataGalleryHeadlines);
				}
			}
			else if (i==2) 
			{
				k=0;
				Gson gson = new Gson();
				SearchResponseCategories responseCategories = gson.fromJson(aBuffer, SearchResponseCategories.class);
				List<ResultCategories> resultsCategories = responseCategories.root;
				for (ResultCategories resultCategories : resultsCategories) 
				{
					if (k>10) 
					{
						
					}
					dataGalleryCategories=new DataModelGalleryCategories();
					dataGalleryCategories.setID(resultCategories.ID);
					dataGalleryCategories.setName(resultCategories.Name);
					dataGalleryCategories.setSortOrder(resultCategories.SortOrder);
					dataArray_GalleryCategories.add(dataGalleryCategories);
				}
			}
			mAdapter=new GalleryFragmentAdapter(this.Fragment_Manager, dataArray_GalleryHeadlines, "");
			GalleryFragmentAdapter.setContent(context, contextDialog);
	       	mPager.setAdapter(mAdapter);
	       	mIndicator.setViewPager(mPager);
	       	mIndicator.setCurrentItem(0);
		}
		TestFragmentAdapterVideo.setContent(valuesHeadlines,this.context,dataCountHeadlines,false,contextDialog);
	    mPager.setAdapter(mAdapter);
	    mIndicator.setViewPager(mPager);
		String[] urlAddress=null;
		FeaturedLayout.removeAllViews();
		for (i = 0; i < dataArray_GalleryFeatured.size(); i++)
		{
			GalleryItems_view=LayInflater.inflate(R.layout.gallery_featured_item,FeaturedLayout,false);
			((TextView)GalleryItems_view.findViewById(R.id.featuredText0)).setText(dataArray_GalleryFeatured.get(i).getTitle());
			GalleryImageLoad=new DataModelVideoImageLoad();
			GalleryImageLoad.setItemLayout((LinearLayout)GalleryItems_view.findViewById(R.id.featuredLayout0));
			GalleryImageLoad.setImageURL(dataArray_GalleryFeatured.get(i).getImageURL());
			GalleryImageLoad.setNewsImage((ImageView)GalleryItems_view.findViewById(R.id.featuredImage0));
			GalleryImageLoad.setPlayIcon((ImageView)GalleryItems_view.findViewById(R.id.playIcon0)); 
			Video.Array_FeaturedVideo_Load.add(GalleryImageLoad);
			((LinearLayout)GalleryItems_view.findViewById(R.id.featuredClick0)).setOnClickListener(new ClickListeneNewsItem(dataArray_GalleryFeatured.get(i).getContentType(), dataArray_GalleryFeatured.get(i).getID(), null,""));
			urlAddress=dataArray_GalleryFeatured.get(i).getImageURL().split("/");
			if (i<Math.ceil(DpHeight/152.0)) 
			{
				if (!(new File(DirName+urlAddress[urlAddress.length-1])).exists())
				{
					(new DownloadImageTaskVideo((ImageView)GalleryItems_view.findViewById(R.id.featuredImage0), (ImageView)GalleryItems_view.findViewById(R.id.playIcon0), urlAddress[urlAddress.length-1], DirName,Video.Array_FeaturedVideo_Load.get(i).getIsShown(),false)).execute(dataArray_GalleryFeatured.get(i).getImageURL());
				} 
				else
				{
					((ImageView)GalleryItems_view.findViewById(R.id.featuredImage0)).setImageURI(Uri.fromFile(new File(DirName+urlAddress[urlAddress.length-1])));
					Video.Array_FeaturedVideo_Load.get(i).setIsShown(true);
				}
			}
			FeaturedLayout.addView(GalleryItems_view); 
		}
		CategoryLayout.removeAllViews();
		for (i = 0; i < dataArray_GalleryCategories.size(); i++)
		{
			GalleryItems_view=LayInflater.inflate(R.layout.video_categories_item,CategoryLayout,false);
			((TextView)GalleryItems_view.findViewById(R.id.categoriesText0)).setText(dataArray_GalleryCategories.get(i).getName());
			if (i%2==0) 
			{
				((LinearLayout)GalleryItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_white);
			}
			else
			{
			    ((LinearLayout)GalleryItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_grey);
			} 
			((LinearLayout)GalleryItems_view.findViewById(R.id.categoriesLayout0)).setOnClickListener(new ClickListenerCategory(dataArray_GalleryCategories.get(i).getName(), dataArray_GalleryCategories.get(i).getID()));
			CategoryLayout.addView(GalleryItems_view); 
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
	protected String doInBackground(String... arg0) {
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
		for (int i = 0; i < 3; i++) 
		{
			if (i==0) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_GalleryHeadlines";
				this.fileName=selectFileName(i);
			}
			else if (i==1) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_GalleryFeatured";
				this.fileName=selectFileName(i);
			}
			else if (i==2) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_GalleryCategories";
				this.fileName=selectFileName(i);
			}
			URL url;
			try {
				File root=new File("/sdcard/Milliyet");
				root.mkdir();
				File directory=new File(this.dirName);
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
					File myFile = new File("/sdcard/Milliyet/gallery/"+this.fileName+".txt");
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
		}
		File noMediaFile = new File("/sdcard/Milliyet/gallery/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
		File sizeFile= new File("/sdcard/Milliyet/gallery/size.txt");
		try 
		{
			noMediaFile.createNewFile();
			sizeFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(sizeFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(Integer.toString(dataCountHeadlines));
			myOutWriter.close();
			fOut.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jString;
	}
	
	protected void onPostExecute(String result) 
	{ 
	   readData();
	   Video.backgroungLayout.setVisibility(View.VISIBLE);
	   Dialog.dismiss();
       Dialog = null;
    }
	
	class ClickListeneNewsItem implements OnClickListener
	{
		private String ContentType;
		private String Id;
		private String Link;
		private String TVname;
		
		public ClickListeneNewsItem(String contentType, String id, String link, String tvName) 
		{
			// TODO Auto-generated constructor stubi
			this.ContentType=contentType;
			this.Id=id;
			this.Link=link;
			this.TVname=tvName;
		}

		@Override
		public void onClick(View v)
		{
			Log.e("click","click");
			Log.e("contentType=",this.ContentType);
			Log.e("id=",this.Id);
			appMap.RunActivity(this.ContentType, this.TVname, this.Id, this.Link);
		}
	}
	class ClickListenerCategory implements OnClickListener
	{
		private String CategoryName;
		private String CategoryID;
		
		public ClickListenerCategory(String categoryName, String categoryID) 
		{
			// TODO Auto-generated constructor stubi
			this.CategoryName=categoryName;
			this.CategoryID=categoryID;
		}

		@Override
		public void onClick(View v) 
		{
			appMap.RunActivity("VideoCategory", this.CategoryName, "0", this.CategoryID);
		}
	}
}
