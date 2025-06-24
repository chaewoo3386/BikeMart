package order;

import java.util.Date;
import java.util.List;

import bike.BikeService;

public class OrderServiceImpl implements OrderService {
	
	private OrderDAO orderDAO;
	private BikeService bikeService;
	
	private final int COMPLETE = 10;
	
	public OrderServiceImpl(OrderDAO orderDAO, BikeService bikeService) {
		this.orderDAO = orderDAO;
		this.bikeService = bikeService;
	}

	@Override
	public boolean orderItems(OrderVO order) {

		// 1. 주문 정보 추가 (주문일, 배송상태, 배송완료일)
		order.setOrderDate(new Date());
		order.setStatus(COMPLETE);
		order.setDeliverDate(new Date());
				
		// 2. 자전거 재고량 update
		for (OrderItemVO item : order.getOrderItemList()) {
			int bikeNo = item.getBikeNo();
			int newInstock = bikeService.detailBikeInfo(bikeNo).getInstock() - item.getQuantity();
			if (newInstock >= 0) {
				bikeService.updateBikeInstock(bikeNo, newInstock);
			} else {
				return false;
			}
		}
		
		// 3. 주문 정보 DB에 추가
		orderDAO.insertOrder(order);		
		return true;
	}

	@Override
	public List<OrderVO> listMyOrders(String memberId) {
		return orderDAO.selectOrdersOfMember(memberId);
	}

	@Override
	public List<OrderVO> listAllOrder() {
		return orderDAO.selectAllOrder();
	}

}