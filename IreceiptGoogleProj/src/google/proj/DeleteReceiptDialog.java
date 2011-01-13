package google.proj;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DeleteReceiptDialog extends Dialog implements OnClickListener {
	Button deleteButton, cancelButton;

	public DeleteReceiptDialog(Context context, String str) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deleterec);
		deleteButton = (Button) findViewById(R.id.OkDelete);
		cancelButton = (Button) findViewById(R.id.cancelDelete);
		TextView text = (TextView) findViewById(R.id.error_massage);
		text.setText(str);
		deleteButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	
	public void onClick(View v) {
		if (v == deleteButton) {
			dismiss();
			// *************** return to the rec_view and should add
			// deleteFunc();
		}
		if (v == cancelButton)
			dismiss();
	}
}