package com.example.wo_oauth_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeInfoActivity extends Activity {
private EditText et_client_id,et_client_secret,et_client_url,et_client_option;
private Button btn_ok;
private Context context;
private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_info);
		context=this;
		sp= getSharedPreferences("woOauth", Context.MODE_PRIVATE);
		btn_ok=(Button)findViewById(R.id.btn_ok);
		et_client_id=(EditText)findViewById(R.id.et_client_id);
		et_client_secret=(EditText)findViewById(R.id.et_client_secret);
		et_client_url=(EditText)findViewById(R.id.et_client_url);
		et_client_option=(EditText)findViewById(R.id.et_client_option);
		
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String client_id= et_client_id.getText().toString().trim();
				String client_secret= et_client_secret.getText().toString().trim();
				String client_url= et_client_url.getText().toString().trim();
				String client_option= et_client_option.getText().toString().trim();
				if("".equals(client_option)){
					Toast.makeText(context, "option不能为空", 1).show();
					return;
				}
				if(!"code".equals(client_option)&&!"token".equals(client_option)){
					Toast.makeText(context, "option请填写:code或者token", 1).show();
					return;
				}
				if("code".equals(client_option)){
					if("".equals(client_id)||"".equals(client_secret)||"".equals(client_url)){
						Toast.makeText(context, "必需参数不能为空", 1).show();
						return ;
					}
				      
				      Editor editor = sp.edit();//获取编辑器
				      editor.putString("client_id", client_id);
				      editor.putString("client_secret", client_secret);
				      editor.putString("client_redirect", client_url);
				      editor.putString("client_option", client_option);
				      editor.commit();//提交修改
				      
				      finish();
				}else if("token".equals(client_option)){
					if("".equals(client_id)||"".equals(client_url)){
						Toast.makeText(context, "必需参数不能为空", 1).show();
						return ;
					}
				
				      
				      Editor editor = sp.edit();//获取编辑器
				      editor.putString("client_id", client_id);
				      editor.putString("client_redirect", client_url);
				      editor.putString("client_option", client_option);
				      editor.commit();//提交修改
				      finish();
				      
				    
				}
				 
			}
		});
	}
	
}
