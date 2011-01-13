package google.proj;
import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;



import singer.PMF;
import sun.swing.AccumulativeRunnable;

@SuppressWarnings("serial")
public class SyncServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		if (req.getParameter("size")==null){
			resp.sendError(404);
			return;
		}
		int size=Integer.parseInt(req.getParameter("size"));
		if (size==0){
			return;
		}
		String account=req.getParameter("account");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query =pm.newQuery(singer.iReceipt.class);
		//query.se
		query.setFilter("user_id==id");
		query.declareParameters("String id");
		
		@SuppressWarnings("unchecked")
		List<singer.iReceipt> serverlist=(List<singer.iReceipt>)query.execute(account);
		
		boolean add=false;
		try {
			
			for (int i = 0; i < size; i++) {
				
				Blob blob=null;
				String in=req.getParameter(i+"");
				if(in==null){
					resp.sendError(404);
					return;}
				byte[] decoded = Base64.decode(in.getBytes("ASCII"),Base64.DEFAULT);
				iReceipt rec=new iReceipt();
				rec.fromByteArray(decoded);
				//get image if present
				if (req.getParameter(i+"_image") != null){
					decoded = Base64.decode(req.getParameter(i+"_image").getBytes("ASCII"),Base64.DEFAULT);
					blob = new Blob(decoded);
				}
				//rec is a copy of the new uploaded receipt
				singer.iReceipt rec_s=new singer.iReceipt(rec.getRdate(),rec.getStoreName(),rec.getTotal(), rec.getCategory(),rec.isFlaged(),rec.isProcessed(),rec.getNotes(),blob,account,rec.getUniqueIndex());
				//////////////////////////////
				rec_s.setUpdate(rec.getUpdate());
				rec_s.setSyncdate(rec.getSyncdate());
				 
				 for (singer.iReceipt tmprr:serverlist){
					 
					 if (tmprr.equals(rec_s)){
						 if (rec_s.getUpdate().after(tmprr.getUpdate())){
							 pm.deletePersistent(tmprr);
							 pm.makePersistent(rec_s);
							 break;
						 }
						 add=true;
							 
						 
					 }
					 
				 }
				 if (!add){
					 pm.makePersistent(rec_s);
					 add=false;
					 
				 }
			}
			/*
				if (serverlist.contains(rec_s)){ // the receipt is already in the server
					
				singer.iReceipt r=serverlist.get(serverlist.indexOf(rec_s));
				
				if (rec_s.getUpdate().after(r.getUpdate())){ // the receipt we got is after the receipt in the sever need to replace it
					pm.deletePersistent(r);// delete the old 
					pm.makePersistent(rec_s); // add the new
				}
				else {// the receipt at the server is newer than in the android
					
				}
				}
				else { // the receipt doesn't appear on the server
					pm.makePersistent(rec_s); // add the new
					
				}
				
				
				//pm.makePersistent(rec_s); // add the new
			
				

			}
			*/

		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			pm.close();
		}

	}
	
	
	
	 
}
