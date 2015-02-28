package com.ahluwalia.atlas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@SuppressLint("NewApi")
public class GameScreen extends Activity implements TextToSpeech.OnInitListener {

	private static SpeechRecognizer sr;
	private String TAG = "mySpeechRecognizer";
	private static ImageView iv1, iv2, ivAlpha;
	static AssetManager am;
	static TextToSpeech tts;
	private static Multimap<String, String> countryAlphabets = ArrayListMultimap.create();
	private static HashMap<String,Integer> countriesDone = new HashMap<String,Integer>(),
			alphabets_256 = new HashMap<String, Integer>();
	static HashMap<String,Bitmap> listBitmaps = new HashMap<String,Bitmap>(), 
			 listBitmaps256 = new HashMap<String,Bitmap>(); 
	private static List<String> countriesLastChar;
	private static Intent SpeechIntent;
	static String alphabets[] ={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}, foneCountry;
	private static String [] countries;
	static final String TEST_DEVICE_ID_xolo = "E1E0C462889E44F5DAA2742AE3F0A147", TEST_DEVICE_ID_htc = "2DCD6018C18788813385A385CAC2F97B";
	private static Context GameScreenThis = null;
	private AdView mAdView;
	private static AutoCompleteTextView userInput;
	static boolean inputSubmitted = false;
	static int num_of_faults = 1;
	static Button inputSubmit,pass;
	static TextView timer;
	long startTime;
	Game game;
	private MainHandler mainHandler;
	static CountDownTimer countDownTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		GameScreenThis = this;

		//mAdView = (AdView) findViewById(R.id.banner_view_adMob);

		AdRequest adRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice(TEST_DEVICE_ID_xolo)
		.addTestDevice(TEST_DEVICE_ID_htc)
		.build();
		//mAdView.loadAd(adRequest);

		countries = flagDB.countries;

		for(int i=0;i<countries.length;i++){     	       	
			countryAlphabets.put(""+countries[i].charAt(0),countries[i]);
		}
		for(int i=0;i<alphabets.length;i++){
			alphabets_256.put(alphabets[i],getResources().getIdentifier("letter_"+alphabets[i], "drawable", getPackageName()));
		}

		//startActivity(new Intent(this,FlagScreen.class));

		tts = new TextToSpeech(this, this);
		//speakOut("just checking, namaskar, welcome to Atlas by Ahluwalia");

		iv1 = (ImageView)findViewById(R.id.imageView1);    
		iv2 = (ImageView)findViewById(R.id.imageView2);
		ivAlpha = (ImageView)findViewById(R.id.imageViewAlphabet);
		userInput = (AutoCompleteTextView)findViewById(R.id.UserInput);
		inputSubmit = (Button)findViewById(R.id.inputSubmit);
		pass = (Button)findViewById(R.id.inputSubmit);
		timer = (TextView)findViewById(R.id.timer);

		ivAlpha.setImageResource(getResources().getIdentifier("letter_a", "drawable", getPackageName()));
		iv1.setImageResource(getResources().getIdentifier("flags_256_india", "drawable", getPackageName()));
		iv2.setImageResource(getResources().getIdentifier("flags_256_india", "drawable", getPackageName()));      

		userInput.setThreshold(1);		
		userInput.addTextChangedListener(new TextWatcher() {		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if(s.length()>0){
					List<String> countriesDoneFromFirstChar = (List<String>)countryAlphabets.get(""+s.charAt(0)), searchResultsFromChar = new ArrayList<String>(); 
					for(int i=0;i<countriesDoneFromFirstChar.size();i++){
						if(countriesDone.containsKey(countriesDoneFromFirstChar.get(i))){
							searchResultsFromChar.add(countriesDoneFromFirstChar.get(i));
						}
					}
					userInput.setAdapter(new ArrayAdapter<String>(GameScreen.this, android.R.layout.select_dialog_item,searchResultsFromChar));
				}
			}		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}		
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
				
