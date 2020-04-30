package com.sprintpay.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;

@AuthorizedFeignClient(name = "spminfimsgq")
public interface RESTClientQuittanceService {
	
	@PostMapping("/api/justificatif-paiements")
	public ResponseEntity<JustificatifPaiementDTO> createJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO);

	@PostMapping("/api/justificatif-paiements/genererRecuOuQuittance")
	public ResponseEntity<byte []> genererRecuOuQuittance(@RequestBody  JustificatifPaiementDTO justificatifPaiementDTO);
}
