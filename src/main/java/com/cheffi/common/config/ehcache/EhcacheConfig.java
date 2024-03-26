package com.cheffi.common.config.ehcache;

import java.time.Duration;
import java.util.List;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class EhcacheConfig {

	@Primary
	@Bean
	public CacheManager localCacheManager() {
		return new JCacheCacheManager(ehCacheManager());
	}

	private javax.cache.CacheManager ehCacheManager() {
		CachingProvider provider = Caching.getCachingProvider();
		javax.cache.CacheManager cacheManager = provider.getCacheManager();

		javax.cache.configuration.Configuration<SimpleKey, List> tagTypeCacheConfiguration =
			Eh107Configuration.fromEhcacheCacheConfiguration(config());
		cacheManager.createCache("Tag", tagTypeCacheConfiguration);

		return cacheManager;
	}

	private CacheConfigurationBuilder<SimpleKey, List> config() {
		return CacheConfigurationBuilder.newCacheConfigurationBuilder(
				SimpleKey.class,
				List.class,
				ResourcePoolsBuilder
					.heap(100)
					.offheap(10, MemoryUnit.MB))
			.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofDays(1)));
	}
}
