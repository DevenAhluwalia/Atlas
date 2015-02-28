package com.ahluwalia.atlas;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appflood.AFBannerView;
import com.appflood.AppFlood;

public class FlagPagerScreen extends FragmentActivity implements TextToSpeech.OnInitListener{

	static int NUM_PAGES;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	static TextToSpeech tts;
	static Activity flagPagerScreenThis;
	static String[] array;
	static AFBannerView appFlood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flag_pager);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		SharedPreferences prefs = getSharedPreferences("atlas_preferences", MODE_PRIVATE);
		if(prefs.getBoolean("firstTurn",true) || prefs.getBoolean("showDemo",false)){
			FeedbackDialog.inflateDemoFlagPagerDialog(this);
		}

		flagPagerScreenThis = this;

		Bundle b = this.getIntent().getExtras();
		array = b.getStringArray("countries_for_pager");

		NUM_PAGES = array.length;
		tts = new TextToSpeech(this, this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(FlagScreen.pos);

		PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagerTitleStrip);
		pagerTitleStrip.setTextColor(Color.BLUE);
		pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	} 
	@Override
	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		if (appFlood != null) {
			AppFlood.destroy();
		}
		super.onDestroy();
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
		case R.id.demoToggle:
			FeedbackDialog.inflateDemoToggleDialog(this,true);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	} 

	//    @Override
	//    public void onBackPressed() {
	//        if (mPager.getCurrentItem() == 0) {
	//            super.onBackPressed();
	//        } else {
	//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
	//        }
	//    }
}