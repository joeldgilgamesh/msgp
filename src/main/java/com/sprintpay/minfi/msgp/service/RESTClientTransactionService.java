package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimstransaction")
@Service
public interface RESTClientTransactionService {

	@PostMapping("/api/payment/{provider}")
	Map<String, String> getTransaction(@PathVariable ("provider") String provider, @RequestBody Map<String, String> request);

	@GetMapping("/api/payment/{provider}/notification/status")
	String getAlltransaction(@PathVariable ("provider") String provider);

}
