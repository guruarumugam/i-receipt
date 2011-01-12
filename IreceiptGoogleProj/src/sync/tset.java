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
				"test", 11.11, "Dining", false, true, "", null,-1);		
		Syncer sync=new Syncer();

		List<iReceipt> list=new ArrayList<iReceipt>();
		list.add(rec);
		//sync.sendSync(list);
		/*byte[] by=rec.toByteArray();
		byte[]encoded=Base64.encode(by, Base64.DEFAULT);
		String str=new String(encoded,"ASCII");
		//System.out.println(str);
		/*byte[] decoded = Base64.decode(str.getBytes("ASCII"),Base64.DEFAULT);
		 for (int i = 0; i < by.length; i++) {
		        assert by[i] == decoded[i];
		}
		 iReceipt rec1 =new iReceipt();
		 rec1.fromByteArray(decoded);
		 System.err.println(rec1.getCategory());/*


		
		/*
		String str=new String (rec.toByteArray(),"utf-8");
		rec=new iReceipt();
		rec.fromByteArray(str.getBytes("utf-8"));*/}
		/*} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

/*
	
;
	
	}
	
	*/
	

}