		initialiseGame();

		inputSubmit.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				try{
					Game();
				}catch(NullPointerException e){Log.e("gs","NullPointerException"+e);}
				catch(ArrayIndexOutOfBoundsException e){Log.e("gs","ArrayIndexOutOfBoundsException"+e);}
				catch(StringIndexOutOfBoundsException e){Log.e("gs","StringIndexOutOfBoundsException"+e);}
			}
		});
		
		pass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("status","TIMES_UP");
				bundle.putFloat("offset", System.currentTimeMillis()-startTime);
				Message message = Message.obtain();
				message.setData(bundle);
				mainHandler.sendMessage(message);
			}
		});

		//			}
		//		});
		//        startRecognising();
		//        sr.startListening(SpeechIntent); 	

		//        handler = new Handler();
		//        runnable = new Runnable() {
		//	    	   @Override
		//	    	   public void run() {
		//	    		   //Toast.makeText(getApplicationContext(), "listening :)", 1000).show();
		//	    		   //sr.startListening(SpeechIntent); 		    		  
		//	    		   //handler.postDelayed(this, 15000);
		//	    		   
		//	    	   }
		//	    	}; 
		//	    	
		//	    handler.postDelayed(runnable,10000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_action_bar, menu);
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
		case R.id.feedback:
			new FeedbackDialog().inflateFeedbackDialog(this);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	} 

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mAdView != null) {
			mAdView.resume(); 
		}
		//        sr.startListening(SpeechIntent); 
		//        handler.postDelayed(runnable,1000);
	}

	@Override
	protected void onPause()
	{
		stopSR_tts();
		if (mAdView != null) {
			mAdView.pause(); 
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		stopSR_tts();
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		stopSR_tts();
		if (mAdView != null) {
			mAdView.destroy();
		}
		num_of_faults = 1;
		super.onDestroy();
	}

	public void stopSR_tts(){
		//handler.removeCallbacks(runnable);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		if(sr != null){
			sr.stopListening();
		}
	}

	class listener implements RecognitionListener          
	{
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
			String [] speechAtlasResultList = null;

			if(x != null) {
				speechAtlasResultList = x.toArray (new String[x.size ()]);

				if(countriesDone.containsKey(speechAtlasResultList[0])){ 
					speakOut("this country has already been played upon, please speak again");
					//        				sr.startListening(SpeechIntent);
					//        				Wait();
					//handler.postDelayed(runnable,1000);
				} else {
					//                		  flag_name = speechAtlasResultList[0];
					//                    	  speakOut(flag_name);
					//                    	  flag_name = flag_name.toLowerCase().replace(" ", "_");               	 
				}
			} else {
				speakOut("not recognized, please speak again");
			}
			//Game();
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

	public static Collection<String> getBestMatch(List<String> listKeys){

		String key = "";
		String [] countries = flagDB.countries;
		Multimap<Integer, String> map = ArrayListMultimap.create();
		int minWordPieceDistance = 100, minBestCountryMatch = 100, minForDifferentKeys = 100;
		levenshtein ls = new levenshtein();

		for(int j = 0;j<listKeys.size();j++){	
			key = listKeys.get(j);
			minBestCountryMatch = 100;

			String wordPiece = "";
			for (int i = 0; i < countries.length; i++) {
				String[] wordPieces = countries[i].replace("_", " ").split("\\s+");
				minWordPieceDistance = 100;

				for(int x = 0; x < wordPieces.length; x++){	
					wordPiece = wordPieces[x];
					if(wordPiece.equals("and") || wordPiece.equals("of")){
						continue;
					}
					ls.calc_distance(wordPiece, key);
					minWordPieceDistance = minWordPieceDistance < ls.getLevenshteinDistance() ? minWordPieceDistance : ls.getLevenshteinDistance();
				}					
				if(!map.containsEntry(minWordPieceDistance, countries[i])){
					map.put(minWordPieceDistance, countries[i]);      
				}
				minBestCountryMatch = minBestCountryMatch < minWordPieceDistance ? minBestCountryMatch : minWordPieceDistance;
			}
			minForDifferentKeys = minForDifferentKeys < minBestCountryMatch ? minForDifferentKeys : minBestCountryMatch;
		}	
		return minForDifferentKeys > 2 ? null : map.get(minForDifferentKeys);
	}

	public void Game(){
		char lastChar = '#';
		String userCountry = "";
		
		List<String> listKeys = new ArrayList<String>();
		listKeys.add(userInput.getText().toString());
		Collection<String> x = GameScreen.getBestMatch(listKeys);
		
		if(x != null) {
			userCountry = x.toArray (new String[x.size ()])[0];
			//if(System.currentTimeMillis() - startTime < 30*1000){
				if(num_of_faults < 3){
					if(userCountry != null && !userCountry.equals("")){
						if(!countriesDone.containsKey(userCountry)){
							if(lastChar == '#' || userCountry.charAt(0) == lastChar){
								finishSubmit();
								
								speakOut("user : "+userCountry);
								setFlag(iv1,MenuScreen.imageIds_256,userCountry);
								setFlag(ivAlpha,alphabets_256,""+userCountry.toUpperCase().charAt(userCountry.length()-1));
								Log.e("game","user's turn : "+userCountry);
								
								lastChar = updateList(userCountry);
								foneTurn(userCountry);
								
								Log.e("game","restarting timer");
								setTimer(30);
								startTime = System.currentTimeMillis();
								startSubmit();
							} else {
								num_of_faults++;
								startSubmit();
								Log.e("game","u have to speak from letter : "+lastChar+" - "+num_of_faults);
							}
						} else {
							//num_of_faults++;
							startSubmit();
							Log.e("game","already exists : "+num_of_faults);
						}
					} else {
						num_of_faults++;
						startSubmit();
						Log.e("game","null pointer : "+num_of_faults);
					}
				} else {
					userInput.setClickable(false);
					inputSubmit.setClickable(false);
					timer.setText("game over");
					Log.e("game","game over");
				}
//			} else {
//				userInput.setClickable(false);
//				inputSubmit.setClickable(false);
//				timer.setText("timesUP");
//				Log.e("game","timesUP");
//			}
		} else {
			num_of_faults++;
			startSubmit();
			Log.e("game","no match found : "+num_of_faults);
		}
	}

	public static char foneTurn(String userCountry){
		int size = 0;
		char lastChar;
		String newChar = "";

		if(userCountry == null){					
			userCountry = foneCountry; Log.e("game","using null");
		} else {
			lastChar = userCountry.charAt(userCountry.length()-1); 
			countriesLastChar = (List<String>) countryAlphabets.get(""+lastChar);
			size = countriesLastChar.size();
		}
		if(size == 0){
			while(true){
				newChar = alphabets[new Random().nextInt(26)]; //make it 26
				if(countryAlphabets.containsKey(newChar)) {
					//speakOut("newChar : "+newChar);
					countriesLastChar = (List<String>) countryAlphabets.get(""+newChar);
					size = countriesLastChar.size(); 
					foneCountry = countriesLastChar.get(new Random().nextInt(size)); 
					break;
				}
			} 
		} else {
			foneCountry = countriesLastChar.get(new Random().nextInt(size)); 
		}    	
		lastChar = updateList(foneCountry);
		Log.e("game","foneCountry : "+foneCountry);
		
		speakOut("fone : "+foneCountry);
		setFlag(iv2,MenuScreen.imageIds_256,foneCountry);
		setFlag(ivAlpha,alphabets_256,""+foneCountry.toUpperCase().charAt(foneCountry.length()-1));
		
		return lastChar;
	}

	public void initialiseGame(){
		mainHandler = new MainHandler();
		game = new Game(mainHandler);	
		final Message message = Message.obtain();

		Bundle bundle = new Bundle();
		bundle.putString("status","START_SUBMIT_COUNTRY");
		message.setData(bundle);
		game.start();
		game.getHandler().sendMessage(message);	
		setTimer(30);

		startTime = System.currentTimeMillis();
	}

	public void startSubmit(){
		Log.e("game","india");
		Bundle bundle = new Bundle();
		bundle.putString("status","START_SUBMIT_COUNTRY");
		Message message = Message.obtain();
		message.setData(bundle);
		game.getHandler().sendMessage(message);
	}

	public void finishSubmit(){
		Bundle bundle = new Bundle();
		bundle.putString("status","FINISH_SUBMIT_COUNTRY");
		bundle.putFloat("offset", System.currentTimeMillis()-startTime);
		Message message = Message.obtain();
		message.setData(bundle);
		game.getHandler().sendMessage(message);
	}

	public static char updateList(String country){
		if(!countriesDone.containsKey(country)){
			countriesDone.put(country,1);
		}
		if(countryAlphabets.containsValue(country)){
			countryAlphabets.remove(""+country.charAt(0), country); 
		}
		return country.charAt(country.length()-1);
	}
	
	public void setTimer(int time){
		countDownTimer = new CountDownTimer(time*1000, 1000) {       
			public void onTick(long millisUntilFinished) {          
				timer.setText("seconds remaining: " + millisUntilFinished / 1000);      
			}       
			public void onFinish() {         
				timer.setText("done!");    
			}   
		};
		countDownTimer.start();
	}

	//static void Game(){
	//	
	//	char lastChar = 0;
	//	int num_of_faults = 0, max_tries = 3, size = 0;
	//	List<String> countriesLastChar = null;
	//	Boolean firstTurn = true;
	//	
	//	while(true){
	//		if(!firstTurn && !countryAlphabets.containsKey(""+lastChar)){
	//			speakOut("as there is no country left from the letter ,"+lastChar+", U can choose any random country");
	//		}	
	//		firstTurn = false;
	//		getTimedInput();
	//		//getInput();
	//		//sr.startListening(SpeechIntent);	    	
	//	    //handler.postDelayed(runnable,1000);
	//		//flag_name = userInput.getText().toString();
	//		//Wait();
	//		if(!countriesDone.containsKey(flag_name)){
	//			countriesDone.put(flag_name,1);
	//		}				
	//		speakOut("user : "+flag_name);	
	//		setFlag(iv1,MenuScreen.imageIds_256,flag_name);
	//		setFlag(ivAlpha,alphabets_256,""+flag_name.toUpperCase().charAt(flag_name.length()-1));
	//		
	//		while(true){		
	//			if(lastChar != 0 && countryAlphabets.containsKey(""+lastChar) && flag_name.charAt(0) != lastChar){
	//				num_of_faults++;
	//				speakOut("fault number : "+ num_of_faults +"\nu have to speak a country starting with the letter : "+lastChar);
	//				getTimedInput();
	//				//getInput();
	//				//sr.startListening(SpeechIntent);	
	//				//Wait();				
	//				//handler.postDelayed(runnable,1000);
	//				//flag_name = userInput.getText().toString();
	//			} else {
	//				if(!countriesDone.containsKey(flag_name)){
	//					countriesDone.put(flag_name,1);
	//				}
	//				lastChar = flag_name.charAt(flag_name.length()-1);
	//				break;
	//			}
	//			if(num_of_faults == max_tries-1){
	//				speakOut("max faults reached");
	//				break;
	//			}
	//		}		
	//		if(num_of_faults == max_tries-1){
	//			speakOut("Game over");
	//			return;
	//		}
	//		if(countryAlphabets.containsValue(flag_name)){
	//			countryAlphabets.remove(""+flag_name.charAt(0), flag_name); 
	//		}
	//		
	//    	if(countryAlphabets.isEmpty()){
	//    		speakOut("No countries left");
	//    		break;
	//    	}
	//	}
	//}

	//public static void getTimedInput(){
	//	final Thread thread=  new Thread(){
	//        @Override
	//        public void run(){
	//        	Log.e("gs","Started");
	//            try {
	//                synchronized(this){
	//                    wait(10000);Log.e("gs","in waitin");
	//                }
	//            }
	//            catch(InterruptedException ex){                    
	//            }
	//            Log.e("gs","times up");
	//        }
	//    };
	//    thread.start();
	//    
	//   inputSubmit.setOnClickListener(new OnClickListener() {			
	//		@Override
	//		public void onClick(View v) {
	//			synchronized(thread){
	//	            thread.notifyAll();
	//			}
	//			Toast.makeText(GameScreen.GameScreenThis, "Enter", Toast.LENGTH_SHORT).show();
	//			List<String> listKeys = new ArrayList<String>();
	//			listKeys.add(userInput.getText().toString());
	//			//inputSubmitted = false;
	//			 Collection<String> x = GameScreen.getBestMatch(listKeys);
	//		     String [] speechAtlasResultList = null;
	//		     
	//		     if(x != null) {
	//		   	  speechAtlasResultList = x.toArray (new String[x.size ()]);
	//		   	  
	//		   	  if(countriesDone.containsKey(speechAtlasResultList[0])){ 
	//					speakOut("this country has already been played upon, please speak again");
	//					Log.e("gs","this country has already been played upon, please speak again");
	//					//Wait(5);
	//					//getInput();
	//					//sr.startListening(SpeechIntent);
	//					//handler.postDelayed(runnable,1000);
	//					inputSubmit.setClickable(false);
	//					inputSubmit.setOnClickListener(null);
	//					getTimedInput();
	//		   	  } else {
	//		   		  flag_name = speechAtlasResultList[0];
	//		       	  speakOut(flag_name);
	//		       	  flag_name = flag_name.toLowerCase().replace(" ", "_");               	 
	//		   	  }
	//		     } else {
	//		   	  speakOut("not recognized, please speak again");
	//		   	 Log.e("gs","not recognized, please speak again");
	//		   	  //Wait(5);
	//		   	  //getInput();
	//		   	inputSubmit.setClickable(false);
	//			inputSubmit.setOnClickListener(null);
	//		   	getTimedInput();
	//		     }
	//		}
	//	});  
	//}

	public static void setFlag(ImageView ivX, HashMap<String,Integer> imageIds_x,String image){
		try {
			ivX.setImageResource(imageIds_x.get(image)); 
		} catch (NullPointerException e) {}
	}

	public void startRecognising() {
		// TODO Auto-generated method stub
		sr = SpeechRecognizer.createSpeechRecognizer(this);       
		sr.setRecognitionListener(new listener());   

		SpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
		SpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		SpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
		SpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3); 
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.getDefault());

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	private static void speakOut(String text_to_speak) {
		tts.speak(text_to_speak, TextToSpeech.QUEUE_ADD, null);
	}

	
	public static void gameRestartDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameScreenThis);

		alertDialogBuilder.setTitle("Game over");
		alertDialogBuilder
		.setMessage("Want to restart ?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//					restart();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}

class levenshtein {

	int min = 100,bLength = 0;
	int [] costs;

	public int calc_distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		bLength = b.length();
		// i == 0
				costs = new int [b.length() + 1];
				for (int j = 0; j < costs.length; j++)
					costs[j] = j;
				for (int i = 1; i <= a.length(); i++) {
					// j == 0; nw = lev(i - 1, j)
					costs[0] = i;
					int nw = i - 1;
					for (int j = 1; j <= b.length(); j++) {
						int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
						nw = costs[j];
						costs[j] = cj;
					}
				}
				return costs[bLength];
	}
	public int getMin(){
		return min;
	}
	public int getLevenshteinDistance(){
		return costs[bLength];
	}
}