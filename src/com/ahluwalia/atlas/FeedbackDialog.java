package com.ahluwalia.atlas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.common.base.FinalizablePhantomReference;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

class FeedbackDialog {
	Button dialogButton = null;
	boolean canDismissNow = false;

	public void postData(String radioOption, String input) throws UnsupportedEncodingException {

		final String fullUrl = "https://docs.google.com/forms/d/1GiYq9Z3v4_SnvDuBgIw-8p0HHkTriaVe2H47r67hwK4/formResponse";

		final String data = "entry_1912511009=" + URLEncoder.encode(radioOption,"UTF-8") + "&" +
				"entry_1193609570=" + URLEncoder.encode(input,"UTF-8");

		new AsyncTask<Void, Void, Void>() {	 
			@Override
			protected Void doInBackground(Void... params) {
				new HttpRequest().sendPost(fullUrl, data);
				return null;
			} 	 
			@Override
			protected void onPostExecute(Void x) {
				dialogButton.setText("Success, click to dismiss");
				canDismissNow = true;
			} 	 
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	void inflateFeedbackDialog(final Context context){

		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.feedback_dialog);
		dialog.setTitle("Bug/Feedback...");

		final EditText feedback_input = (EditText) dialog.findViewById(R.id.feedback_text);
		final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.feedback_radio);

		dialogButton = (Button) dialog.findViewById(R.id.feedback_dialogButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//dialog.dismiss();
				if(!canDismissNow){	
					if(MenuScreen.isNetworkAvailable(context)){				
						try {
							if(feedback_input.getText().toString().equals("")){
								dialogButton.setText("Please enter text");
							} else{
								RadioButton radioButton = (RadioButton) dialog.findViewById(radioGroup.getCheckedRadioButtonId());
								postData(radioButton.getText().toString(),feedback_input.getText().toString());	
								dialogButton.setText("Processing, please wait");
							}	
						} catch (UnsupportedEncodingException e) {
						}
					}else {
						MenuScreen.showDialogBox(context);
						dialogButton.setText("Click to connect to internet");
					}
				}else {
					dialog.dismiss();
				}
			} 
		});
		dialog.show();
	}

	static void inflateDemoToggleDialog(final Context context, final Boolean isShowNow){

		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.demo_toggle_dialog);
		dialog.setTitle("Toggle Demo");

		final TextView demoText = (TextView) dialog.findViewById(R.id.demo_text);
		final ToggleButton demoToggleButton = (ToggleButton) dialog.findViewById(R.id.demo_toggle_button);
		Button dismissButton = (Button) dialog.findViewById(R.id.demo_dismiss_button); 
		
		SharedPreferences prefs = context.getSharedPreferences("atlas_preferences", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();

		if(prefs.getBoolean("showDemo", false)){
			demoText.setText("Demo turned on");	   
			demoToggleButton.setChecked(true);
		} else {
			demoText.setText("Demo turned off");	   
			demoToggleButton.setChecked(false);
		}
		demoToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					demoText.setText("Demo turned on");	           
					editor.putBoolean("showDemo",true).commit();
					if(isShowNow){
						dialog.dismiss();
						inflateDemoFlagPagerDialog(context);
					}
				} else {
					demoText.setText("Demo turned off");	           
					editor.putBoolean("showDemo",false).commit();
				}
			}
		});
		dismissButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	static void inflateDemoFlagPagerDialog(final Context context){

		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.demo_flag_pager_screen);
		dialog.setTitle("Swipe through the flags");

		final Button demoButton = (Button) dialog.findViewById(R.id.demo_button);

		demoButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				SharedPreferences.Editor editor = context.getSharedPreferences("atlas_preferences", Context.MODE_PRIVATE).edit();
				editor.putBoolean("firstTime",false).commit();
				editor.putBoolean("showDemo",false).commit();
			}
		});
		dialog.show();
	}
}

class HttpRequest{

	DefaultHttpClient httpClient;
	HttpContext localContext;
	private String ret;

	HttpResponse response = null;
	HttpPost httpPost = null;
	HttpGet httpGet = null;

	public HttpRequest(){
		HttpParams myParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
		httpClient = new DefaultHttpClient(myParams);
		localContext = new BasicHttpContext();
	}

	public void clearCookies() {
		httpClient.getCookieStore().clear();
	}

	public void abort() {
		try {
			if (httpClient != null) {
				System.out.println("Abort.");
				httpPost.abort();
			}
		} catch (Exception e) {
			System.out.println("Your App Name Here" + e);
		}
	}

	public String sendPost(String url, String data) {
		return sendPost(url, data, null);
	}

	public String sendJSONPost(String url, JSONObject data) {
		return sendPost(url, data.toString(), "application/json");
	}

	public String sendPost(String url, String data, String contentType) {
		ret = null;

		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

		httpPost = new HttpPost(url);
		response = null;

		StringEntity tmp = null;

		Log.d("Your App Name Here", "Setting httpPost headers");

		httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
		httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*;q=0.5");

		if (contentType != null) {
			httpPost.setHeader("Content-Type", contentType);
		} else {
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		}

		try {
			tmp = new StringEntity(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("Your App Name Here", "HttpUtils : UnsupportedEncodingException : "+e);
		}

		httpPost.setEntity(tmp);

		Log.d("Your App Name Here", url + "?" + data);

		try {
			response = httpClient.execute(httpPost,localContext);

			if (response != null) {
				ret = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e("Your App Name Here", "HttpUtils: " + e);
		}

		Log.d("Your App Name Here", "Returning value:" + ret);

		return ret;
	}

	public String sendGet(String url) {
		httpGet = new HttpGet(url);

		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			Log.e("Your App Name Here", e.getMessage());
		}

		//int status = response.getStatusLine().getStatusCode();

		// we assume that the response body contains the error message
		try {
			ret = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			Log.e("Your App Name Here", e.getMessage());
		}

		return ret;
	}

	public InputStream getHttpStream(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception e) {
			throw new IOException("Error connecting");
		} // end try-catch

		return in;
	}
}