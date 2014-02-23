package com.sherlynn.invisiblewatermarkapp;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	private ImageButton gallery, camera;
	private Intent gal, cam;
	private final static int CAMERA_DATA = 0, IMAGE_PICK = 1;
	private long lastPress;
	private Toast alert;
	private Uri fileUri;
	private String capturedImageFilePath = null, fileName;
	private File imageFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonAssign();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void buttonAssign() {
		// TODO Auto-generated method stub
		gallery = (ImageButton) findViewById(R.id.ibGallery);
		camera = (ImageButton) findViewById(R.id.ibCamera);
		
		gallery.setOnClickListener(this);
		camera.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed() { // to exit the application when back button pressed twice
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
		case R.id.ibGallery:			
			gal = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //starts gallery activity
            gal.setType("image/*");
			startActivityForResult(Intent.createChooser(gal, "Select your source"), IMAGE_PICK); 
			break;
		
		case R.id.ibCamera:
		    fileName = System.currentTimeMillis()+"";
		    //create parameters for Intent with filename
		    ContentValues values = new ContentValues();
		    values.put(MediaStore.Images.Media.TITLE, fileName);
		    values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		    //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		    fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		    //create new Intent
		    cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); //open camera activity
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
			case IMAGE_PICK:
				this.imageFromGallery(resultCode, data);
				break;
			case CAMERA_DATA:
				this.imageFromCamera(resultCode, data);
				break;
			default:
				break;
			}
		}
	}
	
	private void imageFromGallery(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Uri selectedImage = data.getData();
		String[] filePathColumn = {MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex); // String picturePath contains the path of selected Image
		cursor.close();

		Intent display = new Intent(this, ViewImageFromGallery.class);
		display.putExtra("bitmap", picturePath);
		startActivity(display);
	}

	private void imageFromCamera(int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
