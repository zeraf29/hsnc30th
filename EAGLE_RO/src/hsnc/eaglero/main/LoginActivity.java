package hsnc.eaglero.main;


import hsnc.eaglero.main.R;
import hsnc.eaglero.main.R.id;
import hsnc.eaglero.main.R.layout;
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
	Toast.makeText(LoginActivity.this, "??????????",Toast.LENGTH_SHORT).show();
	
	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	startActivity(intent);
	
	///???????? ????????


				break;
			case R.id.bt_reg:
				Toast.makeText(LoginActivity.this, "????????????",Toast.LENGTH_SHORT).show();
				
	/// ???????? ????			
				break;
			}
		}
	};
}
