package com.sprintpay.minfi.msgp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

@AuthorizedFeignClient(name = "spminfimsrnf")
@Service
public interface RESTClientRNFService {

	@GetMapping("/api/recettes-services/{id}")
	Object getRecettesService(@PathVariable ("id") Long id);

    @PutMapping("/api/payerRecette/{id}/{idPaiement}")
    public ResponseEntity<Object> payerRecettesService(@PathVariable("id")  Long id, @PathVariable("idPaiement") Long idPaiement);

    @GetMapping("/api/recettes-services/resume/{id}")
    Map<String, Object> getResumeRecettesService(@PathVariable("id") Long id);
}
