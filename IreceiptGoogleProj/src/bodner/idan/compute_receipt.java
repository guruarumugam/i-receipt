package bodner.idan;

import java.io.FileNotFoundException;
import bodner.idan.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser.BookmarkColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class compute_receipt extends Activity {
	/** Called when the activity is first created. */
	// public static DocsService client= new DocsService("My new Application");

	private TextView PickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	protected String prices[] = new String[4];
	private String dates[] = new String[4];
	private String stores[] = new String[4];
	private String cat[] = new String[4];
	static final int DATE_DIALOG_ID = 0;
	private Spinner spinner_d;
	private Spinner spinner_p;
	private Spinner spinner_s;
	private Spinner spinner_c;
	private ArrayAdapter<String> adapter_d;
	private ArrayAdapter<String> adapter_p;
	private ArrayAdapter<String> adapter_s;
	private ArrayAdapter<String> adapter_c;
	private iReceipt r;
	boolean t = false;// if true come from manual scan else come from rec_list
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			dates[3] = mMonth + 1 + "-" + mDay + "-" + mYear;
			r.setRdate(new IDate(year, monthOfYear + 1, dayOfMonth));
			spinner_d.setSelection(3);

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compute_receipt_layout);
		int index = getIntent().getFlags();
		r = (iReceipt) idan.rec_arr.get(index);
		boolean good_ocr = false;
		if (r.getFilepath() != null)
			good_ocr = preform_ocr(r);
		if (!good_ocr) {
			prices[0] = "";
			prices[1] = "";
			prices[2] = "";
			prices[3] = "";
			dates[0] = "";
			dates[1] = "";
			dates[2] = "";
			dates[3] = "";
		}
		stores[0] = "";
		stores[1] = "";
		stores[2] = "";
		stores[3] = "";

		cat[0] = "sports";
		cat[1] = "Fuel";
		cat[2] = "market";
		cat[3] = "cloths";

		/*
		 * Bundle extras = getIntent().getExtras(); if (extras!= null){ r=
		 * (iReceipt) extras.getSerializable("Receipt"); } if
		 * (extras.getBoolean("man"))// arrived from manual sacn t=true;
		 */

		// used this button when get a Receipt from singer
		/*
		 * Bundle extras=getIntent().getExtras(); if (extras==null) return;
		 * r=(iReceipt)extras.getSerializable("Receipt");
		 */

		// r=new iReceipt();
		// just for check now need to insert content by OCR

		// --------------------------------------------------

		spinner_s = (Spinner) findViewById(R.id.Spinner01);
		adapter_s = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, stores);
		adapter_s
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_s.setAdapter(adapter_s);
		spinner_s
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner01());

		spinner_d = (Spinner) findViewById(R.id.Spinner02);
		adapter_d = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dates);
		adapter_d
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_d.setAdapter(adapter_d);
		spinner_d
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner02());

		spinner_p = (Spinner) findViewById(R.id.Spinner03);
		adapter_p = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, prices);
		adapter_p
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_p.setAdapter(adapter_p);
		spinner_p
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner03());

		spinner_c = (Spinner) findViewById(R.id.Spinner04);
		adapter_c = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cat);
		adapter_c
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_c.setAdapter(adapter_c);
		spinner_c
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner04());

		PickDate = (TextView) findViewById(R.id.EditDate);

		PickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

	}

	private boolean preform_ocr(iReceipt r) {
		OCR ocr_obj = new OCR(r.getFilepath(), "google_username",
				"google_password");
		int ret = 0;
		try {
			ret = ocr_obj.preformOCR();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		if (ret != 1)
			return false;
		for (int i = 0; i < prices.length; i++) {
			if (ocr_obj.get_total()[i].getEntry() == null)
				prices[i] = "";
			else
				prices[i] = new String(ocr_obj.get_total()[i].getEntry());
			if (ocr_obj.getDate()[i] == null)
				dates[i] = "";
			else
				dates[i] = ocr_obj.getDate()[i].toString();
		}
		return true;
	}

	// handler for spinner 03
	public class MyOnItemSelectedListenerSpinner03 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			String str = (String) item;
			if (str.equals(""))
				r.setTotal(0);
			else
				r.setTotal(Double.valueOf(str));
		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	// handler for spinner 02
	public class MyOnItemSelectedListenerSpinner02 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			String str = (String) item;
			IDate date;
			if (str.equals(""))
				date = new IDate(2010, 10, 10);
			else {
				String str_arr[] = str.split("-");
				int month = Integer.parseInt(str_arr[0]);
				int day = Integer.parseInt(str_arr[1]);
				int year = Integer.parseInt(str_arr[2]);
				date = new IDate(year, month, day);
			}
			r.setRdate(date);
		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	// handler for spinner 01
	public class MyOnItemSelectedListenerSpinner01 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			String str = (String) item;
			r.setStoreName(str);
		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	// handler for spinner 04
	public class MyOnItemSelectedListenerSpinner04 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			String str = (String) item;
			r.setCategory(str);
		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	// call the Edit Store dialog
	public void OnClickEditStoreDialog(View view) {
		showDialog(3);
	}

	// call the Edit Store dialog
	public void OnClickEditCatDialog(View view) {
		showDialog(7);
	}

	// call the Edit price dialog
	public void OnClickEditPricekDialog(View view) {
		showDialog(5);
	}

	// create and open the date dialog
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case 5: {
			// edit price dialog
			final Dialog dialog5 = new Dialog(this);
			dialog5.setContentView(R.layout.edit_price_layout);
			dialog5.setTitle("Edit your price");

			final EditText text = (EditText) dialog5
					.findViewById(R.id.EditText01DialogPrice);

			// b1 is ok button
			Button b1 = (Button) dialog5.findViewById(R.id.Button01Dialogprice);
			b1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String price_new = text.getText().toString();
					prices[3] = price_new;
					spinner_p.setSelection(3);
					r.setTotal(Double.valueOf(price_new));
					dialog5.dismiss();

				}
			});
			// b2 is cancel button
			Button b2 = (Button) dialog5.findViewById(R.id.Button02Dialogprice);
			b2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog5.dismiss();
				}
			});
			dialog5.show();
			return null;
		}
		case 3: {
			final Dialog dialog3 = new Dialog(this);
			dialog3.setContentView(R.layout.edit_store_layout);
			dialog3.setTitle("Edit your Store");

			final EditText textstore = (EditText) dialog3
					.findViewById(R.id.EditTextStore01);

			// b1 is ok button
			Button b1store = (Button) dialog3.findViewById(R.id.ButtonStore01);
			b1store.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String store_new = textstore.getText().toString();
					stores[3] = store_new;
					spinner_s.setSelection(3);
					spinner_s.refreshDrawableState();
					r.setStoreName(store_new);
					dialog3.dismiss();

				}
			});
			// b2 is cancel button
			Button b2store = (Button) dialog3.findViewById(R.id.ButtonStore02);
			b2store.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog3.dismiss();

				}
			});
			dialog3.show();
			return null;

		}

		case 7: {

			final Dialog dialog7 = new Dialog(this);
			dialog7.setContentView(R.layout.edit_cat_layout);
			dialog7.setTitle("Edit your Category");

			final EditText textCat = (EditText) dialog7
					.findViewById(R.id.EditTextCat01);

			// b1 is ok button
			Button b1cat = (Button) dialog7.findViewById(R.id.ButtonCat01);
			b1cat.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String cat_new = textCat.getText().toString();
					cat[3] = cat_new;
					spinner_c.setSelection(3);
					// spinner_c.refreshDrawableState();
					r.setCategory(cat_new);
					dialog7.dismiss();

				}
			});
			// b2 is cancel button
			Button b2cat = (Button) dialog7.findViewById(R.id.ButtonCat02);
			b2cat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog7.dismiss();

				}
			});
			dialog7.show();
			return null;
		}
		}
		return null;

	}

	public void onClick(View view) {
		setResult(1);
		r.setProcessed(true);
		saveList();
		finish();
	}
	public void onClick2(View view) {
		setResult(0);
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}
}
