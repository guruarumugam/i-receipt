package bodner.idan;

/*this interface is made to represent reciept DS.*/

public interface Receipt  {

	public void setRdate(Date rdate);
	public Date getRdate();
	
	public String getStoreName();
	public void setStoreName(String storeName);
	
	public void setTotal(double total);
	public double getTotal();
	
	public String getCategory();
	public void setCategory(String category);
	
	public void setFlaged(boolean flaged);
	public boolean isFlaged();
	
	public void setProcessed(boolean processed);
	public boolean isProcessed() ;
	
	public String getNotes();
	public void setNotes(String notes);
	
	public void setFilepath(String filepath);
	public String getFilepath();
	
	

}