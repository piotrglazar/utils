package com.piotrglazar.algorithm;

import static com.piotrglazar.algorithm.TestUtils.integerComparator;
import static java.lang.Integer.valueOf;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TreeNode234Test {

	@Test
	public void shouldConvertToBtreeEntry() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(5);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.toBtreeEntry();

		// then
		assertEquals("Returned entry should contain proper value", valueOf(5), entry.getEntry());
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailWhenConvertingEmptyNode() {
		// given
		TreeNode234<Integer> node = new TreeNode234<>();

		// when
		node.toBtreeEntry();

		// then exception
	}

	@Test
	public void shouldAddSonToEmptyNode() {
		// given
		TreeNode234<Integer> node = new TreeNode234<>();
		TreeNode234<Integer> son = treeNodeWithKeys(5);

		// when
		node.addSon(son, integerComparator);

		// then
		assertEquals("Node should have one son", 1, node.size());
		assertEquals("Entry should contain proper value", valueOf(5), node.getEntries().get(0).getEntry());
	}

	@Test
	public void shouldInsertSonToAlmostFullNode() {
		Integer[] values = { 1, 5, 9 };
		for (Integer i : values) {
			// given
			TreeNode234<Integer> node = treeNodeWithKeys(3, 8);
			TreeNode234<Integer> son = createSonWithKey(i);

			// when
			node.addSon(son, integerComparator);

			// then
			assertEquals("Son should be added", 3, node.getEntries().size());
			assertTrue("Node list should be sorted", isListSorted(node.getEntries()));
		}
	}

	@Test
	public void shouldNotFindEntryWithLesserKey() {
		// given
		TreeNode234<Integer> node = new TreeNode234<>();

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.findEntryWithGreaterKey(5, integerComparator);

		// then
		assertTrue("Should find no entry", entry instanceof BtreeEntry.Sentinel);
	}

	@Test
	public void shouldFindFirstEntry() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.findEntryWithGreaterKey(1, integerComparator);

		// then
		assertEquals("Should find first entry", valueOf(1), entry.getEntry());
	}

	@Test
	public void shouldFindSecondEntry() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.findEntryWithGreaterKey(2, integerComparator);

		// then
		assertTrue("Should find second entry", 2 < entry.getEntry());
	}

	@Test
	public void shouldFindThirdEntry() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.findEntryWithGreaterKey(6, integerComparator);

		// then
		assertTrue("Should find third entry", 6 < entry.getEntry());
	}

	@Test
	public void shouldGoBeyondThirdEntry() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.findEntryWithGreaterKey(16, integerComparator);

		// then
		assertTrue("Should find no entry", entry instanceof BtreeEntry.Sentinel);
	}

	@Test
	public void shouldSearchAndFindExactMatch() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.search(3, integerComparator);

		// then
		assertEquals("Should find value", valueOf(3), entry.getEntry());
	}

	@Test
	public void shouldReturnNullForSearchingNonExistingValueInLeaf() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(1, 3, 8);

		// when
		BtreeEntry<Integer, TreeNode234<Integer>> entry = node.search(20, integerComparator);

		// then
		assertNull("Should return null value", entry);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGoDeepInSonWhileSearching() {
		// given
		TreeNode234<Integer> node = new TreeNode234<>();
		BtreeEntry<Integer, TreeNode234<Integer>> entry = mock(BtreeEntry.class);
		TreeNode234<Integer> son = mock(TreeNode234.class);
		node.setLeaf(false);
		node.setEntries(singletonList(entry));
		Integer value = 3;
		when(entry.getEntry()).thenReturn(5);
		when(entry.isKeyLessThanOrEqualTo(value, integerComparator)).thenReturn(true);
		when(entry.getSon()).thenReturn(son);

		// when
		node.search(value, integerComparator);

		// then
		verify(son).search(value, integerComparator);
	}

	@Test
	public void shouldSplitChild() {
		// given
		TreeNode234<Integer> son = treeNodeWithKeys(1, 3, 8);
		TreeNode234<Integer> parent = treeNodeWithKeyAndSon(10, son);

		// when
		parent.bTreeSplitChild(son, integerComparator);

		// then
		assertEquals("Parent must have 2 children", 2, parent.getEntries().size());
		assertEquals("Child must contain 1 key", 1, son.getEntries().size());
	}

	@Test
	public void shouldInsertIntoLeaf() {
		// given
		TreeNode234<Integer> node = treeNodeWithKeys(3, 8);

		// when
		node.bTreeInsertNonFull(5, integerComparator);

		// then
		assertTrue("New item should be added in order", isListSorted(node.getEntries()));
	}

	@Test
	public void shouldInsertIntoSon() {
		// given
		TreeNode234<Integer> son = treeNodeWithKeys(3, 8);
		TreeNode234<Integer> parent = treeNodeWithKeyAndSon(10, son);

		// when
		parent.bTreeInsertNonFull(2, integerComparator);

		// then
		assertEquals("Son should contain 3 entries", 3, son.size());
		assertEquals("Parent should contain 1 entry", 1, parent.size());
		assertTrue("Son should be properly ordered", isListSorted(son.getEntries()));
	}

	private TreeNode234<Integer> treeNodeWithKeyAndSon(int key, TreeNode234<Integer> son) {
		TreeNode234<Integer> parent = new TreeNode234<>();
		parent.setLeaf(false);
		BtreeEntry<Integer, TreeNode234<Integer>> parentEntry = new BtreeEntry<>(key);
		parentEntry.setSon(son);
		parent.addEntry(parentEntry, integerComparator);
		return parent;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldInsertIntoParentAndSplitChild() {
		// given
		TreeNode234<Integer> leftSon = treeNodeWithKeys(1, 3, 8);
		TreeNode234<Integer> rightSon = createSonWithKey(11);
		TreeNode234<Integer> parent = new TreeNode234<>();
		parent.setLeaf(false);
		BtreeEntry<Integer, TreeNode234<Integer>> parentEntry = new BtreeEntry<>(10);
		parentEntry.setSon(leftSon);
		BtreeEntry<Integer, TreeNode234<Integer>> parentSentinelEntry = BtreeEntry.sentinel();
		parentSentinelEntry.setSon(rightSon);
		parent.setEntries(Lists.newArrayList(parentEntry, parentSentinelEntry));
		Integer element = 5;

		// when
		parent.bTreeInsertNonFull(element, integerComparator);

		// then
		assertEquals("Son should contain 1 entries", 1, leftSon.size());
		assertEquals("Parent should contain 2 entries", 2, parent.size());
		assertTrue("Son should be properly ordered", isListSorted(leftSon.getEntries()));
		assertTrue("Parent should be properly ordered", isListSorted(parent.getEntries()));
		assertTrue("New son should be properly ordered", isListSorted(parent.getSon(1).getEntries()));
		assertEquals("New son should contain 2 entries", 2, parent.getSon(1).size());
	}

	private boolean isListSorted(List<BtreeEntry<Integer, TreeNode234<Integer>>> entries) {
		Integer previous = Integer.MIN_VALUE;

		for (BtreeEntry<Integer, TreeNode234<Integer>> entry : entries) {
			Integer value = entry.getEntry();
			if (previous > value) {
				return false;
			}
			previous = value;
		}
		return true;
	}

	private TreeNode234<Integer> createSonWithKey(Integer i) {
		TreeNode234<Integer> son = new TreeNode234<>();
		son.insert(i);
		return son;
	}

	private TreeNode234<Integer> treeNodeWithKeys(Integer... keys) {
		List<Integer> keyList = Arrays.asList(keys);
		Collections.sort(keyList);

		TreeNode234<Integer> node = new TreeNode234<>();
		for (Integer key : Lists.reverse(keyList)) {
			node.insert(key);
		}
		return node;
	}

}
