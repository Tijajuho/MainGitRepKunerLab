import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class FileLoadingOptimized {
	private ArrayList<Integer> dichroicWavelengths = null;
	private ArrayList<Double> dichroicProbabilities = null;
	private ArrayList<Integer> filterWavelengths = null;
	private ArrayList<Double> filterProbabilities = null;
	
	private String baseFilename = null;
	private String path = null;
	
	private int currentFileNumber = 0;
	
	private LoadedFileStorage lfs1 = null;
	
	public FileLoadingOptimized(String baseFilename, String path, LoadedFileStorage lfs1) {
		this.baseFilename = baseFilename;
		this.path = path;
		this.lfs1 = lfs1;
	}
	
	public boolean loadWavelengthFile() throws IOException{
		String filePath = path;
		BufferedReader input = null;
		String line = null;
		Pattern pattern = null;
		boolean startCheck = true;
		int intensity = 0;
		int blinkingNo = 0;
		int wavelength = 0;
		try {
			filePath = path + currentFileNumber + baseFilename;
			File currentFile = new File(filePath);
			if (!currentFile.exists()) {
				return false;
			}
			input = new BufferedReader(new FileReader(filePath));
			
			//load complete first file into LoadedFileStorage object
			//in object: if requested intensity is at end of ArrayList --> load next File
			pattern = Pattern.compile(" ");
			while ((line = input.readLine()) != null) {
				String[] splitLine = pattern.split(line);
				if ((splitLine[0].charAt(0) == "[".charAt(0))&&((splitLine[splitLine.length-1]).equals("]"))){
					if (startCheck == true) {
						intensity = splitLine.length-1;
						startCheck = false;
						blinkingNo = 0;
					} else if (intensity != splitLine.length-1){
						intensity = splitLine.length-1;
						blinkingNo =0;
					}
					for (int j = 0; j<splitLine.length-1;j++){
						if (j==0) {
							wavelength = Integer.valueOf(splitLine[j].substring(1));
						} else {
							wavelength = Integer.valueOf(splitLine[j]);
						}
						lfs1.setWavelengths(intensity, blinkingNo, wavelength, currentFileNumber);
					}
					blinkingNo++;
				}
			}
			return true;
		} finally {
			if (input != null) {
				input.close();
			}
		}
		
	}
	
	public void loadDichroicMirror() throws IOException{
		String filePath = "\\\\129.206.158.175\\FN-Praktikant\\Timm\\SpectraRawEtc\\F33-692Strahlteiler.txt";
		BufferedReader input = null;
		String line = null;
		Pattern pattern = null;
		int counter = 0;
		//load file
		// write wavelengths into variable
		// write probabilities into variable
		try {
			System.out.println("Load file... " + filePath);
			dichroicWavelengths = new ArrayList<Integer>(Collections.nCopies(getLineNumber(filePath)-1,0));
			dichroicProbabilities = new ArrayList<Double>(Collections.nCopies(dichroicWavelengths.size(),0.));
			input = new BufferedReader(new FileReader(filePath));
			
			pattern = Pattern.compile(" ");
			
			while ((line = input.readLine()) != null) {
				String[] splitLine = pattern.split(line);
				dichroicWavelengths.set(counter,Integer.valueOf(splitLine[0]));
				dichroicProbabilities.set(counter, Double.valueOf(splitLine[1]));
				counter++;
				//System.out.println(intensities[counter-1][0]);
			};
			//System.out.println(intensities.length);
			
		} finally {
			if (input != null){
				input.close();
				System.out.println("Input closed.");
			}
		}
	}
	
	public void loadFilters() throws IOException {
		String filePath = "\\\\129.206.158.175\\FN-Praktikant\\Timm\\filterFiles\\combinedFilters.txt";
		BufferedReader input = null;
		String line = null;
		Pattern pattern = null;
		int counter = 0;
		//load file
		// write wavelengths into variable
		// write probabilities into variable
		try {
			System.out.println("Load file... " + filePath);
			filterWavelengths = new ArrayList<Integer>(Collections.nCopies(getLineNumber(filePath)-1,0));
			filterProbabilities = new ArrayList<Double>(Collections.nCopies(filterWavelengths.size(),0.));
			input = new BufferedReader(new FileReader(filePath));
			
			pattern = Pattern.compile(" ");
			
			while ((line = input.readLine()) != null) {
				String[] splitLine = pattern.split(line);
				filterWavelengths.set(counter,Integer.valueOf(splitLine[0]));
				filterProbabilities.set(counter, Double.valueOf(splitLine[1]));
				counter++;
				//System.out.println(intensities[counter-1][0]);
			};
			//System.out.println(intensities.length);
			
		} finally {
			if (input != null){
				input.close();
				System.out.println("Input closed.");
			}
		}
	}
	
	private int getLineNumber(String filename) throws IOException {
		LineNumberReader lnr = null;
		try {
		int lineNo = 0;
		lnr = new LineNumberReader(new FileReader(filename));
		lnr.skip(Long.MAX_VALUE);
		lineNo = lnr.getLineNumber() + 1; //Add 1 because line index starts at 0
		// Finally, the LineNumberReader object should be closed to prevent resource leak
		return lineNo;
		} finally {
			if (lnr != null) {
				lnr.close();
			}
		}
	}

	public ArrayList<Integer> getDichroicWavelengths() {
		return dichroicWavelengths;
	}

	public ArrayList<Double> getDichroicProbabilities() {
		return dichroicProbabilities;
	}

	public ArrayList<Integer> getFilterWavelengths() {
		return filterWavelengths;
	}

	public ArrayList<Double> getFilterProbabilities() {
		return filterProbabilities;
	}

	public int getCurrentFileNumber() {
		return currentFileNumber;
	}

	public void setCurrentFileNumber(int currentFileNumber) {
		this.currentFileNumber = currentFileNumber;
	}

	public String getBaseFilename() {
		return baseFilename;
	}

	public String getPath() {
		return path;
	}
}
