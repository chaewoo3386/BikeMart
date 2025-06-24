package app;

import java.util.ArrayList;
import java.util.List;

import bike.BikeService;
import bike.BikeVO;
import bike.CWBikeService;
import bike.file.ObjFileHashMapBikeDAO;
import cart.CartItemVO;
import cart.CartService;
import cart.CartServiceImpl;
import cart.HashMapCartDAO;
import member.CWMemberService;
import member.MemberService;
import member.MemberVO;
import member.ObjFileHashMapMemberDAO;
import order.ObjFileHashMapOrderDAO;
import order.OrderItemVO;
import order.OrderService;
import order.OrderServiceImpl;
import order.OrderVO;

public class BikeMartConsoleApp {

	String[] startMenuList = {"종료", "자전거 목록", "로그인", "회원 가입"};
	String[] adminMenuList = {"로그아웃", "자전거 목록", "자전거 등록", "자전거 정보 수정", "자전거 삭제", "회원 목록", "주문 목록"};
	String[] memberMenuList = {"로그아웃", "자전거 목록", "자전거 주문", "주문 목록", "장바구니 자전거 담기", "장바구니 보기", "내 정보"};
	String[] cartMenuList = {"돌아가기", "자전거 주문", "자전거 삭제", "장바구니 비우기"};
	String[] myInfoMenuList = {"돌아가기", "비밀번호 변경", "회원 탈퇴"};
	
	final String ADMIN_ID = "admin";
	final String ADMIN_PWD = "1234";
	final String ADMIN_NAME = "관리자";
	
	final String CONFIRM = "yes";
	
	BikeService bs = new CWBikeService(new ObjFileHashMapBikeDAO());
	MemberService ms = new CWMemberService(new ObjFileHashMapMemberDAO());
	OrderService os = new OrderServiceImpl(new ObjFileHashMapOrderDAO(), bs);
	CartService cs = new CartServiceImpl(new HashMapCartDAO());
	MemberVO loggedMember;
	
	MyAppReader input = new MyAppReader();
	
	public static void main(String[] args) {
		BikeMartConsoleApp app = new BikeMartConsoleApp();
		app.displayWelcome();
		app.controlStartMenu();
	}

	private void displayWelcome() {
		System.out.println("***********************************");
		System.out.println("*  Welcome to ChaeWoo Bike Mart  *");
		System.out.println("***********************************");
	}

	private void controlStartMenu() {
		int menu;
		do {
			menu = selectMenu(startMenuList);
			
			switch (menu) {
			case 1: menuBikeList(); break;
			case 2: menuLogin(); break;
			case 3: menuSignUp(); break;
			case 0: menuExit(); break;
			default : menuWrongNumber();
			}
			
		} while (menu != 0);
		
	}

	private void menuWrongNumber() {
		System.out.println("없는 메뉴입니다.");
		
	}

	private void menuExit() {
		System.out.println("ChaeWoo Bike Mart 서비스를 종료합니다.");
		
	}

	private void menuBikeList() {
		System.out.println("*** 자전거 목록 ***");
		displayBikeList();
	}
	
	private void displayBikeList() {
		List<BikeVO> bikeList = bs.listBikes();
		System.out.println("---------------------------------------");
		if (bikeList.isEmpty()) {
			System.out.println("등록된 자전거가 없습니다.");
		} else {
			for (BikeVO bike : bikeList) {
				System.out.println(bike);
			}
		}
		System.out.println("---------------------------------------");	
	}

	private void menuLogin() {
		System.out.println("*** 로그인 ***");
		String id = input.readString(">> id : ");
		String password = input.readString(">> password : ");
		
		// 관리자 -> 관리자 메뉴
		if (id.equals(ADMIN_ID) && password.equals(ADMIN_PWD)) {
			loggedMember = new MemberVO(ADMIN_ID, ADMIN_PWD, ADMIN_NAME);
			System.out.println("관리자 모드로 변경합니다.");
			controlAdminMenu();
		} else {
			// 회원 -> 회원 메뉴
			loggedMember = ms.login(id, password);
			
			if (loggedMember != null) {
				System.out.println("[로그인] " + loggedMember.getUsername() + "님 안녕하세요.");
				controlMemberMenu();
			} else {
				// 아니면
				System.out.println("로그인을 하지 못했습니다.");
			}
		}
		
	}

