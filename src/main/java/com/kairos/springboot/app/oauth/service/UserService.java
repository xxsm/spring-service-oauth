package com.kairos.springboot.app.oauth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kairos.springboot.app.commons.users.entity.User;
import com.kairos.springboot.app.oauth.clients.UserFeignClient;

import feign.FeignException;

@Service
public class UserService implements UserDetailsService, IUserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserFeignClient client;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = client.findByUsername(username);

			List<GrantedAuthority> authorities = user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getNombre()))
					.peek(authority -> log.info("Role " + authority.getAuthority())).collect(Collectors.toList());

			log.info("Usuario autenticado: " + username);

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					user.getEnabled(), true, true, true, authorities);
		} catch (FeignException e) {
			log.error("Error en el login, no existe el usuario '" + username + "' en el sistema");
			throw new UsernameNotFoundException(
					"Error en el login, no existe el usuario '" + username + "' en el sistema");
		}
	}

	@Override
	public User findByUsername(String username) {
		return client.findByUsername(username);
	}

	@Override
	public User update(User user, Long id) {
		return client.update(user, id);
	}
}
