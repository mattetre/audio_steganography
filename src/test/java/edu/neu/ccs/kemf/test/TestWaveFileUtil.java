package edu.neu.ccs.kemf.test;

import static org.junit.Assert.*;

import edu.neu.ccs.kemf.Complex;
import edu.neu.ccs.kemf.WaveFileUtil;

import org.junit.Test;

public class TestWaveFileUtil {

	private static double doubleRange = 0.000000000001;

	byte[] byteArray = {1, 2, 3, 4, 5, 6, 7 ,8};
	
	int[] intSamples1 = {72, 125, 50, 100};
	int[] intSamples2 = {200, 250, 300};
	int[] intSamples3 = {0, -100};
	
	double[] doubleSamples1 = {0.002197265625, 0.003814697265625, 0.00152587890625, 0.0030517578125};
	double[] doubleSamples2 = {0.006103515625, 0.00762939453125, 0.0091552734375};
	double[] doubleSamples3 = {0.0, -0.0030517578125};
	
	Complex c1 = new Complex(75, 10);
	Complex c2 = new Complex(25, 5);
	Complex c3 = new Complex(100, 15);
	Complex c4 = new Complex(50, 5);
	
	Complex[] complexArray1 = {c1, c2};
	Complex[] complexArray2 = {c1, c2, c3, c4};
	Complex[] complexArray3 = {};

	@Test
	public void testExtractSamples() {
		int[] actual = WaveFileUtil.extractSamples(byteArray, 8);
		int[] expected1 = {513};
		assertArrayEquals(expected1, actual);
		
		actual = WaveFileUtil.extractSamples(byteArray, 4);
		int[] expected2 = {513, 1541};
		assertArrayEquals(expected2, actual);
		
		actual = WaveFileUtil.extractSamples(byteArray, 2);
		int[] expected3 = {513, 1027, 1541, 2055};
		assertArrayEquals(expected3, actual);
	}

	@Test
	public void testScaleSamples() {
		double[] actual = WaveFileUtil.scaleSamples(intSamples1);
		assertArrayEquals(doubleSamples1, actual, doubleRange);
		int[] unscaled = WaveFileUtil.unscaleSamples(actual);
		assertArrayEquals(intSamples1, unscaled);

		actual = WaveFileUtil.scaleSamples(intSamples2);
		assertArrayEquals(doubleSamples2, actual, doubleRange);
		unscaled = WaveFileUtil.unscaleSamples(actual);
		assertArrayEquals(intSamples2, unscaled);
		
		actual = WaveFileUtil.scaleSamples(intSamples3);
		assertArrayEquals(doubleSamples3, actual, doubleRange);
		unscaled = WaveFileUtil.unscaleSamples(actual);
		assertArrayEquals(intSamples3, unscaled);
	}

	@Test
	public void testUnscaleSamples() {
		int[] actual = WaveFileUtil.unscaleSamples(doubleSamples1);
		assertArrayEquals(intSamples1, actual);
		double[] scaled = WaveFileUtil.scaleSamples(actual);
		assertArrayEquals(doubleSamples1, scaled, doubleRange);
				
		actual = WaveFileUtil.unscaleSamples(doubleSamples2);
		assertArrayEquals(intSamples2, actual);
		scaled = WaveFileUtil.scaleSamples(actual);
		assertArrayEquals(doubleSamples2, scaled, doubleRange);
		
		actual = WaveFileUtil.unscaleSamples(doubleSamples3);
		assertArrayEquals(intSamples3, actual);
		scaled = WaveFileUtil.scaleSamples(actual);
		assertArrayEquals(doubleSamples3, scaled, doubleRange);
	}

	@Test
	public void testGetMaxMagnitudeIndex() {
		int actual = WaveFileUtil.getMaxMagnitudeIndex(complexArray1);
		assertEquals(1, actual);
		
		actual = WaveFileUtil.getMaxMagnitudeIndex(complexArray2);
		assertEquals(2, actual);
		
		actual = WaveFileUtil.getMaxMagnitudeIndex(complexArray3);
		assertEquals(-1, actual);
	}

	@Test
	public void testComplexArrayGetReals() {
		double c1Real = c1.getReal();
		double c2Real = c2.getReal();
		double c3Real = c3.getReal();
		double c4Real = c4.getReal();
		
		double[] actual = WaveFileUtil.complexArrayGetReals(complexArray1);
		double[] expected1 = {c1Real, c2Real};
		assertArrayEquals(expected1, actual, doubleRange);

		actual = WaveFileUtil.complexArrayGetReals(complexArray2);
		double[] expected2 = {c1Real, c2Real, c3Real, c4Real};
		assertArrayEquals(expected2, actual, doubleRange);
		
		actual = WaveFileUtil.complexArrayGetReals(complexArray3);
		double[] expected3 = {};
		assertArrayEquals(expected3, actual, doubleRange);

	}

	@Test
	public void testCombineSamples() {
		int[] actual = WaveFileUtil.combineSamples(intSamples1, intSamples1);
		int[] expected1 = {72, 72, 125, 125, 50, 50, 100, 100};
		assertArrayEquals(expected1, actual);
		
		actual = WaveFileUtil.combineSamples(intSamples2, intSamples2);
		int[] expected2 = {200, 200, 250, 250, 300, 300};
		assertArrayEquals(expected2, actual);
		
		actual = WaveFileUtil.combineSamples(intSamples3, intSamples3);
		int[] expected3 = {0, 0, -100, -100};
		assertArrayEquals(expected3, actual);
	}

	@Test
	public void testGetAverageMag() {
		double actual = WaveFileUtil.getAverageMag(complexArray1);
		double expected1 = 12.747548783981962;
		assertEquals(expected1, actual, doubleRange);
		
		actual = WaveFileUtil.getAverageMag(complexArray2);
		double expected2 = 44.21580443858795;
		assertEquals(expected2, actual, doubleRange);
	}

}
