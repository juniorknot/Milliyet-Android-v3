package com.amvg.milliyet.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewExt extends ScrollView
{
	private OnScrollViewListener scrollViewListener = null;
	
	public ScrollViewExt(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ScrollViewExt(Context context, AttributeSet attrs) 
	{ 
        super(context, attrs); 
    } 

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) 
    { 
        super(context, attrs, defStyle); 
    } 
    
    public void setScrollViewListener(OnScrollViewListener scrollViewListener) 
    {
        this.scrollViewListener = scrollViewListener;
    }
    
    protected void onScrollChanged(int l, int t, int oldl, int oldt) 
    { 
    	scrollViewListener.onScrollChanged( this, l, t, oldl, oldt ); 
//    	View view = (View) getChildAt(getChildCount()-1);
//        int diff = (view.getBottom()-(getHeight()+getScrollY()));// Calculate the scrolldiff
//        boolean flag =true;
//        if( diff == 0 )  // if diff is zero, then the bottom has been reached
        {
//        	if (ScrollViewExt.ClassName.equals("Home"))
//			{
//        		Log.e("sonson", "MyScrollView: Bottom has been reached" ); 
//                for (int i = 0; i < Home.Array_HomePage_Load.size(); i++) 
//                {
//    				if (!Home.Array_HomePage_Load.get(i).getIsShown()) 
//    				{
//    					String[] urlAddress; 
//    					urlAddress=Home.Array_HomePage_Load.get(i).getImageURL().split("/");
//    					if (!(new File("/sdcard/Milliyet/home/"+urlAddress[urlAddress.length-1])).exists())
//    					{
////    						Log.e("yok",Integer.toString(videoRowShowCount_hp));
//    						new DownloadImageTask(Home.Array_HomePage_Load.get(i).getNewsImage(),urlAddress[urlAddress.length-1],"/sdcard/Milliyet/home/",Home.Array_HomePage_Load.get(i).getIsShown()).execute(Home.Array_HomePage_Load.get(i).getImageURL());
//    					}
//    					else
//    					{
////    						Log.e("var","var");
//    						Home.Array_HomePage_Load.get(i).getNewsImage().setImageURI(Uri.fromFile(new  File("/sdcard/Milliyet/home/"+urlAddress[urlAddress.length-1])));
//    						Home.Array_HomePage_Load.get(i).setIsShown(true);
//    					}
//    				}
//    			}
//			}
//        	else
//        	{
//        		Log.e("sonson", "MyScrollView: Bottom has been reached" ); 
//                for (int i = 0; i < Skorer.Array_HomePage_Load.size(); i++) 
//                {
//    				if (!Skorer.Array_HomePage_Load.get(i).getIsShown()) 
//    				{
//    					if (!Skorer.Array_HomePage_Load.get(i).getImageURL().equals(""))
//						{
//    						String[] urlAddress; 
//        					urlAddress=Skorer.Array_HomePage_Load.get(i).getImageURL().split("/");
//        					if (!(new File("/sdcard/Milliyet/skorer/"+urlAddress[urlAddress.length-1])).exists())
//        					{
////        						Log.e("yok",Integer.toString(videoRowShowCount_hp));
//        						new DownloadImageTask(Skorer.Array_HomePage_Load.get(i).getNewsImage(),urlAddress[urlAddress.length-1],"/sdcard/Milliyet/skorer/",Skorer.Array_HomePage_Load.get(i).getIsShown()).execute(Skorer.Array_HomePage_Load.get(i).getImageURL());
//        					}
//        					else
//        					{
////        						Log.e("var","var");
//        						Skorer.Array_HomePage_Load.get(i).getNewsImage().setImageURI(Uri.fromFile(new  File("/sdcard/Milliyet/skorer/"+urlAddress[urlAddress.length-1])));
//        						Skorer.Array_HomePage_Load.get(i).setIsShown(true);
//        					}
//						}
//    					else
//    					{
//    						Skorer.Array_HomePage_Load.get(i).getNewsImage().setImageResource(R.drawable.thumb);
//    					}
//    				}
//    			}
//        	}
        }
        super.onScrollChanged( l, t, oldl, oldt ); 
    } 
}
