package singer;
import java.io.Serializable;


public class store implements Serializable {

	private String name;
	private Double cost;

	
	public store(){
		this.name= "";
		this.cost=0.0;
	}
	public store(String name, Double cost){
		this.name=name;
		this.cost=cost;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getCost() {
		return cost;
	}
}
