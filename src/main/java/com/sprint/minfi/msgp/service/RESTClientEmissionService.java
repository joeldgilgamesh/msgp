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
import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;

@AuthorizedFeignClient(name = "spminfimsged")
public interface RESTClientEmissionService {

	//cette demande doit etre envoyé sans Id
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> historiserEmission(@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);
	
	@PostMapping("/api/updateEmission/{idEmis}/{status}")
	public void updateEmission(@PathVariable ("idEmis") Long idEmis, @PathVariable ("status") Statut status);
	
	@PostMapping("/api/historiserEmissions/{status}/{idEmis}")
	public ResponseEntity<String> createEmissionHistorique(@RequestBody EmissionHistoriqueDTO historiqueEmissionDTO,
									@PathVariable ("status") String status, @PathVariable ("idEmis") Long idEmis);
	
	@PostMapping("/api/emissions")
	public EmissionDTO createEmission(@RequestBody EmissionDTO emissionDTO);
	
	@GetMapping("/api/emission-temp/{id}")
	public Map<String, String> findRefEmission(@PathVariable ("id") Long id);
	
	@GetMapping("/api/emissions/{id}")
    public EmissionDTO getEmission(@PathVariable ("id") Long id);
}
