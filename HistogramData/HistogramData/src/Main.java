import java.io.IOException;


public class Main {
	
	private Main(){
		
	}
	
	public static void main(String[] args){
		String filename=null,pathname = null,tagLeft = null,tagRight=null;
		Results results = null;
		Boolean angleProcessing = null;
		try{
			//filename = "\\\\129.206.158.175\\FN-Praktikant\\Timm\\LeftChannel141118MicrotubuliCF680MitochondriaA647Cos2DMessung1_demixingStatistic.txt";
			pathname = "\\\\129.206.158.175\\FN-Praktikant\\Timm\\CF680\\";
			filename = "150818PhaloidinAlexa647MitochondriaCF680Messung2pt2.txt";
			tagLeft = "LeftChannel";
			tagRight = "RightChannel";
			///////// load txt and extract intensities////////
			//FileLoading.loadFile(pathname+filename);
			FileLoading.loadFiles((pathname+tagLeft+filename), (pathname+tagRight+filename));
			/* System.out.println(intensities[1][0]); */
			angleProcessing = false;
			results = BinsOfHistogram.createHistogramData(angleProcessing);
			FileOutput.saveFile(results,pathname,filename,angleProcessing);
			
			angleProcessing = true;
			results = BinsOfHistogram.createHistogramData(true);
			FileOutput.saveFile(results,pathname,filename,angleProcessing);

		} catch(IOException e) {
			 System.err.println("Caught IOException: " + e.getMessage());
			 

		} finally {
		}
	}
}
