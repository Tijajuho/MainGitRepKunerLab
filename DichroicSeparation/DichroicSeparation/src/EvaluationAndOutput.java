import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EvaluationAndOutput {
	ArrayList<ArrayList<Integer>> input = null;
	ArrayList<Integer> intensList = null;
	String fileTag = null;
	FileLoadingOptimized flo1 = null;
	
	public EvaluationAndOutput(ArrayList<ArrayList<Integer>> input, ArrayList<Integer> intensList, String fileTag,FileLoadingOptimized flo1) {
		this.input = input;
		this.intensList = intensList;
		this.fileTag = fileTag;
		this.flo1 = flo1;
	}
	
	
	public void saveData() throws IOException{
		PrintWriter output = null;
		try {
			String path = flo1.getPath();
			int fileNumber = 0;
			String newFileName = path + fileTag + path.split("\\\\")[path.split("\\\\").length-1] + "_" + (int)(fileNumber) + ".txt";
			File theNewFile = new File(newFileName);
			while (theNewFile.exists()){
				fileNumber++;
				newFileName = path + fileTag + path.split("\\\\")[path.split("\\\\").length-1] + "_" + (int)(fileNumber) + ".txt";
				theNewFile = new File(newFileName);
			}
				
			output = new PrintWriter(new FileWriter(newFileName));
			output.println(fileTag);
			for (int i = 0; i<input.size();i++){
				if ( ((i > 0) && (!intensList.get(i).equals(intensList.get(i-1)))) || (i == 0) ){
					output.println("original intensity: " + intensList.get(i));
					output.println("effective intensities of blinking events:");
					output.print("[ ");
				}
				for (int j = 0; j<input.get(i).size();j++){
					output.print(String.valueOf(input.get(i).get(j)) + " ");
				}
				if( ((i+1<intensList.size()) && (!intensList.get(i).equals(intensList.get(i+1)))) || (i+1==intensList.size()) ){
					output.println("]");
				}
			}
		} finally {
			if (output != null){
				output.close();
			}
		}
	}
}
