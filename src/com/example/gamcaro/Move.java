package com.example.gamcaro;

public class Move extends Cell {

	private byte piece;
	
	public Move() {
		super();
		this.piece = Board.BLANK;
	}
	
	public Move(final int row, final int column, final byte piece) {
		super(row, column);
		this.piece = piece;
	}
	
	public byte getPiece() {
		return piece;
	}
	
	public boolean equals(Move move) {
		return getRow() == move.getRow() && getColumn() == move.getColumn() && piece == move.getPiece();
	}
	
	public Move clone() {
		return new Move(getRow(), getColumn(), piece);
	}
	
	public void clone(Move move) {
		super.clone(move);
		piece = move.getPiece();
	}
	
	public void clear() {
		super.clear();
		piece = Board.BLANK;
	}
	
	public String toString() {
		return Board.playerName(piece) + " " + super.toString();
	}
	
}
