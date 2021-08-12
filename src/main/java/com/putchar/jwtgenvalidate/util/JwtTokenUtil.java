package com.putchar.jwtgenvalidate.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final long JWT_TOKEN_VALIDITY = 5*60*60;
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	public String getusernameFromToken(String token) 
	{
		return getClaimsFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) 
	{
		return getClaimsFromToken(token, Claims::getExpiration);
	}
	
	private boolean isTokenExpired(String token) 
	{
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) 
	{
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//get JWT body values by the secret key
	private Claims getAllClaimsFromToken(String token)
	{
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public String generateToken(UserDetails userdetails) 
	{
		Map<String, Object> claims = new HashMap<String, Object>();
		return doGenerateToken(claims, userdetails.getUsername());
	}
	
	private String doGenerateToken(Map<String, Object> claims, String subject) 
	{
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) 
	{
		final String  username = getusernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
