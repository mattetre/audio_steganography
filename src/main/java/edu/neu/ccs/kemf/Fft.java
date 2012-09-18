package edu.neu.ccs.kemf;

/**
 * FFT class
 */
public class Fft {

	/**
	 * Compute the fft of the given double value array (assumes all real vals)
	 * @param input The array of real values
	 * @return A complex array of FFT results
	 * @throws Exception
	 */
	public static Complex[] fft(double[] input) throws Exception{
		return fft(doubleToComplex(input));
	}

	/**
	 * Compute the fft of the given array
	 * @param input The array of complex number
	 * @return A complex array of FFT results
	 * @throws Exception
	 */
	public static Complex[] fft(Complex[] input) throws Exception{

		// length of array
		int n = input.length;
		// if n is 1 then return a new array of the element
		if (n == 1)
			return new Complex[] { input[0] };
		
		// check the length is power of 2
		if ((n % 2) != 0)
			throw new Exception("Length of array must be power of 2");
		
		// half the input length
		int n2 = n/2;
		
		Complex[] half = new Complex[n2];

		// compute fft on even elements
		for (int i = 0; i < n2; i++) {
			half[i] = input[2 * i];
		}
		Complex[] even = fft(half);

		// compute fft on even elements
		for (int i = 0; i < n2; i++) {
			half[i] = input[2 * i + 1];
		}
		Complex[] odd = fft(half);
		
		// constant -2pi 
		double i2piN = -2 * Math.PI / n;
		Complex[] returnArray = new Complex[n];
		// loop through two halves combining
		for (int i = 0; i < n2; i++) {

			double twiddle = i * i2piN;
            Complex twiddleC = new Complex(Math.cos(twiddle), Math.sin(twiddle));
            
            returnArray[i] = even[i].add(twiddleC.mult(odd[i]));
            returnArray[i + n2] = even[i].sub(twiddleC.mult(odd[i]));
		}
		
		return returnArray;
	}	
	
	
	/**
	 * Compute the Inverse FFT
	 * <br>
	 * ifft(x) = 1/N conj(fft(conj(x)))
	 * <br>
	 * 
	 * @param input The input to compute the inverse of
	 * @return A Complex array
	 * @throws Exception
	 */
	public static Complex[] fftInverse(Complex[] input) throws Exception {
		int n = input.length;
		
		Complex[] xConj = new Complex[n];
		// loop through the input and compute conjucates
		for (int i = 0; i < n; i++) {
			xConj[i] = input[i].conj();
		}
		
		// compute the FFT of the conjugates
		xConj = fft(xConj);

		// loop through the FFT of conjugates and compute the conjugate again and then divide it by n
		for (int i = 0; i < n; i++) {
			xConj[i] = xConj[i].conj().mult(1.0 / n);
		}
		
		return xConj;
		
	}
	
	/**
	 * Compute the Inverse FFT
	 * <br>
	 * ifft(x) = 1/N conj(fft(conj(x)))
	 * <br>
	 * 
	 * @param input The input to compute the inverse of
	 * @return A Complex array
	 * @throws Exception
	 */
	public static Complex[] fftInverse(double[] input) throws Exception {
		return fftInverse(doubleToComplex(input));		
	}
	
	private static Complex[] doubleToComplex(double[] input) {
		int n = input.length;
		Complex[] inputComplex = new Complex[n];
		for (int i = 0; i < n; i++) {
			inputComplex[i] = new Complex(input[i], 0.0);
		}
		return inputComplex;
	}
	
	
	

	
}
