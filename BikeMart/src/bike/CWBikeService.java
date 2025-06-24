package bike;

import java.util.List;

public class CWBikeService implements BikeService {

	private BikeDAO bikeDAO;
	
	public CWBikeService(BikeDAO bikeDAO) {
		this.bikeDAO = bikeDAO;
	}
	
	@Override
	public boolean registBike(BikeVO bike) {
		return bikeDAO.insertBike(bike);
	}

	@Override
	public List<BikeVO> listBikes() {
		return bikeDAO.selectAllBikes();
	}

	@Override
	public BikeVO detailBikeInfo(int bikeNo) {
		
		return bikeDAO.selectBike(bikeNo);
	}

	@Override
	public boolean updateBikePrice(int bikeNo, int price) {
		BikeVO bike = bikeDAO.selectBike(bikeNo);
		
		if (bike != null) {
			bike.setPrice(price);
			bikeDAO.updateBike(bike);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean updateBikeInstock(int bikeNo, int instock) {
		BikeVO bike = bikeDAO.selectBike(bikeNo);
		
		if (bike != null) {
			bike.setInstock(instock);
			bikeDAO.updateBike(bike);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean removeBike(int bikeNo) {
		/*
		BikeVO bike = bikeDAO.selectBike(bikeNo);
		if (bike != null) {
			bikeDAO.deleteBike(bikeNo);
			return true;
		}
		return false;
		 */
		return bikeDAO.deleteBike(bikeNo);		
	}

}