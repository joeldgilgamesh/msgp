package com.sprintpay.minfi.msgp.service;

import java.util.List;
import java.util.Map;

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

@AuthorizedFeignClient(name = "spminfimsged")
@Service
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
		@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
		public ResponseEntity<String> historiserEmission(@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);

		@PostMapping("/api/updateEmission/{idEmis}/{status}")
		public ResponseEntity<RetPaiFiscalis[]> updateEmission(@PathVariable ("idEmis") Long idEmis, @PathVariable ("status") Statut status);

		@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
		public ResponseEntity<String> createEmissionHistorique(@RequestBody EmissionHistoriqueDTO historiqueEmissionDTO,
										@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);

		@PostMapping("/api/emissions")
		public EmissionDTO createEmission(@RequestBody EmissionDTO emissionDTO);

		@GetMapping("/api/emission-temp/{id}")
		public Map<String, String> findRefEmission(@PathVariable ("id") Long id);

		@GetMapping("/api/getEmissionById/{id}")
	    public EmissionDTO getEmission(@PathVariable ("id") Long id);

		@GetMapping("/api/emissionsContri/{niu}")
		public List<String> getEmissionsContri(@PathVariable ("niu") String niu);
}
