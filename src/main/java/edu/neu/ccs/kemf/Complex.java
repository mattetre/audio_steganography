package edu.neu.ccs.kemf;

/**
 * Class to represent a Complex number
 */
public class Complex {

	private double real;
	private double imaginary;
	
	public Complex() {
		this.real = 0;
		this.imaginary = 0;
	}
	
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public double getReal() {
		return this.real;
	}
	
	public void setReal(double real) {
		this.real = real;
	}
	
	public double getImaginary() {
		return this.imaginary;
	}
	
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
	
	/**
	 * Adds a complex number to this complex number
	 * @param number The complex number to add
	 * @return New complex number 
	 */
	public Complex add(Complex number) {
		Complex newC = new Complex();
		newC.setReal(this.real + number.real);
		newC.setImaginary(this.imaginary + number.imaginary);
		
		return newC;
	}
	
	/**
	 * Adds a complex number to this complex number
	 * @param number The complex number to add
	 * @return New complex number 
	 */
	public Complex sub(Complex number) {
		Complex newC = new Complex();
		newC.setReal(this.real - number.real);
		newC.setImaginary(this.imaginary - number.imaginary);
		
		return newC;
	}
	
	/**
	 * Adds a complex number to this complex number
	 * @param number The complex number to add
	 * @return New complex number 
	 */
	public Complex mult(Complex number) {
		Complex newC = new Complex();
		newC.setReal( (this.real * number.real) - (this.imaginary * number.imaginary) );
		newC.setImaginary( (this.real * number.imaginary) + (this.imaginary * number.real) );
		
		return newC;
	}
	
	/**
	 * Adds a complex number to this complex number
	 * @param number The complex number to add
	 * @return New complex number 
	 */
	public Complex mult(double number) {
		Complex newC = new Complex();
		newC.setReal(this.real * number);
		newC.setImaginary(this.imaginary * number);
		
		return newC;
	}
	
	/**
	 * Computes the conjugate of this complex number
	 * @return New complex number 
	 */
	public Complex conj() {
		return new Complex(this.real, (-1.0 * this.imaginary));
	}
	
	/**
	 * Computes the magnitude of the complex number
	 * @return The magnitude
	 */
	public double magnitude() {
		return Math.abs(Math.sqrt( Math.pow(this.real, 2) + Math.pow(this.imaginary, 2) )); 
	}
	
	public String toString() {
		if (this.imaginary == 0.0)
			return Double.toString(this.real);
		
		String sym = (this.imaginary < 0) ? "" : "+";
		return this.real + sym + Double.toString(this.imaginary) +"i";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(imaginary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(real);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (Double.doubleToLongBits(imaginary) != Double
				.doubleToLongBits(other.imaginary))
			return false;
		if (Double.doubleToLongBits(real) != Double
				.doubleToLongBits(other.real))
			return false;
		return true;
	}
	
}
