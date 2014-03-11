package com.amvg.milliyet.main; //heeeee7

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.annotations.SerializedName;
import com.mobilike.garantiad.GarantiAdManager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import de.madvertise.android.sdk.MadvertiseTracker;
import de.madvertise.android.sdk.MadvertiseView;
import de.madvertise.android.sdk.MadvertiseView.MadvertiseViewCallbackListener;

import com.comscore.analytics.comScore;

@SuppressLint(
{ "NewApi", "SetJavaScriptEnabled", "CutPasteId" })
public class Home extends FragmentActivity implements AnimationListener, MadvertiseViewCallbackListener
{
	private final String providerUrl = "http://adserv.nmdapps.com/milliyet_android.mobilike";
	private ListView listView;
	public float dpHeight;
	public static float dpHeight_hp;
	private Display display;
	private DisplayMetrics outMetrics;
	public static int dikeyVideoSay;
	public int dikeyVideoSay_hp;
	private File myFile;
	private FileWriter fooWriter;
	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private String DirName = "/sdcard/Milliyet/home/";
	private LinearLayout TopHeadlinesC_layout; // guncel
	private LinearLayout HomePage_layout;
	private LayoutInflater LayInfalater; // guncel
	public static TextView weatherDate;
	public static TextView weatherInfo;
	public static ImageView weatherImage;
	public static LinearLayout yellowBandLinearLayout;
	public static RelativeLayout yellowBandRelativeLayout;
	public static TextView yellowBandTitle;
	public static TextView yellowBandID;
	public static TextView yellowBandContentType;
	public static View YellowBandDivider;
	public static boolean imageClickControl;
	public static TextView financeImkbPuan;
	public static TextView financeImkbYuzde;
	public static ImageView financeImkbIcon;
	public static TextView financeEuroPuan;
	public static TextView financeEuroYuzde;
	public static ImageView financeEuroIcon;
	public static TextView financeUsdPuan;
	public static TextView financeUsdYuzde;
	public static ImageView financeUsdIcon;
	public static TextView financeAltinPuan;
	public static TextView financeAltinYuzde;
	public static ImageView financeAltinIcon;
	private LinearLayout KategorilerLayout;
	private LinearLayout YazarlarLayout;
	private LinearLayout SkorerSporLayout;
	private LinearLayout GaleriLayout;
	private LinearLayout MilliyetTvLayout;
	private LinearLayout SkorerTvLayout;
	private LinearLayout HavaDurumuLayout;
	private LinearLayout AstrolojiLayout;
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	public static float scale;
	private View BannerDivider;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	public static ArrayList<DataModelNewsImageLoad> Array_TopHeadlinesC_Load;
	public static ArrayList<DataModelNewsImageLoad> Array_HomePage_Load;
	private int videoRowShowCount = 0; // videolardan kaçıncı satıra kadar gözükmesi gerektiğini tutar.
	private int videoRowShowCount_hp = 0;
	public ScrollViewExt sv;
	public static boolean HasBanner;
	public AppMap appmap;
	private MadvertiseTracker mTracker;
    public MadvertiseView mMadView;
	public View menu;
	public ViewUtils viewUtils;
	public View app;
	public View app2madad;
	public boolean menuOut = false;
	public static Context context;
	public AnimParams animParams = new AnimParams();
	public ListView listView_menu;
	private boolean AutoRefresh;

	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			System.out.println("onClick " + new Date());
			Home me = Home.this;
			context = me;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			Log.e("Menu Genisligi=", Integer.toString(left));
			if (!menuOut)//slide menünün açılışı
			{
				anim = new TranslateAnimation(0, left, 0, 0);
				menu.setVisibility(View.VISIBLE);
				animParams.init(left, 0, left + w, h);
			} 
			else  // slide menünün kapanışı
			{ 
				anim = new TranslateAnimation(0, -left, 0, 0);
				animParams.init(0, 0, w, h);
			}
			anim.setDuration(500);
			anim.setAnimationListener(me);
			anim.setFillAfter(true);
			app.startAnimation(anim);
		}
	}

	public class SearchResponse
	{
		public List<Result> root;
	}

	public class Result
	{
		@SerializedName("Controller")
		public String Controller;

		@SerializedName("Ref")
		public String Ref;

		@SerializedName("Title")
		public String Title;

		@SerializedName("CID")
		public String CID;
	}

	public boolean checkInternetConnection()
	{
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected())
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	public AlertDialog alertDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.con_message).setTitle(R.string.con_title);
		builder.setPositiveButton(R.string.yeniden_dene, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if (!checkInternetConnection())
				{
					alertDialog().show();
				} 
				else
				{
					updateApp();
				}
			}
		});
		builder.setNegativeButton(R.string.vazgec,new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}

	public void updateApp()
	{
		final JSon json = new JSon();
		String versionName = null;
		String parseParam[] =
		{
			"http://m2.milliyet.com.tr/AppConfigs/Milliyet-Android-v2/AppConfig.json",
			"AppID", "BundleID", "Version" };
		try
		{
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = pinfo.versionName;
		} 
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		json.MyPreloader(this, versionName);
		json.execute(parseParam);
	}

	@SuppressLint("NewApi")
	private void doFirstRun()
	{
		SharedPreferences settings = getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
		if (settings.getBoolean("isFirstRun", true))
		{
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		boolean firstboot = getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstboot", true);
		if (firstboot)
		{
			getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit().putBoolean("firstboot", false).commit();
		}
		super.onCreate(savedInstanceState);
		
		GarantiAdManager.loadAd(this, providerUrl, new GarantiAdManager.AdListener() {
			
			@Override
			public void onLoad()
			{
			}
			
			@Override
			public void onError()
			{
			}
		});
		
		AutoRefresh=false;
		
		comScore.setAppContext(this.getApplicationContext());
		
		
		HasBanner=false;
		MadvertiseView.setAge("20-26");
        MadvertiseView.setGender(MadvertiseView.GENDER_FEMALE);
		setContentView(R.layout.activity_home);
		mMadView = (MadvertiseView) findViewById(R.id.madad);
        mMadView.setMadvertiseViewCallbackListener(this);
		mTracker = MadvertiseTracker.getInstance(this);
		dikeyVideoSay_hp = 0;

		updateApp();

		viewUtils = new ViewUtils();

		menu = findViewById(R.id.menu);
		app = findViewById(R.id.app);

		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);

		listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
		listView_menu.invalidateViews();
		
		MainMenuAccessData mainMenuAccessData = new MainMenuAccessData(getApplicationContext(), listView_menu);
		mainMenuAccessData.execute("");
		
		viewUtils.initListView(this, listView_menu, "Item ", 30,R.id.list_View_Menu, getApplicationContext(), Home.this); // ,

		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-12");
		mGaTrackerGlobal=mGaInstance.getTracker("UA-15581378-27");
		// Placeholder
																// tracking ID.
		imageClickControl = true;
		try
		{
			SplashScreen.splash.finish();
			Home.scale = this.getResources().getDisplayMetrics().density;
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Milliyet");

		Array_TopHeadlinesC_Load = new ArrayList<DataModelNewsImageLoad>();
		Array_HomePage_Load = new ArrayList<DataModelNewsImageLoad>();

		appmap = new AppMap(getApplicationContext(), Home.this);

		mPager = (ViewPager) findViewById(R.id.pager);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		sv = new ScrollViewExt(getApplicationContext());
		sv = (ScrollViewExt)findViewById(R.id.scrollview);
		sv.setScrollViewListener(new OnScrollViewListener()
		{
			@Override
			public void onScrollChanged(ScrollViewExt scrollView, int x, int y,int oldx, int oldy)
			{
				boolean flag = true;
				flag = true;
				if ((y / scale) / 89 > videoRowShowCount && dikeyVideoSay + videoRowShowCount < Home.Array_TopHeadlinesC_Load.size())
				{
					String[] urlAddress;
					urlAddress = Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle doldurulmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						if (!(new File(DirName + urlAddress[urlAddress.length - 1])).exists())
						{
							new DownloadImageTask(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage(),urlAddress[urlAddress.length - 1], DirName, Home.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).getIsShown()).execute(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL());
						} 
						else
						{
							Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							Home.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).setIsShown(true);
						}
						
					} 
					catch (Exception e)
					{
						flag = false;
					}

					if (flag)
					{
						videoRowShowCount++;
					}
				}

				boolean flag2 = true;
				flag2 = true;
				if ((y / scale + dpHeight_hp) / 89 > videoRowShowCount_hp && videoRowShowCount_hp < Home.Array_HomePage_Load.size()&& !Home.Array_HomePage_Load.get(videoRowShowCount_hp).getIsShown())
				{
					String[] urlAddress;
					urlAddress = Array_HomePage_Load.get(videoRowShowCount_hp).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle doldurulmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						Log.e("girdi", "girdi");
						if (!(new File(DirName+ urlAddress[urlAddress.length - 1])).exists())
						{
							new DownloadImageTask(Array_HomePage_Load.get(videoRowShowCount_hp).getNewsImage(),urlAddress[urlAddress.length - 1], DirName,Home.Array_HomePage_Load.get(videoRowShowCount_hp).getIsShown()).execute(Array_HomePage_Load.get(videoRowShowCount_hp).getImageURL());
						} 
						else
						{
							Array_HomePage_Load.get(videoRowShowCount_hp).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							Home.Array_HomePage_Load.get(videoRowShowCount_hp).setIsShown(true);
						}
						
					} 
					catch (Exception e)
					{
						flag2 = false;
					}

					if (flag2)
					{
						videoRowShowCount_hp++;
					}
				}
			}
		});

		mIndicator.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				// TODO Auto-generated method stub
				Log.e("1", "1");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
			}
		});
		
		mPager.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.getParent().requestDisallowInterceptTouchEvent(true);
	            return false;
	        }
	    });

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

	        @Override
	        public void onPageSelected(int arg0) {
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	            mPager.getParent().requestDisallowInterceptTouchEvent(true);
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    });

