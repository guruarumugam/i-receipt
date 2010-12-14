package google.proj;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import google.proj.R;

public class statistics extends Activity {

	private TextView fromPickDate;
	private TextView toPickDate;
	private int Year, Month, Day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		fromPickDate = (TextView) findViewById(R.id.pickDateFrom);
		toPickDate = (TextView) findViewById(R.id.pickDateTo);
		toPickDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(R.id.pickDateTo);
			}
		});
		fromPickDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(R.id.pickDateFrom);
			}
		});
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);

		updateDisplay(R.id.pickDateFrom); // default-receipts from today
		updateDisplay(R.id.pickDateTo); // default-receipts from today
	}

	private void updateDisplay(int id) {
		if (id == R.id.pickDateFrom)
			fromPickDate.setText(new StringBuilder().append("From:")
					.append(Month + 1).append("-").append(Day).append("-")
					.append(Year).append(" "));
		if (id == R.id.pickDateTo)
			toPickDate.setText(new StringBuilder().append("To:")
					.append(Month + 1).append("-").append(Day).append("-")
					.append(Year).append(" "));
	}

	private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay(R.id.pickDateTo);
		}
	};
	private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay(R.id.pickDateFrom);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case R.id.pickDateFrom:
			DatePickerDialog Date1 = new DatePickerDialog(this,
					fromDateSetListener, Year, Month, Day);
			return Date1;
		case R.id.pickDateTo:
			DatePickerDialog Date2 = new DatePickerDialog(this,
					toDateSetListener, Year, Month, Day);
			return Date2;
		}
		return null;
	}
}
