package com.amvg.milliyet.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapterVideo extends FragmentPagerAdapter implements IconPagerAdapter 
{
    public static String[] CONTENT;
    public static String[][] videos;
    private static Context context;
    private static Context contextDialog;
    private static boolean isVideo;
    protected static final int[] ICONS = new int[] {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };


    public static void setContent(String newsParam[][], Context contextParam, int videoCount, boolean isVideo, Context contextD)
    {
    	CONTENT=new String[videoCount];
    	for (int i = 0; i < videoCount; i++) 
    	{
			CONTENT[i]="a";
		}
    	videos = new String[videoCount][6];
    	videos=newsParam;
    	context=contextParam;
    	contextDialog=contextD;
    	TestFragmentAdapterVideo.isVideo=isVideo;
    }
    public TestFragmentAdapterVideo(FragmentManager fm) 
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) 
    {
    	String imageName=null;
    	if (isVideo) 
    	{
			imageName=videos[position][4];
		}
    	else
    	{
    		imageName=videos[position][5];
    	}
        return TestFragmentVideo.newInstance(imageName,videos,context,position,isVideo,contextDialog);
    }

    @Override
    public int getCount() 
    {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) 
    {
      return TestFragmentAdapterVideo.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) 
    {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) 
    {
        if (count > 0 && count <= 10) 
        {
            notifyDataSetChanged();
        }
    }
}