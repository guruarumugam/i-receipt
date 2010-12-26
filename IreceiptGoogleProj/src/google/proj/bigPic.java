package google.proj;

import google.proj.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class bigPic extends Activity {
	private iReceipt rec;
	private int index;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rec_pic);
		// ImageSwitcher iSwitcher = (ImageSwitcher)
		// findViewById(R.id.ImageBig);
		// iSwitcher.setFactory(this);
		index = getIntent().getFlags();
		rec = (iReceipt) idan.rec_arr.get(index);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=3;
		Bitmap bMap = BitmapFactory.decodeFile(rec.getFilepath(),options);
		// int theID = getIntent().getExtras().getInt("image_id");
		ImageView bigImage = (ImageView) findViewById(R.id.ImageBig);
		bigImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		bigImage.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		bigImage.setBackgroundColor(0xFF000000);
		// bigImage.setImageResource(bMap);
		bigImage.setImageBitmap(bMap);
		// iSwitcher.setImageURI(bMap);
		// Uri imgUri=Uri.parse("file:///data/rec13.jpg");
		// iSwitcher.setImageUri(imgUri);
		// iSwitcher.setImageResource(pics[arg2]);
	}

	public void onClick(View view) { // BACK button
		/*setResult(1);
		Intent i = new Intent(bigPic.this, rec_view.class);
		i.setFlags(idan.rec_arr.indexOf(rec));
		startActivityForResult(i, idan.rec_arr.indexOf(rec));*/
	}
}