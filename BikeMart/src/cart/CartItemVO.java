package cart;

public class CartItemVO {
	
	private int bikeNo;
	private int quantity;
	
	public CartItemVO(int bikeNo, int quantity) {
		this.bikeNo = bikeNo;
		this.quantity = quantity;
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

	@Override
	public String toString() {
		return "[" + bikeNo + "번 자전거," + quantity + "대]";
	}
	
	
}
