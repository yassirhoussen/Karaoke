package Karaoke.src;

	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.List;

	import javax.xml.parsers.ParserConfigurationException;
	import javax.xml.parsers.SAXParser;
	import javax.xml.parsers.SAXParserFactory;

	import org.xml.sax.Attributes;
	import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

	public class SAXParserExample extends DefaultHandler{

		private List<Vers> myVers = new ArrayList();
		private String tempVal;
		int i=1;
		String[][] tab= new String [100][3];
		//to maintain context
		private Vers tempEmp;
		private File fichier;
		
		public SAXParserExample(File fic){
			this.fichier=fic;
			runParser(fic);
		}
		
		public void runParser(File fichier) {
			parseDocument(fichier);
		}

		private void parseDocument(File fichier) {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
				//get a new instance of parser
				SAXParser sp = spf.newSAXParser();
				//parse the file and also register this class for call backs
				sp.parse(fichier, this);
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch (IOException ie) {
				ie.printStackTrace();
			}
		}

		public  String[][] retour_vers(int i){
			String[][] tab_vers= new String[1][3];
			tab_vers[0][0]=tab[i][0];
			tab_vers[0][1]=tab[i][1];
			tab_vers[0][2]=tab[i][2];
			return (tab_vers);
		}

		public int nbrVers(){
			int i = myVers.size();
			return (i);
		}
		

		//Event Handlers
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			//reset
			tempVal = "";
			if(qName.equalsIgnoreCase("vers")) {
				//create a new instance of vers
				tempEmp = new Vers();
				tempEmp.setTexte(attributes.getValue("type"));
			}
		}
		

		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}
		
		
		public void endElement(String uri, String localName, String qName) throws SAXException {

			if(qName.equalsIgnoreCase("vers")) {
				//add it to the list
				myVers.add(tempEmp);
				i++;
			}else if (qName.equalsIgnoreCase("texte")) {
				tempEmp.setTexte(tempVal);
				tab[i-1][0]=tempVal;
			}else if (qName.equalsIgnoreCase("tps_debut")) {
				tempEmp.setTpsDebut(tempVal);
				tab[i-1][1]=tempVal;
			}else if (qName.equalsIgnoreCase("tps_fin")) {
				tempEmp.setTpsFin(tempVal);
				tab[i-1][2]=tempVal;
			}
		}
	}
		