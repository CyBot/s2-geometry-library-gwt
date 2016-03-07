package com.google.common.geometry;

public class Utils {
	public static double IEEEremainder(double f1, double f2)
	{
		double div = Math.round(f1 / f2);
		return f1 - (div * f2);
	}

	public static double[] ArrayClone(double[] src) {
		double[] dst = new double[src.length];
		System.arraycopy(src, 0, dst, 0, src.length);
		return dst;
	}

	public static double[][] ArrayClone(double[][] src) {
		double[][] dst = new double[src.length][];
		for (int i = 0; i < src.length; i++) 
			dst[i] = ArrayClone(src[i]);
		return dst;
	}

	public static <T> T[] ArrayClone(T[] src) {
		@SuppressWarnings("unchecked")
		T[] dst = (T[]) new Object[src.length];
		System.arraycopy(src, 0, dst, 0, src.length);
		return dst;
	}
}
