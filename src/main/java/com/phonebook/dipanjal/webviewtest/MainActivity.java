package com.phonebook.dipanjal.webviewtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    TextView errorMsg;
    WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.errorMsg=(TextView)findViewById(R.id.errorTextView);
        this.webView=(WebView)findViewById(R.id.webview1);

        /*WebSettings webSettings=this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);*/ //making JS enable

        this.wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        this.webView.addJavascriptInterface(new WebAppClass(this,webView),"Andro");

    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(150);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //

        if(this.isNetworkAvailable())
        {
            webView.setInitialScale(getScale());
            this.webView.loadUrl("file:///android_asset/index.html");
            //this.webView.loadUrl("https://testomania.000webhostapp.com/");
        }
        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            this.webView.loadUrl("file:///android_asset/message.html");
            this.wifiManager.setWifiEnabled(true);
            //this.onResume();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if(activeNetworkInfo!=null)
        {
            //Toast.makeText(this, "Net Connected : "+String.valueOf(activeNetworkInfo.isConnected()), Toast.LENGTH_SHORT).show();
            return activeNetworkInfo.isConnected();
        }
        else
        {
            return false;
        }
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
