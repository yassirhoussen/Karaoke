package Karaoke.src;

import java.io.*;

public class Charger {
	private String fichier;
	StringBuffer chaine = new StringBuffer();
	private boolean reussir = false;
	public Charger(String fichier){
		this.fichier="Paroles/"+fichier;
		chargement();
	}
	//Chargement du fichier texte dans la m�moire
	public void chargement(){	
			try{
				File read=new File(fichier); 
				FileInputStream in=new FileInputStream(read);
				InputStreamReader br=new InputStreamReader(in);
				BufferedReader re = new BufferedReader(br);
				String ligne;
				while ((ligne=re.readLine())!=null){
					chaine.append(ligne);
					chaine.append("\n");				
				}
				re.close(); 
				reussir = true;
			}		
			catch (Exception e){
				System.out.println("Vous avez rencontr� un probl�me de chargement de texte,v�rifiez votre chemin de fichier.\n");
			}	    
	}
	// @return: un string contenant l'information charg�e
	public String text(){
		return chaine.toString();
	}
	// @return: "true" si le chargement s'est bien d�roul� 
	public boolean reussirChargement(){
		return reussir;
	}
}

