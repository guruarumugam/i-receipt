package google.proj;

import java.util.Calendar;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.error_price_date);
		okButton = (Button) findViewById(R.id.OkButtonErrorPrice);
		TextView text = (TextView) findViewById(R.id.error_massage);
		text.setText(str);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (v == okButton)
					dismiss();
			}
		});
		// okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	/*
	 * public void onClick(View v) { if (v == okButton) dismiss(); }
	 */
}