package com.piotrglazar.algorithm;

import java.util.Comparator;

public abstract class BtreeNode<T, N extends BtreeNode<T, N>> {

	protected abstract BtreeEntry<T, N> search(T element, Comparator<T> comparator);

	protected abstract void bTreeInsertNonFull(T element, Comparator<T> comparator);

	protected abstract void bTreeSplitChild(N child, Comparator<T> comparator);

	protected abstract boolean isFull();

	protected abstract boolean isEmpty();
}
