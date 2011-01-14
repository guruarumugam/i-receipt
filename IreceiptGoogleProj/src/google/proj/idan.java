package google.proj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import google.proj.R;
import java.util.ArrayList;
import java.util.List;
import sync.Syncer;
import sync.tset;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

public class idan extends Activity {
	private static int TAKE_PICTURE = 1;
	static final int PROGRESS_DIALOG = 0;
	private static final int SETTINGS = 0;
	private static final int ABOUT_IRECEIPT = 1;
	ProgressDialog progressDialog;
	int index = 0;
	public static int receiptUniqueIndex;
	protected static Syncer sync;
	public static List<iReceipt> rec_arr;

	// private ImageButton new_scan, manual, receipts, stats;
	// nothing
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		/*
		 * Button button = (Button) findViewById(R.id.sync);
		 * button.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { showDialog(PROGRESS_DIALOG); } });
		 */

		rec_arr = loadList();
		// rec_arr.clear(); // ///////////להוריד לפני שמעלים לנקסוס1, טוב רק
		// לאפליקציה
		// במחשב*******************************************
		if (rec_arr == null)
			rec_arr = new ArrayList<iReceipt>();
		if (sync == null)
			sync = new Syncer();
		sync.sendSync(loginpage.accountname);

		/*
		 * iReceipt rec1 = (new iReceipt(new IDate(2010, 12, 8), "LACOSTE",
		 * 498.29, "Shopping", true, true, "",
		 * "/data/receipts_prev/IMG_7953.JPG")); iReceipt rec2 = (new
		 * iReceipt(new IDate(2010, 12, 10), "L'OCCITANE", 49.22, "Shopping",
		 * false, true, "", "/data/receipts_prev/IMG_7949.JPG")); iReceipt rec3
		 * = (new iReceipt(new IDate(2010, 12, 10), "QUIZNO'S Food Court",
		 * 10.63, "Dining", false, true, "",
		 * "/data/receipts_prev/IMG_7950.JPG")); // iReceipt rec4 = (new
		 * iReceipt(new IDate(2010, 12, 9), // "Celebrate Today", 10.30,
		 * "Dining", false, true, "", // "/data/receipts_prev/IMG_7941.JPG"));
		 * iReceipt rec5 = (new iReceipt(new IDate(2010, 12, 9), "Reebok",
		 * 95.40, "Shopping", true, true, "",
		 * "/data/receipts_prev/IMG_7941.JPG")); // iReceipt rec6 = (new
		 * iReceipt(new IDate(2001, 11, 5), "DUFRITAL SPA", // 21.10,
		 * "Shopping", false, true, "", // "/data/receipts_prev/IMG_7942.JPG"));
		 * iReceipt rec7 = (new iReceipt(new IDate(2010, 9, 26),
		 * "AMERICAN EAGLE", 39.50, "Shopping", false, true, "Great store!",
		 * "/data/receipts_prev/IMG_7944.JPG")); iReceipt rec8 = (new
		 * iReceipt(new IDate(2010, 9, 25), "Macys", 71.54, "Shopping", true,
		 * true, "", "/data/receipts_prev/IMG_7945.JPG")); iReceipt rec9 = (new
		 * iReceipt(new IDate(2010, 9, 23), "H&M", 11.90, "Shopping", false,
		 * true, "2 shirts", "/data/receipts_prev/IMG_7948.JPG")); iReceipt
		 * rec10 = (new iReceipt(new IDate(2010, 12, 9), "THE BODY SHOP", 40.47,
		 * "Shopping", false, true, "", "/data/receipts_prev/IMG_7935.JPG"));
		 * iReceipt rec11 = (new iReceipt(new IDate(2008, 2, 11), "GETGO",
		 * 47.27, "Car", false, true, "", "/data/receipts_prev/rec1.jpg"));
		 * iReceipt rec12 = (new iReceipt(new IDate(2010, 12, 9), "adidas",
		 * 165.08, "Shopping", false, true, "Orlando, FL 32809",
		 * "/data/receipts_prev/IMG_7936.JPG")); // iReceipt rec13 = (new
		 * iReceipt(new IDate(2010, 11, 16), "Fieldpine", // 4.50, "Dining",
		 * false, true, "", "/data/rec111.JPG")); iReceipt rec14 = (new
		 * iReceipt(new IDate(2010, 12, 7), "MK MS Bev Wagon", 5.25, "Dining",
		 * false, true, "", "/data/receipts_prev/IMG_7932.JPG")); iReceipt rec15
		 * = (new iReceipt(new IDate(2010, 12, 11), "Orlando Airport O Store",
		 * 21.30, "Shopping", false, true, "i came with the Car",
		 * "/data/receipts_prev/IMG_7940.JPG")); iReceipt rec16 = (new
		 * iReceipt(new IDate(2010, 12, 5), "Duty Free Israel", 340.60,
		 * "Shopping", false, true, "", "/data/receipts_prev/IMG_7951.JPG"));
		 * iReceipt rec17 = (new iReceipt(new IDate(2010, 12, 7), "Boma", 40.45,
		 * "Dining", false, true, "", "/data/receipts_prev/IMG_7955.JPG"));
		 * iReceipt rec18 = (new iReceipt(new IDate(2010, 12, 6),
		 * "BBVA Bancomer", 500.00, "Shopping", false, true, "",
		 * "/data/receipts_prev/IMG_7956.JPG")); iReceipt rec19 = (new
		 * iReceipt(new IDate(2010, 12, 7), "Jiko", 112.10, "Dining", false,
		 * true, "", "/data/receipts_prev/IMG_7957.JPG")); iReceipt rec20 = (new
		 * iReceipt(new IDate(2010, 12, 2), "PIZZA ROLANDI", 1607.10, "Dining",
		 * false, true, "", "/data/receipts_prev/IMG_7960.JPG")); iReceipt rec21
		 * = (new iReceipt(new IDate(2010, 12, 6), "HOT DOGS ALL DRESSED",
		 * 47.00, "Dining", false, true, "",
		 * "/data/receipts_prev/IMG_7961.JPG")); iReceipt rec22 = (new
		 * iReceipt(new IDate(2010, 12, 10), "ZAZA", 3.63, "Dining", false,
		 * true, "", "/data/receipts_prev/IMG_7929.JPG")); iReceipt rec23 = (new
		 * iReceipt(new IDate(2008, 12, 30), "Saugus", 19.92, "Car", false,
		 * true, "", "/data/receipts_prev/2.jpg")); iReceipt rec24 = (new
		 * iReceipt(new IDate(2009, 04, 23), "Fastfuel", 24.36, "Car", false,
		 * true, "Car & gas Shop", "/data/receipts_prev/3.jpg"));
		 * 
		 * rec_arr.add(rec1); rec_arr.add(rec2); rec_arr.add(rec3); //
		 * rec_arr.add(rec4); rec_arr.add(rec5); // rec_arr.add(rec6);
		 * rec_arr.add(rec7); rec_arr.add(rec8); rec_arr.add(rec9);
		 * rec_arr.add(rec10); rec_arr.add(rec11); rec_arr.add(rec12); //
		 * rec_arr.add(rec13); rec_arr.add(rec14); rec_arr.add(rec15);
		 * rec_arr.add(rec16); rec_arr.add(rec17); rec_arr.add(rec18);
		 * rec_arr.add(rec19); rec_arr.add(rec20); rec_arr.add(rec21);
		 * rec_arr.add(rec22); rec_arr.add(rec23); rec_arr.add(rec24);
		 */
	}

	/*
	 * String tmp=loadHandler(); if (tmp==null){//never write before;
	 * 
	 * //need to write for the first time ito Disk saveHandler(); saveList();
	 * scan=false; receitnumber=Integer.parseInt(tmp); }
	 * 
	 * if (scan){// call first time to this application
	 * 
	 * scan=false; mylistname=loadList();
	 * 
	 * // loop that read al receipt and add them to list for (int i=0;
	 * i<mylistname.size();i++){ rec_arr.add(loadR(mylistname.get(i))); } }
	 */
	/*
	 * public void new_scan_handler(View view) { iReceipt r = new iReceipt(); //
	 * Intent i = new Intent(idan.this, prev.class); Intent i = new
	 * Intent(idan.this, CameraPreview.class); rec_arr.add(r); int index =
	 * rec_arr.indexOf(r); i.setFlags(index); startActivityForResult(i, index);
	 * }
	 */

	public void new_scan_handler(View view) {
		receiptUniqueIndex++;
		iReceipt r = new iReceipt(receiptUniqueIndex);
		rec_arr.add(r);
		index = rec_arr.indexOf(r);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = new File(Environment.getExternalStorageDirectory(),
				receiptUniqueIndex + "ireceipt.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		r.setFilepath(photo.toString());
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	public void manual_scan_handler(View view) {
		receiptUniqueIndex++;
		iReceipt r = new iReceipt(receiptUniqueIndex);
		Intent i = new Intent(idan.this, manual_scan.class);
		rec_arr.add(r);
		index = rec_arr.indexOf(r);
		i.setFlags(index);
		startActivityForResult(i, index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// in case we cancel in compute_receipt

		if (resultCode == 100) { // return after save from compute receipt
			return;
		}

		if (requestCode == TAKE_PICTURE)
			if (resultCode != Activity.RESULT_OK) {
				rec_arr.remove(index);
				receiptUniqueIndex--;
				return;
			} else {
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				// Limit the filesize since 5MP pictures will kill you RAM
				iReceipt r = rec_arr.get(index);
				bitmapOptions.inSampleSize = 3;
				Bitmap imgBitmap = BitmapFactory.decodeFile(r.getFilepath(),
						bitmapOptions);
				try {
					imgBitmap.compress(Bitmap.CompressFormat.JPEG, 88,
							new FileOutputStream(r.getFilepath()));
				} catch (FileNotFoundException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}

				Intent i = new Intent(idan.this, compute_receipt.class);
				i.setFlags(index);
				startActivityForResult(i, index);
			}

		if (resultCode == 0) {
			rec_arr.remove(requestCode);
			receiptUniqueIndex--;
		}
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // in case we cancel in compute_receipt if
	 * (requestCode == TAKE_PICTURE) if (resultCode != Activity.RESULT_OK) {
	 * rec_arr.remove(requestCode); return; } else { Intent i = new
	 * Intent(idan.this, compute_receipt.class); i.setFlags(index);
	 * startActivityForResult(i, index); }
	 * 
	 * if (resultCode == 0) { rec_arr.remove(requestCode); } }
	 */

	public void receipts_handler(View view) {
		Intent i = new Intent(idan.this, listview.class);
		startActivity(i);
	}

	public void statistics_handler(View view) {
		Intent i = new Intent(idan.this, statistics.class);
		startActivity(i);
	}

	/*
	 * public static String getnewplaceforsave(){
	 * 
	 * String ret=String.valueOf(idan.receitnumber-1)+".tmp"; receitnumber++;
	 * return ret;
	 * 
	 * }
	 */

	/*
	 * public String loadHandler() { String str[]; String ret=null; try{
	 * ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput("receitnumber.tmp"));
	 * str=(String[])inputStream.readObject(); ret=str[0];
	 * 
	 * inputStream.close(); } catch (IOException ex) { return null; } catch
	 * (ClassNotFoundException e) { // TODO Auto-generated catch block return
	 * null; } return ret; }
	 * 
	 * @SuppressWarnings("unchecked") public ArrayList<String> loadList() {
	 * ArrayList<String> ret=null; try{ ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput("arrayList.tmp"));
	 * ret=(ArrayList<String>)inputStream.readObject();
	 * 
	 * inputStream.close(); } catch (IOException ex) { ex.printStackTrace(); }
	 * catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return ret; } public iReceipt loadR(String place){
	 * 
	 * 
	 * iReceipt ret=null; try{ ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput(place));
	 * ret=(iReceipt)inputStream.readObject();
	 * 
	 * inputStream.close(); } catch (IOException ex) { ex.printStackTrace(); }
	 * catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return ret; }
	 * 
	 * //write for the first time reciept number static file public void
	 * saveHandler() { String str[]=new String[1];
	 * str[0]=String.valueOf(receitnumber); try{ ObjectOutputStream outputStream
	 * = new ObjectOutputStream(openFileOutput("receitnumber.tmp",
	 * Context.MODE_PRIVATE)); outputStream.writeObject(str);
	 * outputStream.close();
	 * 
	 * } catch (IOException ex) { ex.printStackTrace(); } }
	 */

	/*
	 * protected Dialog onCreateDialog(int id) { switch (id) { case
	 * PROGRESS_DIALOG: progressDialog = new ProgressDialog(idan.this);
	 * progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 * progressDialog.setMessage("Sync with web app..."); progressThread = new
	 * ProgressThread(handler); progressThread.start(); return progressDialog;
	 * default: return null; } }
	 */

	// Define the Handler that receives messages from the thread and update the
	// progress
	/*
	 * final Handler handler = new Handler() { public void handleMessage(Message
	 * msg) { int total = msg.getData().getInt("total");
	 * progressDialog.setProgress(total); if (total >= 100) {
	 * dismissDialog(PROGRESS_DIALOG);
	 * progressThread.setState(ProgressThread.STATE_DONE); } } };
	 */

	/** Nested class that performs progress calculations (counting) */
	/*
	 * private class ProgressThread extends Thread { Handler mHandler; final
	 * static int STATE_DONE = 0; final static int STATE_RUNNING = 1; int
	 * mState; int total;
	 * 
	 * ProgressThread(Handler h) { mHandler = h; }
	 * 
	 * public void run() { mState = STATE_RUNNING; total = 0; while (mState ==
	 * STATE_RUNNING) { try { Thread.sleep(100); } catch (InterruptedException
	 * e) { Log.e("ERROR", "Thread Interrupted"); } Message msg =
	 * mHandler.obtainMessage(); Bundle b = new Bundle(); b.putInt("total",
	 * total); msg.setData(b); mHandler.sendMessage(msg); total++; } }
	 * 
	 * //sets the current state for the thread, used to stop the thread
	 * 
	 * public void setState(int state) { mState = state; } }
	 */

	public ArrayList<iReceipt> loadList() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					openFileInput("RecListsave.tmp"));
			@SuppressWarnings("unchecked")
			ArrayList<iReceipt> rec_arr_tmp = (ArrayList<iReceipt>) inputStream
					.readObject();
			inputStream.close();

			inputStream = new ObjectInputStream(
					openFileInput("recIndexsave.tmp"));
			Integer tmp = (Integer) inputStream.readObject();
			inputStream.close();
			receiptUniqueIndex = tmp;

			return rec_arr_tmp;

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void crash() {
		System.err.println(2 / 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, SETTINGS, 0, "Settings");
		menu.add(Menu.NONE, ABOUT_IRECEIPT, 1, "About iReceipt");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case SETTINGS:
			i = new Intent(idan.this, settings.class);
			startActivity(i);
			break;
		case ABOUT_IRECEIPT:
			i = new Intent(idan.this, aboutireceipt.class);
			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}