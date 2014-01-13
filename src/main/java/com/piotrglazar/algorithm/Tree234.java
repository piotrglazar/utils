package com.piotrglazar.algorithm;

import java.util.Comparator;

public class Tree234<E> extends AbstractBtree<E, TreeNode234<E>> {

	public static final int t = 2;

	public Tree234(Comparator<E> comparator) {
		super(comparator);
	}

	protected TreeNode234<E> getRoot() {
		return root;
	}

	protected Comparator<E> getComparator() {
		return comparator;
	}

	@Override
	protected TreeNode234<E> rootProvider(final TreeNode234<E> oldRoot) {
		if (oldRoot == null) {
			return new TreeNode234<>();
		} else {
			TreeNode234<E> root = new TreeNode234<>(oldRoot);
			root.setLeaf(false);
			return root;
		}
	}

}
