package com.piotrglazar.algorithm;

import java.util.Comparator;

public class BtreeEntry<T, U> {

	public static <K, V> BtreeEntry<K, V> sentinel() {
		return new Sentinel<>();
	}

	private T entry;

	private U son;

	public BtreeEntry(T element) {
		entry = element;
	}

	public BtreeEntry() {

	}

	public boolean isKeyLessThanOrEqualTo(T key, Comparator<T> comparator) {
		return comparator.compare(key, entry) <= 0;
	}

	public boolean isKeyEqualTo(T key, Comparator<T> comparator) {
		return comparator.compare(key, entry) == 0;
	}

	public boolean isKeyLessThan(T key, Comparator<T> comparator) {
		return comparator.compare(key, entry) < 0;
	}

	public T getEntry() {
		return entry;
	}

	public U getSon() {
		return son;
	}

	public void setSon(U son) {
		this.son = son;
	}

	public void setEntry(T entry) {
		this.entry = entry;
	}
	
	static class Sentinel<T, U> extends BtreeEntry<T, U> {

		@Override
		public boolean isKeyLessThanOrEqualTo(T element, Comparator<T> comparator) {
			return true;
		}

		@Override
		public boolean isKeyEqualTo(T element, Comparator<T> comparator) {
			return false;
		}

		@Override
		public String toString() {
			return String.format("Entry sentinel son %s", System.identityHashCode(getSon()));
		}
	}

	@Override
	public String toString() {
		return String.format("Entry %s son %s", entry, System.identityHashCode(son));
	}
}
