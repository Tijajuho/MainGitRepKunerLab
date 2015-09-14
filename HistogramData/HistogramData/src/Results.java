
public class Results {
	private int[] countsLeft;
	private int[] countsRight;
	private double[] binCentersLeft;
	private double[] binCentersRight;
	private int[] counts;
	private double[] centers;
	private int[] centersInt;
	
	public Results(int[] countsLeft,int[] countsRight,
			double[] binCentersLeft,double[] binCentersRight){
		this.countsLeft = countsLeft;
		this.countsRight = countsRight;
		this.binCentersLeft = binCentersLeft;
		this.binCentersRight = binCentersRight;
	}
	
	public Results(int[] counts,double[] centers){
		this.counts = counts;
		this.centers = centers;
	}
	
	public Results(int[] counts,int[] centers){
		this.centersInt = centers;
		this.counts= counts;
	}
	
	public int[] getCentersInt() {
		return centersInt;
	}

	public int[] getCountsLeft(){
		return this.countsLeft;		
	}
	
	public int[] getCountsRight(){
		return this.countsRight;
	}
	
	public double[] getBinsLeft(){
		return this.binCentersLeft;
	}
	
	public double[] getBinsRight(){
		return this.binCentersRight;
	}
	
	public int[] getCounts(){
		return this.counts;
	}
	
	public double[] getCenters(){
		return this.centers;
	}
}
