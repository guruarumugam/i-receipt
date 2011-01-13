package google.proj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import singer.PMF;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GetImage extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		   String index=req.getParameter("index");
		   if(index==null)
			   return;
		   UserService userService = UserServiceFactory.getUserService();
		   User user = userService.getCurrentUser();
		   PersistenceManager pm = PMF.get().getPersistenceManager();
			/*Query query = pm.newQuery(singer.iReceipt.class);
		    query.setFilter("receiptuniqueindex == index");
		    query.declareParameters("String userid,String index");
			List<singer.iReceipt> imgs = (List<singer.iReceipt>) query.execute(user.getEmail(),index);*/
		   
		   Query query =pm.newQuery(singer.iReceipt.class);
		    query.setFilter("user_id==userid && receiptuniqueindex == indexx");
		    query.declareParameters("String userid,Integer indexx");
			List<singer.iReceipt> imgs = (List<singer.iReceipt>) query.execute(user.getEmail(),Integer.parseInt(index));
		   
			Blob blob;
			if (imgs.isEmpty()==false) {
				blob=imgs.get(0).getFilepath();
			}
			else
				return;
			byte[] imgData = blob.getBytes();
		    try{
			   resp.setContentType("image/jpeg");
		       OutputStream o = resp.getOutputStream();
		       o.write(imgData);
		       o.flush(); 
		       o.close();
			}
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    finally{
		    pm.close();
		    }

	}
}