	private void controlMemberMenu() {
		int menu;
		do {
			menu = selectMenu(memberMenuList);
			// "로그아웃", "자전거 목록", "자전거 주문", "주문 목록", "장바구니 자전거 담기", "장바구니 보기", "내 정보"
			switch (menu) {
			case 1 : menuBikeList(); break;
			case 2 : menuBikeOrder(); break;
			case 3 : menuOrderList(); break;
			case 4 : menuAddBike2Cart(); break;
			case 5 : menuCartView(); break;
			case 6 : menuMyInfo(); break;
			case 0 : menuLogout(); break;
			default : menuWrongNumber();
			}
		} while (menu != 0);

	}

	private void menuBikeOrder() {
		System.out.println("*** 자전거 주문 ***");
		displayAvailableBikeList();
		int bikeNo = input.readInt(">> 자전거 번호 : ");
		BikeVO bike = bs.detailBikeInfo(bikeNo);
		
		if (bike == null) {
			System.out.println("없는 자전거 입니다.");
			return;
		}
		
		int quantity = input.readInt(">> 주문량 (" + bike.getInstock() + "대 이내) : ");
		if (quantity > bike.getInstock()) {
			System.out.println("주문량이 재고량보다 큽니다.");
			return;
		}
		
		// 주문 자전거 목록
		List<OrderItemVO> orderItemList = new ArrayList<>();
		int price = bike.getPrice() * quantity;
		orderItemList.add(new OrderItemVO(bikeNo, quantity, price));
		
		// 주문 정보 생성
		OrderVO order = new OrderVO(loggedMember.getId(), orderItemList, price);
		// 배송 정보 추가
		setDeliveryInfo();
		order.setMobile(loggedMember.getMobile());
		order.setAddress(loggedMember.getAddress());
		
		if (os.orderItems(order)) {
			System.out.println("주문이 완료되었습니다.");
			System.out.println("배송이 완료되었습니다.");
		} else {
			System.out.println("주문을 하지 못했습니다.");
		}
	}
	
	private void setDeliveryInfo() {
		if (loggedMember.getMobile() == null) {
			System.out.println("*** 배송 정보 입력 ***");
			
			String mobile = input.readString(">> 모바일 번호 : ");
			String email = input.readString(">> 이메일 주소 : ");
			String address = input.readString(">> 주소 : ");
			
			loggedMember.setMobile(mobile);
			loggedMember.setEmail(email);
			loggedMember.setAddress(address);
			
			ms.addMemberInfo(loggedMember.getId(), mobile, email, address);
			//loggedMember = ms.detailMemberInfo(loggedMember.getId());
			
		}
	}

	private void displayAvailableBikeList() {
		List<BikeVO> bikeList = bs.listBikes();
		System.out.println("---------------------------------------");
		if (bikeList.isEmpty()) {
			System.out.println("주문 가능한 자전거가 없습니다.");
		} else {
			int count = 0;
			for (BikeVO bike : bikeList) {
				if (bike.getInstock() > 0) {
					System.out.println(bike);
					count++;
				}
			}
			if (count == 0) 
				System.out.println("주문 가능한 자전거가 없습니다.");
		}
		System.out.println("---------------------------------------");	
		
	}

	private void menuAddBike2Cart() {
		System.out.println("*** 장바구니에 자전거 담기 ***");
		
		displayAvailableBikeList();
		int bikeNo = input.readInt(">> 자전거 번호 : ");
		BikeVO bike = bs.detailBikeInfo(bikeNo);
		
		if (bike == null) {
			System.out.println("없는 자전거 입니다.");
			return;
		}
		
		int quantity = input.readInt(">> 주문량 (" + bike.getInstock() + "대 이내) : ");
		if (quantity > bike.getInstock()) {
			System.out.println("주문량이 재고량보다 큽니다.");
			return;
		}
		
		// 이미 장바구니에 있는지 확인
		// 없으면, 장바구니에 넣기
		if (cs.getCartItemInfo(bikeNo) == null) {
			cs.addItem2Cart(new CartItemVO(bikeNo, quantity));
			System.out.println("장바구니에 추가했습니다.");
		} else {
			System.out.println("이미 장바구니에 있는 자전거입니다.");
		}
		
		
	}

	private void menuCartView() {
		System.out.println("*** 장바구니 보기 ***");
		displayCartItemList();
		
		if (!cs.isCartEmpty())controlCartMenu();
		
	}

	private void displayCartItemList() {	
		if (cs.isCartEmpty()) {
			System.out.println("장바구니가 비어 있습니다.");
		} else {
			System.out.println("---------------------------------------");	
			for (CartItemVO item : cs.listCartItems()) {
				System.out.println(item);
			}
			System.out.println("---------------------------------------");
		}
	}

