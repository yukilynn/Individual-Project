package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewImageFromCamera extends Activity implements View.OnClickListener { //display the image taken from the camera  
	private ImageView view;
	private String gotImage;
	private Bitmap bitmapImg, resultBitmap;
	private Drawable image;
	private int orientation;
	private ExifInterface exifInterface;
	private Button watermark; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		initialise();
		
		Bundle getImage = this.getIntent().getExtras();
		gotImage = getImage.getString("photo");

		//view.setImageBitmap(BitmapFactory.decodeFile(image));
		
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = 4;

		bitmapImg = BitmapFactory.decodeFile(gotImage, options);
		image = new BitmapDrawable(Resources.getSystem(), rotateImage(bitmapImg, gotImage));
		view.setImageDrawable(image);
	}
	
	private void initialise() {
		// TODO Auto-generated method stub
		view = (ImageView) findViewById(R.id.ivDisplay);
		watermark = (Button) findViewById(R.id.bWatermark);
		
		watermark.setOnClickListener(this);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("Are you sure?").setMessage("Confirm leaving?")
		.setNeutralButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
                finish();
			}
		}).setIcon(R.drawable.error).show();
	}

	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub
		Intent watermarkTime= new Intent(this, WatermarkActivity.class);
		watermarkTime.putExtra("watermark", gotImage);
		startActivity(watermarkTime);
	}
}
