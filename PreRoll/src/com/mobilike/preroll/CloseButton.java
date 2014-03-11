package com.mobilike.preroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

final class CloseButton extends BaseView
{
	int state = MotionEvent.ACTION_UP;
	
	public CloseButton(Context context)
	{
		super(context);
		
		initialize();
	}
	
	public CloseButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		initialize();
	}

	public CloseButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		initialize();
	}
	
	private void initialize()
	{
		// Add button-ish behaviors
		setClickable(true);
		setBackgroundColor(Color.TRANSPARENT);
		setSoundEffectsEnabled(true);
		
//		StateListDrawable background = new StateListDrawable();
//		
//		background.
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		

		// TODO: Move object allocations to initializer & update with callback method relative to size.
		
		//// Sizes
		// View
		float width = (float) ViewUtilities.sharedInstance().getViewWidth(this);
		float height = (float) ViewUtilities.sharedInstance().getViewHeight(this);
		// Wrapper
		float wrapperWidth = width;
		float wrapperHeight = height;
		float wrapperPaddingTop = 0;
		float wrapperPaddingLeft = 0;
		// Background
		float backgroundWidth = width *.8f;
		float backgroundHeight = height * .8f;
		float backgroundPaddingTop = (height - backgroundHeight) * .5f;
		float backgroundPaddingLeft = (width - backgroundWidth) * .5f;
		// Mark
		float markWidth = width * .45f;
		float markHeight = height * .45f;
		float markPaddingTop = (height - markHeight) * .5f;
		float markPaddingLeft = (width - markWidth) * .5f;
		float markLineThickness = width * .12f;
		
		
		RectF wrapperRectangle = new RectF(	wrapperPaddingLeft,
											wrapperPaddingTop,
											wrapperPaddingLeft + wrapperWidth,
											wrapperPaddingTop + wrapperHeight);
		RectF backgroundRectangle = new RectF(	backgroundPaddingLeft,
											  	backgroundPaddingTop,
											  	backgroundPaddingLeft + backgroundWidth, 
											  	backgroundPaddingTop + backgroundHeight);
		
		// Paints
		Paint wrapperPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		wrapperPaint.setColor(foregroundColorForState(this.state));
		
		Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(backgroundColorForState(this.state));
		
		Paint markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markPaint.setColor(foregroundColorForState(this.state));
		markPaint.setStyle(Paint.Style.STROKE);
		markPaint.setStrokeWidth(markLineThickness);
		
		// Wrapper ellipse drawing
		canvas.drawOval(wrapperRectangle, wrapperPaint);
		
		// Background ellipse drawing
		canvas.drawOval(backgroundRectangle, backgroundPaint);
		
		//// X mark drawing
		// Draw '\' (Left Top to Right Bottom)
		canvas.drawLine(markPaddingLeft, markPaddingTop, markPaddingLeft + markWidth, markPaddingTop + markHeight, markPaint);
		
		// Draw '/' (Right Top to Left Bottom)
		canvas.drawLine(markPaddingLeft + markWidth, markPaddingTop, markPaddingLeft, markPaddingTop + markHeight, markPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		this.state = event.getAction();
		
		// Force re-draw for current state
		invalidate();
		
		return super.onTouchEvent(event);
	}
	
	private int backgroundColorForState(int state)
	{
		int color = Color.BLACK;
		
		switch (state)
		{
			case MotionEvent.ACTION_DOWN:
			{
				color = Color.rgb(140, 0, 5);
				
				break;
			}
			case MotionEvent.ACTION_UP:
			default:
			{
				color = Color.BLACK;
				
				break;
			}
		}
		
		return color;
	}
	
	private int foregroundColorForState(int state)
	{
		return Color.WHITE;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		
		// If set alpha supported
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			if(enabled)
			{
				this.setAlpha(1.0f);
			}
			else
			{
				this.setAlpha(.15f);
			}
		}
	}
	

	/***************************************
	 * Log
	 */

	@Override
	protected boolean isLogEnabled() {
		return true;
	}

	@Override
	protected String getLogTag() {
		return "CloseButton";
	}
}
