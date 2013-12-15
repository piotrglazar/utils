package com.piotrglazar.algorithm;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

public class Heaps {

	public static boolean isMaxHeap(Integer[] array) {
		for (int i = 0; i < array.length / 2; ++i) {
			if (!isParentGreaterThanSons(i, array))
				return false;
		}
		return true;
	}

	private static boolean isParentGreaterThanSons(int i, Integer[] array) {
		int parent = i + 1;
		int leftSon = parent << 1;
		int rightSon = leftSon + 1;
		return isParentGreaterThanSon(parent, leftSon, array) && isParentGreaterThanSon(parent, rightSon, array);
	}

	private static boolean isParentGreaterThanSon(int parent, int son, Integer[] array) {
		if (son < array.length) {
			Integer parentValue = array[parent - 1];
			Integer sonValue = array[son - 1];
			if (sonValue == null)
				return true;
			else
				return parentValue >= sonValue;
		} else {
			return true;
		}
	}

	public static <E> List<E> heapSort(List<E> elements, Comparator<E> comparator) {
		List<E> sorted = Lists.newLinkedList();
		MaxHeap<E> heap = new MaxHeap<>(comparator);
		heap.addAll(elements);

		while (!heap.isEmpty()) {
			E currentMax = heap.remove();
			sorted.add(currentMax);
		}

		return sorted;
	}
}
