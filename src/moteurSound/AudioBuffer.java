package moteurSound;

public class AudioBuffer {
	
	private AudioAnalyser analyser;
	private int frameSize;
	private int hopSize;
	private int[] buffer;
	private double[] frame;
	private int headWrite, headRead;
	
	/**
	  * constructeur 
	  * initialisation des variables de classes
	  * @param frameSize correspond a la taille de la fenetre d'anticipation
	  * @param analyser de type AudioAnalyser
	  */
	
	public AudioBuffer(int frameSize,  AudioAnalyser analyser) {
		this.analyser = analyser;
		headWrite = 0;
		headRead = 0;
		this.frameSize = frameSize;
		this.hopSize = frameSize/2;
		buffer = new int[frameSize];
		frame = new double[frameSize];
	}
	
	/**
	 * 
	 * @param newData
	 * a partir d'un tableau de int de donnée(newData),on va remplir un tableau de double 
	 * qui contiendra l'amplitude d'un signal
	 */
	public void addSamples(int[] newData) {
		int N = newData.length;
		
		for(int i=0; i<N; i++) {
			buffer[headWrite] = newData[i];
			headWrite++;
			if(headWrite==buffer.length) headWrite=0;
			
			// if the number of newly incoming samples equals to hopsize
			if(headWrite==headRead) {
//				System.out.println("esit");
				for(int n=0; n<frameSize; n++) {
					frame[n] = ((double)buffer[(headRead+n)%frameSize])/32768;		
				}
				headRead += hopSize;
				if(headRead==buffer.length) headRead=0;
				
				analyser.bufferReady();
			}
		}
	}
	
	/**
	 * 
	 * @return frame le tableau contenant les valeurs des amplitudes.
	 */
	public double[] getFrame() {
		return frame;
	}
}
