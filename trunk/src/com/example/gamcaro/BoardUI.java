package com.example.gamcaro;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


public class BoardUI extends View {
	public static int LEFT_MARGIN = 5;
	public static int TOP_MARGIN = 5;
	public static int CELL_WIDTH_DEFAUTL = 20;
	public static int CELL_HEIGHT_DEFAUTL = 20;
	public static int PIECE_WIDTH_DEFAUTL = 19;
	public static int PIECE_HEIGHT_DEFAUTL = 19;
	public static int CELL_WIDTH = 20;
	public static int CELL_HEIGHT = 20;
	public static int PIECE_WIDTH = 19;
	public static int PIECE_HEIGHT = 19;
	
	private Handler handler=new Handler();
	
	float minZoom;
		
	protected final Board board;
	private final List<MoveListener> listeners;
	
	private float zoom=1;
	public int position_x;
	public int position_y;
	 
	private int colBackground;
	private int colLine;
	private int colSelect;
	private int colBorder;
	
	void getCol(){ 
		Resources r=getContext().getResources();
		colBackground=r.getColor(R.color.BoardBackground);
		colLine=r.getColor(R.color.BoardLine);
		colSelect=r.getColor(R.color.BoardSelect);
		colBorder=r.getColor(R.color.BoardBorder);
	}
	public BoardUI(Context c,final Board board) {
		super(c);
		this.board = board;
				
		/////////////////////////////////////////////
		this.listeners = new ArrayList<MoveListener>();
		
		minZoom=Math.min((getWidth()/(float)board.getWidth())/(float)CELL_WIDTH_DEFAUTL,(getHeight()/(float)board.getHeight())/(float)CELL_HEIGHT_DEFAUTL);
		setZoom(1.5F);
		position_x=(board.getWidth()*CELL_WIDTH/2-getWidth())/2;
		position_y=(board.getHeight()*CELL_HEIGHT/2-getHeight())/2;
		getCol();
		update();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		minZoom=Math.min((getWidth()/(float)board.getWidth())/(float)CELL_WIDTH_DEFAUTL,(getHeight()/(float)board.getHeight())/(float)CELL_HEIGHT_DEFAUTL);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void center()
	{
		position_x=(board.getWidth()*CELL_WIDTH-getWidth())/2;
		position_y=(board.getHeight()*CELL_HEIGHT-getHeight())/2;
	}
	public void setZoom(float i) {
		if(i<minZoom || i>2.5F)return;
		int x_center,y_center;
		if(board.getLastMovew()!=null){
			x_center=board.getLastMovew().getColumn();
			y_center=board.getLastMovew().getRow();
		}
		else
		{
				x_center=(int)(position_x/(float)CELL_WIDTH+(getWidth()/(float)CELL_WIDTH)/2);
				y_center=(int)(position_y/(float)CELL_HEIGHT+(getHeight()/(float)CELL_HEIGHT)/2);
		}
		
		zoom=i;
		CELL_WIDTH=(int)(CELL_WIDTH_DEFAUTL*zoom);
		CELL_HEIGHT=(int)(CELL_HEIGHT_DEFAUTL*zoom);
		PIECE_WIDTH=(int)(PIECE_WIDTH_DEFAUTL*zoom);
		PIECE_HEIGHT=(int)(PIECE_HEIGHT_DEFAUTL*zoom);
		
		position_x=(int)((x_center+0.5F-(getWidth()/(float)CELL_WIDTH)/2)*CELL_WIDTH);
		position_y=(int)((y_center+0.5F-(getHeight()/(float)CELL_HEIGHT)/2)*CELL_HEIGHT);
		
		position_x=(position_x<0)?0:position_x;
		position_y=(position_y<0)?0:position_y;
		if(board.getWidth()*CELL_WIDTH<=getWidth()||board.getHeight()*CELL_HEIGHT<=getHeight())
		{
			position_x=position_y=0;
		}
		getbitbap();
		update();
	}
	Bitmap bmpShadowBlack;
	Bitmap bmpShadowWhite;
	Bitmap bmpHigh;
	Bitmap bmpSquare_black;
	Bitmap bmpSquare_white;
	Bitmap bmpSquare_black_last;
	Bitmap bmpSquare_white_last;
	void getbitbap()
	{
			Resources r=getResources();
			Drawable tile;
			tile=r.getDrawable(R.drawable.shadow_black);
			Canvas canvas;
			
			bmpShadowBlack = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpShadowBlack);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.shadow_white);
	        bmpShadowWhite = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpShadowWhite);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.high);
	        bmpHigh = Bitmap.createBitmap(PIECE_WIDTH*5,PIECE_HEIGHT*5, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpHigh);
	        tile.setBounds(0, 0, PIECE_WIDTH*5,PIECE_HEIGHT*5);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.square_black);
	        bmpSquare_black = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpSquare_black);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.square_white);
	        bmpSquare_white = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpSquare_white);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.square_black_last);
	        bmpSquare_black_last = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpSquare_black_last);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
	        
	        tile=r.getDrawable(R.drawable.square_white_last);
	        bmpSquare_white_last = Bitmap.createBitmap(PIECE_WIDTH,PIECE_HEIGHT, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bmpSquare_white_last);
	        tile.setBounds(0, 0, PIECE_WIDTH,PIECE_HEIGHT);
	        tile.draw(canvas);
		
	}
	
	public float getZoom() {
		return zoom;
	}
	public void update()
	{
		handler.post(new Runnable() {
			//@Override
			public void run() {
				invalidate();
			}
		});
	}

	int TouchDownX,TouchDownY;
	Point celltouch=new Point();
	PointF LastTouch=new PointF();
	boolean mButtonPressed=false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			LastTouch.x=(int)event.getX();
			LastTouch.y=(int)event.getY();
			
			float x=LastTouch.x+position_x-LEFT_MARGIN;
			float y=LastTouch.y+position_y-TOP_MARGIN;
			celltouch.x=(int)(x/CELL_WIDTH);
			celltouch.y=(int)(y/CELL_WIDTH);
			mButtonPressed=true;
			update();
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			final float x = event.getX()+position_x-LEFT_MARGIN;
	        final float y = event.getY()+position_y-TOP_MARGIN;
	        if((int)(x/CELL_WIDTH)==(int)((LastTouch.x+position_x-LEFT_MARGIN)/CELL_WIDTH)&&
	        		(int)(y/CELL_HEIGHT)==(int)((LastTouch.y+position_y-TOP_MARGIN)/CELL_HEIGHT)&&mButtonPressed==true){
	        	return true;
	        }
	        mButtonPressed=false;
	        // Calculate the distance moved
	        final float dx =  event.getX() - LastTouch.x;
	        final float dy =  event.getY() - LastTouch.y;
	        
	        // Move 
	        position_x -= dx;
	        position_y -= dy;
	        
	        // Remember this touch position for the next move event
	        LastTouch.x=(int)event.getX();
			LastTouch.y=(int)event.getY();
			
			position_x=(position_x<0)?0:position_x;
			position_y=(position_y<0)?0:position_y;
			
			position_x=(position_x+getWidth()>board.getWidth()*CELL_WIDTH-LEFT_MARGIN)?(board.getWidth()*CELL_WIDTH+LEFT_MARGIN*2-getWidth()):position_x;
			position_y=(position_y+getHeight()>board.getHeight()*CELL_HEIGHT-TOP_MARGIN)?(board.getHeight()*CELL_HEIGHT+TOP_MARGIN*2-getHeight()):position_y;
			
			position_x=(getWidth()>board.getWidth()*CELL_WIDTH-LEFT_MARGIN)?0:position_x;
			position_y=(getHeight()>board.getHeight()*CELL_HEIGHT-TOP_MARGIN)?0:position_y;
			
			update();
			return true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(mButtonPressed==false)return true;
			mButtonPressed=false;
			update();
	    	if (celltouch.x >= board.getWidth() || celltouch.y >= board.getHeight()) return true;
	    	fireMoveMade(new Move(celltouch.y,celltouch.x, board.getCurrentPiece()));
			return super.onTouchEvent(event);
			}
			return super.onTouchEvent(event);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		Move m=board.getLastMovew();
		//Background
		canvas.drawColor(colBackground);
		Paint myPen=new Paint();
		
		for (int r = 0; r <= board.getHeight(); r++) {
			drawLine(LEFT_MARGIN-position_x, (TOP_MARGIN + r * CELL_HEIGHT)-position_y,
					(LEFT_MARGIN + board.getWidth() * CELL_WIDTH)-position_x, (TOP_MARGIN + r * CELL_HEIGHT)-position_y,canvas,myPen);
		}
		for (int c = 0; c <= board.getWidth(); c++) {
			drawLine((LEFT_MARGIN + c * CELL_WIDTH)-position_x, TOP_MARGIN-position_y,
					(LEFT_MARGIN + c * CELL_WIDTH)-position_x, (TOP_MARGIN + board.getHeight() * CELL_HEIGHT)-position_y,canvas,myPen);
		}
		myPen.setColor(colBorder);
		myPen.setStrokeWidth(3);
		canvas.drawRect(LEFT_MARGIN-position_x, TOP_MARGIN-position_y,LEFT_MARGIN+board.getWidth()* CELL_WIDTH-position_x,  (TOP_MARGIN + board.getHeight() * CELL_HEIGHT)-position_y, myPen);
		
		for (int r = 0; r < board.getHeight(); r++) {
			for (int c = 0; c < board.getWidth(); c++) {
				int x_in=0,y_in=0;
				if(m!=null){
					 x_in=(int)((m.getColumn()-c)/(float)board.getWidth()*PIECE_WIDTH);
					 y_in=(int)((m.getRow()-r)/(float)board.getWidth()*PIECE_HEIGHT);
					 x_in=(x_in>PIECE_WIDTH/2)?PIECE_WIDTH/2:x_in;
					 y_in=(y_in>PIECE_HEIGHT/2)?PIECE_HEIGHT/2:y_in;
				}
				byte piece = board.getPiece(r, c);
				if (piece == Board.WHITE) {
					if(board.lastmovew(new Move(r,c,Board.WHITE))){
						drawWhiteLast(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2,canvas);
					}
					else{
						drawShadowWhite(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2-x_in, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2-y_in,canvas);
						drawWhite(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2, canvas);
						}
				}
				if (piece == Board.BLACK) {
					if(board.lastmovew(new Move(r,c,Board.BLACK))){
						drawBlackLast(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2,canvas);
					}
					else{
						drawShadowBlack(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2-x_in, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2-y_in,canvas);
						drawBlack(LEFT_MARGIN-position_x + c * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2, TOP_MARGIN-position_y + r * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2,canvas);
					}
				}
			}
		}
		if(m!=null){
			drawHigh(LEFT_MARGIN-position_x + m.getColumn() * CELL_WIDTH + (CELL_WIDTH - PIECE_WIDTH) / 2, TOP_MARGIN-position_y + m.getRow() * CELL_HEIGHT + (CELL_HEIGHT - PIECE_HEIGHT) / 2, canvas);	
		} 
		
		if(mButtonPressed==true)
		{
			if(celltouch.x<board.getWidth()&&celltouch.y<board.getHeight()/*&&board.getPiece(celltouch.x,celltouch.y)==Board.BLACK*/){
				myPen.setColor(colSelect);
				myPen.setStrokeWidth(3);
				myPen.setStyle(Paint.Style.STROKE);
				canvas.drawRect(LEFT_MARGIN-position_x + celltouch.x * CELL_WIDTH,TOP_MARGIN-position_y+celltouch.y*CELL_WIDTH,
						LEFT_MARGIN-position_x+(celltouch.x+1)*CELL_WIDTH, TOP_MARGIN-position_y+(celltouch.y+1)*CELL_HEIGHT, myPen);
			}
		}
		super.onDraw(canvas);
	}
	
	protected void drawLine(final int x1, final int y1, final int x2, final int y2,Canvas c,Paint myPen) {
		myPen.setColor(colLine);
		myPen.setStrokeWidth(1);
		myPen.setStyle(Paint.Style.STROKE);
		c.drawLine(x1, y1, x2, y2, myPen);
	}
	
	protected void drawHigh(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpHigh,x-PIECE_WIDTH*2,y-PIECE_HEIGHT*2,null);   
	}
	protected void drawShadowBlack(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpShadowBlack,x,y,null);   
	}
	
	protected void drawShadowWhite(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpShadowWhite,x,y,null);   
	}
	
	protected void drawWhite(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpSquare_white,x,y,null);   
	}
	
	protected void drawWhiteLast(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpSquare_white_last,x,y,null);   
	}

	protected void drawBlack(final int x, final int y, Canvas c) {
	    c.drawBitmap(bmpSquare_black,x,y,null); 
	}
	
	protected void drawBlackLast(final int x, final int y,Canvas c) {
	    c.drawBitmap(bmpSquare_black_last,x,y,null); 
	}
	
	public void addMoveListener(final MoveListener src) {
		listeners.add(src);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	public void fireMoveMade(final Move move) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).moveMade(move);
		}
	}
	
}
