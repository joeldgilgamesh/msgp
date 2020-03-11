package com.sprint.minfi.msgp.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.service.dto.TransactionDTO;

@AuthorizedFeignClient(name = "spminfimstransaction")
public interface RESTClientTransactionService {

	@PostMapping("/api/payment/{provider}")
	public TransactionDTO getTransaction(@PathVariable ("provider") String provider, @RequestBody Map<String, String> request);
	
	@GetMapping("/api/payment/{provider}/notification/status")
	public String getAlltransaction(@PathVariable ("provider") String provider);
	
	@GetMapping("/xxxx/xxxx")
	public Page<TransactionDTO> getAllTransaction();
}
