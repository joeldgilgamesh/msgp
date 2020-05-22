package com.sprintpay.minfi.msgp.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsrnf")
public interface RESTClientRNFService {

	@GetMapping("/api/recettes-services/{id}")
	Object getRecettesService(@PathVariable ("id") Long id);
}
