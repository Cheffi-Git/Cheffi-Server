package com.cheffi.oauth.service.ios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cheffi.oauth.dto.IdTokenAttributes;
import com.cheffi.oauth.model.OAuthAttributes;
import com.cheffi.oauth.service.OidcLoginApiService;
import com.cheffi.oauth.service.OidcService;
import com.cheffi.user.constant.UserType;
import com.cheffi.web.apple.client.AppleOauthClient;

@Service("apple")
public class AppleApiService implements OidcLoginApiService {

	private final OidcService oidcService;
	private final AppleOauthClient appleOauthClient;
	private final String iss;
	private final String aud;

	public AppleApiService(
		OidcService oidcService,
		AppleOauthClient appleOauthClient,
		@Value("${apple.oidc.iss}") String iss,
		@Value("${apple.oidc.aud}") String aud
	) {
		this.oidcService = oidcService;
		this.appleOauthClient = appleOauthClient;
		this.iss = iss;
		this.aud = aud;
	}

	@Override
	public IdTokenAttributes verify(String token, String platform) {

		return oidcService.getPayloadFromIdToken(token, iss, aud,
			appleOauthClient.getAppleOidcOpenKeys());
	}

	@Override
	public OAuthAttributes getUserInfo(IdTokenAttributes attributes) {
		return new OAuthAttributes(
			attributes.getEmail(),
			attributes.getName("기본이름"),
			attributes.getProfile("기본프로필"),
			UserType.APPLE
		);
	}
}
