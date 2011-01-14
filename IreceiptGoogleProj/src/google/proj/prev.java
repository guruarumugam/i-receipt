package google.proj;

/*this is the camera preview intent. notice that it is
 * fixed for the presentation. on the real device, some of the code
 * in the comment marks needs to be added in
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;


import java.io.IOException;

// ----------------------------------------------------------------------

public class prev extends Activity {
	private Preview mPreview;
	private BMcamera mCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// show instruction toast
		CharSequence text = "Touch when ready to take a picture!";
		final Toast toast = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();
		BMcamera.t=(BMcamera.t+1)%3;
		
		// Create our Preview view and set it as the content of our activity.
		try {
			mPreview = new Preview(this);
			setContentView(mPreview);
			mPreview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// mCamera.takePicture(null, null, null, null);
					//toast.setText("please wait for the OCR result...");
					mCamera.release();
					Intent i = new Intent(prev.this, compute_receipt.class);
					Receipt rec = idan.rec_arr.get(getIntent().getFlags());
					if (BMcamera.t==0) {//first
					rec.setFilepath("/data/receipts_prev/MK.jpg");// hard code the path for the
					}
													// emulator
					if (BMcamera.t==1){// second
						rec.setFilepath("/data/receipts_prev/HM.JPG");
						}
					if (BMcamera.t==2){//third
						rec.setFilepath("/data/receipts_prev/JIKO.JPG");
						}
					int index = idan.rec_arr.indexOf(rec);
					i.setFlags(index);
					startActivityForResult(i, index);
				}
			});
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
			public void onPictureTaken(byte[] imageData, Camera c) {

				/*
				* if (imageData != null) {
				* 
				* Intent mIntent = new Intent();
				* 
				* FileUtilities.StoreByteImage(mContext, imageData, 50,
				* "ImageName"); mCamera.startPreview();
				* 
				* setResult(FOTO_MODE,mIntent); finish();
				* 
				* 
				* }
				*/}
		};
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// in case we cancel in compute_receipt
		if (resultCode == 0)
			setResult(0);
		else
			setResult(1);
		finish();
	}

	// ----------------------------------------------------------------------

	class Preview extends SurfaceView implements SurfaceHolder.Callback {
		SurfaceHolder mHolder;

		// Camera mCamera;
		Preview(Context context) {
			super(context);

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it
			// where
			// to draw.
			// mCamera = Camera.open();
			mCamera = BMcamera.open();
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
				// TODO: add more exception handling logic here
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very
			// important to release it when the activity is paused.
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			// Now that the size is known, set up the camera parameters and
			// begin
			// the preview.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}
	}
}
