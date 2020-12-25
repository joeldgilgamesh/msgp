package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsreport")
@Service
public interface RESTClientReportService {
	
	@PostMapping("/repartitionByOrganizationByPeriod")
	Map<String, Object> repartitionByOrganisation(@RequestBody Map<String, String> object);

}
