package bike;

import java.util.LinkedList;
import java.util.List;

public class ListBikeDAO implements BikeDAO {

	private List<BikeVO> bikeList = new LinkedList<BikeVO>();
	private int bikeSeq = 111; // 자전거 번호 1씩 증가
	
	@Override
	public boolean insertBike(BikeVO bike) {
		bike.setBikeNo(bikeSeq++);
		bikeList.add(bike);
		return true;
	}

	@Override
	public BikeVO selectBike(int bikeNo) {
		for (BikeVO bike : bikeList) {
			if (bike.getBikeNo() == bikeNo)
				return bike;
		}
		return null;
	}

	@Override
	public List<BikeVO> selectAllBikes() {
		return bikeList;
	}

	@Override
	public boolean updateBike(BikeVO newBike) {
		for (int i = 0; i < bikeList.size(); i++) {
			if (bikeList.get(i).getBikeNo() == newBike.getBikeNo()) {
				bikeList.set(i, newBike);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteBike(int bikeNo) {

		for (BikeVO bike : bikeList) {
			if (bike.getBikeNo() == bikeNo) {
				bikeList.remove(bike);
				return true;
			}
		}
		return false;
	}

}