package com.example.androidgame;

import android.widget.ImageView;

public class BinPaper extends RecycleBin {

	public BinPaper() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public BinPaper(ImageView img){
		super(img);
	}
	
	public BinPaper(int x, int y){
		super(x,y);
	}

}
