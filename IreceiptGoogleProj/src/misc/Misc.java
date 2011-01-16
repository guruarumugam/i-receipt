package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import google.proj.iReceipt;
import google.proj.idan;

public class Misc {

	private static Date date=null;
	
	
	public static void makeDelete(){
		
		
		List<iReceipt> list=new ArrayList<iReceipt>();
		for (iReceipt rec : idan.rec_arr){
			
			if(idan.settings.needToDelete(rec)){
				list.add(rec);
			}
		}
		deleteReceipt(list,idan.settings.getDeleteOnSerever());
		date=new Date();
	}
	
	
	public static void deleteReceipt(List<iReceipt> list, boolean deleteOnServer){
		if (list==null)
			return;
		for (iReceipt rec : list) {
			deleteReceipt_helper(rec,Settings.deleteOnServer());
		}
		if (deleteOnServer)		
			idan.sync.sendSync();

	}

	//important: this method deletes the receipt from both the server and yhe device
	public static void deleteReceipt(iReceipt rec){
		deleteReceipt_helper(rec,Settings.deleteOnServer());
		idan.sync.sendSync();
	}

	private static void deleteReceipt_helper(iReceipt rec,boolean deleteOnServer){
		int recIndex=rec.getUniqueIndex();
		if (deleteOnServer){
			idan.sync.addToDeleteList(recIndex+"");
		}
		if (rec.getFilepath()!=null&&rec.getFilepath()!=""){
			File file=new File(rec.getFilepath());
			file.delete();
		}
		idan.rec_arr.remove(rec);
	}
	
	public static  boolean needToCheckDelete(){

		if (date==null)
			return true;
		else if ((new Date()).getTime()- date.getTime() > 86400000)
			return true;
		else return false;
		
	}
	
	
	public static void saveList(Activity act){
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					act.openFileOutput("RecListsave.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject(idan.rec_arr);
			outputStream.close();
			outputStream = new ObjectOutputStream(act.openFileOutput(
					"recIndexsave.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject((Integer) idan.receiptUniqueIndex);
			outputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static boolean chekConnection(Activity act){
		
		boolean connected = false;
		ConnectivityManager mConnectivity = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting())
			connected = false;
		else {
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				connected = info.isConnected();
			}
			if (!connected && netType == ConnectivityManager.TYPE_MOBILE
					&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				connected = info.isConnected();
			}
		}
		
		return connected;
		
	}
	
	public static void saveSetting(Activity act){
		
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					act.openFileOutput("Settings.tmp", Context.MODE_PRIVATE));
			outputStream.writeObject(idan.settings);
			outputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static Settings loadSetting(Activity act){
		try {
			
			FileInputStream ofi=act.openFileInput("Settings.tmp");
			if (ofi==null)
				return null ;
			
			ObjectInputStream inputStream = new ObjectInputStream(ofi);
			
			Settings settmp = (Settings) inputStream
			.readObject();
			if (settmp ==null ){
				inputStream.close();
				return null ;
			}
			inputStream.close();
			
		return settmp;
		}
		
		catch (FileNotFoundException ex){
			return  null;
		}
		catch (IOException ex) {
			return null ;
		} catch (ClassNotFoundException e) {
		 return null ;
		}
	}
	
	
	public static List<iReceipt> loadList(Activity act){
		
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					act.openFileInput("RecListsave.tmp"));
			@SuppressWarnings("unchecked")
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
	
	
	
	
	public static int loadreceiptUniqueIndex(Activity act){
		
		
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					act.openFileInput("recIndexsave.tmp"));
			Integer tmp = (Integer) inputStream.readObject();
			inputStream.close();
			
			return tmp;

		} catch (IOException ex) {
			ex.printStackTrace();
			return 0;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
