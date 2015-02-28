package com.ahluwalia.atlas;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appflood.AFBannerView;
import com.appflood.AppFlood;
import com.appflood.AppFlood.AFRequestDelegate;

public class ScreenSlidePageFragment extends Fragment {

	public Activity __activity = FlagPagerScreen.flagPagerScreenThis;
	public int __position = ScreenSlidePagerAdapter._position;
	private String [] __searchResults = FlagPagerScreen.array;
	private TextToSpeech __tts = FlagPagerScreen.tts;

	//public ScreenSlidePageFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.flag_scroller, container, false);

		AppFlood.initialize(__activity, "Za0LIuRCXqIX8MK1" ,"hVsgr8he36d8L531fc236", AppFlood.AD_ALL);
		FlagPagerScreen.appFlood = (AFBannerView)rootView.findViewById(R.id.banner_view_appFlood);
		AppFlood.preload(AppFlood.AD_PANEL| AppFlood.AD_LIST, new AFRequestDelegate() {
			@Override
			public void onFinish(JSONObject arg0) {
			}
		});

		TextView cName = (TextView)rootView.findViewById(R.id.cName),
				cFullName = (TextView)rootView.findViewById(R.id.cFullName), 
				cCapital = (TextView)rootView.findViewById(R.id.cCapital),
				cArea = (TextView)rootView.findViewById(R.id.cArea),
				cCont = (TextView)rootView.findViewById(R.id.cCont),
				cCurrency = (TextView)rootView.findViewById(R.id.cCurrency),
				cGov = (TextView)rootView.findViewById(R.id.cGov),
				clang = (TextView)rootView.findViewById(R.id.cLang),
				cPop = (TextView)rootView.findViewById(R.id.cPop),
				cTLD = (TextView)rootView.findViewById(R.id.cTLD),
				cColCode = (TextView)rootView.findViewById(R.id.cColCode);
		final TextView cWiki = (TextView)rootView.findViewById(R.id.cWiki);

		ImageView cPronounce = (ImageView)rootView.findViewById(R.id.cPronounce);

		final flagObject countryObj = MenuScreen.mapDB.get(__searchResults[__position]);        
		cWiki.setPaintFlags(cWiki.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		try{
			cName.setText(countryObj.cName);
			cFullName.setText(countryObj.cFullName);
			cCapital.setText(countryObj.cCapital);
			cArea.setText(countryObj.cArea);
			cCont.setText(countryObj.cCont);
			cCurrency.setText(countryObj.cCurrency);
			cGov.setText(countryObj.cGov);
			clang.setText(countryObj.cLang);
			cPop.setText(countryObj.cPop);
			cTLD.setText(countryObj.cTLD);
			cColCode.setText(countryObj.cColCode);

			cPronounce.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					__tts.speak(countryObj.cName, TextToSpeech.QUEUE_ADD, null);			
				}
			});

			cWiki.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(MenuScreen.isNetworkAvailable(__activity)){
						startActivity(new Intent(__activity,WebViewScreen.class).putExtra("url", countryObj.cURL));	
					} else {
						cWiki.setText("To see Wikipedia, click here to go to network connection settings");
						MenuScreen.showDialogBox(__activity);
					}							
				}
			});
		} catch(NullPointerException e){}

		final ImageView thumb_image = (ImageView)rootView.findViewById(R.id.list_image); // thumb image

		new AsyncTask<ImageView, Integer, Bitmap>() {

			private final WeakReference<ImageView> viewReference = new WeakReference<ImageView>( thumb_image );
			private Bitmap bmp = null;
			@Override
			protected Bitmap doInBackground(ImageView... params) {
				try{
					//if(listBitmaps_256.containsKey(__searchResults[__position])){
					//bmp = listBitmaps_256.get(__searchResults[__position]);
					//} else {
					bmp = BitmapFactory.decodeStream(__activity.getResources().openRawResource(MenuScreen.imageIds_256.get(__searchResults[__position])));
					//listBitmaps_256.put(GameScreen.countries[__position],bmp);
					//}
				} catch(Exception e1){
					//bmp = BitmapFactory.decodeStream(_activity.getResources().openRawResource(GameScreen.imageIds_128[0]));
				}
				catch(OutOfMemoryError e2){}        		  
				return bmp;
			}

			@Override
			protected void onPostExecute( Bitmap bitmap ) {
				ImageView imageView = viewReference.get();
				if( imageView != null ) {
					imageView.setImageBitmap( bitmap );
				}
			} 	 
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumb_image);

		return rootView;
	}
}

class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public Activity _activity;
	public String[] _list;
	private TextToSpeech _tts;
	static int _position;

	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
		_activity = FlagPagerScreen.flagPagerScreenThis;
		_list = FlagPagerScreen.array;
		_tts = FlagPagerScreen.tts;
	}

	@Override
	public Fragment getItem(int position) {
		_position = position;
		return new ScreenSlidePageFragment();
	}

	@Override
	public int getCount() {
		return FlagPagerScreen.NUM_PAGES;
	}

	@Override
	public CharSequence getPageTitle (int position) {
		return FlagPagerScreen.array[position];
	}
}

class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 1) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
					(1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}

class LazyAdapter extends BaseAdapter {

	private String [] _list;
	private Activity _activity;
	//private HashMap<String, Bitmap> listBitmaps_128 = new HashMap<String, Bitmap>();

	public LazyAdapter(String[] collection) {
		_list = collection;
		_activity = FlagScreen.FlagScreenThis;
	}

