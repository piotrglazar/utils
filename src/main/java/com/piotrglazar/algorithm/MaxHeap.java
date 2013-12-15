package com.piotrglazar.algorithm;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Preconditions;

public class MaxHeap<E> implements Heap<E> {

	private ArrayList<E> array = new ArrayList<E>();

	private Comparator<? super E> comparator;

	private int heapSize = 0;

	private Map<E, Integer> map;

	public MaxHeap(Comparator<? super E> comparator) {
		map = new HashMap<>();
		this.comparator = comparator;
	}

	public static <F> MaxHeap<F> buildHeap(Collection<F> collection, Comparator<? super F> comparator) {
		MaxHeap<F> maxHeap = new MaxHeap<F>(comparator);
		maxHeap.addAll(collection);
		return maxHeap;
	}

	@Override
	public int size() {
		return heapSize;
	}

	@Override
	public boolean isEmpty() {
		return heapSize == 0;
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public Iterator<E> iterator() {
		return array.iterator();
	}

	@Override
	public Object[] toArray() {
		return array.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return array.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		if (!map.containsKey(o))
			return false;
		int index = map.get(o);
		while (isNotRoot(index)) {
			swapWithParent(index);
			index = parent(index);
		}
		extractMax();
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return map.keySet().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Preconditions.checkArgument(c != null, "Null collection");
		for (E e : c)
			add(e);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Preconditions.checkArgument(c != null, "Null collection");
		for (Object o : c)
			remove(o);
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Preconditions.checkArgument(c != null, "Null collection");
		Set<E> retainingElements = new HashSet<E>(map.keySet());
		retainingElements.retainAll(c);

		clear();

		addAll(retainingElements);
		return true;
	}

	@Override
	public void clear() {
		array.clear();
		heapSize = 0;
		map.clear();
	}

	@Override
	public boolean add(E e) {
		set(e, heapSize + 1);
		++heapSize;
		changeKey(e);
		return true;
	}

	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	public E remove() {
		if (isEmpty())
			throw new NoSuchElementException();
		else
			return extractMax();
	}

	private E extractMax() {
		E max = getElement(1);
		map.remove(max);
		E last = getElement(heapSize);
		set(last, 1);
		array.remove(heapSize - 1);
		--heapSize;
		restoreHeap(1);
		return max;
	}

	private void restoreHeap(int i) {
		int leftSon = leftSon(i);
		int rightSon = rightSon(i);
		int greatestSon = greatestSon(leftSon, rightSon);
		if (greatestSon == 0)
			return;
		if (parentIsSmaller(getElement(greatestSon), greatestSon)) {
			swapWithParent(greatestSon);
			restoreHeap(greatestSon);
		}
	}

	private int greatestSon(int leftSon, int rightSon) {
		E leftSonValue = (leftSon <= heapSize) ? getElement(leftSon) : null;
		E rightSonValue = (rightSon <= heapSize) ? getElement(rightSon) : null;

		if (leftSonValue != null) {
			if (rightSonValue != null) {
				switch (comparator.compare(leftSonValue, rightSonValue)) {
				case -1:
					return rightSon;
				case 0:
					return leftSon;
				case 1:
					return leftSon;
				}
			} else {
				return leftSon;
			}
		}
		return 0;
	}

	private int rightSon(int i) {
		return leftSon(i) + 1;
	}

	private int leftSon(int i) {
		return i << 1;
	}

	@Override
	public E poll() {
		if (isEmpty())
			return null;
		else
			return extractMax();
	}

	@Override
	public E element() {
		if (isEmpty())
			throw new NoSuchElementException();
		return (E) array.get(0);
	}

	@Override
	public E peek() {
		if (isEmpty())
			return null;
		else
			return (E) array.get(0);
	}

	@Override
	public void changeKey(E e) {
		checkState(map.containsKey(e), "Element %s should be present in heap", e);
		int i = map.get(e);
		while (isNotRoot(i) && parentIsSmaller(e, i)) {
			swapWithParent(i);
			i = parent(i);
		}
	}

	private boolean isNotRoot(int i) {
		return i > 1;
	}

	private void swapWithParent(int index) {
		E element = getElement(index);
		int parentIndex = parent(index);
		E parent = getElement(parentIndex);
		set(element, parentIndex);
		set(parent, index);
	}

	private void set(E element, int index) {
		if (index - 1 < array.size())
			array.set(index - 1, element);
		else
			array.add(element);
		map.put(element, index);
	}

	private boolean parentIsSmaller(E element, int elementIndex) {
		int parentIndex = parent(elementIndex);
		E parent = getElement(parentIndex);
		return comparator.compare(parent, element) < 0;
	}

	private int parent(int i) {
		return i >> 1;
	}

	private E getElement(int i) {
		return (E) array.get(i - 1);
	}

}
