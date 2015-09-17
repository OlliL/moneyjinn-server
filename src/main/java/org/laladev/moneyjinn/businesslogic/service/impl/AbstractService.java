package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.mapper.AbstractMapperSupport;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public abstract class AbstractService extends AbstractMapperSupport {
	@Inject
	private CacheManager cacheManager;

	protected Cache getCache(final String... cacheNameParts) {
		final String cacheName = String.join("#", cacheNameParts);
		if (cacheName != null) {
			return this.cacheManager.getCache(cacheName);
		}
		return null;
	}

}