	public int getCount() {
		return _list.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View vi=convertView;
		View vi = null;
		//if(convertView==null)
		vi = inflater.inflate(R.layout.flag_list, null);

		TextView cName = (TextView)vi.findViewById(R.id.CountryName),
				cCapitalName = (TextView)vi.findViewById(R.id.CapitalName);
		final ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image); // thumb image

		try{
			cName.setText(_list[position]);
			cCapitalName.setText(MenuScreen.mapDB.get(_list[position]).cCapital);
		} catch(NullPointerException e){}
		//thumb_image.setImageResource(GameScreen.imageIds[position]);

		new AsyncTask<ImageView, Integer, Bitmap>() {

			private final WeakReference<ImageView> viewReference = new WeakReference<ImageView>( thumb_image );
			private Bitmap bmp = null;
			@Override
			protected Bitmap doInBackground(ImageView... params) {
				try{
					//if(listBitmaps_128.containsKey(_list[position])){
					//bmp = listBitmaps_128.get(_list[position]);
					//} else {
					bmp = BitmapFactory.decodeStream(_activity.getResources().openRawResource(MenuScreen.imageIds_128.get(_list[position])));
					//listBitmaps_128.put(_list[position],bmp);
					//}
				} catch(Exception e1){
					//bmp = BitmapFactory.decodeStream(_activity.getResources().openRawResource(GameScreen.imageIds_128[0]));
				}
				catch(OutOfMemoryError e2){}        		  
				return bmp;
			}

			@Override
			protected void onPostExecute( Bitmap bitmap ) {
				ImageView imageView = viewReference.get();
				if( imageView != null ) {
					imageView.setImageBitmap( bitmap );
				}
			} 	 
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumb_image);

		return vi;
	}
}

class listener implements RecognitionListener          
{
	String TAG = "FlagScrenSpeechSearch";
	public void onReadyForSpeech(Bundle params)
	{
		//Log.d(TAG, "onReadyForSpeech");
	}
	public void onBeginningOfSpeech()
	{
		Log.d(TAG, "onBeginningOfSpeech");
	}
	public void onRmsChanged(float rmsdB)
	{
		//Log.d(TAG, "onRmsChanged");
	}
	public void onBufferReceived(byte[] buffer)
	{
		//Log.d(TAG, "onBufferReceived");
	}
	public void onEndOfSpeech()
	{
		Log.d(TAG, "onEndofSpeech");
	}
	public void onError(int error)
	{
		if ((error == SpeechRecognizer.ERROR_NO_MATCH)
				|| (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT))
		{
			Log.d(TAG, "didn't recognize anything");
			FlagScreen.tts.speak("Din't recognize anything, please tap button to search again", TextToSpeech.QUEUE_ADD, null);
			// keep going
			//startRecognising();
		}
	}
	public void onResults(Bundle results)                   
	{
		Log.d(TAG, "onResults " + results);
		ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		List<String> listKeys = new ArrayList<String>();

		for (int i = 0; i < data.size(); i++)
		{
			Log.d(TAG, "result " + data.get(i));
			listKeys.add(data.get(i).toString().toLowerCase());
		}             		

		Collection<String> x = GameScreen.getBestMatch(listKeys);
		if(x != null) FlagScreen.searchResults = x.toArray (new String[x.size ()]);
		if(FlagScreen.searchResults != null){
			FlagScreen.adapterList = new LazyAdapter(FlagScreen.searchResults);     
			FlagScreen.list.setAdapter(FlagScreen.adapterList);
			FlagScreen.adapter = new CustomGrid(FlagScreen.searchResults);
			FlagScreen.grid.setAdapter(FlagScreen.adapter);
		} else {
			FlagScreen.tts.speak("Din't recognize anything, please tap button to search again", TextToSpeech.QUEUE_ADD, null);
		}

	}
	public void onPartialResults(Bundle partialResults)
	{
		//Log.d(TAG, "onPartialResults");
	}
	public void onEvent(int eventType, Bundle params)
	{
		//Log.d(TAG, "onEvent " + eventType);
	}
}

class CustomGrid extends BaseAdapter{
	private Context _activity;
	private final String[] _list;

	public CustomGrid(String[] list) {
		_activity = FlagScreen.FlagScreenThis;
		_list = list;
	}
	@Override
	public int getCount() {
		return _list.length;
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid = convertView;
		LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = inflater.inflate(R.layout.flag_grid_single_cell, null);
		}           
		TextView textView = (TextView) grid.findViewById(R.id.grid_text);
		textView.setText(_list[position]);
		final ImageView thumb_image = (ImageView)grid.findViewById(R.id.grid_image);

		new AsyncTask<ImageView, Integer, Bitmap>() {

			private final WeakReference<ImageView> viewReference = new WeakReference<ImageView>( thumb_image );
			private Bitmap bmp = null;
			@Override
			protected Bitmap doInBackground(ImageView... params) {
				try{
					//if(listBitmaps_128.containsKey(_list[position])){
						//bmp = listBitmaps_128.get(_list[position]);
						//} else {
							bmp = BitmapFactory.decodeStream(_activity.getResources().openRawResource(MenuScreen.imageIds_128.get(_list[position])));
							//listBitmaps_128.put(_list[position],bmp);
							//}
				} catch(Exception e1){
					//bmp = BitmapFactory.decodeStream(_activity.getResources().openRawResource(GameScreen.imageIds_128[0]));
				}
				catch(OutOfMemoryError e2){}        		  
				return bmp;
			}

			@Override
			protected void onPostExecute( Bitmap bitmap ) {
				ImageView imageView = viewReference.get();
				if( imageView != null ) {
					imageView.setImageBitmap( bitmap );
				}
			} 	 
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumb_image);

		return grid;
	}
}
