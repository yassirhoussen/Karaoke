package Karaoke.src;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class Sound extends Thread{
	
	
	private static int duree= 0;
	static AudioFormat format;
	private byte[] samples;
	private static InputStream stream;
	
	private static int bufferSize;
	private byte[] buffer;

	/**
	 * initialise un inputstream a partir d'un autre inputstream deja construit
	 * @param stream deja existant
	 */
	public void setStream(InputStream stream){
		this.stream = stream;
	}
	/**
	 * recupere l'audioformat d'un fichier
	 * @return format de type audioformat
	 */
	public AudioFormat getFormat1(){
		return format;
	}
	/**
	 * 
	 * @return longueur du tableau de byte contenant les datas
	 */
	public int getLength(){
		return samples.length;
	}
	/**
	 * 
	 * @param file fichier source 
	 * on recupere un AIS a partir du fichier source, de cette ais on en recupere l'audioformat
	 * et un tableau de byte, samples, contenant les donn�es du fichier sources 
	 * a partir de ce tableau de byte on creer un bytearrayinputstream, ce nouveau stream va
	 * permettre d'initialiser l'inputstream qui est la varialbe de classe
	 */
	public Sound(File file) {
		duree=0;
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
			samples = getSamples(stream); 
		} catch (Exception e) {
			e.getStackTrace();
		}
		setStream(new ByteArrayInputStream(getSamples()));
		//connait pas 
		bufferSize = format.getFrameSize() * Math.round(format.getSampleRate() / 10);
		buffer = new byte[bufferSize];
		duree=calcultemps(buffer);
	}
	/**
	 * retourne le tableau de byte de donn�e 
	 * @return samples tableau de byte 
	 */
	public byte[] getSamples() {
		return samples;
	}
	/**
	 * recupere un tableau de byte provenant d'audioinputstream
	 * @param stream ais
	 * @return sample tableau de byte 
	 */
	public byte[] getSamples(AudioInputStream stream) {
		int length = (int) (stream.getFrameLength() * format.getFrameSize());
		byte[] samples = new byte[length];
		DataInputStream in = new DataInputStream(stream);
		try {
			in.readFully(samples);
		} catch (Exception e) {}
		return samples;
	}
	
	/**
	 * permet le calcul de la dur�e d'une chanson a partir d'un tableau de byte utiliser comme buffer
	 * @param buffer tableau de byte utiliser comme buffer 
	 * @return la dur�e de la piste audio
	 */
	public int calcultemps(byte[]buffer){
		return((int)Math.abs((samples.length  * format.getFrameRate())/8700000));// calcul arbitraire de la dur�e de la piste audio
	}
	

	private DataLine.Info info=null;
	
	/**
	 * 
	 * @return info de type DataLine.Info
	 */
	public DataLine.Info getDataLine(){
		return info;
	}
	
	static SourceDataLine line;
	//initialise le volume � 50%.
	static int value=50;
	
	/**
	 * Modification du gain (volume) d'un fichier son
	 * @param value la nouvelle valeur du gain
	 */
	public static void setVolume(int value){
		value=value;
	}
	
	/**
	 * retourn la valeur actuelle du volume
	 * @return value  valeur du volume
	 */
	public int getGain (){
		return value;
	}
	
	/**
	 * modificateur du volume de sortie du son en prenant en parametre une valeur
	 * on modifie le controleur de volume par la valeur ajuster en decibel
	 * @param value int nouvelle valeur du volume 
	 * @throws Exception
	 */
	public static void setGain(int value) throws Exception {
		 
        if (line != null){
        	double val = value / 100.0;
            FloatControl gainControlClip = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float)(Math.log(val==0.0?0.0001:val)/Math.log(10.0)*20.0);//on ajuste le volume en DB
            gainControlClip.setValue(dB);
        }
    }
	
	public void run() {
		 boolean fin = false;
	
	        while( !fin ) {
	                try {

						info = new DataLine.Info(SourceDataLine.class, format);
						line = (SourceDataLine) AudioSystem.getLine(info);
						line.open(format, bufferSize);
					} catch (LineUnavailableException e) {
						e.printStackTrace();
						return;
					}
					
					line.start();
					try {
						Sound.setGain(getGain());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					int numBytesRead = 0;
					while (numBytesRead != -1) {
						try {
							numBytesRead = stream.read(buffer, 0, buffer.length);
						} catch (IOException e) {

							e.printStackTrace();
						}
						if (numBytesRead != -1)
							line.write(buffer, 0, numBytesRead);
						
					} 
					line.drain();
					line.close();
					line = null;
					synchronized(this) {
					    Thread.yield();
					
}  fin = this.stopThread; 
    }
	}
	
	/**
	 * 
	 * @param min nombre de minutes
	 * @param sec nombre de secondes 
	 * @return time une string contenant la chaine "minutes : secondes"
	 */
	public String toString (int min, int sec){
		String time=min+":"+sec;
		return (time);
	}
	/**
	 * retourne la dur�e
	 * @return duree 
	 */
	public static int getDuree(){
		return(duree);
	}
	
	private boolean stopThread = false;

	/**
	 * methode d'arret du player
	 */
	public synchronized void stopper() {
	        this.stopThread = true;
	} 
}