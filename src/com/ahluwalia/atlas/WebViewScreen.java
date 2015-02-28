package com.ahluwalia.atlas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewScreen extends Activity {
	
	private String url = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.web_view);
        
        final WebView webView = (WebView)findViewById(R.id.webView1);
        url = getIntent().getExtras().getString("url");
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar); //progressBar.setVisibility(0);
        final ProgressDialog pDialogRouteFetch = new ProgressDialog(this);
		
        pDialogRouteFetch.setMessage("Please wait, while wikipedia is loaded...");
	    pDialogRouteFetch.setIndeterminate(false);
	    //pDialogRouteFetch.setCancelable(false);
	    pDialogRouteFetch.show();
        
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        
        webView.setWebChromeClient(new WebChromeClient() {
        	   public void onProgressChanged(WebView view, int progress) {Log.e("progress",""+progress);
        	   progressBar.setProgress(progress);  
        	   if(progress == 100) {
        	    	 pDialogRouteFetch.dismiss();
        	    	 progressBar.setVisibility(View.GONE);
        	     }
        	   }
        });
        webView.setWebViewClient(new WebViewClient() {
        	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	     Toast.makeText(WebViewScreen.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
        	   }
        });
		webView.loadUrl(url);
	}
}