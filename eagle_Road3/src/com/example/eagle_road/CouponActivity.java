package com.example.eagle_road;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.app.*;
import android.content.*;
import android.graphics.Color;


public class CouponActivity extends Activity{

	//json형식 임의data
	String str = "{\"coupon\": [{\"title\":\"***나이키 덩크 20%할인\", \"index\":\"0\", \"address\":\"***서울특별시 강남구 압구정동 494\",\"info\":\"***스포츠레저 | 7F\"}]}";
	List<String> coupon_list  = new ArrayList<String>();
	String coupon_array[]   = {};
	String title,address,info;

	//String postTitle = null;
	CouponInfoClass couponInfoClass;
	String intentTitle;
	private String resultJson;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupon_detail);		
		
		
		
		
		Intent intent = getIntent();
		intentTitle = intent.getStringExtra("title"); //LoginActivity에서 title값 가져오기 => 서버에 보낼 값
		//Toast.makeText(getBaseContext(), title, Toast.LENGTH_LONG).show();
		Log.e("why!!",intentTitle.trim());
		
		JsonObject jsonObject = new JsonObject();
		JsonObject jsonObject2 = new JsonObject();
		// JsonArray jArray = new JsonArray();
		jsonObject.addProperty("title", intentTitle);
		
		jsonObject2.add("data", jsonObject);
		
		
		// System.out.println(jsonObject);
		Log.e("json", jsonObject2.toString());
		intentTitle = jsonObject2.toString();
		
		try {
			
			new HttpAsyncTask().execute(GCMConstants.API_URL+"couponSpecific/").get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try{
        	//String result="";
	        JSONObject obj = new JSONObject(resultJson);        
	    	
	    	JSONArray couponArray = obj.getJSONArray("coupon"); 
	    	for(int i=0; i<couponArray.length(); i++){
	    		JSONObject order = couponArray.getJSONObject(i);
	    		//result += order.getString("title") + order.getString("index");	  		
	    		//coupon_list.add(order.getString("title"));			//"title"의 내용 coupon_list에 저장
	    		//coupon_array  = coupon_list.toArray(new String[coupon_list.size()]); //coupon_list값 array에 저장
	    		title = order.getString("title");
	    		address = order.getString("address");
	    		info = order.getString("info");
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }

		
		
		
		TextView tv_title = (TextView) findViewById(R.id.title);
		TextView tv_address = (TextView) findViewById(R.id.address);
		TextView tv_info = (TextView) findViewById(R.id.info);
		tv_title.setText(title);	//textview에 쿠폰 제목 출력	
		tv_address.setText(address);
		tv_info.setText(info);
		
		
		
		Button ib_1 = (Button) findViewById(R.id.bt_use);
		ib_1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
			
				AlertDialog.Builder alertDlg = new AlertDialog.Builder(CouponActivity.this);
	    		alertDlg.setTitle(R.string.alert_question);
	    		
	    		// 여기서 부터는 알림창의 속성 설정
	    		alertDlg.setTitle("쿠폰 사용 여부")        // 제목 설정
	    		.setMessage("쿠폰을 사용하시겠습니까?")        // 메세지 설정
	    		.setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
	    		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
	    		 // 확인 버튼 클릭시 설정
	    		public void onClick(DialogInterface dialog, int whichButton){
	    			
	    			Button bt = (Button) findViewById(R.id.bt_use);
					bt.setBackgroundColor(Color.GRAY);
					bt.setText("사용완료");	
	    			
	    		}
	    		})
	    		.setNegativeButton("취소", new DialogInterface.OnClickListener(){      
	    		     // 취소 버튼 클릭시 설정
	    		public void onClick(DialogInterface dialog, int whichButton){
	    		dialog.cancel();
	    		}
	    		});

	    		AlertDialog dialog = alertDlg.create();    // 알림창 객체 생성
	    		dialog.show();    // 알림창 띄우기	
			}
		});		
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	
    		
        	String str = intentTitle;
        	resultJson = HTTPSendClass.HttpPostData(str, urls[0]);
            return resultJson;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
       }
    }
}
