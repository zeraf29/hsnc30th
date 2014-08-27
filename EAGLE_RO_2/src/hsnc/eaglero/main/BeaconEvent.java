package hsnc.eaglero.main;

import hsnc.eaglero.gcm.GCMConstants;
import hsnc.eaglero.gcm.GCMEventInfoClass;
import hsnc.eaglero.gcm.GCMinfoClass;
import hsnc.eaglero.gcm.HTTPSendClass;

import java.util.concurrent.ExecutionException;

import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class BeaconEvent {
	private GCMEventInfoClass gEinfo;
	public void nearMessage(int type){
		message(type);
	}
	public void message(int type) {
		//GCMRegistrar.checkDevice(this);
		//GCMRegistrar.checkManifest(this);
		
		//String regId = GCMRegistrar.getRegistrationId(this);
		
	
		//System.out.println(GCMConstants.GCMRegUrl);
		gEinfo = new GCMEventInfoClass();
		//Log.e("test","test");
		//System.out.println(regId+"test3");
		//Log.d("==============", regId);
		//System.out.println(regId+"test2");
		//gEinfo.setReg_id(regId);
		gEinfo.setS_key(GCMConstants.SEND_ID);
		gEinfo.setType(type);
		/*
		 * 1: 쿠폰
		 * 2: 방
		 */
		Log.e("url",GCMConstants.API_URL+"beMessage");
		try {
			new HttpAsyncTask().execute(GCMConstants.API_URL+"beMessage").get();
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	String str = gEinfo.get_jsonObject().toString();
            return HTTPSendClass.HttpPostData(str, urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
       }
    }
}
