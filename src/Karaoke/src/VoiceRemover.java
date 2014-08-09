package Karaoke.src;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
 	Cette classe permet de supprimer la voix d'un fichier audio 
*/
public class VoiceRemover {
	
	/* 	Séparation de la partie gauche du signal (tableau de byte passé en paramètre)
		@param b le signal 
		@return resultat la partie gauche du signal (en byte)
	*/
	static byte[] vG(byte[] b) {
		byte[] resultat = new byte[b.length / 2];
		for (int i = 0; i < b.length; i += 4) {
			resultat[i / 2] = b[i];
			resultat[i / 2 + 1] = b[i + 1];
		}
		return resultat;
	}
	
	/* 	Séparation de la partie droite du signal (tableau de byte passé en paramètre)
		@param b le signal 
		@return resultat la partie droite du signal (en byte)
	*/
	static byte[] vD(byte[] b) {
		byte[] resultat = new byte[b.length / 2];
		for (int i = 0; i < b.length; i += 4) {
			resultat[i / 2] = b[i + 2];
			resultat[i / 2 + 1] = b[i + 3];
		}
		return resultat;
	}
	
	/* Récupération de la voix gauche (signal gauche) convertie en entier
		@param b le signal gauche
		@return le signal gauche converti en int
	*/
		public static int[] voixGauche(byte[] b) {
		return toInt(vG(b));
	}

	/* Récupération de la voix droite (signal droit) convertie en entier
		@param b le signal droit
		@return le signal droit converti en int
	*/
		public static int[] voixDroite(byte[] b) {
		return toInt(vD(b));
	}
	
	 /*	Mixage des deux signaux (partie gauche et partie droite) en un seul pour obtenir le signal complet.
		@param gauche : partie gauche du signal, droit : signal droit
		@return bb un byte buffer qui va contenir le signal complet
	*/
	public static byte[] mix(int[] gauche, int[] droit) {
		ByteBuffer bb = ByteBuffer.allocate(gauche.length * 4);
		bb = bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < gauche.length; i++) {
			bb.putShort((short) gauche[i]);
			bb.putShort((short) droit[i]);
		}
		return bb.array();
	}
	
	/* 	Conversion de byte en entier
		@param b le signal à convertir
		@return table contient le signal convrti
	*/
	public static int[] toInt(byte[] b) {
		ByteBuffer bf = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
		int[] table = new int[b.length / 2];
		for (int i = 0; i < b.length / 2; i++) {
			table[i] = bf.getShort();
		}
		return table;
	}

	static int[] voixGauche=null;
	static int[] voixDroite=null;
	static ByteArrayInputStream stream;
	
	
	/* 	Récupération d'un InputStream
	*/
	public static InputStream getInputStream(){
		return (InputStream)stream;
	}
	
	/* 	Récupération d'un ByteArrayInputStream
	 */
	public static ByteArrayInputStream getByteArrayInputStream(){
		return stream;
	}
	
	/* 	Méthode permettant de suprimer la partie milieu d'une piste audio
		@param signal : le signal à supprimer la voix 
	*/
	public static void noVoice( byte[]signal) throws Exception {
		
		voixGauche= voixGauche(signal);
		voixDroite = voixDroite(signal);
		
		//inversion du signal gauche
		int [] inversegauche= new int[voixGauche.length];
		for(int i=0;i<voixGauche.length;i++){
			inversegauche[i]=1-voixGauche[i];
			}
		
		//sortie final gauche sans parole
		int[]signalfiniGauche=new int [voixGauche.length];
		for(int i=0;i<voixGauche.length;i++){
			signalfiniGauche[i]=voixDroite[i]+inversegauche[i];
			}
		
		//inverse du signal droit
		int [] inversedroite= new int[voixGauche.length];
		for(int i=0;i<voixGauche.length;i++){
			inversedroite[i]=1-voixGauche[i];
			}
		
		//sortie final droit sans parole
		int[]signalfiniDroite=new int [voixGauche.length];
		for(int i=0;i<voixGauche.length;i++){
			signalfiniDroite[i]=voixGauche[i]+inversedroite[i];
			}
		
			//mix permet de jouer le morceau en un seul
		stream = new ByteArrayInputStream(mix(signalfiniDroite,signalfiniGauche));
		
	}
	
	
}


