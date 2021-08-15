package com.putchar.jwtgenvalidate.model;

import java.io.Serializable;

public class JwtResponse implements Serializable
{
	private static final long serialVersionUID = 7025533417392065214L;
	private final String jwttoken;
	
	public JwtResponse(String jwttoken) 
	{
		this.jwttoken = jwttoken;
	}

	public String getJwttoken() 
	{
		return jwttoken;
	}
	
}
