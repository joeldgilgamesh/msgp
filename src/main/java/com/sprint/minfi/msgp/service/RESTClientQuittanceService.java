package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsxxxx")
public interface RESTClientQuittanceService {

//	@PostMapping("/xxx/xxx")
//	public QuittanceDTO getQuittance(PaymentDTO paymentDTO);
}
