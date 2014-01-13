package com.piotrglazar.algorithm;

import static com.piotrglazar.algorithm.TestUtils.integerComparator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TreesTest {

	@Test
	public void shouldAcceptEmptyTree() {
		// given
		final Tree234<Integer> tree = new Tree234<>(TestUtils.integerComparator);

		// when
		final boolean result = Trees.isHeightConsistent(tree);

		// then
		assertTrue("Empty tree must have consistent height", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAcceptTreeWithTwoSonsOnTheSameLevel() {
		// given
		final Tree234<Integer> tree = tree(treeNode(0, treeNode(1), treeNode(2)));

		// when
		final boolean result = Trees.isHeightConsistent(tree);

		// then
		assertTrue("Root with two sons must have consistent height", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotAcceptTreeWithOneSubtreeGreater() {
		// given
		final Tree234<Integer> tree = tree(treeNode(0, treeNode(1, treeNode(2)), treeNode(3)));

		// when
		final boolean result = Trees.isHeightConsistent(tree);

		// then
		assertFalse("Root with one son must have inconsistent height", result);
	}

	@Test
	public void shouldEmptyTreeBeBstTree() {
		// given
		final Tree234<Integer> tree = new Tree234<>(integerComparator);

		// when
		final boolean result = Trees.isBstTree(tree);

		// then
		assertTrue("Empty tree is sorted", result);
	}

	@Test
	public void shouldSortedRootBeBstTree() {
		// given
		final Tree234<Integer> tree = tree(treeRoot(3, 5, 10));

		// when
		final boolean result = Trees.isBstTree(tree);

		// then
		assertTrue("Root is unsorted but is should be", result);
	}

	@Test
	public void shouldUnsortedRootNotBeBstTree() {
		// given
		final Tree234<Integer> tree = tree(treeRoot(2, 1));

		// when
		final boolean result = Trees.isBstTree(tree);

		// then
		assertFalse("Root is sorted but it shouldn't be", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldDetectLeftSonBstViolation() {
		// given
		final Tree234<Integer> tree = tree(treeNode(1, treeNode(4)));

		// when
		final boolean result = Trees.isBstTree(tree);

		// then
		assertFalse("Son should violate BST tree invariant", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldDetectMostRightSonBstViolation() {
		// given
		final Tree234<Integer> tree = tree(treeNode(1, treeNode(0), treeNode(0)));

		// when
		final boolean result = Trees.isBstTree(tree);

		// then
		assertFalse("Son should violate BST tree invariant", result);
	}

	@SuppressWarnings("unchecked")
	private TreeNode234<Integer> treeRoot(final Integer... keys) {
		final TreeNode234<Integer> node = mock(TreeNode234.class);
		final List<BtreeEntry<Integer, TreeNode234<Integer>>> entries = Lists.newArrayList();

		for (int i = 0; i < keys.length; ++i) {
			entries.add(new BtreeEntry<Integer, TreeNode234<Integer>>(keys[i]));
		}

		when(node.isLeaf()).thenReturn(true);
		when(node.getEntries()).thenReturn(entries);

		return node;
	}

	@SuppressWarnings("unchecked")
	private Tree234<Integer> tree(final TreeNode234<Integer> root) {
		final Tree234<Integer> tree = mock(Tree234.class);

		when(tree.getRoot()).thenReturn(root);
		when(tree.getComparator()).thenReturn(integerComparator);

		return tree;
	}

	@SuppressWarnings("unchecked")
	private <T> TreeNode234<T> treeNode(final T key, final TreeNode234<T>... sons) {
		final BtreeEntry<T, TreeNode234<T>> entry = new BtreeEntry<>(key);
		final TreeNode234<T> node = mock(TreeNode234.class);

		when(node.getEntries()).thenReturn(Arrays.asList(entry));

		if (sons.length > 0) {
			when(node.isLeaf()).thenReturn(false);
			when(node.getAllSons()).thenReturn(Arrays.asList(sons));
			entry.setSon(sons[0]);
		} else {
			when(node.isLeaf()).thenReturn(true);
		}

		return node;
	}
}
