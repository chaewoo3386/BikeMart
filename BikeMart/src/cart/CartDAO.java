package cart;

import java.util.List;

public interface CartDAO {
	boolean insertCartItem(CartItemVO item);
	CartItemVO selectCartItem(int bikeNo);
	List<CartItemVO> selectAllCartItem();
	boolean deleteCartItem(int bikeNo);
	boolean clear();
	
}
