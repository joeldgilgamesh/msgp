package com.sprintpay.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;

import java.util.List;

@AuthorizedFeignClient(name = "spminfimsgq")
@Service
public interface RESTClientQuittanceService {

	@PostMapping("/api/justificatif-paiements")
    @Async
	public ResponseEntity<JustificatifPaiementDTO> createJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO);

    @PostMapping("/api/justificatif-paiements/list")
    @Async
    public void createManyJustificatifPaiement(@RequestBody List<JustificatifPaiementDTO> justificatifPaiementDTOs);

    @PostMapping("/api/justificatif-paiements/genererRecuOuQuittance")
    public ResponseEntity<byte []> genererRecuOuQuittance(@RequestBody  JustificatifPaiementDTO justificatifPaiementDTO);
}
