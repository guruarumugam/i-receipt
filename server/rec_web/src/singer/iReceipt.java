package singer;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class iReceipt implements java.io.Serializable {

	/**
	 * 
	 */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String user_id;
	private google.proj.IDate Rdate;
    @Persistent
    private String Date_s;
    @Persistent
	private String StoreName;
    @Persistent
	private double Total;
    @Persistent
	private String Category;
    @Persistent
	private boolean Flaged;
    @Persistent
	private boolean processed;
    @Persistent
	private String Notes;
    @Persistent
	private Blob filepath;
    
    @Persistent
    private Integer receiptuniqueindex;
    @Persistent
    private Date update;
    @Persistent
    private Date syncdate;
    
	public iReceipt(google.proj.IDate rdate, String storeName, double total,
			String category, boolean flaged, boolean processed, String notes,
			Blob filepath,User author) {
		Rdate = rdate;
		StoreName = storeName;
		Total = total;
		Category = category;
		Flaged = flaged;
		this.processed = processed;
		Notes = notes;
		this.filepath = filepath;
		this.user_id=author.getUserId();
		if (this.Rdate!=null)
			this.Date_s=this.Rdate.toString();
	}
	
	public iReceipt(google.proj.IDate rdate, String storeName, double total,
			String category, boolean flaged, boolean processed, String notes,
			Blob filepath) {
		Rdate = rdate;
		StoreName = storeName;
		Total = total;
		Category = category;
		Flaged = flaged;
		this.processed = processed;
		Notes = notes;
		this.filepath = filepath;
		this.user_id=null;
		if (this.Rdate!=null)
			this.Date_s=this.Rdate.toString();
	}
	
	public iReceipt(google.proj.IDate rdate, String storeName, double total,
			String category, boolean flaged, boolean processed, String notes,
			Blob filepath,String userid,Integer unique) {
		Rdate = rdate;
		StoreName = storeName;
		Total = total;
		Category = category;
		Flaged = flaged;
		this.processed = processed;
		Notes = notes;
		this.filepath = filepath;
		this.user_id=userid;
		receiptuniqueindex=unique;
		if (this.Rdate!=null)
			this.Date_s=this.Rdate.toString();
	}
	
	
	

	public iReceipt() {
		Rdate = null;
		StoreName = null;
		Total = -1;
		Category = null;
		Flaged = false;
		this.processed = false;
		Notes = null;
		this.filepath = null;
	}

	public void setRdate(google.proj.IDate rdate) {
		Rdate = rdate;
		setDate_s(rdate.toString());
	}
	
	//public static hobot(String Date_s)

	public google.proj.IDate getRdate() {
			  int year,day,month;
			  String Date_s=this.getDate_s();
			  month=Integer.parseInt(Date_s.substring(0,2));
			  day=Integer.parseInt(Date_s.substring(3,5));
			  year=Integer.parseInt(Date_s.substring(6));
			  return new google.proj.IDate(year, month, day);
		}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public void setTotal(double total) {
		Total = total;
	}

	public double getTotal() {
		return Total;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public void setFlaged(boolean flaged) {
		Flaged = flaged;
	}

	public boolean isFlaged() {
		return Flaged;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isProcessed() {
		return processed;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public void setFilepath(Blob filepath) {
		this.filepath = filepath;
	}

	public Blob getFilepath() {
		return filepath;
	}

	public String toString() {
		return String
				.format("%s\t%-6.6s\t%-8.8s", Rdate, this.Total, StoreName);
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setUserID(User author) {
		this.user_id=author.getUserId();
	}

	public String getUserID() {
		return this.user_id;
	}

	public void setDate_s(String date_s) {
		Date_s = date_s;
	}

	public String getDate_s() {
		return Date_s;
	}
	
	public Date getUpdate(){
		return update;
	}
	
	public Date getSyncDate(){
		return syncdate;
	}
	public void setUpdate(){
		update=new Date();
	}
	public void setUpdate(Date date){
		update=date;
	}
	public void setSyncdate(){
		syncdate=new Date();
	}
	public void setSyncdate(Date date){
		syncdate=date;
	}
	public Integer getUnique(){
		return receiptuniqueindex;
	}
	
	public boolean equals(Object r){
		if (receiptuniqueindex==null)
			return false;
		else
		return (this.getUnique()==((iReceipt) r).getUnique());
	}
	


	}

