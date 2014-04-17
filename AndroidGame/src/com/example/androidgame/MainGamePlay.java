package com.example.androidgame;

import java.util.ArrayList;
import java.util.Random;

import com.example.movingobject123.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainGamePlay extends Activity {

	// 	ACTIVITY VARIABLE
		private Intent i ;
		private int request_code;
		
		//OBEJECTS VARIABLE
		private ArrayList<Rubbish> rubbishes = new ArrayList<Rubbish>();
		private boolean isReachBin = false;
		//CONSTANT FOR TYPE OF RUBBISH
		private static final int  COMPOST = 0;
		private static final int PAPER = 1;
		private static final int GLASS = 2;
	   
	   //CONSTANT FOR TYPE OF SPECTIAL
		private static final int SLOWDOWN = 5;
		private static final int ADDINGTIME = 1;
	   
	   
       //LAYOUT AND DISPLAY VARIABLE
       private LayoutParams layoutParams;
       private RecycleBin binCompost, binPaper, binGlass;
       private int score;
       private int level;
       private String playerName;
       private String location;
       int windowwidth;
       int windowheight;
       int startX, toX, startY, toY;
       
       //HANDLER VARIABLE
       private Handler movingRubbish = new Handler();
       private Handler displayNewRubbish = new Handler();
       private Handler timeCountDown = new Handler();
       
       private boolean gameOn = false;
       
       //TIME VARIABLE
       private static final int TIME = 20;
       int DELAY =50;
       private long startTime;
       private long addingTime;
	

       @Override
       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
              
              //SET LAYOUT FOR BINS
              setStartLayout();
              
              //INITIAL VALUE OF GAME ATTRIBUTE
              score = 0;
              level = 10;
              location = "NTU";
              playerName = "LUIS NGO";
              playGame(level, location, playerName);             
       }
       
       //----------------------------BEGIN PLAY GAME FUNCTION-----------------------------------------------//
       	private void playGame(int level, String location, final String playerName){
    	   
    	 //GET THE SIZE OF THE SCREEN
       		//SET THE GAME ON
       		gameOn = true;
           	RelativeLayout binLayout = (RelativeLayout)findViewById(R.id.bin_layout);
 			int layout_width = binLayout.getWidth();
          
           	//INITAILIZE RECYCLE BIN
           	
 			//RECYCLE OF FOOD
 			binCompost = new BinCompost((ImageView)findViewById(R.id.binCompost));
 			//RECYCLE OF GLASS
           	binGlass = new BinGlass((ImageView)findViewById(R.id.binGlass));
 			//RECYCLE OF PAPER
           	binPaper = new BinPaper((ImageView)findViewById(R.id.binPaper));
           	
          //DRAG BIN
           	dragObject(binCompost.getImg());
          	dragObject(binPaper.getImg());
			dragObject(binGlass.getImg());
			
			//RANDOM NEW OBJECT
			displayNewRubbish.postDelayed(addRubbish, 0);

			//MOVING OBJECT
       		movingRubbish.postDelayed(movingObjectThread, 0);
        			
       		//DISPLAY INFORMATION
       		displayInfomation();
       		
    	   return;
       }
   //-------------------------------END PLAY GAME FUNCTION -----------------------------------//
       	
  //---------------------------------BEGIN CREATE RUNNABLE CLASS------------------------------//	
       //TIMER METHOD - UPDATE FRAME LAYOUT
     Runnable movingObjectThread = new Runnable() {
     
   	   public void run() {
	  			if (gameOn){
	  				updateFrame();
	  				movingRubbish.postDelayed(this, 0);
	  			}
	  		}
   	   
 	};
 	
 	 //TIMER METHOD - ADD NEW OBJECT
 	Runnable addRubbish = new Runnable(){
 		public void run(){
 			if (gameOn){
 				RelativeLayout rel_layout = (RelativeLayout)findViewById(R.id.rel_layout);
 				int layout_width = rel_layout.getWidth();
	 			Random random = new Random();
	 			if (layout_width <= 0 ){                        
	 				startX = random.nextInt(100);
	 			}
	 			else{
	 				startX = random.nextInt((int)(0.8*layout_width)); //Position of rubbish appearing
	 			}
	 			addObject(rubbishes, startX, startY);
	 			displayNewRubbish.postDelayed(this, 2000 - 100*level);
 			}
 		}
 	};
 	
 	//TIMER METHOD - COUNT DOWN
 	Runnable countDownThread = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startTime = startTime + addingTime/2;			//DONT KNOW WHY HAVE TO DIVIDE
			addingTime = 0;
			startTime = startTime - 1;
			String time_str = String.format("TIME : %2d : %2d" , startTime/60, startTime % 60);  //SET TIME FORMAT
			TextView clock =  (TextView)findViewById(R.id.time);
			clock.setText(time_str);
			timeCountDown.postDelayed(this, 1000);
			
			//WHEN TIME UP
			if (startTime <= 0 ){
				gameOn = false;
				rubbishes.clear();
				onStop();
				
				//Pass parameter to next Activity
				i.putExtra("name", playerName);
				i.putExtra("score", String.format("%d", score));
				
				//GO TO NEXT ACTIVY
				startActivityForResult(i,request_code);
				timeCountDown.removeCallbacks(this);
			}
			
		}
	};
	
  	//-----------------------------------END START HANDLER CLASS-----------------------------------------------//
  	
	//----------------------------------BEGIN MOTION IN GAME FUNCTIONS--------------------------------------------------------//
  	//DRAG OBJECT WHEN TOUCHING
  	protected void dragObject(final ImageView img_view){  		
  		 img_view.setOnTouchListener(new View.OnTouchListener() {
  		
             @Override
             public boolean onTouch(View v, MotionEvent event) {
            	 
            	 //Get layout of recycle bins
            	 RelativeLayout rel_layout = (RelativeLayout)findViewById(R.id.bin_layout);
                 RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_view.getLayoutParams();
                 
                   //ACTION MOVE
                   switch (event.getAction()) {
                   case MotionEvent.ACTION_DOWN:	
                          break;
                   case MotionEvent.ACTION_MOVE:	
                	   	//Record the coordinate of pointing
                          int x_cord = (int) event.getRawX();
                          if (x_cord > rel_layout.getWidth() - img_view.getWidth()/2) {     //If move out the screen
                                 x_cord = rel_layout.getWidth() - img_view.getWidth()/2;
                          }
                          layoutParams.leftMargin = x_cord - img_view.getWidth()/2;   //Reduce the offset between position of image with 
                          															//position of hand-touched point	
                          img_view.setLayoutParams(layoutParams);
                          
                          break;
                   default:
                          break;
                   }
                   return true;
             }
      });
  		return;
  	}
  	
	
	//UPDATE FRAME LAYOUT EACH PERIOD 
	private void updateFrame(){
		
		RelativeLayout rel_layout 	= (RelativeLayout)findViewById(R.id.rel_layout);
		
		//REMOVE ALL THE VIEW ON THE LAYOUT - IMPORTANT
		rel_layout.removeAllViews();

		ArrayList<RelativeLayout.LayoutParams> layout_paras = new ArrayList<RelativeLayout.LayoutParams>();		
		if (rubbishes.size() >  0){
			for (int i = 0; i < rubbishes.size(); i++){
				isReachBin = false;
				ImageView v = rubbishes.get(i).getImg();
				//CREATE NEW LAYOUT PARAGRAM
				layout_paras.add(i,
						new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
				
				//ALIGN LAYOUT
				layout_paras.get(i).addRule(RelativeLayout.ALIGN_LEFT);
				layout_paras.get(i).addRule(RelativeLayout.ALIGN_TOP);
				
				//CREATE NEW IMAGE VIEW
				
				ImageView img = new ImageView(getBaseContext());
				//GET THE SOURCE IMAGE OF CURRENT IMAGE VIEW
				img.setImageDrawable(v.getDrawable());
				img.setScaleX(1);
				img.setScaleY(1);
						
				//SET NEW POSITION FOR IMAGE VIEW			
				v.setY(v.getY() + level );
				layout_paras.get(i).leftMargin =(int) v.getX();
				layout_paras.get(i).topMargin = (int)v.getY();
				
				
				//ADD IMAGE VIEW TO LAYOUT
				rel_layout.removeView(rubbishes.get(i).getImg());
				rel_layout.addView(img, layout_paras.get(i));
				
		
				//WHEN OBJECT IS OUT OF SCREEN
				if (v.getY() > rel_layout.getHeight() ){
					rubbishes.remove(i);
					
				}
				if (rubbishes.size() > 0){
				//WHEN OBJECTS REACH THE BIN
					if (!(reachBin(i, binCompost) || reachBin(i, binPaper) || reachBin(i, binGlass))){
						if (isReachBin){
							isReachBin = false;
							score = score - 21;
							rubbishes.remove(i);
						}
						
					}else if (reachBin(i, binCompost) || reachBin(i, binPaper) || reachBin(i, binGlass)){
						rubbishes.remove(i);
						
					}
					
					//DISPLAY SCORE TO THE SCREEN
		        	String score_str = String.format("SCORE : %d", score	);
		       		TextView score_view = (TextView)findViewById(R.id.score);
		       		score_view.setText(score_str);
				}
	
			}	
		}
	}
	
	//----------------------------------END MOTION IN GAME FUNCTIONS ---------------------------------------------//
	
	//----------------------------------BEGIN FUNCTIONS RELATED OBJECTS -----------------------------------------//
	//ADD NEW OBJECT RUBBISH
	private void addObject (ArrayList<Rubbish> rubbishes, int x, int y){
		//RANDOM TYPE OF RUBBISH
		Random rand = new Random();
		ImageView img = new ImageView(getBaseContext());
       	Rubbish rb;
		int typeRubbish = rand.nextInt(3);
		
		//RANDOM SPECTIAL ITEM
		Random rand_special = new Random();
		int typeSpecial = rand_special.nextInt(10);
		
		//RANDOM TYPE OF RUBBISH : FOOD, PAPER, GLASS
		switch (typeRubbish) {
		case COMPOST:
			switch(typeSpecial){
			case ADDINGTIME:                              //ADDING TIME
				rb = new CompostAddingTime();
				img.setImageResource(R.drawable.compost_adding_1);
				break;
			case SLOWDOWN:                                  //SLOW DOWN THE SPEED OF DROP
				rb = new CompostSlowDown();
				img.setImageResource(R.drawable.compost_slow_1);
				break;
			default:                                       //NORMAL
				rb = new RubbishCompost();
				img.setImageResource(R.drawable.rubbish_compost);
				break;
			}
			rb.setImg(img);
			rb.setCoordinate(x, y);
			break;
			
		case PAPER:
			switch(typeSpecial){
			case ADDINGTIME:                                //ADDING TIME
				rb = new PaperAddingTime();
				img.setImageResource(R.drawable.paper_adding_1);
				break;
			case SLOWDOWN:                                 //SLOW THE SPEED OF DROP
				rb = new PaperSlowDown();
				img.setImageResource(R.drawable.paper_slow_1);
				break;
			default:                                        //NORMAL
				rb = new RubbishPaper();
				img.setImageResource(R.drawable.rubbish_paper);
				break;
			}
			rb.setImg(img);
			rb.setCoordinate(x, y);
			break;
		case GLASS:
			switch(typeSpecial){
			case ADDINGTIME:                                        //ADDING TIME
				rb = new GlassAddingTime();
				img.setImageResource(R.drawable.glass_adding_1);
				break;
			case SLOWDOWN:                                    //SLOW THE SPEED OF DROP
				rb = new GlassSlowDown();
				img.setImageResource(R.drawable.glass_slow_1);
				break;
			default:                                             //NORMAL
				rb = new RubbishGlass();
				img.setImageResource(R.drawable.rubbish_glass);
				break;
			}
			rb.setImg(img);
			rb.setCoordinate(x, y);
			break;
		default:
			rb = null;
			break;
		}
		
       	rubbishes.add(rb);
		
	}
	
	//WHEN RUBBISH REACH THE BIN
	private boolean reachBin(int i, RecycleBin bin){
		
		ImageView rb = rubbishes.get(i).getImg();
		ImageView b = bin.getImg();
		//When the rubbish reach the bin
		if ((rb.getX() >= b.getX() - b.getWidth()/2 && rb.getX() <= b.getX() + b.getWidth()/2) 
        		  && (rb.getY() >= b.getY() - b.getHeight()/2 && rb.getY() <= b.getY()+ b.getHeight())){
			isReachBin = true;
			
			//Compost Rubbish reach Compost Bin
          	if ( bin instanceof BinCompost && rubbishes.get(i) instanceof RubbishCompost){
          		score = score  + 10;                         //+ 10 SCORE FOR COMPOST
          		if (rubbishes.get(i) instanceof CompostAddingTime){     
          			addingTime += 5;                         //+ 5 SECOND IF THAT IS ADDING TIME ITEM
          			
          		}
          		return true;
    			//Paper Rubbish reach Paper Bin	
          	}else if ( bin instanceof BinPaper && rubbishes.get(i) instanceof RubbishPaper){
          		score = score + 13;                           //+ 13 SCORE FOR PAPER
          		if (rubbishes.get(i) instanceof PaperAddingTime){   //+ 5 SECOND IF THAT IS ADDING TIME ITEM
          			addingTime += 5;
          			
          		}
          		return true;
          	} else if (bin instanceof BinGlass && rubbishes.get(i) instanceof RubbishGlass){
          		score = score + 14;	                                   //+14 SCORE FOR GLASS
          		if (rubbishes.get(i)   instanceof GlassAddingTime){        //+ 5 SECOND IF THAT IS ADDING TIME ITEM
          			addingTime += 5;
          			
          		}
          		return true;
          	}
          	
         }
		return false;
	}
		
	//----------------------------------END FUNCTIONS RELATED OBJECTS---------------------------------------------//
	//STOP GAME
	
	//---------------------------------BEGIN LIFETIME FUNCTIONS---------------------------------------------//
	//WHEN CLICKING QUIT BUTTON
	@Override
	public void finish(){
		rubbishes.clear();
		gameOn = false;
		super.finish();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//RANDOM NEW OBJECT
		gameOn = false;

		//MOVING OBJECT
   		//movingRubbish.postDelayed(movingObjectThread, 0);
		
	}
	protected void startDisplayScoreActivity(View view){            //Start the display score top-up screen
		Intent i = new Intent(this, DisplayScoreActivity.class);
		startActivity(i);
	}
	
	public void Quit(View v){            //Quit game function
		rubbishes.clear();
		onDestroy();
	}
	
	//WAITTING FOR RESULT
	public void onActivityResult(int requestCode ,int resultCode, Intent data){
		if (requestCode == request_code ){
			if (resultCode == RESULT_OK){
				finish();
				onDestroy();
				return;
			}
		}
		
	}
	
	//-------------------------------------END LIFETIEM FUNCTIONS-----------------------------------------------------------///
	
	//-------------------------------------BEGIN FUNCTIONS RELATED TO DISPLAY------------------------------------------------//
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//DISPLAY INFORMATION
	private void displayInfomation(){
		//Current level
   		String level_str = String.format("LEVEL : %d", level);
   		TextView level_view = (TextView)findViewById(R.id.level);
   		level_view.setText(level_str);
   		
   		//Current location
   		
   		TextView location_view = (TextView)findViewById(R.id.location);
   		location_view.setText("LOCATION: " + location);
   		
   		//Player Name
   		TextView playerName_view = (TextView)findViewById(R.id.player_name);
   		playerName_view.setText("NAME : " + playerName);
   		
   		//Score
   		String score_str = String.format("SCORE : %d", score);
   		TextView score_view = (TextView)findViewById(R.id.score);
   		score_view.setText(score_str);
   		
   		//DISPLAY TIME
   		startTime = TIME ;    //START TIME COUNT BY SECOND
   		i = new Intent(this,DisplayScoreActivity.class);
   		timeCountDown.postDelayed(countDownThread,0);
		return;
	}
	
	//SET LAYOUT ONCREAT
	private void setStartLayout(){
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		Point size = new Point();
		display.getSize(size);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		//GET ID OF BINS
		ImageView compostBin = (ImageView)findViewById(R.id.binCompost);
		ImageView paperBin = (ImageView)findViewById(R.id.binPaper);
		ImageView glassBin = (ImageView)findViewById(R.id.binGlass);

		//SET LAYOUT FOR BINS
		final RelativeLayout bin_layout = (RelativeLayout) findViewById(R.id.bin_layout);
		//SET LAYOUT FOR MOVING OF OBJECT
		RelativeLayout rel_layout = (RelativeLayout) findViewById(R.id.rel_layout);
		//RelativeLayout moving_layout = (RelativeLayout)findViewById(R.id.rel_layout);
		
		//MAIN LAYOUT
		FrameLayout main_layout = (FrameLayout)findViewById(R.id.main_layout);
		//DISPLAY LAYOUT
		RelativeLayout display_layout = (RelativeLayout)findViewById(R.id.info_layout);
		
		
		//REMOVE CHILDREN
		//main_layout.removeAllViews();
		//bin_layout.removeAllViews();
		//display_layout.removeAllViews();
		
		//main_layout.add
		//SET PARAMETER FOR BIN LAYOUT
	//	main_layout.addView(bin_layout, new RelativeLayout.LayoutParams(width*2/3, height));
		
		//SET PARAMETER FOR MOVING OBJECT LAYOUT
	//	main_layout.addView(moving_layout, new RelativeLayout.LayoutParams(width * 2/3, height));
		
		//SET PARAMETER FOR DISPLAY LAYOUT
		RelativeLayout.LayoutParams display_layout_para = new RelativeLayout.LayoutParams(width/3, height);
		display_layout_para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		//main_layout.addView(display_layout,display_layout_para);
		
		//Layout parameter for 3 recycle bins
		/*RelativeLayout.LayoutParams layout_para1 = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams layout_para2 = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams layout_para3 = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);*/
		//int bin_layout_width = bin_layout.getWidth();
		//int bin_layout_height = bin_layout.getHeight();
		
		String a = String.format("%d", width);
		Log.d("S",a);
		
		//TEST GET SCREEN LAYOUT
		bin_layout.post(new Runnable() {
			RelativeLayout.LayoutParams layout_para1 = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams layout_para2 = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams layout_para3 = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			ImageView compostBin = (ImageView)findViewById(R.id.binCompost);
			ImageView paperBin = (ImageView)findViewById(R.id.binPaper);
			ImageView glassBin = (ImageView)findViewById(R.id.binGlass);
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Rect rect = new Rect();
				Window win = getWindow();					//Get the window
				win.getDecorView().getWindowVisibleDisplayFrame(rect);
				//Get the height of status bar
				int statusBarHeight = rect.top;
				//Get the height occupied by decoration content
				int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
				// Calculate titleBarHeight by deducting statusBarHeight from contentViewTop  
	            int titleBarHeight = contentViewTop - statusBarHeight; 
	            Log.i("MY", "titleHeight = " + titleBarHeight + " statusHeight = " + statusBarHeight + " contentViewTop = " + contentViewTop); 
	         // By now we got the height of titleBar & statusBar
	            // Now lets get the screen size
	            DisplayMetrics metrics = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(metrics);   
	            int screenHeight = metrics.heightPixels;
	            int screenWidth = metrics.widthPixels;
	            Log.i("MY", "Actual Screen Height = " + screenHeight + " Width = " + screenWidth);   
	 
	            // Now calculate the height that our layout can be set
            // If you know that your application doesn't have statusBar added, then don't add here also. Same applies to application bar also 
	            int layoutHeight = screenHeight - (titleBarHeight + statusBarHeight);
	            Log.i("MY", "Layout Height = " + layoutHeight);   
	 
	           // Lastly, set the height of the layout       
	          // LinearLayout.LayoutParams rootParams = (android.widget.LinearLayout.LayoutParams)bin_layout.getLayoutParams();
	          // rootParams.height = layoutHeight;
	          //.setLayoutParams(rootParams);
	          //ALIGN LAYOUT
	    		//Layout rule for 3 recycle bins
	    		layout_para1.addRule(RelativeLayout.ALIGN_LEFT);
	    		layout_para1.addRule(RelativeLayout.ALIGN_BOTTOM);
	    		
	    		layout_para2.addRule(RelativeLayout.ALIGN_LEFT);
	    		layout_para2.addRule(RelativeLayout.ALIGN_BOTTOM);
	    		
	    		layout_para3.addRule(RelativeLayout.ALIGN_LEFT);
	    		layout_para3.addRule(RelativeLayout.ALIGN_BOTTOM);
	    		
	    		//REMOVE ALL VIEW
	    		bin_layout.removeAllViews();
	    		
	    		//SET NEW POSITION FOR COMPOST BIN			
	    		layout_para1.leftMargin = screenWidth/20;
	    		bin_layout.addView(compostBin, layout_para1);
	    		layout_para1.topMargin = layoutHeight - compostBin.getHeight();
	    		
	    		//SET NEW POSITION FOR PAPER BIN			
	    		layout_para2.leftMargin  = screenWidth/4;
	    		bin_layout.addView(paperBin, layout_para2);
	    		layout_para2.topMargin = layoutHeight - paperBin.getHeight();
	    		
	    		
	    		//SET NEW POSITION FOR GLASS BIN			
	    		layout_para3.leftMargin  = screenWidth/(2);
	    		bin_layout.addView(glassBin, layout_para3);
	    		layout_para3.topMargin = layoutHeight - glassBin.getHeight();
	        } 
	      
		});
		
		
		
		
		
		
		
	}
	//----------------------------------------------END FUNCTIONS RELATED TO DISPLAY ---------------------------------------//
		
}
