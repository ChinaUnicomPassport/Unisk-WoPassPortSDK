package com.example.wo_oauth_demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.unicom.wowagarpass.service.WagarPass;
import com.unicom.wowagarpass.service.WogarOAuth;
import com.unicom.wowagarpass.util.Constants;
import com.unicom.wowagarpass.util.CryptLib;
import com.unicom.wowagarpass.util.TextUtil;
import com.unicom.wowagarpass.util.WoOauthCallbackListener;
import com.unicom.wowagarpass.view.ShowInfoDialog;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements WoOauthCallbackListener,
		OnClickListener {
	private Button btn_start_manual, btn_user_info, btn_start_automatic,
			btn_clear_cache;
	private TextView tv_show;


//	// 测试环境，安全等级是0
//	private String CLIENT_ID = "FINDME";
//	private String CLIENT_SECRET = "FINDME";
//	public static String 	OAUTH_CALLBACK_URL = "http://www.anywhere";
	
	 private String CLIENT_ID = "b61a4b2bf76c4b3bb02097a18a5764c3";
	 private String CLIENT_SECRET = "accb90eaea6241e2ba835a28700bf4ce";
	 public  String OAUTH_CALLBACK_URL ="http://www.wobendi.com/";
	

	

	int type = 0;
	private Context context;

	private String openid;
	private String expires_in;
	private String refresh_token;
	private String access_token;
	private String code;
	private String token_type;

	private String error;
	private String errorDescription;

	private int type_way = 0;// 请求的方式1.自动模式请求2.手动模式请求

	private List<String> list = new ArrayList<String>();
	private Spinner mySpinner;
	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		context = this;
		btn_start_manual = (Button) findViewById(R.id.btn_start_manual);
		btn_user_info = (Button) findViewById(R.id.btn_user_info);
		btn_start_automatic = (Button) findViewById(R.id.btn_start_automatic);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_show.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_show.setText("......");

		btn_clear_cache = (Button) findViewById(R.id.btn_clear_cache);
		btn_start_manual.setOnClickListener(this);
		btn_user_info.setOnClickListener(this);
		btn_clear_cache.setOnClickListener(this);
		btn_start_automatic.setOnClickListener(this);

		list.add("安全等级：0");
		list.add("安全等级：1");
		list.add("安全等级：2");

		mySpinner = (Spinner) findViewById(R.id.Spinner_city);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(adapter);
		mySpinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						if (adapter.getItem(arg2).equals("安全等级：0")) {
							
//							CLIENT_ID = "FINDME";
//							CLIENT_SECRET = "FINDME";
//							OAUTH_CALLBACK_URL = "http://www.anywhere";
							
							CLIENT_ID = "b61a4b2bf76c4b3bb02097a18a5764c3";
							CLIENT_SECRET = "accb90eaea6241e2ba835a28700bf4ce";
							OAUTH_CALLBACK_URL = "http://www.wobendi.com/";
							
						} else if (adapter.getItem(arg2).equals("安全等级：1")) {
							CLIENT_ID = "unisk";
							CLIENT_SECRET = "UNISK";
							OAUTH_CALLBACK_URL = "http://www.unisk.cn";
						} else if (adapter.getItem(arg2).equals("安全等级：2")) {
							CLIENT_ID = "0E6E5A4747B5BF9E";
							CLIENT_SECRET = "0E6E5A4747B5BF9E";
							OAUTH_CALLBACK_URL = "http://www.anywhere";
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_start_automatic:
			type_way = 1;
			automaticAuthorizeMode(1);
			break;
		case R.id.btn_start_manual:
			type_way = 2;
			tv_show.setText("等待响应...");
			manualAuthorizationMode();
			break;
		case R.id.btn_user_info:
			getUserInfo();
			tv_show.setText("等待响应...");
			break;
		case R.id.btn_clear_cache:
			clearCache();
			break;

		}

	}


	private void manualAuthorizationMode() {
		WogarOAuth wogarOauth = new WogarOAuth(context);
		wogarOauth.setCodeParams(CLIENT_ID, CLIENT_SECRET, OAUTH_CALLBACK_URL);
		wogarOauth.manualAuthorizationMode(this,
				true,R.style.Dialog_Fullscreen);
	}

	protected void getUserInfo() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				WogarOAuth wogarOauth = new WogarOAuth(context);
				String result = wogarOauth.getUserInfo(access_token, openid,
						CLIENT_ID);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				tv_show.setText("..." + result);
			}
		}.execute();
	}

	/**
	 * 延迟多长时间启动自动模式
	 * 
	 * @param delaytime
	 *            单位是秒
	 */
	private void automaticAuthorizeMode(int delaytime) {
		float screenWidth = (float) (getWindowManager().getDefaultDisplay()
				.getWidth() * 0.8);
		float screenHeight = (float) (getWindowManager().getDefaultDisplay()
				.getHeight() * 0.5);
		WogarOAuth wogarOauth = new WogarOAuth(context);
		wogarOauth.setCodeParams(CLIENT_ID, CLIENT_SECRET, OAUTH_CALLBACK_URL);

		String state = wogarOauth.automaticAuthorizeMode(this, screenWidth,
				screenHeight, delaytime, R.style.wo_passport_dialog);
		tv_show.setText(state);// 这里主要是自动模式，返回的状态

	}

	public void processAccessToken(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.has("error"))
				return;
			openid = json.optString("openid");
			expires_in = json.optString("expires_in");
			refresh_token = json.optString("refresh_token");
			access_token = json.optString("access_token");

		} catch (JSONException e) {
			Log.e("testopenauth", "json字符串不合法");
		}
	}

	
	public void clearCache() {
		tv_show.setText("......");
		code = "";
		openid = "";
		expires_in = "";
		refresh_token = "";
		access_token = "";
		error = "";
		errorDescription = "";
	}

	@Override
	protected void onPause() {
		super.onPause();
		tv_show.setText("......");
	}

	@Override
	protected void onResume() {
		super.onResume();
		tv_show.setText("......");
	}

	@Override
	public void onResultCallback(String message, int type) {
		processAccessToken(message);
		tv_show.setText(message);

	}

	// 这个方法，开发者可以用来控制蒙层的显示，在这个方法里边，取消蒙层
	@Override
	public void onMongoliaLayerFinish() {
		tv_show.setText("......");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

}