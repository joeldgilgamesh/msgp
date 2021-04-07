package com.sprintpay.minfi.msgp.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.EmissionDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.utils.RetPaiFiscalis;

import feign.hystrix.FallbackFactory;

@Component
public class MsgedFallback implements RESTClientEmissionService{


	@Override
	public ResponseEntity<String> historiserEmission(String status, Long idEmis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<RetPaiFiscalis[]> updateEmission(Long idEmis, Statut status, PaymentDTO payment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> createEmissionHistorique(EmissionHistoriqueDTO historiqueEmissionDTO,
			String status, Long idEmis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmissionDTO createEmission(EmissionDTO emissionDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> findRefEmission(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmissionDTO getEmission(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getEmissionsContri(String niu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllEmissionTemps() {
		/*
		 * // TODO Auto-generated method stub HashMap<String, Object> map = new
		 * HashMap<String, Object>(); map.put("result", "forbidden"); List<Map<String,
		 * Object>> list = new ArrayList<>(); list.add(map);
		 */
		return null;
	}
	
	@Override
	public boolean checkEmission(String niu, String refEmis) {
		// TODO Auto-generated method stub
		return null != null;
	}


}