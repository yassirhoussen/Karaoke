package Karaoke.src;

import java.io.File;

import javax.swing.JOptionPane;

public class Rechercher {
	private String reponse;
	private String resultat;
	private String liste;
	private int r;
	private String[] t_res=new String[50];

	public Rechercher(){
		this.reponse= new String("");
		this.liste=new String("");
		this.r=0;
	}


//demande le motif à rechercher
	public String recherche(){
		String message = "votre recherche:";
		reponse = JOptionPane.showInputDialog( message,"tapez un mot ou une phrase");	

		
		return reponse;
	}
	
	//retourne le résultat sous forme de String
	 public String resultat(){
		 try {		
				Thread.sleep(1000);
				dossier();
							
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	    	return(resultat);
	 }
	    	
	 //recherche le motif dans les fichiers contenus dans le dossier "Paroles"
	public void dossier(){
		File folder = new File("Paroles/");
		File[] listOfFiles = folder.listFiles();

		int j=0;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String n= listOfFiles[i].getName();
				
				Charger c= new Charger(n);
				String texte=c.text();
				KMP rech=new KMP(texte,reponse);
				if (rech.test ()==true) {
					r++;
					//enlève l'extension du fichier
					n=n.substring(0, n.length()-4);
					liste=liste+"\n"+n;
					t_res[j]=n;
					j++;
				}
				if (r!=0){resultat="se trouve dans le(s) fichier(s): \n"+liste ;
				}
				else{
					resultat="Aucun fichier ne contient la recherche demandée.";
				}
				
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}

		}
	
		}
	
	//retourne la liste des fichiers trouvés dans lesquelles il y a une occurence du motif
	
	public String[] res(){
			return (t_res);
	}
	
	
	
}

