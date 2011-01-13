package singer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UpdateReceiptEntryServlet extends HttpServlet{

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null){
			resp.sendRedirect("/rec_list.jsp");
			return;
		}

		if (req.getParameter("id")==null){
			resp.sendRedirect("/rec_list.jsp");
			return;
		}

		long id=Long.parseLong(req.getParameter("id"));	        	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			 //pm.currentTransaction().begin();
			iReceipt rec=pm.getObjectById(iReceipt.class,id);
			if (rec.getUserID().equals(user.getUserId())){
				if (req.getParameter("id")==null){
					resp.sendRedirect("/rec_list.jsp");
					return;
				}
				rec.setStoreName(req.getParameter("business"));
				rec.setTotal(Double.parseDouble(req.getParameter("total")));
				rec.setCategory(req.getParameter("category"));
				rec.setDate_s(req.getParameter("date"));
				rec.setNotes(req.getParameter("notes"));
				if((req.getParameter("flaged")!=null)&&(req.getParameter("flaged").equals("flaged")))
					rec.setFlaged(true);
				else
					rec.setFlaged(false);
				pm.makePersistent(rec);
				pm.currentTransaction().commit();
			}
		}
		catch (Exception e) {
			pm.currentTransaction().rollback();
			 throw new RuntimeException(e);
		}
		finally{
			pm.close();
		}

		resp.sendRedirect("/recview.jsp?id=("+id+")");
	}
}





