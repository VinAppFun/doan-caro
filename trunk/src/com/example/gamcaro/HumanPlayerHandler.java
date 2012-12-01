package com.example.gamcaro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HumanPlayerHandler extends DefaultHandler {
	Boolean currentElement = false;
	String currentValue = null;
	private HumanPlayerXml List = null;
	
	public HumanPlayerXml getList() {
		return List;
	}
	
	public void setList(HumanPlayerXml List)
	{
		this.List=List;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
			{
				currentElement = true;
				if (localName.equalsIgnoreCase("DATA"))		
				{
					/** Start */ 
					List = new HumanPlayerXml();
				} 
				else if (localName.equalsIgnoreCase("Item")) {
					/** Get attribute value */
					List.setDong(attributes.getValue("Dong"));
					List.setCot(attributes.getValue("Cot"));
					List.setGiaTri(attributes.getValue("GiaTri"));
				}
			}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */ 
		if (localName.equalsIgnoreCase("CurrentPlayer"))
			List.setCurrentPlayer(currentValue);
		else if (localName.equalsIgnoreCase("XVIEW"))
			List.setXview(currentValue);
		else if (localName.equalsIgnoreCase("YVIEW"))
			List.setYview(currentValue);
		else if (localName.equalsIgnoreCase("ZOOM"))
			List.setZoom(currentValue);
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