	private void controlCartMenu() {
		int menu;
		do {
			menu = selectMenu(cartMenuList);
			//"돌아가기", "자전거 주문", "자전거 삭제", "장바구니 비우기"}
			switch (menu) {
			case 1 : menuCartOrder(); break;
			case 2 : menuCartBikeDelete(); break;
			case 3 : menuCartClear();
			case 0 : break;
			default : menuWrongNumber();
			}
		} while (menu != 0 && !cs.isCartEmpty());	
	}

	
	private void menuCartOrder() {
		System.out.println("*** 장바구니 자전거 주문 ***");
		displayCartItemList();
		
		List<OrderItemVO> orderItemList = new ArrayList<>();
		int totalPrice = 0;
		for(CartItemVO item : cs.listCartItems()) {
			BikeVO bike = bs.detailBikeInfo(item.getBikeNo());
			totalPrice += bike.getPrice() * item.getQuantity();
			orderItemList.add(new OrderItemVO(item.getBikeNo(), item.getQuantity(),
					bike.getPrice() * item.getQuantity()));
		}
		
		OrderVO order = new OrderVO(loggedMember.getId(), orderItemList, totalPrice);
		
		setDeliveryInfo();
		order.setMobile(loggedMember.getMobile());
		order.setAddress(loggedMember.getAddress());
		
		displayOrderInfo(order);
		
		String confirm = input.readString(">> 위와 같은 내용을 주문 및 결제를 진행하시겠습니까? ('"
				+ CONFIRM + "'이면 주문 실행) : ");
			if (confirm.equals(CONFIRM)) {
			if (os.orderItems(order)) {
			cs.clearCart();
			System.out.println("주문이 완료되었습니다.");
			System.out.println("배송이 완료되었습니다.");
			} else {
			System.out.println("주문을 하지 못했습니다.");
			}
			} else {
			System.out.println("주문이 취소되었습니다.");
			}
			
			}
	private void displayOrderInfo(OrderVO order) {
		System.out.println(order);
			
			}
	private void menuCartBikeDelete() {
		System.out.println("*** 장바구니 자전거 삭제 ***");
		displayCartItemList();
		int bikeNo = input.readInt(">> 자전거 번호 :");
		CartItemVO item = cs.getCartItemInfo(bikeNo);
		if (item == null) {
			System.out.println("없는 자전거입니다.");
		} else {
			cs.removeCartItem(bikeNo);
			System.out.println("장바구니에서 자전거를 삭제하였습니다.");
		}
		displayCartItemList();
	}

	private void menuCartClear() {
		System.out.println("*** 장바구니 비우기 ***");
		String confirm = input.readString(">> 장바구니의 모든 자전거를 삭제하시겠습니까? ('" + CONFIRM + "'이면 삭제) : ");
		if (confirm.equals(CONFIRM)) {
			cs.clearCart();
			System.out.println("장바구니의 모든 자전거를 삭제하였습니다.");
		} else {
			System.out.println("장바구니 비우기가 취소되었습니다.");
		}
		
	}

	private void menuMyInfo() {
		System.out.println("*** 내 정보 ***");
		System.out.println(loggedMember);
		
		controlMyInfoMenu();
	}

	private void controlMyInfoMenu() {
		int menu;
		do {
			menu = selectMenu(myInfoMenuList);
			// "돌아가기", "비밀번호 변경", "회원 탈퇴"
			switch (menu) {
			case 1 : menuUpatePassword(); break;
			case 2 : menuMemberExit(); break;
			case 0 : break;
			default : menuWrongNumber();
			}
		} while (menu != 0 && loggedMember != null);
		
	}

	private void menuUpatePassword() {
		System.out.println("*** 비밀번호 수정 ***");
		String oldPassword = input.readString(">> 기존 비밀번호 : ");
		String newPassword = input.readString(">> 새 비밀번호 : ");
		if (ms.updatePassword(loggedMember.getId(), oldPassword, newPassword)) {
			System.out.println("[비밀번호 수정] 비밀번호를 수정했습니다.");
		} else {
			System.out.println("[비밀번호 수정 실패] 비밀번호 수정에 실패했습니다.");
		}
	}

	private void menuMemberExit() {
		System.out.println("*** 회원 탈퇴 ***");
		String password = input.readString(">> 비밀번호 : ");
		if (ms.removeMember(loggedMember.getId(), password)) {
			System.out.println("[회원 탈퇴] 회원정보, 주문정보를 삭제하였습니다. 그동안 서비스를 이용해 주셔서 감사합니다.");
			loggedMember = null;
		} else {
			System.out.println("[회원 탈퇴 실패] 회원 탈퇴 처리에 실패했습니다.");
		}
		
	}

