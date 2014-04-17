package com.example.androidgame;

import com.example.movingobject123.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayScoreActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.display_score);
	    // TODO Auto-generated method stub
	    
	    //CONGRATULATION TEXT
	    TextView congra_text = (TextView)findViewById(R.id.congra_text);
	    
	    //NAME TEXT
	    TextView name_text = (TextView)findViewById(R.id.name);
	    name_text.setText("Name : " + getIntent().getStringExtra("name"));
	    
	    //SCORE TEXT
	    TextView score_text = (TextView)findViewById(R.id.score);	
	    score_text.setText("Score : "+  getIntent().getStringExtra("score"));
	}

	public void onClick(View v){
		
		Intent data = new Intent();
		data.setData(Uri.parse("True"));
		setResult(RESULT_OK, data);
		finish();
		//finishActivity(requestCode);
		
	}
}
