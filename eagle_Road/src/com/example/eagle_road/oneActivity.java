package com.example.eagle_road;

import java.util.ArrayList;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.app.*;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.os.*;

public class oneActivity extends TabActivity implements OnItemClickListener {
	
	String[] age = new String[] { "1.   ������ ���� 20% ����", "2.   ���������������� ���� ������", "3.   ������ ���� ������ 15% ����", "4.   ������ ���� 40% ����"}; // ���������� ������ ������ ������ ����
	
	
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Custom_List_Adapter adapter;	
	
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycoupon_page);
        
		TextView tv = (TextView) findViewById(R.id.text);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
 
        HorizontalScrollView sView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        //sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);
 
        TabHost mTabHost = getTabHost();
 
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("  WISH LIST  ").setContent(R.id.view1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("AVAILABLE COUPON").setContent(R.id.view2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(" USED COUPON ").setContent(R.id.view3));
      
        
        /*
        Array_Data = new ArrayList<Custom_List_Data>();
        data = new Custom_List_Data(R.drawable.icon_number, "���� ����",
                "���� 5,000km");
        Array_Data.add(data);
        data = new Custom_List_Data(R.drawable.icon_number2, "������ ���� ����",
                "���� 20,000km");
        Array_Data.add(data);
        data = new Custom_List_Data(R.drawable.icon_number3, "���� ����",
                "���� 30,000km");
        ListView custom_list = (ListView) findViewById(R.id.listView1);
        adapter = new Custom_List_Adapter(this,
                android.R.layout.simple_list_item_1, Array_Data);
        custom_list.setAdapter(adapter);*/
        
        
        
        
        
        
        
        
        
        
		// ������ ������ ������ id_list ���� ID �� ���������� ������.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter ������ ��������.
		ArrayAdapter<String> adapter;

		// ���������� ������ ������ �������� ������ ������ ��������
		// ������ = new ArrayAdapter<��������>(������������, ������������������, ������������);
		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1, age  );

		// ���������� ArrayAdapter ������ �������� ���������� �������� ���� ������ ��������.
		list.setAdapter(adapter);

		// �������� ���� �� �������� ��������.
		list.setOnItemClickListener(this);
    }   
    
	// �������� ���� �� ������ ���� ������
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// arg1�� ���� �������� �������� ���� ����
		// arg2�� ���� �������� �������� ���� ���� id ��

		// �� ������ ���� ������ ������ id���� ���� ������
		TextView a = (TextView) findViewById(R.id.textView1);

		// ���� ���������� ���� ���� ���� ����
		TextView tv = (TextView) arg1;

		// ���� ���������� ������ �������� ���� ������ id���� ����
		a.setText("������ �� : " + tv.getText() + "\n������ id��: " + arg2);
		
		Intent intent = new Intent(oneActivity.this, CouponActivity.class);
		startActivity(intent);

	}
 
}