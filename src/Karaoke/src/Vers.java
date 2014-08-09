package Karaoke.src;
public class Vers {
	
	private String texte, tpsDebut, tpsFin;
	
	public Vers(){
	}
	
	public Vers(String texte, String tpsDebut, String tpsFin) {
		this.texte = texte;
		this.tpsDebut = tpsDebut;
		this.tpsFin  = tpsFin;
	}
	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public String getTpsDebut() {
		return tpsDebut;
	}

	public void setTpsDebut(String tpsDebut) {
		this.tpsDebut = tpsDebut;
	}

	public String getTpsFin() {
		return tpsFin;
	}

	public void setTpsFin(String tpsFin) {
		this.tpsFin = tpsFin;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Détails du vers - ");
		sb.append("Texte:" + getTexte());
		sb.append(", ");
		sb.append("Temps début:" + getTpsDebut());
		sb.append(", ");
		sb.append("Temps fin:" + getTpsFin());
		sb.append(", ");
		
		return sb.toString();
	}
}