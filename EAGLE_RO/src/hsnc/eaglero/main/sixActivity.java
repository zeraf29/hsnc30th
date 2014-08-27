package hsnc.eaglero.main;

import hsnc.eaglero.main.R;
import hsnc.eaglero.main.R.id;
import hsnc.eaglero.main.R.layout;
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
        //????1?????? ????????????     
       btn_1.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_one.class);  
        		startActivity(intent);
        	}
        });      
      
              
      //????2?????? ????????????
        btn_2.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_two.class);  
        		startActivity(intent);
        	}
        });
        
        
      //????3?????? ????????????
        btn_3.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(sixActivity.this, six_three.class); 
        		startActivity(intent);
        	}
        });*/
    }	
}
