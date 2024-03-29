package google.proj;

import google.proj.R;
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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.Date;

public class manual_scan extends Activity implements OnClickListener {

	EditText business, price, notes;
	TextView date, uri;
	private Spinner spinner_cat;
	private ArrayAdapter<String> adapter_cat;
	private iReceipt rec;
	CheckBox myCheckBox;
	private int Year, Month, Day;
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	private String filemanagerstring;
	// public static int limit = 600;
	public static double totalMonth, totalPeriod;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manualscan);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// speak
		Button speakButton1 = (Button) findViewById(R.id.voice1);
		Button speakButton2 = (Button) findViewById(R.id.voice2);
		Button speakButton3 = (Button) findViewById(R.id.voice3);

		Button AddImage = (Button) findViewById(R.id.AddImage);
		AddImage.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});
		uri = (TextView) findViewById(R.id.uri);
		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			speakButton1.setOnClickListener(this);
			speakButton2.setOnClickListener(this);
			speakButton3.setOnClickListener(this);
		} else {
			speakButton1.setEnabled(false);
			speakButton2.setEnabled(false);
			speakButton3.setEnabled(false);
		}

		int index = getIntent().getFlags();
		rec = (iReceipt) idan.rec_arr.get(index);

		rec = idan.rec_arr.get(index);

		rec.setCategory("Dining");
		business = (EditText) findViewById(R.id.EditText01);
		price = (EditText) findViewById(R.id.EditText02);
		notes = (EditText) findViewById(R.id.EditNotesManual);
		date = (TextView) findViewById(R.id.dateView);
		myCheckBox = (CheckBox) findViewById(R.id.CheckBox1);
		spinner_cat = (Spinner) findViewById(R.id.Spinner04);
		adapter_cat = new ArrayAdapter<String>(manual_scan.this,
				android.R.layout.simple_spinner_item, MainActivity.cat);
		adapter_cat
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_cat.setAdapter(adapter_cat);
		spinner_cat
				.setOnItemSelectedListener(new MyOnItemSelectedListenerSpinner04());
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay(R.id.dateView);
		date.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(R.id.dateView);
			}
		});
	}

	public void onClick(View v) {
		if (v.getId() == R.id.voice1)
			startVoiceRecognitionActivity(11);
		if (v.getId() == R.id.voice2)
			startVoiceRecognitionActivity(22);
		if (v.getId() == R.id.voice3)
			startVoiceRecognitionActivity(33);
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity(int code) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		String str = " ";
		if (code == 11)
			str = "Say business name:";
		else if (code == 22)
			str = "Say price:";
		else if (code == 33)
			str = "Say Notes:";

		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, str);
		startActivityForResult(intent, code);
	}

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

	private DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay(R.id.dateView);
		}
	};

	private void updateDisplay(int id) { // have only one option - date.
		if ((Month > 8) && (Day > 9))
			date.setText(new StringBuilder().append(Month + 1).append("-")
					.append(Day).append("-").append(Year).append(" "));
		if ((Month > 8) && (Day < 10))
			date.setText(new StringBuilder().append(Month + 1).append("-")
					.append("0" + Day).append("-").append(Year).append(" "));
		if ((Month < 9) && (Day > 9))
			date.setText(new StringBuilder().append("0" + (Month + 1))
					.append("-").append(Day).append("-").append(Year)
					.append(" "));
		if ((Month < 9) && (Day < 10))
			date.setText(new StringBuilder().append("0" + (Month + 1))
					.append("-").append("0" + Day).append("-").append(Year)
					.append(" "));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case R.id.dateView:
			DatePickerDialog Date1 = new DatePickerDialog(this,
					DateSetListener, Year, Month, Day);
			return Date1;
		}
		return null;
	}

	public void onClick3(View view) {
		String str1;
		int month, day, year, i = 0, j = 0;
		// setResult(1);
		rec.setProcessed(true);
		rec.setFlaged(myCheckBox.isChecked());
		rec.setNotes(notes.getText().toString());
		str1 = date.getText().toString();
		


		
		month = Integer.parseInt(str1.substring(0, 2));
		day = Integer.parseInt(str1.substring(3, 5));
		year = Integer.parseInt(str1.substring(6,10));
		rec.setRdate(new IDate(year, month, day));
		rec.setStoreName(business.getText().toString());
		if (price.getText().toString().length() == 0)
			rec.setTotal(0);
		else
			rec.setTotal(Double.parseDouble(price.getText().toString()));
		rec.setFilepath(uri.getText().toString());
		if (idan.settings.getMaxmonth() != (-1)) {  
			   i = checkLimitException(idan.settings.getMaxmonth());  
			   }  
			   if (idan.settings.getMaxUniquely() != (-1)) {  
			    j = checkPeriodLimitException(idan.settings.getMaxUniquely(),  
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
		
		
		
		
		
		//(new save()).execute();//will finish
			   saveList();
			   finish();
	}

	public void onClick4(View view) {
		// setResult(100);
		setResult(0);
		finish();
	}

	/*
	 * public void saveList2() { try { ObjectOutputStream outputStream = new
	 * ObjectOutputStream( openFileOutput("RecListsave.tmp",
	 * Context.MODE_PRIVATE)); outputStream.writeObject(idan.rec_arr);
	 * outputStream.close(); } catch (IOException ex) { ex.printStackTrace(); }
	 * }
	 */

	public void saveList() {
		rec.setUpdate();
		/*boolean connected = Misc.checkConnection(this);
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
		}*/
		Misc.saveList(this);
	}

	String onlyNumbers(String str) {
		String ret = "";
		boolean point = false;
		if (str == null)
			return null;
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= '0') && (str.charAt(i) <= '9'))
				ret += str.charAt(i);
			if (str.charAt(i) == '.' && !point) {
				if (ret.length() == 0)
					ret += "0.";
				else
					ret += ".";
				point = true;
			}
		}
		if (ret.charAt(ret.length() - 1) == '.')
			ret += '0';
		return ret;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (((requestCode == 11) || (requestCode == 22) || (requestCode == 33))
				&& resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String res = "";
			for (String string : matches) {
				res = res + " " + string;
			}
			if (requestCode == 11)
				business.setText(res);
			if (requestCode == 22)
				price.setText(onlyNumbers(res));
			if (requestCode == 33)
				notes.setText(res);
			return;
		}
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();

				// OI FILE Manager
				filemanagerstring = selectedImageUri.getPath();

				// MEDIA GALLERY
				selectedImagePath = getPath(selectedImageUri);

				// DEBUG PURPOSE - you can delete this if you want
				if (selectedImagePath != null) {
					uri.setText(selectedImagePath);
					/*
					 * Intent intent = new Intent();
					 * intent.setAction(android.content.Intent.ACTION_VIEW);
					 * intent.setDataAndType( Uri.fromFile(new
					 * File(selectedImagePath)), "image/jpg");
					 * startActivity(intent);
					 */
					// System.out.println(selectedImagePath);
				} else
					System.out.println("selectedImagePath is null");
				if (filemanagerstring != null) {
					// System.out.println(filemanagerstring);
				} else
					System.out.println("filemanagerstring is null");

				// NOW WE HAVE OUR WANTED STRING
				if (selectedImagePath != null)
					System.out
							.println("selectedImagePath is the right one for you!");
				else
					System.out
							.println("filemanagerstring is the right one for you!");
			}
			return;
		}
		finish();
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
/*
	class save extends AsyncTask<Void, Void, Void> {

		protected ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(manual_scan.this, "Ireceipt",
					"Saving...", true, false);
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
*/
	public static int checkLimitException(double limit) {
		totalMonth = 0.0;
		int calYear, calMonth;
		// int calDay;
		IDate date;
		final Calendar cal = Calendar.getInstance();
		calYear = cal.get(Calendar.YEAR);
		calMonth = cal.get(Calendar.MONTH);
		// calDay = cal.get(Calendar.DAY_OF_MONTH);
		date = new IDate(calYear, calMonth + 1, 1); // start of this month
		for (iReceipt rec : idan.rec_arr) {
			if (rec.getRdate().compareTo(date) <= 0)
				totalMonth += rec.getTotal();
		}
		if (totalMonth > limit) {
			/*
			 * CustomizeDialog customizeDialog = new CustomizeDialog(this,
			 * "Your expenditures this month passed your limit - \"" + limit +
			 * "\""); customizeDialog.show();
			 */
			return 1;
		}
		return 0;
	}

	public static int checkPeriodLimitException(double limit, Date idate) {
		totalPeriod = 0.0;
		int calYear, calMonth, calDay;
		IDate date;
		calYear = idate.getYear();
		calMonth = idate.getMonth();
		calDay = idate.getDay();
		date = new IDate(calYear, calMonth, calDay);
		for (iReceipt rec : idan.rec_arr) {
			if (rec.getRdate().compareTo(date) <= 0)
				totalPeriod += rec.getTotal();
		}
		if (totalPeriod > limit) {
			return 1;
		}
		return 0;
	}
}