package com.example.gamcaro;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private final List<MoveListener> listeners;
	protected final Board board;
	private final byte piece;
	
	public Player(final Board board, final byte piece) {		
		this.board = board;
		this.piece = piece;
		this.listeners = new ArrayList<MoveListener>();
	}
	
	public byte getPiece() {
		return piece;
	}
	
	public void makeMove(final Move move) {
		if (piece != move.getPiece()) return;
		if (piece != board.getCurrentPiece()) return;
		if (move.getRow()>= board.getHeight()||move.getColumn()>= board.getWidth()) return;
		if (board.getPiece(move.getRow(), move.getColumn()) != Board.BLANK) return;
		board.setPiece(move.getRow(), move.getColumn(), piece);
		fireMoveMade(move);
	}
	
	public void addMoveListener(final MoveListener src) {
		listeners.add(src);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	protected void fireMoveMade(final Move move) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).moveMade(move);
		}
	}
	
	public void think(final Move move) {
	}
	
	public boolean checkReady() {
		return true;
	}
	
}
