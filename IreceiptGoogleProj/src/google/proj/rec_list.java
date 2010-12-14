package google.proj;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;

import google.proj.R;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.R.layout;

public class rec_list extends Activity {
	iReceipt globalR;
	// List<iReceipt> rec_arr;
	ListView myListView;
	public ArrayAdapter<iReceipt> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reclist);
		myListView = (ListView) findViewById(R.id.ListView01);
		// rec_arr=new ArrayList<iReceipt>();
		/*
		 * idan.rec_arr.add(new iReceipt(new Date(2009, 2, 12), "Sporty's",
		 * 39.99, "Electronics", false, false,
		 * "","hobot",idan.getnewplaceforsave())); idan.rec_arr.add(new
		 * iReceipt(new Date(2010, 8, 25), "MCdonalds", 29.99, "Food", true,
		 * true, "", "bodner",idan.getnewplaceforsave())); idan.rec_arr.add(new
		 * iReceipt(new Date(2010, 8, 16), "BestBuy", 129.99, "Pilot Supplies",
		 * true, true, "", "noiman",idan.getnewplaceforsave()));
		 * idan.rec_arr.add(new iReceipt(new Date(2010, 12, 2), "Tronics",
		 * 329.99, "Computers", true, true, "",
		 * "singer",idan.getnewplaceforsave())); idan.rec_arr.add(new
		 * iReceipt(new Date(2010, 12, 1), "Office Depot", 19.99,
		 * "Office Supplys",true, true, "", "cohen",idan.getnewplaceforsave()));
		 * idan.rec_arr.add(new iReceipt(new Date(2010, 10, 13), "El Al",
		 * 999.99, "Airliner", true, true, "",
		 * "levi",idan.getnewplaceforsave()));
		 */
		// if we open from compute Receipt
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idan.rec_arr.add((iReceipt) extras.get("Receipt"));
			if (extras.getBoolean("man")) {// comes from manual{
				setResult(30);
				finish();
			}
		}

		adapter = new ArrayAdapter<iReceipt>(this,
				android.R.layout.simple_list_item_checked, idan.rec_arr);
		myListView.setAdapter(adapter);
		String[] items = new String[] { "Date", "Total", "Business" };
		Spinner sort = (Spinner) findViewById(R.id.Spinner01listofR);
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
					adapter.notifyDataSetChanged();
				}
				if (pos == 1) {
					Collections.sort(idan.rec_arr, new TotaltComparator());
					adapter.notifyDataSetChanged();
				}
				if (pos == 2) {
					Collections.sort(idan.rec_arr, new NameComparator());
					adapter.notifyDataSetChanged();
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(rec_list.this, rec_view.class);
				iReceipt r = (iReceipt) arg0.getItemAtPosition(arg2);
				;
				i.setFlags(idan.rec_arr.indexOf(r));
				// globalR=r;

				// i.putExtra("Receipt",r);
				if (r.isProcessed()) {
					startActivityForResult(i, idan.rec_arr.indexOf(r));
				} else {
					i.setClass(rec_list.this, compute_receipt.class);
					startActivityForResult(i, idan.rec_arr.indexOf(r));

				}

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
	}

	/*
	 * iReceipt new_r; switch (resultCode){ case 10:{
	 * 
	 * new_r=(iReceipt)data.getExtras().get("Receipt");
	 * adapter.remove(adapter.getItem(adapter.getPosition(globalR)));
	 * adapter.add(new_r); adapter.notifyDataSetChanged();
	 * myListView.refreshDrawableState(); globalR=null; //need to write the new
	 * reciept to disk //saveR(new_r);
	 * //idan.mylistname.add(new_r.getplaceofsave()); //need to write mylistname
	 * to Disk //();
	 * 
	 * return;
	 * 
	 * } case 5:{ new_r=(iReceipt)data.getExtras().get("Receipt");
	 * adapter.remove(adapter.getItem(adapter.getPosition(globalR)));
	 * adapter.add(new_r); adapter.notifyDataSetChanged();
	 * myListView.refreshDrawableState(); globalR=null; //need to write the new
	 * reciept to disk //saveR(new_r);
	 * //idan.mylistname.add(new_r.getplaceofsave()); //need to write mylistname
	 * to Disk //saveList(); return;
	 * 
	 * } case 15: finish();
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	class DateComparator implements Comparator<Receipt> {

		@Override
		public int compare(Receipt object1, Receipt object2) {
			return object1.getRdate().compareTo(object2.getRdate());
		}

	}

	class TotaltComparator implements Comparator<Receipt> {

		@Override
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
		@Override
		public int compare(Receipt object1, Receipt object2) {
			return (object1.getStoreName().compareTo(object2.getStoreName()));
		}

	}

	public void onClick(View view) {
		finish();
	}

	public void onClick2(View view) {
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

	/*
	 * public void saveList(){
	 * 
	 * try{ ObjectOutputStream outputStream = new
	 * ObjectOutputStream(openFileOutput("arrayList.tmp",
	 * Context.MODE_PRIVATE)); outputStream.writeObject(idan.mylistname);
	 * outputStream.close();
	 * 
	 * } catch (IOException ex) { ex.printStackTrace(); }
	 * 
	 * 
	 * 
	 * }
	 */
}