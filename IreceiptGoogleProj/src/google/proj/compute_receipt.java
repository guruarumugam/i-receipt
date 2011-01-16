package google.proj;

import java.io.FileNotFoundException;
import google.proj.R;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import misc.Misc;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
	private Spinner spinner_d; // date
	private Spinner spinner_p; // price
	private Spinner spinner_s; // store
	private Spinner spinner_c; // category
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compute_receipt_layout);

		// speack
		Button speakButton = (Button) findViewById(R.id.speachButton1);
		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			speakButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
							RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
							"Speech recognition demo");
					startActivityForResult(intent, 1234);

				}
			});
		} else {
			speakButton.setEnabled(false);
		}

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
			if (str.equals("")){
				final Calendar cal = Calendar.getInstance();
				date = new IDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)); // start of this month
			}
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

				public void onClick(View v) {
					String price_new = text.getText().toString();
					prices[3] = price_new;
					spinner_p.setSelection(3);
					if (price_new.equals(""))
						price_new="0";
					rec.setTotal(Double.valueOf(price_new));
					dialog5.dismiss();
				}
			});
			// b2 is cancel button
			Button b2 = (Button) dialog5.findViewById(R.id.Button02Dialogprice);
			b2.setOnClickListener(new OnClickListener() {

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
					rec.setStoreName(store_new);
					dialog3.dismiss();
				}
			});
			// b2 is cancel button
			Button b2store = (Button) dialog3.findViewById(R.id.ButtonStore02);
			b2store.setOnClickListener(new OnClickListener() {

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
		//saveList();
		int i=0,j=0;
		if (idan.settings.getMaxmonth() != (-1)) {  
			   i = manual_scan.checkLimitException(idan.settings.getMaxmonth());  
			   }  
			   if (idan.settings.getMaxUniquely() != (-1)) {  
			    j = manual_scan.checkPeriodLimitException(idan.settings.getMaxUniquely(),  
			      idan.settings.getDateEx());  
			   }  
			   if (i == 1) {// over MonthLimit  
			    if (j == 0)  
			     setResult(100); // only over MonthLimit  
			    else  
			     setResult(200);// over MonthLimit and PeriodLimit  
			   } else {  
			    if (j == 0)  
			     setResult(2); // NOT over any limit  
			    else  
			     setResult(300);// over PeriodLimit  
			   }
			   
		(new save()).execute();
		
		//finish();
	}

	public void onClick2(View view) {
		setResult(0);
		if (rec.getFilepath()!=null&&!rec.getFilepath().equals("")){
		 File file = new File(rec.getFilepath());
		 file.delete();
		}
		finish();
	}

	public void saveList() {

		rec.setUpdate();
		boolean connected = Misc.checkConnection(this);
		if (connected) {
			for (iReceipt tmprr : idan.rec_arr) {
				if (rec_view.notSync(tmprr)) {
					tmprr.setSync();
					idan.sync.addtoUpdateList(tmprr);
				}
			}
			idan.sync.sendSync();
			// need to check if the sync run ok
			idan.sync.clearUpdateList();
		}
		Misc.saveList(this);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String res = "";
			for (String string : matches) {
				res = res + " " + string;
			}
			stores[3] = res;
			spinner_s.setSelection(3);
			adapter_s.notifyDataSetChanged();
			return;
		}

		finish();
	}

	class save extends AsyncTask<Void, Void, Void> {

		protected ProgressDialog progressDialog;

		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(compute_receipt.this,
					"Ireceipt", "Saving...", true, false);
		}

		protected Void doInBackground(Void... params) {
			saveList();
			return null;
		}

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

		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog
					.show(compute_receipt.this, "Ireceipt",
							"Extracting the data from the receipt."
									+ "\n\n(Press the back key to skip...)",
							true, true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
                
				
				
				@Override
                public void onCancel(DialogInterface dialog) {
					initialize_fields();
					Toast.makeText(compute_receipt.this, ocr_preform.this.cancel(true)+"", Toast.LENGTH_SHORT).show();
					
					
				}
				

		});
		}

		protected void initialize_fields(){
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
		
		}
	
		protected Void doInBackground(Void... params) {
			if (rec.getFilepath() != null){
				
				good_ocr = preform_ocr(rec);
			}
			return null;
			}

		protected void onPostExecute(Void result) {
			super.onPostExecute(null);


			initialize_fields();
			// get the current date
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);

			progressDialog.dismiss();
		}

	}

	// voice recognition

}
