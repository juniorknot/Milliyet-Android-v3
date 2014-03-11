package com.amvg.milliyet.main;

import java.io.File;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class DownloadImageTaskVideo extends AsyncTask<String, Void, String> 
{
    private ImageView bmImage;
    private ImageView PlayIcon;
    private String ImageName;
    private String DirName;
    private boolean IsShown; 
    private boolean IsVideo;
    
    public DownloadImageTaskVideo(ImageView bmImage, ImageView playIcon, String imageName, String dirNAme, boolean isShown, boolean isVideo) 
    {
        this.bmImage = bmImage;
        this.PlayIcon=playIcon;
        this.ImageName=imageName; 
        this.DirName=dirNAme;
        this.IsShown=isShown;
        this.IsVideo=isVideo;
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
    	if (this.IsVideo)
		{
    		PlayIcon.setVisibility(View.VISIBLE);
		}
    	this.IsShown=true;
    }
}