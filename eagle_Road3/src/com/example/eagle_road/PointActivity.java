package com.example.eagle_road;

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

	//String[] list_point = new String[] { "2014/8/20   회원가입 100포인트 적립", "2014/8/20   쇼핑몰 입장 50포인트 적립","2014/8/21   쇼핑몰 입장 50포인트 적립","2014/8/22   쇼핑몰 입장 50포인트 적립"};
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
	    		point_list.add(order.getString("title"));			//"title"의 내용 coupon_list에 저장
	    		array_point = point_list.toArray(new String[point_list.size()]); //coupon_list값 array에 저장
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        //list앞에 공백 추가
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

	    		array_mypoint = mypoint_list.toArray(new String[mypoint_list.size()]); //coupon_list값 array에 저장
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

		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array_point2);

		// 리스트뷰에 ArrayAdapter 객체를 설정하여 리스트뷰에 데이터와 출력 형태를 지정한다.
		list.setAdapter(adapter);

		// 리스트뷰 선택 시 이벤트를 설정한다.
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