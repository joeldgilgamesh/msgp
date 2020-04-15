package com.sprint.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.service.dto.JustificatifPaiementDTO;

@AuthorizedFeignClient(name = "spminfimsxxxx")
public interface RESTClientQuittanceService {
	
	@PostMapping("/api/justificatif-paiements")
	public ResponseEntity<JustificatifPaiementDTO> createJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO);
}
