import java.util.Arrays;


public class BinsOfHistogram {
	private static double[][] intensities = null;
	private static final double numberBins = 200;
	
	private BinsOfHistogram(){
		
	}
	
	public static Results createHistogramData(Boolean angleProcessing){
		intensities = FileLoading.getIntensities();
		Results resultsLeft = null;
		Results resultsRight = null;
		Results results = null;
		if (angleProcessing == true){
			boolean roundIntens = false;
			results = createBins(calculateAngles(intensities),roundIntens);
		} else {
			boolean roundIntens = true;
			resultsLeft = createBins(intensities[0],roundIntens);
			resultsRight = createBins(intensities[1],roundIntens);
			results = new Results(resultsLeft.getCounts(),resultsRight.getCounts(),resultsLeft.getCenters(),resultsRight.getCenters());
		}
		return results;
	}
	
	private static double[] calculateAngles(double[][] intensities){
		double[] angles = new double[intensities[0].length];
		int i = 0;
		
		while (i++ < intensities[0].length){
			angles[i-1] = Math.atan(intensities[1][i-1]/intensities[0][i-1]);
		}
		
		return angles;
	}
	
	private static Results createBins(double[] data, boolean roundIntens){
		
		Results resultsIntern = null;
		int[] binCounts = new int[(int)(numberBins)];
		double[] binCenters = new double[(int)(numberBins)];
		int i = -1;

		double tmpMax = data[0];
		double binSize;
		double[] intens = new double[data.length];
		if (roundIntens) {
			while (++i < data.length)
				data[i] = Math.round(data[i]);
		}
		
		i = -1;
		while (++i < data.length){
			if (data[i]>tmpMax) {
				tmpMax = data[i];
			}
			intens[i] = data[i];
		}
		
		binSize = tmpMax/numberBins;
		//System.out.println(binSize);
		
		Arrays.sort(intens);
		i = 0;
		int tmpCounts = 0;
		
		for (int j=0;j<numberBins;j++){
			//System.out.println("LEFT");
			int counter = 0;
			for (i = (j>0) ? tmpCounts : 0;i<intens.length;i++){	
				//System.out.println(i);
				counter++;
				//System.out.println("counter: " + counter);
				if (intens[i] >= (binSize * (j+1))) {
					binCounts[j] = counter-1;
					if (roundIntens) {
						binCenters[j] = Math.round((binSize*j) + (binSize/2));
					} else {
						binCenters[j] = (binSize*j) + (binSize/2);
					}
					//System.out.println("intensity: " + intensLeft[i]);
					//System.out.println(binCountsLeft[j]);
					if (binCounts[j] !=0) {
						tmpCounts += binCounts[j];
					}
					//System.out.println(binCentersLeft[j]);
					break;
				}
			}
			//System.out.println("RIGHT");
			counter = 0;			
		}

		resultsIntern = new Results(binCounts,binCenters);
		
		return resultsIntern;
	}

}
