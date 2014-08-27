package com.example.eagle_road;

import android.os.Bundle;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;

//OnItemClickListener �������̽� ���
public class six_one extends Activity implements OnItemClickListener {

	// ����Ʈ�信 ������ ���ڿ� �迭�� �Ҵ�
	String[] age = new String[] { "10��", "20��", "30��", "40��", "50��" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		// ���ҽ� ���Ͽ� ���ǵ� id_list ��� ID �� ����Ʈ�並 ��´�.
		ListView list = (ListView) findViewById(R.id.listView1);

		// ArrayAdapter ��ü�� �����Ѵ�.
		ArrayAdapter<String> adapter;

		// ����Ʈ�䰡 ������ ������ ���ҽ��� ���ڿ� ������ �����Ѵ�
		// ��ü�� = new ArrayAdapter<��������>(�����Ҹ޼ҵ�, �����������۸��ҽ�, ���������ڿ�);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, age);

		/*
		 * 
		 * �ȵ���̵忡�� �����ϴ� �⺻���� ����Ʈ�� ������ ���ҽ�
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
		a.setText("���õ� �� : " + tv.getText() + "\n���õ� id��: " + arg2);

	}
}