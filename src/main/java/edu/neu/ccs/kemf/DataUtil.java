package edu.neu.ccs.kemf;

import java.util.BitSet;

/**
 * Perform Data manipulations 
 */
public class DataUtil {
	
	/**
	 * Convert a bitset to an integer value
	 * @param bitSetValue The bitset to convert
	 * @return the int value of a bit set
	 */
	public static int bitSetToInt(BitSet bitSetValue) {
		int returnInt = 0;
		
		// Loop through int size shifting bytes as needed
		for (int i = Integer.SIZE; i >=0; i--) {
			if (bitSetValue.get(i)) {
				returnInt |= (1 << i);
			}
		}
		return returnInt;
	}
	
	/**
	 * Convert byte to bitset
	 * @param byteValue byte value to convert
	 * @return bitset for the byte
	 */
	public static BitSet byteToBitSet(byte byteValue) {
		return valueToBitSet(byteValue, 8);
	}
	
	/**
	 * Convert an 8 bit int (unsigned byte) to a bitset
	 * @param byteValue The 8bit int value
	 * @return bitset for the byte
	 */
	public static BitSet byte8ToBitSet(int byteValue) {
		return valueToBitSet(byteValue, 8);
	}
	
	/**
	 * Convert an in to a bitset
	 * @param byteValue The in value
	 * @return bitset for the int
	 */
	public static BitSet intToBitSet(int byteValue) {
		return valueToBitSet(byteValue, 32);
	}
	
	/**
	 * Convert a value to a bitset
	 * @param byteValue The value to convert
	 * @param size How many bits are in the value (ex: 8 for bit, 32 for int)
	 * @return The bitset for the value
	 */
	public static BitSet valueToBitSet(int byteValue, int size) {
		// create a bit set to hold values
		BitSet returnBitSet = new BitSet(size);
		//byte shiftedValue = byteValue;
		int shiftedValue = byteValue;
		if (byteValue < 0) {
			if (size == Byte.SIZE)
				shiftedValue = DataUtil.signedToUnsigned((byte)byteValue);
			if (size == Integer.SIZE)
				shiftedValue = DataUtil.signedToUnsigned(byteValue);
		}
		
		// loop through the size which should be either a 8 for a byte or 32 for an int
		for (int i = (size - 1); i >= 0; i--) {
			boolean setVal = ((shiftedValue >> i) == 1);
			returnBitSet.set(i, setVal);
			
			if (setVal)
				shiftedValue ^= (1 << i);
		}
		return returnBitSet;
	}
	
	/**
	 * Returns a string of the bits of a bitset <br>
	 * Starts from the end of the bitset and prints 
	 * left to right moving to lower bits
	 * 
	 * @param bs BitSet to return a string for
	 * @return A String containing the bits
	 */
	public static String bitSetToString(BitSet bs) {
		String bitSetStr = new String();
		for (int i = (Byte.SIZE - 1); i >= 0; i--) {
			bitSetStr += (bs.get(i)? 1 : 0);
		}
		return bitSetStr;
	}
	
	/**
	 * Signed byte to an unsigned value
	 * @param byteValue The byte value
	 * @return an int with value of byteValue unsigned
	 */
	public static int signedToUnsigned(byte byteValue) {
		return byteValue & 0xFF;
	}
	
	/**
	 * Signed into to unsigned
	 * @param intValue The int value to convert
	 * @return an unsigned int
	 */
	public static int signedToUnsigned(int intValue) {
		return intValue & 0xFFFFFFFF;
	}
	
	/**
	 * Retruns the lowest bit value
	 * @param value The byte to check
	 * @return 0 or 1 value of lowest bit
	 */
	public static int getLowestBit(byte value) {
		return getLowestBit((int)value);
	}
	
	/**
	 * Retruns the lowest bit value
	 * @param value The int to check
	 * @return 0 or 1 value of lowest bit
	 */
	public static int getLowestBit(int value) {
		return (Integer.lowestOneBit(value) == 1)? 1 : 0;
	}
	
	/**
	 * Returns a value with the least significan bit set to the value
	 * @param byteValue The int value to set
	 * @param value The value to set the LSB (true : 1 false : 0)
	 * @return new int with lsb changed
	 */
	public static int setLeastSignificanBit(int byteValue, boolean value) {
		boolean leastIsOne = (byteValue % 2) == 1;
		/*
		 * Covers cases:
		 * If least sig is 1 and we want to set 0
		 * If least sig is 0 and we want to set 1
		 */
		if (leastIsOne ^ value)
			return byteValue ^= 1;
		
		// else leave value
		else
			return byteValue;
	}
	
	/**
	 * Extracts the least significant bits from a byte array and creats a byte[] with values
	 * @param byteArray The byte array to extract from
	 * @return A new byte[] containing all the least significant bits
	 */
	public static byte[] extractLsbFromByteArray(byte[] byteArray) {
		
		byte[] lsbByteArray = new byte[byteArray.length / Byte.SIZE];
		
		int count = 1;
		byte lsbByte = 0;
		int finalByteArrayPos = 0;
		
		// loop through the byte array
		for (int i = 0; i < byteArray.length; i++) {
			int byteValue = byteArray[i];
			
			// get the least signiciant value
			boolean value = (Integer.lowestOneBit(byteValue) == 1);
			// set the byte
			if (value)
				lsbByte |= (1 << (Byte.SIZE - count));
				
			lsbByteArray[finalByteArrayPos] = lsbByte;
				
			// if size of byte then reset value and increment position
			if (count == Byte.SIZE) {
				lsbByte = 0;
				count = 0;
				finalByteArrayPos++;
			}
			count++;
		}
		
		return lsbByteArray;
	}
			
	
}
