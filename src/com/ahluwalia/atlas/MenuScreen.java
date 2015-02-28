package com.ahluwalia.atlas;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuScreen extends Activity {

	static HashMap<String, flagObject> mapDB = new HashMap<String, flagObject>();
	
	static HashMap<String, Integer>  
				imageIds_128 = new HashMap<String, Integer>(),
				imageIds_256 = new HashMap<String, Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_screen);
		
		SharedPreferences prefs = getSharedPreferences("atlas_preferences", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		Log.e("atlas1",""+prefs.getBoolean("firstTime", true));
		if(!prefs.getBoolean("firstTime", false)){
			editor.putBoolean("firstTime",true).commit();
		}Log.e("atlas2",""+prefs.getBoolean("firstTime", true));
		
		mapDB = new flagDB().getMapDB();
		String [] countries = flagDB.countries;
		
		for(int i=0;i<countries.length;i++){     	       	
    		imageIds_128.put(countries[i],getResources().getIdentifier("flags_"+countries[i], "drawable", getPackageName())); 
    		imageIds_256.put(countries[i],getResources().getIdentifier("flags_256_"+countries[i], "drawable", getPackageName())); 
    		if(imageIds_128.get(countries[i]) == 0) Log.e("lol","flags_"+countries[i].toLowerCase().replace(" ", "_"));      	
		}
		
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		
		Button gameScreenButton = (Button)findViewById(R.id.gameScreen);
		Button flagScreenButton = (Button)findViewById(R.id.flagScreen);
		
		gameScreenButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),GameScreen.class));				
			}
		});
		
		flagScreenButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),FlagScreen.class));
			}
		});		
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
				AboutScreen.rateApp(MenuScreen.this);
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
	
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			alertDialogBuilder.setTitle("Exit ?");
			alertDialogBuilder
				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						finish();
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
	
	public static void showDialogBox(final Context context){

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		TextView textView = new TextView(context);
//		textView.setText(title);
//		builder.setCustomTitle(textView);
	    builder.setTitle("Please connect to internet");
	    builder.setItems(new CharSequence[] {"Wifi network", "Cellular network","Not Now, dismiss"},
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    
	                    switch (which) {
	                        case 0:
	                        	context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
	                            break;
	                        case 1:
	                        	context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	                            break;
	                        case 2:
	                    }
	                }
	            });
	    builder.create().show();
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    boolean available = false;
	    try {
	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivity != null) {
	            NetworkInfo[] info = connectivity.getAllNetworkInfo();
	            if (info != null) {
	                for (int i = 0; i < info.length; i++) {
	                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                        available = true;
	                    }
	                }
	            }
	        }
	        if (available == false) {
	            NetworkInfo wiMax = connectivity.getNetworkInfo(6);

	            if (wiMax != null && wiMax.isConnected()) {
	                available = true;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return available;
	}
}