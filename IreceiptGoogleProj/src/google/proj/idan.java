package google.proj;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import google.proj.R;

import java.util.ArrayList;
import java.util.List;

public class idan extends Activity {

	// private static int receitnumber=1;
	// public static List<String> mylistname=new ArrayList<String>();//list of
	// places in memory
	// public static boolean scan=true;
	public static List<iReceipt> rec_arr;

	// private ImageButton new_scan, manual, receipts, stats;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		rec_arr = loadList();
		// if (rec_arr==null)//-------------------------------------
		rec_arr = new ArrayList<iReceipt>();

		iReceipt rec1 = (new iReceipt(new IDate(2010, 12, 3), "AppleStore",
				1.99, "music", true, true, "", "/data/4.jpg"));
		iReceipt rec2 = (new iReceipt(new IDate(2010, 10, 16), "BP-Gass", 19.92,
				"fuel", false, true, "", "/data/2.jpg"));
		iReceipt rec3 = (new iReceipt(new IDate(2010, 11, 18), "Chang-Mai ",
				15.00, "Thai food", false, true, "", "/data/5.jpg"));
		iReceipt rec4 = (new iReceipt(new IDate(2010, 11, 19), "Target", 3.00,
				"Shops", false, true, "", "/data/6.jpg"));
		iReceipt rec5 = (new iReceipt(new IDate(2010, 12, 4), "Sony", 99.99,
				"Electricity", true, true, "", "/data/3.jpg"));
		iReceipt rec6 = (new iReceipt(new IDate(2010, 12, 5), " ", -1, " ",
				false, false, "", "/data/111.jpg"));
		iReceipt rec7 = (new iReceipt(new IDate(2010, 12, 6), " ", -1, " ",
				false, false, "", "/data/1.jpg")); // ------- to test
		rec_arr.add(rec1);
		rec_arr.add(rec2);
		rec_arr.add(rec3);
		rec_arr.add(rec4);
		rec_arr.add(rec5);
		rec_arr.add(rec6);
		rec_arr.add(rec7);
	}

	/*
	 * String tmp=loadHandler(); if (tmp==null){//never write before;
	 * 
	 * //need to write for the first time ito Disk saveHandler(); saveList();
	 * scan=false; receitnumber=Integer.parseInt(tmp); }
	 * 
	 * if (scan){// call first time to this application
	 * 
	 * scan=false; mylistname=loadList();
	 * 
	 * // loop that read al receipt and add them to list for (int i=0;
	 * i<mylistname.size();i++){ rec_arr.add(loadR(mylistname.get(i))); } }
	 */

	public void new_scan_handler(View view) {
		// Intent i = new Intent(idan.this, idan.class);
		// startActivityForResult(i, 0);
		finish();
	}

	public void manual_scan_handler(View view) {
		// saveHandler();
		iReceipt r = new iReceipt();
		// mylistname.add(r.getplaceofsave());
		// ----------->r.setFilepath("");
		Intent i = new Intent(idan.this, compute_receipt.class);
		// i.putExtra("Receipt", r);
		// i.putExtra("man",true);
		rec_arr.add(r);
		int index = rec_arr.indexOf(r);
		i.setFlags(index);
		startActivityForResult(i, index);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// in case we cancel in compute_receipt
		if (resultCode==0){
			rec_arr.remove(requestCode);
		}
	}

	public void receipts_handler(View view) {
		Intent i = new Intent(idan.this, rec_list.class);
		startActivity(i);
	}

	public void statistics_handler(View view) {
		Intent i = new Intent(idan.this, statistics.class);
		startActivity(i);
	}

	/*
	 * public static String getnewplaceforsave(){
	 * 
	 * String ret=String.valueOf(idan.receitnumber-1)+".tmp"; receitnumber++;
	 * return ret;
	 * 
	 * }
	 */

	/*
	 * public String loadHandler() { String str[]; String ret=null; try{
	 * ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput("receitnumber.tmp"));
	 * str=(String[])inputStream.readObject(); ret=str[0];
	 * 
	 * inputStream.close(); } catch (IOException ex) { return null; } catch
	 * (ClassNotFoundException e) { // TODO Auto-generated catch block return
	 * null; } return ret; }
	 * 
	 * @SuppressWarnings("unchecked") public ArrayList<String> loadList() {
	 * ArrayList<String> ret=null; try{ ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput("arrayList.tmp"));
	 * ret=(ArrayList<String>)inputStream.readObject();
	 * 
	 * inputStream.close(); } catch (IOException ex) { ex.printStackTrace(); }
	 * catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return ret; } public iReceipt loadR(String place){
	 * 
	 * 
	 * iReceipt ret=null; try{ ObjectInputStream inputStream = new
	 * ObjectInputStream(openFileInput(place));
	 * ret=(iReceipt)inputStream.readObject();
	 * 
	 * inputStream.close(); } catch (IOException ex) { ex.printStackTrace(); }
	 * catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return ret; }
	 * 
	 * //write for the first time reciept number static file public void
	 * saveHandler() { String str[]=new String[1];
	 * str[0]=String.valueOf(receitnumber); try{ ObjectOutputStream outputStream
	 * = new ObjectOutputStream(openFileOutput("receitnumber.tmp",
	 * Context.MODE_PRIVATE)); outputStream.writeObject(str);
	 * outputStream.close();
	 * 
	 * } catch (IOException ex) { ex.printStackTrace(); } }
	 */

	public ArrayList<iReceipt> loadList() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					openFileInput("RecList1.tmp"));
			ArrayList<iReceipt> rec_arr_tmp = (ArrayList<iReceipt>) inputStream
					.readObject();
			inputStream.close();
			return rec_arr_tmp;

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}
}
