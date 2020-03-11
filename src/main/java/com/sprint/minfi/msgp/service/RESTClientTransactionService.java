package com.sprint.minfi.msgp.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.service.dto.TransactionDTO;

@AuthorizedFeignClient(name = "spminfimstransaction")
public interface RESTClientTransactionService {

	@PostMapping("/api/payment/{provider}")
	public TransactionDTO getTransaction(JSONObject requestTransaction);
	
	@GetMapping("/xxxx/xxxx")
	public Page<TransactionDTO> getAllTransaction();
}
