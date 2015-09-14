package helperFunctions;

public class MultidimArrays {
	public MultidimArrays(){
		
	}
	/**
	public static double[] getColumnOfDouble(double[][] inputArray,int column, int dimension){
		// dimension can be either 0 or 1
		// column must be within range of arrays
		
		double[] outputColumn = null;
		int i = 0;
		if (dimension == 0){
			while (inputArray[column][i++] != null){
				
			}
			outputColumn = new double[i-1];
			i = 0;
			while (inputArray[column][i++] != null){
				outputColumn[i-1] = inputArray[column][i-1];
			}
		} else {
			if (dimension == 1){
				while (inputArray[i++][column] != null){
				}
				outputColumn = new double[i-1];
				i = 0;
				while (inputArray[i++][column] != null){
					outputColumn[i-1] = inputArray[i-1][column];
				}
			}
		}

		return outputColumn;
	}
	
	public double[] getRow(double[][] inputArray,int row, int dimension){
		double[] outputRow = null;
		int i = 0;
		if (dimension == 0){
			while (inputArray[row][i++] != null){
			}
			outputRow = new double[i-1];
			i = 0;
			while (inputArray[row][i++] != null){
				outputRow[i-1] = inputArray[row][i-1];
			}
		} else {
			if (dimension == 1){
				while (inputArray[i++][row] != null){
				}
				outputRow = new double[i-1];
				i = 0;
				while (inputArray[i++][row] != null){
					outputRow[i-1] = inputArray[i-1][row];
				}
			}
		}

		return outputRow;
	}
	*/
}
