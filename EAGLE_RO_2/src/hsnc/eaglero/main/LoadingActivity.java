package hsnc.eaglero.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
         
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
 
            public void run() {
                finish();       // 3 ���� �̹����� �ݾƹ���
            }
        }, 2000);    
    }
}
