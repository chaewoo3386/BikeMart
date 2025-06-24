package bike.file;

public interface FileBikeDB {
	String DATA_FILE = "./data/bikeDB";
	void saveBikes();
	void loadBikes();
}
