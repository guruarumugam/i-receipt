package misc;

import java.io.File;
import java.util.List;

import google.proj.iReceipt;
import google.proj.idan;

public class Misc {

	
	public static void deleteReceipt(List<iReceipt> list, boolean deleteOnServer){
		if (list==null)
			return;
		for (iReceipt rec : list) {
			deleteReceipt_helper(rec,Settings.deleteOnServer());
		}
		if (deleteOnServer)		
			idan.sync.sendSync();

	}

	public static void deleteReceipt(iReceipt rec){
		deleteReceipt_helper(rec,Settings.deleteOnServer());
		idan.sync.sendSync();
	}

	private static void deleteReceipt_helper(iReceipt rec,boolean deleteOnServer){
		int recIndex=rec.getUniqueIndex();
		if (deleteOnServer){
			idan.sync.addToDeleteList(recIndex+"");
		}
		if (rec.getFilepath()!=null){
			File file=new File(rec.getFilepath());
			file.delete();
		}
		idan.rec_arr.remove(rec);
	}


}
