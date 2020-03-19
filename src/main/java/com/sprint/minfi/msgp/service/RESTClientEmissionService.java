package com.sprint.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> historiserEmission(@PathVariable String status, @PathVariable Long idEmis);
	
	@PutMapping("/api/updateEmission/{refEmi}/{status}")
	public ResponseEntity<String> updateEmission(@PathVariable ("refEmi") String refEmi, @PathVariable ("status") String status);
}
