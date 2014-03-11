package com.amvg.milliyet.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class TestFragmentVideo extends Fragment 
{
    private static final String KEY_CONTENT = "TestFragment:Content";
    private static final String KEY_INDEX = "TestFragment:Index";
    public static String[][] newsParams;
    private static Context context;
    private static AppMap appMap;
    public static ImageView image;
    public static TextView tv;
    public static TextView tvInvisible;
    public static TextView tvInvisible2;
    public static ImageView imagePlayButton;
    public static boolean isVideo;

    public static TestFragmentVideo newInstance(String imageName, String newsPara[][], Context contextParam, int position, boolean isVideo, Context contextDialog) 
    {
    	newsParams=newsPara;
    	context=contextParam;
    	appMap=new AppMap(context,contextDialog);
        TestFragmentVideo fragment = new TestFragmentVideo();
        StringBuilder builder = new StringBuilder();
        builder.append(imageName);
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();
        fragment.index=position;
        TestFragmentVideo.isVideo=isVideo;
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
            index=savedInstanceState.getInt(KEY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	image=new ImageView(getActivity());
    	RelativeLayout.LayoutParams lptv = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) (29 * getResources().getDisplayMetrics().density + 0.5f));
    	lptv.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	RelativeLayout.LayoutParams lptvinvisible = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) (29 * getResources().getDisplayMetrics().density + 0.5f));
    	lptvinvisible.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	tvInvisible=new TextView(getActivity());
    	tvInvisible.setText("a");
    	tvInvisible.setTextColor(0x00000000);
    	RelativeLayout.LayoutParams lptvinvisible2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) (29 * getResources().getDisplayMetrics().density + 0.5f));
    	lptvinvisible2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	tvInvisible2=new TextView(getActivity());
    	tvInvisible2.setText("a");
    	tvInvisible2.setTextColor(0x00000000);
    	tv = new TextView(getActivity());
        tv.setBackgroundColor(0x00000000);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTypeface(null, Typeface.BOLD);
        if (isVideo) 
        {
        	tv.setText(newsParams[index][3]);
        }
        else
        {
        	tv.setText(newsParams[index][4]);
        }
        tv.setGravity(Gravity.CENTER);
        tv.setId(1);
    	if (isVideo) 
    	{
    		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/video/"+mContent+"g");
        	image.setImageBitmap(bitmap);
		}
    	else
    	{
    		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/gallery/"+mContent+"g");
        	image.setImageBitmap(bitmap);
    	}
    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) (320 * getResources().getDisplayMetrics().density + 0.5f),(int) (360 * getResources().getDisplayMetrics().density + 0.5f));
    	lp.addRule(RelativeLayout.BELOW, tv.getId());
    	lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	image.setScaleType(ImageView.ScaleType.FIT_XY);
    	image.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				Video.backgroungLayout.setVisibility(View.GONE);
				image.setClickable(false);
				appMap.RunActivity(newsParams[index][0], newsParams[index][3], newsParams[index][1], newsParams[index][4]);
			}
		});
    	RelativeLayout.LayoutParams lpPlayButtonLayout = new RelativeLayout.LayoutParams((int) (320 * getResources().getDisplayMetrics().density + 0.5f),(int) (360 * getResources().getDisplayMetrics().density + 0.5f));
    	lpPlayButtonLayout.addRule(RelativeLayout.BELOW, tv.getId());
    	lpPlayButtonLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	RelativeLayout playButtonLayout=new RelativeLayout(getActivity());
    	RelativeLayout.LayoutParams lpPlayButton = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	lpPlayButton.addRule(RelativeLayout.CENTER_IN_PARENT);
    	if (isVideo) 
    	{
    		ImageView playButton=new ImageView(getActivity());
        	playButton.setImageResource(R.drawable.video_play);
        	playButtonLayout.addView(playButton, lpPlayButton);
		}
        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.addView(tvInvisible2,lptvinvisible2);
        layout.addView(tvInvisible,lptvinvisible);
        layout.addView(tv,lptv);
        layout.addView(image,lp);
        layout.addView(playButtonLayout, lpPlayButtonLayout);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
        outState.putInt(KEY_INDEX, index);
    }
}
