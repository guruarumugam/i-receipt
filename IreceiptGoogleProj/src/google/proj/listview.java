package google.proj;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class listview extends Activity {
	iReceipt globalR;
	ListView myListView;
	EfficientAdapter EA;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview2);
		ListView myListView = (ListView) findViewById(R.id.ListView01);
		//
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idan.rec_arr.add((iReceipt) extras.get("Receipt"));
			if (extras.getBoolean("man")) {// comes from manual{
				setResult(30);
				finish();
			}
		}
		EA = new EfficientAdapter(this);
		myListView.setAdapter(EA);
		String[] items = new String[] { "Date", "Total", "Business", "Flaged" };
		Spinner sort = (Spinner) findViewById(R.id.Spinner01list);
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort.setAdapter(spinner_adapter);
		sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 0) {
					Collections.sort(idan.rec_arr, new DateComparator());
					EA.notifyDataSetChanged();
				}
				if (pos == 1) {
					Collections.sort(idan.rec_arr, new TotaltComparator());
					EA.notifyDataSetChanged();
				}
				if (pos == 2) {
					Collections.sort(idan.rec_arr, new NameComparator());
					EA.notifyDataSetChanged();
				}
				if (pos == 3) {
					Collections.sort(idan.rec_arr, new FlagedComparetor());
					EA.notifyDataSetChanged();
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		myListView.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*
				 * Toast.makeText(getBaseContext(), " "+arg2+" "+arg3,
				 * Toast.LENGTH_LONG) .show();
				 */
				Intent i = new Intent(listview.this, rec_view.class);
				iReceipt r = (iReceipt) idan.rec_arr.get(arg2);
				;
				i.setFlags(idan.rec_arr.indexOf(r));
				if (r.isProcessed()) {
					startActivityForResult(i, idan.rec_arr.indexOf(r));
				} else {
					i.setClass(listview.this, compute_receipt.class);
					startActivityForResult(i, idan.rec_arr.indexOf(r));
				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EA.notifyDataSetChanged();
	}

	class DateComparator implements Comparator<Receipt> {
		public int compare(Receipt object1, Receipt object2) {
			return object1.getRdate().compareTo(object2.getRdate());
		}
	}

	class FlagedComparetor implements Comparator<Receipt> {
		public int compare(Receipt object1, Receipt object2) {
			if (((object1.isFlaged()) && (object2.isFlaged()))
					|| ((!object1.isFlaged()) && (!object2.isFlaged())))
				return object1.getRdate().compareTo(object2.getRdate());
			else if ((object2.isFlaged()) && (!object1.isFlaged()))
				return 1;
			else
				return -1; // ((!object2.isFlaged()) && (object1.isFlaged()))
		}
	}

	class TotaltComparator implements Comparator<Receipt> {
		public int compare(Receipt object1, Receipt object2) {
			if (object1.getTotal() > object2.getTotal())
				return 1;
			else if (object1.getTotal() < object2.getTotal())
				return -1;
			else
				return 0;
		}
	}

	class NameComparator implements Comparator<Receipt> {
		public int compare(Receipt object1, Receipt object2) {
			return (object1.getStoreName().compareTo(object2.getStoreName()));
		}
	}

	public void onClick(View view) {
		finish();
	}

	// write for the first time reciept number static file
	public void saveR(iReceipt r) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					openFileOutput(r.getplaceofsave(), Context.MODE_PRIVATE));
			outputStream.writeObject(r);
			outputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return idan.rec_arr.size();

		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView
						.findViewById(R.id.TextView01);
				holder.text2 = (TextView) convertView
						.findViewById(R.id.TextView02);
				holder.text3 = (TextView) convertView
						.findViewById(R.id.TextView03);
				holder.text4 = (TextView) convertView
						.findViewById(R.id.TextView04);
				holder.text5 = (CheckedTextView) convertView
						.findViewById(R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text1.setText(idan.rec_arr.get(position).getStoreName());
			holder.text2.setText(Double.toString(idan.rec_arr.get(position)
					.getTotal()));
			holder.text3.setText(idan.rec_arr.get(position).getCategory());
			holder.text4.setText(idan.rec_arr.get(position).getRdate()
					.toString());
			holder.text5.setChecked(idan.rec_arr.get(position).isFlaged());

			return convertView;
		}

		static class ViewHolder {
			TextView text1;
			TextView text2;
			TextView text3;
			TextView text4;
			CheckedTextView text5;
		}
	}
}