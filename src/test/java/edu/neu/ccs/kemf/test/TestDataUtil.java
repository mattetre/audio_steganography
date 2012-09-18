package edu.neu.ccs.kemf.test;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import edu.neu.ccs.kemf.DataUtil;

public class TestDataUtil {

	int byteValue1 = 55;
	int byteValue2 = -13;
	int size = 8;

	@Test
	public void testValueToBitSet() {
		BitSet bitSet = new BitSet();
		bitSet.set(0);
		bitSet.set(1);
		bitSet.set(2);
		bitSet.set(4);
		bitSet.set(5);
		assertEquals(bitSet, DataUtil.valueToBitSet(byteValue1, size));

		bitSet.clear();
		bitSet.set(0);
		bitSet.set(1);
		bitSet.set(4);
		bitSet.set(5);
		bitSet.set(6);
		bitSet.set(7);
		assertEquals(bitSet, DataUtil.valueToBitSet(byteValue2, size));
	}

	@Test
	public void testSignedToUnsignedByte() {
		byte b1 = -10;
		byte b2 = 15;
		byte b3 = 127;
		byte b4 = -120;
		
		assertEquals(246, DataUtil.signedToUnsigned(b1));
		assertEquals(15, DataUtil.signedToUnsigned(b2));
		assertEquals(127, DataUtil.signedToUnsigned(b3));
		assertEquals(136, DataUtil.signedToUnsigned(b4));
	}
	
	@Test
	public void testSignedToUnsignedInt() {
		int i1 = 33;
		int i2 = -60;
		int i3 = 140;
		
		assertEquals(33, DataUtil.signedToUnsigned(i1));
		assertEquals(-60, DataUtil.signedToUnsigned(i2));
		assertEquals(140, DataUtil.signedToUnsigned(i3));
	}
	
}