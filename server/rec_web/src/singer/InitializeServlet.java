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
	

	public class InitializeServlet extends HttpServlet {
	   // private static final Logger log = Logger.getLogger(Initialize.class.getName());

	    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {
	    	 UserService userService = UserServiceFactory.getUserService();
	         User user = userService.getCurrentUser();
	         if (user==null){
	        	 resp.sendRedirect("/rec_list.jsp");
	        	 return;
	         }
	        /*	 
			iReceipt rec1 = (new iReceipt(new IDate(2010, 12, 1), "AppleStore",
					1.99, "Travel", true, true, "", "/data/4.JPG",user,user));
			iReceipt rec2 = (new iReceipt(new IDate(2010, 10, 16), "BP-Gass", 19.92,
					"Car", false, true, "", "/data/2.JPG",user,user));
			iReceipt rec3 = (new iReceipt(new IDate(2010, 11, 18), "Chang-Mai ",
					15.00, "Dining", false, true, "", "/data/5.JPG",user,user));
			iReceipt rec4 = (new iReceipt(new IDate(2010, 11, 19), "Target", 3.00,
					"Shopping", false, true, "", "/data/6.JPG",user,user));
			iReceipt rec5 = (new iReceipt(new IDate(2010, 12, 4), "Sony", 99.99,
					"Shopping", true, true, "", "/data/3.JPG",user,user));
			iReceipt rec6 = (new iReceipt(new IDate(2001, 11, 16), "Fieldpine", 4.50, "Dining", false, true, "really good!", "/data/111.JPG",user,user));
			iReceipt rec7 = (new iReceipt(new IDate(2010, 12, 9), "BODY SHOP", 40.47, "Shopping", false, true, "wow it worth it!", "/data/1.JPG",user,user));
			iReceipt rec8 = (new iReceipt(new IDate(2010, 9, 25), "Macys", 71.54, "Shopping", true, true, "Nautica & Hilfinger", "/data/1.JPG",user,user));		
			iReceipt rec9 = (new iReceipt(new IDate(2010, 9, 23), "H&M", 11.90, "Shopping", false, true, "", "/data/1.JPG",user,user));
		  */
	         /*
	         iReceipt rec10 = (new iReceipt(new IDate(2010, 12, 9), "adidas", 165.08, "Shopping", false, true, "", "/data/1.JPG",user,user));
			iReceipt rec11 = (new iReceipt(new IDate(2010, 12, 9), "Celebrate Today", 10.30, "Dining", false, true, "at the disney", "/data/1.JPG",user,user));
			iReceipt rec12 = (new iReceipt(new IDate(2010, 12, 7), "MK Churro Wagon", 6.50, "Dining", false, true, "", "/data/1.JPG",user,user));
				*/
	         
	         
	         
	         ///////////////////////////////////////////////////////////////////////////////////////////////
	         
	        /* 
	         iReceipt rec1 = (new iReceipt(new IDate(2010, 12, 8), "LACOSTE",
	                 498.29, "Shopping", true, true, " cloths for mom", "/images/receipts_prev/IMG_7953.JPG",user));
	        
	         iReceipt rec2 = (new iReceipt(new IDate(2010, 12, 10), "L'OCCITANE",
	                 49.22, "Shopping", false, true, "perfume", "/images/receipts_prev/IMG_7949.JPG",user));
	         
	         iReceipt rec3 = (new iReceipt(new IDate(2010, 12, 10),
	                 "QUIZNO'S Food Court", 10.63, "Dining", false, true, "food at disney",
	                 "/images/receipts_prev/IMG_7950.JPG",user));
	         
	         iReceipt rec4 = (new iReceipt(new IDate(2010, 12, 9),
	                 "Celebrate Today", 10.30, "Dining", false, true, "food at disney",
	                 "/images/receipts_prev/IMG_7937.JPG",user));
	         
	         iReceipt rec5 = (new iReceipt(new IDate(2010, 12, 9), "Reebok", 95.40,
	                 "Shopping", true, true, " new shoes for me", "/images/receipts_prev/IMG_7941.JPG",user));
	         
	        // iReceipt rec6 = (new iReceipt(new IDate(2001, 11, 5), "DUFRITAL SPA",
	        //         21.10, "Shopping", false, true, "massage", "/images/receipts_prev/IMG_7942.JPG",user));
	         
	         iReceipt rec7 = (new iReceipt(new IDate(2010, 9, 26), "AMERICAN EAGLE",
	                 39.50, "Shopping", false, true, "cloths for me", "/images/receipts_prev/IMG_7944.JPG",user));
	         
	         iReceipt rec8 = (new iReceipt(new IDate(2010, 9, 25), "Macys", 71.54,
	                 "Shopping", true, true, "cloths for me", "/images/receipts_prev/IMG_7945.JPG",user));
	         
	         iReceipt rec9 = (new iReceipt(new IDate(2010, 9, 23), "H&M", 11.90,
	                 "Shopping", false, true, "2 shirts for ", "/images/receipts_prev/IMG_7948.JPG",user));
	         
	         iReceipt rec10 = (new iReceipt(new IDate(2010, 12, 9), "THE BODY SHOP",
	                 40.47, "Shopping", false, true, "soap", "/images/receipts_prev/IMG_7933.JPG",user));
	         
	         iReceipt rec11 = (new iReceipt(new IDate(2008, 2, 11), "GETGO", 47.27,
	                 "Car", false, true, "", "/images/receipts_prev/rec1.JPG",user));
	         
	         iReceipt rec12 = (new iReceipt(new IDate(2010, 12, 9), "adidas",
	                 165.08, "Shopping", false, true, "Orlando, FL 32809 new shoes", "/images/receipts_prev/IMG_7936.JPG",user));
	         
	        // iReceipt rec13 = (new iReceipt(new IDate(2010, 11, 16), "Fieldpine",
	          //       4.50, "Dining", false, true, "", "/images/rec111.JPG",user));
	         
	         iReceipt rec14 = (new iReceipt(new IDate(2010, 12, 7),
	                 "MK MS Bev Wagon", 4.50, "Dining", false, true, " best food",
	                 "/images/receipts_prev/IMG_7932.JPG",user));
	         
	         iReceipt rec15 = (new iReceipt(new IDate(2010, 12, 10),
	                 "Orlando Airport O Store", 4.50, "Shopping", false, true, "coffe",
	                 "/images/receipts_prev/IMG_7940.JPG",user));
	         
	         iReceipt rec16 = (new iReceipt(new IDate(2010, 12, 5),
	                 "Duty Free Israel", 340.60, "Shopping", false, true, "cigarets",
	                 "/images/receipts_prev/IMG_7951.JPG",user));
	         
	         iReceipt rec17 = (new iReceipt(new IDate(2010, 12, 7), "Boma", 40.45,
	                 "Dining", false, true, "breakfast at disney", "/images/receipts_prev/IMG_7955.JPG",user));
	         
	         iReceipt rec18 = (new iReceipt(new IDate(2010, 12, 6), "BBVA Bancomer",
	                 500.08, "Shopping", false, true, "food", "/images/receipts_prev/IMG_7956.JPG",user));
	         
	         iReceipt rec19 = (new iReceipt(new IDate(2010, 12, 7), "Jiko", 500.00,
	                 "Dining", false, true, "dinner with keren", "/images/receipts_prev/IMG_7957.JPG",user));
	         
	         iReceipt rec20 = (new iReceipt(new IDate(2010, 12, 2), "PIZZA ROLANDI",
	                 1607.10, "Dining", false, true, "dinner with keren", "/images/receipts_prev/IMG_7960.JPG",user));
	         
	         iReceipt rec21 = (new iReceipt(new IDate(2010, 12, 6),
	                 "HOT DOGS ALL DRESSED", 47.00, "Dining", false, true, "",
	                 "/images/receipts_prev/IMG_7961.JPG",user));
	         
	         iReceipt rec22 = (new iReceipt(new IDate(2010, 12, 10), "ZAZA", 3.63,
	                 "Dining", false, true, "", "/images/receipts_prev/IMG_7929.JPG",user));
	         
	         iReceipt rec23 = (new iReceipt(new IDate(2008, 12, 30), "Saugus", 19.92,
	                 "Car", false, true, "", "/images/receipts_prev/2.jpg",user));
	         
	         iReceipt rec24 = (new iReceipt(new IDate(2009, 04, 23), "Fastfuel", 24.36,
	                 "Car", false, true, "", "/images/receipts_prev/3.jpg",user));	         
	         
	         
	         
	         
	         //////////////////////////////////////////////////////////
	         
	         
	         
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        try {
	            pm.makePersistent(rec1);
	            pm.makePersistent(rec2);
	            pm.makePersistent(rec3);
	            pm.makePersistent(rec4);
	            pm.makePersistent(rec5);
//	            pm.makePersistent(rec6);
	            pm.makePersistent(rec7);
	            pm.makePersistent(rec8);
	            pm.makePersistent(rec9);
	            pm.makePersistent(rec10);
	            pm.makePersistent(rec11);
	            pm.makePersistent(rec12);
	            //pm.makePersistent(rec13);
	            pm.makePersistent(rec14);
	            pm.makePersistent(rec15);
	            pm.makePersistent(rec16);
	            pm.makePersistent(rec17);
	            pm.makePersistent(rec18);
	            pm.makePersistent(rec19);
	            pm.makePersistent(rec20);
	            pm.makePersistent(rec21);
	            pm.makePersistent(rec22);
	            pm.makePersistent(rec23);
	            pm.makePersistent(rec24);
	            
	            
	        } finally {
	            pm.close();
	        }
*/
	        resp.sendRedirect("/index.jsp");
	    }
	}
