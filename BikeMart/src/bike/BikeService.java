package bike;

import java.util.List;

public interface BikeService {
	boolean registBike(BikeVO bike);
	List<BikeVO> listBikes();
	BikeVO detailBikeInfo(int bikeNo);
	boolean updateBikePrice(int bikeNo, int price);
	boolean updateBikeInstock(int bikeNo, int instock);
	boolean removeBike(int bikeNo);
}
