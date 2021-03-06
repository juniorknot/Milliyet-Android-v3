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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class MilliyetTvFragment extends Fragment {
    private static final String KEY_INDEX = "TestFragment:Index";
    private ImageView image;
    private ImageView PlayIcon;
    private TextView VideoTitle;
    private LinearLayout BlackBand;
    public DownloadImageTask downloadImageTask;
    public static ArrayList<DataModelVideoHeadlines> results_array;
    private View view;
    private int index=0;
    private static String DirName="/sdcard/Milliyet/video/";
    private final String advertorialContentName="advertoriallink";
    private static String TvName;
    public static int maxIndex=-1;
    private static AppMap appMap;

    public static MilliyetTvFragment newInstance(ArrayList<DataModelVideoHeadlines> results, int position, Context context, Context contextDialog, String controller) 
    {
    	appMap=new AppMap(context,contextDialog);
    	results_array=results;
        MilliyetTvFragment fragment = new MilliyetTvFragment();
        fragment.index=position;
        if (controller.toLowerCase().equals("milliyettv") || controller.toLowerCase().equals("dummymilliyettv"))
		{
			DirName="/sdcard/Milliyet/milliyettv/";
			TvName="MilliyetTV";
		}
        else
        {
        	DirName="/sdcard/Milliyet/skorertv/";
        	TvName="SkorerTV";
        }
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
    	image=new ImageView(getActivity());
        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.milliyettv_headlines,null);
        image=(ImageView)view.findViewById(R.id.videoImage);
        PlayIcon=(ImageView)view.findViewById(R.id.PlayIcon);
        BlackBand=(LinearLayout)view.findViewById(R.id.BlackBand);
        VideoTitle=(TextView)view.findViewById(R.id.VideoTitle);
        if (results_array.get(index).getTitle().equals("") || DirName.contains("skorer"))
		{
        	BlackBand.setVisibility(View.GONE);
		}
        else
        {
        	VideoTitle.setText(results_array.get(index).getTitle());
        }
        
        image.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				appMap.RunActivity(results_array.get(index).getContentType(), TvName, results_array.get(index).getID(),results_array.get(index).getLink());
			}
		});
        downloadImageTask=new DownloadImageTask(image, PlayIcon,index);
        if (!(new File(DirName+Integer.toString(index)+".jpeg")).exists())
        {
        	try 
            {
            	downloadImageTask.execute(results_array.get(index).getImageURL());
            	maxIndex=index;
    		} 
            catch (Exception e) 
            {
    			Log.e("Hata",e.toString());
    		}
		}
        else
        {
        	image.setImageURI(Uri.fromFile(new  File(DirName+Integer.toString(index)+".jpeg")));
        	if (!results_array.get(index).getContentType().toLowerCase().equals(advertorialContentName)) //content type advertoriallink ise play iconunu görünmez olarak bırakıyoruz
			{
	    		this.PlayIcon.setVisibility(View.VISIBLE);
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
	    private ImageView bmImage;
	    private int Index;
	    private ImageView PlayIcon;
	    
	    public DownloadImageTask(ImageView bmImage, ImageView playIcon, int index_) 
	    {
	        this.bmImage = bmImage;
	        this.Index=index_;
	        this.PlayIcon=playIcon;
	    }

	    protected Bitmap doInBackground(String... urls) 
	    {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try 
	        {
	        	Global.DownloadFile(Global.resizeImageURL(urldisplay, "320", "260"), Integer.toString(this.Index)+".jpeg",DirName);
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
	    	if (!results_array.get(this.Index).getContentType().toLowerCase().equals(advertorialContentName)) //content type advertoriallink ise play iconunu görünmez olarak bırakıyoruz
			{
	    		this.PlayIcon.setVisibility(View.VISIBLE);
			} 
	    }
	}
}
