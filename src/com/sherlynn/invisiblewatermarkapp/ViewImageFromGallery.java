package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ViewImageFromGallery extends Activity implements View.OnClickListener { //display the selected from the gallery  
	private ImageView view;
	private String gotImage;
	private Bitmap bitmap;
	private Drawable image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		initialise();
		
		Bundle getImage = this.getIntent().getExtras();
		gotImage = getImage.getString("bitmap");

		//view.setImageBitmap(BitmapFactory.decodeFile(gotImage));
		
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = 4;

		bitmap = BitmapFactory.decodeFile(gotImage, options);
		image  = new BitmapDrawable(Resources.getSystem(), bitmap);
		view.setImageDrawable(image);
 
	}
	
	private void initialise() {
		// TODO Auto-generated method stub
		view = (ImageView) findViewById(R.id.ivDisplay);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}