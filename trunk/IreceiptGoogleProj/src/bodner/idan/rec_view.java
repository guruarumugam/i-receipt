package bodner.idan;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class rec_view extends Activity {
	/** Called when the activity is first created. */
	private TextView mDateDisplay;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	private iReceipt rec;
	private EditText text[]=new EditText[4];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiptpage);
		rec=idan.rec_arr.get(getIntent().getFlags());
		ImageView image = (ImageView) findViewById(R.id.Image01);
		if (rec.getFilepath()!=null)
			image.setImageResource(R.drawable.receipt);
		
		// capture our View elements
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		/*Bundle extras = getIntent().getExtras();
        if (extras== null){
        	return;
        }
        rec= (iReceipt) extras.getSerializable("Receipt");*/
		
		text[0] = (EditText) findViewById(R.id.EText01);
		text[1] = (EditText) findViewById(R.id.EText02);
		text[2] = (EditText) findViewById(R.id.EText03);
		text[3] = (EditText) findViewById(R.id.EText04);
		
		text[0].setText(rec.getStoreName());
		text[1].setText(((Double) rec.getTotal()).toString());
		text[2].setText(rec.getCategory());
		if (rec.isFlaged()) text[3].setText("Yes");
		else text[3].setText("No");
		
		
		// get the current date
		mYear = rec.getRdate().getYear();
	    mMonth = rec.getRdate().getMonth()-1;
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
	public void onClick(View view){
			Date d;
			d=new Date(mYear,mDay,mMonth+1);
			rec.setRdate((Date)d);
			rec.setStoreName(text[0].getText().toString());
			rec.setTotal(Double.parseDouble(text[1].getText().toString()));
			rec.setCategory(text[2].getText().toString());
		//	Intent i = new Intent(this, rec_list.class);
		//	i.putExtra("Receipt", rec);
		//	setResult(10, i);
		//	saveR(rec);
			saveList();
			finish();
		}
	
	public void saveList(){

		try{
			ObjectOutputStream outputStream = new ObjectOutputStream(openFileOutput("RecList1.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject(idan.rec_arr);
			outputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	/*
	public void saveR(iReceipt r) {
		
		  try{
		    ObjectOutputStream outputStream = new ObjectOutputStream(openFileOutput(r.getplaceofsave(), Context.MODE_PRIVATE));
		    outputStream.writeObject(r);
		    outputStream.close();
		   
		  } catch (IOException ex) {
		        ex.printStackTrace();
		  }
	}
*/

}