package interfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Line2D;
import javax.swing.JPanel;


public class Display extends JPanel implements Runnable, ComponentListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6538499312205612081L;
	private ResourceManager rm;
	private int refreshRate;
	private boolean antialiased;
	private int width,height;
	protected Thread displayThread;
	
	public static final Color BACKGROUND =  new Color(73, 70, 73);
	public static final Color YELLOW = new Color(222, 169, 65);

	/**
	 * constructeur
	 * @param rm ressourcemanager 
	 * initialisation des variable de classe
	 */
	public Display(ResourceManager rm) {
		super();
		this.rm = rm;
		this.setBackground(BACKGROUND);
		displayThread = new Thread(this);
		refreshRate = 25; // milliseconds
		antialiased = true;
		this.addComponentListener(this);
		width = this.getWidth();
		height = this.getHeight();
	}
	
	/**
	 * @param g Graphics
	 * cette m�thode permet a partir d'un tableau de double contenant les valeurs des amplitudes 
	 * obtenus par les calculs l'audiobuffer d'afficher dans ce graphique les valeurs de ce tableau, en prenant comme 
	 * contraintes la hauteur et la largeur de la fentre d'affichage
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
      
        if (antialiased == true) {
        	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
        double y0, l, x0=10.0;
			double[] amplitude = rm.getAudioEngine().getAudioAnalyser().getCurrentFrame();
			y0 = this.getHeight()/2;    
			g2.setColor( YELLOW );
	        for (int i = 0; i < width-1; i++) {
				int index = i*amplitude.length/width;
				int index2 = (i+1)*amplitude.length/width;
				Line2D.Double line = new Line2D.Double(i, y0-y0*amplitude[index], i+1, y0-y0*amplitude[index2]);            
	            g2.draw(line);
			}
		g2.dispose();
	}
	
	/**
	 * m�thode run, qui redessine le graphe toutes des 25 millisecondes, pour avoir une.
	 * sensation de mouvements 
	 */
	public void run() {
		while (true) {
			this.repaint();
			try {
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	public int getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}

	public void componentHidden(ComponentEvent arg0) {
		
	}

	public void componentMoved(ComponentEvent arg0) {
		
	}

	public void componentResized(ComponentEvent arg0) {
		width = this.getWidth();
		height = this.getHeight();
	}

	public void componentShown(ComponentEvent arg0) {
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
