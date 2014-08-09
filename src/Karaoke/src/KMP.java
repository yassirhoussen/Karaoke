package Karaoke.src;

import java.util.Vector;

// l'algorithme Knuth-Morris-Pratt

//Algorithme permettant de faire une recherche de motif dans un texte.

public class KMP{
	private String texte;
	private String motif;
	private int[] pi;
	private int numligne = 1;
	private int position = 0;
	private int pn = -1;
	private boolean trouve = false;
	private int nb;
	private Vector t_motif = new Vector();
	
	//recherche, comparaison et décalage
	public KMP(String text,String mo) {
		texte = text;
		motif = mo;
		int m= motif.length();
		int n= texte.length();
		Pi tab = new Pi(motif);
		pi = tab.pi();
		int i=0,j;
		while (i<n-m+1) {
			j = 0;
			if (texte.charAt(i)=='\n') {        
				pn = i;
				numligne+=1;
			}
			while (j<m && motif.charAt(j)==texte.charAt(i+j)) j++;
			if (j==m) {
				position = i - pn;
			   	nb++;
				trouve = true;
			   	t_motif.add(new int[2]);
			    ((int[])t_motif.get(t_motif.size()-1))[0] = position;
			    ((int[])t_motif.get(t_motif.size()-1))[1] = numligne;
				}
			if (j==0 || pi[j-1]==0) i= i+j+1;
			else {
				i = i+j-pi[j-1];
				j=pi[j-1];
			}
		}
		
	}
	
	//retourne le numéro de la ligne
	public int numLigne(){
		return numligne;
	}
	
	//retourne la position 
	public int  position(){
		return position;
	}
	
	//retourne la liste 
    public Vector getT_motif(){
    	return t_motif;
    }
  //retourne un booléen selon le résultat du test
    public boolean test(){
    	return(trouve);
    }
    
}


//Analyse sur le motif pour connaitre les décalages à effectuer

 class Pi{
	int[] tab;
	public Pi(String motif){
		int n=motif.length();
		tab = new int[n];
		for (int i=1;i<n;i++) {
			while (motif.charAt(i)==motif.charAt(0)&&tab[i]==0) {
			tab[i] = 1;
			for (int j=i+1;j<n;j++) {
				if (motif.charAt(j)==motif.charAt(j-i)) tab[j] = tab[j-1]+1;
				}
			}
		}
	}
	public int[] pi(){
		System.out.println(tab);
		return tab;
	}
}