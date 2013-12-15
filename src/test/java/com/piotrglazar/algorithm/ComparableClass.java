package com.piotrglazar.algorithm;

import java.util.Comparator;

public class ComparableClass {

	private int key;

	public ComparableClass(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public static Comparator<ComparableClass> getComparator() {
		return new Comparator<ComparableClass>() {

			@Override
			public int compare(ComparableClass o1, ComparableClass o2) {
				return (int) Math.signum(o1.key - o2.key);
			}
		};
	}

	public static ComparableClass[] fromInts(Integer[] ints) {
		ComparableClass[] result = new ComparableClass[ints.length];

		for (int i = 0; i < ints.length; ++i)
			result[i] = new ComparableClass(ints[i]);

		return result;
	}
}
