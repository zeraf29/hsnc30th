package hsnc.eaglero.main;

import hsnc.eaglero.gcm.GCMConstants;
import hsnc.eaglero.gcm.HTTPSendClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PointActivity extends MainActivity {

	//String[] list_point = new String[] { "2014/8/20   ȸ���� 100����Ʈ ��", "2014/8/20   ���θ� ���� 50����Ʈ ��","2014/8/21   ���θ� ���� 50����Ʈ ��","2014/8/22   ���θ� ���� 50����Ʈ ��"};
	//String[] list_point2 = new String[list_point.length];

	List<String> point_list = new ArrayList<String>();
	String array_point[] = {};
	String array_point2[] = {};

	List<String> mypoint_list = new ArrayList<String>();
	String array_mypoint[] = {};
	//String array_mypoint2[] = {};
	
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Custom_List_Adapter adapter;		
	private String resultJson;
	private String resultJson2;
	String point,name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);
		
    	try {
			new HttpAsyncTask().execute(GCMConstants.API_URL+"pointList/").get();
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	String temp1 = resultJson;
    	
    	
    	///////////////////////////////////////////////////////////////////////////
    	try {
			new HttpAsyncTask().execute(GCMConstants.API_URL+"myPoint/").get();
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//resultJson;
    	String temp2 = resultJson;
    	/////////////////////////////////////////////////////////////////////////////////////
        try{
        	//String result="";
	        JSONObject obj = new JSONObject(temp1);        
	    	
	    	JSONArray couponArray = obj.getJSONArray("point");
	    	for(int i=0; i<couponArray.length(); i++){
	    		JSONObject order = couponArray.getJSONObject(i);
	    		//result += order.getString("title") + order.getString("index");	  		
	    		point_list.add(order.getString("title"));			//"title"�� ���� coupon_list�� ����
	    		array_point = point_list.toArray(new String[point_list.size()]); //coupon_list�� array�� ����
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        //list�տ� ��� �߰�
        array_point2  = new String[array_point.length];       
        for(int i=0; i<array_point.length ; i++){
        	array_point2[i] = "     "+array_point[i];
			
		}	
		////////////////////////////////////////////////////////////////////////////////////
        try{
        	//String result="";
	        JSONObject obj = new JSONObject(temp2);        
	    	
	    	JSONArray couponArray = obj.getJSONArray("point");
	    	for(int i=0; i<couponArray.length(); i++){
	    		JSONObject order = couponArray.getJSONObject(i);

	    		array_mypoint = mypoint_list.toArray(new String[mypoint_list.size()]); //coupon_list�� array�� ����
	    		point = order.getString("point");
	    		name = order.getString("name");
	    	
	    	
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        String finalPoint = name + "님의 포인트는 \""+point +"\"점 입니다.";
        
		TextView tv_point = (TextView) findViewById(R.id.textview_point);
		tv_point.setText(finalPoint);
		
        ///////////////////////////////////////////////////////////////////////////////////
        
       
        
        
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter ��ü�� ���Ѵ�.
		ArrayAdapter<String> adapter;

		// ����Ʈ�䰡 ������ ������ ���ҽ��� ���ڿ� ������ �����Ѵ�
		// ��ü�� = new ArrayAdapter<��������>(�����Ҹ޼ҵ�, ����������۸��ҽ�, �������ڿ�);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array_point2);

		// ����Ʈ�信 ArrayAdapter ��ü�� �����Ͽ� ����Ʈ�信 �����Ϳ� ��� ���¸� �����Ѵ�.
		list.setAdapter(adapter);

		// ����Ʈ�� ���� �� �̺�Ʈ�� �����Ѵ�.
		//list.setOnItemClickListener(this);
	}
	
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
         //String str = gcminfo.get_jsonObject().toString();
        	
        	resultJson = HTTPSendClass.HttpPostData("", urls[0]);
            return resultJson;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        	super.onPostExecute(result);
       }
        @Override
        protected void onPreExecute(){
        	super.onPreExecute();
        }
    }
}