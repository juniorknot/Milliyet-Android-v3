package com.amvg.milliyet.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewExt extends HorizontalScrollView {
	
	private OnScrollViewHorizontalListener scrollViewHorizontalListener = null;
	private int index;

	public HorizontalScrollViewExt(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public HorizontalScrollViewExt(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public HorizontalScrollViewExt(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
    public void setScrollViewListener(int index, OnScrollViewHorizontalListener scrollViewHorizontalListener) {
    	this.index=index;
        this.scrollViewHorizontalListener = scrollViewHorizontalListener;
    }
    
    protected void OnScrollViewHorizontalListener(int i)
    {
    	this.index=i;
    }
    
    protected void onScrollChanged(int l, int t, int oldl, int oldt) { 
    	scrollViewHorizontalListener.onScrollChanged( this, l, t, oldl, oldt , this.index); 
        super.onScrollChanged( l, t, oldl, oldt );
    } 
}
