package com.example.androidgame;

import android.widget.ImageView;

abstract class RecycleBin extends Object {

	private int cord_x,cord_y;
	private ImageView img;
	private int resouceId;
	protected RecycleBin() {
		cord_x = 0;
		cord_y = 0;
		img = null;
		// TODO Auto-generated constructor stub
	}
	
	protected RecycleBin (int x, int y){
		cord_x = x;
		cord_y = y;
	}
	
	protected RecycleBin (ImageView img){
		this.img = img;
	}
	
	public ImageView getImg() {
		return img;
	}
	public void setImg(ImageView img) {
		this.img = img;
	}
	public int getCord_x() {
		return cord_x;
	}
	public void setCord_x(int cord_x) {
		this.cord_x = cord_x;
	}
	public int getCord_y() {
		return cord_y;
	}
	public void setCord_y(int cord_y) {
		this.cord_y = cord_y;
	}

}
