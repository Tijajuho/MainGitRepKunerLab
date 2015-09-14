package helperFunctions;

public class test {
	public static void main(String[] args){
		double[][] doubleTest = {
				{1.2, 1.3, 1.4},{1.5,1.6,1.7}
		};
		int[][] intTest = {
				{2, 3, 4},{5,6,7}
		};
		double[] columnDouble = null;
		System.out.println(doubleTest.length);
		System.out.println(doubleTest[0].length);
		//columnDouble = MultidimArrays.getColumn(doubleTest, 1, 1);
	}
}
