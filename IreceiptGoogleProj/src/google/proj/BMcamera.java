package google.proj;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

public class BMcamera {
 
 private static final String LOG_TAG = "SocketCamera:";
 
 public static int t=-1;
 
 static private BMcamera bmcamera;
 private CameraCapture capture;
 private Camera parametersCamera;
 private SurfaceHolder surfaceHolder;
 private static String bmFilePath[]=new String[30];
 
 private final boolean preserveAspectRatio = true;
 private final Paint paint = new Paint();

 
 private int width = 240;
 private int height = 320;
 private Rect bounds = new Rect(0, 0, width, height);
 
 private BMcamera() {
  //Just used so that we can pass Camera.Paramters in getters and setters
  parametersCamera = Camera.open();
  // first "movie"

 }
 
 static public BMcamera open()
 {
	 if (bmcamera==null){
  
   bmcamera = new BMcamera();}
	  if ( t==0){
		  for (int i = 0; i < bmFilePath.length; i++) {
			  bmFilePath[i]="/data/receipts_prev/0/"+(1+i)+".JPG";
		}
		 
	  }
	  //second movie
	  if ( t==1){
		  for (int i = 0; i < bmFilePath.length; i++) {
			  bmFilePath[i]="/data/receipts_prev/1/"+(1+i)+".JPG";
		}
		  
	  }
	  //third movie
	  if ( t==2){
		  for (int i = 0; i < bmFilePath.length; i++) {
			  bmFilePath[i]="/data/receipts_prev/2/"+(1+i)+".JPG";
		}

	 }
	 
  Log.i(LOG_TAG, "Creating Socket Camera");
  return bmcamera;
 }

 public void startPreview() {
  capture = new CameraCapture();
  capture.setCapturing(true);
  capture.start(); 
  Log.i(LOG_TAG, "Starting Socket Camera");
  
 }
 
 public void stopPreview(){
  capture.setCapturing(false);
  Log.i(LOG_TAG, "Stopping Socket Camera");
 }
 
 public void setPreviewDisplay(SurfaceHolder surfaceHolder) throws IOException {
  this.surfaceHolder = surfaceHolder;
 }
 
 public void setParameters(Camera.Parameters parameters) {
  //Bit of a hack so the interface looks like that of
  Log.i(LOG_TAG, "Setting Socket Camera parameters");
  parametersCamera.setParameters(parameters);
  Size size = parameters.getPreviewSize();
  bounds = new Rect(0, 0, size.width, size.height);
 }
 public Camera.Parameters getParameters() { 
  Log.i(LOG_TAG, "Getting Socket Camera parameters");
  return parametersCamera.getParameters(); 
 } 
 
 public void release() {
  Log.i(LOG_TAG, "Releasing Socket Camera parameters");
  //TODO need to implement this function
 } 
 
 public final void takePicture (Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback postview, Camera.PictureCallback jpeg){
	 //this is a fake method. in to feet Camera class
 }
 
 
 private class CameraCapture extends Thread  {
  private int place=0;//not for general use, just for method run
  private boolean capturing = false;
  
  public boolean isCapturing() {
   return capturing;
  }

  public void setCapturing(boolean capturing) {
   this.capturing = capturing;
  }

  @Override
     public void run() {
         while (capturing) {
             Canvas c = null;
             try {
                 c = surfaceHolder.lockCanvas(null);
                 synchronized (surfaceHolder) {
               try {
            	  Bitmap bitmap = BitmapFactory.decodeFile(bmFilePath[place++]);
            	  if (place>29)
            		  place=0;
                
                //render it to canvas, scaling if necessary
                if (
                  bounds.right == bitmap.getWidth() &&
                  bounds.bottom == bitmap.getHeight()) {
                 c.drawBitmap(bitmap, 0, 0, null);
                } else {
                 Rect dest;
                 if (preserveAspectRatio) {
                  dest = new Rect(bounds);
                  dest.bottom = bitmap.getHeight() * bounds.right / bitmap.getWidth();
                  dest.offset(0, (bounds.bottom - dest.bottom)/2);
                 } else {
                  dest = bounds;
                 }
                 if (c != null)
                 { 
                  c.drawBitmap(bitmap, null, dest, paint);
                 }
                }
 
               } catch (RuntimeException e) {
                e.printStackTrace();
                }finally {
               }
                 }
             } catch (Exception e) {
              e.printStackTrace();
             } finally {
            
                 // do this in a finally so that if an exception is thrown
                 // during the above, we don't leave the Surface in an
                 // inconsistent state
                 if (c != null) {
                     surfaceHolder.unlockCanvasAndPost(c);
                 }
             }
         }
         Log.i(LOG_TAG, "Socket Camera capture stopped");
     }
 }

}