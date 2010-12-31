package google.proj;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.SweepGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class listview extends Activity {
	iReceipt globalR;
	ListView myListView;
	EfficientAdapter EA;
	String search = null;
	public static List<iReceipt> search_rec_arr;
	static int switcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listview2);
		ListView myListView = (ListView) findViewById(R.id.ListView01);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idan.rec_arr.add((iReceipt) extras.get("Receipt"));
			if (extras.getBoolean("man")) {// comes from manual{
				setResult(30);
				finish();
			}
		}
		switcher = 0;
		search_rec_arr = new ArrayList<iReceipt>();
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
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new DateComparator());
					else
						Collections.sort(search_rec_arr, new DateComparator());
					// EA.notifyDataSetChanged();
				}
				if (pos == 1) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new TotaltComparator());
					else
						Collections.sort(search_rec_arr, new DateComparator());
					// EA.notifyDataSetChanged();
				}
				if (pos == 2) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new NameComparator());
					else
						Collections.sort(search_rec_arr, new DateComparator());
					// EA.notifyDataSetChanged();
				}
				if (pos == 3) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new FlagedComparetor());
					else
						Collections.sort(search_rec_arr, new DateComparator());
					// EA.notifyDataSetChanged();
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
					startActivity(i);
					// startActivityForResult(i, idan.rec_arr.indexOf(r));
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
			if (switcher == 0)
				return idan.rec_arr.size();
			else
				return search_rec_arr.size();

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
			if (switcher == 0) {
				holder.text1.setText(idan.rec_arr.get(position).getStoreName());
				holder.text2.setText(Double.toString(idan.rec_arr.get(position)
						.getTotal()));
				holder.text3.setText(idan.rec_arr.get(position).getCategory());
				holder.text4.setText(idan.rec_arr.get(position).getRdate()
						.toString());
				holder.text5.setChecked(idan.rec_arr.get(position).isFlaged());
			} else {
				holder.text1.setText(search_rec_arr.get(position)
						.getStoreName());
				holder.text2.setText(Double.toString(search_rec_arr.get(
						position).getTotal()));
				holder.text3
						.setText(search_rec_arr.get(position).getCategory());
				holder.text4.setText(search_rec_arr.get(position).getRdate()
						.toString());
				holder.text5
						.setChecked(search_rec_arr.get(position).isFlaged());
			}

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

	public void search(View view) {
		final Dialog dialog1 = new Dialog(this);
		dialog1.setContentView(R.layout.search);
		dialog1.setTitle("Search receipt:");
		dialog1.show();

		final EditText text = (EditText) dialog1.findViewById(R.id.Editsearch);

		// b1 is search button
		Button b1 = (Button) dialog1.findViewById(R.id.ButtonSearch);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search_rec_arr.clear();
				search = text.getText().toString();
				// Search function!!!!!!!!!!!!!!!!!!!!!!
				for (int j = 0; j < idan.rec_arr.size(); j++) {
					if ((idan.rec_arr.get(j).getStoreName().indexOf(search) != -1)
							|| (idan.rec_arr.get(j).getCategory()
									.indexOf(search) != -1)
							|| (idan.rec_arr.get(j).getNotes().indexOf(search) != -1)
							|| Double.toString(idan.rec_arr.get(j).getTotal())
									.indexOf(search) != -1)
						// add Idate compare & search
						search_rec_arr.add(idan.rec_arr.get(j));
				}
				dialog1.dismiss();
				switcher = 1;
				listChange();
			}
		});
		// b1 is AdvanceSearch button
		Button b2 = (Button) dialog1.findViewById(R.id.ButtonAdvanceSearch);
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search_rec_arr.clear();
				search = text.getText().toString();
				dialog1.dismiss();
				// intent for the advance search!!!!!!!!!!!!!!!!!!!!!!
				switcher = 1;
				listChange();
			}
		});
		// return search_rec_arr;
	}

	public void listChange() {
		// EA = new EfficientAdapter(listview.this);
		// myListView.setAdapter(EA);
		EA.notifyDataSetChanged();
	}
}