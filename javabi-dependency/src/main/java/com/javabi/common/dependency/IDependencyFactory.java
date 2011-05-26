package com.javabi.common.dependency;

public interface IDependencyFactory {

	<I> I set(Object key, I instance);

	<I> void setInstanceFactory(Object key, IInstanceFactory<I> instance);

	boolean remove(Object key);

	<I> I get(Object key);

	<I> I get(Object key, I defaultInstance);

	<I> I getInstance(Object key, Object... args);

	int size();

	boolean isEmpty();

	void clear();

}
