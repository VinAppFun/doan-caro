package com.example.gamcaro;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

public class ComputerPlayerActivity extends Activity {
	private Board board;
	private BoardUI boardui;
	private ZoomControls zoomcontrol;
	private HumanPlayer humanplayer;
	private Boolean finish=false;
	private TextView textTime;
	private Handler handler=new Handler();
	AlertDialog dialog;
	static String FILENAMEXML = "ComputerPlayerXml"; 
	MediaPlayer mediaplayer;
	MediaPlayer mediaplayer2;
	private final int timeLimit=60;
	
	
	String strYes,strNo,strOk,strBlack,strWhite,strStringDialogFirstPlay,
	strBlackWin,strWhiteWin,strDraw,strEmpty;
	
	configXml config;
	
	void getString()
	{
		strYes=getResources().getString(R.string.yes);
		strNo=getResources().getString(R.string.no);
		strOk=getResources().getString(R.string.ok);
		strBlack=getResources().getString(R.string.black);
		strWhite=getResources().getString(R.string.white);
		strStringDialogFirstPlay=getResources().getString(R.string.stringDialogFirstPlay);
		strBlackWin=getResources().getString(R.string.blackWin);
		strWhiteWin=getResources().getString(R.string.whiteWin);
		strDraw=getResources().getString(R.string.draw);
		strEmpty="";
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		getString();
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
		config=SettingActivity.setting(this);
		board=new BoardAi();
		boardui=new BoardUI(this, board);
		zoomcontrol = new ZoomControls(this);
		textTime=new TextView(this);
		mediaplayer=MediaPlayer.create(this,R.raw.d);
		mediaplayer2=MediaPlayer.create(this,R.raw.putpiece);
		textTime.setTextColor(Color.BLUE);
		textTime.setText(String.valueOf(timeLimit));
		humanplayer = new HumanPlayer(board,Board.BLACK, boardui);
		humanplayer.addMoveListener(new MoveListener() {
			public void moveMade(Move move) {
				updateMove(move);
			}
		});
		
		board.setCurrentPiece(Board.BLANK);
		////////////////////////////////////////////////////////////////////////////////////////////////
        RelativeLayout layout=new RelativeLayout(this);
        layout.addView(boardui, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout.addView(zoomcontrol,lp);
        
        RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.setMargins(0,10, 10,0);
        layout.addView(textTime,lp2);
        
        setContentView(layout);
		////////////////////////////////////////////////////////////////////////////////////////////////
		zoomcontrol.setOnZoomInClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boardui.setZoom(boardui.getZoom()+0.5F);   
            }
			});
		zoomcontrol.setOnZoomOutClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boardui.setZoom(boardui.getZoom()-0.5F);    
            }
			});
		
		dialog = new AlertDialog.Builder(this).create();
		dialog.setCancelable(false);
		
		getString();
		String xml=readFile();
		if(xml.length()!=0){
			playFromXml(xml);
		}
		else play();
	}
	
	void playFromXml(String xml){
		HumanPlayerHandler humanplayerhandler=new HumanPlayerHandler();
		try {
			Xml.parse(xml,humanplayerhandler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HumanPlayerXml humanplayerxml= humanplayerhandler.getList();
		int d,c;
		byte cell;
		int size=humanplayerxml.getGiaTri().size();
		for(int i=0;i<size;i++)
		{
			d=Integer.parseInt(humanplayerxml.getDong().get(i));
			c=Integer.parseInt(humanplayerxml.getCot().get(i));
			cell=Byte.parseByte(humanplayerxml.getGiaTri().get(i));
			
			board.setPiece(d, c, cell);
		}
		board.setCurrentPiece(Byte.parseByte(humanplayerxml.getCurrentPlayer()));
		
		int xview,yview;
		float zoom;
		try {
			xview=Integer.parseInt(humanplayerxml.getXview());
		} catch (Exception e) {
			xview=0;
		}
		try {
			yview=Integer.parseInt(humanplayerxml.getYview());
		} catch (Exception e) {
			yview=0;
		}
		try {
			zoom=Float.parseFloat(humanplayerxml.getZoom());
		} catch (Exception e) {
			zoom=1.5F;
		}
		boardui.setZoom(zoom);
		boardui.position_x=xview;
		boardui.position_y=yview;
		
		finish=false;
    	textTime.setText(String.valueOf(timeLimit));
    	
    	boardui.update();
    	
    	threadComputer = new myThread(doBackgroundThreadProcessing); 
    	threadComputer.start();
    	
    	threadTimer = new myThread(doBackgroundThreadProcessing2); 
    	threadTimer.start();
	}
	
	void updateMove(Move m)
	{
		if(config!=null && config.getSoundeffect().equalsIgnoreCase(String.valueOf(true)))
		mediaplayer2.start();
		boardui.update();
		handler.post(new Runnable() {
			public void run() {
				textTime.setText(String.valueOf(timeLimit));
			}
		});
		
		byte victory = board.victory();
		switch (victory) {
		case Board.BLACK_WIN:
			threadComputer.requestStop();
			threadTimer.requestStop();
			finish=true;
			dialog.setTitle(strEmpty);
			dialog.setMessage(strBlackWin);
			dialog.setButton(strOk, new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  handler.post(new Runnable() {
							public void run() {
								play();
							}
						});
			    } });
			dialog.show();
			break;
		case Board.WHITE_WIN:
			threadComputer.requestStop();
			threadTimer.requestStop();
			finish=true;
			dialog.setTitle(strEmpty);
			dialog.setMessage(strWhiteWin);
			dialog.setButton(strOk, new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  handler.post(new Runnable() {
							public void run() {
								play();
							}
						});
			    } });
			handler.post(new Runnable() {
				public void run() {
					dialog.show();
				}
			});
			break;
		case Board.DRAW:
			threadComputer.requestStop();
			threadTimer.requestStop();
			finish=true;
			dialog.setTitle(strEmpty);
			dialog.setMessage(strDraw);
			dialog.setButton(strOk, new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  handler.post(new Runnable() {
							public void run() {
								play();
							}
						});
			    } });
			dialog.show();
			break;
			default:
				board.setCurrentPiece(Board.opponentPiece(board.getCurrentPiece()));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	
		boardui.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
    
    class myThread extends Thread {
    	  // Must be volatile:
    	public myThread(Runnable a) {
			super(a);
		}
    	  private volatile boolean stop = false;
    	  private volatile boolean pause = false;
    	  public void run() {
    	    while (stop==false&&finish==false) {
    	    	try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
    	    	if(pause==true)continue;
    	    	super.run();
    	    }
    	  }
    	  public void requestStop() {
    	    stop = true;
    	  }
    	  public void setPause(boolean i) {
			pause=i;
		}
    	}
    
    private myThread threadComputer=null;
    private myThread threadTimer=null;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void play()
    {
    	boardui.center();
    	finish=false;
    	textTime.setText(String.valueOf(timeLimit));
    	board.clear();
    	boardui.update();
    	
    	if(threadComputer!=null)
    	threadComputer.requestStop();
    	if(threadTimer!=null)
		threadTimer.requestStop();
		
    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    	dialogBuilder.setTitle(strEmpty);
    	dialogBuilder.setMessage(strStringDialogFirstPlay);
    	dialogBuilder.setPositiveButton(strYes, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			board.setCurrentPiece(Board.WHITE);
    			threadComputer = new myThread(doBackgroundThreadProcessing); 
    	    	threadComputer.start();
    	    	threadTimer = new myThread(doBackgroundThreadProcessing2); 
    	    	threadTimer.start();
    		}
    		});
    	dialogBuilder.setNegativeButton(strNo, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			board.setCurrentPiece(Board.BLACK);
    			threadComputer = new myThread(doBackgroundThreadProcessing); 
    	    	threadComputer.start();
    	    	threadTimer = new myThread(doBackgroundThreadProcessing2); 
    	    	threadTimer.start();
    		}
    		});
    	AlertDialog option = dialogBuilder.create();
    	option.setCancelable(false);
    	option.show();
    }
    
    private Runnable doBackgroundThreadProcessing = new Runnable() {
    	  public void run() {
    		 	if(board.getCurrentPiece()==Board.WHITE){
  					Move m=board.FindMove(Board.WHITE);
  					//Move m=BoardAB.FindMove(Board.WHITE, board.getWidth(), board.getMoveHistory());
  					board.setPiece(m.getRow(), m.getColumn(),m.getPiece());
  					boardui.update();
  					updateMove(m);
  					////////////////////////////////////////////////////////////////////////////////
  					
  				}
  			}
    	};

    private Runnable doBackgroundThreadProcessing2 = new Runnable() {
      	  public void run() {
      		  	handler.post(new Runnable() {
  						public void run() {
  							int i=Integer.parseInt(textTime.getText().toString());
  		    				i--;
  		    				if(i<10)
  		    					{
  		    						textTime.setTextColor(Color.RED);
  		    						if(config!=null && config.getSoundeffect().equalsIgnoreCase(String.valueOf(true)))
  		    						mediaplayer.start();
  		    					}
  		    				else
  		    					textTime.setTextColor(Color.BLUE);
  		    				textTime.setText(String.valueOf(i));
  		    				if(i<=0)
  		    				{
  		    					byte victory;
  		    					if(board.getCurrentPiece()==Board.WHITE)
  		    						victory=Board.BLACK_WIN;
  		    					else victory=Board.WHITE_WIN;
  		    					
  		    					switch (victory) {
  		    					case Board.BLACK_WIN:
  		    						threadComputer.requestStop();
  		    						threadTimer.requestStop();
  		    						finish=true;
  		    						dialog.setTitle(strEmpty);
  		    						dialog.setMessage(strBlackWin);
  		    						dialog.setButton(strOk, new DialogInterface.OnClickListener() {
  		    						      public void onClick(DialogInterface dialog, int which) {
  		    						    	  handler.post(new Runnable() {
  		    										public void run() {
  		    											play();
  		    										}
  		    									});
  		    						    } });
  		    						dialog.show();
  		    						break;
  		    					case Board.WHITE_WIN:
  		    						threadComputer.requestStop();
  		    						threadTimer.requestStop();
  		    						finish=true;
  		    						dialog.setTitle(strEmpty);
  		    						dialog.setMessage(strWhiteWin);
  		    						dialog.setButton(strOk, new DialogInterface.OnClickListener() {
  		    						      public void onClick(DialogInterface dialog, int which) {
  		    						    	  handler.post(new Runnable() {
  		    										public void run() {
  		    											play();
  		    										}
  		    									});
  		    						    } });
  		    						handler.post(new Runnable() {
  		    							public void run() {
  		    								dialog.show();
  		    							}
  		    						});
  		    						break;
  		    					}
  		    				}
  						}
  					});
    			}
      	  };

	////////////////////////////////////////////////////////////////////////////////////////////////
    //file menu
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.new_game:
	    	play();
	        return true;
	    case R.id.undo:
	    	if(finish==false){
		    	board.undo(2);
		    	boardui.update();
	    	}
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	
	
	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	} 
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	
	protected void onStop(){
		super.onStop();
	}
	//lay du lieu va doc file xml
	void writeFile(String xml)
	{
		FileOutputStream fos;
		try {
			fos = this.openFileOutput(FILENAMEXML, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF8"); 
			osw.write(xml);
			osw.flush();
			fos.close();
		} catch (Exception e) {
			
		}
	}
	String readFile() 
	{
		StringBuffer xml=new StringBuffer();
		try {
			FileInputStream fis;
			fis = this.openFileInput(FILENAMEXML);
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
}
