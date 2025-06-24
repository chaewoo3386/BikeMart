package bike;

import java.io.Serializable;
import java.util.Date;

public class BikeVO implements Serializable {
	private int bikeNo;
	private String name;
	private String model;
	private String brand;
	private int price;
	private int instock;
	private Date regdate;
	
	public BikeVO(int bikeNo, String name, String model, String brand, int price, int instock, Date regdate) {
		this.bikeNo = bikeNo;
		this.name = name;
		this.model = model;
		this.brand = brand;
		this.price = price;
		this.instock = instock;
		this.regdate = regdate;
	}
	
	public BikeVO(String name, String model, String brand, int price, int instock) {
		this(-1, name, model, brand, price, instock, null);
	}
	
	public String toString() {
		return "[" + bikeNo + ", " + name + ", " + model + ", " + brand + ", " + price + ", " + instock + "]";
	}

	public int getBikeNo() {
		return bikeNo;
	}

	public void setBikeNo(int bikeNo) {
		this.bikeNo = bikeNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getInstock() {
		return instock;
	}

	public void setInstock(int instock) {
		this.instock = instock;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	
	
}