package com.piotrglazar.algorithm;

import com.google.common.base.Optional;

public interface Btree<E> {

	Optional<E> search(E element);

	void insert(E element);
}
