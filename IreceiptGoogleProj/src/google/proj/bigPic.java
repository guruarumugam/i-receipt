package google.proj;

import google.proj.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class bigPic extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_pic);
        int theID = getIntent().getExtras().getInt("image_id");
        ImageView bigImage = (ImageView) findViewById(R.id.ImageBig);
        bigImage.setImageResource(theID);
    }
    
    public void onClick(View view){
    	finish();
    }
}