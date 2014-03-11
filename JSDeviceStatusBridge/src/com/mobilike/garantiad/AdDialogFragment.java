package com.mobilike.garantiad;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobilike.garantiad.GarantiAdManager.Ad;
import com.mobilike.garantiad.GarantiAdManager.AdListener;
import com.mobilike.jsdevicestatusbridge.Bridge;

public class AdDialogFragment extends WebviewDialogFragment
{
	/***************************************
	 * Variables
	 */
	
	public static final String SAVEINSTANCE_AD_KEY = "saveinstance.ad";
	public static final String FRAGMENT_TAG = "mobilike.addialog";
	
	private Ad ad = null;
	private AdListener listener = null;
	
	
	/***************************************
	 * Constructors and instance providers
	 */
	
	public static AdDialogFragment newInstance(final Ad ad, final AdListener listener)
	{
		AdDialogFragment dialog = new AdDialogFragment();
		
		dialog.ad = ad;
		dialog.listener = listener;
		
		return dialog;
	}
	
	public AdDialogFragment() {
		// Empty constructor
		// Default layout values will be used (w:match_parent, h:match_parent)
	}
	
	
	/***************************************
	 * Dialog fragment state handlers
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null)
		{
			this.ad = savedInstanceState.getParcelable(SAVEINSTANCE_AD_KEY);
		}
	}
	
	@TargetApi(19)
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	public void onStart()
	{
		super.onStart();
		
		final WebView webView = getWebView();
		
		if(webView != null)
		{
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			    WebView.setWebContentsDebuggingEnabled(true);
			}

	        webView.getSettings().setJavaScriptEnabled(true);
	        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	        webView.setWebChromeClient(new WebChromeClient());
			webView.loadUrl(ad.getUrl());
	        webView.addJavascriptInterface(new Bridge(getActivity(), this), "AndroidBridge");
	        
	        webView.setWebViewClient(new AdWebViewClient(listener));
		}
		else if(listener != null)
		{
			listener.onError();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle out)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(out);
		
		out.putParcelable(SAVEINSTANCE_AD_KEY, this.ad);
	}
	
	
	/***************************************
	 * Minions
	 */
	
	private class AdWebViewClient extends WebViewClient
	{
		private AdListener listener = null;
		
		private AdWebViewClient(final AdListener listener)
		{
			this.listener = listener;
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
		 */
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			
			if(this.listener != null)
			{
				listener.onLoad();
			}
			
			view.clearCache(true);
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

			//webView.setBackgroundResource(android.R.color.transparent);
			view.setBackgroundColor(0x00000000);
			if (Build.VERSION.SDK_INT >= 11) view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			
			if(this.listener != null)
			{
				listener.onError();
			}
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
			
			return true;
		}
	}
}
