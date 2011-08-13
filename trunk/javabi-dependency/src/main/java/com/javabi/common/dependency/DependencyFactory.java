package com.javabi.common.dependency;

import org.mockito.Mockito;

public class DependencyFactory {

	private static volatile IDependencyFactory globalFactory = new SimpleDependencyFactory();
	private static volatile ThreadLocal<IDependencyFactory> localFactories = new ThreadLocal<IDependencyFactory>();

	public static void clearDependencies() {
		globalFactory.clear();
		localFactories = new ThreadLocal<IDependencyFactory>();
	}

	public static IDependencyFactory getInstance() {
		IDependencyFactory localFactory = localFactories.get();
		if (localFactory != null) {
			return localFactory;
		}
		return globalFactory;
	}

	public static IDependencyFactory getLocalInstance() {
		return localFactories.get();
	}

	public static IDependencyFactory getGlobalInstance() {
		return globalFactory;
	}

	public static void setGlobalInstance(IDependencyFactory factory) {
		// There must always be a global instance!
		if (factory == null) {
			throw new NullPointerException();
		}
		globalFactory = factory;
	}

	public static void setLocalInstance(IDependencyFactory factory) {
		// Set to null to remove the local instance if required
		localFactories.set(factory);
	}

	@SuppressWarnings("unchecked")
	public static <I> I getDependency(Class<I> clazz) {
		return (I) getInstance().get(clazz);
	}

	public static <I> I getDependency(Class<I> clazz, I defaultInstance) {
		return getInstance().get(clazz, defaultInstance);
	}

	public static <I> I setDependency(Class<I> clazz, I instance) {
		return getInstance().set(clazz, instance);
	}

	@SuppressWarnings("unchecked")
	public static <I> I getDependencyInstance(Class<I> clazz, Object... args) {
		return (I) getInstance().getInstance(clazz, args);
	}

	public static <I> void setDependencyInstanceFactory(Class<I> clazz, IInstanceFactory<I> factory) {
		getInstance().setInstanceFactory(clazz, factory);
	}

	public static <I> I setMockDependency(Class<I> clazz) {
		I instance = Mockito.mock(clazz);
		getInstance().set(clazz, instance);
		return instance;
	}

}
