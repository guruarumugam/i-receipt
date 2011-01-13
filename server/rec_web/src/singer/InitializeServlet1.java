package singer;

import java.io.IOException;
//import java.util.Date;
import google.proj.IDate;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import singer.iReceipt;
import singer.PMF;
	

	public class InitializeServlet1 extends HttpServlet {
	   // private static final Logger log = Logger.getLogger(Initialize.class.getName());

	    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {
	    	 UserService userService = UserServiceFactory.getUserService();
	         User user = userService.getCurrentUser();
	         if (user==null){
	        	 resp.sendRedirect("/rec_list.jsp");
	        	 return;
	         }/*
			iReceipt rec1 = (new iReceipt(new IDate(2010, 12, 1), "AppleStore",
					1.99, "Travel", true, true, "", "/data/4.jpg",user));
			iReceipt rec2 = (new iReceipt(new IDate(2010, 10, 16), "BP-Gass", 19.92,
					"Car", false, true, "", "/data/2.jpg",user));
			iReceipt rec3 = (new iReceipt(new IDate(2010, 11, 18), "Chang-Mai ",
					15.00, "Dining", false, true, "", "/data/5.jpg",user));
			iReceipt rec4 = (new iReceipt(new IDate(2010, 11, 19), "Target", 3.00,
					"Shopping", false, true, "", "/data/6.jpg",user));
			iReceipt rec5 = (new iReceipt(new IDate(2010, 12, 4), "Sony", 99.99,
					"Shopping", true, true, "", "/data/3.jpg",user));
			iReceipt rec6 = (new iReceipt(new IDate(2001, 11, 16), "Fieldpine", 4.50, "Dining", false, true, "really good!", "/data/111.jpg",user));
			iReceipt rec7 = (new iReceipt(new IDate(2010, 12, 9), "BODY SHOP", 40.47, "Shopping", false, true, "wow it worth it!", "/data/1.jpg",user));
			iReceipt rec8 = (new iReceipt(new IDate(2010, 9, 25), "Macys", 71.54, "Shopping", true, true, "Nautica & Hilfinger", "/data/1.jpg",user));		
			iReceipt rec9 = (new iReceipt(new IDate(2010, 9, 23), "H&M", 11.90, "Shopping", false, true, "", "/data/1.jpg",user));
			//iReceipt rec10 = (new iReceipt(new IDate(2010, 12, 9), "adidas", 165.08, "Shopping", false, true, "", "/data/1.jpg",user));
			//iReceipt rec11 = (new iReceipt(new IDate(2010, 12, 9), "Celebrate Today", 10.30, "Dining", false, true, "at the disney", "/data/1.jpg",user));
			//iReceipt rec12 = (new iReceipt(new IDate(2010, 12, 7), "MK Churro Wagon", 6.50, "Dining", false, true, "", "/data/1.jpg",user));

	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        try {
	            pm.makePersistent(rec1);
	            pm.makePersistent(rec2);
	            pm.makePersistent(rec3);
	            pm.makePersistent(rec4);
	            pm.makePersistent(rec5);
	            pm.makePersistent(rec6);
	            pm.makePersistent(rec7);
	            pm.makePersistent(rec8);
	            pm.makePersistent(rec9);
	            //pm.makePersistent(rec10);
	            //pm.makePersistent(rec11);
	            //pm.makePersistent(rec12);
	            
	        } finally {
	            pm.close();
	        }
*/
	        resp.sendRedirect("/index.jsp");
	    }
	}
