package bike.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bike.BikeVO;
import bike.HashMapBikeDAO;

public class TextFileHashMapBikeDAO extends HashMapBikeDAO implements FileBikeDB {

	private String dataFilename = DATA_FILE + ".txt";
	private final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
			
	@Override
	public void saveBikes() {
		
		try (
			FileWriter fw = new FileWriter(dataFilename);
			PrintWriter pw = new PrintWriter(fw);
		) {
			
			for (BikeVO bike : bikeDB.values()) {
				pw.println(bike.getBikeNo());
				pw.println(bike.getName());
				pw.println(bike.getModel());
				pw.println(bike.getBrand());
				pw.println(bike.getPrice());
				pw.println(bike.getInstock());
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				pw.println(sdf.format(bike.getRegdate()));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
	}

	@Override
	public void loadBikes() {

		try ( FileReader fr = new FileReader(dataFilename);
			  BufferedReader br = new BufferedReader(fr);
		) {
			
			while (br.ready()) {
				int bikeNo = Integer.parseInt(br.readLine());
				String name = br.readLine().strip();
				String model = br.readLine().strip();
				String brand = br.readLine().strip();
				int price = Integer.parseInt(br.readLine());
				int instock = Integer.parseInt(br.readLine());
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				Date regdate = sdf.parse(br.readLine());
				
				bikeDB.put(bikeNo, new BikeVO(bikeNo, name, model, brand, price, instock, regdate));
				
				if (bikeSeq <= bikeNo) bikeSeq = bikeNo + 1;
			}
		} catch (FileNotFoundException e) {
			System.out.println("[로딩] " + dataFilename + "이 없습니다.");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}

}
