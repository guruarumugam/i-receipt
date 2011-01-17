package google.proj;

import google.proj.R;
import google.proj.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class aboutireceipt extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutireceipt);
		TextView about = (TextView) findViewById(R.id.about);
		String str;
		str = "iReceipt BETA\nversion mars release1\nBuild id: 20100917-0705\n(c) copyright iReceipt team 2011 all rights receieved.\nvisit http://ireceipt.assaf.in";
		about.setText(str);
	}
}