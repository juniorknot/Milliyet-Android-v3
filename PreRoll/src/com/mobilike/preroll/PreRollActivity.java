package com.mobilike.preroll;

import java.lang.ref.WeakReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.VideoView;

public class PreRollActivity extends BaseActivity
{
	public static final String PREROLLACTIVITY_XMLDOCUMENTURL_KEY = "prerollactivity.xmldocumenturl";
	
	
	private String xmlDocumentURLString = "http://mobworkz.com/video/vatan_spor.xml";
	
	private PreRoll preRoll = null;
	private WeakReference<VideoView> videoViewWeak = null;
	private WeakReference<TextView> messageTextViewWeak = null;
	private WeakReference<CloseButton> closeButtonWeak = null;
	
	private Handler handler = null;
	
	private boolean stopped = true;
	private boolean paused = true;
	private boolean contentMetadataLoaded = false;
	private int pauseTimestamp = 0;
	
	/**
	 * Prints loading message "Yükleniyor..." to UI
	 * with dots animating at each run
	 */
	private Runnable loadStateUIUpdateCallback = new Runnable()
	{
		private int dotCount = 0;
		
		@Override
		public void run()
		{
			PreRollActivity activity = PreRollActivity.this;
			
			// If application is not dead (non-null, non-quitting)
			if(ApplicationUtilities.sharedInstance().isActivityAlive(activity) && !activity.stopped)
			{
				TextView messageTextView = activity.getMessageTextView();
				
				// And if there is a view to print output
				if(messageTextView != null)
				{
					// Generate load message
					String loadMessage = createLoadMessage();
					
					// Print output
					messageTextView.setText(loadMessage);
				}
				
				activity.handler.postDelayed(activity.activeUIUpdateCallback, 1000);
			}
			
			// Increment dot count
			dotCount++;
		}
		
		private String createLoadMessage()
		{
			StringBuffer buffer = new StringBuffer("Yükleniyor");
			
			for(int i = 0; i < dotCount; i++)
			{
				buffer.append('.');
			}
			
			return buffer.toString();
		}
	};
	
	/**
	 * Prints restricted time period information to UI
	 */
	private Runnable restrictedStateUIUpdateCallback = new Runnable()
	{
		int elapsedTime = 0;
		
		@Override
		public void run()
		{
			PreRollActivity activity = PreRollActivity.this;
			
			// If application is not dead (non-null, non-quitting)
			if(ApplicationUtilities.sharedInstance().isActivityAlive(activity) && !activity.stopped)
			{
				// If we are in restricted period
				if(elapsedTime < activity.preRoll.getRestrictedDuration())
				{
					TextView messageTextView = activity.getMessageTextView();
					
					// And if there is a view to print output
					if(messageTextView != null)
					{
						// Generate load message
						String restrictionMessage = createRestrictedCountdownMessage();
						
						// Print output
						messageTextView.setText(restrictionMessage);
					}
					
					activity.handler.postDelayed(activity.activeUIUpdateCallback, 1000);
				}
				else
				{
					TextView messageTextView = activity.getMessageTextView();
					
					// And if there is a view to print output
					if(messageTextView != null)
					{
						// Clear restriction message
						messageTextView.setText("");
					}
					
					// Show skip/close button
					CloseButton closeButton = activity.getCloseButton();
					
					if(closeButton != null)
					{
						closeButton.setVisibility(View.VISIBLE);
					}
				}
				
				if(!activity.paused)
				{
					// Increment elapsed time, as it is elapsed :)
					elapsedTime++;
				}
			}
		}
		
		private String createRestrictedCountdownMessage()
		{
			StringBuffer buffer = new StringBuffer();
			
			int countdownTime = PreRollActivity.this.preRoll.getRestrictedDuration() - elapsedTime;
			
			buffer.append(countdownTime);
			buffer.append(" sn. sonra reklamı atlayabilirsiniz");
			
			return buffer.toString();
		}
	};
	
	private Runnable activeUIUpdateCallback = loadStateUIUpdateCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.stopped = false;
		
