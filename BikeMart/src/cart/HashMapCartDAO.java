package cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapCartDAO implements CartDAO {
	
	Map<Integer, CartItemVO> cartDB = new HashMap<>();

	@Override
	public boolean insertCartItem(CartItemVO item) {
		cartDB.put(item.getBikeNo(), item);
		return true;
	}

	@Override
	public CartItemVO selectCartItem(int bikeNo) {
		
		return cartDB.get(bikeNo);
	}

	@Override
	public List<CartItemVO> selectAllCartItem() {
		return new ArrayList<>(cartDB.values()); 
	}

	@Override
	public boolean deleteCartItem(int bikeNo) {
		return cartDB.remove(bikeNo) != null;
	}

	@Override
	public boolean clear() {
		cartDB.clear();
		return true;
	}

}
