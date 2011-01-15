package google.proj;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import sync.Syncer;
import google.proj.R;
import google.proj.R.id;
import google.proj.R.layout;
import google.proj.compute_receipt.MyOnItemSelectedListenerSpinner04;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class rec_view extends Activity {
	/** Called when the activity is first created. */
	private TextView mDateDisplay;
	private TextView mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String category;
	static final int DATE_DIALOG_ID = 0;
	private iReceipt rec;
	private EditText text[] = new EditText[4];
	private CheckBox check;
	// private EditText NotesEditText;
	private TextView show_notes;
	private Spinner spinner_c; // category
	private ArrayAdapter<String> adapter_c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.receiptpage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		rec = idan.rec_arr.get(getIntent().getFlags());
		ImageView myImage = (ImageView) findViewById(R.id.Image01);
		spinner_c = (Spinner) findViewById(R.id.Spinner01);
		adapter_c = new ArrayAdapter<String>(rec_view.this,
				android.R.layout.simple_spinner_item, MainActivity.cat);
		adapter_c
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_c.setAdapter(adapter_c);
		spinner_c
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner01());
		int cat_index = get_cat_index(rec.getCategory());
		spinner_c.setSelection(cat_index);
		category = rec.getCategory();
		if (rec.getFilepath() == null)
			myImage.setVisibility(View.INVISIBLE);
		/*
		 * ImageView image = (ImageView) findViewById(R.id.Image01); if
		 * (rec.getFilepath() != null)
		 * image.setImageResource(R.drawable.receipt);
		 */
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (TextView) findViewById(R.id.TextView01);
		check = (CheckBox) findViewById(R.id.CheckBox01);
		// NotesEditText = (EditText) findViewById(R.id.EditText01);
		show_notes = (TextView) findViewById(R.id.show_note);
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		/*
		 * Bundle extras = getIntent().getExtras(); if (extras== null){ return;
		 * } rec= (iReceipt) extras.getSerializable("Receipt");
		 */

		text[0] = (EditText) findViewById(R.id.EText01);
		text[1] = (EditText) findViewById(R.id.EText02);
		text[2] = (EditText) findViewById(R.id.EText03);

		text[0].setText(rec.getStoreName());
		text[1].setText(((Double) rec.getTotal()).toString());
		text[2].setText(rec.getCategory());
		check.setChecked(rec.isFlaged());
		// NotesEditText.setText(rec.getNotes());
		if ((rec.getNotes() == null) || (rec.getNotes() == "")
				|| (rec.getNotes().length() == 0))
			show_notes.setText("Click here to add note");
		else
			show_notes.setText(rec.getNotes());

		// get the current date
		mYear = rec.getRdate().getYear();
		mMonth = rec.getRdate().getMonth() - 1;
		mDay = rec.getRdate().getDay();
		// display the current date (this method is below)
		updateDisplay();

	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	/*
	 * public void onClickPic(View view) { Intent i = new Intent(rec_view.this,
	 * bigPic.class); i.setFlags(idan.rec_arr.indexOf(rec)); startActivity(i);
	 * // startActivityForResult(i, idan.rec_arr.indexOf(rec)); // Bitmap bMap =
	 * BitmapFactory.decodeFile(rec.getFilepath()); // i.putExtra("image_id",
	 * bMap); // i.putExtra("image_id","/data/rec2.jpg"); //
	 * i.putExtra("image_id",R.drawable.receipt); }
	 */
	public void onClickPic(View view) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(rec.getFilepath())),
				"image/jpg");
		startActivity(intent);
	}

	public void onClick(View view) {
		IDate d;
		d = new IDate(mYear, mMonth + 1, mDay);
		rec.setRdate((IDate) d);
		rec.setStoreName(text[0].getText().toString());
		rec.setTotal(Double.parseDouble(text[1].getText().toString()));
		// rec.setCategory(text[2].getText().toString());
		if (!(rec.getCategory().equals(category)))
			rec.setCategory(category);
		rec.setFlaged(check.isChecked());
		if (show_notes.getText() == "Click here to add note")
			rec.setNotes("");
		else
			rec.setNotes(show_notes.getText().toString());
		saveList();
		setResult(1);
		finish();
	}

	public void deleteRec(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this receipt?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								misc.Misc.deleteReceipt(rec);
								setResult(1); // EA.notifyDataSetChanged();
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void EditRecNote(View view) {
		// final TextView show_notes = (TextView) findViewById(R.id.show_note);
		final Dialog EditNoteDialog = new Dialog(this);
		EditNoteDialog.setContentView(R.layout.editnote);
		EditNoteDialog.setTitle("Edit note for current receipt");
		final EditText note = (EditText) EditNoteDialog
				.findViewById(R.id.EditNote01);
		if (show_notes.getText().equals("Click here to add note"))
			note.setText("");
		else
			note.setText(show_notes.getText());
		// b1 is ok button
		Button b1Note = (Button) EditNoteDialog.findViewById(R.id.SetNote);
		b1Note.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String new_note = note.getText().toString();
				rec.setNotes(new_note);
				String str = note.getText().toString();
				EditNoteDialog.dismiss();
				if ((str == null) || (str == "") || (str.length() == 0))
					show_notes.setText("Click here to add note");
				else
					show_notes.setText(str);
			}
		});
		// b2 is cancel button
		Button b1CancelNote = (Button) EditNoteDialog
				.findViewById(R.id.CancelNote);
		b1CancelNote.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditNoteDialog.dismiss();
			}
		});
		EditNoteDialog.show();
	}

	public void saveList() {

		// ConnectivityManager conMgr =
		// (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		rec.setUpdate();
		/*
		 * boolean connected = ( conMgr.getActiveNetworkInfo() != null &&
		 * conMgr.getActiveNetworkInfo().isAvailable() &&
		 * conMgr.getActiveNetworkInfo().isConnected() );
		 */
		// add rec to update rec list

		// check if the device is connected

		boolean connected = false;
		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting())
			connected = false;
		else {
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				connected = info.isConnected();
			}
			if (!connected && netType == ConnectivityManager.TYPE_MOBILE
					&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				connected = info.isConnected();
			}
		}

		if (connected) {
			for (iReceipt tmprr : idan.rec_arr) {
				if (rec_view.notSync(tmprr)) {
					tmprr.setSync();
					idan.sync.addtoUpdateList(tmprr);
				}
			}

			idan.sync.sendSync();
			// need to check if the sync run ok
			// for (iReceipt tmprr: idan.sync.getUpdateList()){
			// tmprr.setSync();
			// }
			idan.sync.clearUpdateList();
		}

		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					openFileOutput("RecListsave.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject(idan.rec_arr);
			outputStream.close();
			outputStream = new ObjectOutputStream(openFileOutput(
					"recIndexsave.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject((Integer) idan.receiptUniqueIndex);
			outputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static boolean notSync(iReceipt tmprr) {
		if (tmprr.getSyncdate() == null)
			return true;
		else
			return (tmprr.getUpdate().after(tmprr.getSyncdate()));
	}

	private int get_cat_index(String category) {
		int cat_index = 0;
		for (String cat : MainActivity.cat) {
			if (cat.equals(category))
				return cat_index;
			cat_index++;
		}
		return 1;
	}

	public class MyOnItemSelectedListenerSpinner01 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			category = (String) item;
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	}
}