package google.proj;

import android.app.Activity;
import java.lang.Number;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import google.proj.R;

public class statistics extends Activity {

	private TextView fromPickDate;
	private TextView toPickDate;
	private TextView totalThisMonth, totalLastMonth, showTotalFromTo;
	private int Year, Month, Day;
	private double total1 = 0;
	private double total2 = 0;
	private IDate date1, date2, date3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stats);
		fromPickDate = (TextView) findViewById(R.id.pickDateFrom);
		toPickDate = (TextView) findViewById(R.id.pickDateTo);
		totalThisMonth = (TextView) findViewById(R.id.totalThisMonth);
		totalLastMonth = (TextView) findViewById(R.id.totalLastMonth);
		showTotalFromTo = (TextView) findViewById(R.id.totalFromTo);
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
		date1 = new IDate(Year, Month + 1, 1); // start of this month
		if (Month > 0)
			date3 = new IDate(Year, Month, 1); // start of last month
		else
			date3 = new IDate(Year - 1, 12, 1);

		updateDisplay(R.id.pickDateFrom); // default-receipts from today
		updateDisplay(R.id.pickDateTo); // default-receipts from today

		for (int i = 0; i < idan.rec_arr.size(); i++) {
			date2 = idan.rec_arr.get(i).getRdate();
			if (date1.compareTo(date2) >= 0) // rec.date > start of this month
				total1 += idan.rec_arr.get(i).getTotal();
			else if (date3.compareTo(date2) >= 0) // re.date < start of this
													// month and > start of last
													// month
				total2 += idan.rec_arr.get(i).getTotal();
		}
		totalThisMonth.setText(Double.toString(total1));
		totalLastMonth.setText(Double.toString(total2));
	}

	private void updateDisplay(int id) {
		if (id == R.id.pickDateFrom) {
			if ((Month > 8) && (Day > 9))
				fromPickDate.setText(new StringBuilder().append(Month + 1)
						.append("-").append(Day).append("-").append(Year)
						.append(" "));
			if ((Month > 8) && (Day < 10))
				fromPickDate.setText(new StringBuilder().append(Month + 1)
						.append("-").append("0" + Day).append("-").append(Year)
						.append(" "));
			if ((Month < 9) && (Day > 9))
				fromPickDate.setText(new StringBuilder()
						.append("0" + (Month + 1)).append("-").append(Day)
						.append("-").append(Year).append(" "));
			if ((Month < 9) && (Day < 10))
				fromPickDate
						.setText(new StringBuilder().append("0" + (Month + 1))
								.append("-").append("0" + Day).append("-")
								.append(Year).append(" "));
		}
		if (id == R.id.pickDateTo) {
			if ((Month > 8) && (Day > 9))
				toPickDate.setText(new StringBuilder().append(Month + 1)
						.append("-").append(Day).append("-").append(Year)
						.append(" "));
			if ((Month > 8) && (Day < 10))
				toPickDate.setText(new StringBuilder().append(Month + 1)
						.append("-").append("0" + Day).append("-").append(Year)
						.append(" "));
			if ((Month < 9) && (Day > 9))
				toPickDate.setText(new StringBuilder()
						.append("0" + (Month + 1)).append("-").append(Day)
						.append("-").append(Year).append(" "));
			if ((Month < 9) && (Day < 10))
				toPickDate
						.setText(new StringBuilder().append("0" + (Month + 1))
								.append("-").append("0" + Day).append("-")
								.append(Year).append(" "));
		}
	}

	public void totalFromTo() {

		int year, month, day, check;
		Double totalFromTo = 0.0;
		String str1, str2;
		IDate dateFrom, dateTo, date4;

		str1 = fromPickDate.getText().toString();
		str2 = toPickDate.getText().toString();

		month = Integer.parseInt(str1.substring(0, 2));
		day = Integer.parseInt(str1.substring(3, 5));
		year = Integer.parseInt(str1.substring(6, 10));

		dateFrom = new IDate(year, month, day);

		month = Integer.parseInt(str2.substring(0, 2));
		day = Integer.parseInt(str2.substring(3, 5));
		year = Integer.parseInt(str2.substring(6, 10));

		dateTo = new IDate(year, month, day);

		if (dateTo.compareTo(dateFrom) > 0)
			showTotalFromTo.setText("invalid dates..."); // dateFrom
															// >
															// dateTo
		else {
			totalFromTo = 0.0;
			for (int i = 0; i < idan.rec_arr.size(); i++) {
				date4 = idan.rec_arr.get(i).getRdate();
				if ((date4.compareTo(dateTo) >= 0)
						&& (dateFrom.compareTo(date4) >= 0))
					totalFromTo += idan.rec_arr.get(i).getTotal();
			}
			showTotalFromTo.setText("" + totalFromTo);
		}
	}

	private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay(R.id.pickDateTo);
			totalFromTo();
		}
	};
	private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay(R.id.pickDateFrom);
			totalFromTo();
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

	public void pieChart(View view) {
		Intent i = new Intent(statistics.this, MainActivity.class);
		startActivity(i);
	}
}
