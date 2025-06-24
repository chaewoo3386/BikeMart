package order;

import java.io.Serializable;

public class OrderItemVO implements Serializable {
	private int bikeNo;
	private int quantity;
	private int price;
	
	public OrderItemVO(int bikeNo, int quantity, int price) {
		this.bikeNo = bikeNo;
		this.quantity = quantity;
		this.price = price;
	}

	public int getBikeNo() {
		return bikeNo;
	}

	public void setBikeNo(int bikeNo) {
		this.bikeNo = bikeNo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "\t***" + bikeNo + ", " + quantity + "(대), " + price + "(원)";
	}
	
	
}