package bike;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapBikeDAO implements BikeDAO {

	protected Map<Integer, BikeVO> bikeDB = new HashMap<>();
	protected int bikeSeq = 111;
	
	@Override
	public boolean insertBike(BikeVO bike) {
		bike.setBikeNo(bikeSeq++);
		bike.setRegdate(new Date());
		bikeDB.put(bike.getBikeNo(), bike);
		return true;
	}

	@Override
	public BikeVO selectBike(int bikeNo) {
		return bikeDB.get(bikeNo);
	}

	@Override
	public List<BikeVO> selectAllBikes() {
		return new ArrayList<>(bikeDB.values());
	}

	@Override
	public boolean updateBike(BikeVO newBike) {
		bikeDB.put(newBike.getBikeNo(), newBike);
		return true;
	}

	@Override
	public boolean deleteBike(int bikeNo) {
		return bikeDB.remove(bikeNo) != null;
	}

}