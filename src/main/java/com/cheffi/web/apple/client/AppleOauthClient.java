package com.cheffi.web.apple.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.cheffi.common.config.fegin.OpenFeignConfig;
import com.cheffi.web.kakao.dto.OidcPublicKeysResponse;

@FeignClient(
	name = "AppleOauthClient",
	url = "https://appleid.apple.com",
	configuration = OpenFeignConfig.class)
public interface AppleOauthClient {
	@Cacheable(cacheNames = "AppleKid", cacheManager = "oidcCacheManager")
	@GetMapping("/auth/oauth2/v2/keys")
	OidcPublicKeysResponse getAppleOidcOpenKeys();
}
