package com.kairos.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.kairos.springboot.app.commons.users.entity.User;
import com.kairos.springboot.app.oauth.service.IUserService;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	@Autowired
	private IUserService userService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String mensaje = "Success login: " + userDetails.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		
		User user = userService.findByUsername(authentication.getName());
		if (user.getIntentos() != null && user.getIntentos() > 0) {
			user.setIntentos(0);
			userService.update(user, user.getId());
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje = "Error en el login: " + exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);

		try {
			User user = userService.findByUsername(authentication.getName());
			if (user.getIntentos() == null) {
				user.setIntentos(0);
			}

			log.info("Intentos actual es de: " + user.getIntentos());
			user.setIntentos(user.getIntentos() + 1);
			log.info("Intentos después es de: " + user.getIntentos());

			if (user.getIntentos() >= 5) {
				log.error(String.format("El usuario %s des-habilitado por máximos intentos", user.getUsername()));
				user.setEnabled(false);
			}

			userService.update(user, user.getId());

		} catch (FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}
	}

}
