package com.example.eagle_road;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class sixActivity extends MainActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.six);        
        
        Button btn_1 = (Button)findViewById(R.id.bt_1);   
        Button btn_2 = (Button)findViewById(R.id.bt_2);
        

        /* 
        //��ư1Ŭ���� �̺�Ʈ������     
       btn_1.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_one.class);  
        		startActivity(intent);
        	}
        });      
      
              
      //��ư2Ŭ���� �̺�Ʈ������
        btn_2.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_two.class);  
        		startActivity(intent);
        	}
        });
        
        
      //��ư3Ŭ���� �̺�Ʈ������
        btn_3.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_three.class); 
        		startActivity(intent);
        	}
        });*/
    }	
}
