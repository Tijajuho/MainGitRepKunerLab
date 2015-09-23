import java.io.IOException;

public class Main {
	
	private Main() {
	}
	
	public static void main(String[] args){
		try {
			LoadedFileStorage lfs1 = new LoadedFileStorage();
//			FileLoadingOptimized flo1 = new FileLoadingOptimized("_testlauf150903.txt","\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\Testlauf\\",lfs1);
			FileLoadingOptimized flo1 = new FileLoadingOptimized("_2015-09-11_waveDistrosinBlinkingEvents_150908PaperAlexa647HistoDataEstimation.txt",
																	"\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\2015-09-11Alexa647\\",lfs1);
			flo1.loadDichroicMirror();
			flo1.loadFilters();
			ActualSeparation al1 = new ActualSeparation(flo1,lfs1);
			al1.distributePhotons();

		} catch(IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		} catch(InterruptedException e2){
			System.err.println("Caught InterruptedException: " + e2.getMessage());
		}
	}
}
