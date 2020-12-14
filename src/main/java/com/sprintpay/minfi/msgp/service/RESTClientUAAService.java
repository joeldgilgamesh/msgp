package com.sprintpay.minfi.msgp.service;

import com.sprintpay.minfi.msgp.service.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

import java.util.Optional;

@AuthorizedFeignClient(name = "spminfimsuaa")
@Service
public interface RESTClientUAAService {

	@GetMapping("/api/users/niu/{niu}")
	Optional<UserDTO> getNiuContribuablesEnregistres(@PathVariable ("niu") String niu);

    @GetMapping("/api/users/search/{login}")
    Optional<UserDTO> searchUser(@PathVariable("login") String login);

}
