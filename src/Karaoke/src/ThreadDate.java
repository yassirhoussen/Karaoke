package Karaoke.src;
import java.util.Date;
public class ThreadDate extends Thread{
	
	public ThreadDate (){
		
	}
/*fonction temps 
 * utilis� pendant la lecture pour voir l'avancement
 * dans la cr�ation du fichier xml (synchronisation de parole)
 * dans l'affichage des paroles synchronis�s
 * 	
	*/
	public void run( ){
		boolean continuer=true;
		long secondes=0;
		long time0= new Date().getTime();
		while (continuer) {
long time = (new Date( ).getTime())-time0;
			
			if (time >=999){
				time0=(new Date().getTime());
				secondes++;

				System.out.println("secondes"+ secondes);
			}
				System.out.println("millisecondes"+time );
			
			}
		}
	}