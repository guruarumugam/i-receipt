package singer;

import java.io.Serializable;
import java.util.Calendar;
import google.proj.IDate;

public class DateAndCost  implements Serializable {

	private IDate date;
	private Double TotalCost;
	
	
	public DateAndCost(){
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		this.date= new IDate(year, month, day);
		this.TotalCost=0.0;
	}
	
	public DateAndCost(IDate date, Double TotalCost){
		this.date= new IDate(date.getYear(),date.getMonth(),date.getDay());
		this.TotalCost=TotalCost;
	}
	
	public void setDate(IDate Date) {
		this.date=new IDate(Date.getYear(),Date.getMonth(),Date.getDay());
	}
	
	public IDate getDate() {
		return this.date;
	}
	
	public void setCost(Double TotalCost) {
		this.TotalCost = TotalCost;
	}
	
	public Double getCost() {
		return TotalCost;
	}

}
