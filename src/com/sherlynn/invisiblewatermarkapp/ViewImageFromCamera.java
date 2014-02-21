package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ViewImageFromCamera extends Activity implements View.OnClickListener { //display the image taken from the camera  
	private ImageView view;
	private String image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		initialise();
        
//		image  = getIntent().getExtras().getParcelable("photo");        
//		view.setImageBitmap(image); 
		
		Bundle getImage = this.getIntent().getExtras();
		image = getImage.getString("photo");

		view.setImageBitmap(BitmapFactory.decodeFile(image));
	}
	
	private void initialise() {
		// TODO Auto-generated method stub
		view = (ImageView) findViewById(R.id.ivDisplay);
		if (view != null)
		   view.setImageBitmap(null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
