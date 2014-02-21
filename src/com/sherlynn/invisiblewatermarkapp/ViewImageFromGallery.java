package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ViewImageFromGallery extends Activity implements View.OnClickListener { //display the selected from the gallery  
	private ImageView view;
	private String gotImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		initialise();
		
		Bundle getImage = this.getIntent().getExtras();
		gotImage = getImage.getString("bitmap");

		view.setImageBitmap(BitmapFactory.decodeFile(gotImage));
 
	}
	
	private void initialise() {
		// TODO Auto-generated method stub
		view = (ImageView) findViewById(R.id.ivDisplay);
		if (view != null)
			   view.setImageBitmap(null);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}