package google.proj;

import google.proj.*;
import java.io.FileNotFoundException;
import google.proj.R;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser.BookmarkColumns;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class compute_receipt extends Activity {
	// public static DocsService client= new DocsService("My new Application");

	private TextView PickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	protected String prices[] = new String[4];
	private String dates[] = new String[4];
	private String stores[] = new String[4];
	public static String cat[] = new String[10];
	static final int DATE_DIALOG_ID = 0;
	private Spinner spinner_d;
	private Spinner spinner_p;
	private Spinner spinner_s;
	private Spinner spinner_c;
	private ArrayAdapter<String> adapter_d;
	private ArrayAdapter<String> adapter_p;
	private ArrayAdapter<String> adapter_s;
	private ArrayAdapter<String> adapter_c;
	private iReceipt rec;
	boolean good_ocr = false;
	CheckBox myCheckBox;
	EditText myEditText;
	boolean t = false;// if true come from manual scan else come from rec_list
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			dates[3] = mMonth + 1 + "-" + mDay + "-" + mYear;
			rec.setRdate(new IDate(year, monthOfYear + 1, dayOfMonth));
			spinner_d.setSelection(3);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compute_receipt_layout);
		myCheckBox = (CheckBox) findViewById(R.id.CheckBox1);
		myEditText = (EditText) findViewById(R.id.EditNotesManual);
		int index = getIntent().getFlags();
		rec = (iReceipt) idan.rec_arr.get(index);
		(new ocr_preform()).execute();
	}

	// handler for spinner 03
	public class MyOnItemSelectedListenerSpinner03 implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Object item = parent.getItemAtPosition(pos);
			String str = (String) item;
			if (str.equals(""))
				rec.setTotal(0);
			else
				rec.setTotal(Double.valueOf(str));
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
			rec.setRdate(date);
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
			rec.setStoreName(str);
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
			rec.setCategory(str);
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
					rec.setTotal(Double.valueOf(price_new));
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

			// ///////////////////////// AutoComplete Business ////

			/*
			 * final String store_names[] = new String[idan.rec_arr.size()]; for
			 * (int k = 0; k < idan.rec_arr.size(); k++) { store_names[k] =
			 * idan.rec_arr.get(k).getStoreName(); } ArrayAdapter<String>
			 * arrAdapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_dropdown_item_1line, store_names);
			 * AutoCompleteTextView Business = (AutoCompleteTextView)
			 * findViewById(R.id.AutoCompleteBusiness);
			 * Business.setAdapter(arrAdapter);
			 */

			// //////////////////////////////////////////////

			// b1 is ok button
			Button b1store = (Button) dialog3.findViewById(R.id.ButtonStore01);
			b1store.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String store_new = textstore.getText().toString();
					stores[3] = store_new;
					spinner_s.setSelection(3);
					spinner_s.refreshDrawableState();
					rec.setStoreName(store_new);
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
					rec.setCategory(cat_new);
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
		rec.setProcessed(true);
		rec.setFlaged(myCheckBox.isChecked());
		rec.setNotes(myEditText.getText().toString());
		// saveList();
		(new save()).execute(null);
		setResult(100);
	}

	public void onClick2(View view) {
		setResult(0);
		finish();
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
			} else if (netType == ConnectivityManager.TYPE_MOBILE
					&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				connected = info.isConnected();
			} else {
				connected = false;
			}
		}

		if (connected) {
			for (iReceipt tmprr : idan.rec_arr) {

				if (rec_view.notSync(tmprr)) {
					tmprr.setSync();
					idan.sync.addtoUpdateList(tmprr);
				}
			}

			idan.sync.sendSync(loginpage.accountname);
			// need to check if the sync run ok
			// for (iReceipt tmprr: idan.sync.getUpdateList()){
			// tmprr.setSync();
			// }
			idan.sync.deleteupdatelist();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}

	class save extends AsyncTask<Void, Void, Void> {

		protected ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(compute_receipt.this,
					"Ireceipt", "Saving...", true, false);
		}

		protected Void doInBackground(Void... params) {
			saveList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(null);
			progressDialog.dismiss();
			finish();

		}

	}

	class ocr_preform extends AsyncTask<Void, Void, Void> {

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
				if (ocr_obj.getStoreName()[i].getEntry() == null)
					stores[i] = "";
				else
					stores[i] = new String(ocr_obj.getStoreName()[i].getEntry());

			}
			return true;
		}

		private final static String TAG = "LoginActivity.EfetuaLogin";

		protected ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog
					.show(compute_receipt.this, "Ireceipt",
							"Extracting the data from the receipt."
									+ "\n\n(Press the back key to skip...)",
							true, true);

		}

		protected Void doInBackground(Void... params) {
			if (rec.getFilepath() != null)
				good_ocr = preform_ocr(rec);
			if (!good_ocr) {
				prices[0] = "";
				prices[1] = "";
				prices[2] = "";
				prices[3] = "";
				dates[0] = "";
				dates[1] = "";
				dates[2] = "";
				dates[3] = "";
				stores[0] = "";
				stores[1] = "";
				stores[2] = "";
				stores[3] = "";
			}
			cat[0] = "Dining";
			cat[1] = "Car";
			cat[2] = "Travel";
			cat[3] = "Shopping";
			cat[4] = "Rent";
			cat[5] = "Groceries";
			cat[6] = "Presents";
			cat[7] = "Entertainment";
			cat[8] = "Household goods";
			cat[9] = "Other";

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(null);

			spinner_s = (Spinner) findViewById(R.id.Spinner01);
			adapter_s = new ArrayAdapter<String>(compute_receipt.this,
					android.R.layout.simple_spinner_item, stores);
			adapter_s
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_s.setAdapter(adapter_s);
			spinner_s
					.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner01());

			spinner_d = (Spinner) findViewById(R.id.Spinner02);
			adapter_d = new ArrayAdapter<String>(compute_receipt.this,
					android.R.layout.simple_spinner_item, dates);
			adapter_d
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_d.setAdapter(adapter_d);
			spinner_d
					.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner02());

			spinner_p = (Spinner) findViewById(R.id.Spinner03);
			adapter_p = new ArrayAdapter<String>(compute_receipt.this,
					android.R.layout.simple_spinner_item, prices);
			adapter_p
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_p.setAdapter(adapter_p);
			spinner_p
					.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner03());

			spinner_c = (Spinner) findViewById(R.id.Spinner04);
			adapter_c = new ArrayAdapter<String>(compute_receipt.this,
					android.R.layout.simple_spinner_item, cat);
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

			progressDialog.dismiss();
		}

	}

}
