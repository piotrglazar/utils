package com.piotrglazar.algorithm;

import java.util.Comparator;

public class TestUtils {
	public static Comparator<Integer> integerComparator = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};
}
