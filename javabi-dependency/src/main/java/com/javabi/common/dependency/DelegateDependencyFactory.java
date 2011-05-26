package com.javabi.common.dependency;

import static com.javabi.common.dependency.DependencyFactory.getGlobalInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegateDependencyFactory extends SimpleDependencyFactory {

	private static final Logger log = LoggerFactory.getLogger(DelegateDependencyFactory.class);

	private final IDependencyFactory delegate;
	private final Map<Object, Object> keyToObjectMap = new ConcurrentHashMap<Object, Object>();

	public DelegateDependencyFactory(IDependencyFactory delegate) {
		if (delegate == null) {
			throw new NullPointerException("delegate");
		}
		this.delegate = delegate;
	}

	public DelegateDependencyFactory() {
		this(getGlobalInstance());
	}

	@Override
	public void clear() {
		keyToObjectMap.clear();
	}

	@Override
	public <I> I set(Object key, I instance) {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (instance == null) {
			throw new NullPointerException("instance");
		}

		if (keyToObjectMap.put(key, instance) == null) {
			log.debug("Registered Dependency #" + keyToObjectMap.size() + ": '" + key + "' -> " + instance);
		} else {
			log.warn("Replaced Dependency: '" + key + "' -> " + instance);
		}
		return instance;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I> I get(Object key) {
		if (key == null) {
			throw new NullPointerException("key");
		}

		I instance = (I) keyToObjectMap.get(key);
		if (instance == null) {
			return (I) delegate.get(key);
		}
		if (instance instanceof IInstanceFactory) {
			IInstanceFactory<I> factory = (IInstanceFactory<I>) instance;
			return factory.newInstance();
		}
		return instance;
	}

	@Override
	public <I> void setInstanceFactory(Object key, IInstanceFactory<I> factory) {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (factory == null) {
			throw new NullPointerException("factory");
		}

		if (keyToObjectMap.put(key, factory) == null) {
			log.debug("Registered Dependency: '" + key + "' -> " + factory);
		} else {
			log.warn("Replaced Dependency: '" + key + "' -> " + factory);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I> I getInstance(Object key, Object... args) {
		if (key == null) {
			throw new NullPointerException("key");
		}

		I instance = (I) keyToObjectMap.get(key);
		if (instance == null) {
			return (I) delegate.get(key, args);
		}
		if (!(instance instanceof IInstanceFactory)) {
			throw new IllegalArgumentException("Dependency not a factory: '" + key + "'");
		}
		IInstanceFactory<I> factory = (IInstanceFactory<I>) instance;
		return factory.newInstance(args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I> I get(Object key, I defaultInstance) {
		if (key == null) {
			throw new NullPointerException("key");
		}

		I instance = (I) keyToObjectMap.get(key);
		if (instance == null) {
			return delegate.get(key, defaultInstance);
		}
		if (instance instanceof IInstanceFactory) {
			IInstanceFactory<I> factory = (IInstanceFactory<I>) instance;
			return factory.newInstance();
		}
		return instance;
	}

	@Override
	public boolean remove(Object key) {
		return keyToObjectMap.remove(key) != null;
	}

	@Override
	public String toString() {
		return "this=" + keyToObjectMap + ",delegate=" + delegate;
	}

}
