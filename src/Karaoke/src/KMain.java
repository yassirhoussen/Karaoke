package Karaoke.src;


import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

// la classe main qui lance l'application "Karaoke"

public class KMain {
	public static void main (String args[]){ 
		File f= new File("no.txt");
		if(!f.exists()){
		Component frame = null;
		Icon icon =new ImageIcon("Images/ok.jpg");
		//custom title, custom icon
		JOptionPane.showMessageDialog(frame,
		    "Pour une meilleure utilisation de l'application:\n" +
		    "-Mettre les fichiers audios dans la racine du programme.\n\n\n" +
		    "Pour les fichier .txt contenant les paroles de chanson,il faut que:\n\n" 
		    + "- les sauts de lignes soient supprimes.\n\n" 
		    +"- au lieu d'ecrire 2, par exemple, a la fin d'un refrain\n" 
		    +" celui-ci doit etre ecrit une deuxieme fois en dessous\n\n" 
		    +"- apres le dernier vers, a la ligne, soit ajoutee la chaine de caractere ----",
		        "Conseils",
		    JOptionPane.INFORMATION_MESSAGE,
		    icon);
	
		//default icon, custom title
		int n = JOptionPane.showConfirmDialog(
		    frame,
		    "Souhaitez-vous garder l'affichage des conseils au lancement du programme?",
		    "Affichage des conseils",
		    JOptionPane.YES_NO_OPTION);
		if (n==1){
			try 
			{ 

			FileWriter fw = new FileWriter("no.txt",true); 
			fw.close();
			} 
			catch(IOException ioe){ 
			System.out.println("erreur : " + ioe ); 
			} 

		}
		}



		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new MetalLookAndFeel());
					Karaoke fen = new Karaoke() ;
					fen.setVisible(true) ;
				} catch (UnsupportedLookAndFeelException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}
