package moteurSound;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioEngine implements Runnable {
	
    private static final int SAMPLE_RATE = 44100;
    private static final int BIT_DEPTH = 16;
    private static final int N_CHANNELS = 1;
    private static final boolean BIG_ENDIAN = false;
    private AudioFormat format;
    
    private int bufferSize = 2048;    
    private TargetDataLine inputLine;
    private SourceDataLine outputLine;  

    private int blockSize = 512;
        
    private AudioAnalyser audioAnalyser;
    
    private AudioInputStream inputFileStream;
    private AudioFileFormat inputFileFormat;
    private File inputFile;
      
    private Thread audioThread;
    
    @SuppressWarnings("unused")
	private Mixer.Info[] mixerInfo;
    private DataLine.Info sourceInfo;
    private DataLine.Info targetInfo;
    
   public boolean muteFile;
   public boolean muteSpeaker;
   
   /**
    * constructeur de la classe
    * ce constructeur permet entre autre de verfier si � l'extension du fichier concern�,
    * il existe un codecs qui lui est associ�.si tel est le cas,on ouvre un objet de type Line 
    * avec comme parametre l'audioFormat, ainsi qu'un tableaud e byte qui va servir de buffer. 
    */
   
    public AudioEngine() {
    	
    	 muteFile = false;
    	 muteSpeaker=false;
         int frameSizeInBytes = BIT_DEPTH/8;
         int frameRate = SAMPLE_RATE;
         format = new  AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                 SAMPLE_RATE, BIT_DEPTH, N_CHANNELS, frameSizeInBytes, frameRate, BIG_ENDIAN);
         
         System.out.println("audio format: "+format.toString());
             
        mixerInfo = AudioSystem.getMixerInfo(); 
        
//        on recupere les infos sur les codecs, pour verifier si il supporte un format donn�e de codage audio
        targetInfo = new DataLine.Info(TargetDataLine.class, format);        
        sourceInfo = new DataLine.Info(SourceDataLine.class, format);
        
        if(AudioSystem.isLineSupported(targetInfo)) {
            try {
                inputLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
                System.out.println(inputLine.getLineInfo().toString());
                inputLine.open(format, bufferSize);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }            
        }
        
        if(AudioSystem.isLineSupported(sourceInfo)) {
            try {
                outputLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
                outputLine.open(format, bufferSize);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }            
        }
   
        audioAnalyser = new AudioAnalyser();
        audioThread = new Thread(this);
        audioThread.start();
    }
    
    /**
     * la m�thode run permet de lancer le thread associ� a cette classe.
     * A partir du stream recuperer par le fichier on lit le stream et on remplit le buffer au fur et a mesure de la lecture.
     * on renvoie a l'audioanalyser un tableau de int de taille 1024(datafromfile)( chaque element sont la somme des deux elements succesifs du buffer), qui va 
     * permetre d'effectuer les calculs et l'affichage.
     * de plus un autre tableau de byte de taille 1024(datamasterout) dont le contenus et la conversion en byte de chaque donn�e de datafromfile en byte.
     * ce dermier tableau est ensuite envoy� au mixer, pour que le son sorte au niveau des speakers.
     * dans notre cas, aucun son ne sortira car les valeurs du tableau, convertis en son ne seront pas audible.
     */
    public void run()  {
        byte[] dataFromFile = new byte[blockSize*2];
        byte[] dataMasterOut = new byte[blockSize*2];
        
        inputLine.start();
        outputLine.start();   
        while(true) {
            		try {
						inputLine.open(format, bufferSize);
						outputLine.open(format, bufferSize);
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
            		inputLine.start();
                    outputLine.start();
                if( inputLine.available() > blockSize*2 )
                {           	
                	if ( !muteFile && inputFile != null ) {
                		try {
							inputFileStream.read(dataFromFile);
                		} catch (IOException e) {
							e.printStackTrace();
						}
                	}              
                	
                	int[] masterOut = new int[blockSize];                	
                	
//                	conversion d'un tableaud e byte en int
                	for(int i=0, j = 0; j<blockSize; i+=2, j++) {
                		masterOut[j] = 0;
                		if( !muteFile ) {
                			masterOut[j] += (dataFromFile[i] & 0xFF) | (dataFromFile[i+1]<<8);
                		}
                	}                	
               
					audioAnalyser.addSamples(masterOut);    				
//					conversion d'un tableau de int en byte.
                	for(int i=0, j=0; j<blockSize; i+=2, j++) {
                		if( muteSpeaker ) {
                			dataMasterOut[i]   = (byte) ((masterOut[j] &  0xFF) & (masterOut[j] << 8));
                		}
                	} 
//                	on envoie au  mixer les donn�es pour qu'il soit envoy� au speakers
               	outputLine.write(dataMasterOut, 0, blockSize);
                	
                } 
        }
    }
    
    /**
     *  permets d'ouvrir un fichier son, d'en extraire un audioinputstream
     *  ainsi qu'un audiofileformat.
     *  l'audioinputstream va permettre de recuperer les donn�es du fichier
     * @param file fichier sources
     */
    public void openFile(File file) {
    	inputFile = file;
        try { 	
			inputFileStream = AudioSystem.getAudioInputStream(file);
			inputFileFormat = AudioSystem.getAudioFileFormat(file);
		} catch (UnsupportedAudioFileException e) {
			System.out.println("unsupported file format");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(inputFileFormat.toString());
    }
    /**
     * peremt la reouverture d'un fichier 
     */
    public void reopenFile() {
    	if(inputFile !=  null ) {
    		openFile(inputFile);
    	}
    }
    /**
     * methode ascenseur, renvoi l'AudioAnalyser
     * @return audioAnalyser
     */
	public AudioAnalyser getAudioAnalyser() {
		return audioAnalyser;
	}
	
	/**
	 * m�thode d'arret brutale du thread et initialisation des variables de classes a null
	 * @throws Exception
	 */
	public void stop() throws Exception{
		inputLine.close();
		inputLine.drain();
		outputLine.close();
		outputLine.drain();
		inputFileStream.close();
		inputFileStream=null;
		audioThread.stop();
		inputLine=null;
		outputLine=null;
		format=null;
	}
}