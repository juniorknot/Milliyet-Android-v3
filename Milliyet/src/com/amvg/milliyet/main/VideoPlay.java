package com.amvg.milliyet.main;
import com.mobilike.garantiad.GarantiAdManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlay extends FragmentActivity {
	VideoView videoView;

	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	
	private void Video_Play(final String url){
		
		String httpLiveUrl = url;   
      videoView = (VideoView) findViewById(R.id.VideoView); 
      videoView.setVideoURI(Uri.parse(httpLiveUrl));
      MediaController mediaController = new MediaController(this);
      videoView.setMediaController(mediaController);
      videoView.requestFocus();
      videoView.start();  
    }
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);
		
		
		
		String url=null;
//		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//            return;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     url=extras.getString("url"); 
		} 
		Video_Play(url);
		
    	GarantiAdManager.loadAd(this, providerUrl, new GarantiAdManager.AdListener() {
			
			@Override
			public void onLoad()
			{
			}
			
			@Override
			public void onError()
			{
			}
			
		});
		
    	Toast.makeText(getApplicationContext(), "Video yükleniyor, lütfen bekleyiniz...", Toast.LENGTH_LONG).show();
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_play, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
	   onPause();
	   finish();
	}
	@SuppressLint("NewApi")
	@Override
	 public void onConfigurationChanged(Configuration newConfig) {
	  // TODO Auto-generated method stub
	  super.onConfigurationChanged(newConfig);
	  Log.e("change","configuration");
	  videoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	 }

}