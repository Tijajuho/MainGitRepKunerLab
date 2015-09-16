
import java.util.ArrayList;
import java.util.Random;

public class CheckIfs {
	private static Random rnd = null;
	private static FileLoadingOptimized flo1 = null;
	private static ArrayList<Integer> dichroicWavelengths  = null;
	private static ArrayList<Double> dichroicProbabilities = null;
	private static ArrayList<Integer> filterWavelengths = null;
	private static ArrayList<Double> filterProbabilities = null;
	
	public static void initChecks(Random rndVar, FileLoadingOptimized flo1Var){
		rnd = rndVar;
		flo1 = flo1Var;
		dichroicWavelengths = flo1.getDichroicWavelengths();
		dichroicProbabilities = flo1.getDichroicProbabilities();
	    filterWavelengths = flo1.getFilterWavelengths();
		filterProbabilities = flo1.getFilterProbabilities();
	}
	
	public static boolean checkIfLeft(int wavelength){
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
	
	public static boolean checkIfDiscarded(int wavelength) {
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
