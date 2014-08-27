package hsnc.eaglero.main;



import hsnc.eaglero.gcm.GCMConstants;
import hsnc.eaglero.gcm.GCMSampleActivity;
import hsnc.eaglero.gcm.GCMinfoClass;
import hsnc.eaglero.gcm.HTTPSendClass;
import hsnc.eaglero.main.R;
import hsnc.eaglero.main.R.id;
import hsnc.eaglero.main.R.layout;

import com.google.android.gcm.GCMRegistrar;

import android.app.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.*;


public class MainActivity extends Activity{
	
	private GCMinfoClass gcminfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		startService(new Intent(this, BeaconService.class));
		
		GCMSampleActivity gcmsa = new GCMSampleActivity();
		gcmsa.registGCM(this);
		
		TextView tv = (TextView) findViewById(R.id.text);
		TextView tv1 = (TextView) findViewById(R.id.my_coupon);
		TextView tv2 = (TextView) findViewById(R.id.my_store);
		TextView tv3 = (TextView) findViewById(R.id.maps);
		TextView tv4 = (TextView) findViewById(R.id.news);
		TextView tv5 = (TextView) findViewById(R.id.point);
		TextView tv6 = (TextView) findViewById(R.id.setting);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
		tv1.setTypeface(typeface);
		tv2.setTypeface(typeface);
		tv3.setTypeface(typeface);
		tv4.setTypeface(typeface);
		tv5.setTypeface(typeface);
		tv6.setTypeface(typeface);
		
		
		
		
		ImageButton ib_1 = (ImageButton) findViewById(R.id.one);
		ImageButton ib_2 = (ImageButton) findViewById(R.id.two);
		ImageButton ib_5 = (ImageButton) findViewById(R.id.five);
		ImageButton ib_6 = (ImageButton) findViewById(R.id.six);

		ib_1.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, oneActivity.class);
				startActivity(intent);

			}
		});
		
		ib_2.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, twoActivity.class);
				startActivity(intent);

			}
		});
		
		ib_5.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, fiveActivity.class);
				startActivity(intent);

			}
		});
		
		ib_6.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, sixActivity.class);
				startActivity(intent);

			}
		});
		//n          registGCM();
		//GCMSampleActivity.registGCM();
		//new GCMSampleActivity();
	}
	/*
	private void registGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		String regId = GCMRegistrar.getRegistrationId(this);
		
		Log.e("regid",regId);
		System.out.println("!!"+regId);
		System.out.println(GCMConstants.GCMRegUrl);
		gcminfo = new GCMinfoClass();
		Log.e("test","test");
		if("".equals(regId)){  
			GCMRegistrar.register(this, GCMConstants.SEND_ID);
			regId = GCMRegistrar.getRegistrationId(this);
			Log.e("test22222","test");
		    gcminfo.setReg_id(regId);
		    gcminfo.setS_key(GCMConstants.SEND_ID);
		    //HTTPSendClass.HttpPostData(jsonObject2, GCMConstants.GCMRegUrl);
		    new HttpAsyncTask().execute(GCMConstants.GCMRegUrl);
		}else
			Log.d("==============", regId);
		
			gcminfo.setReg_id(regId);
		    gcminfo.setS_key(GCMConstants.SEND_ID);
		    Log.e("test1111","test");
		    //HTTPSendClass.HttpPostData(jsonObject2, GCMConstants.GCMRegUrl);
		    new HttpAsyncTask().execute(GCMConstants.GCMRegUrl);
		
		 //send to server
		 
		
   }
	*/
	/*
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	String str = gcminfo.get_jsonObject().toString();
            return HTTPSendClass.HttpPostData(str, urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
       }
    }
    */
}



//ImageButton ib_2 = (ImageButton) findView(R.id.two);
//ImageButton ib_3 = (ImageButton) findView(R.id.three);
//ImageButton ib_4 = (ImageButton) findView(R.id.four);
//ImageButton ib_5 = (ImageButton) findView(R.id.five);