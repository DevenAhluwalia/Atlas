package com.ahluwalia.atlas;

import java.lang.ref.WeakReference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;


public class Game extends Thread {

	private final long SUBMIT_TIMEOUT = 30*1000;
	private static long countrySubmitBeginTime = 0, timeSpent = 0;
	private static boolean isRunning = true;
	private static boolean submitCountry = false;
	private Handler parentHandler;
	private final GameHandler gameHandler = new GameHandler(this);

	public Game(Handler parentHandler) {
		this.parentHandler = parentHandler;
		Log.e("game","in game");
	}
	public Handler getHandler() {
		return gameHandler;
	}
	@Override
	public void run() {
		long timeDiff;
		Message message = Message.obtain();
		Bundle bundle = new Bundle();
		try {
			while(isRunning) {
				if (submitCountry) {
					timeDiff = System.currentTimeMillis() - countrySubmitBeginTime;
					if (timeDiff > SUBMIT_TIMEOUT-timeSpent) {
						if(GameScreen.num_of_faults > 3) {
							Log.e("game", "game over");
							bundle.clear();
							bundle.putString("status","GAME_OVER");
							message.setData(bundle);
							parentHandler.sendMessage(message);                        
							isRunning = false;
						} else {
							Log.e("game", "times up, faults : "+GameScreen.num_of_faults);
							bundle.clear();
							bundle.putString("status","TIMES_UP");
							message.setData(bundle);
							parentHandler.sendMessage(message);                        
							isRunning = false;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.e("game", "Thread Loop Exception: " + e);
		}
	}

	static class GameHandler extends Handler {

		private final WeakReference<Game> mGame;

		public GameHandler(Game game) {
			mGame = new WeakReference<Game>(game);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mGame.get() != null) {
				Bundle bundle = msg.getData();
				if (bundle.containsKey("status")) {
					String status = bundle.getString("status"); 
					//num_of_faults = bundle.getInt("num_of_faults");
					if(status.equals("START_SUBMIT_COUNTRY")){
						isRunning = true;
						submitCountry = true;
						if (bundle.containsKey("offset")) {
							timeSpent = (long) bundle.getFloat("offset"); 
						}            	
						Log.e("game","timeSpent : "+timeSpent/1000);
						countrySubmitBeginTime = System.currentTimeMillis();
						Log.e("game","START_SUBMIT_COUNTRY");
					} else if(status.equals("FINISH_SUBMIT_COUNTRY")){
						isRunning = false;
						submitCountry = false;
						Log.e("game","FINISH_SUBMIT_COUNTRY");
					}
				}
			}
		}
	}
}

class MainHandler extends Handler {
	@Override
	public void handleMessage(Message msg) {      
		if (msg != null) {
			Bundle bundle = msg.getData();
			if (bundle != null && bundle.containsKey("status")) {
				String status = bundle.getString("status");
				if(status.equals("TIMES_UP")){
					GameScreen.num_of_faults++;
					Log.e("game","TIMES_UP : "+GameScreen.num_of_faults);
					GameScreen.foneTurn(null);
				} else if(status.equals("GAME_OVER")){
					Log.e("game","game over");
					GameScreen.gameRestartDialog();
				}
			}
		}
	}
}