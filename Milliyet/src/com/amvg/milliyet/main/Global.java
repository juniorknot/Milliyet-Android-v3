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
import java.net.URLDecoder;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

public class Global extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String apiName;
	private Context context;
	private int[] index;
	private String[] outputParameters;
	private int dataSayisi;
	
	@SuppressLint("NewApi")
	private static Point getDisplaySize(final Display display) 
	{
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
	    return point;
	}
	
	public String readData(String filename)
	{
		String aBuffer = "";
		try 
		{ //okumaya yar�yor
			File myFile = new File(filename);
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
			Toast.makeText(context, e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		return aBuffer;
	}
	
	public static String resizeImageURL(String ImageURL, String Width, String Height)
	{
		Log.e("Path:","http://im.milliyet.com.tr/"+Width+"/"+Height+"/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg");
		return "http://im.milliyet.com.tr/"+Width+"/"+Height+"/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg";
	}
	public static String resizeImageURLwithColorCode(String ImageURL, String Width, String Height)
	{
		return "http://im.milliyet.com.tr/"+Width+"/"+Height+"/"+"000000"+"/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg";
	}
	
	public static int calculateTextHeight(String text, int textLength, int screenWidth)
	{
		String[] bol=text.split("\\n");
		int boslukSay=bol.length;
		int satirSay = 0;
		if (400<=screenWidth && screenWidth<500) 
		{
			satirSay=textLength/35;
		}
		else if (500<=screenWidth && screenWidth<=600) 
		{
			satirSay=textLength/50;
		}
		else if (600<=screenWidth && screenWidth<700) 
		{
			satirSay=textLength/65;
		}
		else if (700<=screenWidth && screenWidth<800) 
		{
			satirSay=textLength/80;
		}
		else if (800<=screenWidth && screenWidth<900) 
		{
			satirSay=textLength/95;
		}
		else if (900<=screenWidth && screenWidth<1000) 
		{
			satirSay=textLength/110;
		}
		else if (1000<=screenWidth && screenWidth<1100) 
		{
			satirSay=textLength/125;
		}
		else if (1100<=screenWidth && screenWidth<1200) 
		{
			satirSay=textLength/140;
		}
		else if (1200<=screenWidth && screenWidth<1300) 
		{
			satirSay=textLength/155;
		}
		if (textLength>0) 
		{
			satirSay++;
		}
		return (satirSay+boslukSay)*49;
	}
	
	public static int calculateTextHeightYellowBand(int textLength, int screenWidth)
	{
		int satirSay = 0;
		if (400<=screenWidth && screenWidth<500) 
		{
			satirSay=textLength/35;
		}
		else if (500<=screenWidth && screenWidth<=600) 
		{
			satirSay=textLength/50;
		}
		else if (600<=screenWidth && screenWidth<700) 
		{
			satirSay=textLength/65;
		}
		else if (700<=screenWidth && screenWidth<800) 
		{
			satirSay=textLength/80;
		}
		else if (800<=screenWidth && screenWidth<900) 
		{
			satirSay=textLength/95;
		}
		else if (900<=screenWidth && screenWidth<1000) 
		{
			satirSay=textLength/110;
		}
		else if (1000<=screenWidth && screenWidth<1100) 
		{
			satirSay=textLength/125;
		}
		else if (1100<=screenWidth && screenWidth<1200) 
		{
			satirSay=textLength/140;
		}
		else if (1200<=screenWidth && screenWidth<1300) 
		{
			satirSay=textLength/155;
		}
		if (textLength>0) 
		{
			satirSay++;
		}
		return 9+satirSay*20;
	}
	
	public boolean writeFile(String content, String dirname, String filename) throws IOException
	{
		try 
		{  //yazmaya yar�yor
			File root=new File("/sdcard/Milliyet");
			root.mkdir();
			File directory=new File(dirname);
			directory.mkdir(); 
			File noMediaFile = new File(dirname+"/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
			noMediaFile.createNewFile();
			File myFile = new File(filename);
			myFile.createNewFile();
			
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(content);
			myOutWriter.close();
			fOut.close();
		} 
		catch (Exception e) 
		{
		}
		return true;
	}
	
	public static boolean haveFile(String filename) throws IOException
	{
		File file = new File(filename);
		if (file.exists()) 
		{
		  return true;
		}
		else
		{
			return false;
		}
	}

	public String convertCategoryNameToCategoryID(String categoryName)
	{
		if (categoryName.equals("Siyaset")) 
		{
			return "12";
		}
		else if(categoryName.equals("Ekonomi"))
		{
			return "11";
		}
		else if(categoryName.equals("Spor"))
		{
			return "14";
		}
		else if(categoryName.equals("Dünya"))
		{
			return "19";
		}
		else if(categoryName.equals("Gündem"))
		{
			return "15";
		}
		else if(categoryName.equals("Magazin"))
		{
			return "17";
		}
		else
		{
			return "0";
		}
	}
	public static String convertCategoryIDtoCategoryName(String categoryID)
	{
		if (categoryID.equals("12")) 
		{
			return "Siyaset";
		}
		else if (categoryID.equals("11")) 
		{
			return "Ekonomi";
		}
		else if (categoryID.equals("14")) 
		{
			return "Spor";
		}
		else if (categoryID.equals("19")) 
		{
			return "Dünya";
		}
		else if (categoryID.equals("15")) 
		{
			return "Gündem";
		}
		else if (categoryID.equals("17")) 
		{
			return "Magazin";
		}
		else 
		{
			return "0";
		}
	}
	public static String convertGalleryCatIDtoCatName(String CategoryID)
	{
		if (CategoryID.equals("1")) 
		{
			return "Yaşam";
		}
		else if (CategoryID.equals("6")) 
		{
			return "Spor";
		}
		else
		{
			return "0";
		}
		
	}
	public static String convertVideoCatIDtoCatName(String CategoryID)
	{
		if (CategoryID.equals("2")) 
		{
			return "Haberler";
		}
		else if (CategoryID.equals("3")) 
		{
			return "Spor";
		}
		else if (CategoryID.equals("39")) 
		{
			return "Ekonomi";
		}
		else if (CategoryID.equals("6")) 
		{
			return "Dünya";
		}
		else if (CategoryID.equals("40")) 
		{
			return "Hava Durumu";
		}
		else if (CategoryID.equals("20")) 
		{
			return "Yaşam";
		}
		else if (CategoryID.equals("38")) 
		{
			return "Magazin";
		}
		else if (CategoryID.equals("41")) 
		{
			return "Bir Bilene Sorduk";
		}
		else
		{
			return "0";
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
			if (nowSeconds-fileSeconds<600) 
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
	
	public static void DownloadFile(String imageURL, String fileName, String dirName) throws IOException 
	{
		URL url = new URL(imageURL);
		File director=new File(dirName);
		director.mkdir();
		File file = new File(dirName+"/"+fileName);
		URLConnection ucon = url.openConnection();
		InputStream is = ucon.getInputStream();
		try 
		{
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1)
				baf.append((byte) current);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}
	
	public boolean isFileOK(String filename) throws IOException, ParseException
	{
		this.apiName=apiName;
		this.index=index;
		this.outputParameters=outputParameters;
		if (haveFile(filename)) 
		{
			File file = new File(filename);
			java.sql.Time lastTime=new java.sql.Time(file.lastModified());
			Date lastModDate = new Date(file.lastModified());
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        final Calendar c = Calendar.getInstance();
	        c.setTime(df.parse(lastModDate.toString()));
			if (isUpdateTimeOK(lastTime, c)) 
			{
				return true;
				//readData(apiName);
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
	
	public void fillData() throws IOException, ParseException, JSONException
	{
		parseData(readData(this.apiName));
		fillListView();
	}
	
	public static String setHtmlText(boolean isBanner, String body)
	{
		String htmlText="";
		htmlText=htmlText+"<!DOCTYPE HTML>";
		htmlText=htmlText+"<html>";
		htmlText=htmlText+"<head>";
		htmlText=htmlText+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
		htmlText=htmlText+"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		if (isBanner) 
		{
			htmlText=htmlText+"<link rel=\"stylesheet\" href=\"css/BannerAndroid.css\" />";
		}
		else
		{
			htmlText=htmlText+"<link rel=\"stylesheet\" href=\"css/InterstitialAndroid.css\" />";
		}
		htmlText=htmlText+"</head>";
		htmlText=htmlText+"<body>";
		htmlText=htmlText+"<div class=\"Banner\"";
		if (isBanner) {
			htmlText=htmlText+"style=\" border-bottom:2px solid #FF0000\"";
		}
		htmlText=htmlText+">";
		htmlText=htmlText+body;
		htmlText=htmlText+"</div>";
		htmlText=htmlText+"</body>";
		htmlText=htmlText+"</html>";
		return htmlText;
	}
	
	public class Result {
	    @SerializedName("CategoryID")
	    public String CategoryID;
	
	    @SerializedName("PageIndex")
	    public String PageIndex;
	}
	public class ResultCategoryID {
	    @SerializedName("CategoryID")
	    public String CategoryID;
	}
	public class ResultID {
	    @SerializedName("ID")
	    public String ID;
	}
	public class ResultColumnistID {
	    @SerializedName("ColumnistID")
	    public String ColumnistID;
	}

public static void parseURLrequest(String URL, Context context, Context contextDialog, String videoCode)
{
	AppMap appMap=new AppMap(context, contextDialog);
	if (URL.startsWith("http") || URL.startsWith("rtsp")) 
	{
		if (URL.endsWith(".mp4") || URL.startsWith("rtsp")) 
		{
			appMap.RunActivity("VideoPlay", URL, videoCode, "");
		}
		else
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			contextDialog.startActivity(browserIntent);
		}
	}
	else
	{
		URL=URLDecoder.decode(URL);
		String controller[]=URL.split("://");
		if (controller.length==1) 
		{
			appMap.RunActivity(controller[0], "", "", "14"); //eğer controller[0]="DummySkorer" olur ise AppMap'in haber kategorilerinden sporu açması için 14 sayısını categoryID olarak metoda gönderdik.
		}
		else if(controller.length==2)
		{
			if (controller[1].contains(",")) 
			{
				Gson gson = new Gson();
				Result response = gson.fromJson(controller[1].substring(3, controller[1].length()), Result.class);
				if (controller[0].equals("videocategory")) 
				{
					appMap.RunActivity("VideoCategory", convertVideoCatIDtoCatName(response.CategoryID), "1", response.CategoryID);
				}
				else
				{
					appMap.RunActivity("VideoCategory", convertGalleryCatIDtoCatName(response.CategoryID), "0", response.CategoryID);
				}
				//video galeri, photo galeri
			}
			else
			{
				if (controller[1].contains("CategoryID")) {
					Gson gson = new Gson();
					ResultCategoryID responseCatID = gson.fromJson(controller[1].substring(3, controller[1].length()), ResultCategoryID.class);
					appMap.RunActivity(controller[0], Global.convertCategoryIDtoCategoryName(responseCatID.CategoryID), "", responseCatID.CategoryID);
				}
				else if(controller[1].contains("ColumnistID"))
				{
					Gson gson = new Gson();
					ResultColumnistID responseColumnistID = gson.fromJson(controller[1].substring(3, controller[1].length()), ResultColumnistID.class);
					appMap.RunActivity(controller[0], "", responseColumnistID.ColumnistID, "");
				}
				else
				{
					Gson gson = new Gson();
					ResultID responseID = gson.fromJson(controller[1].substring(3, controller[1].length()), ResultID.class);
					if (controller[0].equals("videoclip")) 
					{
						appMap.RunActivity(controller[0], "1", responseID.ID, "");
					}
					else if (controller[0].equals("videoclipbycode")) 
					{
						appMap.RunActivity("VideoClip", "0", responseID.ID, "");
					}
					else
					{
						appMap.RunActivity(controller[0], "", responseID.ID, "");
					}
				}
			}
		}
	}
}

	private String[][] parseData(String JsonString) throws JSONException
	{
		JSONObject jObject = new JSONObject(JsonString);
		JSONArray jsonMainArr = jObject.getJSONArray("root");
		values = new String[jsonMainArr.length()][this.outputParameters.length];
		dataSayisi=jsonMainArr.length();
		for (int i = 0; i < jsonMainArr.length(); i++) 
		{
			JSONObject jsonObject = new JSONObject(jsonMainArr.getString(i));
			for (int j = 0; j <this.outputParameters.length ; j++) 
			{
				values[i][j]=jsonObject.getString(this.outputParameters[j].toString());
			}
		}
		return values;
	}
	
	private void fillListView()
	{
        String[][] vals = new String[this.index.length][dataSayisi];
        for (int j = 0; j < this.index.length; j++) 
        {
        	for (int i = 0; i < dataSayisi; i++) 
        	{
    			vals[j][i]=values[i][this.index[j]];
    		}
		}
	}
	
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		File dir = new File("/sdcard/breakingnewscategory");
		if (dir.isDirectory()) 
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) 
	        {
	            new File(dir, children[i]).delete();
	        }
	    }
		String urlAdress=this.pathName+this.apiName;
		String jString = null;
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
