//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.laladev.moneyjinn.core.mapper.AbstractMapperSupport;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.inject.Inject;

public abstract class AbstractService extends AbstractMapperSupport {
	@Inject
	private CacheManager cacheManager;
	@Inject
	private ApplicationEventPublisher applicationEventPublisher;

	static final LocalDate MAX_DATE = LocalDate.parse("2999-12-31");

	protected String getCombinedCacheName(final Object... cacheNameParts) {
		return String.join("#", Arrays.toString(cacheNameParts));
	}

	protected void evictFromCache(final String cacheName, final Object key) {
		final var cache = this.cacheManager.getCache(cacheName);
		if (cache != null)
			cache.evict(key);
	}

	protected void clearCache(final String cacheName) {
		final var cache = this.cacheManager.getCache(cacheName);
		if (cache != null)
			cache.clear();
	}

	protected <T> T getFromCacheOrExecute(final String cacheName, final Object key, final Supplier<T> supplier,
			final Class<T> clazz) {
		final Cache cache = this.cacheManager.getCache(cacheName);

		final T cacheValue = cache.get(key, clazz);
		if (cacheValue != null) {
			return cacheValue;
		}

		final T value = supplier.get();
		cache.put(key, value);
		return value;
	}

	protected <T> List<T> getListFromCacheOrExecute(final String cacheName, final Object key,
			final Supplier<List<T>> supplier, final Class<T> clazz) {
		final Cache cache = this.cacheManager.getCache(cacheName);

		@SuppressWarnings("unchecked")
		final List<T> cacheValue = cache.get(key, List.class);
		if (cacheValue != null) {
			return cacheValue;
		}

		final List<T> value = supplier.get();
		cache.put(key, value);
		return value;
	}

	protected void publishEvent(final ApplicationEvent event) {
		this.applicationEventPublisher.publishEvent(event);
	}
}
