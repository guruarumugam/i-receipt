package google.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import google.proj.R;
import google.proj.R.id;
import google.proj.R.layout;
import google.proj.compute_receipt.MyOnItemSelectedListenerSpinner04;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;

public class searchReceipt extends Activity {

	public static List<iReceipt> rec_arr_search;
	private RadioButton radioBusiness, radioDate, radioPrice, radioCategory;
	private EditText Business, PriceFrom, PriceTo;
	private Button searchButton;
	private Spinner spinner_c;
	private ArrayAdapter<String> adapter_c;
	public String str_cat;
	public int mYear, mDay, mMonth, wait = 0;
	public IDate date1, date2;
	private TextView DateFrom, DateTo;
	private iReceipt rec;

	DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			date1 = new IDate(year, monthOfYear + 1, dayOfMonth);
			radioDate.setChecked(true);
			updateDisplay(R.id.pickDateFrom);
		}
	};
	DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			date2 = new IDate(year, monthOfYear + 1, dayOfMonth);
			radioDate.setChecked(true);
			updateDisplay(R.id.pickDateTo);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchreceipt);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		searchButton = (Button) findViewById(R.id.searchButton);
		PriceFrom = (EditText) findViewById(R.id.EditText05);
		PriceTo = (EditText) findViewById(R.id.EditText06);
		radioDate = (RadioButton) findViewById(R.id.RadioButton01);
		radioPrice = (RadioButton) findViewById(R.id.RadioButton02);
		radioCategory = (RadioButton) findViewById(R.id.RadioButton03);
		DateFrom = (TextView) findViewById(R.id.pickDateFrom);
		DateTo = (TextView) findViewById(R.id.pickDateTo);
		rec_arr_search = new ArrayList<iReceipt>();
		updateDisplay(0);
		PriceFrom.setText("0");
		PriceTo.setText("100");

		DateTo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				showDialog(1);
			}
		});

		DateFrom.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				showDialog(0);
			}
		});

		spinner_c = (Spinner) findViewById(R.id.Spinner01);
		adapter_c = new ArrayAdapter<String>(searchReceipt.this,
				android.R.layout.simple_spinner_item, MainActivity.cat);
		adapter_c
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_c.setAdapter(adapter_c);
		spinner_c
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner04());
		// ///////////////////////////////////////////////////////////
		/*
		 * PriceTo.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { radioPrice.setChecked(true);
		 * } }); PriceFrom.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { radioPrice.setChecked(true);
		 * } });
		 */
		/*
		 * PriceFrom.setOnEditorActionListener(new OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView v, int actionId,
		 * KeyEvent event) { radioPrice.setChecked(true); return false; } });
		 * PriceTo.setOnEditorActionListener(new OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView v, int actionId,
		 * KeyEvent event) { radioPrice.setChecked(true); return false; } });
		 */
		PriceTo.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				radioPrice.setChecked(true);
			}
		});
		PriceFrom.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				radioPrice.setChecked(true);
			}
		});
		// //////////////////////////////////////////////////////////
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new DatePickerDialog(this, fromDateSetListener, mYear,
					mMonth, mDay);
		case 1:
			return new DatePickerDialog(this, toDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	public void search(View view) {
		if (radioDate.isChecked()) {
			DateHandler();
		}
		if (radioPrice.isChecked())
			PriceHandler();
		if (radioCategory.isChecked())
			CategoryHandler();
		if (wait == 0) {
			Intent i = new Intent(searchReceipt.this, listview.class);
			/*
			 * i.setFlags(5); startActivity(i);
			 */
			setResult(1);
		}
		wait = 0;
		finish();
	}

	/*
	 * iReceipt r = new iReceipt(); Intent i = new Intent(idan.this,
	 * prev.class); rec_arr.add(r); int index = rec_arr.indexOf(r);
	 * i.setFlags(index); startActivityForResult(i, index);
	 */

	/*
	 * public void BusinessHandler() { String str1, str2; str1 =
	 * Business.getText().toString(); for (int i = 0; i < idan.rec_arr.size();
	 * i++) { str2 = idan.rec_arr.get(i).getStoreName(); // only for the //
	 * debuging!!! if (idan.rec_arr.get(i).getStoreName().toLowerCase()
	 * .equals(str1.toLowerCase())) rec_arr_search.add(idan.rec_arr.get(i)); } }
	 */

	public void DateHandler() {
		rec_arr_search.clear();
		IDate date0; // only for debuging!!
		if ((date1 == null) || (date2 == null) || (date1.compareTo(date2) < 0)) {// error!!!
			wait = 1;
			CustomizeDialog customizeDialog = new CustomizeDialog(this,
					"Invalid dates");
			customizeDialog.show();
		} else {
			wait = 0;
			for (iReceipt rec : idan.rec_arr) {
				date0 = rec.getRdate(); // only for debuging!!
				if ((date0.compareTo(date1) <= 0)
						&& (date0.compareTo(date2) >= 0))
					rec_arr_search.add(rec);
			}
		}
	}

	public void PriceHandler() {
		rec_arr_search.clear();
		int price1, price2;
		// Double price = 0.0; // only for debug!!!
		price1 = Integer.parseInt(PriceFrom.getText().toString());
		price2 = Integer.parseInt(PriceTo.getText().toString());
		if ((price1 > price2) || (price1 < 0)) {
			wait = 1;
			CustomizeDialog customizeDialog = new CustomizeDialog(this,
					"Invalid prices");
			customizeDialog.show();
		} else {
			wait = 0;
			for (iReceipt rec : idan.rec_arr) {
				// price = rec.getTotal(); // only for the debuging!!!
				if ((rec.getTotal() >= price1) && (rec.getTotal() <= price2))
					rec_arr_search.add(rec);
			}
		}
	}

	public void CategoryHandler() {
		rec_arr_search.clear();
		for (iReceipt rec : idan.rec_arr) {
			if (rec.getCategory().equals(str_cat))
				rec_arr_search.add(rec);
		}
	}

	// handler for spinner 04
	public class MyOnItemSelectedListenerSpinner04 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			radioCategory.setChecked(true);
			str_cat = (String) item;
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	private void updateDisplay(int id) {
		if (id == R.id.pickDateFrom) {
			if ((mMonth > 8) && (mDay > 9))
				DateFrom.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append(mDay).append("-").append(mYear)
						.append(" "));
			if ((mMonth < 9) && (mDay < 10))
				DateFrom.setText(new StringBuilder().append("0")
						.append(mMonth + 1).append("-").append("0")
						.append(mDay).append("-").append(mYear).append(" "));
			if ((mMonth < 9) && (mDay > 9))
				DateFrom.setText(new StringBuilder().append("0")
						.append(mMonth + 1).append("-").append(mDay)
						.append("-").append(mYear).append(" "));
			if ((mMonth > 8) && (mDay < 10))
				DateFrom.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append("0").append(mDay).append("-")
						.append(mYear).append(" "));
		}
		if (id == R.id.pickDateTo) {
			if ((mMonth > 8) && (mDay > 9))
				DateTo.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append(mDay).append("-").append(mYear)
						.append(" "));
			if ((mMonth < 9) && (mDay < 10))
				DateTo.setText(new StringBuilder().append("0")
						.append(mMonth + 1).append("-").append("0")
						.append(mDay).append("-").append(mYear).append(" "));
			if ((mMonth < 9) && (mDay > 9))
				DateTo.setText(new StringBuilder().append("0")
						.append(mMonth + 1).append("-").append(mDay)
						.append("-").append(mYear).append(" "));
			if ((mMonth > 8) && (mDay < 10))
				DateTo.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append("0").append(mDay).append("-")
						.append(mYear).append(" "));
		}
		if (id == 0) {
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplay(R.id.pickDateTo);
			date2 = new IDate(mYear, mMonth + 1, mDay);
			if (mDay > 28)
				mDay = 28;
			if (mMonth > 0)
				mMonth -= 1;
			else {
				mMonth = 11; // =>12
				mYear -= 1;
			}
			date1 = new IDate(mYear, mMonth + 1, mDay);
			updateDisplay(R.id.pickDateFrom);
			/*
			 * mMonth = c.get(Calendar.MONTH); mYear = c.get(Calendar.YEAR);
			 * mDay = c.get(Calendar.DAY_OF_MONTH);
			 */
		}
	}

	public void advance(View view) {
		Intent i = new Intent(searchReceipt.this, listview.class);
		startActivity(i);
	}

	public void onBackPressed() {
		setResult(0);
		finish();
	}
}