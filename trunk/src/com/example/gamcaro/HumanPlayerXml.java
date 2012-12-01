package com.example.gamcaro;

import java.util.ArrayList;

import com.example.gamcaro.Board;

public class HumanPlayerXml {
	private ArrayList<String> Dong = new ArrayList<String>();
	private ArrayList<String> Cot = new ArrayList<String>();
	private ArrayList<String> GiaTri = new ArrayList<String>();
	
	private String CurrentPlayer=String.valueOf(Board.BLACK);
	private String xview="0";
	private String yview="0";
	private String Zoom="1";
	
	public ArrayList<String> getDong() {
		return Dong;
	}

	public void setDong(String name) {
		this.Dong.add(name);
	}

	public ArrayList<String> getCot() {
		return Cot;
	}

	public void setCot(String Cot) {
		this.Cot.add(Cot);
	}

	public ArrayList<String> getGiaTri() {
		return GiaTri;
	}

	public void setGiaTri(String GiaTri) {
		this.GiaTri.add(GiaTri);
	}
	
	public String getCurrentPlayer() {
		return CurrentPlayer;
	}

	public void setCurrentPlayer(String CurrentPlayer) {
		this.CurrentPlayer=CurrentPlayer;
	}
	
	public String getXview() {
		return xview;
	}

	public void setXview(String Xview) {
		this.xview=Xview;
	}
	
	public String getYview() {
		return yview;
	}

	public void setYview(String Yview) {
		this.yview=Yview;
	}
	
	public String getZoom() {
		return Zoom;
	}

	public void setZoom(String Zoom) {
		this.Zoom=Zoom;
	}
}
