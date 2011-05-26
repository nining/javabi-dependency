package com.javabi.common.dependency;

public interface IInstanceFactory<I> {

	I newInstance(Object... args);

}
