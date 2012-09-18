package edu.neu.ccs.kemf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.neu.ccs.kemf.Complex;

public class TestComplex {

	private static double doubleRange = 0.000000000001;

	Complex c1 = new Complex(75, 10);
	Complex c2 = new Complex(25, 5);
	Complex c3 = new Complex(100, 15);
	Complex c4 = new Complex(50, 5);
	Complex c5 = new Complex(1825, 625);
	Complex c6 = new Complex(750, 100);
	Complex c7 = new Complex(50, 10);
	Complex c8 = new Complex(75, -10);
	Complex c9 = new Complex(0, 0);
	double c1Mag = 75.66372975210778;
	double c2Mag = 25.495097567963924;

	@Test
	public void testAdd() {
		assertTrue(c3.equals(c1.add(c2)));
		assertTrue(c6.equals(c6.add(c9)));
	}

	@Test
	public void testSub() {
		assertTrue(c4.equals(c1.sub(c2)));
		assertTrue(c7.equals(c7.sub(c9)));
	}

	@Test
	public void testMultComplex() {
		assertTrue(c5.equals(c1.mult(c2)));
		assertTrue(c9.equals(c5.mult(c9)));
	}

	@Test
	public void testMultDouble() {
		assertTrue(c6.equals(c1.mult(10)));
		assertTrue(c7.equals(c2.mult(2)));
	}

	@Test
	public void testConj() {
		assertTrue(c8.equals(c1.conj()));
	}

	@Test
	public void testMagnitude() {
		assertEquals(c1Mag, c1.magnitude(), doubleRange);
		assertEquals(c1Mag, c1.magnitude(), doubleRange);
	}

}
