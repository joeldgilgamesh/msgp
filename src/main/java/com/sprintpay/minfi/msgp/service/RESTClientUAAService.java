package com.sprintpay.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsuaa")
public interface RESTClientUAAService {

	@GetMapping("/api/users/niu/{niu}")
	ResponseEntity<Object> getNiuContribuablesEnregistres(@PathVariable ("niu") String niu);
	
}
