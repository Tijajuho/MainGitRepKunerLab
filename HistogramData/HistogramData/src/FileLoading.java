import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Pattern;


public class FileLoading {
	private static double[][] intensities; // = new double[2][getLineNumber()]
	//private static double[] intensRight;
	
	private FileLoading() {
	}
	
	public static void loadFile(String filename) throws IOException{
		BufferedReader input = null;
		Integer counter = null;
		String line = null;
		Pattern pattern = null;
		try {
			System.out.println("Load file... " + filename);
			intensities = new double[2][(getLineNumber(filename)-1)];
			input = new BufferedReader(new FileReader(filename));
			
			counter = -1;
			pattern = Pattern.compile(" ");
			
			while ((line = input.readLine()) != null) {
				if (counter++ > -1) {
					String[] splitLine = pattern.split(line);
					intensities[0][counter-1] = Double.valueOf(splitLine[4]);
					intensities[1][counter-1] = Double.valueOf(splitLine[10]);
					//System.out.println(intensities[counter-1][0]);
				}
			};
			//System.out.println(intensities.length);
		}
		finally{
			if (input != null){
				input.close();
				System.out.println("Input closed.");
			}
		}
	}
	
	public static void loadFiles(String filename1, String filename2) throws IOException{
		BufferedReader input1 = null;
		BufferedReader input2 = null;
		Integer counter = null;
		String line = null;
		Pattern pattern = null;
		intensities = null;
		
		try {
			System.out.println("Load left file... " + filename1);
			intensities = new double[2][((getLineNumber(filename1)-1)>(getLineNumber(filename2)-1)) ? 
											(getLineNumber(filename1)-1) : (getLineNumber(filename2)-1)];
			input1 = new BufferedReader(new FileReader(filename1));
			input2 = new BufferedReader(new FileReader(filename2));
			
			counter = -1;
			pattern = Pattern.compile(" ");
			
			while ((line = input1.readLine()) != null) {
				if (counter++ > -1) {
					String[] splitLine = pattern.split(line);
					intensities[0][counter-1] = Double.valueOf(splitLine[3]);
					//System.out.println(intensities[counter-1][0]);
				}
			};
			System.out.println("Load right file... " + filename2);
			counter = -1;
			while ((line = input2.readLine()) != null) {
				if (counter++ > -1) {
					String[] splitLine = pattern.split(line);
					intensities[1][counter-1] = Double.valueOf(splitLine[3]);
					//System.out.println(intensities[counter-1][0]);
				}
			};
			//System.out.println(intensities.length);
		}
		finally{
			if (input1 != null){
				input1.close();
				System.out.println("Input1 closed.");
			}
			if (input2 != null){
				input2.close();
				System.out.println("Input2 closed.");
			}
		}
	}
	
	private static int getLineNumber(String filename) throws IOException {
		LineNumberReader lnr = null;
		try {
		int lineNo = 0;
		lnr = new LineNumberReader(new FileReader(filename));
		lnr.skip(Long.MAX_VALUE);
		lineNo = lnr.getLineNumber() + 1; //Add 1 because line index starts at 0
		// Finally, the LineNumberReader object should be closed to prevent resource leak
		return lineNo;
		} finally {
			lnr.close();
		}
	}
	
	public static double[][] getIntensities() {
		return intensities;
	}
	
}
