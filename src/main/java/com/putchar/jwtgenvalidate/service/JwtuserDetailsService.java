package com.putchar.jwtgenvalidate.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.putchar.jwtgenvalidate.model.LoginUser;
import com.putchar.jwtgenvalidate.model.UserRegistrationRequest;
import com.putchar.jwtgenvalidate.repository.UserRepository;

@Service
public class JwtuserDetailsService implements UserDetailsService 
{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		System.out.println("loadUserByUsername : "+username);
		LoginUser user = userRepository.findByUsername(username);
		
		if(user != null) 
		{
			return new User(user.getUsername(), user.getPassword(), 
					new ArrayList<>());
		}
		else 
		{
			throw new UsernameNotFoundException("User not found with username : "+username);
		}
			
	}
	
	
	public LoginUser save(UserRegistrationRequest req) 
	{
		LoginUser user = new LoginUser();
		user.setUsername(req.getUsername());
		user.setPassword(bcryptEncoder.encode(req.getPassword()));
		return userRepository.save(user);
	}

}
