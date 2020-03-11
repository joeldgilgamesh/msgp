package com.sprint.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	@PostMapping("/api/historiserEmissions")
	public ResponseEntity<HistoriquePaymentDTO> historiserEmission(Long emissionId);
}
