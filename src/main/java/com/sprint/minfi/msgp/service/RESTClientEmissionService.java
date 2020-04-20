package com.sprint.minfi.msgp.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.domain.enumeration.Statut;
import com.sprint.minfi.msgp.service.dto.EmissionDTO;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> historiserEmission(@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);
	
	@PostMapping("/api/updateEmission/{idEmis}/{status}")
	public void updateEmission(@PathVariable ("idEmis") Long idEmis, @PathVariable ("status") Statut status);
	
//	@PostMapping("/api/emissions")
//	public ResponseEntity<EmissionDTO> createEmission(@RequestBody EmissionDTO emissionDTO);
	
	@PostMapping("/api/emissions")
	public void createEmission(@RequestBody EmissionDTO emissionDTO);
	
	@GetMapping("/api/emission-temps/{id}")
	public Map<String, String> findRefEmission(@PathVariable ("id") Long id);
}
