package google.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import google.proj.IDate;
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
	// public static String cat[] = new String[10];
	public String str_cat;
	public int mYear, mDay, mMonth;
	public IDate date1, date2;
	private TextView DateFrom, DateTo;

	DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			date1 = new IDate(year, monthOfYear + 1, dayOfMonth);
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
			updateDisplay(R.id.pickDateTo);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchreceipt);
		// rec_arr_search = new ArrayList<iReceipt>();///////////
		searchButton = (Button) findViewById(R.id.searchButton);
		// Business = (EditText) findViewById(R.id.EditText01);
		PriceFrom = (EditText) findViewById(R.id.EditText05);
		PriceTo = (EditText) findViewById(R.id.EditText06);
		radioDate = (RadioButton) findViewById(R.id.RadioButton01);
		radioPrice = (RadioButton) findViewById(R.id.RadioButton02);
		radioCategory = (RadioButton) findViewById(R.id.RadioButton03);
		DateFrom = (TextView) findViewById(R.id.pickDateFrom);
		DateTo = (TextView) findViewById(R.id.pickDateTo);

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
		/*cat[0] = "Dining";
		cat[1] = "Car";
		cat[2] = "Travel";
		cat[3] = "Shopping";
		cat[4] = "Rent";
		cat[5] = "Groceries";
		cat[6] = "Presents";
		cat[7] = "Entertainment";
		cat[8] = "Household goods";
		cat[9] = "Other";*/
		adapter_c = new ArrayAdapter<String>(searchReceipt.this,
				android.R.layout.simple_spinner_item, MainActivity.cat);
		adapter_c
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_c.setAdapter(adapter_c);
		spinner_c
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner04());
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
		// if (radioBusiness.isChecked())
		// BusinessHandler();
		if (radioDate.isChecked())
			DateHandler();
		if (radioPrice.isChecked())
			PriceHandler();
		if (radioCategory.isChecked())
			CategoryHandler();
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
		IDate date0; // only for debuging!!
		if (date1.compareTo(date2) < 0) {// error!!!
			// לעשות דיאלוג שמודיע שהתאריכים לא טובים..
			final Dialog dialog2 = new Dialog(this);
			dialog2.setContentView(R.layout.error_price_date);
			Button ok = (Button) findViewById(R.id.OkButtonErrorPrice);
			TextView text = (TextView) findViewById(R.id.error_massage);
			text.setText("Invalid dates.");
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog2.dismiss();
				}
			});
		} else {
			for (int i = 0; i < idan.rec_arr.size(); i++) {
				date0 = idan.rec_arr.get(i).getRdate(); // only for debuging!!
				if ((date0.compareTo(date1) <= 0)
						&& (date0.compareTo(date2) >= 0))
					rec_arr_search.add(idan.rec_arr.get(i));
			}
		}
	}

	public void PriceHandler() {
		Double price1, price2;
		Double price; // only for debug!!!
		price1 = Double.parseDouble(PriceFrom.getText().toString());
		price2 = Double.parseDouble(PriceTo.getText().toString());
		if (price1 > price2) {
			final Dialog dialog1 = new Dialog(this);
			dialog1.setContentView(R.layout.error_price_date);
			Button ok = (Button) findViewById(R.id.OkButtonErrorPrice);
			TextView text = (TextView) findViewById(R.id.error_massage);
			text.setText("Invalid prices.");
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog1.dismiss();
				}
			});
		}
		for (int i = 0; i < idan.rec_arr.size(); i++) {
			price = idan.rec_arr.get(i).getTotal(); // only for the debuging!!!
			if ((idan.rec_arr.get(i).getTotal() >= price1)
					&& (idan.rec_arr.get(i).getTotal() <= price2))
				rec_arr_search.add(idan.rec_arr.get(i));
		}
	}

	public void CategoryHandler() {
		for (int i = 0; i < idan.rec_arr.size(); i++) {
			if (idan.rec_arr.get(i).getCategory().equals(str_cat))
				rec_arr_search.add(idan.rec_arr.get(i));
		}
	}

	// handler for spinner 04
	public class MyOnItemSelectedListenerSpinner04 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			str_cat = (String) item;
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	private void updateDisplay(int id) {
		if (id == R.id.pickDateFrom) {
			DateFrom.setText(new StringBuilder().append("From:")
					.append(mMonth + 1).append("-").append(mDay).append("-")
					.append(mYear).append(" "));
		}
		if (id == R.id.pickDateTo) {
			DateTo.setText(new StringBuilder().append("To:").append(mMonth + 1)
					.append("-").append(mDay).append("-").append(mYear)
					.append(" "));
		}
	}

	public void advance(View view) {
		Intent i = new Intent(searchReceipt.this, listview.class);
		startActivity(i);
	}

}