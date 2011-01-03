package google.proj;

import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.SimpleZoomListener;
import com.sonyericsson.zoom.ZoomState;
import com.sonyericsson.zoom.SimpleZoomListener.ControlType;

import google.proj.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class bigPic extends Activity {
	private iReceipt rec;
	private int index;

	/** Constant used as menu item id for setting zoom control type */
	private static final int MENU_ID_ZOOM = 0;

	/** Constant used as menu item id for setting pan control type */
	private static final int MENU_ID_PAN = 1;

	/** Constant used as menu item id for resetting zoom state */
	private static final int MENU_ID_RESET = 2;

	/** Image zoom view */
	private ImageZoomView mZoomView;

	/** Zoom state */
	private ZoomState mZoomState;

	/** Decoded bitmap image */
	private Bitmap mBitmap;

	/** On touch listener for zoom view */
	private SimpleZoomListener mZoomListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recpicview);
		// ImageSwitcher iSwitcher = (ImageSwitcher)
		// findViewById(R.id.ImageBig);
		// iSwitcher.setFactory(this);
		mZoomState = new ZoomState();
		index = getIntent().getFlags();
		rec = (iReceipt) idan.rec_arr.get(index);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap mBitmap = BitmapFactory.decodeFile(rec.getFilepath(), options);
		// ImageView bigImage = (ImageView) findViewById(R.id.ImageBig);
		// bigImage.setScaleType(ImageView.ScaleType.FIT_START);
		// bigImage.setLayoutParams(new ImageSwitcher.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// bigImage.setBackgroundColor(0xFF000000);
		// bigImage.setImageBitmap(bMap);
		mZoomListener = new SimpleZoomListener();
		mZoomListener.setZoomState(mZoomState);

		mZoomView = (ImageZoomView) findViewById(R.id.zoomview);
		mZoomView.setZoomState(mZoomState);
		mZoomView.setImage(mBitmap);
		mZoomView.setOnTouchListener(mZoomListener);

		resetZoomState();
	}

	public void onClick(View view) { // BACK button
		/*
		 * setResult(1); Intent i = new Intent(bigPic.this, rec_view.class);
		 * i.setFlags(idan.rec_arr.indexOf(rec)); startActivityForResult(i,
		 * idan.rec_arr.indexOf(rec));
		 */
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBitmap.recycle();
		mZoomView.setOnTouchListener(null);
		mZoomState.deleteObservers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ID_ZOOM, 0, R.string.menu_zoom);
		menu.add(Menu.NONE, MENU_ID_PAN, 1, R.string.menu_pan);
		menu.add(Menu.NONE, MENU_ID_RESET, 2, R.string.menu_reset);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_ZOOM:
			mZoomListener.setControlType(ControlType.ZOOM);
			break;

		case MENU_ID_PAN:
			mZoomListener.setControlType(ControlType.PAN);
			break;

		case MENU_ID_RESET:
			resetZoomState();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Reset zoom state and notify observers
	 */
	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}
}