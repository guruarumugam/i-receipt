package sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Base64;

import google.proj.IDate;
import google.proj.iReceipt;

public class tset {
	public static void main1() {
		iReceipt rec = new iReceipt(new IDate(2011, 1, 4),
				"test", 11.11, "Dining", false, true, "", null);		
		try {
			byte[] by=rec.toByteArray();
			
			String str=new String (rec.toByteArray(),"utf-8");
			rec=new iReceipt();
			rec.fromByteArray(str.getBytes("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	
	Syncer sync=new Syncer();

	List<iReceipt> list=new ArrayList<iReceipt>();
	list.add(rec);
	sync.sendSync(list);
	
	}
	
	
	

}
