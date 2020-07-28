package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sprintpay.minfi.msgp.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "spminfimsorg")
@Service
public interface RESTClientOrganisationService {

	@GetMapping("/api/organisationsByLibelleCourt/{libelleCourt}")
    Map<String, Object> findOrganisationByLibelleCourt(@PathVariable("libelleCourt") String libelleCourt);

    @GetMapping("/api/resumeOrganisation/{id}")
    Map<String, Object> findOrganisationById(@PathVariable("id") Long id);

}
