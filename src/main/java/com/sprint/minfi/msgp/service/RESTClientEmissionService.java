package com.sprint.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.domain.enumeration.Statut;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> historiserEmission(@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);
	
	@PutMapping("/api/updateEmission/{idEmis}/{status}")
	public void updateEmission(@PathVariable ("idEmis") Long idEmis, @PathVariable ("status") Statut status);
}
