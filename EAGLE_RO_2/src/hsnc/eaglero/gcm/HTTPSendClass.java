package hsnc.eaglero.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HTTPSendClass {
	public static String HttpPostData(String str,String snedUrl) { 
		
		//String URL = "http://###.###.###.###:8080/TeamNote/JSONServer.jsp";
		
		String result = "";
		
		try {
			
			InputStream inputStream = null;
			//String str = jsonObject.toString();
			StringEntity se = new StringEntity(str,"UTF-8");
			URI url = new URI(snedUrl);
			// 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            //UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(se, HTTP.UTF_8);
            //Log.e("tes6t",str);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            httpPost.getEntity();
            
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            //Log.e("test7","1");
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            //Log.e("test8","1");
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            
            Log.e("result",result);
			
		} catch (Exception e) {
			Log.e("exception",e.toString());
		}
		return result;
	}
	
	public boolean isConnected(){
		Context context = null;
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        	return true;
        else
        	return false;    
    }
	
	

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }   
	 
}
