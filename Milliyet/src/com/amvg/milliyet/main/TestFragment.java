package com.amvg.milliyet.main;
import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public final class TestFragment extends Fragment 
{
    private static final String KEY_INDEX = "TestFragment:Index";
    public static ImageView image;
    public static RelativeLayout progressBarLayout;
    public DownloadImageTask downloadImageTask;
    public static ArrayList<DataModelHomeHeadlines> results_array;
    private View view;
    private int index=0;
    private String DirName="/sdcard/Milliyet/home/";
    private static AppMap appMap;
    
    

    public static TestFragment newInstance(ArrayList<DataModelHomeHeadlines> results, int position, Context context, Context contextDialog) 
    {
    	appMap=new AppMap(context,contextDialog);
    	results_array=results;
        TestFragment fragment = new TestFragment();
        fragment.index=position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_INDEX)) 
        {
            index=savedInstanceState.getInt(KEY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	Log.e("public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) ",".");
    	image=new ImageView(getActivity());
    	progressBarLayout=new RelativeLayout(getActivity());
        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.home_headlines,null); 
        image=(ImageView)view.findViewById(R.id.videoImage);
        image.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				appMap.RunActivity(results_array.get(index).getContentType(), "", results_array.get(index).getID(),results_array.get(index).getLink());
			}
		});
        progressBarLayout=(RelativeLayout)view.findViewById(R.id.loadingPanel);
        Log.e("Index:",Integer.toString(index));
        Log.e("ImageURL:",results_array.get(index).getImageURL());
        
        if (!(new File(DirName+Integer.toString(index)+".jpeg")).exists())
        {
        	if (index!=0 || true)
			{
            	try 
                {
            		downloadImageTask=new DownloadImageTask(image, progressBarLayout,index);
                	downloadImageTask.execute(results_array.get(index).getImageURL());
        		} 
                catch (Exception e) 
                {
        			Log.e("Hata",e.toString());
        		}

			}
		}
        else
        {
        	if (index!=0 || true)
			{
        		image.setImageURI(Uri.fromFile(new  File(DirName+Integer.toString(index)+".jpeg")));
			}
        }
        layout.addView(view);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, index);
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> 
    {
	    ImageView bmImage;
	    int Index;
	    
	    public DownloadImageTask(ImageView bmImage, RelativeLayout loadingPanel, int index_) 
	    {
	        this.bmImage = bmImage;
	        this.Index=index_;
	    }

	    protected Bitmap doInBackground(String... urls) 
	    {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try 
	        {
	        	Global.DownloadFile(Global.resizeImageURL(urldisplay, "320", "320"), Integer.toString(this.Index)+".jpeg",DirName);
	        }
	        catch (Exception e) 
	        {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) 
	    {
	    	bmImage.setImageURI(Uri.fromFile(new  File(DirName+Integer.toString(this.Index)+".jpeg")));
	    }
	}
}
