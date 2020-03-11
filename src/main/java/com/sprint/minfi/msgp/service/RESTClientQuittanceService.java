package com.sprint.minfi.msgp.service;

import org.springframework.web.bind.annotation.PostMapping;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;

@AuthorizedFeignClient(name = "spminfimsxxxx")
public interface RESTClientQuittanceService {

//	@PostMapping("/xxx/xxx")
//	public QuittanceDTO getQuittance(PaymentDTO paymentDTO);
}
