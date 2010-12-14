package google.proj;
import java.io.Serializable;

public class IDate implements Comparable<IDate>,Serializable {
	
	private int day;
	private int month;
	private int year;
	
	public IDate(){
		day=0;
		month=0;
		year=0;
	}
	
	public IDate (int year,int month,int day){
		this.day=day;
		this.month=month;
		this.year=year;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	public int getDay() {
		return day;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getMonth() {
		return month;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getYear() {
		return year;
	}

	@Override//sort from current to oldest
	public int compareTo(IDate another) {
		if (this.year>another.getYear())
				return -1;
		else
			if (this.year<another.getYear())
				return 1;
			else
				if(this.month>another.getMonth())
					return -1;
				else
					if(this.month<another.getMonth())
						return 1;
					else
						if (this.day>another.getDay())
							return -1;
						else
							if (this.day<another.getDay())
									return 1;
							else
								return 0;
	}
	
	public String toString(){
		return ""+ this.month+"-"+this.day+"-"+this.year;
	}
	
	
}
	
