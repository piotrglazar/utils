package com.piotrglazar.algorithm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TreeNode234<T> extends BtreeNode<T, TreeNode234<T>> {

	private boolean isLeaf;

	private List<BtreeEntry<T, TreeNode234<T>>> entries = new LinkedList<>();

	public TreeNode234() {
		isLeaf = true;
		entries.add(BtreeEntry.<T, TreeNode234<T>> sentinel());
	}

	public TreeNode234(TreeNode234<T> son) {
		this();
		entries.get(0).setSon(son);
	}

	@Override
	protected BtreeEntry<T, TreeNode234<T>> search(T element, Comparator<T> comparator) {
		BtreeEntry<T, TreeNode234<T>> n = findEntryWithGreaterKey(element, comparator);

		if (isExactKeyMatch(element, comparator, n)) {
			return n;
		}

		if (isLeaf) {
			return null;
		} else {
			return n.getSon().search(element, comparator);
		}
	}

	boolean isExactKeyMatch(T element, Comparator<T> comparator, BtreeEntry<T, TreeNode234<T>> n) {
		return n != null && n.isKeyEqualTo(element, comparator);
	}

	protected BtreeEntry<T, TreeNode234<T>> findEntryWithGreaterKey(T element, Comparator<T> comparator) {
		BtreeEntry<T, TreeNode234<T>> result = null;
		for (BtreeEntry<T, TreeNode234<T>> entry : entries) {
			if (entry.isKeyLessThanOrEqualTo(element, comparator)) {
				result = entry;
				break;
			}
		}

		return result;
	}

	protected void insert(T element) {
		entries.add(0, new BtreeEntry<T, TreeNode234<T>>(element));
	}

	public int size() {
		// don't count the sentinel
		return entries.size() - 1;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	protected BtreeEntry<T, TreeNode234<T>> toBtreeEntry() {
		Preconditions.checkState(size() == 1, "Cannot convert to BtreeEntry when there are multiple keys");
		return entries.get(0);
	}

	protected void addSon(TreeNode234<T> node, Comparator<T> comparator) {
		addEntry(node.toBtreeEntry(), comparator);
	}

	@Override
	protected void bTreeInsertNonFull(T element, Comparator<T> comparator) {
		if (isLeaf) {
			addEntry(new BtreeEntry<T, TreeNode234<T>>(element), comparator);
		} else {
			TreeNode234<T> son = findFirstEntryWithGreaterOrEqualKey(element, comparator).getSon();
			if (son.isFull()) {
				bTreeSplitChild(son, comparator);
			}
			// it might change after splitting, so recalculate
			son = findFirstEntryWithGreaterOrEqualKey(element, comparator).getSon();
			son.bTreeInsertNonFull(element, comparator);
		}
	}

	private BtreeEntry<T, TreeNode234<T>> findFirstEntryWithGreaterOrEqualKey(T element, Comparator<T> comparator) {
		for (BtreeEntry<T, TreeNode234<T>> entry : entries) {
			if (entry.isKeyLessThanOrEqualTo(element, comparator)) {
				return entry;
			}
		}

		// the sentinel will not allow to get here
		throw new IllegalStateException("There is something wrong");
	}

	protected void addEntry(BtreeEntry<T, TreeNode234<T>> toBeInserted, Comparator<T> comparator) {
		ListIterator<BtreeEntry<T, TreeNode234<T>>> nodes = entries.listIterator();

		while (nodes.hasNext()) {
			BtreeEntry<T, TreeNode234<T>> current = nodes.next();
			if (current.isKeyLessThanOrEqualTo(toBeInserted.getEntry(), comparator)) {
				nodes.previous();
				nodes.add(toBeInserted);
				return;
			}
		}
	}

	@Override
	protected void bTreeSplitChild(TreeNode234<T> child, Comparator<T> comparator) {
		Preconditions.checkState(child.isFull(), "Child must be full");
		// child must be really a child :)

		TreeNode234<T> newChild = newChild(child);

		TreeNode234<T> middleSon = child.getSon(child.getMiddleIndex());

		BtreeEntry<T, TreeNode234<T>> middle = insertMiddleItem(child, comparator);

		child.keepLeftHalf(middleSon);

		addNewChildPointerNextTo(middle, newChild, comparator);
	}

	private TreeNode234<T> newChild(TreeNode234<T> child) {
		TreeNode234<T> newSibling = new TreeNode234<>();
		newSibling.setLeaf(child.isLeaf);
		newSibling.setEntries(child.getRightHalf());
		return newSibling;
	}

	List<TreeNode234<T>> getAllSons() {
		List<TreeNode234<T>> result = Lists.newLinkedList();

		for (BtreeEntry<T, TreeNode234<T>> entry : entries) {
			TreeNode234<T> son = entry.getSon();
			if (son != null) {
				result.add(son);
			}
		}

		return result;
	}

	protected void addNewChildPointerNextTo(BtreeEntry<T, TreeNode234<T>> middle, TreeNode234<T> newChild, Comparator<T> comparator) {
		ListIterator<BtreeEntry<T, TreeNode234<T>>> i = entries.listIterator();

		while (i.hasNext()) {
			if (i.next().equals(middle)) {
				break;
			}
		}

		BtreeEntry<T, TreeNode234<T>> entry = i.next();
		entry.setSon(newChild);
	}

	private void keepLeftHalf(TreeNode234<T> middleSon) {
		BtreeEntry<T, TreeNode234<T>> sentinel = BtreeEntry.<T, TreeNode234<T>> sentinel();
		sentinel.setSon(middleSon);
		entries = new LinkedList<>(entries.subList(0, getMiddleIndex()));
		entries.add(sentinel);
	}

	private BtreeEntry<T, TreeNode234<T>> insertMiddleItem(TreeNode234<T> child, Comparator<T> comparator) {
		BtreeEntry<T, TreeNode234<T>> middle = child.entries.get(child.getMiddleIndex());
		middle.setSon(child);
		addEntry(middle, comparator);
		return middle;
	}

	private List<BtreeEntry<T, TreeNode234<T>>> getRightHalf() {
		int middleIndex = getMiddleIndex();
		return new LinkedList<>(entries.subList(middleIndex + 1, entries.size()));
	}

	private int getMiddleIndex() {
		int middleIndex = (entries.size() - 1) / 2;
		return middleIndex;
	}

	public List<BtreeEntry<T, TreeNode234<T>>> getEntries() {
		return ImmutableList.copyOf(entries.subList(0, entries.size() - 1));
	}

	protected void setEntries(List<BtreeEntry<T, TreeNode234<T>>> entries) {
		this.entries = entries;
	}

	protected TreeNode234<T> getSon(int i) {
		Preconditions.checkArgument(i >= 0 && i < entries.size(), "Invalid index");
		return entries.get(i).getSon();
	}

	protected boolean isFull() {
		return size() == 2 * Tree234.t - 1;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	protected boolean isEmpty() {
		// there's always a sentinel
		return entries.size() == 1;
	}

}
