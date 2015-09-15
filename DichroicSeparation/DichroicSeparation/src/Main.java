import java.io.IOException;

public class Main {
	
	private Main() {
	}
	
	public static void main(String[] args){
		try {
			FileLoading fl1 = new FileLoading();
			fl1.loadDichroicMirror();
			fl1.loadFilters();
			ActualSeparation al1 = new ActualSeparation(fl1);
//			al1.distributePhotons("\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\Testlauf\\",
//									"_testlauf150903.txt");
			al1.distributePhotons("\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\2015-09-11Alexa647\\",
												"_2015-09-11_waveDistrosinBlinkingEvents_150908PaperAlexa647HistoDataEstimation.txt");
		} catch(IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
}
