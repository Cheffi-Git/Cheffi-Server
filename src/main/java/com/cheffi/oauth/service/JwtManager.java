package com.cheffi.oauth.service;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.AuthenticationException;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.oauth.dto.IdTokenAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtManager {

	private static final String KID = "kid";

	public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
		return (String)getUnsignedTokenClaims(token, iss, aud).getHeader().get(KID);
	}

	/**
	 * aud, iss, exp 검증
	 */
	public Jwt<Header, Claims> getUnsignedTokenClaims(String jwt, String iss, String aud) {
		try {
			return Jwts.parserBuilder()
				.requireAudience(aud)
				.requireIssuer(iss)
				.build()
				.parseClaimsJwt(getUnsignedToken(jwt));
		} catch (InvalidClaimException e) {
			throw new AuthenticationException(ErrorCode.INVALID_PAYLOAD, e);
		} catch (ExpiredJwtException e) {
			throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED, e);
		} catch (UnsupportedJwtException e) {
			throw new AuthenticationException(ErrorCode.NOT_SUPPORTED_JWT, e);
		} catch (MalformedJwtException e) {
			throw new AuthenticationException(ErrorCode.MALFORMED_JWT, e);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private String getUnsignedToken(String jwt) {
		String[] splitToken = jwt.split("\\.");
		if (splitToken.length != 3)
			throw new AuthenticationException(ErrorCode.MALFORMED_JWT);
		return splitToken[0] + "." + splitToken[1] + ".";
	}

	public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(getRSAPublicKey(modulus, exponent))
				.build()
				.parseClaimsJws(token);
		} catch (SignatureException e) {
			throw new AuthenticationException(ErrorCode.JWT_VERIFY_FAILED, e);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public IdTokenAttributes getOIDCTokenBody(String token, String modulus, String exponent) {
		Claims body = getOIDCTokenJws(token, modulus, exponent).getBody();
		return new IdTokenAttributes(body);
	}

	private Key getRSAPublicKey(String modulus, String exponent)
		throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
		byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
		BigInteger n = new BigInteger(1, decodeN);
		BigInteger e = new BigInteger(1, decodeE);

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
		return keyFactory.generatePublic(keySpec);
	}

}
