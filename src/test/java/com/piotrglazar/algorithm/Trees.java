package com.piotrglazar.algorithm;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Trees {

	public static <T> boolean isHeightConsistent(final Tree234<T> tree) {
		Preconditions.checkArgument(tree != null, "True must not be null");

		try {
			getHeight(tree.getRoot());
			return true;
		} catch (InconsistentHeightException e) {
			return false;
		}
	}

	static <T> int getHeight(final TreeNode234<T> node) throws InconsistentHeightException {
		if (node.isLeaf()) {
			return 0;
		} else {
			List<Integer> heights = Lists.newLinkedList();
			for (TreeNode234<T> son : node.getAllSons()) {
				heights.add(getHeight(son));
			}
			assertAllSonsAreOfEqualHeight(heights);
			return heights.get(0) + 1;
		}
	}

	public static <T> boolean isBstTree(final Tree234<T> tree) {
		Preconditions.checkArgument(tree != null, "Tree must not be null");

		try {
			isBstTree(tree.getRoot(), tree.getComparator());
			return true;
		} catch (BstTreeInvariantVolationException e) {
			return false;
		}
	}

	static <T> T isBstTree(final TreeNode234<T> node, final Comparator<T> comparator) throws BstTreeInvariantVolationException {
		final List<BtreeEntry<T, TreeNode234<T>>> entries = node.getEntries();
		assertAllKeysAreSorted(entries, comparator);
		if (node.isLeaf()) {
			if (entries.isEmpty()) {
				return null;
			}
			return entries.get(entries.size() - 1).getEntry();
		} else {
			for (final BtreeEntry<T, TreeNode234<T>> entry : entries) {
				final T greatestKeyFromSubtree = isBstTree(entry.getSon(), comparator);
				if (!entry.isKeyLessThan(greatestKeyFromSubtree, comparator)) {
					throw new BstTreeInvariantVolationException();
				}
			}
			final List<TreeNode234<T>> sons = node.getAllSons();
			final T greatestKeyFromSubtree = isBstTree(sons.get(sons.size() - 1), comparator);
			if (entries.get(entries.size() - 1).isKeyLessThan(greatestKeyFromSubtree, comparator)) {
				throw new BstTreeInvariantVolationException();
			}
			return greatestKeyFromSubtree;
		}
	}

	private static <T> void assertAllKeysAreSorted(final List<BtreeEntry<T, TreeNode234<T>>> entries, final Comparator<T> comparator)
			throws BstTreeInvariantVolationException {
		if (entries.isEmpty()) {
			return;
		}
		BtreeEntry<T, TreeNode234<T>> previous = entries.get(0);

		for (final BtreeEntry<T, TreeNode234<T>> entry : entries) {
			if (!entry.isKeyLessThanOrEqualTo(previous.getEntry(), comparator)) {
				throw new BstTreeInvariantVolationException();
			}
			previous = entry;
		}
	}

	private static void assertAllSonsAreOfEqualHeight(List<Integer> heights) throws InconsistentHeightException {
		Integer commonHeight = heights.get(0);

		for (Integer height : heights) {
			if (height != commonHeight) {
				throw new InconsistentHeightException();
			}
		}
	}

	private static class BstTreeInvariantVolationException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2472339517058913451L;

	}

	private static class InconsistentHeightException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9161669363619313591L;

	}
}
