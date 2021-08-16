package com.putchar.jwtgenvalidate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.putchar.jwtgenvalidate.model.JwtRequest;
import com.putchar.jwtgenvalidate.model.JwtResponse;
import com.putchar.jwtgenvalidate.model.UserRegistrationRequest;
import com.putchar.jwtgenvalidate.service.JwtuserDetailsService;
import com.putchar.jwtgenvalidate.util.JwtTokenUtil;

@RestController
@CrossOrigin
public class JwtAuthenticationController 
{ 
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtuserDetailsService userDetailsService;
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception 
	{
		System.out.println("createAuthenticationToken");
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@RequestMapping(value = "/register", method=RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserRegistrationRequest request) throws Exception 
	{
		System.out.println("inside saveUser()");
		return ResponseEntity.ok(userDetailsService.save(request));
	}
	
	private void authenticate(String username, String password) throws Exception 
	{
		
		try 
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}
		catch (DisabledException e) 
		{
			throw new Exception("USER DISABLED ",e);
		}
	}
}
