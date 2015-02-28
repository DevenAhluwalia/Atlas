package com.ahluwalia.atlas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.banner.bannerstandard.BannerStandard;

public class FlagScreen extends Activity implements TextToSpeech.OnInitListener{

	static AssetManager am;
	static ListView list;
	static LazyAdapter adapterList; 
	static CustomGrid adapter;
	static GridView grid;
	static Activity FlagScreenThis;
	static AutoCompleteTextView myAutoComplete;
	static ImageView FlagSpeechSearch, clearSearch;
	static String [] searchResults;
	SpeechRecognizer sr;
	static TextToSpeech tts;
	private BannerStandard startAppAd;
	static int pos = 0, n=1;
	static boolean searchButtonVisible = false;
	private ViewFlipper flipper;
	private Menu _menu;   
	private Animation animFlipInForeward, animFlipOutForeward;
	private int[] grid_list_IDs = new int[2];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flag_screen);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		startAppAd = (BannerStandard)findViewById(R.id.footer).findViewById(R.id.banner_view_startApp);  
		StartAppAd.init(this, "107431880", "207635201");
		startAppAd.showBanner();

		FlagScreenThis = this;
		tts = new TextToSpeech(this, this);

		animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
		animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
		
		searchResults = new String[270];
		searchResults = flagDB.countries;

		grid_list_IDs[0] = getResources().getIdentifier("list", "drawable", getPackageName()); 
		grid_list_IDs[1] = getResources().getIdentifier("grid", "drawable", getPackageName());

		list=(ListView)findViewById(R.id.list);
		flipper = (ViewFlipper)findViewById(R.id.flipper);

		adapterList = new LazyAdapter(searchResults);        
		list.setAdapter(adapterList);

		grid=(GridView)findViewById(R.id.grid); 
		adapter = new CustomGrid(searchResults);
		grid.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				Bundle b=new Bundle();
				b.putStringArray("countries_for_pager", searchResults); 
				startActivity(new Intent(getApplicationContext(),FlagPagerScreen.class).putExtras(b));
			}
		});

		FlagSpeechSearch = (ImageView)findViewById(R.id.flagSpeechSearch); 
		FlagSpeechSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				 
				if(!MenuScreen.isNetworkAvailable(FlagScreen.this)){
					MenuScreen.showDialogBox(FlagScreen.this);
				} else {
					Toast.makeText(getApplicationContext(), "listening :)", 1000).show();
					startRecognising();		
				}
			}
		});

		clearSearch = (ImageView)findViewById(R.id.clearSearch); 
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				myAutoComplete.setText("");
				searchResults = flagDB.countries;
				grid.setAdapter(new CustomGrid(searchResults));
				list.setAdapter(new LazyAdapter(searchResults));
			}
		});

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				Bundle b=new Bundle();
				b.putStringArray("countries_for_pager", searchResults); 
				startActivity(new Intent(getApplicationContext(),FlagPagerScreen.class).putExtras(b));
			}
		});


		final ArrayAdapter<String> adapterSearchBox = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,flagDB.countries);  
		myAutoComplete = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
		myAutoComplete.setHint("Search");
		myAutoComplete.setAdapter(adapterSearchBox);
		myAutoComplete.setThreshold(1);      
		myAutoComplete.setDropDownHeight(0);
		myAutoComplete.setDropDownWidth(0);

		adapterSearchBox.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();

				int numOfResults = adapterSearchBox.getCount(); 
				searchResults = new String[numOfResults];

				if(numOfResults != 0){
					for(int i=0;i<numOfResults;i++){
						searchResults[i] = adapterSearchBox.getItem(i).toString();        
					} 
					adapterList = new LazyAdapter(searchResults);        
					list.setAdapter(adapterList);
					adapter = new CustomGrid(searchResults);
					grid.setAdapter(adapter);
				}else {
					myAutoComplete.setHint("No match found, search again");
				}
			}
		});
	}

	private void SwipeRight(){
		//     	page.setInAnimation(animFlipInBackward);
		// 		page.setOutAnimation(animFlipOutBackward);
		flipper.showPrevious();
	}

	private void SwipeLeft(){
		//     	page.setInAnimation(animFlipInForeward);
		// 		page.setOutAnimation(animFlipOutForeward);
		flipper.showNext(); 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener(){
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			float sensitvity = 50;
			if((e1.getX() - e2.getX()) > sensitvity){
				SwipeLeft();
			}else if((e2.getX() - e1.getX()) > sensitvity){
				SwipeRight();
			}			
			return true;
		}    	
	};  
	GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

	@Override
	public void onResume() {
		super.onResume();	
	}

	@Override
	public void onBackPressed() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		if(sr != null){
			sr.stopListening();
		}
		super.onBackPressed();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if(tts != null) {
			tts.stop();
			tts.shutdown();
		}
		if(sr != null){
			sr.stopListening();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.grid_list, menu);
		_menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.aboutUS:
			startActivity(new Intent(this,AboutScreen.class));
			break;
		case R.id.rateApp:
			AboutScreen.rateApp(this);
			break;
		case R.id.grid_list:
			flipper.setInAnimation(animFlipInForeward);
			flipper.setOutAnimation(animFlipOutForeward);
			flipper.showNext();
			_menu.getItem(1).setIcon(grid_list_IDs[n^=1]);
			break;
		case R.id.search:
			if(searchButtonVisible){
				myAutoComplete.setVisibility(View.GONE);
				FlagSpeechSearch.setVisibility(View.GONE);
				clearSearch.setVisibility(View.GONE);
				searchButtonVisible = false;
			} else {
				myAutoComplete.setVisibility(View.VISIBLE);
				FlagSpeechSearch.setVisibility(View.VISIBLE);
				clearSearch.setVisibility(View.VISIBLE);
				searchButtonVisible = true;
			}
			break;
		case R.id.feedback:
			new FeedbackDialog().inflateFeedbackDialog(this);
			break;
		case R.id.demoToggle:
			FeedbackDialog.inflateDemoToggleDialog(this,false);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	} 

	public void startRecognising() {
		// TODO Auto-generated method stub
		sr = SpeechRecognizer.createSpeechRecognizer(this);       
		sr.setRecognitionListener(new listener());   

		Intent SpeechSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
		SpeechSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		SpeechSearchIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
		SpeechSearchIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3); 

		sr.startListening(SpeechSearchIntent); 	
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}
}