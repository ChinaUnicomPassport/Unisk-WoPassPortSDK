package com.example.wo_oauth_demo;

import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.unicom.wowagarpass.service.WagarPass;
import com.unicom.wowagarpass.service.WogarOAuth;
import com.unicom.wowagarpass.ui.WebActivity;
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
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements WoOauthCallbackListener,
OnClickListener {
private Button btn_start_manual, btn_user_info,
	      btn_start_automatic,btn_clear_cache;
private TextView tv_show;
private String CLIENT_ID = "unisk";
private String CLIENT_SECRET = "UNISK";
public static final String OAUTH_CALLBACK_URL = "http://www.unisk.cn";


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
}

@Override
public void onClick(View v) {
WogarOAuth wogarOauth = new WogarOAuth(context);
wogarOauth.setCodeParams(CLIENT_ID, CLIENT_SECRET, OAUTH_CALLBACK_URL);
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
int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); 
int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
wogarOauth.manualAuthorizationMode(this, screenWidth, screenHeight,true);
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
		tv_show.setText("..."+result);
	}
}.execute();
}


/**
* 延迟多长时间启动自动模式
* @param delaytime  单位是秒
*/
private void automaticAuthorizeMode(int delaytime) {
float screenWidth = (float) (getWindowManager().getDefaultDisplay()
		.getWidth() * 0.8);
float screenHeight = (float) (getWindowManager().getDefaultDisplay()
		.getHeight() * 0.5);
WogarOAuth wogarOauth = new WogarOAuth(context);
String state = wogarOauth.automaticAuthorizeMode(this, screenWidth,
		screenHeight, delaytime);
tv_show.setText(state);//这里主要是自动模式，返回的状态
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

public void processAutomatic(String message) {
access_token = message.split("&")[0].split("=")[1];
token_type = message.split("&")[1].split("=")[1];
expires_in = message.split("&")[2].split("=")[1];
openid = message.split("&")[3].split("=")[1];

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
// TODO Auto-generated method stub
super.onPause();
tv_show.setText("......");
}

@Override
protected void onResume() {
// TODO Auto-generated method stub
super.onResume();
tv_show.setText("......");
}








@Override
public void onResultCallback(String message) {
if (type_way == 1) {// 自动
	processAutomatic(message);
} else if (type_way == 2) {
	processAccessToken(message);
}

tv_show.setText("..."+message);

}
//这个方法，开发者可以用来控制蒙层的显示，在这个方法里边，取消蒙层
@Override
public void onMongoliaLayerFinish() {
tv_show.setText("......");
}

}