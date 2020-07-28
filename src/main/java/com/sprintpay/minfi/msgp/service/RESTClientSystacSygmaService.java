package com.sprintpay.minfi.msgp.service;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;
import com.sprintpay.minfi.msgp.service.dto.TransactionSSDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@AuthorizedFeignClient(name = "spminfimsss")
@Service
public interface RESTClientSystacSygmaService {
    @GetMapping("/api/transactions/find/{numeroVersment}")
    ResponseEntity<TransactionSSDTO> searchTransaction(@PathVariable("numeroVersment") String numeroVersment, @RequestParam("token") String token);
}
