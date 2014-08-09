
package interfaces;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import moteurSound.AudioEngine;

public class ResourceManager extends JFrame {

    private AudioEngine audioEngine;
    private Display display;
    
    /**
     * constructeur
     * @param audioEngine
     * prends en parametre un audioengine, permet d'initialiser la classe et de 
     * recuperer la taille et la hauteur de la fenetre initiale karaoke. 
     */
    
    public ResourceManager(AudioEngine audioEngine) {
    	
    	super("VISUALISATION: SPECTRE AUDIO");
    	this.setSize(100,250);
    	this.setUndecorated(true);
    	this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray));
    	
    	/* calcul du placement dans la fenetre */
		Toolkit t = this.getToolkit(); 
		Dimension d = t.getScreenSize(); 
		int w = d.width; 
		int h = d.height; 
		this.setLocation(w/2+453, h/2-292);
		
    	this.setLayout(new BorderLayout());
    	
    	
    	this.audioEngine = audioEngine;

    	display = new Display(this); 
    	
    	this.add(display, BorderLayout.CENTER);
       
        this.setVisible(true);
        
        display.displayThread.start();
    }

    /**
     * 
     * @return l'objet audioengine
     */
    public AudioEngine getAudioEngine() {
    	return audioEngine;
    }
    /**
     * 
     * @return l'objet display
     */
    public Display getDisplay() {    
        return this.display;
    }   
}