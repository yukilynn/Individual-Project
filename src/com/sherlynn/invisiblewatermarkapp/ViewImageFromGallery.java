package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ViewImageFromGallery extends Activity implements View.OnClickListener { //display the selected from the gallery  
	private ImageView view;
	private String gotImage;
	private Bitmap bitmapImg, resultBitmap;
	private Drawable image;
	private int orientation;
	private ExifInterface exifInterface;

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

		bitmapImg = BitmapFactory.decodeFile(gotImage, options);
		image = new BitmapDrawable(Resources.getSystem(), rotateImage(bitmapImg, gotImage));
		view.setImageDrawable(image);
 
	}
	
	private void initialise() {
		// TODO Auto-generated method stub
		view = (ImageView) findViewById(R.id.ivDisplay);
	}
	
	private Bitmap rotateImage(Bitmap bitmap, String filePath) {
	    resultBitmap = bitmap;
	    try {
	        exifInterface = new ExifInterface(gotImage);
	        orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

	        Matrix matrix = new Matrix();

	        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
	            matrix.postRotate(90);
	        }
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
	            matrix.postRotate(180);
	        }
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
	            matrix.postRotate(270);
	        }

	        // Rotate the bitmap
	        resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    }
	    catch (Exception exception) {
	        Log.d("Image error", "Could not rotate the image");
	    }
	    return resultBitmap;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}