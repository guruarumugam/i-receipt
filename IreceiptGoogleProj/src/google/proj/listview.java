package google.proj;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import sync.tset;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.SweepGradient;
import android.view.Gravity;
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
	public static EfficientAdapter EA;
	String search = null;
	IDate date;
	public static List<iReceipt> search_rec_arr;
	static int switcher;
	public static int refresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listview2);
		ListView myListView = (ListView) findViewById(R.id.ListView01);
		if (idan.sync.needtoSync())
			idan.sync.sendSync(loginpage.accountname);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idan.rec_arr.add((iReceipt) extras.get("Receipt"));
			if (extras.getBoolean("man")) {// comes from manual
				setResult(30);
				finish();
			}
		}
		switcher = 0;
		int index = getIntent().getFlags();
		if (index == 5) // back from advance search
			switcher = 2;
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
					else {
						if (switcher == 1)
							Collections.sort(search_rec_arr,
									new DateComparator());
						else
							// switcher == 2
							Collections.sort(searchReceipt.rec_arr_search,
									new DateComparator());
					}
					EA.notifyDataSetChanged();
				}
				if (pos == 1) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new TotaltComparator());
					else {
						if (switcher == 1)
							Collections.sort(search_rec_arr,
									new TotaltComparator());
						else
							// switcher == 2
							Collections.sort(searchReceipt.rec_arr_search,
									new TotaltComparator());
					}
					EA.notifyDataSetChanged();
				}
				if (pos == 2) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new NameComparator());
					else {
						if (switcher == 1)
							Collections.sort(search_rec_arr,
									new NameComparator());
						else
							// switcher == 2
							Collections.sort(searchReceipt.rec_arr_search,
									new NameComparator());
					}
					EA.notifyDataSetChanged();
				}
				if (pos == 3) {
					if (switcher == 0)
						Collections.sort(idan.rec_arr, new FlagedComparator());
					else {
						if (switcher == 1)
							Collections.sort(search_rec_arr,
									new FlagedComparator());
						else
							// switcher == 2
							Collections.sort(searchReceipt.rec_arr_search,
									new FlagedComparator());
					}
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
				iReceipt r;
				Intent i = new Intent(listview.this, rec_view.class);
				if (switcher == 0) {
					r = (iReceipt) idan.rec_arr.get(arg2);
					;
				} else {
					if (switcher == 1) {
						r = (iReceipt) search_rec_arr.get(arg2);
						;
					} else { // switcher==2
						r = (iReceipt) searchReceipt.rec_arr_search.get(arg2);
						;
					}
				}
				i.setFlags(idan.rec_arr.indexOf(r));
				if (r.isProcessed()) {
					startActivityForResult(i, 1);
					// startActivity(i);
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

	class FlagedComparator implements Comparator<Receipt> {
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
				return -1;
			else if (object1.getTotal() < object2.getTotal())
				return 1;
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
			else {
				if (switcher == 1)
					return search_rec_arr.size();
				else
					// switcher == 2
					return searchReceipt.rec_arr_search.size();
			}
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
				if (switcher == 1) {
					holder.text1.setText(search_rec_arr.get(position)
							.getStoreName());
					holder.text2.setText(Double.toString(search_rec_arr.get(
							position).getTotal()));
					holder.text3.setText(search_rec_arr.get(position)
							.getCategory());
					holder.text4.setText(search_rec_arr.get(position)
							.getRdate().toString());
					holder.text5.setChecked(search_rec_arr.get(position)
							.isFlaged());
				} else {
					holder.text1.setText(searchReceipt.rec_arr_search.get(
							position).getStoreName());
					holder.text2.setText(Double
							.toString(searchReceipt.rec_arr_search
									.get(position).getTotal()));
					holder.text3.setText(searchReceipt.rec_arr_search.get(
							position).getCategory());
					holder.text4.setText(searchReceipt.rec_arr_search
							.get(position).getRdate().toString());
					holder.text5.setChecked(searchReceipt.rec_arr_search.get(
							position).isFlaged());
				}
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

			public void onClick(View v) {
				search_rec_arr.clear();
				search = text.getText().toString().toLowerCase();
				date = getDate(search);
				// Search function!!!!!!!!!!!!!!!!!!!!!!
				for (int j = 0; j < idan.rec_arr.size(); j++) {
					if ((idan.rec_arr.get(j).getStoreName().toLowerCase()
							.indexOf(search) != -1)
							|| (idan.rec_arr.get(j).getCategory().toLowerCase()
									.indexOf(search) != -1)
							|| (idan.rec_arr.get(j).getNotes().toLowerCase()
									.indexOf(search) != -1)
							|| Double.toString(idan.rec_arr.get(j).getTotal())
									.indexOf(search) != -1)
						search_rec_arr.add(idan.rec_arr.get(j));
					if (date != null) {
						if (idan.rec_arr.get(j).getRdate().compareTo(date) == 0)
							search_rec_arr.add(idan.rec_arr.get(j));
					}
				}
				dialog1.dismiss();
				switcher = 1;
				EA.notifyDataSetChanged();
				if (search_rec_arr.size() == 0) {
					/*
					 * CustomizeDialog customizeDialog = new
					 * CustomizeDialog(this, "Sorry, No match for: \"" + search
					 * + "\""); customizeDialog.show();
					 */
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast_layout,
							(ViewGroup) findViewById(R.id.toast_layout_root));
					TextView text2 = (TextView) layout.findViewById(R.id.text);
					text2.setText("Sorry, No match for: \"" + search + "\"");
					Toast toast2 = new Toast(getApplicationContext());
					toast2.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast2.setDuration(Toast.LENGTH_LONG);
					toast2.setView(layout);
					toast2.show();
				}
			}
		});
		// b1 is AdvanceSearch button
		Button b2 = (Button) dialog1.findViewById(R.id.ButtonAdvanceSearch);
		b2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog1.dismiss();
				search_rec_arr.clear();
				// intent for the advance search!!!!!!!!!!!!!!!!!!!!!!
				switcher = 1;
				Intent i = new Intent(listview.this, searchReceipt.class);
				startActivity(i);
				/*
				 * final Dialog dialog5 = new Dialog(listview.this);
				 * dialog5.setContentView(R.layout.searchreceipt);
				 * dialog5.requestWindowFeature
				 * (dialog5.getWindow().FEATURE_NO_TITLE); dialog5.show();
				 */
				// EA.notifyDataSetChanged();
			}
		});
		// return search_rec_arr;
	}

	public IDate getDate(String str) { // 0-9 or "-" or "/"

		char[] ch = str.toCharArray();
		int len = ch.length;
		if (len != 10)
			return null; // gets only "27-12-2010" or "07/02/2011", or not a
							// date.
		if (((ch[0] > 47) && (ch[0] < 58) && (ch[1] > 47) && (ch[1] < 58)))
			;
		else
			return null;
		if ((ch[2] == 45) || (ch[2] == 47))
			;
		else
			return null;
		if (((ch[3] > 47) && (ch[3] < 58) && (ch[4] > 47) && (ch[4] < 58)))
			;
		else
			return null;
		if ((ch[5] == 45) || (ch[5] == 47))
			;
		else
			return null;
		if (((ch[6] > 47) && (ch[6] < 58) && (ch[7] > 47) && (ch[8] < 58)))
			;
		else
			return null;
		if (((ch[8] > 47) && (ch[8] < 58) && (ch[9] > 47) && (ch[9] < 58)))
			;
		else
			return null;

		int month = (ch[0] - 48) * 10 + (ch[1] - 48);
		int day = (ch[3] - 48) * 10 + (ch[4] - 48);
		int year = (ch[6] - 48) * 1000 + (ch[7] - 48) * 100 + (ch[8] - 48) * 10
				+ (ch[9] - 48);
		IDate newDate = new IDate(year, month, day);
		return newDate;
	}
}