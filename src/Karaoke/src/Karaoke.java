package Karaoke.src;


import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.swing.* ;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import moteurSound.MainAnalyser;

class Karaoke extends JFrame implements ActionListener, ChangeListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118447034718127335L;
	private JMenuBar barreMenus ;
	private JMenu fichier, edition, aide;
	private JMenuItem nouveau, ouvrir, quitter, rechercher, help, credits, conseils;
	private JLabel arrierePlan, lb_voice, lb_noVoice, lb_volume, temps_ecoule;
	private JTextArea console;
	private JScrollPane defile;
	private JTextPane vers, suivant;
	private Icon icon;
	private JButton btnPlay, btnStop, btnPlay_noVoice, btnStop_noVoice, btnSynchro, btn_Valider, btnTxt, btn_exporter_novoice, btn_bg1, btn_bg2, btn_bg3, btn_bg4;
	private Color couleurmenu = new Color(222, 169, 65);
	private Font police =new Font("tahoma",Font.TYPE1_FONT,12);
	private File file_voice = null, file_txt;
	private static Sound player;
	public JProgressBar progression, progression_noVoice;
	private Integer duree=0, dureechanson=0, signal_fin=0, nb_vers, j=1, k_progression=0, l_progression=0, k_no_progression=0, l_no_progression=0, num_vers=0;
	final Timer timer1;
	final Timer timer_noVoice;
	private boolean no_voice_active=false, affichage=false, debut=true; 
	private StringBuffer baliser;
	private String nom_chanson;
	private String[] tab_vers;
	public JSlider slideGain;
	private File file_xml;
	private long time0,time,time01,time1, minute=0,secondes=0,milliseconde=000;
	public SAXParserExample spe; 
	public String[][] tab_1vers= new String[1][3], tab_2vers= new String[1][3];
	public MainAnalyser Analyser;

	/** 
	 * Cr�er la fen�tre principale
	 **/
	public Karaoke (){
		//quitter programme avec la croix rouge de la fenetre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle ("Karaoke") ;
		setSize (900, 292) ;
		setResizable(false);

		/* calcul du placement dans la fenetre */
		Toolkit t = this.getToolkit(); 
		Dimension d = t.getScreenSize(); 
		int w = d.width; 
		int h = d.height; 
		this.setLocation(w/2-450, h/2-335);

		/* creation barre des menus */
		barreMenus = new JMenuBar() ;
		setJMenuBar(barreMenus) ;

		/* creation menu fichier */
		fichier = new JMenu ("Fichier") ;
		barreMenus.add(fichier) ;

		//Rubrique nouveau
		nouveau = new JMenuItem ("Nouvelle chanson") ; 
		fichier.add(nouveau) ;

		//Rubrique ouvrir
		ouvrir = new JMenuItem ("Karaoke") ; 
		fichier.add(ouvrir) ;

		//Commande quitter
		quitter = new JMenuItem ("Quitter Karaoke") ; 
		fichier.add(quitter) ;

		/* creation menu edition */
		edition = new JMenu ("Edition") ;
		barreMenus.add(edition) ;

		//Rubrique rechercher
		rechercher = new JMenuItem ("Rechercher une phrase") ; 
		edition.add(rechercher) ;

		/* creation d'un menu aide*/
		aide = new JMenu ("Aide") ;
		barreMenus.add(aide) ;

		//Rubrique manuel utilisateur
		help = new JMenuItem ("Manuel d'utilisation") ;
		aide.add(help) ;

		//Rubrique cr�dits
		credits = new JMenuItem ("Cr�dits");
		aide.add(credits) ;
		//
		conseils = new JMenuItem ("Affichage des conseils au d�marrage");
		aide.add(conseils) ;


		//Racourcis clavier pour le menu
		nouveau		.setMnemonic ('N') ; nouveau	.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N, InputEvent.CTRL_MASK)) ;
		ouvrir		.setMnemonic ('O') ; ouvrir		.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_O, InputEvent.CTRL_MASK)) ;
		quitter		.setMnemonic ('Q') ; quitter	.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_Q, InputEvent.CTRL_MASK)) ;
		rechercher	.setMnemonic ('R') ; rechercher	.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_R, InputEvent.CTRL_MASK)) ;
		help		.setMnemonic ('H') ; help		.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_H, InputEvent.CTRL_MASK)) ;

		JLayeredPane lp = getLayeredPane(); //Permet de superposer des calques
		this.icon=new ImageIcon("Images/fondkaraoke2.png");
		this.arrierePlan=new JLabel (icon);
		lp.add(arrierePlan, new Integer(1)); //Calque d'arri�re plan (1)

		btnPlay_noVoice 		= new JButton("");
		btnStop_noVoice 		= new JButton("");
		btnPlay 				= new JButton("");
		btnStop 				= new JButton("");
		btnTxt 					= new JButton("importer TXT");
		btn_exporter_novoice	= new JButton("Exporter WAV");
		btnSynchro 				= new JButton("");
		btn_bg1					= new JButton("");
		btn_bg2					= new JButton("");
		btn_bg3					= new JButton("");
		btn_bg4					= new JButton("");
		btn_Valider				= new JButton(new ImageIcon("Images/btnValider2.png"));
		lb_voice				= new JLabel(new ImageIcon("Images/btnVoiceOff.png"));
		lb_noVoice				= new JLabel(new ImageIcon("Images/btnVoiceOn.png"));
		lb_volume				= new JLabel(new ImageIcon("Images/volume.png"));
		console					= new JTextArea();
		defile					= new JScrollPane(console);
		vers					= new JTextPane();
		suivant					= new JTextPane();
		progression				= new JProgressBar(JProgressBar.HORIZONTAL);
		progression_noVoice		= new JProgressBar(JProgressBar.HORIZONTAL);
		temps_ecoule			= new JLabel(minute+":"+secondes+":"+milliseconde);
		baliser					= new StringBuffer();
		tab_vers				= new String[100];
		slideGain 				= new JSlider(0, 100, 50);

		slideGain				.setToolTipText("Volume");
		btnPlay_noVoice			.setToolTipText("Lecture");
		btnStop_noVoice			.setToolTipText("Stop");
		btnPlay					.setToolTipText("Lecture");
		btnStop					.setToolTipText("Stop");
		btnTxt					.setToolTipText("Importer le fichier texte contenant les paroles");
		btn_Valider				.setToolTipText("Creer le fichier XML pour sauvegarder le synchronisation");
		btn_exporter_novoice	.setToolTipText("Creer un fichier no-voice");

		progression				.setValue(0);
		progression_noVoice		.setValue(0);

		progression				.setStringPainted(true);
		progression_noVoice		.setStringPainted(true);
		console.setText("A vous de jouer !");

		/* Action r�alis�e par le timer */
		int delais=1; //temps de rafraichissement en milliseconde
		ActionListener tache_timer= new ActionListener(){
			public void actionPerformed(ActionEvent e1)
			{
				if(signal_fin<=dureechanson){
					signal_fin++;

					{
						time = (new Date( ).getTime())-time0;

						if (time >=999){
							time0=(new Date().getTime());
							secondes++;

						}
					}

					milliseconde=time;

					if(secondes==60)
					{
						secondes=0;
						minute++;
					}
					if (!no_voice_active){if(k_progression==(dureechanson/100)){
						l_progression++;
						k_progression=0;
						progression.setValue(l_progression);
					}
					k_progression++;}
					else
					{if(k_no_progression==(dureechanson/100)){
						l_no_progression++;
						k_no_progression=0;
						progression_noVoice.setValue(l_no_progression);
					}
					k_no_progression++;
					}

					temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);/* rafraichir le label */
					if(affichage){
						tab_1vers=spe.retour_vers(num_vers);
						tab_2vers=spe.retour_vers(num_vers+1);
						if(debut){
							if(signal_fin>Integer.parseInt(tab_1vers[0][1])){
								vers.setText(tab_1vers[0][0]);
								suivant.setText(tab_2vers[0][0]);
								debut=false;
							}
						}
						else{
							if(signal_fin>Integer.parseInt(tab_1vers[0][2])){
								vers.setText("");
								debut=true;
								num_vers++;
							}
						}
					}
				}
				else {
					timer1.stop();
					minute=0;
					secondes=0;
					milliseconde=0;
					signal_fin=0;
					dureechanson=0;
					k_progression=0;l_progression=0;
					num_vers=0;
					progression.setValue(0);
					temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);
					btnPlay.setEnabled(true);
					btnStop.setEnabled(false);
					btnPlay_noVoice.setEnabled(true);
					btnStop_noVoice.setEnabled(false);
					btnSynchro.setEnabled(false);
					btn_exporter_novoice.setEnabled(true);

					if(no_voice_active){
						NoVoiceSound.getTestSound().stop();
						NoVoiceSound.stopTestSound();
					}
					else{
						player.stop();
						player=null;
					}
				}
			}
		};
		timer1= new Timer(delais,tache_timer);

		/* Action r�alis�e par le timer_noVoice */
		int delais_noVoice=1; //temps de rafraichissement en milliseconde
		ActionListener tache_timer_noVoice= new ActionListener(){
			public void actionPerformed(ActionEvent e1)
			{
				if(signal_fin<=dureechanson){
					signal_fin++;

					temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);/* rafraichir le label */
					if(affichage){
						tab_1vers=spe.retour_vers(num_vers);
						tab_2vers=spe.retour_vers(num_vers+1);
						if(debut){
							if(signal_fin>Integer.parseInt(tab_1vers[0][1])){
								vers.setText(tab_1vers[0][0]);
								suivant.setText(tab_2vers[0][0]);
								debut=false;
							}
						}
						else{
							if(signal_fin>Integer.parseInt(tab_1vers[0][2])){
								vers.setText("");
								debut=true;
								num_vers++;
							}
						}
					}
				}
				else {
					timer_noVoice.stop();
					minute=0;
					secondes=0;
					milliseconde=0;
					signal_fin=0;
					dureechanson=0;
					k_no_progression=0;l_no_progression=0;
					progression_noVoice.setValue(0);
					num_vers=0;
					temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);
					btnPlay.setEnabled(true);
					btnStop.setEnabled(false);
					btnPlay_noVoice.setEnabled(true);
					btnStop_noVoice.setEnabled(false);
					btnSynchro.setEnabled(false);
					if(no_voice_active){
						NoVoiceSound.getTestSound().stop();
						NoVoiceSound.stopTestSound();
					}
					else{
						player.stop();
						player=null;
					}
				}
			}
		};
		timer_noVoice= new Timer(delais_noVoice,tache_timer_noVoice);

		btnSynchro				.setBorder(BorderFactory.createLineBorder(couleurmenu));
		btnTxt					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btn_Valider				.setBorder(BorderFactory.createLineBorder(couleurmenu));
		btn_exporter_novoice	.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btn_bg1					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btn_bg2					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btn_bg3					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btn_bg4					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		defile					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		vers					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		suivant					.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		lb_voice				.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		lb_noVoice				.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		barreMenus				.setBackground(couleurmenu);
		btnPlay_noVoice			.setBackground(couleurmenu);
		btnStop_noVoice			.setBackground(couleurmenu);
		btnPlay					.setBackground(couleurmenu);
		btnStop					.setBackground(couleurmenu);
		btnTxt					.setBackground(couleurmenu);
		btn_Valider				.setBackground(Color.darkGray);
		btn_exporter_novoice	.setBackground(couleurmenu);
		btn_bg1					.setBackground(Color.darkGray);
		btn_bg2					.setBackground(Color.red);
		btn_bg3					.setBackground(Color.green);
		btn_bg4					.setBackground(Color.blue);
		console					.setBackground(new Color(73, 70, 73));
		vers					.setBackground(new Color(73, 70, 73));
		suivant					.setBackground(Color.black);
		progression				.setBackground(Color.darkGray);
		progression_noVoice		.setBackground(Color.darkGray);
		temps_ecoule			.setBackground(Color.darkGray);
		slideGain				.setBackground(new Color(73, 70, 73));

		console					.setForeground(Color.white);
		vers					.setForeground(couleurmenu);
		suivant					.setForeground(Color.white);
		temps_ecoule			.setForeground(couleurmenu);

		edition					.setFont(police);
		aide					.setFont(police);
		fichier					.setFont(police);
		nouveau					.setFont(police);
		ouvrir					.setFont(police);
		quitter					.setFont(police);
		help					.setFont(police);
		credits					.setFont(police);
		conseils				.setFont(police);
		rechercher				.setFont(police);
		btnTxt					.setFont(police);
		btn_exporter_novoice	.setFont(police);
		console					.setFont(new Font("tahoma",Font.TYPE1_FONT,11));
		vers					.setFont(new Font("tahoma",Font.TYPE1_FONT,22));
		suivant					.setFont(new Font("tahoma",Font.TYPE1_FONT,11));
		temps_ecoule			.setFont(new Font("tahoma",Font.TYPE1_FONT,17));

		arrierePlan				.setBounds(0, 0, 900, 292);
		lb_voice				.setBounds(20, 32, 130, 112);
		lb_noVoice				.setBounds(160, 32, 130, 112);
		lb_volume				.setBounds(20, 152, 20, 20);
		btnPlay_noVoice			.setBounds(163, 47, 36, 36);
		btnStop_noVoice			.setBounds(202, 47, 36, 36);
		btnPlay					.setBounds(23, 47, 36, 36);
		btnStop					.setBounds(62, 47, 36, 36);
		btnTxt					.setBounds(605, 152, 130, 20);
		btnSynchro				.setBounds(20, 231, 827, 31);
		btn_Valider				.setBounds(852, 187, 31, 70);
		btn_exporter_novoice	.setBounds(160,152,130,20);
		btn_bg1					.setBounds(820, 2, 15, 15);
		btn_bg2					.setBounds(840, 2, 15, 15);
		btn_bg3					.setBounds(860, 2, 15, 15);
		btn_bg4					.setBounds(880, 2, 15, 15);
		defile					.setBounds(300, 32, 435, 112);
		progression				.setBounds(35,102, 100, 15);
		progression_noVoice		.setBounds(175,102, 100, 15);
		vers					.setBounds(20, 179, 827, 30);
		suivant					.setBounds(200, 212, 463, 16);
		temps_ecoule			.setBounds(310, 152, 100, 18);
		slideGain				.setBounds(47, 152, 100, 20);

		btnPlay_noVoice			.setIcon(new ImageIcon("Images/btnPlay.png"));
		btnStop_noVoice			.setIcon(new ImageIcon("Images/btnStop.png"));
		btnPlay					.setIcon(new ImageIcon("Images/btnPlay.png"));
		btnStop					.setIcon(new ImageIcon("Images/btnStop.png"));
		btnSynchro				.setIcon(new ImageIcon("Images/barre_synchro.png"));

		console					.setEditable(false);
		vers					.setEditable(false);
		suivant					.setEditable(false);
		temps_ecoule			.setText("0:0.000");

		ouvrir					.addActionListener (this);
		quitter					.addActionListener (this);
		rechercher				.addActionListener (this);
		help					.addActionListener (this);
		nouveau					.addActionListener (this);
		credits					.addActionListener (this);
		conseils				.addActionListener (this);
		btnPlay_noVoice			.addActionListener (this);
		btnStop_noVoice			.addActionListener(this);
		btnPlay					.addActionListener (this);
		btnStop					.addActionListener(this);
		btnTxt					.addActionListener (this);
		btnSynchro				.addMouseListener(this);
		btn_Valider				.addActionListener(this);
		btn_exporter_novoice	.addActionListener(this);
		btn_bg1					.addActionListener(this);
		btn_bg2					.addActionListener(this);
		btn_bg3					.addActionListener(this);
		btn_bg4					.addActionListener(this);
		slideGain				.addChangeListener(this);

		// Composants du calque au premier plan (2)
		lp.add(btnPlay_noVoice, 		new Integer(2));
		lp.add(btnStop_noVoice, 		new Integer(2));
		lp.add(btnPlay, 				new Integer(2));
		lp.add(btnStop, 				new Integer(2));
		lp.add(btnTxt, 					new Integer(2)); 
		lp.add(btnSynchro, 				new Integer(2)); 
		lp.add(btn_Valider, 			new Integer(2));
		lp.add(btn_bg1, 				new Integer(2));
		lp.add(btn_bg2, 				new Integer(2));
		lp.add(btn_bg3, 				new Integer(2));
		lp.add(btn_bg4, 				new Integer(2));
		lp.add(defile,		 			new Integer(2));
		lp.add(vers, 					new Integer(2));
		lp.add(suivant, 				new Integer(2));
		lp.add(progression, 			new Integer(2));
		lp.add(progression_noVoice,		new Integer(2));
		lp.add(temps_ecoule, 			new Integer(2));
		lp.add(lb_voice, 				new Integer(2));
		lp.add(lb_noVoice, 				new Integer(2));
		lp.add(lb_volume, 				new Integer(2));
		lp.add(slideGain, 				new Integer(2));
		lp.add(btn_exporter_novoice, 	new Integer(2));

		ctrlEnabledNouveau(false);
		ctrlEnabledOuvrir(false);
	}

	/**
	 * Actions effectu�es par clique sur un bouton, li� au ActionListener
	 * @param e contient la r�f�rence du bonton activ�
	 */
	public void actionPerformed (ActionEvent e){
		Object source = e.getSource() ;
		System.out.println ("Action sur un bouton") ;
		//quitter le programme
		if (source == quitter) System.exit(0);
		
		//affichage des participants au projet
		if (source == credits) {
			System.out.println("Lancer credits");
			new Credits();
		}
		
		//affichage des conseils au d�marrage.
		if (source == conseils) {
			System.out.println("conseils");
			File f=new File("no.txt");
			f.delete();
		}
		
		if(source==btn_exporter_novoice ){
			if(!file_voice.equals(null)){
				new WaveFIleWriter(file_voice);
			}
		}
		//lancer une nouvelle chanson
		if (source == nouveau) {
			file_voice=explorer(".wav");
			nom_chanson=file_voice.getName();
			if(file_voice==null);
			else{ System.out.println(nom_chanson);
			File f1=new File(nom_chanson+".xml");
			//selon le r�sultat du test; affichage d'une information dans la console
			//Test: v�rification de l'existence du fichier .xml
			if (!f1.exists()){
				console.setText("-------Nouvelle chanson-------\n\nfichier audio choisi : "+nom_chanson+"\n\nVeuillez importer le fichier parole(.TXT).");
			}
			if (f1.exists()){
				console.setText("-------Nouvelle chanson-------\n\nfichier audio choisi : "+nom_chanson+"\n\n Attention :le fichier synchronis� existe d�j� !! \n si vous faites la synchronisation\n le fichier sera remplac� par le nouveau cr��.");
			}
			//Activation des composants correspondant � la fonction nouveau du menu et desactivation des autres
			ctrlEnabledOuvrir(true);
			ctrlEnabledNouveau(true);
			affichage=false;
			j=1;
			nb_vers=0;
			InitialiserBaliser();

			}
		}
		if (source == ouvrir) {
			file_voice=explorer(".wav");
			if(file_voice==null);
			else{ 
				//Activation des composants correspondant � la fonction ouvrir du menu et desactivation des autres
				ctrlEnabledNouveau(false);
				ctrlEnabledOuvrir(true);

				nom_chanson=file_voice.getName();
				file_xml = new File (nom_chanson+".xml");
				//selon le r�sultat du test; affichage d'une information dans la console
				//Test: v�rification de l'existence du fichier .xml
				//ici, le test est vrai, le fichier .xml n'existe pas.
				if (!file_xml.exists()){
					console.setText("-------KARAOKE-------\n\n fichier audio choisi : "+nom_chanson+"\n\n"+"ERREUR: Pas de fichier (.XML)\n Faire la synchronisation de la parole, d�abord.");
					btnPlay.setEnabled(false);
					btnPlay_noVoice.setEnabled(false);
					affichage=false;
				}

				else{
					//le test est faux, le fichier existe donc la fonction karaoke est possible
					spe = new SAXParserExample(file_xml);
					console.setText("-------KARAOKE-------\n\n Fichier audio choisi :"+nom_chanson+"\n\n nombres de vers: "+spe.nbrVers());
					affichage=true;
				}
			}
		}

		//Module de recherche
		if (source == rechercher)  {
			ctrlEnabledNouveau(false);
			ctrlEnabledOuvrir(false);

			Rechercher rech = new Rechercher();			
			console.setText("Recherche en cours ...");
			String motif=rech.recherche();		

			String resultat= rech.resultat();
			console.setText("-------Recherche-------\n\nrecherche du fichier parole contenant : "+motif+"\n" +resultat);

			String t_res[]=rech.res();

			String txt = (String)JOptionPane.showInputDialog(this,"lancer une piste","R�sultat de la recherche",JOptionPane.QUESTION_MESSAGE,null,rech.res(),t_res[1]);
			txt=txt.concat(".wav");
			File f= new File(txt);			
			file_voice=f;
			System.out.println(f);


			if (f!=null){
				console.setText("-------Recherche-------\n\nrecherche du fichier parole contenant : "+motif+"\n"+"\n piste audio :"+f);}





			btnPlay.setEnabled(true);
			btnPlay_noVoice.setEnabled(true);


		}
		//affichage des participants � la cr�ation de cette application
		if (source == credits) {
			console.setText(new Credits().text());
		}
		
		//affichage du manuel d'utilisateur
		if (source == help) {
			Runtime runtime = Runtime.getRuntime();
			try {

				runtime.exec(new String[]{"CoolPDFReader.exe","manuel.pdf"});
			}
			catch (Exception err){
				System.out.println("err="+err);
			}
		}
		
		//chargement du fichier texte pour la synchronisation
		if (source == btnTxt) {
			String texte="Paroles de la chanson: \n";
			StringBuffer sb= new StringBuffer(texte);
			file_txt=explorer(".txt");
			if(file_txt==null){
				console.setText("Cr�er dabord le fichier parole en (.txt)");
			}

			else{
				try {
					InputStream ips = null;
					ips = new FileInputStream(file_txt);
					InputStreamReader ipsr=new InputStreamReader(ips);
					BufferedReader br=new BufferedReader(ipsr);

					// d�but de lecture de la 1ere ligne
					String machin;
					machin = br.readLine();
					int i=0;
					tab_vers[i]=machin;
					// d�but de lecture des ligne suivantes si test=false
					do{
						i++;
						sb.append("\n"+machin);
						machin=br.readLine();
						tab_vers[i]=machin;
						nb_vers++;
					}while(!machin.contains("-----"));// fin de lecture du fichier quand on rencontre "-----"
					vers.setText(tab_vers[0]);

					br.close();
					ips.close();
					ipsr.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				console.setText(sb.toString());
			}
		}
		if (source == btn_Valider){
			baliser.append("</chanson>");
			try {
				CreerXml xml= new CreerXml(baliser.toString(), nom_chanson);
				xml.fichierXml();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			btn_Valider.setEnabled(false);
		}
		if (source == btnPlay) {
			if(file_voice==null){
				file_voice=explorer(".wav");
			}
			else{
				time0= new Date().getTime();
				timer1.start();
				btnPlay.setEnabled(false);
				btnPlay_noVoice.setEnabled(false);
				btnStop.setEnabled(true);

				player = new Sound(file_voice);
				player.start();
				Analyser= new MainAnalyser();
				Analyser.setFile(file_voice);										
				Analyser.start();
				dureechanson=Sound.getDuree();	
			}
		}
		if (source == btnPlay_noVoice) {
			if(file_voice==null){
				file_voice=explorer(".wav");
			}
			else{
				
				time0= new Date().getTime();
				btnPlay_noVoice.setEnabled(false);
				btnPlay.setEnabled(false);
				btnStop_noVoice.setEnabled(true);
				try {
					new WaveFIleWriter(file_voice);
					new NoVoiceSound(file_voice);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				no_voice_active=true;
				dureechanson=player.getDuree();
				Analyser= new MainAnalyser();
				String name=file_voice.getName();
				File file = new File("No-voice\\"+name.substring(0,name.length()-4)+"_novoice.wav");
				Analyser.setFile(file);
				Analyser.start();
				timer1.start();
				dureechanson=Sound.getDuree();	
			}
		}
		if (source == btnStop) {
			timer1.stop();
			minute=0;
			secondes=0;
			milliseconde=0;
			signal_fin=0;
			dureechanson=0;
			k_progression=0;l_progression=0;
			progression.setValue(0);
			num_vers=0;
			vers.setText("");
			suivant.setText("");
			temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);
			try {
				Analyser.Stop();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Analyser.yield();
			Analyser=null;
			player.stop();
			btnPlay.setEnabled(true);
			btnPlay_noVoice.setEnabled(true);
			btnStop.setEnabled(false);

			player=null;


		}
		if (source == btnStop_noVoice) {
			try {
				Analyser.Stop();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Analyser=null;
			timer1.stop();
			minute=0;
			secondes=0;
			milliseconde=0;
			signal_fin=0;
			dureechanson=0;
			k_no_progression=0;l_no_progression=0;
			progression_noVoice.setValue(0);
			num_vers=0;
			vers.setText("");
			suivant.setText("");
			temps_ecoule.setText(minute+":"+secondes+":"+milliseconde);
			NoVoiceSound.getTestSound().stop();
			btnPlay_noVoice.setEnabled(true);
			btnPlay.setEnabled(true);
			btnStop_noVoice.setEnabled(false);
			NoVoiceSound.stopTestSound();
			no_voice_active=false;
		}
		if(e.getSource()==btn_bg1){
			arrierePlan.setIcon(new ImageIcon("Images/fondkaraoke2.png"));
			console.setBackground(new Color(73, 70, 73));
			slideGain.setBackground(new Color(73, 70, 73));
			vers.setBackground(new Color(73, 70, 73));
			lb_voice.setIcon(new ImageIcon("Images/btnVoiceOFF.png"));
			lb_noVoice.setIcon(new ImageIcon("Images/btnVoiceOn.png"));
		}
		if(e.getSource()==btn_bg2){
			arrierePlan.setIcon(new ImageIcon("Images/fondkaraoke3.png"));
			console.setBackground(new Color(88, 35, 38));
			slideGain.setBackground(new Color(88, 35, 38));
			vers.setBackground(new Color(88, 35, 38));
			lb_voice.setIcon(new ImageIcon("Images/btnVoiceOFF3.png"));
			lb_noVoice.setIcon(new ImageIcon("Images/btnVoiceOn3.png"));
		}
		if(e.getSource()==btn_bg3){
			arrierePlan.setIcon(new ImageIcon("Images/fondkaraoke4.png"));
			console.setBackground(new Color(68, 98, 68));
			slideGain.setBackground(new Color(68, 98, 68));
			vers.setBackground(new Color(68, 98, 68));
			lb_voice.setIcon(new ImageIcon("Images/btnVoiceOFF4.png"));
			lb_noVoice.setIcon(new ImageIcon("Images/btnVoiceOn4.png"));
		}
		if(e.getSource()==btn_bg4){
			arrierePlan.setIcon(new ImageIcon("Images/fondkaraoke5.png"));
			console.setBackground(new Color(58, 55, 90));
			slideGain.setBackground(new Color(58, 55, 90));
			vers.setBackground(new Color(58, 55, 90));
			lb_voice.setIcon(new ImageIcon("Images/btnVoiceOFF5.png"));
			lb_noVoice.setIcon(new ImageIcon("Images/btnVoiceOn5.png"));
		}
	}	

	/**
	 * Active ou desactive les composants de la fonction karaok�
	 * @param val bool�en qui informe de l'�tat voulu des boutons
	 */
	public void ctrlEnabledOuvrir(boolean val){
		btnPlay.setEnabled(val);
		btnStop.setEnabled(false);
		btnPlay_noVoice.setEnabled(val);
		btnStop_noVoice.setEnabled(false);
	}

	/**
	 * Active ou desactive les composants de la fonction nouvelle chanson
	 * @param val
	 */
	public void ctrlEnabledNouveau(boolean val){
		btnTxt.setEnabled(val);
		btnSynchro.setEnabled(val);
		btn_Valider.setEnabled(val);
		btn_exporter_novoice.setEnabled(val);
	}

	/**
	 * explorateur de fichier
	 * @param ext chaine de caract�re correspondant au nom du fichier choisi
	 * @return retourne le fichier choisi
	 */
	public File explorer(String ext){
		//Cr�er un file chooser
		final JFileChooser fc = new JFileChooser();
		//Definir un r�pertoire par defaut pour windows
		fc.setCurrentDirectory(new File("C:\\Users\\Vijay\\Music\\"));
		//Filtre d'extensions de fichier
		fc.addChoosableFileFilter(new FiltreExtension(ext, "Fichier"));
		int returnVal = fc.showOpenDialog(Karaoke.this);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
		}
		return file;
	}

	/**
	 * Initialise le StringBuffer baliser avant de d'y ajouter les vers et les temps de synchronisation, destin� � etre copi� sur le fichier xml
	 */
	public void InitialiserBaliser(){
		baliser.setLength(0);
		baliser.append("<?xml version='1.0' encoding='iso-8859-1'?><chanson>");
	}

	/**
	 * utile pour que les boutons soient editables sous mac os
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==slideGain){
			if(no_voice_active){
				slideGain.setToolTipText("Gain : " + slideGain.getValue() + "%");
				try {
					Sound.setGain(slideGain.getValue());
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(slideGain, "Gain operation not supported", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				slideGain.setToolTipText("Gain : " + slideGain.getValue() + "%");
				try {
					Sound.setGain(slideGain.getValue());
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(slideGain, "Gain operation not supported", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getSource()== btnSynchro){
			String tps="";
			btnSynchro.setIcon(new ImageIcon("Images/barre_synchro_1.png"));
			baliser.append("<vers><texte>"+vers.getText()+"</texte><tps_debut>"+(signal_fin-500)+"</tps_debut>");			
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getSource()== btnSynchro){
			btnSynchro.setIcon(new ImageIcon("Images/barre_synchro.png"));
			baliser.append("<tps_fin>"+(signal_fin-500)+"</tps_fin></vers>");
			vers.setText(tab_vers[j]);
			suivant.setText(tab_vers[j+1]);
			j++;
			if(j==nb_vers)vers.setText("");
		}
	}
}