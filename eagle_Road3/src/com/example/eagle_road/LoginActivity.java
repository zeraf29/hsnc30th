package com.example.eagle_road;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

    Handler h;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);   
        
        startActivity(new Intent(this,LoadingActivity.class));
        
       // h = new Handler();
       // h.postDelayed(irun, 8000);
    
    /*Runnable irun = new Runnable(){
    	public void run(){
    		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    		startActivity(intent);
    		finish();
    		
    		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    	}
    };*/
        
        
        
 
    	findViewById(R.id.bt_login).setOnClickListener(mClickListener);
		findViewById(R.id.bt_reg).setOnClickListener(mClickListener);
    }
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_login:
	Toast.makeText(LoginActivity.this, "로그인버튼",Toast.LENGTH_SHORT).show();
	
	Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
	startActivity(intent);
	
	///여기다가 내용쓰기


				break;
			case R.id.bt_reg:
				Toast.makeText(LoginActivity.this, "회원가입버튼",Toast.LENGTH_SHORT).show();
				
	/// 여기다가 내용			
				break;
			}
		}
	};
}

