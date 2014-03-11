package com.amvg.milliyet.main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class TestFragmentPhotoGallery extends Fragment 
{
    private static final String KEY_CONTENT = "TestFragment:Content";
    private static final String KEY_INDEX = "TestFragment:Index";
    public static String[][] newsParams =new String[3][4];
    public static ImageView image;
//    public static TextView tv;
    public static WebView webView; 
    public TextView CopyRight;
    public static boolean k=true;
    private BitmapFactory.Options options=new BitmapFactory.Options();
    private View view;
	public int year;

    public static TestFragmentPhotoGallery newInstance(String imageName, String newsPara[][], Context contextParam, int position, Context contextDialog) 
    {
    	newsParams=newsPara;
        TestFragmentPhotoGallery fragment = new TestFragmentPhotoGallery();
        StringBuilder builder = new StringBuilder();
        builder.append(imageName);
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();
        fragment.index=position;
        return fragment;
    }
    private String mContent = "???";
    private int index=0;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT) && savedInstanceState.containsKey(KEY_INDEX)) 
        {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
        Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	int kont=0;
    	try 
    	{
			do 
			{
				kont++;
				if (kont>1000) 
				{
				} 
			} 
			while (!Global.haveFile("/sdcard/Milliyet/photogallery/"+mContent+"g"));
		} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	PhotoGalleryAccessData.textLength[index]=newsParams[index][2].length();
    	PhotoGalleryAccessData.text[index]=newsParams[index][2];
//    	tv = new TextView(getActivity());
    	webView=new WebView(getActivity());
    	image=new ImageView(getActivity());
    	CopyRight=new TextView(getActivity());
        options.inSampleSize = 8;
        File imgFile = new  File("/sdcard/Milliyet/photogallery/"+mContent+"g");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile("/sdcard/Milliyet/photogallery/"+mContent+"g", options);
    	PhotoGalleryAccessData.bitmapWidth[index]=options.outWidth;;//this.bitmap.getWidth();
    	PhotoGalleryAccessData.bitmapHeight[index]=options.outHeight;;//this.bitmap.getHeight();
    	if (k) 
    	{
    		PhotoGallery.linearLayout.getLayoutParams().height=(int) ((60+Global.calculateTextHeight(newsParams[index][2], PhotoGalleryAccessData.textLength[index], PhotoGallery.width))*getResources().getDisplayMetrics().density+(int) Math.round((int) (320 * getResources().getDisplayMetrics().density + 0.5f)*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]));//(int) (600 * scale + 0.5f);
    		k=false;
		}
    	LinearLayout layout = new LinearLayout(getActivity());
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	params.gravity=Gravity.CENTER;
    	layout.setLayoutParams(params);
//        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        
        layout.setBackgroundColor(0xFFFFFFFF);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.photo_gallery,null);
//        if (newsParams[index][2].equals(""))
//		{
//			((View)view.findViewById(R.id.view)).setVisibility(View.GONE);
//		}
//        Log.e("LENGTH::",Integer.toString(newsParams.length));
        ((TextView)view.findViewById(R.id.ResimIndis)).setText(Integer.toString(index+1));
        ((TextView)view.findViewById(R.id.ResimTop)).setText(Integer.toString(newsParams.length-1));
        image=(ImageView)view.findViewById(R.id.imageView);
        image.setImageURI(Uri.fromFile(imgFile));
//        image.getLayoutParams().width=PhotoGallery.width;//(int) (420 * scale + 0.5f);
        image.getLayoutParams().width=(int) (320 * getResources().getDisplayMetrics().density + 0.5f);
    	try 
    	{
    		Log.e("HATASIZ","BOYUT");
    		image.getLayoutParams().height=(int) Math.round(image.getLayoutParams().width*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]);//(int) (320 * scale + 0.5f);
//    		image.getLayoutParams().height=(int) (320 * getResources().getDisplayMetrics().density + 0.5f);
    	} 
    	catch (Exception e) {
			// TODO: handle exception
    		Log.e("HATAYA","GŞİRDİ");
			image.getLayoutParams().height=850;
		}
    	image.setScaleType(ImageView.ScaleType.FIT_XY);
//        tv=(TextView)view.findViewById(R.id.text);
//        tv.setText(newsParams[index][2]);
    	webView=(WebView)view.findViewById(R.id.webView);
    	//webView.loadData(newsParams[index][2], "text/html", "UTF-8");
    	webView.loadDataWithBaseURL("http://www.google.com", createHTML(newsParams[index][2]), "text/html", "UTF-8", "");
        CopyRight=(TextView)view.findViewById(R.id.Copyrigth);
        CopyRight.setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");
        layout.addView(view);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
        outState.putInt(KEY_INDEX, index);
    }
    
    private String createHTML(String Description)
	{ 
		String customHtml;
		customHtml="";
		customHtml+="<!DOCTYPE HTML>";
		customHtml+="<html>";
		customHtml+="<head>";
		customHtml+="<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">";
		customHtml+="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml+="<link rel=\"stylesheet\" href=\"file:///android_asset/css/PhotoGalleryAndroid.css\" />";
		customHtml+="</head>";
		customHtml+="<body data-role=\"page\">";
		customHtml+="<div class=\"Description\">"+Description+"</div>";
		customHtml+="</body>";
		customHtml+="</html>";
		return customHtml;
	}
}
