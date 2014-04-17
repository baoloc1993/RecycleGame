package com.example.androidgame;

import android.view.View;
import android.widget.ImageView;

abstract class Rubbish extends Object{

	protected Rubbish() {
		// TODO Auto-generated constructor stub
		super();
		cordX = 0;
		cordY = 0;
		img = null;
	}
	
	protected Rubbish (int x, int y){
		cordX = x;
		cordY = y;
	}
	
	protected Rubbish (ImageView img){
		super();
		this.img = img;
	}
	
	
	private int cordX, cordY;
	private ImageView img;
	
	
	//SETTER and GETTER
	public int getCordY() {
		return cordY;
	}
	public void setCordY(int cordY) {
		this.cordY = cordY;
	}
	public int getCordX() {
		return cordX;
	}
	public void setCordX(int cordX) {
		this.cordX = cordX;
	}
	public ImageView getImg() {
		return img;
	}
	public void setImg(ImageView img) {
		this.img = img;
	}
	public void finalize(){
		cordX = 0;
		cordY = 0;
		img = null;
	}
	
	//Set Position of Image
	//Image will allignBottom , config PaddingLeft and PaddingTop
	public void setCoordinate(float x, float y){
		img.setX(x);
		img.setY(y);
		cordX = (int)x;
		cordY = (int)y;
	}
	
   
}
