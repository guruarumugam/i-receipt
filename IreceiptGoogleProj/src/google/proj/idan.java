package google.proj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import google.proj.R;
import java.util.ArrayList;
import java.util.List;

import misc.Misc;
import misc.Settings;
import sync.Syncer;
import android.os.Environment;
import android.provider.MediaStore;

public class idan extends Activity {
	private static int TAKE_PICTURE = 1;
	static final int PROGRESS_DIALOG = 0;
	private static final int SETTINGS = 0;
	private static final int ABOUT_IRECEIPT = 1;
	private static final int SYNC_NOW = 2;
	ProgressDialog progressDialog;
	int index = 0;
	public static int receiptUniqueIndex;
	public static Syncer sync;
	public static List<iReceipt> rec_arr;
	public static Settings settings;

	// private ImageButton new_scan, manual, receipts, stats;
	// nothing
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/*
		 * rec_arr = loadList();
		 */
		// //////////////////////////////////////////
		rec_arr = Misc.loadList(this);
		receiptUniqueIndex = Misc.loadreceiptUniqueIndex(this);

		// /////////////////////////////////////////////

		if (rec_arr == null)
			rec_arr = new ArrayList<iReceipt>();
		if (sync == null)
			sync = new Syncer(google.proj.loginpage.accountname,this);

		sync.sendSync_h();

		settings = Misc.loadSetting(this);
		if (settings == null)
			settings = new Settings();
		Misc.saveSetting(this);

		if (Misc.needToCheckDelete()) {
			Misc.makeDelete();
		}
		Misc.saveList(this);

	}

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
			CustomizeDialog customizeDialog = new CustomizeDialog(this,
					"Your expenditures this month passed your limit - "
							+ idan.settings.getMaxmonth()
							+ ". \nyour expenditures - "
							+ manual_scan.totalMonth);
			customizeDialog.show();
			// return;
		}
		if (resultCode == 2) {
			return;
		}
		if (resultCode == 1000) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("Are you sure you want to delete all receipts?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Misc.deleteAllReceipts(idan.this);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder1.create();
			alert.show();
		}
		if (resultCode == 200) { // return after save from compute receipt
			CustomizeDialog customizeDialog = new CustomizeDialog(
					this,
					"Your expenditures this month passed your limit - "
							+ idan.settings.getMaxmonth()
							+ ". \nyour expenditures - "
							+ manual_scan.totalMonth
							+ "\nYour expenditures this Period also passed your limit - "
							+ idan.settings.getMaxUniquely()
							+ ". \nyour expenditures - "
							+ manual_scan.totalPeriod);
			customizeDialog.show();
			// return;
		}
		if (resultCode == 300) { // return after save from compute receipt
			CustomizeDialog customizeDialog = new CustomizeDialog(this,
					"Your expenditures this Period passed your limit - "
							+ idan.settings.getMaxUniquely()
							+ ". \nyour expenditures - "
							+ manual_scan.totalPeriod);
			customizeDialog.show();
			// return;
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

	public void receipts_handler(View view) {
		Intent i = new Intent(idan.this, listview.class);
		startActivity(i);
	}

	public void statistics_handler(View view) {
		Intent i = new Intent(idan.this, statistics.class);
		startActivity(i);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////replace with Misc
	/*
	 * public ArrayList<iReceipt> loadList() { try { ObjectInputStream
	 * inputStream = new ObjectInputStream( openFileInput("RecListsave.tmp"));
	 * 
	 * @SuppressWarnings("unchecked") ArrayList<iReceipt> rec_arr_tmp =
	 * (ArrayList<iReceipt>) inputStream .readObject(); inputStream.close();
	 * 
	 * inputStream = new ObjectInputStream( openFileInput("recIndexsave.tmp"));
	 * Integer tmp = (Integer) inputStream.readObject(); inputStream.close();
	 * receiptUniqueIndex = tmp;
	 * 
	 * return rec_arr_tmp;
	 * 
	 * } catch (IOException ex) { ex.printStackTrace(); return null; } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); return null; } }
	 */
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, SETTINGS, 0, "Settings");
		menu.add(Menu.NONE, ABOUT_IRECEIPT, 1, "About iReceipt");
		menu.add(Menu.NONE, SYNC_NOW, 2, "Sync now");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case SETTINGS:
			Intent settingsActivity = new Intent(getBaseContext(),
					Preferences.class);
			startActivityForResult(settingsActivity, index);
			/*
			 * i = new Intent(idan.this, HelloPreferences.class);
			 * startActivity(i);
			 */
			break;
		case ABOUT_IRECEIPT:
			i = new Intent(idan.this, aboutireceipt.class);
			startActivity(i);
			break;
		case SYNC_NOW:
			sync.syncNow(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}