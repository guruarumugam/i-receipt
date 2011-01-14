package sync;

import java.io.File;

import google.proj.iReceipt;
import google.proj.idan;

public class Misc {
	
	public static void deleteReceipt(iReceipt rec){
		int recIndex=rec.getUniqueIndex();
		if (Settings.deleteOnServer()){
			idan.sync.addToDeleteList(recIndex+"");
			idan.sync.sendSync();
		}
		if (rec.getFilepath()!=null){
			File file=new File(rec.getFilepath());
			file.delete();
		}
		idan.rec_arr.remove(rec);
	}

}