	private void controlAdminMenu() {
		int menu;
		do {
			menu = selectMenu(adminMenuList);
			// "로그아웃", "자전거 목록", "자전거 등록", "자전거 정보 수정", "자전거 삭제", "회원 목록", "주문 목록"
			switch (menu) {
			case 1: menuBikeList(); break;
			case 2: menuBikeRegist(); break;
			case 3: menuBikeUpdate(); break;
			case 4: menuBikeRemove(); break;
			case 5: menuMemberList(); break;
			case 6: menuOrderList(); break;
			case 0: menuLogout(); break;
			default : menuWrongNumber();
			}
			
		} while (menu != 0 && loggedMember != null);
		
	}

	private void menuBikeRegist() {
		
		System.out.println("*** 자전거 등록 ***");
		String name = input.readString(">> 자전거 이름 : ");
		String model = input.readString(">> 자전거 모델 : ");
		String brand = input.readString(">> 브랜드 : ");
		int price = input.readInt(">> 가격 : ");
		int instock = input.readInt(">> 재고량 : ");
		
		if (bs.registBike(new BikeVO(name, model, brand, price, instock))) {
			System.out.println("자전거를 등록했습니다.");
			displayBikeList();
		} else {
			System.out.println("자전거 등록에 실패했습니다.");
		}
		
	}

	private void menuBikeUpdate() {
		System.out.println("*** 자전거 정보 수정 ***");
		displayBikeList();
		int bikeNo = input.readInt(">> 자전거 번호 :");
		
		int select = input.readInt(">> 수정할 정보 선택 (1. 가격, 2. 재고량) : ");
		if (select == 1) { // 가격
			int price = input.readInt(">> 새 가격 : ");
			if (bs.updateBikePrice(bikeNo, price)) {
				System.out.println("[자전거 정보 수정] 가격을 수정하였습니다.");
			} else {
				System.out.println("[자전거 정보 수정 오류] 없는 자전거입니다.");
			}
			
		} else if (select == 2) {// 재고량
			int instock = input.readInt(">> 새 재고량 :");
			if (bs.updateBikeInstock(bikeNo, instock)) {
				System.out.println("[자전거 정보 수정] 재고량을 수정하였습니다.");
			} else {
				System.out.println("[자전거 정보 수정 오류] 없는 자전거입니다.");
			}
		} else {
			System.out.println("[자전거 정보 수정 취소] 지원하지 않는 기능입니다.");
		}
		
	}

	private void menuBikeRemove() {
		System.out.println("*** 자전거 삭제 ***");
		displayBikeList();
		int bikeNo = input.readInt(">> 자전거 번호 :");
		String confirm = input.readString("선택한 자전거를 삭제하시겠습니까? ('" + CONFIRM + "'를 입력하면 실행) : ");
		if (confirm.equals(CONFIRM)) {
			if (bs.removeBike(bikeNo)) {
				System.out.println("[자전거 삭제] 자전거를 삭제했습니다.");
			} else {
				System.out.println("[자전거 삭제 오류] 없는 자전거입니다.");
			}
		} else {
			System.out.println("[자전거 삭제 취소] 자전거 삭제를 취소했습니다.");
		}
	}

	private void menuMemberList() {
		System.out.println("*** 회원 목록 ***");
		System.out.println("---------------------------------------");
		List<MemberVO> memberList = ms.listMembers();
		if (memberList.isEmpty()) {
			System.out.println("회원이 없습니다.");
		} else {
			for (MemberVO member : memberList) {
				System.out.println(member);
			}
		}
		System.out.println("---------------------------------------");
		
	}

	private void menuOrderList() {
		if (loggedMember.getId().equals(ADMIN_ID)) {
			System.out.println(os.listAllOrder());
		} else {
			System.out.println(os.listMyOrders(loggedMember.getId()));
		}
		
	}

	private void menuLogout() {
		
		System.out.println("[로그아웃] " + loggedMember.getUsername() + "님, 안녕히 가십시오.");
		loggedMember = null;
		
	}

	private void menuSignUp() {
		System.out.println("*** 회원 가입 ***");
		String id = input.readString(">> id : ");
		String password = input.readString(">> password : ");
		String username = input.readString(">> username : ");
		
		if (ms.registMember(new MemberVO(id, password, username))) {
			System.out.println("회원 가입이 완료되었습니다. 서비스 이용을 위한 로그인 해주세요.");
		} else {
			System.out.println("회원 가입에 실패하였습니다.");
		}
		
	}

	private int selectMenu(String[] menuList) {

		System.out.println("-------------------------------");
		for (int i = 1; i < menuList.length; i++) {
			System.out.println(i + ". " + menuList[i]);
		}
		System.out.println("0. " + menuList[0]);
		System.out.println("-------------------------------");
		return input.readInt(">> 메뉴 선택 : ");
	}
}