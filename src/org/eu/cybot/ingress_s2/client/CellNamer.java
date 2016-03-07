package org.eu.cybot.ingress_s2.client;

import com.google.common.geometry.MutableInteger;
import com.google.common.geometry.S2CellId;

import java.util.Arrays;

public class CellNamer {
	protected static final int REGION_BIT_SHIFT = 26;
	protected static final int LOCAL_BIT_SHIFT = 49;

	protected S2CellId cellId;
	private static String[] regions = { "AF", "AS", "NR", "PA", "AM", "ST", };
	private static String[] codewords = { "ALPHA", "BRAVO", "CHARLIE", "DELTA",
			"ECHO", "FOXTROT", "GOLF", "HOTEL", "JULIET", "KILO", "LIMA", "MIKE",
			"NOVEMBER", "PAPA", "ROMEO", "SIERRA", };

	public CellNamer(S2CellId cellId) {
		this.cellId = cellId;
	}

	public static S2CellId parseName(String name) {
		if (!name.matches("[ANPS][AFMRST][0-9]{2}-[A-Z]+-[0-9]{2}")) {
			throw new IllegalArgumentException("Invalid name format: " + name);
		}
		String region = name.substring(0, 2);
		int regionNumber = Integer.parseInt(name.substring(2, 4), 10) - 1;
		String codeword = name.substring(5, name.lastIndexOf("-"));
		int division = Integer.parseInt(name.substring(1 + name.lastIndexOf("-")),
				10);

		int regionValue = Arrays.asList(regions).indexOf(region);
		long codeNumber = Arrays.asList(codewords).indexOf(codeword);

		if (regionValue < 0 || regionValue > 5) {
			throw new IllegalArgumentException("Invalid region code: " + name);
		}
		if (regionNumber < 0 || regionNumber > 15) {
			throw new IllegalArgumentException("Invalid region number: " + name);
		}
		if (codeNumber < 0 || codeNumber > 15) {
			throw new IllegalArgumentException("Invalid codeword: " + name);
		}
		if (division < 0 || division > 15) {
			throw new IllegalArgumentException("Invalid division number: " + name);
		}

		int face = regionValue;
		long iValue = regionNumber << REGION_BIT_SHIFT;
		long jValue = codeNumber << REGION_BIT_SHIFT;
		long child = division;

		S2CellId id;
		if (shouldSwap(face)) {
			// swap i and j
			id = S2CellId.fromFaceIJ(face, (int) jValue, (int) iValue);
		} else {
			id = S2CellId.fromFaceIJ(face, (int) iValue, (int) jValue);
		}
		if (id.level() > 4) {
			id = id.parent(4);
		}
		id = id.childBegin(6);
		long newId = id.id() | (child << LOCAL_BIT_SHIFT);
		return new S2CellId(newId);
	}

	protected static boolean shouldSwap(int face) {
		return face % 2 == 1;
	}

	public String getName() {
		return getRegion() + (getRegionNumber() < 10 ? "0" : "") + getRegionNumber() +
				'-' + getCodeword() + '-' + (getDivision()  < 10 ? "0" : "") + getDivision();
	}

	public String getRegion() {
		return regions[cellId.face()];
	}

	public int getRegionNumber() {
		MutableInteger i = new MutableInteger(0);
		MutableInteger j = new MutableInteger(0);
		cellId.toFaceIJOrientation(i, j, null);
		if (shouldSwap(cellId.face())) {
			return (int) (j.intValue() >>> REGION_BIT_SHIFT) + 1;
		} else {
			return (int) (i.intValue() >>> REGION_BIT_SHIFT) + 1;
		}
	}

	public String getCodeword() {
		MutableInteger i = new MutableInteger(0);
		MutableInteger j = new MutableInteger(0);
		cellId.toFaceIJOrientation(i, j, null);
		if (shouldSwap(cellId.face())) {
			return codewords[(int) (i.intValue() >>> REGION_BIT_SHIFT)];
		} else {
			return codewords[(int) (j.intValue() >>> REGION_BIT_SHIFT)];
		}
	}

	public int getDivision() {
		return (int) ((cellId.pos() >>> LOCAL_BIT_SHIFT) & 0xF);
	}
}
