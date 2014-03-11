package com.amvg.milliyet.main;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

class MilliyetTvFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter 
{
    private ArrayList<DataModelVideoHeadlines> results;
    private String Controller;
    private static Context Context;
    private static Context ContextDialog;

    public static void setContent(Context context, Context contextDialog)
    {
    	MilliyetTvFragment.maxIndex=-1;
    	Context=context;
    	ContextDialog=contextDialog;
    }
    public MilliyetTvFragmentAdapter(FragmentManager fm, ArrayList<DataModelVideoHeadlines> results, String controller) 
    {
        super(fm);
        this.Controller=controller;
        setResults(results);
    }

    @Override
    public Fragment getItem(int position) 
    {
        return MilliyetTvFragment.newInstance(getResults(),position,Context,ContextDialog,Controller);
    }

    @Override
    public int getCount() 
    {
        return getResults().size();
    }

    public void setCount(int count) 
    {
        if (count > 0 && count <= 10) 
        {
            notifyDataSetChanged();
        }
    }
	@Override
	public int getIconResId(int index) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	public ArrayList<DataModelVideoHeadlines> getResults() 
	{
		return results;
	}
	public void setResults(ArrayList<DataModelVideoHeadlines> results) 
	{
		this.results = results;
	}
}