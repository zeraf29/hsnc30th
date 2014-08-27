package hsnc.eaglero.main;

import hsnc.eaglero.gcm.GCMConstants;
import hsnc.eaglero.gcm.HTTPSendClass;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.app.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.json.*;


public class oneActivity extends TabActivity implements OnItemClickListener {	

	//json형식 임의data
	String str = "{\"coupon\": [{\"title\":\"***나이키 덩크 20%할인\", \"index\":\"0\"},{\"title\":\"질바이질 스튜어트 원피스\", \"index\":\"1\"},{\"title\":\"아가타 로고 귀걸이 15%할인\",\"index\":\"2\"},{\"title\":\"나이키 루나 40%할인\", \"index\":\"3\"}]}";

	List<String> coupon_list = new ArrayList<String>();
	String coupon_array[] = {};
	String coupon_array2[] = {};
	
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Custom_List_Adapter adapter;		
	private String resultJson;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	try {
			new HttpAsyncTask().execute(GCMConstants.API_URL+"couponList/").get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//resultJson;
    	
        try{
        	//String result="";
	        JSONObject obj = new JSONObject(resultJson);        
	    	
	    	JSONArray couponArray = obj.getJSONArray("coupon");
	    	for(int i=0; i<couponArray.length(); i++){
	    		JSONObject order = couponArray.getJSONObject(i);
	    		//result += order.getString("title") + order.getString("index");	  		
	    		coupon_list.add(order.getString("title"));			//"title"의 내용 coupon_list에 저장
	    		coupon_array = coupon_list.toArray(new String[coupon_list.size()]); //coupon_list값 array에 저장
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        //list앞에 공백 추가
        coupon_array2  = new String[coupon_array.length];       
        for(int i=0; i<coupon_array.length ; i++){
        	coupon_array2[i] = "     "+coupon_array[i];
			
		}

        //layout 연결		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycoupon_page); 	
         
		TextView tv = (TextView) findViewById(R.id.text);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
		

		
 
        HorizontalScrollView sView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        //sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);
 
        TabHost mTabHost = getTabHost();
 
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(" 이용 가능한 쿠폰 ").setContent(R.id.view1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(" 이용한 쿠폰 ").setContent(R.id.view2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(" 장바구니 ").setContent(R.id.view3)); 
         
        
		// 리소스 파일에 정의된 id_list 라는 ID 의 리스트뷰를 얻는다.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1, coupon_array2  );

		// 리스트뷰에 ArrayAdapter 객체를 설정하여 리스트뷰에 데이터와 출력 형태를 지정한다.
		list.setAdapter(adapter);

		// 리스트뷰 선택 시 이벤트를 설정한다.
		list.setOnItemClickListener(this);

    }   
    
	// 리스트뷰 선택 시 이벤트 처리 메소드
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// arg1는 현재 리스트에 뿌려지고 있는 정보
		// arg2는 현재 리스트에 뿌려지고 있는 해당 id 값

		// 값 출력을 위해 불러온 도구를 id값을 통해 불러옴
		TextView a = (TextView) findViewById(R.id.textView1);

		// 현재 리스트뷰에 있는 해당 값을 보기
		TextView tv = (TextView) arg1;

		// 현재 리스트뷰에 나오는 문자열과 해당 라인의 id값을 확인
		//a.setText("선택된 값 : " + tv.getText() + "\n선택된 id값: " + arg2);
		
		Intent intent = new Intent(oneActivity.this, CouponActivity.class);
		intent.putExtra("title",tv.getText());
		startActivity(intent);
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







