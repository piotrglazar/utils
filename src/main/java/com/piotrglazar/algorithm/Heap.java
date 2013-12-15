package com.piotrglazar.algorithm;

import java.util.Queue;

public interface Heap<E> extends Queue<E> {

	void changeKey(E e);
}
