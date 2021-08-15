package com.putchar.jwtgenvalidate.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.putchar.jwtgenvalidate.service.JwtuserDetailsService;
import com.putchar.jwtgenvalidate.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter 
{
	@Autowired
	private JwtuserDetailsService jwtuserDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		final String requestTokenHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwtToken = null;
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) 
		{
			jwtToken = requestTokenHeader.substring(7);
			
			try 
			{
				username = jwtTokenUtil.getusernameFromToken(jwtToken);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("Unable to get JWT token");
			} 
			catch(ExpiredJwtException e)
			{
				System.out.println("JWT token has expired");
			}
		}
		else 
		{
			logger.warn("JWT token does not begin with Bearer String");
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) 
		{
			UserDetails userDetails = this.jwtuserDetailsService.loadUserByUsername(username);
			
			if(jwtTokenUtil.validateToken(jwtToken, userDetails)) 
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, 
						null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.
				setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
