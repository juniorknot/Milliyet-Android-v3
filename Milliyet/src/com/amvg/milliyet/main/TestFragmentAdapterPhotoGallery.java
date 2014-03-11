package com.amvg.milliyet.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapterPhotoGallery extends FragmentPagerAdapter implements IconPagerAdapter 
{
    public static String[] CONTENT;
    public static String[][] news;
    private static Context context;
    private static Context contextDialog;
    protected static final int[] ICONS = new int[] {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };

    private static int mCount;

    public static void setContent(String newsParam[][], Context contextParam, int dataCount, Context contextD)
    {
    	TestFragmentPhotoGallery.k=true;
    	mCount=dataCount;
    	CONTENT=new String[dataCount];
    	for (int i = 0; i < dataCount; i++) 
    	{
			CONTENT[i]="a";
		}
    	news = new String[dataCount][4];
    	news=newsParam;
    	context=contextParam;
    	contextDialog=contextD;
    }
    public TestFragmentAdapterPhotoGallery(FragmentManager fm) 
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) 
    {
        return TestFragmentPhotoGallery.newInstance(news[position][3],news,context,position,contextDialog);
    }

    @Override
    public int getCount() 
    {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) 
    {
      return TestFragmentAdapterPhotoGallery.CONTENT[position % CONTENT.length];
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
            mCount = count;
            notifyDataSetChanged();
        }
    }
}