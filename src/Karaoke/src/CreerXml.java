package Karaoke.src;

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.bind.Element;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;

public class CreerXml {
	private String texte, nom;
	@SuppressWarnings("unused")
	private File file;

	public CreerXml(String texte, String nom) throws IOException{
		this.texte=texte;
		this.nom=nom;
	}

	/*Cr�ation du fichier xml et �criture des balises, des paroles et des temps de synchronisation*/
	public void fichierXml() throws IOException{
		// on creer un fichier txt avant de changer l'extension
		BufferedWriter fichier = new BufferedWriter(new FileWriter(nom+".txt"));
		// on �crit dans le fichier texte
		fichier.write(texte);
		// on referme les fichiers
		fichier.close();
		File f=new File(nom+".txt");
		File f1=new File(nom+".xml");
			f1.delete();
				f.renameTo(new File(nom+".xml"));

		   
		}

	}


