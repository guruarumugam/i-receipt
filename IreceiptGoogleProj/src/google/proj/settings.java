package google.proj;

import google.proj.R;
import google.proj.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class settings extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
	}
}