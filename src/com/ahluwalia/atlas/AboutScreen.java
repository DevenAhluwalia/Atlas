package com.ahluwalia.atlas;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AboutScreen extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_screen);
		
		Button emailMEButton = (Button)findViewById(R.id.emailMEButton),
			   rateAppButton = (Button)findViewById(R.id.rateAppButton);
		
		rateAppButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				rateApp(AboutScreen.this);
			}
		});
		
		emailMEButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"deven.walia@gmail.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
				i.putExtra(Intent.EXTRA_TEXT   , "body of email");
				if(MenuScreen.isNetworkAvailable(AboutScreen.this)){
					try {
						startActivity(Intent.createChooser(i, "Send mail..."));
					} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					}
				}else {
					MenuScreen.showDialogBox(AboutScreen.this);
				}
			}
		});
	}
	
	static void rateApp(Context context){
		if(MenuScreen.isNetworkAvailable(context)){
			 try{
				Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
				Intent rateAppIntent = new Intent(Intent.ACTION_VIEW,uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
			    
				if (context.getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0){
					context.startActivity(rateAppIntent);
			    } else{
			    	uri = Uri.parse("https://play.google.com/store/apps/details?"+ context.getPackageName());
			    	rateAppIntent = new Intent(Intent.ACTION_VIEW,uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    	context.startActivity(rateAppIntent);
			    } 
			 }catch(ActivityNotFoundException e){			  
				Toast.makeText(context, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
			 }
		} else {
			MenuScreen.showDialogBox(context);
		}
	}
}