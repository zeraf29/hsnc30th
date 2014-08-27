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

	//json���� ����data
	String str = "{\"coupon\": [{\"title\":\"�����Ƿ�\", \"index\":\"0\"},{\"title\":\"�����Ƿ�\", \"index\":\"1\"},{\"title\":\"�Ƶ��Ƿ�\",\"index\":\"2\"},{\"title\":\"�м���ȭ\", \"index\":\"3\"},{\"title\":\"ȭ��ǰ/�̿�\", \"index\":\"3\"},{\"title\":\"������/����\", \"index\":\"3\"},{\"title\":\"����/���׸���\", \"index\":\"3\"},{\"title\":\"���/����\", \"index\":\"3\"}]}";

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
	    		coupon_list.add(order.getString("title"));			//"title"�� ���� coupon_list�� ����
	    		coupon_array = coupon_list.toArray(new String[coupon_list.size()]); //coupon_list�� array�� ����
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
 
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(" ��� ī�װ� ").setContent(R.id.view1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(" ������ ī�װ� ").setContent(R.id.view2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(" �̼����� ī�װ� ").setContent(R.id.view3)); 
         
        
		// ���ҽ� ���Ͽ� ���ǵ� id_list ��� ID �� ����Ʈ�並 ��´�.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter ��ü�� �����Ѵ�.
		ArrayAdapter<String> adapter;

		// ����Ʈ�䰡 ������ ������ ���ҽ��� ���ڿ� ������ �����Ѵ�
		// ��ü�� = new ArrayAdapter<��������>(�����Ҹ޼ҵ�, �����������۸��ҽ�, ���������ڿ�);
		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1, coupon_array2  );

		// ����Ʈ�信 ArrayAdapter ��ü�� �����Ͽ� ����Ʈ�信 �����Ϳ� ��� ���¸� �����Ѵ�.
		list.setAdapter(adapter);

		// ����Ʈ�� ���� �� �̺�Ʈ�� �����Ѵ�.
		//list.setOnItemClickListener(this);

    }   
    
}





