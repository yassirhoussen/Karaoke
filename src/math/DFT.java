package math;

/**
 * cette classe a été prise sur la page 
 * http://www.cs.princeton.edu/introcs/97data/
 */

public class DFT {
	

//	public static final int BLACKMANN = 3;
	
	public static final double[] forwardMagnitude(double[] input) {
		int N = input.length;
		double[] mag = new double[N];
		double[] c = new double[N];
		double[] s = new double[N];
		double twoPi = 2*Math.PI;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				c[i] += input[j]*Math.cos(i*j*twoPi/N);
				s[i] -= input[j]*Math.sin(i*j*twoPi/N);
			}
			c[i]/=N;
			s[i]/=N;
			
			mag[i]=Math.sqrt(c[i]*c[i]+s[i]*s[i]);
		}
		
		return mag;
	}
//	blackman seulement fentre de ponderantion de blackman 
//	
	public static final double[] window(double[] input) {
		int N = input.length;
		double[] windowed = new double[N];

			for(int n=0; n<N; n++) {
				windowed[n] = (0.42-0.5*Math.cos(2*Math.PI*n/(N-1))+0.08*Math.cos(4*Math.PI*n/(N-1)) ) * input[n];
			}
		return windowed;
	}
}
