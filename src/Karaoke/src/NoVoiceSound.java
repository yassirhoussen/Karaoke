package Karaoke.src;


import java.io.*;

public class NoVoiceSound {
	public  byte[]signal;
	public static Sound testSound;
	
	/* 	Constructeur
	 	@param file : fichier audio sous format (WAV)
	 */
	public NoVoiceSound (File file) throws Exception{
		traitement (file);		
	}
	
	/* Récupérer le signal
	 * @return signal : signal à récupérer
	 */
	public byte[] getSignal(){
		return signal;
	}
	
	/* Méthode permettant d'appliquer la fonction Voice Remover sur le stream
	 *  
	 * @param file : fichier audio sous format (WAV)
	 */
	public void traitement(File file) throws Exception{
		testSound= new Sound(file);
		signal=testSound.getSamples();
		System.out.println(testSound.format.toString() + " "
				+ testSound.getSamples().length);
		//on enleve la voix
		VoiceRemover.noVoice(signal);
		InputStream stream=VoiceRemover.getInputStream();
		VoiceRemover.noVoice(signal);
		testSound.setStream(stream);
		testSound.start();
	}
	
	/* 	Récupérer le son
	 *  @return testSound : le son
	 */
	public static Sound getTestSound(){
		return testSound;
	}
	
	/* Permet d'arrêter le son
	 * 
	 */
	public static void stopTestSound(){
		testSound=null;
	}
	
	/* Modification du volume de la piste audio 
	 * @param value : valeur du volume souhaité
	 */
	public static void modifVolume(int value) throws Exception{
		testSound.setGain(value);
	}
}