package com.example.eagle_road;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.app.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.json.*;

public class twoActivity extends TabActivity {	

	//json형식 임의data
	String str = "{\"coupon\": [{\"title\":\"남성의류\", \"index\":\"0\"},{\"title\":\"여성의류\", \"index\":\"1\"},{\"title\":\"아동의류\",\"index\":\"2\"},{\"title\":\"패션잡화\", \"index\":\"3\"},{\"title\":\"화장품/미용\", \"index\":\"3\"},{\"title\":\"디지털/가전\", \"index\":\"3\"},{\"title\":\"가구/인테리어\", \"index\":\"3\"},{\"title\":\"출산/육아\", \"index\":\"3\"}]}";

	List<String> coupon_list = new ArrayList<String>();
	String coupon_array[] = {};
	String coupon_array2[] = {};
	
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Custom_List_Adapter adapter;		
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        try{
        	String result="";
	        JSONObject obj = new JSONObject(str);        
	    	
	    	JSONArray couponArray = obj.getJSONArray("coupon");
	    	for(int i=0; i<couponArray.length(); i++){
	    		JSONObject order = couponArray.getJSONObject(i);
	    		result += order.getString("title") + order.getString("index");	  		
	    		coupon_list.add(order.getString("title"));			//"title"의 내용 coupon_list에 저장
	    		coupon_array = coupon_list.toArray(new String[coupon_list.size()]); //coupon_list값 array에 저장
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        coupon_array2  = new String[coupon_array.length];       
        for(int i=0; i<coupon_array.length ; i++){
        	coupon_array2[i] = "     "+coupon_array[i];
			
		}
        
        		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystore); 	
         
		TextView tv = (TextView) findViewById(R.id.text);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
 
        HorizontalScrollView sView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        //sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);
 
        TabHost mTabHost = getTabHost();
 
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(" 모든 카테고리 ").setContent(R.id.view1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(" 선택한 카테고리 ").setContent(R.id.view2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(" 미선택한 카테고리 ").setContent(R.id.view3)); 
         
        
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
		//list.setOnItemClickListener(this);

    }   
    
}