		setContentView(R.layout.activity_pre_roll);
		
		// Attach weak references of populated UI
		setVideoView((VideoView) findViewById(R.id.preroll_videoview));
		setMessageTextView((TextView) findViewById(R.id.preroll_header_messagetextview));
		setCloseButton((CloseButton) findViewById(R.id.preroll_header_closebutton));
		
		CloseButton closeButton = getCloseButton();
		
		if(closeButton != null)
		{
			closeButton.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					setResult(RESULT_OK);
					finish();
				}
			});
		}
		
		// If we received icicle topping on our sweet activity
		if(savedInstanceState != null)
		{
			this.xmlDocumentURLString = savedInstanceState.getString(PREROLLACTIVITY_XMLDOCUMENTURL_KEY);
		}
		
		// Initialize an handler for event scheduling, and much more...
		this.handler = new Handler();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		this.stopped = false;
		
		loadPreRoll();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		// Mark activity as not-paused
		this.paused = false;
		
		// If advertisement started to display before
		if(this.contentMetadataLoaded)
		{
			// Resume video content as we are active (visible) UI now.
			VideoView videoView = getVideoView();
			
			if(videoView != null)
			{
				videoView.seekTo(pauseTimestamp);
				videoView.resume();
			}
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		// Mark activity as paused
		this.paused = true;
		
		// Also, pause video content as we are not active (visible)
		// UI anymore.
		VideoView videoView = getVideoView();
		
		if(videoView != null)
		{
			pauseTimestamp = videoView.getCurrentPosition();
			videoView.pause();
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		
		// Mark activity as stopped
		this.stopped = true;
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		// TODO: Delete this! This was a poor attempt during bug fix...
		this.videoViewWeak = null;
		this.messageTextViewWeak = null;
		this.closeButtonWeak = null;
	}
	
	@Override
	public void onBackPressed()
	{
		// Logic is simple,
		// If we are at restricted period or video loading state,
		// we will not permit finishing activity.
		// Else, we are good to close pre-roll activity
		
		// In order to check permission of close attempt
		// Close button visibility is the decider for now.
		
		CloseButton closeButton = getCloseButton();
		
		// If our ultimate decider is not available,
		if(closeButton == null)
		{
			// We don't know what to do, try our best option
			// Perform default closing operation with result
			setResult(RESULT_OK);
			super.onBackPressed();
		}
		else if(closeButton.getVisibility() == View.VISIBLE)
		{
			// We got the button and it is visible
			// (no-restriction applied to user)
			// Perform default closing operation with result
			setResult(RESULT_OK);
			super.onBackPressed();
		}
		else
		{
			// We got the button but it is not enabled (visible) yet.
			// Ignore back presses, as it is restricted for now...
		}
	}

	private void loadPreRoll()
	{
		// Start UI state message updater
		handler.post(activeUIUpdateCallback);
		
		new XMLHTTPRequestWorker()
		.setOnJobDoneListener(new OnJobDoneListener<Document>()
		{
			@Override
			void onJobDone(int status, Document result)
			{	
				switch (status)
				{
					case OnJobDoneListener.JobStatus.SUCCEED:
					{
						final PreRollActivity activity = PreRollActivity.this;
						
						if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
						{
							activity.contentMetadataLoaded = true;
							activity.preRoll = new PreRoll(result);
							
							activity.runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									activity.playPreRoll();
								}
							});
						}
						else
						{
							// Activity already dead or dying
							// Will take no action as this scenario is not at our control.
						}
						
						break;
					}
					default:
					{
						final PreRollActivity activity = PreRollActivity.this;
						
						if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
						{
							activity.contentMetadataLoaded = true;
							
							// Close pre-roll
							activity.setResult(RESULT_CANCELED);
							activity.finish();
						}
						
						break;
					}
				}
			}
		})
		.execute(xmlDocumentURLString);
	}
	
	void playPreRoll()
	{
		// Play video
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				PreRollActivity activity = PreRollActivity.this;
				
				if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
				{
					VideoView videoView = activity.getVideoView();
					
					if(videoView != null)
					{
					    if(URLUtil.isValidUrl(activity.preRoll.getVideoURLString()))
					    {
					    	// We want user to observe our precious ads, don't we.
					    	videoView.setKeepScreenOn(true);
					    	
							Uri uri = Uri.parse(activity.preRoll.getVideoURLString());
							
							videoView.setMediaController(null);
					    	videoView.setVideoURI(uri);
					    	videoView.requestFocus();
						    videoView.start();
						    
						    // Update active UI update callback
							activity.activeUIUpdateCallback = restrictedStateUIUpdateCallback;
						    
						    // Attach click listener for advertisement target navigation
						    videoView.setOnTouchListener(new View.OnTouchListener()
							{
						    	private boolean isRedirected = false;
						    	
								@Override
								public boolean onTouch(View v, MotionEvent event)
								{
									synchronized (this)
									{
										if(!isRedirected)
										{
//											isRedirected = true;
//											
//											Uri targetUri = Uri.parse(PreRollActivity.this.preRoll.getTargetURLString());
//											
//											Intent browserIntent = new Intent(Intent.ACTION_VIEW, targetUri);
//											PreRollActivity.this.getApplicationContext().startActivity(browserIntent);
//											
////											new Handler().postDelayed(new Runnable()
////											{
////												@Override
////												public void run()
////												{
//													setResult(RESULT_OK);
//													finish();
////												}
////											}, 1000);
											
											isRedirected = true;
											
											PreRollActivity activity = PreRollActivity.this;
											
											if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
											{
												Uri targetUri = Uri.parse(activity.preRoll.getTargetURLString());
												
												Intent browserIntent = new Intent(Intent.ACTION_VIEW, targetUri);
												activity.startActivity(browserIntent);
												
												activity.setResult(RESULT_OK);
												activity.finish();
											}
										}
									}
									
									return false;
								}
							});
						 
						    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
							{
								@Override
								public void onCompletion(MediaPlayer mp)
								{
									// Video finished inform host activity with RESULT_OK callback.
									
									PreRollActivity activity = PreRollActivity.this;
									
									if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
									{
										activity.setResult(RESULT_OK);
										finish();
									}
									
								}
							});
					    }
					    else
					    {
					    	log("External error: Unable to play given URL!");
							
							activity.setResult(RESULT_CANCELED);
							activity.finish();
					    }
					}
					else
					{
						log("Internal error: Unable to find video view reference!");
						
						activity.setResult(RESULT_CANCELED);
						activity.finish();
					}
				}
				else
				{
					log("Unable to play pre-roll. Activity is rotten!");
				}
			}
		});
		
		
	}
	
	
	/***************************************
	 * Accessors
	 */
	
	private VideoView getVideoView()
	{
		return (this.videoViewWeak == null)?(null):(this.videoViewWeak.get());
	}
	
	private void setVideoView(VideoView videoView)
	{
		if(videoView == null)
		{
			this.videoViewWeak = null;
		}
		else
		{
			this.videoViewWeak = new WeakReference<VideoView>(videoView);
		}
	}
	
	private TextView getMessageTextView()
	{
		return (this.messageTextViewWeak == null)?(null):(this.messageTextViewWeak.get());
	}
	
	private void setMessageTextView(TextView textView)
	{
		if(textView == null)
		{
			this.messageTextViewWeak = null;
		}
		else
		{
			this.messageTextViewWeak = new WeakReference<TextView>(textView);
		}
	}
	
	private CloseButton getCloseButton()
	{
		return (this.closeButtonWeak == null)?(null):(this.closeButtonWeak.get());
	}
	
	private void setCloseButton(CloseButton closeButton)
	{
		if(closeButton == null)
		{
			this.closeButtonWeak = null;
		}
		else
		{
			this.closeButtonWeak = new WeakReference<CloseButton>(closeButton);
		}
	}
	
	/***************************************
	 * Etc...
	 */
	
	/**
	 * Simple interface far adding dynamic callback.
	 */
	abstract class OnJobDoneListener<E>
	{
		public class JobStatus
	    {
			public static final int SUCCEED = 1;
			public static final int FAILED = 0;
			public static final int CANCELLED = -1;
	    }
		
		abstract void onJobDone(int status, E result);
		
		/**
		 * 
		 * @param status {@code OnJobDoneListener.JobStatus}
		 */
		void completeJobAsynch(int status)
		{
			this.completeJobAsynch(status, null);
		}
		
		/**
		 * 
		 * @param status {@code OnJobDoneListener.JobStatus}
		 * @param attachment Attachment object for flexible job callback. This parameter is optional
		 */
		@SuppressWarnings("unchecked")
		void completeJobAsynch(int status, E attachment)
		{
			new JobStatusCallbackWorker()
			.setJobStatus(status)
			.setAttachment(attachment)
			.execute(this, null, null);
		}
		
		private class JobStatusCallbackWorker extends AsyncTask<OnJobDoneListener<E>, Void, Void>
		{
			private int jobStatus = JobStatus.FAILED;
			private E attachment = null;

			@Override
			protected Void doInBackground(OnJobDoneListener<E>... params)
			{
				// Initialize listener as null (invalid)
				OnJobDoneListener<E> listener = null;
							
				// If parameters provided
				if((params != null) && (params.length > 0))
				{
					// Get listener reference
					listener = params[0];
				}
				
				listener.onJobDone(jobStatus, attachment);
				
				return null;
			}
			
			JobStatusCallbackWorker setJobStatus(int status)
			{
				this.jobStatus = status;
				
				return this;
			}
			
			JobStatusCallbackWorker setAttachment(E attachment)
			{
				this.attachment = attachment;
				
				return this;
			}
		}
	}
	
	class XMLHTTPRequestWorker extends AsyncTask<String, Void, Document>
	{
		private OnJobDoneListener<Document> onJobDoneListener = null;
		
		@Override
		protected Document doInBackground(String... params)
		{
			String urlString = (params.length > 0)?(params[0]):(null);
			
			return getXMLDocument(urlString);
		}
		
		@Override
		protected void onPostExecute(Document result)
		{
			super.onPostExecute(result);
			
			// If there is a callback defined
			if(this.onJobDoneListener != null)
			{
				// If task failed to fetch & parse the remote document
				if(result == null)
				{
					onJobDoneListener.completeJobAsynch(OnJobDoneListener.JobStatus.FAILED, result);
				}
				else
				{
					onJobDoneListener.completeJobAsynch(OnJobDoneListener.JobStatus.SUCCEED, result);
				}
			}
		}
		
		private Document getXMLDocument(String urlString)
		{
			Document xmlDocument = null;
			
			// If request worth to try (valid URL provided)
			if(!TextUtils.isEmpty(urlString))
			{
				try
				{
					// Fetch remote XML document
					HttpGet uri = new HttpGet(urlString);    

					DefaultHttpClient client = new DefaultHttpClient();
					HttpResponse resp = client.execute(uri);

					// Read status code
					StatusLine status = resp.getStatusLine();
					// If request failed (Status code not OK.)
					if (status.getStatusCode() != 200)
					{
						BaseActivity baseObject = PreRollActivity.this;
						
						if(baseObject != null)
						{
							baseObject.log("HTTP error, invalid server status code: " + resp.getStatusLine());
						}
					}
					else
					{
						// Request succeed
						// Parse XML data
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						xmlDocument = builder.parse(resp.getEntity().getContent());
					}
					
					// Consume unused content
					resp.getEntity().consumeContent();
				}
				catch(Exception e)
				{
					BaseActivity baseObject = PreRollActivity.this;
					
					if(baseObject != null)
					{
						baseObject.log(e.getLocalizedMessage());
					}
				}
			}
			
			return xmlDocument;
		}
		
		XMLHTTPRequestWorker setOnJobDoneListener(OnJobDoneListener<Document> listener)
		{
			this.onJobDoneListener = listener;
			
			return this;
		}
	}

	
	/***************************************
	 * Log
	 */
	
	@Override
	protected boolean isLogEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getLogTag()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
