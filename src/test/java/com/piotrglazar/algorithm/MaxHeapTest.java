package com.piotrglazar.algorithm;

import static com.piotrglazar.algorithm.Heaps.heapSort;
import static com.piotrglazar.algorithm.Heaps.isMaxHeap;
import static com.piotrglazar.algorithm.MaxHeap.buildHeap;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class MaxHeapTest {

	private Comparator<Integer> integerComparator = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};

	private MaxHeap<Integer> heap;

	@Before
	public void createHeap() {
		heap = new MaxHeap<>(integerComparator);
	}

	@Test
	public void shouldAddElementsToHeap() {
		// given
		Integer[] ints = { 4, 1, 3, 8 };

		// when
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// then
		assertHeap(ints.length);
	}

	private void assertHeap(int length) {
		Integer[] heapInternal = heap.toArray(new Integer[length]);
		assertTrue(String.format("%s is not heap", Arrays.toString(heapInternal)), isMaxHeap(heapInternal));
	}

	@Test
	public void shouldBuildHeapWithDuplicates() {
		// given
		Integer[] ints = { 1, 3, 3, 2 };

		// when
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		assertHeap(ints.length);
	}

	@Test
	public void shouldExtractMaxAndRemainHeap() {
		// given
		Integer[] ints = { 1, 3, 3, 2 };
		int max = 3;

		// when
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);
		assertEquals("Wrong heap size", ints.length, heap.size());
		int extractedMax = heap.poll();

		// then
		assertTrue("Extracted max is wrong", extractedMax == max);
		assertHeap(ints.length);
		assertEquals("Wrong heap size", ints.length - 1, heap.size());
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldFailWhenRemovingFromEmpty() {
		// given

		// when
		heap.remove();

		// then exception
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldFailWhenGettingElementFromEmpty() {
		// given

		// when
		heap.element();

		// then exception
	}

	@Test
	public void shouldRemoveElementFromMiddle() {
		// given
		Integer[] ints = { 1, 3, 5, 7, 6 };
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		heap.remove(6);

		// then
		assertHeap(ints.length);
	}

	@Test
	public void shouldKeepRemovingFourthFromMiddle() {
		// given
		Integer[] ints = randomInts(15);
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		while (heap.size() >= 4) {
			Integer[] heapInternal = heap.toArray(new Integer[ints.length]);

			// when
			heap.remove(heapInternal[3]);

			// then
			assertHeap(ints.length);
		}
		assertHeap(ints.length);
	}

	@Test
	public void shouldIncreaseKeyForReferenceValue() {
		// given
		ComparableClass[] refs = ComparableClass.fromInts(new Integer[] { 3, 2, 5, 9, 4 });
		MaxHeap<ComparableClass> heap = new MaxHeap<>(ComparableClass.getComparator());
		for (int i = 0; i < refs.length; ++i)
			heap.add(refs[i]);
		refs = heap.toArray(refs);

		// when
		ComparableClass lastOne = refs[refs.length - 1];
		lastOne.setKey(10);
		heap.changeKey(lastOne);

		// then
		refs = heap.toArray(refs);
		assertTrue("Value is not at the top of heap", lastOne == refs[0]);
	}

	@Test
	public void shouldHeapSort() {
		// given
		Integer[] ints = randomInts(10);

		// when
		List<Integer> sorted = heapSort(Arrays.asList(ints), integerComparator);

		// then
		assertTrue("Array is not sorted", isArraySorted(sorted.toArray(new Integer[ints.length])));
	}

	@Test
	public void shouldBuildMaxHeapFromCollection() {
		// given
		List<Integer> ints = Arrays.asList(randomInts());

		// when
		heap = buildHeap(ints, integerComparator);

		// then
		assertHeap(ints.size());
	}

	@Test
	public void shouldContainValueInHeap() {
		// given
		Integer[] ints = randomInts();
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		boolean contains = heap.contains(ints[0]);

		// then
		assertTrue("Heap does not contain element", contains);
	}

	@Test
	public void shouldIteratorReturnHeap() {
		// given
		Integer[] ints = randomInts();
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		Integer[] heapInts = new Integer[ints.length];
		Iterator<Integer> i = heap.iterator();
		int j = 0;
		while (i.hasNext()) {
			heapInts[j++] = i.next();
		}

		// then
		assertTrue("Iterator did not return heap", isMaxHeap(heapInts));
	}

	@Test
	public void shouldContainsAll() {
		// given
		Integer[] ints = randomInts();
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		boolean containsAll = heap.containsAll(Arrays.asList(ints));

		// then
		assertTrue("Heap does not contain elements used to its creation", containsAll);
	}

	@Test
	public void shouldRemoveAllAndRemainHeap() {
		// given
		Integer[] ints = randomInts();
		Integer[] firstHalf = Arrays.copyOfRange(ints, 0, ints.length / 2);
		Integer[] secondHalf = Arrays.copyOfRange(ints, ints.length / 2, ints.length);
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		heap.removeAll(asList(firstHalf));

		// then
		assertHeap(secondHalf.length);
		assertEquals("Heap size does not match", secondHalf.length, heap.size());
		assertTrue("Heap should contain elements", heap.containsAll(asList(secondHalf)));
	}

	@Test
	public void shouldRetainAllAndRemainHeap() {
		// given
		Integer[] ints = randomInts();
		Integer[] firstHalf = Arrays.copyOfRange(ints, 0, ints.length / 2);
		for (int i = 0; i < ints.length; ++i)
			heap.add(ints[i]);

		// when
		heap.retainAll(asList(firstHalf));

		// then
		assertHeap(firstHalf.length);
		assertEquals("Heap size does not match", firstHalf.length, heap.size());
		assertTrue("Heap should contain elements", heap.containsAll(asList(firstHalf)));
	}

	private Integer[] randomInts() {
		return randomInts(5);
	}

	private Integer[] randomInts(int toGenerate) {
		int remaining = toGenerate;
		Set<Integer> generatedSoFar = new TreeSet<>();
		while (remaining > 0) {
			int random = (int) ((Math.random() * 1000) % 100);
			if (!generatedSoFar.contains(random)) {
				generatedSoFar.add(random);
				--remaining;
			}
		}
		return generatedSoFar.toArray(new Integer[toGenerate]);
	}

	public boolean isArraySorted(Integer[] array) {
		Integer previous = array[0];
		for (int i = 1; i < array.length; ++i) {
			Integer current = array[i];
			if (current > previous)
				return false;
			previous = current;
		}
		return true;
	}

	@Test
	public void shouldBuildHeap100Times() {
		for (int i = 0; i < 100; ++i) {
			// given
			Integer[] ints = randomInts();

			// when
			for (int j = 0; j < ints.length; ++j)
				heap.add(ints[j]);

			assertHeap(ints.length);
		}
	}
}
