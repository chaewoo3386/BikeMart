package order;

import java.util.List;

public interface OrderDAO {
	boolean insertOrder(OrderVO order);
	OrderVO selectBike(int orderNo);
	List<OrderVO> selectOrdersOfMember(String memberId);
	List<OrderVO> selectAllOrder();
}