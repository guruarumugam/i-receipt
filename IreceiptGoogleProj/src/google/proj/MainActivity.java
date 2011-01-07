package google.proj;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
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
			Color.parseColor("#00ced1"), Color.RED, Color.LTGRAY, Color.BLACK,
			Color.parseColor("#8b4513"), Color.parseColor("#228b22") };
	List<PieDetailsItem> PieData = new ArrayList<PieDetailsItem>(0);
	// //////////////////////
	public static int period = 3; // 0 => last week, 1=> last month, 2=>last
									// year 3=>not initialized yet
	public static List<iReceipt> pie_rec_arr = new ArrayList<iReceipt>();
	private int Year, Month, Day;
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;

	// //////////////////////

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.piechart);
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
		RadioGroup rg = (RadioGroup) findViewById(R.id.RadioGroup01);
		RadioButton r1 = (RadioButton) findViewById(R.id.RadioButton01);
		RadioButton r2 = (RadioButton) findViewById(R.id.RadioButton02);
		RadioButton r3 = (RadioButton) findViewById(R.id.RadioButton03);

		// ------------------------------------------------------------------------------------------
		// Used vars declaration
		// ------------------------------------------------------------------------------------------
		if (period == 3)
			r1.setChecked(true);
		if (r1.isChecked())
			period = 0;
		if (r2.isChecked())
			period = 1;
		if (r3.isChecked())
			period = 2;
		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				int id = arg1;
				if (id == 2131099710)
					period = 0;
				if (id == 2131099711)
					period = 1;
				if (id == 2131099712)
					period = 2;
				makePie(period);
			}
		});
		makePie(period);
	}

	public void makePie(int period) {
		PieDetailsItem Item;
		int mNumGen = 10;
		int MaxPieItems = cat.length;
		Double MaxCount = 0.0;
		// ///////////////////////////////////////////////////////////////
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH + 1);
		Day = c.get(Calendar.DAY_OF_MONTH);
		IDate dateToday = new IDate(Year, Month, Day);// today
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
			if (rec.getCategory() == "Dining")
				sumPerCat[0] += rec.getTotal();
			if (rec.getCategory() == "Car")
				sumPerCat[1] += rec.getTotal();
			if (rec.getCategory() == "Travel")
				sumPerCat[2] += rec.getTotal();
			if (rec.getCategory() == "Shopping")
				sumPerCat[3] += rec.getTotal();
			if (rec.getCategory() == "Rent")
				sumPerCat[4] += rec.getTotal();
			if (rec.getCategory() == "Groceries")
				sumPerCat[5] += rec.getTotal();
			if (rec.getCategory() == "Presents")
				sumPerCat[6] += rec.getTotal();
			if (rec.getCategory() == "Entertainment")
				sumPerCat[7] += rec.getTotal();
			if (rec.getCategory() == "Household goods")
				sumPerCat[8] += rec.getTotal();
			if (rec.getCategory() == "Other")
				sumPerCat[9] += rec.getTotal();
		}
		t1.setText("" + sumPerCat[0]);
		t2.setText("" + sumPerCat[3]);
		t3.setText("" + sumPerCat[1]);
		t4.setText("" + sumPerCat[2]);
		t5.setText("" + sumPerCat[5]);
		t6.setText("" + sumPerCat[8]);
		t7.setText("" + sumPerCat[4]);
		t8.setText("" + sumPerCat[7]);
		t9.setText("" + sumPerCat[6]);
		t10.setText("" + sumPerCat[9]);
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
		ImageView mImageView = (ImageView) findViewById(R.id.piecontainer);
		//ImageView mImageView = new ImageView(this);
		/*mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));*/
		mImageView.setBackgroundColor(BgColor);
		mImageView.setImageBitmap(mBackgroundImage);		
		// ------------------------------------------------------------------------------------------
		// Finaly add Image View to target view !!!
		// ------------------------------------------------------------------------------------------
		/*LinearLayout TargetPieView = (LinearLayout) findViewById(R.id.pie_container);
		TargetPieView.addView(mImageView);*/
	}

	// //////////////////////////////////
	public IDate getDateFrom(IDate date, int period) {
		int year, month, day, i = 0;
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