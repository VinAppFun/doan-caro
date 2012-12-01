package com.example.gamcaro;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Locale;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class SettingActivity extends Activity {
	static String FILENAMEXML = "ConfigXml"; 
	RadioButton rdoEnglish,rdoVietnam;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	CheckBox chkSoundEffect;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.setting);
        rdoEnglish=(RadioButton)findViewById(R.id.radioButtonEnglish);
        rdoEnglish.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				writeFile(getxmlstatu(),SettingActivity.this);
				if(rdoEnglish.isChecked()){
					String languageToLoad  = "en";  
        		    Locale locale = new Locale(languageToLoad);   
        		    Locale.setDefault(locale);  
        		    Configuration config = new Configuration();  
        		    config.locale = locale;  
        		    getBaseContext().getResources().updateConfiguration(config,   
        		    getBaseContext().getResources().getDisplayMetrics());
        		    refresh();
					}
			}
		});
        rdoVietnam=(RadioButton)findViewById(R.id.radioButtonVietnames);
        rdoVietnam.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				writeFile(getxmlstatu(),SettingActivity.this);
				if(rdoVietnam.isChecked()){
						String languageToLoad  = "vi";  
	        		    Locale locale = new Locale(languageToLoad);   
	        		    Locale.setDefault(locale);  
	        		    Configuration config = new Configuration();  
	        		    config.locale = locale;  
	        		    getBaseContext().getResources().updateConfiguration(config,   
	        		    getBaseContext().getResources().getDisplayMetrics());
        		    refresh();
				}
			}
		});
        chkSoundEffect=(CheckBox)findViewById(R.id.checkBoxEffectSound);
        chkSoundEffect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				writeFile(getxmlstatu(),SettingActivity.this);
			}
		});
        
        String xml=readFile(this);
        if(xml.length()!=0){
        	ConfigHandler confighandler=new ConfigHandler();
        	try {
    			Xml.parse(xml,confighandler);
    		} catch (SAXException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	configXml configxml= confighandler.getconfig();
        	if(configxml!=null)
        	LoadFromConfig(configxml);
        	else LoadDefault();
		}else LoadDefault();
    }
	
	public static void loadConfig(configXml c,Activity ct) {
		if(c.getLanguage().equalsIgnoreCase("Vietnames")){
			
			String languageToLoad  = "vi";  
		    Locale locale = new Locale(languageToLoad);   
		    Locale.setDefault(locale);  
		    Configuration config = new Configuration();  
		    config.locale = locale;  
		    ct.getBaseContext().getResources().updateConfiguration(config,   
		    ct.getBaseContext().getResources().getDisplayMetrics());
		    
		}
		else
		{
			
			String languageToLoad  = "en";  
		    Locale locale = new Locale(languageToLoad);   
		    Locale.setDefault(locale);  
		    Configuration config = new Configuration();  
		    config.locale = locale;  
		    ct.getBaseContext().getResources().updateConfiguration(config,   
		    ct.getBaseContext().getResources().getDisplayMetrics());
		}
	}
	
	void LoadFromConfig(configXml c){
		chkSoundEffect.setChecked(Boolean.parseBoolean(c.getSoundeffect()));
		if(c.getLanguage().equalsIgnoreCase("Vietnames")){
			rdoEnglish.setChecked(false);
			rdoVietnam.setChecked(true);
			
			String languageToLoad  = "vi";  
		    Locale locale = new Locale(languageToLoad);   
		    Locale.setDefault(locale);  
		    Configuration config = new Configuration();  
		    config.locale = locale;  
		    getBaseContext().getResources().updateConfiguration(config,   
		    getBaseContext().getResources().getDisplayMetrics());
		    
		}
		else
		{
			rdoEnglish.setChecked(true);
			rdoVietnam.setChecked(false);
			
			String languageToLoad  = "en";  
		    Locale locale = new Locale(languageToLoad);   
		    Locale.setDefault(locale);  
		    Configuration config = new Configuration();  
		    config.locale = locale;  
		    getBaseContext().getResources().updateConfiguration(config,   
		    getBaseContext().getResources().getDisplayMetrics());
		}
	}
	void LoadDefault(){
		chkSoundEffect.setChecked(false);
		rdoEnglish.setChecked(true);
		rdoVietnam.setChecked(false);
		if(getResources().getConfiguration().locale.getCountry().equalsIgnoreCase("vn")){
			rdoEnglish.setChecked(false);
			rdoVietnam.setChecked(true);
		}

	}
	String getxmlstatu(){
		String language="English";
		if(rdoVietnam.isChecked())language="Vietnames";
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml+="<DATA>";
			xml+="<soundeffect>"+chkSoundEffect.isChecked()+"</soundeffect>";
			xml+="<language>"+language+"</language>";
		xml+="</DATA>";
		return xml;
	}
	
	public static void writeFile(String xml,Context c)
	{
		FileOutputStream fos;
		try {
			fos = c.openFileOutput(FILENAMEXML, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF8"); 
			osw.write(xml);
			osw.flush();
			fos.close();
		} catch (Exception e) {
			
		}
	}
	
	public static String readFile(Context c) 
	{
		StringBuffer xml=new StringBuffer();
		try {
			FileInputStream fis;
			fis = c.openFileInput(FILENAMEXML);
			InputStreamReader isr=new InputStreamReader(fis,"UTF8");
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) 
			{
				xml.append((char)ch);
			}
			in.close();
			fis.close();

		} catch (Exception e) {
			return "";
		}
		return xml.toString();
	}	
	public void refresh(){
		   finish();
		   Intent myIntent = new Intent(SettingActivity.this,SettingActivity.class);
		   SettingActivity.this.startActivity(myIntent);
		}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			   SettingActivity.this.startActivity(new Intent(SettingActivity.this,MainActivity.class));
			   finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	public static configXml setting(Context c){
	String xml=SettingActivity.readFile(c);
	if(xml.length()!=0){
    	ConfigHandler confighandler=new ConfigHandler();
    	try {
			Xml.parse(xml,confighandler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	configXml configxml= confighandler.getconfig();
    	if(configxml!=null)
    	{
    		if(configxml.getLanguage().equalsIgnoreCase("Vietnames")){
    			String languageToLoad  = "vi";  
    		    Locale locale = new Locale(languageToLoad);   
    		    Locale.setDefault(locale);  
    		    Configuration config = new Configuration();  
    		    config.locale = locale;  
    		    c.getResources().updateConfiguration(config,   
    		    c.getResources().getDisplayMetrics());
    		}
    	}
    	return configxml;
	}
	return null;
	}
}

