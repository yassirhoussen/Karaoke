package moteurSound;

import math.DFT;
import math.FFT;

public class AudioAnalyser {
	

	private AudioBuffer audioBuffer;
	private int windowSize;
	private double[] windowedFrame;
	private double[] magnitude;
	
	/**
	 * constructeur, permet d'initialiser windowSize,la taille de la fenetre d'anticipation,
	 * a 2014 ainsi que l'audiobuffer avec les parametres (windowsSize et le construcuteur lui meme(this)
	 */
	public AudioAnalyser() {
		windowSize = 2048;	
		audioBuffer = new AudioBuffer(windowSize, this);
	}
	/**
	 * peremt de recupere les resultats des calculs (DFT et FFT) dans deux tableau de double
	 * - le premier tableau, windowedFrame, correspond au calcul de la DFT par la méthode de Blackman
	 * - le deuxiéme tableau, magnitude, permet à partir du premier de trouver le module des elements du
	 *  1er tableau qui ont été transformé en nombre complexe
	 */
	protected void bufferReady() {
		windowedFrame = DFT.window( audioBuffer.getFrame() );
		magnitude = FFT.magnitudeSpectrum(windowedFrame);	
	}
	/**
	 * 
	 * @param newSamples tableau de int 
	 * permet de renvoyer le meme tableau a l'audiobuffer
	 */
	public void addSamples(int[] newSamples) {
		audioBuffer.addSamples(newSamples);
	}
	 /**
	  * 
	  * @return getter qui permet de recuperer 
	  * le tableau de double utiliser par l'audiobuffer
	  */
	public double[] getCurrentFrame() {
		return audioBuffer.getFrame();
	}
		
}
