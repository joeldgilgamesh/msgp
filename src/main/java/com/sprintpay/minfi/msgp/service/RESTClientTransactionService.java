package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimstransaction")
@Service
public interface RESTClientTransactionService {

	@PostMapping("/api/payment/{provider}")
	Map<String, String> getTransaction(@PathVariable ("provider") String provider, @RequestBody Map<String, String> request);

	@GetMapping("/api/payment/{provider}/notification/status")
	String getAlltransaction(@PathVariable ("provider") String provider);
	
	@GetMapping("/api/payment/afrilandcmr/confirmpayment/{otp}/{trxid}")
	Map<String,String> confirmPayment(@PathVariable("otp") String otp, @PathVariable("trxid") String trxid) throws Exception;
	
	@PostMapping("/api/payment/incash/{provider}")
	Map<String, String> processPaymentInCash(@PathVariable("provider") String provider, 
			@RequestBody Map<String, String> request, @RequestParam ("token") String token);
	
	@PostMapping("/api/payment/confirmation/{provider}")
	Map<String, String> confirmationPaymentUBA(@PathVariable("provider") String provider, 
			@RequestBody Map<String, String> request, @RequestParam ("token") String token);
	
	@GetMapping("/api/transactions/{ref}")
	Map<String, String> getTransactionRefOrOrderId(@PathVariable("ref") String ref);

}
