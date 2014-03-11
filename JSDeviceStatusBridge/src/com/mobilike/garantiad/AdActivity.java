package com.mobilike.garantiad;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobilike.garantiad.GarantiAdManager.Ad;
import com.mobilike.jsdevicestatusbridge.Bridge;

public class AdActivity extends Activity
{
	public static final String BUNDLE_AD_KEY = "bundle.ad";
	
	private WebView webView;
    private boolean isWebViewAvailable;
    private Ad ad;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null)
		{
			ad = getIntent().getExtras().getParcelable(BUNDLE_AD_KEY);
		}
		else
		{
			ad = savedInstanceState.getParcelable(BUNDLE_AD_KEY);
		}
		
		createWebView();
		
		View view = new View(this);
		view.setBackgroundResource(android.R.color.transparent);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		setContentView(webView);

		webView.loadUrl(ad.getUrl());
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@TargetApi(19)
	private void createWebView()
	{
//		if (webView != null)
//        {
//            webView.destroy();
//        }
        
        webView = new WebView(this);
        
        //webView.setBackgroundResource(android.R.color.transparent);
        webView.setBackgroundColor(0x00000000);
		if (Build.VERSION.SDK_INT >= 11) webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        isWebViewAvailable = true;
		
		if(webView != null)
		{
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			    WebView.setWebContentsDebuggingEnabled(true);
			}

			webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        webView.getSettings().setJavaScriptEnabled(true);
	        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	        webView.setWebChromeClient(new WebChromeClient());
	        webView.addJavascriptInterface(new Bridge(this, null), "AndroidBridge");
		}
		
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				//webView.setBackgroundResource(android.R.color.transparent);
				webView.setBackgroundColor(0x00000000);
				if (Build.VERSION.SDK_INT >= 11) webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putParcelable(BUNDLE_AD_KEY, ad);
	}
	
	/**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause()
    {
        super.onPause();
        webView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume()
    {
        webView.onResume();
        super.onResume();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy()
    {
//        if (webView != null)
//        {
//            webView.destroy();
//            webView = null;
//            isWebViewAvailable = false;
//        }
        
        super.onDestroy();
    }
	
	/**
     * Gets the WebView.
     */
    public WebView getWebView()
    {
        return isWebViewAvailable ? webView : null;
    }
}
