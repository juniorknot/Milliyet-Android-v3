package com.amvg.milliyet.main;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoLive extends Activity {

	private VideoView videoView;
	private MediaController ctlr;
	private String url;
	
//	private void test_2(){
//        String httpLiveUrl = "http://mn-i.mncdn.com/milliyet2/smil:milliyet2.smil/playlist.m3u8";   
//        videoView = (VideoView) findViewById(R.id.VideoView);
//        videoView.setVideoURI(Uri.parse(httpLiveUrl));
//        MediaController mediaController = new MediaController(this);
//        videoView.setMediaController(mediaController);
//        videoView.requestFocus();
//        videoView.start();
//    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_live);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     url=extras.getString("url");
		}
		
		
		
		
		videoView=(VideoView)findViewById(R.id.VideoView);
//		videoView.setVideoURI(Uri.parse("rtsp://mn-i.mncdn.com/r_milliyet/milliyetlive2"));
		videoView.setVideoPath(url);   //rtsp://123.29.75.131:1935/live/discovery.stream
		
		ctlr = new MediaController(this);
        ctlr.setMediaPlayer(videoView);
        videoView.setMediaController(ctlr);
        videoView.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_live, menu);
		return true;
	}

}
