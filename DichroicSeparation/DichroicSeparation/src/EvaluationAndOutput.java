import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EvaluationAndOutput {
	ArrayList<ArrayList<Integer>> input = null;
	ArrayList<Integer> intensList = null;
	String fileTag = null;
	FileLoading fl1 = null;
	
	public EvaluationAndOutput(ArrayList<ArrayList<Integer>> input, ArrayList<Integer> intensList, String fileTag,FileLoading fl1) {
		this.input = input;
		this.intensList = intensList;
		this.fileTag = fileTag;
		this.fl1 = fl1;
	}
	
	
	public void saveData() throws IOException{
		PrintWriter output = null;
		try {
			String path = fl1.getFilePath();
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
				output.println("original intensity: " + intensList.get(i));
				output.println("effective intensities of blinking events:");
				output.print("[ ");
				for (int j = 0; j<input.get(i).size();j++){
					output.print(String.valueOf(input.get(i).get(j)) + " ");
				}
				output.println("]");
			}
		} finally {
			if (output != null){
				output.close();
			}
		}
	}
}
