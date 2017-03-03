package com.cqsmiletv.game;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.cqsmiletv.game.RemoteOneUpJoyGameAccount.Stub;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View.OnCreateContextMenuListener;

public class LSRegistInfoGetService extends Service {

	private String TAG = "LSRegistInfoGetService";

	private MsgBinder msgBinder = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return msgBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "1");
		Log.d(TAG, "LSRegistInfoGetService onCreate");
		msgBinder = new MsgBinder();
	}

	public class MsgBinder extends Stub {

		@Override
		public OneUpJoyGameAccount getOneUpJoyGameAccount()
				throws RemoteException {
			Log.d(TAG, "LSRegistInfoGetService getOneUpJoyGameAccount");
			SharedPreferences sp = getSharedPreferences("lsGame", 0);

			OneUpJoyGameAccount oneUpJoyGameAccount = new OneUpJoyGameAccount();
			SharedPreferences.Editor editor = sp.edit();
			String usrIdStr = sp.getString("userId", "");
			String loginNameStr = sp.getString("loginName", "");
			String pwdStr = sp.getString("pwd", "");
			String cpldStr = sp.getString("cpId", "");
			String flagStr = sp.getString("flag", "");
			Log.d(TAG, "usrIdStr =" + usrIdStr);
			Log.d(TAG, "loginNameStr =" + loginNameStr);
			Log.d(TAG, "pwdStr =" + pwdStr);
			Log.d(TAG, "cpldStr =" + cpldStr);
			Log.d(TAG, "flagStr =" + flagStr);
			oneUpJoyGameAccount.setUserId(usrIdStr);
			oneUpJoyGameAccount.setLoginName(loginNameStr);
			oneUpJoyGameAccount.setPwd(pwdStr);
			oneUpJoyGameAccount.setCpId(cpldStr);
			oneUpJoyGameAccount.setFlag(flagStr);

			return oneUpJoyGameAccount;

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
