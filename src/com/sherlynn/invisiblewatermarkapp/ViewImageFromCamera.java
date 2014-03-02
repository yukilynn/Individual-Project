package com.sherlynn.invisiblewatermarkapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ViewImageFromCamera extends Activity implements View.OnClickListener { //display the image taken from the camera  
	private ImageView view;
	private String gotImage;
	private Bitmap bitmapImg, resultBitmap;
	private Drawable image;
	private int orientation;
	private ExifInterface exifInterface;
	private Button setup, watermark; 
	private Intent gal; //, watermarkIntent;
	private final static int IMAGE_PICK = 1;
	//private Uri selectedImage;
	private String picturePath = null;

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
		setup = (Button) findViewById(R.id.bSetup);
		watermark = (Button) findViewById(R.id.bWatermark);
		
		setup.setOnClickListener(this);
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
		switch(button.getId()){
		
		case R.id.bSetup:
			gal = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
            gal.setType("image/*");
			startActivityForResult(Intent.createChooser(gal, "Select your source"), IMAGE_PICK); 
			break;
		
		case R.id.bWatermark:
//			watermarkIntent = new Intent(this, WatermarkActivity.class);
//			watermarkIntent.putExtra("photo", gotImage);
//			startActivity(watermarkIntent);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case IMAGE_PICK:
				this.watermarkFromGallery(resultCode, data);
				break;
			default:
				break;
			}
		}
	}
	
	private void watermarkFromGallery(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//selectedImage = data.getData();
		String[] filePathColumn = {MediaStore.Images.Media.DATA };
		
		// display the watermark folder
		Cursor cursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				filePathColumn, 
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%/InvisibleWatermark/%"},  
                null);

		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		picturePath = cursor.getString(columnIndex);
		cursor.close();
		
		Intent display = new Intent(this, ViewImageFromGallery.class);
		display.putExtra("bitmap", picturePath);
		startActivity(display);
	}
}
