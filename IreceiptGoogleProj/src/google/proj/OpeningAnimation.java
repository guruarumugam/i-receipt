package google.proj;
import misc.Misc;
import sync.Syncer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
public class OpeningAnimation extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.openinganimation);
		Animation firstAnimation = AnimationUtils.loadAnimation(this, R.anim.first_animation);
		ImageView imageView = (ImageView) findViewById(R.id.ImageView01);
		imageView.startAnimation(firstAnimation);
		(new syncthread()).execute();
	}



	class syncthread extends AsyncTask<Void, Void, Void> {

		protected ProgressDialog progressDialog;

		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected Void doInBackground(Void... params) {
			//Syncer.this.syncthread.suspend();
			idan.sync.sendSync_h();
			Misc.saveList(OpeningAnimation.this);
			//Syncer.this.syncthread.resume();
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(null);
			finish();


		}
	}
}