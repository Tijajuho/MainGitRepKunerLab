import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOutput {
	
	private FileOutput() {
		
	}
	
	public static void saveFile(Results results, String pathname, String filename,Boolean angleProcessing) throws IOException {
		PrintWriter output = null;
		try {
			if (angleProcessing) {
				output = new PrintWriter(new FileWriter(pathname + "HistoDataAngleROUND_" + filename));
				output.println("Centers Counts");
				System.out.println("Write file... " + pathname + "HistoDataAngleROUND_" + filename);
			} else {
				output = new PrintWriter(new FileWriter(pathname + "HistoDataIntensROUND_" + filename));
				output.println("CentersLeft CountsLeft CentersRight CountsRight");
				System.out.println("Write file... " + pathname + "HistoDataIntensROUND_" + filename);
			}
			int i = 0;
			
			if (angleProcessing){
				while (i++ < results.getCenters().length) {
					output.println(results.getCenters()[i-1] + " " + results.getCounts()[i-1]);
				}
			} else {
				while (i++ < results.getBinsLeft().length){
					output.println(results.getBinsLeft()[i-1] + " " + results.getCountsLeft()[i-1] + " " +
							results.getBinsRight()[i-1]+ " " + results.getCountsRight()[i-1]);
				}
			}
		}finally{
			if (output != null){
				output.close();
				System.out.println("Output closed.");
			}
		}
        
	}
}
