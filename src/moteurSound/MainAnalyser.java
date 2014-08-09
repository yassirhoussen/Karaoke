/*
 * Main.java
 * les classes ont �t� recuperer du projet frequazoid sur le net
 * elles ont �t� modifi� pour convenir au projet karaok�
 */

package moteurSound;

import java.io.File;
import interfaces.ResourceManager;

public class MainAnalyser extends Thread {
			
	private static String Filename;
	ResourceManager rm ;
	static File file;
	
	public MainAnalyser(){}
	
	public static File setFile(File fich){
		return file=fich;
	}
	
	/**
	 * permet l'arret de tous les threads associ�s
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void Stop() throws Exception{
		rm.getAudioEngine().muteFile = true;
		this.stop();
		rm.dispose();
		rm.getAudioEngine().stop();
		rm=null;
	}
	
	/**
	 * lance le thread qui va permettre l'affichage des amplitudes.
	 */
   public void run() {
	   AudioEngine audioEngine = new AudioEngine();
       rm = new ResourceManager(audioEngine);
       rm.getAudioEngine().muteFile = false;
       rm.getAudioEngine().openFile(file);
       rm.getAudioEngine().reopenFile();
      
   }
}