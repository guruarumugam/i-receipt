package google.proj;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

import google.proj.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class rec_view extends Activity {
	/** Called when the activity is first created. */
	private TextView mDateDisplay;
	private TextView mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	private iReceipt rec;
	private EditText text[] = new EditText[4];
	private CheckBox check;
	private EditText NotesEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiptpage);
		rec = idan.rec_arr.get(getIntent().getFlags());
		ImageView image = (ImageView) findViewById(R.id.Image01);
		if (rec.getFilepath() != null)
			image.setImageResource(R.drawable.receipt);

		// capture our View elements
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (TextView) findViewById(R.id.TextView01);
		check = (CheckBox) findViewById(R.id.CheckBox01);
		NotesEditText = (EditText) findViewById(R.id.EditText01);
		
		// add a click listener to the button
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
		NotesEditText.setText(rec.getNotes());
		/*
		 * if (rec.isFlaged()) text[3].setText("Yes"); else
		 * text[3].setText("No");
		 */

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
	public void onClickPic (View view){
		Intent i = new Intent(this, bigPic.class);
		i.putExtra("image_id",R.drawable.receipt);
		startActivity(i);
	}
	

	
	public void onClick(View view) {
		IDate d;
		d = new IDate(mYear, mDay, mMonth + 1);
		rec.setRdate((IDate) d);
		rec.setStoreName(text[0].getText().toString());
		rec.setTotal(Double.parseDouble(text[1].getText().toString()));
		rec.setCategory(text[2].getText().toString());
		rec.setFlaged(check.isChecked());
		rec.setNotes(NotesEditText.getText().toString());

		saveList();
		finish();
	}

	public void saveList() {

		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					openFileOutput("RecList1.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject(idan.rec_arr);
			outputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}