package com.example.eagle_road;

import android.app.*;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.*;


public class MainActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		TextView tv = (TextView) findViewById(R.id.text);
		TextView tv1 = (TextView) findViewById(R.id.my_coupon);
		TextView tv2 = (TextView) findViewById(R.id.my_store);
		TextView tv3 = (TextView) findViewById(R.id.maps);
		TextView tv4 = (TextView) findViewById(R.id.news);
		TextView tv5 = (TextView) findViewById(R.id.point);
		TextView tv6 = (TextView) findViewById(R.id.setting);
		Typeface typeface = Typeface.createFromAsset(getAssets(),"SohoGothicPro-Light.otf");
		tv.setTypeface(typeface);
		tv1.setTypeface(typeface);
		tv2.setTypeface(typeface);
		tv3.setTypeface(typeface);
		tv4.setTypeface(typeface);
		tv5.setTypeface(typeface);
		tv6.setTypeface(typeface);
		
		
		
		
		ImageButton ib_1 = (ImageButton) findViewById(R.id.one);
		ImageButton ib_2 = (ImageButton) findViewById(R.id.two);
		ImageButton ib_5 = (ImageButton) findViewById(R.id.five);
		ImageButton ib_6 = (ImageButton) findViewById(R.id.six);

		ib_1.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, oneActivity.class);
				startActivity(intent);

			}
		});
		
		ib_2.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, twoActivity.class);
				startActivity(intent);

			}
		});
		
		ib_5.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, fiveActivity.class);
				startActivity(intent);

			}
		});
		
		ib_6.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, sixActivity.class);
				startActivity(intent);

			}
		});
	}
}



//ImageButton ib_2 = (ImageButton) findView(R.id.two);
//ImageButton ib_3 = (ImageButton) findView(R.id.three);
//ImageButton ib_4 = (ImageButton) findView(R.id.four);
//ImageButton ib_5 = (ImageButton) findView(R.id.five);