package com.kairos.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.kairos.springboot.app.commons.users.entity.User;

@FeignClient(name="servicio-usuarios")
public interface UserFeignClient {

	@GetMapping("/users/search/buscar-username")
	public User findByUsername(@RequestParam String username);
	
	@PutMapping("/users/{id}")
	public User update(@RequestBody User user, @PathVariable Long id);
	//@GetMapping("/users/buscar-username")
}
