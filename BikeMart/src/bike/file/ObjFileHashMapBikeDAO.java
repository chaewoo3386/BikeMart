package bike.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bike.BikeVO;
import bike.HashMapBikeDAO;

public class ObjFileHashMapBikeDAO extends HashMapBikeDAO implements FileBikeDB {

	private String dataFilename = DATA_FILE + ".obj";
	
	public ObjFileHashMapBikeDAO() {
		loadBikes();
	}
	
	@Override
	public void saveBikes() {
		
		try (
			FileOutputStream fos = new FileOutputStream(dataFilename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
				
		) {
			oos.writeObject(bikeDB);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void loadBikes() {
		
		try (
			FileInputStream fis = new FileInputStream(dataFilename);
			ObjectInputStream ois = new ObjectInputStream(fis);
		) {
			
			bikeDB = (Map<Integer, BikeVO>)ois.readObject();
			bikeSeq = Collections.max(bikeDB.keySet()) + 1;
			
		} catch (FileNotFoundException e) {
			System.out.println("[DB로딩] " + dataFilename + "가 없습니다.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean insertBike(BikeVO bike) {
		boolean result = super.insertBike(bike);
		if (result) saveBikes();
		return result;
	}

	@Override
	public boolean updateBike(BikeVO newBike) {
		boolean result = super.updateBike(newBike);
		if (result) saveBikes();
		return result;
	}
	
	@Override
	public boolean deleteBike(int bikeNo) {
		boolean result = super.deleteBike(bikeNo);
		if (result) saveBikes();
		return result;
	}

}