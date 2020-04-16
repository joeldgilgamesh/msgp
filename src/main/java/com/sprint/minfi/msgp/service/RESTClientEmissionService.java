package com.sprint.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.domain.enumeration.Statut;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> historiserEmission(@PathVariable ("status") Statut status, @PathVariable ("idEmis") Long idEmis);
	
	@PutMapping("/api/updateEmission/{refEmi}/{status}")
	public void updateEmission(@PathVariable ("refEmi") String refEmi, @PathVariable ("status") String status);
}
