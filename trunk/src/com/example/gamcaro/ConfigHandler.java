package com.example.gamcaro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigHandler extends DefaultHandler {
	Boolean currentElement = false;
	String currentValue = null;
	private  configXml config = null;
	public  configXml getconfig(){
		return config;
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
			{
				currentElement = true;
				if (localName.equalsIgnoreCase("DATA"))		
				{
					config = new configXml();
				} 
			}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = false;
		/** set value */ 
		if (localName.equalsIgnoreCase("soundeffect"))
			config.setSoundeffect(currentValue);
		else if (localName.equalsIgnoreCase("music"))
			config.setMusic(currentValue);
		else if (localName.equalsIgnoreCase("language"))
			config.setLanguage(currentValue);
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}
}
