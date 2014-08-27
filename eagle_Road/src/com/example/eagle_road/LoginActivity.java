package com.example.eagle_road;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);    
 
    	findViewById(R.id.bt_login).setOnClickListener(mClickListener);
		findViewById(R.id.bt_reg).setOnClickListener(mClickListener);

    }

	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_login:
	Toast.makeText(LoginActivity.this, "�α��ι�ư",Toast.LENGTH_SHORT).show();
	
	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	startActivity(intent);
	
	///����ٰ� ���뾲��


				break;
			case R.id.bt_reg:
				Toast.makeText(LoginActivity.this, "ȸ�����Թ�ư",Toast.LENGTH_SHORT).show();
				
	/// ����ٰ� ����			
				break;
			}
		}
	};
}
