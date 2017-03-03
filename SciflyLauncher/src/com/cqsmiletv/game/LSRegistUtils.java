package com.cqsmiletv.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.eostek.tv.launcher.HomeActivity;
import com.eostek.tv.launcher.util.LConstants;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class LSRegistUtils {

	private String TAG = "lsgame";

	private HomeActivity mActivity;

	private Handler mHandler;

	private String mBaseURL = "http://ptls.gitv.tv/OTTAppCenterZH/api/quickUserUncheckMachinecode.action";

	private String mMachineCode;

	private String mLoginName;

	private String mPassword;

	private String reUsrId;

	private String reLoginName;

	private String rePwd;

	private String reCpld;

	private String reFlag;

	private JSONObject mJsonParams;

	private JSONObject jsonResult;

	public LSRegistUtils(HomeActivity homeActivity, Handler handler) {
		mActivity = homeActivity;
		mHandler = handler;
		if (!hasRegist()) {
			Log.d(TAG, "regist lesheng game!");
			getPostReqstInfo();
			new Thread(postTask).start();
		}
	}

	/**
	 * whther has registed in LeSheng Game Hall.
	 * 
	 * @return
	 */
	private boolean hasRegist() {
		SharedPreferences sp = mActivity.getSharedPreferences("lsGame", 0);
		if (sp.getAll().size() != 0) {

			return true;
		}

		return false;
	}

	/**
	 * get requst info for registing.
	 */
	private void getPostReqstInfo() {
		mMachineCode = "16170820160316000001";
		mLoginName = getTimeStamp();
		mPassword = "eostek027";
		try {
			mJsonParams = new JSONObject();
			mJsonParams.put("machineCode", mMachineCode);
			mJsonParams.put("loginName", mLoginName);
			mJsonParams.put("pwd", mPassword);
			Log.d(TAG, "jsonParams.toString() =" + mJsonParams.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "JSONException =" + e);
			e.printStackTrace();
		}
	}

	/**
	 * conn 子线程
	 */
	Runnable postTask = new Runnable() {

		@Override
		public void run() {
			try {
				StringEntity s = new StringEntity(mJsonParams.toString());
				s.setContentEncoding("UTF-8");
				s.setContentType("application/json");
				// URL使用基本URL即可，其中不需要加参数
				HttpPost httpPost = new HttpPost(mBaseURL);
				// 将请求体内容加入请求中
				httpPost.setEntity(s);
				// 需要客户端对象来发送请求
				HttpClient httpClient = new DefaultHttpClient();
				// 发送请求
				HttpResponse response = httpClient.execute(httpPost);

				getResponseResult(response);

			} catch (Exception e) {
				Log.d(TAG, "postTask Exception =" + e);
				Log.d(TAG, "regist again");
				mHandler.removeMessages(LConstants.LSGAME_REGIST);
				mHandler.sendEmptyMessageDelayed(LConstants.LSGAME_REGIST, 5000);
			}
		}

	};

	/**
	 * get result from httpResponse.
	 * 
	 * @param response
	 */
	private void getResponseResult(HttpResponse response) {
		if (null == response) {
			Log.d(TAG, "null == response");
			return;
		}

		HttpEntity httpEntity = response.getEntity();
		try {
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			/** change base64 format to String **/
			String decodeDataString = new String(android.util.Base64.decode(
					result, android.util.Base64.DEFAULT));
			Log.d(TAG, "decodeDataString =" + decodeDataString);
			ParseJsonResult(decodeDataString);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "getResponseResult Exception =" + e);
			Log.d(TAG, "regist again");
			mHandler.removeMessages(LConstants.LSGAME_REGIST);
			mHandler.sendEmptyMessageDelayed(LConstants.LSGAME_REGIST, 5000);
		}

	}

	/**
	 * 解析得到的Json
	 */
	private void ParseJsonResult(String jsonStr) {
		Log.d(TAG, "ParseJsonResult");
		JSONObject jsonObject = null;

		try {
			/** 把json字符串转换成json对象 **/
			jsonObject = getJSON(jsonStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 取出数据
		try {
			reUsrId = jsonObject.getString("userId");
			reLoginName = jsonObject.getString("loginName");
			rePwd = jsonObject.getString("pwd");
			reCpld = jsonObject.getString("cpId");
			reFlag = jsonObject.getString("flag");
			Log.d(TAG, "reUsrId = " + reUsrId);
			Log.d(TAG, "reLoginName = " + reLoginName);
			Log.d(TAG, "rePwd = " + rePwd);
			Log.d(TAG, "reCpld = " + reCpld);
			Log.d(TAG, "reFlag = " + reFlag);
			if (reUsrId == null) {
				Log.d(TAG, "reUsrId == null,regist again");
				mHandler.removeMessages(LConstants.LSGAME_REGIST);
				mHandler.sendEmptyMessageDelayed(LConstants.LSGAME_REGIST, 5000);
			} else {
				savetoSP(reUsrId, reLoginName, rePwd, reCpld, reFlag);
			}
		} catch (Exception e) {
			Log.d(TAG, "ParseJsonResult Exception ：" + e);
			Log.d(TAG, "regist again");
			mHandler.removeMessages(LConstants.LSGAME_REGIST);
			mHandler.sendEmptyMessageDelayed(LConstants.LSGAME_REGIST, 5000);
		}

	}

	/**
	 * save post result to sharepreference.
	 * 
	 * @param reUsrIdStr
	 * @param reLoginNameStr
	 * @param rePwdStr
	 * @param reCpldStr
	 * @param reFlagStr
	 */
	private void savetoSP(String reUsrIdStr, String reLoginNameStr,
			String rePwdStr, String reCpldStr, String reFlagStr) {
		SharedPreferences sp = mActivity.getSharedPreferences("lsGame", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("userId", reUsrIdStr);
		editor.putString("loginName", reLoginNameStr);
		editor.putString("pwd", rePwdStr);
		editor.putString("cpId", reCpldStr);
		editor.putString("flag", reFlagStr);
		editor.commit();
	}

	/**
	 * change String to jsonObject.
	 * 
	 * @param sb
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJSON(String sb) throws JSONException {

		return new JSONObject(sb);
	}

	/**
	 * get timeStamp,use timeStamp as loginName.
	 * 
	 * @return time string like "MMDDhhmm" format.
	 */
	private String getTimeStamp() {
		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date data = new Date(time);
		String timeStamp = format.format(data);
		String timeStampStr = timeStamp.substring(4, 12);

		return timeStampStr;
	}

}
