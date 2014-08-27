package hsnc.eaglero.main;

import hsnc.eaglero.main.R;
import hsnc.eaglero.main.R.id;
import hsnc.eaglero.main.R.layout;

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
	
	String[] age = new String[] { "1.   나이키 덩크 20% 할인", "2.   질바이질스튜어트 패턴 원피스", "3.   아가타 로고 귀걸이 15% 할인", "4.   나이키 루나 40% 할인"}; // 리스트뷰에 보여질 문자열 배열로 할당
	
	
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
        data = new Custom_List_Data(R.drawable.icon_number, "엔진 오일",
                "주기 5,000km");
        Array_Data.add(data);
        data = new Custom_List_Data(R.drawable.icon_number2, "타이어 위치 교환",
                "주기 20,000km");
        Array_Data.add(data);
        data = new Custom_List_Data(R.drawable.icon_number3, "연료 필터",
                "주기 30,000km");
        ListView custom_list = (ListView) findViewById(R.id.listView1);
        adapter = new Custom_List_Adapter(this,
                android.R.layout.simple_list_item_1, Array_Data);
        custom_list.setAdapter(adapter);*/
        
        
        
        
        
        
        
        
        
        
		// 리소스 파일에 정의된 id_list 라는 ID 의 리스트뷰를 얻는다.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1, age  );

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
		a.setText("선택된 값 : " + tv.getText() + "\n선택된 id값: " + arg2);
		
		Intent intent = new Intent(oneActivity.this, CouponActivity.class);
		startActivity(intent);

	}
 
}