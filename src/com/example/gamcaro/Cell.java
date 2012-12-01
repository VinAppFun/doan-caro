package com.example.gamcaro;

public class Cell {

	private byte row;
	private byte column;
	
	
	public Cell() {
		this.row = 0;
		this.column = 0;
	}
	
	
	public Cell(final int row, final int column) {
		this.row = (byte)row;
		this.column = (byte)column;
	}
	
	public byte getRow() {
		return row;
	}
	
	public byte getColumn() {
		return column;
	}
	
	//dinh nghia phuong thuc equals(==)
	public boolean equals(Cell cell) {
		return row == cell.getRow() && column == cell.getColumn();
	}
	
	//ham copy
	public Cell clone() {
		return new Cell(row, column);
	}
	
	//ham copy 
	public void clone(Cell cell) {
		row = cell.getRow();
		column = cell.getColumn();
	}
	
	
	public void clear() {
		row = 0;
		column = 0;
	}
	 
	//chuyen thanh chuoi vd (1,3)   dong 1 cot 3
	public String toString() {
		return "(" + row + ", " + column + ")";
	}
	
}
