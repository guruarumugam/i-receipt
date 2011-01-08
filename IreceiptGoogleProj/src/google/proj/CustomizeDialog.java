package google.proj;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CustomizeDialog extends Dialog implements OnClickListener {
	Button okButton;

	public CustomizeDialog(Context context, String str) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.error_price_date);
		okButton = (Button) findViewById(R.id.OkButtonErrorPrice);
		TextView text = (TextView) findViewById(R.id.error_massage);
		text.setText(str);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == okButton)
			dismiss();
	}
}