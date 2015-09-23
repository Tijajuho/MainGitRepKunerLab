import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ActualSeparation {
	private Random rnd = new Random(2);
	//private static ArrayList<ArrayList<ArrayList<Integer>>> leftChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	//private static ArrayList<ArrayList<ArrayList<Integer>>> rightChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	private FileLoadingOptimized flo1 = null;
	private LoadedFileStorage lfs1 = null;
	
	private ExecutorService executor;
	
	public ActualSeparation(FileLoadingOptimized flo1, LoadedFileStorage lfs1){
		this.flo1 = flo1;
		this.lfs1 = lfs1;
	}
	
	public void distributePhotons() throws IOException, InterruptedException{
		try {
			ArrayList<ArrayList<Integer>> wavelengthChunk = null;
			// getWavelengths and Probabilities from dichroic mirror
			CheckIfs.initChecks(rnd,flo1);
			
			// iterate for all intensities over:
				// loadFile and getWavelengths for intensity/ OR LOAD FILE COMPLETELY???
				// distribute into left and right according to probabilities (built in function?? like choice?)
				// store in variable
			// function returns false if given intensity is not in files

			executor =  Executors.newFixedThreadPool(7);
			boolean falseCheck = true;
			Object lock = new Object();
			int indiI = 0;
			for (int i = 0; i<50000;i++){
				if (lfs1.getFileSize() == 0) {
					falseCheck = flo1.loadWavelengthFile();
					flo1.setCurrentFileNumber(flo1.getCurrentFileNumber()+1);
				} else if (indiI == lfs1.getFileSize()){
					Thread.sleep(5000+flo1.getCurrentFileNumber()*100);
					System.out.println("Next file loading: " + flo1.getCurrentFileNumber()+flo1.getBaseFilename());
					lfs1.resetCurrentFile();
					falseCheck = flo1.loadWavelengthFile();
					flo1.setCurrentFileNumber(flo1.getCurrentFileNumber()+1);
					indiI = 0;
				}
				if (falseCheck == false) {
					System.out.println("no file remaining");
					break;

				}
				
				Runnable t = new Thread(new ChunkProcessing(i, indiI, wavelengthChunk, flo1, lfs1, lock, rnd));
				executor.execute(t);
				indiI++;
			}
			executor.shutdown();
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
			
			}
			EvaluationAndOutput outputLeft = new EvaluationAndOutput(lfs1.getLeftCounts(),lfs1.getIntensityList(),"leftChannelBlinkingEvents",flo1);
			outputLeft.saveData();
			EvaluationAndOutput outputRight = new EvaluationAndOutput(lfs1.getRightCounts(),lfs1.getIntensityList(),"rightChannelBlinkingEvents",flo1);
			outputRight.saveData();
		} finally {
			
		}
	}
	
}

class ChunkProcessing implements Runnable {
	//private ArrayList<ArrayList<ArrayList<Integer>>> leftChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	//private ArrayList<ArrayList<ArrayList<Integer>>> rightChannel = new ArrayList<ArrayList<ArrayList<Integer>>>();
	private int i;
	private int indiI;
	private ArrayList<ArrayList<Integer>> wavelengthChunk = null;
	private LoadedFileStorage lfs1 = null;
	private Object lock;
	private Random rnd;
	

	
	public ChunkProcessing(int i, int indiI, ArrayList<ArrayList<Integer>> wavelengthChunk, 
			FileLoadingOptimized flo1, LoadedFileStorage lfs1, Object lock, Random rndVar) {
		this.i = i;
		this.wavelengthChunk = wavelengthChunk;
		this.lfs1 = lfs1;
		this.lock = lock;
		this.rnd = rndVar;
		this.indiI = indiI;
	}



	public void run(){
		System.out.println("currently processing intensity" + i);
		//leftChannel.add(new ArrayList<ArrayList<Integer>>());
		//rightChannel.add(new ArrayList<ArrayList<Integer>>());
		synchronized (lock) {
			wavelengthChunk = lfs1.getWavelengthChunk(indiI);
			lfs1.getRightCounts().add(new ArrayList<Integer>());
			lfs1.getLeftCounts().add(new ArrayList<Integer>());
			lfs1.getIntensityList().add(wavelengthChunk.get(0).size());
		}
		for (int j = 0; j<wavelengthChunk.size();j++){
			int leftCounter = 0;
			int rightCounter = 0;
			//leftChannel.get(chunkCounter).add(new ArrayList<Integer>());
			//rightChannel.get(chunkCounter).add(new ArrayList<Integer>());
			for (int k = 0; k<wavelengthChunk.get(j).size();k++){
				if (!CheckIfs.checkIfDiscarded(wavelengthChunk.get(j).get(k))) {
					if (CheckIfs.checkIfLeft(wavelengthChunk.get(j).get(k)) ){
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
			synchronized (lock) {
				double intensStd = 47.7;
				leftCounter = leftCounter+(int)(Math.round((rnd.nextGaussian()*intensStd))); // watch out, I guess this is not thread-proof
				rightCounter = rightCounter+(int)(Math.round((rnd.nextGaussian()*intensStd)));
				if (leftCounter <= 0) {
					leftCounter = 0;
				}
				if (rightCounter <= 0) {
					rightCounter = 0;
				}
				lfs1.getLeftCounts().get(i).add(leftCounter);
				lfs1.getRightCounts().get(i).add(rightCounter);
			}
		}
	}
	
}
