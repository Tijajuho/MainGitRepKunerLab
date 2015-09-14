import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class FileLoading {
	private ArrayList<ArrayList<Integer>> wavelengthChunk = null;
	private ArrayList<Integer> dichroicWavelengths = null;
	private ArrayList<Double> dichroicProbabilities = null;
	private ArrayList<Integer> filterWavelengths = null;
	private ArrayList<Double> filterProbabilities = null;
	
	private int currentFileNumber;
	private int intensity;
	private int tmpBlinkCount;
	private boolean returnAFalse = false;
	private int intensInitial;
	private String filePath;
	
	public FileLoading() {
		
	}
	
	public boolean loadWavelengthFile(String baseFilename, String path, int intensityNumber) throws IOException{
		//load file from path\filenumber\file...
		//extract all wavelengths into inner arraylist AT [blinkingEvent] in outer arraylist AT INTENSITY intensityNumber
		// if intensityNumber not in file: go to top, to next file, store filenumber
		filePath = path;
		BufferedReader input = null;
		String line = null;
		Pattern pattern = null;
		if (intensityNumber==0){
			currentFileNumber = 0;
			tmpBlinkCount = 0;
		}
		String filePath = path + currentFileNumber + baseFilename;
		int blinkCounter = tmpBlinkCount;
		int photonCounter = 0;
		boolean intensCheck = false;

		try {
			//System.out.println("Load file... " + filePath);
			if (tmpBlinkCount == 0) {
				wavelengthChunk = new ArrayList<ArrayList<Integer>>(Collections.nCopies(getBlinkingNumber(baseFilename,path,intensityNumber),new ArrayList<Integer>()));
				if (returnAFalse == true){
					returnAFalse = false;
					return false;
				}
			} else {
				intensCheck = true;
			}
			File thisFile = new File(filePath);
			if (!thisFile.exists()){
				return false;
			}
			input = new BufferedReader(new FileReader(filePath));
			
			pattern = Pattern.compile(" ");
			
			System.out.println("current blinks of intensity number: " + intensityNumber + " intensity: " + intensity);
			while ((line = input.readLine()) != null) {
				if ((intensCheck) && (!((line.split(" ")[0]).equals("intensity:")))) {
					String[] splitLine = pattern.split(line);
					if ((splitLine[0].charAt(0) == "[".charAt(0))&& !((splitLine[splitLine.length-1]).equals("]"))){
						wavelengthChunk.set(blinkCounter, new ArrayList<Integer>(Collections.nCopies(intensity,0)));
						wavelengthChunk.get(blinkCounter).set(0, Integer.valueOf(splitLine[0].substring(1)));
						for (int j = 1; j<splitLine.length;j++){
							photonCounter++;
							wavelengthChunk.get(blinkCounter).set(photonCounter, Integer.valueOf(splitLine[j]));
						}
					}else if (((splitLine[splitLine.length-1]).equals("]"))&&(photonCounter>1)&& !((splitLine[splitLine.length-1]).equals("]"))){
						for (int j=0; j<(splitLine.length-1);j++){
							photonCounter++;
							wavelengthChunk.get(blinkCounter).set(photonCounter, Integer.valueOf(splitLine[j]));
						}
						photonCounter = 0;
						blinkCounter++;
						if (blinkCounter == wavelengthChunk.size()){
							break;
						}
					}else if (photonCounter>1){
						for (int j=0; j<splitLine.length;j++){
							photonCounter++;
							wavelengthChunk.get(blinkCounter).set(photonCounter, Integer.valueOf(splitLine[j]));
						}
					} else if ((splitLine[0].charAt(0) == "[".charAt(0))&&((splitLine[splitLine.length-1]).equals("]"))){
						wavelengthChunk.set(blinkCounter, new ArrayList<Integer>(Collections.nCopies(intensity,0)));
						wavelengthChunk.get(blinkCounter).set(0, Integer.valueOf(splitLine[0].substring(1)));
						for (int j = 1; j<splitLine.length-1;j++){
							wavelengthChunk.get(blinkCounter).set(j, Integer.valueOf(splitLine[j]));
						}
						photonCounter = 0;
						blinkCounter++;
						if (blinkCounter == wavelengthChunk.size()){
							break;
						}
					}
				} //System.out.println(intensities[counter-1][0]);
				if (((line.split(" ")[0]).equals("intensity:")) && ((line.split(" ")[1]).equals(String.valueOf((double)(intensity))) )){
					intensCheck = true;
				}
			};
			if (blinkCounter<wavelengthChunk.size()){
				File nextFile = new File(path+(currentFileNumber+1)+baseFilename);
				if (nextFile.exists()){
					currentFileNumber++;
					tmpBlinkCount = blinkCounter;
					// OK HIER KLAPPT WAS NICHT!!
					loadWavelengthFile(baseFilename, path, intensityNumber);
				}
			} else {
				tmpBlinkCount = 0;
			}
			
			return true;
			//System.out.println(intensities.length);
			
		} finally {
			if (input != null){
				input.close();
				//System.out.println("Input closed.");
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
	
	private int getBlinkingNumber(String baseFilename,String path, int intensityNumber) throws IOException{
		String filename = path + currentFileNumber + baseFilename;
		
		BufferedReader bfr = null;
		String line = null;
		boolean intensCounter = false;
		String intVar = "intensity:";
		int lineCounter = 0;
		try {
			int blinkingNo;
			if (tmpBlinkCount > 0) {
				blinkingNo = tmpBlinkCount;
				intensCounter = true;
			} else {
				blinkingNo = 0;
			}
			bfr = new BufferedReader(new FileReader(filename));
			int lineNumber = getLineNumber(filename);
			while ((line = bfr.readLine()) != null) {
				lineCounter++;
				if (intensCounter) {
					if (!((line.split(" ")[0]).equals(intVar))){
						if (line.charAt(0) == "[".charAt(0)) {
							blinkingNo++;
							//System.out.println("blinking all over the world: " + blinkingNo);
						}
					} else {
						break;
					}
				}
				if (intensityNumber == 0){
					if ((line.split(" ")[0]).equals(intVar)) {
						intensCounter = true;
						intensity = Double.valueOf(line.split(" ")[1]).intValue();
						intensInitial = Double.valueOf(line.split(" ")[1]).intValue();
						System.out.println("intensity next initial: " + intensity);
					}
				} else {
					if (((line.split(" ")[0]).equals(intVar)) && ((line.split(" ")[1]).equals(String.valueOf((double)(intensInitial+intensityNumber))) )){
						intensity = intensInitial+intensityNumber;
						System.out.println("intensity next: " + intensity);
						intensCounter = true;
					}
				}
				if (lineCounter+1==lineNumber && intensCounter == true) {
					File nextFile = new File(path + (currentFileNumber+1) + baseFilename);
					if (nextFile.exists()) {
						int tmpCurFile = currentFileNumber;
						int tmptmpBlinkCount = tmpBlinkCount;
						currentFileNumber++;
						tmpBlinkCount = blinkingNo;
						blinkingNo = getBlinkingNumber(baseFilename,path, intensityNumber);
						tmpBlinkCount = tmptmpBlinkCount;
						currentFileNumber = tmpCurFile;
						return blinkingNo;
					}
				}
			}

			if (intensCounter){
				System.out.println("blinking events in intensity were counted:\n blinks: " + blinkingNo + " intensityNumber: " + intensityNumber +" intensity: " + intensity);
				return blinkingNo;
			} else {
				returnAFalse = true;
				System.out.println("blinking events were not counted:\n intensityNumber: " + intensityNumber);
				return 5;
			}
		} finally {
			if (bfr != null) {
				bfr.close();
				System.out.println("Input closed.");
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

	// Getters aaaand Setters
	public ArrayList<Integer> getFilterWavelengths() {
		return filterWavelengths;
	}

	public ArrayList<Double> getFilterProbabilities() {
		return filterProbabilities;
	}

	public ArrayList<ArrayList<Integer>> getWavelengthChunk() {
		return wavelengthChunk;
	}

	public ArrayList<Integer> getDichroicWavelengths() {
		return dichroicWavelengths;
	}

	public ArrayList<Double> getDichroicProbabilities() {
		return dichroicProbabilities;
	}

	public int getCurrentFileNumber() {
		return currentFileNumber;
	}

	public String getFilePath() {
		return filePath;
	}

//	public void setCurrentFileNumber(int currentFileNumber) {
//		this.currentFileNumber = currentFileNumber;
//	}
//
//	public void setIntensityChunk(ArrayList<ArrayList<Integer>> intensityChunk) {
//		this.intensityChunk = intensityChunk;
//	}
//
//	public void setDichroicWavelengths(ArrayList<Integer> dichroicWavelengths) {
//		this.dichroicWavelengths = dichroicWavelengths;
//	}
//
//	public void setDichroicProbabilities(ArrayList<Double> dichroicProbabilities) {
//		this.dichroicProbabilities = dichroicProbabilities;
//	}
	
}
