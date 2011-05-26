package com.javabi.common.dependency;

import static com.javabi.common.dependency.DependencyFactory.getInstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DependencyRegistry<S> implements Iterable<S> {

	/** The interface to instance map. */
	private final Map<Class<? extends S>, S> interfaceToInstanceMap;

	public DependencyRegistry() {
		interfaceToInstanceMap = new LinkedHashMap<Class<? extends S>, S>();
	}

	public <T extends S, I extends T> void register(Class<T> type, I instance) {
		synchronized (interfaceToInstanceMap) {
			interfaceToInstanceMap.put(type, instance);
		}
	}

	public void setDependencies(IDependencyFactory factory) {
		for (Entry<Class<? extends S>, S> entry : interfaceToInstanceMap.entrySet()) {
			Class<?> key = entry.getKey();
			Object value = entry.getValue();
			factory.set(key, value);
		}
	}

	public void removeDependencies(IDependencyFactory factory) {
		for (Entry<Class<? extends S>, S> entry : interfaceToInstanceMap.entrySet()) {
			Class<?> key = entry.getKey();
			factory.remove(key);
		}
	}

	public void setDependencies() {
		setDependencies(getInstance());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<Class<? extends S>> getInterfaceSet() {
		synchronized (interfaceToInstanceMap) {
			return new LinkedHashSet(interfaceToInstanceMap.keySet());
		}
	}

	public Set<Class<? extends S>> getClassSet() {
		synchronized (interfaceToInstanceMap) {
			return new LinkedHashSet<Class<? extends S>>(interfaceToInstanceMap.keySet());
		}
	}

	public List<S> getInstanceList() {
		synchronized (interfaceToInstanceMap) {
			return new ArrayList<S>(interfaceToInstanceMap.values());
		}
	}

	@Override
	public Iterator<S> iterator() {
		return getInstanceList().iterator();
	}

}
