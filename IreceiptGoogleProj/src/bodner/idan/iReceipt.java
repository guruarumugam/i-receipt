package bodner.idan;

public class iReceipt implements Receipt,java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date Rdate;
	private String StoreName;
	private double Total;
	private String Category;
	private boolean Flaged;
	private boolean processed;
	private String Notes;
	private String filepath;
	private final String receitname;
	
	public iReceipt(Date rdate, String storeName, double total,
			String category, boolean flaged, boolean processed, String notes,
			String filepath, String rname) {
		Rdate = rdate;
		StoreName = storeName;
		Total = total;
		Category = category;
		Flaged = flaged;
		this.processed = processed;
		Notes = notes;
		this.filepath = filepath;
		receitname=rname;
	}
	
	public iReceipt(Date rdate, String storeName, double total,
			String category, boolean flaged, boolean processed, String notes,
			String filepath) {
		Rdate = rdate;
		StoreName = storeName;
		Total = total;
		Category = category;
		Flaged = flaged;
		this.processed = processed;
		Notes = notes;
		this.filepath = filepath;
		receitname=null;
	}
	public iReceipt(String rname){
		Rdate = null;
		StoreName = null;
		Total = -1;
		Category = null;
		Flaged = false;
		this.processed = false;
		Notes = null;
		this.filepath = null;
		receitname=rname;
	}
	
	public iReceipt(){
		Rdate = null;
		StoreName = null;
		Total = -1;
		Category = null;
		Flaged = false;
		this.processed = false;
		Notes = null;
		this.filepath = null;
		receitname=null;
	}
	/*
	public iReceipt(){
		Rdate = null;
		StoreName = null;
		Total = -1;
		Category = null;
		Flaged = false;
		this.processed = false;
		Notes = null;
		this.filepath = null;
		
	}
	*/
	public void setRdate(Date rdate) {
		Rdate = rdate;
		}

	public Date getRdate() {
		return Rdate;
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
	
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilepath() {
		return filepath;
	}
	
	public String toString(){
		return String.format("%s\t%-6.6s\t%-8.8s",Rdate,this.Total,StoreName);
		//sprintf(out,"%s.%s.%s\t10.10%s\t%s",Rdate.getDay(),Rdate.getMonth(),Rdate.getYear(),StoreName,this.Total);
		//return (Rdate.getDay()+"."+Rdate.getMonth()+"."+Rdate.getYear()+"\t"+StoreName+"\t"+this.Total);
		
	}
	public String getplaceofsave(){
		return receitname;
	}
	
	

	}