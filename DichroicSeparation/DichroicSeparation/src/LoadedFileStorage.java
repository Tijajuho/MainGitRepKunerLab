import java.util.ArrayList;

public class LoadedFileStorage {
	private ArrayList<ArrayList<ArrayList<Integer>>> currentFile = null;
	private int currentIntensity = 0;
	private int currentBlink = -1;
	private int intensCounter = -1;
	private int fileSize = 0;
	private int currentFileNumber = -1;
	
	private ArrayList<ArrayList<Integer>> leftCounts = null;
	private ArrayList<ArrayList<Integer>> rightCounts = null;
	private ArrayList<Integer> intensityList = null;
	
	public LoadedFileStorage() {
		currentFile = new ArrayList<ArrayList<ArrayList<Integer>>>();
		leftCounts = new ArrayList<ArrayList<Integer>>();
		rightCounts = new ArrayList<ArrayList<Integer>>();
		intensityList = new ArrayList<Integer>();
	}
	
	public void setWavelengths(int intensity,int blinkingEventNo, int wavelength, int fileNumber){
		if (currentFileNumber != fileNumber) {
			currentFileNumber = fileNumber;
			currentIntensity = 0;
			intensCounter = -1;
		}
		if (currentIntensity != intensity) {
			currentIntensity = intensity;
			currentFile.add(new ArrayList<ArrayList<Integer>>());
			currentBlink = -1;
			intensCounter++;
		}
		
		if (currentBlink != blinkingEventNo) {
			currentFile.get(intensCounter).add(new ArrayList<Integer>());
			currentBlink = blinkingEventNo;
		}
		
		currentFile.get(intensCounter).get(currentBlink).add(wavelength);

		fileSize = currentFile.size();
	}
	
	public ArrayList<ArrayList<Integer>> getWavelengthChunk(int intensNumber) {
		ArrayList<ArrayList<Integer>> wavelengthChunk = new ArrayList<ArrayList<Integer>>();
		
		wavelengthChunk = currentFile.get(intensNumber);
		
		return wavelengthChunk;
	}
	
	public void resetCurrentFile(){
		currentFile.removeAll(currentFile);
		currentFile = new ArrayList<ArrayList<ArrayList<Integer>>>();
	}
	
	public int getFileSize(){
		return fileSize;
	}

	public ArrayList<ArrayList<Integer>> getLeftCounts() {
		return leftCounts;
	}

	public void setLeftCounts(ArrayList<ArrayList<Integer>> leftCounts) {
		this.leftCounts = leftCounts;
	}

	public ArrayList<ArrayList<Integer>> getRightCounts() {
		return rightCounts;
	}

	public void setRightCounts(ArrayList<ArrayList<Integer>> rightCounts) {
		this.rightCounts = rightCounts;
	}

	public ArrayList<Integer> getIntensityList() {
		return intensityList;
	}

	public void setIntensityList(ArrayList<Integer> intensityList) {
		this.intensityList = intensityList;
	}
}
