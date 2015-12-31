package com.example.wo_oauth_demo;

import org.json.JSONObject;

import com.unicom.oauth.http.Method;
import com.unicom.oauth.service.WogarOAuth;
import com.unicom.oauth.util.Constants;
import com.unicom.oauth.util.Utils;
import com.unicom.oauth.util.WoOauthCallbackListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements WoOauthCallbackListener,
		OnClickListener {
	private Button btn_start_manual, btn_user_info, btn_start_automatic,
			btn_clear_cache, btn_change;
	private TextView tv_show;
	private WoOauthCallbackListener listener;

	 private String CLIENT_ID = "";
	 // 用户申请的client_secret
	 private String CLIENT_SECRET = "";
	 // 重定向url
	 public String REDIRECT_URI = "";
	 public String OPTION = "";




	// // 测试环境 等级0
//	 private String CLIENT_ID = "FINDME";
//	 // 用户申请的client_secret
//	 private String CLIENT_SECRET = "FINDME";
//	 // 重定向url
//	 public  String REDIRECT_URI = "http://www.anywhere";

	// //测试环境 等级1
//	 private String CLIENT_ID = "unisk";
//	 // 用户申请的client_secret
//	 private String CLIENT_SECRET = "UNISK";
//	 // 重定向url
//	 public static final String REDIRECT_URI = "http://www.unisk.cn";

	// // 测试环境 等级2
//	 private String CLIENT_ID = "0E6E5A4747B5BF9E";
//	 // 用户申请的client_secret
//	 private String CLIENT_SECRET = "0E6E5A4747B5BF9E";
//	 // 重定向url
//	 public String REDIRECT_URI = "http://www.anywhere";
//	 
//	 
//	 
//	public String OPTION = "token";
	int type = 0;
	private Context context;

	private String openid;
	private String expires_in;
	private String refresh_token;
	private String access_token;
	private SharedPreferences sp;

	private float screenWidth ;
	private float screenHeight ;

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
		btn_change = (Button) findViewById(R.id.btn_change);
		btn_start_manual.setOnClickListener(this);
		btn_user_info.setOnClickListener(this);
		btn_clear_cache.setOnClickListener(this);
		btn_start_automatic.setOnClickListener(this);
		btn_change.setOnClickListener(this);
		screenWidth = (float) (getWindowManager().getDefaultDisplay()
				.getWidth() * 0.8);
		screenHeight=(float) (getWindowManager()
				.getDefaultDisplay().getHeight() * 0.5);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start_automatic:// 验证授权
			if ("".equals(CLIENT_ID) || "".equals(REDIRECT_URI)
					|| "".equals(OPTION)) {
				tv_show.setText("请进行参数配置...");
				return;
			}
			automaticAuthorizeMode(1, OPTION);
			break;
		case R.id.btn_start_manual://等级授权
			if ("".equals(CLIENT_ID) || "".equals(REDIRECT_URI)
					|| "".equals(OPTION)) {
				tv_show.setText("请进行参数配置...");
				return;
			}
		    manualAuthorizationMode(OPTION);
			tv_show.setText("等待响应...");
			break;
		case R.id.btn_user_info:
			getUserInfo();
			tv_show.setText("等待响应...");
			break;
		case R.id.btn_clear_cache:
			clearCache();
			break;
		case R.id.btn_change:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ChangeInfoActivity.class);
			startActivity(intent);
			break;

		}

	}

	/**
	 * 等级授权
	 * @param delaytime 延时调用此方法的时间，单位是秒
	 * @param mode ：用于区分授权码模式与简化模式
	 * 授权码模式：需要传入CLIENT_SECRET参数
	 * 简化模式：不必传入CLIENT_SECRET参数
	 * 注意：不建议使用授权码模式，暴露应用的CLIENT_SECRET存在风险
	 */
	private void automaticAuthorizeMode(int delaytime, String mode) {
		if ("code".equals(mode)) {
			//授权码模式，必须传入CLIENT_SECRET
			WogarOAuth wogarOauth = new WogarOAuth(context, CLIENT_ID,
					CLIENT_SECRET, REDIRECT_URI);
			String state = wogarOauth.automaticAuthorizeMode(this, screenWidth,
					screenHeight, delaytime, R.style.wo_passport_dialog);
			tv_show.setText(state);
		} else if ("token".equals(mode)) {
			//简化模式，不用传入CLIENT_SECRET
			WogarOAuth wogarOauth = new WogarOAuth(context, CLIENT_ID,
					REDIRECT_URI);
			String state = wogarOauth.automaticAuthorizeMode(this, screenWidth,
					screenHeight, delaytime, R.style.wo_passport_dialog);
			tv_show.setText(state);
		}

	}
    /**
     *  验证授权 
     * @param mode 用于区分授权码模式与简化模式
	 * 授权码模式：需要传入CLIENT_SECRET参数
	 * 简化模式：不必传入CLIENT_SECRET参数
	 * 注意：不建议使用授权码模式，暴露应用的CLIENT_SECRET存在风险
	 */
	private void manualAuthorizationMode(String mode) {
		if ("code".equals(mode)) {
			//授权码模式，必须传入CLIENT_SECRET
			WogarOAuth wogarOauth = new WogarOAuth(context, CLIENT_ID,
					CLIENT_SECRET, REDIRECT_URI);
			wogarOauth.manualAuthorizationMode(this, true,
					R.style.Dialog_Fullscreen);
		}else if ("token".equals(mode)) {
			//简化模式，不用传入CLIENT_SECRET
			WogarOAuth wogarOauth = new WogarOAuth(context, CLIENT_ID,
					 REDIRECT_URI);
			wogarOauth.manualAuthorizationMode(this, true,
					R.style.Dialog_Fullscreen);
		}
	}
	
	/**
	 * 获取用户信息
	 */
	protected void getUserInfo() {
		WogarOAuth wogarOauth = new WogarOAuth(context);
		wogarOauth.getUserInfo(this, access_token, openid, CLIENT_ID);
	}
    /**
     * 验证授权走sso的时候，会调用这个方法
     * @param userId
     */
	public void SSOLoginGetToken(String userId) {
		WogarOAuth wogarOauth = new WogarOAuth(context);
		wogarOauth.getSSOToken(this, userId);
	}
	/**
	 * 验证授权走sso的时候，会调用这个方法
	 * @param result
	 */
	private void StartPassPort(JSONObject result) {
		Intent i = new Intent();
		i.putExtra("SSO_Login_Data", result.toString());
		ComponentName cn = new ComponentName("com.unicom.wagarpass",
				"com.unicom.wagarpass.activity.OauthLoginActivity");
		if (cn != null) {
			i.setComponent(cn);
			startActivityForResult(i, 0);
		}
	}
	public void clearCache() {
		tv_show.setText("......");
		openid = "";
		expires_in = "";
		refresh_token = "";
		access_token = "";
	}
	@Override
	public void onWoOauthSuccess(JSONObject result, Method method) {
		if (method == Method.NET_GET_USER_INFO_URL) {
			// 获取用户信息
			tv_show.setText("" + result);
		} else if (method == Method.GET_SSO_INFO) {
			StartPassPort(result);
		} else if (method == Method.GET_SSO_TOKEN_INFO) {
			// sso
			tv_show.setText("" + result);
			processJSON(result);
		} else if (method == Method.MANUAL_MODE_REQUEST) {
			// 验证授权
			processJSON(result);
			tv_show.setText("" + result);
		} else if (method == Method.AUTO_MODE_REQUEST) {
			// 等级授权
			processJSON(result);
			tv_show.setText("" + result);
		}

	}

	@Override
	public void onWoOauthFail(String result, Method method) {
		tv_show.setText(result);
	}

	// 这个方法，开发者可以用来控制蒙层的显示，在这个方法里边，取消蒙层
	@Override
	public void onWoOauthMongoliaLayerFinish() {
		tv_show.setText("......");
	}
    //接收sso回调数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == 1) {
				String userId = data.getStringExtra("userId");
				SSOLoginGetToken(userId);
			} else if (resultCode == 0) {
				String mode = data.getStringExtra("info");
				if ("900001".equals(mode)) {
					Toast.makeText(context, "用户点击取消授权", Toast.LENGTH_SHORT)
							.show();
				} else if ("900002".equals(mode)) {
					Toast.makeText(context, "用户取消登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}

	}

	public void processJSON(JSONObject jsonStr) {
		try {
			if (jsonStr.has("error"))
				return;
			JSONObject object = jsonStr.getJSONObject("data");
			openid = object.optString("openid");
			expires_in = object.optString("expires_in");
			access_token = object.optString("access_token");

		} catch (Exception e) {
			Log.e("testopenauth", "json字符串不合法");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		tv_show.setText("......");
	}

	@Override
	protected void onResume() {
		super.onResume();
		sp = getSharedPreferences("woOauth", Context.MODE_PRIVATE);
		CLIENT_ID = sp.getString("client_id", "");
		CLIENT_SECRET = sp.getString("client_secret", "");
		REDIRECT_URI = sp.getString("client_redirect", "");
		OPTION = sp.getString("client_option", "");
		tv_show.setText("......");
	}
}