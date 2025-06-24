package bike;

import java.util.List;

public interface BikeDAO {
	boolean insertBike(BikeVO bike);
	BikeVO selectBike(int bikeNo);
	List<BikeVO> selectAllBikes();
	boolean updateBike(BikeVO newBike);
	boolean deleteBike(int bikeNo);
}