package com.piotrglazar.algorithm;

import static com.piotrglazar.algorithm.TestUtils.integerComparator;
import static com.piotrglazar.algorithm.Trees.isBstTree;
import static com.piotrglazar.algorithm.Trees.isHeightConsistent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class Tree234Test {

	@Test(expected = IllegalStateException.class)
	public void shouldFailWhenSearchingInEmptyTree() {
		// given
		Tree234<Object> tree = new Tree234<>(null);

		// when
		tree.search(new Object());

		// then exception
	}

	@Test
	public void shouldAddThreeItemsToRoot() {
		// given
		final Tree234<Integer> tree = new Tree234<>(integerComparator);

		// when
		for (final Integer item : new Integer[] { 8, 2, 5 }) {
			tree.insert(item);
		}

		// then
		assertBtreeInvariants(tree);
	}

	@Test
	public void shouldAddAndSearchThreeItemsInRoot() {
		// given
		final Integer[] keys = { 8, 2, 5 };
		final Tree234<Integer> tree = new Tree234<>(integerComparator);

		for (final Integer item : keys) {
			tree.insert(item);
		}

		// when
		final List<Integer> searchResult = searchKeysInTree(keys, tree);
		final Optional<Integer> notExistingInTree = tree.search(0);

		// then
		assertEquals("Should find every key", Arrays.asList(keys), searchResult);
		assertFalse("There should be no such key", notExistingInTree.isPresent());
		assertBtreeInvariants(tree);
	}

	@Test
	public void shouldAddFourItemsToRoot() {
		// given
		final Tree234<Integer> tree = new Tree234<>(integerComparator);

		// when
		for (final Integer key : new Integer[] { 4, 8, 10, 1 }) {
			tree.insert(key);
		}

		// then
		assertBtreeInvariants(tree);
	}

	@Test
	public void shouldAddAndSearchSortedSequence() {
		// given
		final Integer[] keys = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		final Tree234<Integer> tree = new Tree234<>(integerComparator);

		// when
		for (final Integer key : keys) {
			tree.insert(key);
		}
		List<Integer> searchResult = searchKeysInTree(keys, tree);

		// then
		assertBtreeInvariants(tree);
		assertEquals("Should find every key", Arrays.asList(keys), searchResult);
	}

	private void assertBtreeInvariants(final Tree234<Integer> tree) {
		assertTrue("Tree should be of equal hight", isHeightConsistent(tree));
		assertTrue("Tree should be BST tree", isBstTree(tree));
	}

	private List<Integer> searchKeysInTree(final Integer[] keys, final Tree234<Integer> tree) {
		final List<Integer> result = Lists.newArrayList();

		for (final Integer key : keys) {
			final Optional<Integer> searchResult = tree.search(key);
			result.add(searchResult.get());
		}

		return result;
	}
}
