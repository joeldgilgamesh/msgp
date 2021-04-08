package com.sprintpay.minfi.msgp.service;

import java.util.List;
import java.util.Map;

import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.EmissionDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprintpay.minfi.msgp.utils.RetPaiFiscalis;

@AuthorizedFeignClient(name = "spminfimsged", fallback=MsgedFallback.class)
@Service
public interface RESTClientEmissionService {

		@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
		ResponseEntity<String> historiserEmission(@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);

		@PostMapping("/api/updateEmission/{idEmis}/{status}")
		ResponseEntity<RetPaiFiscalis[]> updateEmission(@PathVariable ("idEmis") Long idEmis, @PathVariable ("status") Statut status, @RequestBody PaymentDTO payment);

		@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
		ResponseEntity<String> createEmissionHistorique(@RequestBody EmissionHistoriqueDTO historiqueEmissionDTO,
										@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);

		@PostMapping("/api/emissions")
		EmissionDTO createEmission(@RequestBody EmissionDTO emissionDTO);

		@GetMapping("/api/emission-temp/{id}")
		Map<String, String> findRefEmission(@PathVariable ("id") Long id);

		@GetMapping("/api/getEmissionById/{id}")
	    EmissionDTO getEmission(@PathVariable ("id") Long id);

		@GetMapping("/api/emissionsContri/{niu}")
		List<String> getEmissionsContri(@PathVariable ("niu") String niu);
		
		@GetMapping("/api/emission-temps/all")
		List<Map<String, Object>> getAllEmissionTemps();
		
		@GetMapping("/api/emission/check/{niu}/{refEmis}")
		boolean checkEmission(@PathVariable ("niu") String niu, @PathVariable ("refEmis") String refEmis);
		
		@GetMapping("/api/emission/reconciled")
		List<String> notifyReconciledEmission(Map<String,Object> datas);
}
