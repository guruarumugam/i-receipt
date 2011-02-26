package google.proj;

import google.proj.R;
import google.proj.R.drawable;
import google.proj.R.id;
import google.proj.R.layout;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String[] cat = { "Dining", "Car", "Travel", "Shopping",
			"Rent", "Groceries", "Presents", "Entertainment",
			"Household goods", "Other" };
	int[] colors = new int[] { Color.BLUE, Color.GREEN,
			Color.parseColor("#8b8878"), Color.YELLOW,
			Color.parseColor("#00ced1"), Color.RED, Color.LTGRAY,
			Color.parseColor("#ff8c00"), Color.parseColor("#8b4513"),
			Color.parseColor("#228b22") };
	List<PieDetailsItem> PieData = new ArrayList<PieDetailsItem>(0);
	Double total_period;
	ImageView mImageView;
	// //////////////////////
	public static int period; // 0 => last week, 1=> last month, 2=>last
								// year 3=>not initialized yet
	public static List<iReceipt> pie_rec_arr = new ArrayList<iReceipt>();
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, total_this_period, total;
	LinearLayout lin1, lin2, lin3, lin4, lin5, lin6, lin7, lin8, lin9, lin10;
	RadioGroup rg;
	RadioButton r1, r2, r3;
	private int myYear, myMonth, myDay;

	// //////////////////////

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.piechart);
		/*
		 * final Calendar c2 = Calendar.getInstance(); myYear =
		 * c2.get(Calendar.YEAR); myMonth = c2.get(Calendar.MONTH + 1); myDay =
		 * c2.get(Calendar.DAY_OF_MONTH);
		 */
		myYear = statistics.Year;
		myMonth = statistics.Month + 1;
		myDay = statistics.Day;
		period = 3;
		mImageView = (ImageView) findViewById(R.id.piecontainer);
		lin1 = (LinearLayout) findViewById(R.id.LinearLayout02);
		lin2 = (LinearLayout) findViewById(R.id.LinearLayout07);
		lin3 = (LinearLayout) findViewById(R.id.LinearLayout04);
		lin4 = (LinearLayout) findViewById(R.id.LinearLayout03);
		lin5 = (LinearLayout) findViewById(R.id.LinearLayout08);
		lin6 = (LinearLayout) findViewById(R.id.LinearLayout09);
		lin7 = (LinearLayout) findViewById(R.id.LinearLayout05);
		lin8 = (LinearLayout) findViewById(R.id.LinearLayout10);
		lin9 = (LinearLayout) findViewById(R.id.LinearLayout11);
		lin10 = (LinearLayout) findViewById(R.id.LinearLayout06);
		t1 = (TextView) findViewById(R.id.TextView03);
		t2 = (TextView) findViewById(R.id.TextView04);
		t3 = (TextView) findViewById(R.id.TextView05);
		t4 = (TextView) findViewById(R.id.TextView06);
		t5 = (TextView) findViewById(R.id.TextView07);
		t6 = (TextView) findViewById(R.id.TextView08);
		t7 = (TextView) findViewById(R.id.TextView09);
		t8 = (TextView) findViewById(R.id.TextView10);
		t9 = (TextView) findViewById(R.id.TextView13);
		t10 = (TextView) findViewById(R.id.TextView14);
		total_this_period = (TextView) findViewById(R.id.TextView17);
		total = (TextView) findViewById(R.id.TextView16);
		rg = (RadioGroup) findViewById(R.id.RadioGroup01);
		r1 = (RadioButton) findViewById(R.id.RadioWeek);
		r2 = (RadioButton) findViewById(R.id.RadioMonth);
		r3 = (RadioButton) findViewById(R.id.RadioYear);

		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				int id = arg1;
				if (id == R.id.RadioWeek)
					period = 0;
				if (id == R.id.RadioMonth)
					period = 1;
				if (id == R.id.RadioYear)
					period = 2;
				makePie(period);
			}
		});
		if (period == 3) {
			period = 0;
			r1.setChecked(true);
		}
		if (r1.isChecked())
			period = 0;
		if (r2.isChecked())
			period = 1;
		if (r3.isChecked())
			period = 2;
		makePie(period);
	}

	public void makePie(int period) {
		PieDetailsItem Item;
		int mNumGen = 10;
		int MaxPieItems = cat.length;
		Double MaxCount = 0.0;
		// ///////////////////////////////////////////////////////////////
		mImageView.setVisibility(View.VISIBLE);
		IDate dateToday = new IDate(myYear, myMonth, myDay);// today
		IDate date = new IDate();
		date = getDateFrom(dateToday, period);
		IDate rec_date = new IDate();
		pie_rec_arr.clear();
		for (iReceipt rec : idan.rec_arr) {
			rec_date = rec.getRdate();
			if ((dateToday.compareTo(rec_date) <= 0)
					&& (date.compareTo(rec_date) >= 0))
				pie_rec_arr.add(rec);
		}
		// ///////////////////////////////////////////////////////////////
		Double sumPerCat[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		for (iReceipt rec : pie_rec_arr) {
			if (rec.getCategory().equals("Dining"))
				sumPerCat[0] += rec.getTotal();
			if (rec.getCategory().equals("Car"))
				sumPerCat[1] += rec.getTotal();
			if (rec.getCategory().equals("Travel"))
				sumPerCat[2] += rec.getTotal();
			if (rec.getCategory().equals("Shopping"))
				sumPerCat[3] += rec.getTotal();
			if (rec.getCategory().equals("Rent"))
				sumPerCat[4] += rec.getTotal();
			if (rec.getCategory().equals("Groceries"))
				sumPerCat[5] += rec.getTotal();
			if (rec.getCategory().equals("Presents"))
				sumPerCat[6] += rec.getTotal();
			if (rec.getCategory().equals("Entertainment"))
				sumPerCat[7] += rec.getTotal();
			if (rec.getCategory().equals("Household goods"))
				sumPerCat[8] += rec.getTotal();
			if (rec.getCategory().equals("Other"))
				sumPerCat[9] += rec.getTotal();
		}
		total_period = 0.0 + sumPerCat[1] + sumPerCat[2] + sumPerCat[3]
				+ sumPerCat[4] + sumPerCat[5] + sumPerCat[6] + sumPerCat[7]
				+ sumPerCat[8] + sumPerCat[9] + sumPerCat[0];
		total_this_period.setText(total_period.toString()); // ***********************
		if (period == 0)
			total.setText("Total this week:");
		if (period == 1)
			total.setText("Total this month:");
		if (period == 2)
			total.setText("Total this year:");
		if (sumPerCat[0] != 0) {
			lin1.setVisibility(View.VISIBLE);
			t1.setText("" + sumPerCat[0]);
		} else
			lin1.setVisibility(View.GONE);
		if (sumPerCat[1] != 0) {
			lin3.setVisibility(View.VISIBLE);
			t3.setText("" + sumPerCat[1]);
		} else
			lin3.setVisibility(View.GONE);
		if (sumPerCat[2] != 0) {
			lin4.setVisibility(View.VISIBLE);
			t4.setText("" + sumPerCat[2]);
		} else
			lin4.setVisibility(View.GONE);
		if (sumPerCat[3] != 0) {
			lin2.setVisibility(View.VISIBLE);
			t2.setText("" + sumPerCat[3]);
		} else
			lin2.setVisibility(View.GONE);
		if (sumPerCat[4] != 0) {
			lin7.setVisibility(View.VISIBLE);
			t7.setText("" + sumPerCat[4]);
		} else
			lin7.setVisibility(View.GONE);
		if (sumPerCat[5] != 0) {
			lin5.setVisibility(View.VISIBLE);
			t5.setText("" + sumPerCat[5]);
		} else
			lin5.setVisibility(View.GONE);
		if (sumPerCat[6] != 0) {
			lin9.setVisibility(View.VISIBLE);
			t9.setText("" + sumPerCat[6]);
		} else
			lin9.setVisibility(View.GONE);
		if (sumPerCat[7] != 0) {
			lin8.setVisibility(View.VISIBLE);
			t8.setText("" + sumPerCat[7]);
		} else
			lin8.setVisibility(View.GONE);
		if (sumPerCat[8] != 0) {
			lin6.setVisibility(View.VISIBLE);
			t6.setText("" + sumPerCat[8]);
		} else
			lin6.setVisibility(View.GONE);
		if (sumPerCat[9] != 0) {
			lin10.setVisibility(View.VISIBLE);
			t10.setText("" + sumPerCat[9]);
		} else
			lin10.setVisibility(View.GONE);
		// ------------------------------------------------------------------------------------------
		// Generating data by a random loop
		// ------------------------------------------------------------------------------------------
		int q = 0;
		for (Double dbl : sumPerCat) {
			Item = new PieDetailsItem();
			Item.Count = dbl;
			Item.Label = cat[q];
			Item.Color = colors[q];
			PieData.add(Item);
			MaxCount += sumPerCat[q++];
		}
		if (MaxCount == 0) { // ****************
			mImageView.setVisibility(View.INVISIBLE);
			return;
		} else
			mImageView.setVisibility(View.VISIBLE); // **********
		// ------------------------------------------------------------------------------------------
		// OverlayId => Image to be drawn on top of pie to make it more
		// beautiful!
		// ------------------------------------------------------------------------------------------
		int OverlayId = R.drawable.cam_overlay_big;
		// ------------------------------------------------------------------------------------------
		// Size => Pie size
		// ------------------------------------------------------------------------------------------
		int Size = 220;
		// ------------------------------------------------------------------------------------------
		// BgColor => The background Pie Color
		// ------------------------------------------------------------------------------------------
		int BgColor = Color.WHITE;
		// ------------------------------------------------------------------------------------------
		// mBackgroundImage => Temporary image will be drawn with the content of
		// pie view
		// ------------------------------------------------------------------------------------------
		Bitmap mBackgroundImage = Bitmap.createBitmap(Size, Size,
				Bitmap.Config.RGB_565);
		// ------------------------------------------------------------------------------------------
		// Generating Pie view
		// ------------------------------------------------------------------------------------------
		View_PieChart PieChartView = new View_PieChart(this);
		PieChartView.setLayoutParams(new LayoutParams(Size, Size));
		PieChartView.setGeometry(Size, Size, 2, 2, 2, 2, OverlayId);
		PieChartView.setSkinParams(BgColor);
		PieChartView.setData(PieData, MaxCount);
		PieChartView.invalidate();
		// ------------------------------------------------------------------------------------------
		// Draw Pie Vien on Bitmap canvas
		// ------------------------------------------------------------------------------------------
		PieChartView.draw(new Canvas(mBackgroundImage));
		PieChartView = null;
		// ------------------------------------------------------------------------------------------
		// Create a new ImageView to add to main layout
		// ------------------------------------------------------------------------------------------
		// ImageView mImageView = new ImageView(this);
		/*
		 * mImageView.setLayoutParams(new
		 * LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		 */
		mImageView.setBackgroundColor(BgColor);
		mImageView.setImageBitmap(mBackgroundImage);
		// ------------------------------------------------------------------------------------------
		// Finaly add Image View to target view !!!
		// ------------------------------------------------------------------------------------------
		/*
		 * LinearLayout TargetPieView = (LinearLayout)
		 * findViewById(R.id.pie_container); TargetPieView.addView(mImageView);
		 */
	}

	// //////////////////////////////////
	public IDate getDateFrom(IDate date, int period) {
		int year, month, day, i = 0;
		// final Calendar c = Calendar.getInstance();
		year = date.getYear();
		month = date.getMonth();
		day = date.getDay();

		if (period == 0) { // week
			if (day > 7)
				return (new IDate(year, month, (day - 7)));
			else {
				i = 7 - day;
				if ((month == 2) || (month == 4) || (month == 6)
						|| (month == 9) || (month == 11) || (month == 1))
					i = 31 - i;
				else {
					if ((month == 5) || (month == 7) || (month == 10)
							|| (month == 12))
						i = 30 - i;
					else
						i = 28 - i;
				}
				if (month > 1)
					return (new IDate(year, (month - 1), i));
				else { // month == 1
					return (new IDate(year - 1, 12, i));
				}
			}
		}
		if (period == 1) { // month (28 days)
			if (day > 28)
				return (new IDate(year, month, (day - 28)));
			else {
				i = 28 - day;
				if ((month == 2) || (month == 4) || (month == 6)
						|| (month == 9) || (month == 11) || (month == 1))
					i = 31 - i;
				else {
					if ((month == 5) || (month == 7) || (month == 10)
							|| (month == 12))
						i = 30 - i;
					else
						i = 28 - i;
				}
				if (month > 1)
					return (new IDate(year, (month - 1), i));
				else { // month == 1
					return (new IDate(year - 1, 12, i));
				}
			}
		}
		if (period == 2) { // year
			return (new IDate((year - 1), month, day));
		}
		return null;
	}
	// //////////////////////////////////
}