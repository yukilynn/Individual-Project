package com.sherlynn.invisiblewatermarkapp;

import java.io.File;

//import android.media.ExifInterface;
import android.net.Uri;
//import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
//import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	private ImageButton camera; //gallery
	private Intent cam; //gal
	private final static int CAMERA_DATA = 0; //IMAGE_PICK = 1;
	private long lastPress;
	private Toast alert;
	private Uri fileUri; //selectedImage;
	private String capturedImageFilePath = null, fileName; //picturePath = null,  imgModel, imgMake, phoneModel, phoneManufacturer;
	private File imageFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonAssign();		
		
		/*
		 * determine that the application runs for the first time.
		 * creates the folder to store watermarks on application's first run.
		 */
		SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
		boolean firstTime = settings.getBoolean("firstTime", true);
		if (firstTime) { 
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean("firstTime", false);
		    editor.commit();
		    
		    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				Log.d("Storage error", "No SDCard");
			} else {
				File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "InvisibleWatermark");
				directory.mkdirs();
				Toast.makeText(this, "Folder has been created", Toast.LENGTH_LONG).show();
				Log.d("Storage alert", "Folder has been created");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void buttonAssign() {
		// TODO Auto-generated method stub
		//gallery = (ImageButton) findViewById(R.id.ibGallery);
		camera = (ImageButton) findViewById(R.id.ibCamera);
		
		//gallery.setOnClickListener(this);
		camera.setOnClickListener(this);
	}
	
	/*
	 * to exit the application when back button pressed twice
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	    long currentTime = System.currentTimeMillis();
	    if(currentTime - lastPress > 5000){
	        alert = Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT);
	        alert.show();
	        lastPress = currentTime;
	    } else {
	    	alert.cancel();
	        super.onBackPressed();
	    }
	}
	
	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub
		switch(button.getId()){
		
		// removed
//		case R.id.ibGallery:
//			//create Intent to start gallery activity			
//			gal = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
//            gal.setType("image/*");
//			startActivityForResult(Intent.createChooser(gal, "Select your source"), IMAGE_PICK); 
//			break;
		
		case R.id.ibCamera:
		    fileName = System.currentTimeMillis()+"";
		    
		    //create parameters for Intent with filename
		    ContentValues values = new ContentValues();
		    values.put(MediaStore.Images.Media.TITLE, fileName);
		    values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		    
		    //imageUri is the current activity attribute, define and save it for later usage
		    fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		    
		    //create Intent to start camera activity
		    cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		    cam.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(cam, CAMERA_DATA);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK){
			switch (requestCode) {
//			case IMAGE_PICK:
//				this.imageFromGallery(resultCode, data);
//				break;
			case CAMERA_DATA:
				this.imageFromCamera(resultCode, data);
				break;
			default:
				break;
			}
		}
	}
	
//	private void imageFromGallery(int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		selectedImage = data.getData();
//		String[] filePathColumn = {MediaStore.Images.Media.DATA };
//
//		Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//		cursor.moveToFirst();
//
//		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//		picturePath = cursor.getString(columnIndex); // String picturePath contains the path of selected Image
//		cursor.close();
//		
//		/* 
//		 * ensure that the image is taken from the device's camera before displaying.
//		 * determined by cross checking the model and manufacturer of the device with the metadata from the image
//		 */
//		
//		try {
//			ExifInterface exifInterface = new ExifInterface(picturePath);
//			imgModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL); //GT-I9300
//			imgMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE); //SAMSUNG
//			phoneModel = Build.MODEL; //GT-I9300
//			phoneManufacturer = Build.MANUFACTURER; //SAMSUNG
//			 
//			if ((imgMake.equalsIgnoreCase(phoneManufacturer)) && (imgModel.equalsIgnoreCase(phoneModel))) {
//				Intent display = new Intent(this, ViewImageFromGallery.class);
//				display.putExtra("bitmap", picturePath);
//				startActivity(display);
//			} else {
//				new AlertDialog.Builder(this).setTitle("Invalid").setMessage("Image is not taken from this phone!").setNeutralButton("Ok", null).setIcon(R.drawable.error).show();
//			}
//		} catch (Exception exception) {
//			Log.d("File error", "Unable to determine phone model or phone manufacturer");
//		}
//	}


	private void imageFromCamera(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		/* not in use */
//	    bmp = (Bitmap) data.getExtras().get("data");
//	
//	    Intent display = new Intent(this, ViewImageFromCamera.class);
//	    display.putExtra("photo", bmp);
//	    
//	    startActivity(display);
		
		String[] projection = {MediaStore.Images.Media.DATA}; 
		
		Cursor cursor = getContentResolver().query(fileUri, projection, null, null, null); 
		int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
		cursor.moveToFirst(); 
		
		capturedImageFilePath = cursor.getString(column_index_data);
		imageFile = new File(capturedImageFilePath);
		
		if(imageFile.exists()){
			Intent display = new Intent(this, ViewImageFromCamera.class);
			display.putExtra("photo", capturedImageFilePath);
			startActivity(display);
		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		}
	}	
}
