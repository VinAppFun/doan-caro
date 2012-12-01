package com.example.gamcaro;

import java.util.Random;

public class BoardAi extends Board {
	
	int [][][][]Line;
    int [][][]Value;
    static final int[] Weight ={ 0, 0, 4, 20, 100, 500, 0 };
    int AttackFactor =  4;     
    boolean GameWon=false;
    int LineCode=-1;
    public BoardAi() {
		super();
		Line=new int[4][getHeight()][getWidth()][3];
		Value = new int[getHeight()][getWidth()][3];
	}
    
    
    public BoardAi(final int width, final int height) {
		super(width, height);
		Line=new int[4][getHeight()][getWidth()][3];
		Value = new int[getHeight()][getWidth()][3];
	}
    
	
		
	@Override
	public void clear() {
		GameWon=false;
	    LineCode=-1;
		Line=new int[4][getHeight()][getWidth()][3];
		Value = new int[getHeight()][getWidth()][3];
		super.clear();
	}


	void Add(int chieu, int d, int c,byte Player)
	    {
	        /* Adds one to the number.     */
	        Line[chieu][ d][ c][ Player] += 1;
	        
	        /* If it is the first piece in the line, then the opponent cannot use it any more.  */
	        //not use
	        //if (Line[chieu][ d][ c][ Player] == 1) this.TotalLines -= 1;
	        
	        /* The game is won if there are 5 in line. */
	        if (Line[chieu][ d][ c][ Player] == 5) GameWon = true;
	    }
    void Update( int chieu, int dong, int cot, int dv, int cv,byte Player)
    {
          //Updates the value of a square for each player, taking into
          //account that player has placed an extra piece in the square.
          //The value of a square in a usable line is Weight[Lin[Player]+1]
          //where Lin[Player] is the number of pieces already placed
          //in the line 
        byte Opponent=opponentPiece(Player);
        //If the opponent has no pieces in the line, then simply update the value for player
        if (Line[chieu][dong][cot][ Opponent] == 0)
            Value[dv][cv][Player] = Value[dv][cv][Player] + Weight[Line[chieu][ dong][ cot][ Player] +1] 
            		- Weight[Line[chieu][ dong][cot][Player]];
            //If it is the first piece in the line, then the line is spoiled for the opponent 
        else if (Line[chieu][dong][cot][Player] == 1)
                 Value[dv][cv][Opponent] = Value[dv][cv][Opponent] - Weight[Line[chieu][dong][cot][Opponent] + 1];
    }
    
    public void UpdateMove(int dong,int cot,Byte Player)    {
       // ComputerPlayerView.LineCode = -1;
      //  int Opponent = OpponentColor(Player);
      //  ComputerPlayerView.GameWon = false;
        int c1, d1;
        /* Each square of the board is part of 20 different lines. The adds
         * one to the number of pieces in each of these lines. Then it
         * updates the value for each of the 5 squares in each of the 20
         * lines. Finally Board is updated, and the move is printed on the
         * screen. */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 5; i++)
        {
            c1 = cot - i;
             d1 = dong;
            if ((c1>=0) && (c1 <  getWidth() - 4))
            {	/* Check starting point */
                Add(0,d1,c1,Player); /* Add one to line */
	                if (GameWon && (LineCode < 0))	/* Save winning line */
	                    LineCode = 0;
                for (int L = 0; L <5; L++)	/* Update value for the
					 * 5 squares in the line */
                    Update( 0, d1, c1, d1, c1 + L,Player);
            }

        }
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 5; i++)/* Diagonal lines, from lower left to upper right */
        {
            c1 = cot - i;
             d1 = dong-i;
            if ((c1>=0) && (c1 <  getWidth() -4)&&(d1>=0) && (d1 <  getHeight() - 4))
            {
                Add(1,d1,c1,Player);
	                if (GameWon && (LineCode < 0))
	                    LineCode = 1;
                for (int L = 0; L < 5; L++)
                    Update( 1, d1, c1, d1 + L, c1 + L,Player);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 5; i++)
        {
            c1 = cot + i;
             d1= dong - i;
            if ((c1 >= 4) && (c1 <  getWidth()) && (d1 >= 0) && (d1 < getHeight()- 4))
            {
                Add( 3, d1, c1,Player);
	                if (GameWon && (LineCode < 0))
	                    LineCode = 3;
                for (int L = 0; L <= 4; L++)
                    Update( 3, d1, c1,  d1 + L, c1 - L,Player);
            }
        }
        
        for (int i = 0; i < 5; i++) 
        {
            c1 = cot ;
            d1 = dong - i;
            if ((d1 >= 0) && (d1 <  getHeight() - 4))
            {
                Add( 2, d1, c1,Player);
	                if (GameWon && (LineCode < 0))
	                    LineCode = 2;
                for (int L = 0; L <= 4; L++)
                    Update( 2, d1, c1,  d1 + L, c1,Player);
            }
        }
    }
    
    
    public Move FindMove(Byte Player)
    {
        Byte Opponent = opponentPiece(Player);
        int max = Integer.MIN_VALUE,val;
        //If no square has a high value then pick the one in the middle
        int d = (getHeight() + 1) / 2;
        int c = (getWidth() + 1) / 2;
        if (getPiece(d, c)== BLANK)     max = 4; 
        //The evaluation for a square is simply the value of the square
        //for the player (attack points) plus the value for the opponent
        //(defense points). Attack is more important than defense, since
        //it is better to get 5 in line yourself than to prevent the op-
        //ponent from getting it.
        Random ra=new Random();
        for(int i=0;i<getHeight();i++)
            for (int j = 0; j < getWidth(); j++)
            {
                if (getPiece(i, j) ==BLANK)
                {
                    val = Value[i][j][Player] * (16 + AttackFactor) / 16 + Value[i][j][Opponent] +ra.nextInt(4);
                    if (val > max)
                    {
                        d = i;
                        c = j;
                        max = val;
                    }
                }
            }
        
        return new Move(d,c,Player);
    }
    
	@Override
	public void setPiece(int row, int column, byte piece) {
		UpdateMove(row, column, piece);
		super.setPiece(row, column, piece);
	}


	@Override
	public void undo(int j) {
		super.undo(j);
		Line=new int[4][getHeight()][getWidth()][3];
		Value = new int[getHeight()][getWidth()][3];
		for(int i=0;i<moveHistory.size();i++){
			UpdateMove(moveHistory.get(i).getRow(), moveHistory.get(i).getColumn(),  moveHistory.get(i).getPiece());
		}
	}
	
}
