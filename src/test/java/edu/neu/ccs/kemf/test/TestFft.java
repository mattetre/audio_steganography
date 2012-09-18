package edu.neu.ccs.kemf.test;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import edu.neu.ccs.kemf.Complex;
import edu.neu.ccs.kemf.Fft;

public class TestFft {

	Complex c1 = new Complex(75, 25);
	Complex c2 = new Complex(120, 44);
	Complex c3 = new Complex(33, 10);
	Complex c4 = new Complex(100, 60);
	
	Complex c5 = new Complex(328, 139);
	Complex c6 = new Complex(26, -5);
	Complex c7 = new Complex(-112, -69);
	Complex c8 = new Complex(58, 35);
	
	Complex[] actual;
	Complex[] test1;
	Complex[] test2;
	Complex[] complexArray1 = {c1, c2, c3, c4};
	Complex[] complexArray2 = {c5, c6, c7, c8};
	
	@Test
	public void testFftComplexArray() throws Exception {
		actual = Fft.fft(complexArray1);
		assertArrayEquals(complexArray2, actual);
		
		test1 = Fft.fftInverse(complexArray2);
		test2 = Fft.fft(test1);
		assertArrayEquals(complexArray2, test2);
	}

	@Test
	public void testFftInverse() throws Exception {
		actual = Fft.fftInverse(complexArray2);
		assertArrayEquals(complexArray1, actual);
		
		test1 = Fft.fft(complexArray1);
		test2 = Fft.fftInverse(test1);
		assertArrayEquals(complexArray1, test2);
	}
	
}
