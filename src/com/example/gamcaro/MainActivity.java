package com.example.gamcaro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	Button buttonbatdau,buttoncaidat,buttontrogiup,buttonthoat;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        SettingActivity.setting(this);
    	setContentView(R.layout.activity_main);
        
        buttonbatdau=(Button)findViewById(R.id.btnbatdau);
        buttonbatdau.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, ComputerPlayerActivity.class);
        		startActivity(intent);
            }
        });
        
        buttoncaidat=(Button)findViewById(R.id.btncaidat);
        buttoncaidat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        		startActivity(intent);
        		finish();
            }
        });
       
        buttonthoat = (Button)findViewById(R.id.btnthoat);
   	 	buttonthoat.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
		    	case R.id.btnthoat:
		    		finish();
				}
			}
		
        
    });
	}
    
    @Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}