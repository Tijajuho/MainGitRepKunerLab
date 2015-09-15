import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ActualSeparation {
	private Random rnd = new Random(2);
	private ArrayList<Integer> dichroicWavelengths = null;
	private ArrayList<Double> dichroicProbabilities = null;
	private ArrayList<Integer> filterWavelengths = null;
	private ArrayList<Double> filterProbabilities = null;
	//private static ArrayList<ArrayList<ArrayList<Integer>>> leftChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	//private static ArrayList<ArrayList<ArrayList<Integer>>> rightChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	private ArrayList<Integer> intensityList = null;
	private ArrayList<ArrayList<Integer>> leftCounts = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> rightCounts = new ArrayList<ArrayList<Integer>>();
	FileLoading fl1 = null;
	
	public ActualSeparation(FileLoading fl1){
		this.fl1 = fl1;
	}
	
	public void distributePhotons(String path, String baseFilename) throws IOException{
		try {
			ArrayList<ArrayList<Integer>> wavelengthChunk = null;
			// getWavelengths and Probabilities from dichroic mirror
			dichroicWavelengths = fl1.getDichroicWavelengths();
			dichroicProbabilities = fl1.getDichroicProbabilities();
			filterWavelengths = fl1.getFilterWavelengths();
			filterProbabilities = fl1.getFilterProbabilities();
			
			intensityList = new ArrayList<Integer>();
			// iterate for all intensities over:
				// loadFile and getWavelengths for intensity/ OR LOAD FILE COMPLETELY???
				// distribute into left and right according to probabilities (built in function?? like choice?)
				// store in variable
			// function returns false if given intensity is not in files
			int chunkCounter = 0;
			for (int i = 0; i<20;i++){
				boolean falseCheck = fl1.loadWavelengthFile(baseFilename, path, i);
				if (falseCheck) {
					wavelengthChunk = fl1.getWavelengthChunk();
					//leftChannel.add(new ArrayList<ArrayList<Integer>>());
					//rightChannel.add(new ArrayList<ArrayList<Integer>>());
					rightCounts.add(new ArrayList<Integer>());
					leftCounts.add(new ArrayList<Integer>());
					intensityList.add(wavelengthChunk.get(0).size());
					for (int j = 0; j<wavelengthChunk.size();j++){
						int leftCounter = 0;
						int rightCounter = 0;
						//leftChannel.get(chunkCounter).add(new ArrayList<Integer>());
						//rightChannel.get(chunkCounter).add(new ArrayList<Integer>());
						for (int k = 0; k<wavelengthChunk.get(j).size();k++){
							if (!checkIfDiscarded(wavelengthChunk.get(j).get(k))) {
								if (checkIfLeft(wavelengthChunk.get(j).get(k)) ){
									//leftChannel.get(chunkCounter).get(j).add(wavelengthChunk.get(j).get(k));
									//System.out.println("left: " + leftChannel.get(chunkCounter).get(j).get(leftCounter) );
									leftCounter++;
								} else {
									//rightChannel.get(chunkCounter).get(j).add(wavelengthChunk.get(j).get(k));
									//System.out.println("right: " + rightChannel.get(chunkCounter).get(j).get(rightCounter) );
									rightCounter++;
								}
							}
						}
						
						leftCounts.get(chunkCounter).add(leftCounter);
						rightCounts.get(chunkCounter).add(rightCounter);
					}
					chunkCounter++;
					System.out.println("chunkCounter in distributePhotons: "  + chunkCounter);
				}
				
			}
			EvaluationAndOutput outputLeft = new EvaluationAndOutput(leftCounts,intensityList,"leftChannelBlinkingEvents",fl1);
			outputLeft.saveData();
			EvaluationAndOutput outputRight = new EvaluationAndOutput(rightCounts,intensityList,"rightChannelBlinkingEvents",fl1);
			outputRight.saveData();
		} finally {
			
		}
	}
	
	private boolean checkIfLeft(int wavelength){
		boolean left = false;
		double randNo = rnd.nextDouble()*100;
		int waveIndex = dichroicWavelengths.indexOf(wavelength);
		if (dichroicProbabilities.get(waveIndex) < randNo) {
			left = true;
		} else {
			left = false;
		}
		return left;
	}
	
	private boolean checkIfDiscarded(int wavelength) {
		boolean discarded = false;
		double randNo = rnd.nextDouble();
		int waveIndex = filterWavelengths.indexOf(wavelength);
		
		if (filterProbabilities.get(waveIndex) > randNo) {
			discarded = false;
		} else {
			discarded = true;
		}
		return discarded;
	}
}

//public class ChunkProcessing implements Runnable {
//	public ChunkProcessing(){
//		
//	}
//	
//}
