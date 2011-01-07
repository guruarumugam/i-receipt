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
	int period = 0; // 0 => last week, 1=> last month, 2=>last year
	public static List<iReceipt> pie_rec_arr = new ArrayList<iReceipt>();
	private int Year, Month, Day;

	// //////////////////////

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.piechart);
		TextView t1 = (TextView) findViewById(R.id.TextView03);
		TextView t2 = (TextView) findViewById(R.id.TextView04);
		TextView t3 = (TextView) findViewById(R.id.TextView05);
		TextView t4 = (TextView) findViewById(R.id.TextView06);
		TextView t5 = (TextView) findViewById(R.id.TextView07);
		TextView t6 = (TextView) findViewById(R.id.TextView08);
		TextView t7 = (TextView) findViewById(R.id.TextView09);
		TextView t8 = (TextView) findViewById(R.id.TextView10);
		TextView t9 = (TextView) findViewById(R.id.TextView13);
		TextView t10 = (TextView) findViewById(R.id.TextView14);
		RadioButton r1 = (RadioButton) findViewById(R.id.RadioButton01);
		RadioButton r2 = (RadioButton) findViewById(R.id.RadioButton02);
		RadioButton r3 = (RadioButton) findViewById(R.id.RadioButton03);
		// ------------------------------------------------------------------------------------------
		// Used vars declaration
		// ------------------------------------------------------------------------------------------
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
		// last week

		// last month

		// last year

		// ///////////////////////////////////////////////////////////////

		Double sumPerCat[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		for (iReceipt rec : idan.rec_arr) {
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
		ImageView mImageView = new ImageView(this);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mImageView.setBackgroundColor(BgColor);
		mImageView.setImageBitmap(mBackgroundImage);
		// ------------------------------------------------------------------------------------------
		// Finaly add Image View to target view !!!
		// ------------------------------------------------------------------------------------------
		LinearLayout TargetPieView = (LinearLayout) findViewById(R.id.pie_container);
		TargetPieView.addView(mImageView);
	}

	// //////////////////////////////////
	public IDate getDateFrom(IDate date, int period) {

		return null;
	}
	// //////////////////////////////////
}