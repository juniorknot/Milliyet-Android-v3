package com.amvg.milliyet.main;

import java.io.File;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, String> 
{
    private ImageView bmImage;
    private String ImageName;
    private String DirName;
    private boolean IsShown; 
    
    public DownloadImageTask(ImageView bmImage, String imageName, String dirNAme, boolean isShown) 
    {
        this.bmImage = bmImage;
        this.ImageName=imageName; 
        this.DirName=dirNAme;
        this.IsShown=isShown;
    }

    protected String doInBackground(String... urls) 
    {
        String urldisplay = urls[0];
        try 
        {
        	Global.DownloadFile(urldisplay, this.ImageName,this.DirName);
        }  
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		return "a";
    }

    protected void onPostExecute(String string) 
    {
    	bmImage.setImageURI(Uri.fromFile(new  File(this.DirName+this.ImageName)));
    	this.IsShown=true;
    }
}