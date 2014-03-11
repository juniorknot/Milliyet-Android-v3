package com.mobilike.garantiad;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;

/**
 * <h3>Full-screen WebViewFragment & Dialog Fragment hybrid.</h3><br><br>
 * 
 * from WebViewFragment;<br><br>
 * 
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or resumed.
 * </p>
 * 
 * @author gokhanbarisaker
 *
 */
public class WebviewDialogFragment extends DialogFragment
{
	private WebView mWebView;
    private boolean mIsWebViewAvailable;
    
    
    /***************************************
	 * Dialog fragment state handlers
	 */
    
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		
		return dialog;
	}
    
    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    	
        if (mWebView != null)
        {
            mWebView.destroy();
        }
        
        mWebView = new WebView(getActivity());
        
        //mWebView.setBackgroundResource(android.R.color.transparent);
        mWebView.setBackgroundColor(0x00000000);
		if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        mIsWebViewAvailable = true;
        
        return mWebView;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	int style = DialogFragment.STYLE_NO_FRAME;
		int theme = android.R.style.Theme;
		
		setStyle(style, theme);
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause()
    {
        super.onPause();
        mWebView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume()
    {
        mWebView.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView()
    {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy()
    {
        if (mWebView != null)
        {
            mWebView.destroy();
            mWebView = null;
        }
        
        super.onDestroy();
    }

    
    /***************************************
	 * Accessors
	 */
    
    /**
     * Gets the WebView.
     */
    public WebView getWebView()
    {
        return mIsWebViewAvailable ? mWebView : null;
    }
}
