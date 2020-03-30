package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsxxxx")
public interface RESTClientQuittanceService {
	
	//@PostMapping("/api/justificatif-paiements")
	//public ResponseEntity<JustificatifPaiementDTO> createJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO)
}
