package com.piotrglazar.algorithm;

import java.util.Comparator;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public abstract class AbstractBtree<T, N extends BtreeNode<T, N>> implements Btree<T> {

	protected N root;

	protected Comparator<T> comparator;

	protected AbstractBtree(final Comparator<T> comparator) {
		this.comparator = comparator;
		root = rootProvider(null);
	}

	protected abstract N rootProvider(final N oldRoot);

	@Override
	public Optional<T> search(T element) {
		Preconditions.checkState(!root.isEmpty(), "Root must not be empty");
		BtreeEntry<T, ? extends BtreeNode<T, ?>> item = root.search(element, comparator);
		if (item != null) {
			return Optional.of(item.getEntry());
		} else {
			return Optional.absent();
		}
	}

	@Override
	public void insert(T element) {
		N node = root;
		if (node.isFull()) {
			root = rootProvider(node);
			root.bTreeSplitChild(node, comparator);
			root.bTreeInsertNonFull(element, comparator);
		} else {
			node.bTreeInsertNonFull(element, comparator);
		}
	}

}
