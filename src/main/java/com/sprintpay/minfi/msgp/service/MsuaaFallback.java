package com.sprintpay.minfi.msgp.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.sprintpay.minfi.msgp.service.dto.UserDTO;

import feign.hystrix.FallbackFactory;

@Component
public class MsuaaFallback implements RESTClientUAAService{
	
	@Override
	public Optional<UserDTO> getNiuContribuablesEnregistres(String niu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<UserDTO> searchUser(String login) {
		// TODO Auto-generated method stub
		return null;
	}

}