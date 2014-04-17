package com.example.androidgame;

import android.widget.ImageView;

public class BinCompost extends RecycleBin{

	public BinCompost() {
		// TODO Auto-generated constructor stub
		super();
	}

	public BinCompost(ImageView findViewById) {
		// TODO Auto-generated constructor stub
		super(findViewById);
	}
	
	public BinCompost(int x, int y){
		super(x,y);
	}


}
