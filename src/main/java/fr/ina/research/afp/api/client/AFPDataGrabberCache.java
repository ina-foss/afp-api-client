package fr.ina.research.afp.api.client;

import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public abstract class AFPDataGrabberCache {
	private static class NoCache extends AFPDataGrabberCache {

		@Override
		public boolean isInCache(String fullUno) {
			return false;
		}

		@Override
		public void putInCache(String fullUno) {
		}
	}

	private static class SimpleCache extends AFPDataGrabberCache {
		private Set<String> c;

		public SimpleCache() {
			super();
			c = new HashSet<>();
		}

		@Override
		public boolean isInCache(String fullUno) {
			return c.contains(fullUno);
		}

		@Override
		public void putInCache(String fullUno) {
			c.add(fullUno);
		}
	}

	private static class RealCache extends AFPDataGrabberCache {
		private Cache ehcache;

		public RealCache() {
			super();
			CacheManager manager = null;
			synchronized (RealCache.class) {
				manager = CacheManager.getInstance();
				if (!manager.cacheExists("AFPDataGrabberCache")) {
					Cache cache = new Cache(new CacheConfiguration("AFPDataGrabberCache", 50000).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
							.eternal(true).timeToLiveSeconds(0).timeToIdleSeconds(0).persistence(new PersistenceConfiguration().strategy(Strategy.NONE)));
					manager.addCache(cache);
				}
			}
			ehcache = manager.getCache("AFPDataGrabberCache");
		}

		@Override
		public boolean isInCache(String fullUno) {
			return ehcache.isKeyInCache(fullUno);
		}

		@Override
		public void putInCache(String fullUno) {
			ehcache.put(new Element(fullUno, fullUno));
		}

	}

	public static AFPDataGrabberCache noCache() {
		return new NoCache();
	}

	public static AFPDataGrabberCache realCache() {
		return new RealCache();
	}

	public static AFPDataGrabberCache simpleCache() {
		return new SimpleCache();
	}

	public abstract boolean isInCache(String fullUno);

	public abstract void putInCache(String fullUno);
}
