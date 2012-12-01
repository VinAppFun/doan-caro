package com.example.gamcaro;

public class HumanPlayer extends Player {

	protected final BoardUI boardUI;
	
	public HumanPlayer(final Board board, final byte piece, final BoardUI boardUI) {
		super(board, piece);
		this.boardUI = boardUI;
		setup();
	}
	
	protected void setup() {
		boardUI.addMoveListener(new MoveListener() {
			public void moveMade(final Move move) {
				if (getPiece() != move.getPiece()) return;
				makeMove(move);
			}
//			public void lastMove(final byte victory) {
//			}
		});
	}
}
