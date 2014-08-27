package hsnc.eaglero.gcm;


import hsnc.eaglero.main.MainActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GCMSampleActivity extends Activity {
	
	private GCMinfoClass gcminfo;
    /*
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        registGCM();
    }
    */
    
    public void registGCM(MainActivity mainActivity) {
		GCMRegistrar.checkDevice(mainActivity);
		GCMRegistrar.checkManifest(mainActivity);
		
		String regId = GCMRegistrar.getRegistrationId(mainActivity);
		
	
		System.out.println(GCMConstants.GCMRegUrl);
		gcminfo = new GCMinfoClass();
		//Log.e("test","test");
		System.out.println(regId+"test3");
		if("".equals(regId)){  
			GCMRegistrar.register(mainActivity, GCMConstants.SEND_ID);
			regId = GCMRegistrar.getRegistrationId(mainActivity);
			System.out.println(regId+"test");
		    gcminfo.setReg_id(regId);
		    gcminfo.setS_key(GCMConstants.SEND_ID);
		    //HTTPSendClass.HttpPostData(jsonObject2, GCMConstants.GCMRegUrl);
		    new HttpAsyncTask().execute(GCMConstants.GCMRegUrl);
		}else{
			Log.d("==============", regId);
			System.out.println(regId+"test2");
			gcminfo.setReg_id(regId);
			gcminfo.setS_key(GCMConstants.SEND_ID);
		    
		    //HTTPSendClass.HttpPostData(jsonObject2, GCMConstants.GCMRegUrl);
		    new HttpAsyncTask().execute(GCMConstants.GCMRegUrl);
		/*
		 * send to server
		 */
		}
    }
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
}
