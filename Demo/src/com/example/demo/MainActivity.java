
package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    
    private WebView mWebView;
    
    private TextView mTextView;
    
    private Button mButton;
    
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Log.v("tag","text : " + msg.obj + "len : " + msg.obj.toString().length());
                    mTextView.setText((String) msg.obj);
                    break;
                case 1:
                    Log.v("tag","text : " + msg.obj + "len : " + msg.obj.toString().length());
                    mTextView.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textview);
        
        mButton = (Button) findViewById(R.id.button);
        /*mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://www.baidu.com");*/

        mButton.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("Accept-Charset", "UTF-8;");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    if(connection.getResponseCode() != 200){
                        Log.v("rick","respondCode : " + connection.getResponseCode());
                        switch (connection.getResponseCode()) {
                            case 302:
                                String location = connection.getHeaderField("Location");
                                Log.v("rick","reLocation : " + connection.getHeaderField("Location"));
                                connection = (HttpURLConnection) new URL(location).openConnection();
                                connection.setRequestMethod("GET");
                                connection.addRequestProperty("Accept-Charset", "UTF-8;");
                                connection.setConnectTimeout(10000);
                                connection.setReadTimeout(10000);
                                Log.v("rick","respondCode : " + connection.getResponseCode());
                                break;

                            default:
                                break;
                        }
                    }
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder responseSb = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        responseSb.append(line);
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = responseSb.toString();
                    mHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                }
                
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId() == R.id.button){
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://www.baidu.com");
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        if(httpResponse.getStatusLine().getStatusCode() == 200){
                            HttpEntity entity = httpResponse.getEntity();
                            String response = EntityUtils.toString(entity, "utf-8");
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = response;
                            mHandler.sendMessage(msg);
                        }
                        
                    } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
            }).start();
        }
    }
}
