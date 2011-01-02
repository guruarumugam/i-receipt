package google.proj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

// ----------------------------------------------------------------------

public class CameraPreview extends Activity {  
	private static final String TAG = "CameraPreview";
    private Preview mPreview;
    private Receipt rec=null;
   private int index1;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        System.out.println("hoera");
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rec = idan.rec_arr.get(getIntent().getFlags());
        final int index = idan.rec_arr.indexOf(rec);
        index1=index;
        
		CharSequence text = "Touch when ready to take a picture!";
		final Toast toast = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();

        // Create our Preview view and set it as the content of our activity.
        try{
            mPreview = new Preview(this);
            setContentView(mPreview);
            System.out.println("hoera");
            mPreview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mPreview.mCamera.takePicture(shutterCallback, rawCallback,
							jpegCallback);
				}
			});
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }
            
            
    	ShutterCallback shutterCallback = new ShutterCallback() {
    		public void onShutter() {
    			Log.d(TAG, "onShutter'd");
    		}
    	};
        	
        	PictureCallback rawCallback = new PictureCallback() {
        		public void onPictureTaken(byte[] data, Camera camera) {
        			Log.d(TAG, "onPictureTaken - raw");
        		}
        	};

        	/** Handles data for jpeg picture */
        	PictureCallback jpegCallback = new PictureCallback() {
        		public void onPictureTaken(byte[] data, Camera camera) {
        			FileOutputStream outStream = null;
        			try {
        				// write to local sandbox file system
        				// outStream =
        				// CameraDemo.this.openFileOutput(String.format("%d.jpg",
        				// System.currentTimeMillis()), 0);
        				// Or write to sdcard
        				outStream = new FileOutputStream(String.format(
        						"/sdcard/ireceipt_image%d.jpg", index1));
        				outStream.write(data);
        				outStream.close();
        				
        				rec.setFilepath(String.format(
        						"/sdcard/ireceipt_image%d.jpg", index1));   				
        				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
        			} catch (FileNotFoundException e) {
        				e.printStackTrace();
        			} catch (IOException e) {
        				e.printStackTrace();
        			} finally {
        				Intent i = new Intent(CameraPreview.this, compute_receipt.class);
        				i.setFlags(index1);
        				startActivityForResult(i, index1);
        			}
        			Log.d(TAG, "onPictureTaken - jpeg");
        		}
        	};

        


        	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        		// in case we cancel in compute_receipt
        		if (resultCode == 0)
        			setResult(0);
        		else
        			setResult(1);
        		finish();
        	}

}

// ----------------------------------------------------------------------

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;

    Preview(Context context) {
        super(context);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        mCamera = Camera.open();
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
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        int ww=parameters.getSupportedPreviewSizes().get(4).width;
        int hh=parameters.getSupportedPreviewSizes().get(4).height;
        //parameters.set("rotation", 90);
        //parameters.set("rotation", 90);
        //parameters.set("orientation", "portrait");
        //parameters.setPreviewSize(ww,hh);
       // parameters.setPreviewSize(800,480);
        //parameters.set("orientation", "portrait");
        
        //parameters.setPictureSize(512, 384);
       // parameters.set("orientation", "portrait");
       // parameters.set("rotation", 90);
        // p.setPreviewSize(640, 480);

        //Camera.Size s = parameters.getSupportedPreviewSizes().get(0);
       // parameters.setPreviewSize( s.width,s.height );

       // parameters.setPictureFormat(PixelFormat.JPEG);
      //  parameters.set("flash-mode", "auto");
        //parameters.set("rotation", 90);
        //parameters.set("orientation", "portrait");
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

}