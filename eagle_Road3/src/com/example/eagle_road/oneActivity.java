package com.example.eagle_road;

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

	//json���� ����data
	String str = "{\"coupon\": [{\"title\":\"***����Ű ��ũ 20%����\", \"index\":\"0\"},{\"title\":\"�������� ��Ʃ��Ʈ ���ǽ�\", \"index\":\"1\"},{\"title\":\"�ư�Ÿ �ΰ� �Ͱ��� 15%����\",\"index\":\"2\"},{\"title\":\"����Ű �糪 40%����\", \"index\":\"3\"}]}";

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
	    		coupon_list.add(order.getString("title"));			//"title"�� ���� coupon_list�� ����
	    		coupon_array = coupon_list.toArray(new String[coupon_list.size()]); //coupon_list�� array�� ����
	    	}	
         }catch (JSONException e) {
            e.printStackTrace();
        }
        
        //list�տ� ���� �߰�
        coupon_array2  = new String[coupon_array.length];       
        for(int i=0; i<coupon_array.length ; i++){
        	coupon_array2[i] = "     "+coupon_array[i];
			
		}

        //layout ����		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycoupon_page); 	
         
		TextView tv = (TextView) findViewById(R.id.text);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
		

		
 
        HorizontalScrollView sView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        //sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);
 
        TabHost mTabHost = getTabHost();
 
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(" �̿� ������ ���� ").setContent(R.id.view1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(" �̿��� ���� ").setContent(R.id.view2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(" ��ٱ��� ").setContent(R.id.view3)); 
         
        
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
		list.setOnItemClickListener(this);

    }   
    
	// ����Ʈ�� ���� �� �̺�Ʈ ó�� �޼ҵ�
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// arg1�� ���� ����Ʈ�� �ѷ����� �ִ� ����
		// arg2�� ���� ����Ʈ�� �ѷ����� �ִ� �ش� id ��

		// �� ����� ���� �ҷ��� ������ id���� ���� �ҷ���
		TextView a = (TextView) findViewById(R.id.textView1);

		// ���� ����Ʈ�信 �ִ� �ش� ���� ����
		TextView tv = (TextView) arg1;

		// ���� ����Ʈ�信 ������ ���ڿ��� �ش� ������ id���� Ȯ��
		//a.setText("���õ� �� : " + tv.getText() + "\n���õ� id��: " + arg2);
		
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





