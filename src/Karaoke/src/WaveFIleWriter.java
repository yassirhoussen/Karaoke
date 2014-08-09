package Karaoke.src;

/**
 * cette classe permet la creation d'un fichier wav a partir d'un fichier d'un audioInputStream qui est
 * construit avec les paramétres(ByteArrayInputStream, AudioFormat,longueur tableau de byte);
 * l'AudioFormat doit etre récuperer directement du fichier source;
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class WaveFIleWriter extends Thread{
	static String finalfilename;
	Sound sd;
	static File file;

	/**
	 * initialise la variable de classe file par cette du fichier source
	 * @param file fichier source
	 */
	public void setFile(File file){
		this.file=file;
		
	}
	/**
	 * on recupere le nom du fichier source, on lui enleve l'extension et on lui ajoute _novoice.wav
	 * @param name nom du fichier source
	 */
	public static void setNamefile(String name){
		finalfilename=("No-Voice\\"+name.substring(0, name.length()-4)+"_novoice.wav");
	}
	/**
	 * initialisation de la variable sound
	 * @param sd de type Sound 
	 */
	public void setSound(Sound sd){
		this.sd=sd;
	}
	
	static AudioFormat codecFormat;
	static int length;
	
	/**
	 * méthode run permet de creer un fichier destinataire.
	 * on creer un AIS a partir du stream du voiceremovre, ainsi que l'audioforat du fichier source
	 * et la longeur du tableau de sample obtenu par sound divisé par 4 pour obtenir la bonne longeur de chanson
	 * on recupere AudioFileFormat.Type du fichier source(codage audio u fichier)
	 * si l'audiosyteme support l'AIS avec son format, ainsi que AudioFileFormat.Type obtenus, on ecrit dans le 
	 * fichier destinataire le stream
	 */
	public void run(){
		
	File fileOut= new File(finalfilename);
	try {
					AudioInputStream ais = new AudioInputStream( VoiceRemover.getByteArrayInputStream(),codecFormat,length/4);
		  			AudioFileFormat.Type fileType = AudioSystem.getAudioFileFormat(file).getType();
		  			if (AudioSystem.isFileTypeSupported(fileType,ais)) {
		  						System.out.println("Trying to write");
		  						AudioSystem.write(ais, fileType, fileOut);
		  						System.out.println("Written successfully");
		  			}
      }catch(Exception e){
    	  			e.printStackTrace();
      }	
	}

	/**
	 * constructeur
	 * @param file1 fichier source 
	 * on execute le voice remover pour obtenir le stream a ecrire dans le nouveau fichier destinataire
	 * ainsi que l'audioformat du fichier source et la longueur du tableau de byte contenant les données
	 * provenant du fichier source
	 * 
	 */
public WaveFIleWriter(File file1) {
		
		Sound sd =new Sound(file1);
		byte[] signal = sd.getSamples();
		System.out.println(sd.format.toString() + " "
				+ sd.getSamples().length);
		try {
				setNamefile(file1.getName());
				setFile(file1);
				
				codecFormat = sd.getFormat1();
				VoiceRemover.noVoice(signal);
				length=sd.getLength();	
				this.start();		
	      	}catch(Exception e){
	    	  	e.printStackTrace();
	      }				
	}		

}
