package com.kairos.springboot.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.kairos.springboot.app.commons.users.entity.User;
import com.kairos.springboot.app.oauth.service.IUserService;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

	@Autowired
	private IUserService userService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<String, Object>();
		
		
		User user = userService.findByUsername(authentication.getName());
		info.put("name", user.getName());
		info.put("surname", user.getSurname());
		info.put("email", user.getEmail());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