//		mPager.setOnTouchListener(new View.OnTouchListener()
//		{
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event)
//			{
//				sv.requestDisallowInterceptTouchEvent(true);
//				return false;
//			}
//		});

		LayInfalater = getLayoutInflater();
		TopHeadlinesC_layout = (LinearLayout) findViewById(R.id.TopHeadlinesC_layout);
		HomePage_layout = (LinearLayout) findViewById(R.id.HomePage_layout);

		weatherDate = (TextView) findViewById(R.id.date);
		weatherInfo = (TextView) findViewById(R.id.weatherData);
		weatherImage = (ImageView) findViewById(R.id.image);

		yellowBandLinearLayout = (LinearLayout) findViewById(R.id.titleSonDakikaLayoutLinear);
		yellowBandRelativeLayout = (RelativeLayout) findViewById(R.id.titleSonDakikaLayoutRelative);
		yellowBandTitle = (TextView) findViewById(R.id.title);
		yellowBandID = (TextView) findViewById(R.id.yellowBandID);
		yellowBandContentType = (TextView) findViewById(R.id.yellowBandContentType);
		YellowBandDivider=(View)findViewById(R.id.YellowBandDivider);

		Home.yellowBandLinearLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
					appmap.RunActivity(Home.yellowBandContentType.getText().toString(), "",Home.yellowBandID.getText().toString(), "");
				
				
			}
		});
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);

		Home.financeAltinIcon = (ImageView) findViewById(R.id.AltinIcon);
		Home.financeAltinPuan = (TextView) findViewById(R.id.AltinPuan);
		Home.financeAltinYuzde = (TextView) findViewById(R.id.AltinYuzde);

		Home.financeEuroIcon = (ImageView) findViewById(R.id.EuroIcon);
		Home.financeEuroPuan = (TextView) findViewById(R.id.EuroPuan);
		Home.financeEuroYuzde = (TextView) findViewById(R.id.EuroYuzde);

		Home.financeUsdIcon = (ImageView) findViewById(R.id.UsdIcon);
		Home.financeUsdPuan = (TextView) findViewById(R.id.UsdPuan);
		Home.financeUsdYuzde = (TextView) findViewById(R.id.UsdYuzde);

		Home.financeImkbIcon = (ImageView) findViewById(R.id.ImkbIcon);
		Home.financeImkbPuan = (TextView) findViewById(R.id.ImkbPuan);
		Home.financeImkbYuzde = (TextView) findViewById(R.id.ImkbYuzde);
		
		KategorilerLayout=(LinearLayout)findViewById(R.id.KategorilerLayout);
		YazarlarLayout=(LinearLayout)findViewById(R.id.YazarlarLayout);
		SkorerSporLayout=(LinearLayout)findViewById(R.id.SkorerSporLayout);
		GaleriLayout=(LinearLayout)findViewById(R.id.GaleriLayout);
		MilliyetTvLayout=(LinearLayout)findViewById(R.id.MilliyetTvLayout);
		SkorerTvLayout=(LinearLayout)findViewById(R.id.SkorerTvLayout);
		HavaDurumuLayout=(LinearLayout)findViewById(R.id.HavaDurumuLayout);
		AstrolojiLayout=(LinearLayout)findViewById(R.id.AstrolojiLayout);
		
		KategorilerLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("NewsCategories", "", "", "");
			}
		});
		
		YazarlarLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Columnists", "", "", "");
			}
		});
		
		SkorerSporLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Skorer", "", "", "");
			}
		});
		
		GaleriLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Gallery", "", "", "");
			}
		});
		
		MilliyetTvLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("MilliyetTV", "", "", "");
			}
		});
		
		SkorerTvLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("SkorerTV", "", "", "");
			}
		});
		
		HavaDurumuLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Weather", "", "", "");
			}
		});
		
		AstrolojiLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Horoscopes", "", "", "");
			}
		});

		display = getWindowManager().getDefaultDisplay(); 
		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		dpHeight = outMetrics.heightPixels / (getResources().getDisplayMetrics().density);
		dpHeight -= 265;
		HomeAccessData accessData = new HomeAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
		final String[] in = { "in" };
		try
		{
			if (accessData.areFilesOK())
			{
				accessData.readData();
			} 
			else
			{
				accessData.execute(in);
			}
		} 
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File("/sdcard/Milliyet/homefirst/firstrun.txt");
		if (!file.exists())
		{
			File root = new File("/sdcard/Milliyet");
			root.mkdir();
			File directory = new File("/sdcard/Milliyet/homefirst");
			directory.mkdir();
			this.myFile = new File("/sdcard/Milliyet/homefirst/firstrun.txt");

			try
			{
				this.myFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(this.myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append("0");
				myOutWriter.close();
				fOut.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String aBuffer = "";
		File myFile = new File("/sdcard/Milliyet/homefirst/firstrun.txt");
		FileInputStream fIn;
		try
		{
			fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow;
			}
			myReader.close();
			if (aBuffer.equals("0"))
			{
				File dir = new File("/sdcard/Milliyet/newsarticles");
				if (dir.isDirectory())
				{
					String[] children = dir.list();
					for (int i = 0; i < children.length; i++)
					{
						new File(dir, children[i]).delete();
					}
				}
				File dir2 = new File("/sdcard/Milliyet/columnistsarticles");
				if (dir2.isDirectory())
				{
					String[] children = dir2.list();
					for (int i = 0; i < children.length; i++)
					{
						new File(dir2, children[i]).delete();
					}
				}
				this.fooWriter = new FileWriter(myFile, false); // true to
																// append
				// false to overwrite.
				fooWriter.write("1");
				fooWriter.close();
			}

		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// doFirstRun();

		boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
		if (firstrun)
		{

			File dir = new File("/sdcard/Milliyet/newsarticles");
			if (dir.isDirectory())
			{
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++)
				{
					new File(dir, children[i]).delete();
				}
			}
			File dir2 = new File("/sdcard/Milliyet/columnistsarticles");
			if (dir2.isDirectory())
			{
				String[] children = dir2.list();
				for (int i = 0; i < children.length; i++)
				{
					new File(dir2, children[i]).delete();
				}
			}
			// Save the state
			getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
		}

		ImageView menuLogo = (ImageView) findViewById(R.id.menuImage);
		menuLogo.setOnClickListener(new ClickListener());
		final ImageView refreshLogo = (ImageView) findViewById(R.id.refreshImage);
		refreshLogo.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Array_TopHeadlinesC_Load.clear();
				Array_HomePage_Load.clear();
				videoRowShowCount = 0;
				videoRowShowCount_hp = 0;
				HomeAccessData access = new HomeAccessData(getApplicationContext(), listView, Home.this,getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
				access.execute(in);
				sv.scrollTo(0, 0);

			}
		});
		final ImageView mainLogo = (ImageView) findViewById(R.id.logoImage);
		mainLogo.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		// Send a screen view when the Activity is displayed to the user.
		mGaTracker.sendView("Home");
		mGaTrackerGlobal.sendView("Home");
		mTracker.reportActive();
		FlurryAgent.onStartSession(this, "YEV3RXRLFB73A9IHAJEK");
	}

	@Override
	public void onBackPressed()
	{
		setClickable(true);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", true).commit();
			setClickable(true);
			finish();
			try
			{
				File myFileClose = new File("/sdcard/Milliyet/homefirst/firstrun.txt");
				FileWriter fooWriterClose = new FileWriter(myFileClose, false);
				fooWriterClose.write("0");
				fooWriterClose.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public static void setClickable(boolean value)
	{
		for (int i = 0; i < Array_TopHeadlinesC_Load.size(); i++)
		{
			Array_TopHeadlinesC_Load.get(i).getItemLayout().setClickable(value);
		}
		for (int i = 0; i < Array_HomePage_Load.size(); i++)
		{
			Array_HomePage_Load.get(i).getItemLayout().setClickable(value);
		}
	}

	void layoutApp(boolean menuOut)
	{
		System.out.println("layout [" + animParams.left + "," + animParams.top+ "," + animParams.right + "," + animParams.bottom + "]");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)app.getLayoutParams();
		params.gravity=Gravity.TOP;
		if (menuOut)
		{
			
			params.setMargins(animParams.left, 0, -animParams.left, 0); 
		}
		else
		{
			params.setMargins(0, 0, 0, 0); 
		}
		app.setLayoutParams(params);
		app.clearAnimation();
	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
		menuOut = !menuOut;
		if (!menuOut)
		{
			menu.setVisibility(View.INVISIBLE);
			listView_menu.setEnabled(true);
			if (Controller!=null)
			{
				if (!Controller.endsWith("Home") && IsMenuClicked)
				{
					Log.e("Controller",Controller);
					Log.e("CategoryName",CategoryName);
					Log.e("CategoryID",CategoryID);
					appmap.RunActivity(Controller,CategoryName, CategoryID,"");
					IsMenuClicked=false;
				}
			}
		}
		layoutApp(menuOut);
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub
		Log.e("onAnimationRepeat","onAnimationRepeat");
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		Log.e("onAnimationStart","onAnimationStart");
		// TODO Auto-generated method stub
	}

	static class AnimParams
	{
		int left, right, top, bottom;
		void init(int left, int top, int right, int bottom)
		{
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}

	class ViewUtils
	{
		private ViewUtils()
		{
		}

		public void setViewWidths(View view, View[] views)
		{
			int w = view.getWidth();
			int h = view.getHeight();
			for (int i = 0; i < views.length; i++)
			{
				View v = views[i];
				v.layout((i + 1) * w, 0, (i + 2) * w, h);
				printView("view[" + i + "]", v);
			}
		}

		public void printView(String msg, View v)
		{
			System.out.println(msg + "=" + v);
			if (null == v)
			{
				return;
			}
			System.out.print("[" + v.getLeft());
			System.out.print(", " + v.getTop());
			System.out.print(", w=" + v.getWidth());
			System.out.println(", h=" + v.getHeight() + "]");
			System.out.println("mw=" + v.getMeasuredWidth() + ", mh=" + v.getMeasuredHeight());
			System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY() + "]");
		}

		public void initListView(Context context, final ListView listView, String prefix, int numItems, int layout, final Context contextGetApplication, final Context contextThis)
		{
			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					listView.setEnabled(false);
					IsMenuClicked=true;
					Controller=((TextView) view.findViewById(R.id.controller)).getText().toString();
					CategoryName=((TextView) view.findViewById(R.id.empty)).getText().toString();
					CategoryID=((TextView) view.findViewById(R.id.CID)).getText().toString();
					itemClickMenuCloser();
				}
			});
		}

		public void itemClickMenuCloser()
		{
			Context context = Home.context;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			anim = new TranslateAnimation(0, -left, 0, 0);
			animParams.init(0, 0, w, h);
			anim.setDuration(500);
			anim.setAnimationListener((AnimationListener) context);
			anim.setFillAfter(true);
			app.startAnimation(anim);
		}
	}
	
	@Override
    protected void onResume() 
	{
		
		Log.e("onResume","onResume");
    	super.onResume();
    	comScore.onEnterForeground();
    	 
    	HomeAccessData accessData = new HomeAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
		try
		{
			if (AutoRefresh && checkInternetConnection())
			{
				if (accessData.areFilesOK()) 
				{
	//				accessData.readData();
				} 
				else
				{
					Array_TopHeadlinesC_Load.clear();
					Array_HomePage_Load.clear();
					videoRowShowCount = 0;
					videoRowShowCount_hp = 0;
					accessData.execute("");
					sv.scrollTo(0, 0);
				}
			}
		} 
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AutoRefresh=true;
    	
    }
	
	@Override
	public void onPause() 
	{
		super.onPause();
		// Notify comScore about lifecycle usage
		comScore.onExitForeground();
	}

    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        // Report that the application is being ended
        mTracker.reportStop();
    }

    @Override
    protected void onStop() 
    {
        super.onStop();

        // Report that the application becomes inactive
        mTracker.reportInactive();
        FlurryAgent.onEndSession(this);
    }

	@Override
	public void onLoaded(boolean succeed, MadvertiseView madView)
	{
		// TODO Auto-generated method stub
		if (succeed) 
		{
			HasBanner=true;
			BannerDivider.setVisibility(View.VISIBLE);
            Log.e("YOUR_LOG_TAG", "Ad successfully loaded___________________________");
        } 
		else 
		{
            Log.e("YOUR_LOG_TAG", "Ad could not be loaded___________________________");
        }
	}

	@Override
	public void onError(Exception exception)
	{ 
		// TODO Auto-generated method stub
		Log.e("onError","onError");
	}

	@Override
	public void onIllegalHttpStatusCode(int statusCode, String message)
	{
		Log.e("onIllegalHttpStatusCode","onIllegalHttpStatusCode");
		// TODO Auto-generated method stub
	}

	@Override
	public void onAdClicked()
	{
		// TODO Auto-generated method stub
		Log.e("YOUR_LOG_TAG", "Ad clicked");
	}

	@Override
	public void onApplicationPause()
	{
		// TODO Auto-generated method stub
		this.onPause();
	}

	@Override
	public void onApplicationResume()
	{
		// TODO Auto-generated method stub
		this.onResume();
		Log.e("onApplicationResume","onApplicationResume");
	}
}
