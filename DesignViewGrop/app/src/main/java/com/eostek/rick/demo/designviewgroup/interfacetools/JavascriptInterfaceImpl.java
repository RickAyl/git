package com.eostek.rick.demo.designviewgroup.interfacetools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 自定义的Android代码和JavaScript代码之间的桥梁类
 * @author 1
 * 
 */
public class JavascriptInterfaceImpl {

	private final String TAG = JavascriptInterfaceImpl.class.getSimpleName();
	private Context mContext;
	private WebView mWebView;
	private Handler mHandler;

	/** Instantiate the interface and set the context */
	public JavascriptInterfaceImpl(Context c, WebView webView) {
		mContext = c;
		mWebView = webView;
		mHandler = new Handler(Looper.getMainLooper());
	}

	/** Show a toast from the web page */
	// 如果target 大于等于API 17，则需要加上如下注解
	@JavascriptInterface
	public void showToast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
	}

	/**
	 * 同步方法
	 * @return
	 */
	@JavascriptInterface
	public String syncExec() {
		return "hello android";
	}

	/**
	 * 异步方法
	 * @param msg
	 * @param callbackId
	 */
	@JavascriptInterface
	public void asyncExec(final String msg, final String callbackId) {
		Toast.makeText(mContext, "2秒后将跳转到百度页面", Toast.LENGTH_LONG).show();
		new Thread() {
			@Override
			public void run() {
				SystemClock.sleep(2 * 1000);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						String url = "javascript:" + callbackId + "('" + msg
								+ " from android " + "')";
						//interfacecallback.callbackone();
                        mWebView.loadUrl("http://www.baidu.com");
					}
				});
			}
		}.start();
	}

	public static interface DemoCallbackInterface{
		void callbackone();
		void callbacktwo();
	}

	/**
	 * 接收JavaScript的异步消息
	 * @param msg
	 */
	@JavascriptInterface
	public void callBack(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}
}
