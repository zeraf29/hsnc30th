package hsnc.eaglero.main;

import hsnc.eaglero.main.R;
import hsnc.eaglero.main.R.id;
import hsnc.eaglero.main.R.layout;
import android.os.Bundle;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;

//OnItemClickListener 인터페이스 상속
public class six_one extends Activity implements OnItemClickListener {

	// 리스트뷰에 보여질 문자열 배열로 할당
	String[] age = new String[] { "10대", "20대", "30대", "40대", "50대" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		// 리소스 파일에 정의된 id_list 라는 ID 의 리스트뷰를 얻는다.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, age);

		/*
		 * 
		 * 안드로이드에서 제공하는 기본적인 리스트뷰 아이템 리소스
		 * 
		 * simple_list_item_1 
		 * simple_list_item_2 
		 * simple_list_item_activated_1
		 * simple_list_item_activated_2 
		 * simple_list_item_checked
		 * simple_list_item_multiple_choice 
		 * simple_list_item_single_choice
		 * simple_selectable_list_item
		 */

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

	}
}